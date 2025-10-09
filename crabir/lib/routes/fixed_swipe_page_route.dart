import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

CustomTransitionPage<T> fixedSwipePage<T>({
  required Widget child,
  double dragThreshold = 0.5,
  Duration transitionDuration = const Duration(milliseconds: 300),
}) {
  final dragController = ValueNotifier<double>(0.0);

  return CustomTransitionPage<T>(
    key: ValueKey(child.hashCode),
    child: child,
    transitionDuration: transitionDuration,
    transitionsBuilder: (context, animation, secondaryAnimation, child) {
      final width = MediaQuery.of(context).size.width;

      return GestureDetector(
        behavior: HitTestBehavior.translucent,
        onHorizontalDragUpdate: (details) {
          dragController.value += details.delta.dx / width;
        },
        onHorizontalDragEnd: (details) {
          final velocity = details.velocity.pixelsPerSecond.dx;
          final progress = dragController.value;

          if (progress > dragThreshold || velocity > 700) {
            // Complete pop
            dragController.value = 1.0;
            Navigator.of(context).pop();
          } else {
            // Animate back to 0
            dragController.value = 0.0;
          }
        },
        child: AnimatedBuilder(
          animation: Listenable.merge([animation, dragController]),
          builder: (_, __) {
            // Push animation + drag offset
            final pushOffset = (1 - animation.value) * width;
            final dragOffset = dragController.value * width;
            return Transform.translate(
              offset: Offset(pushOffset + dragOffset, 0),
              child: child,
            );
          },
        ),
      );
    },
  );
}

class FixedSwipePageRoute<T> extends PageRoute<T> {
  FixedSwipePageRoute({
    required this.builder,
    this.dragThreshold = 0.5,
    super.fullscreenDialog,
    super.settings,
  });

  final WidgetBuilder builder;

  /// fraction of screen width to trigger pop
  final double dragThreshold;

  late AnimationController _pushController;
  late AnimationController _dragController;

  @override
  Color? get barrierColor => null;
  @override
  bool get opaque => false;
  @override
  bool get barrierDismissible => false;
  @override
  String? get barrierLabel => null;
  @override
  bool get maintainState => true;
  @override
  Duration get transitionDuration => const Duration(milliseconds: 300);

  @override
  void install() {
    super.install();
    // _controller is provided by PageRoute internally; we just use it for vsync
    _pushController = AnimationController(
      vsync: navigator!.overlay!,
      duration: transitionDuration,
    )..forward(); // run the push animation

    _dragController = AnimationController(
      vsync: navigator!.overlay!,
      duration: transitionDuration,
    );
  }

  @override
  Widget buildPage(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation) {
    return builder(context);
  }

  @override
  Widget buildTransitions(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation, Widget child) {
    final width = MediaQuery.of(context).size.width;

    return GestureDetector(
      behavior: HitTestBehavior.translucent,
      onHorizontalDragUpdate: (details) {
        final dx = details.delta.dx;
        _dragController.value += dx / width;
      },
      onHorizontalDragEnd: (details) {
        final velocity = details.velocity.pixelsPerSecond.dx;
        final progress = _dragController.value;

        if (progress > dragThreshold || velocity > 700) {
          // Complete pop
          _dragController
              .animateTo(
                1.0,
                duration: transitionDuration,
                curve: Curves.easeOut,
              )
              .whenComplete(() => navigator?.pop());
        } else {
          // Cancel and animate back
          _dragController.animateBack(
            0.0,
            duration: transitionDuration,
            curve: Curves.easeOut,
          );
        }
      },
      child: AnimatedBuilder(
        animation: Listenable.merge([_pushController, _dragController]),
        builder: (_, __) {
          // Total offset = push animation + drag offset
          final offset = width * (1 - _pushController.value) +
              width * _dragController.value;
          return Transform.translate(offset: Offset(offset, 0), child: child);
        },
      ),
    );
  }
}
