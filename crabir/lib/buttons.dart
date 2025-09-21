import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:flutter/material.dart';

class VoteButton extends StatefulWidget {
  final VoteDirection likes;
  final void Function(VoteDirection)? onChange;
  final VoteDirection action;
  final IconData iconNeutral;
  final IconData iconActive;
  final Color colorActive;
  final double translation;
  const VoteButton({
    super.key,
    required this.likes,
    required this.action,
    required this.iconNeutral,
    required this.iconActive,
    required this.colorActive,
    this.onChange,
    this.translation = 0,
  });

  const VoteButton.like({
    super.key,
    required this.likes,
    required this.colorActive,
    this.onChange,
  })  : iconNeutral = Icons.thumb_up_outlined,
        iconActive = Icons.thumb_up,
        translation = -5,
        action = VoteDirection.up;

  const VoteButton.dislike({
    super.key,
    required this.likes,
    required this.colorActive,
    this.onChange,
  })  : iconNeutral = Icons.thumb_down_outlined,
        iconActive = Icons.thumb_down,
        translation = 5,
        action = VoteDirection.down;

  @override
  State<VoteButton> createState() => _VoteButtonState();
}

class _VoteButtonState extends State<VoteButton>
    with SingleTickerProviderStateMixin {
  double _currentPosition = 0;

  final int duration = 100;
  @override
  void didUpdateWidget(covariant VoteButton oldWidget) {
    super.didUpdateWidget(oldWidget);
    final likes = widget.likes;
    final oldLikes = oldWidget.likes;

    // Trigger animation only when liked changes from false → true
    if (likes != oldLikes) {
      if (likes == widget.action) {
        // Grow temporarily
        setState(() {
          _currentPosition = widget.translation;
        });

        // Shrink back after a short delay
        Future.delayed(Duration(milliseconds: duration), () {
          if (mounted) {
            setState(() {
              _currentPosition = 0;
            });
          }
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final active = widget.likes == widget.action;

    return AnimatedContainer(
      duration: Duration(milliseconds: duration),
      curve: Curves.elasticOut,
      transform: Matrix4.identity()..translate(0.0, _currentPosition),
      child: IconButton(
        icon: Icon(
          active ? widget.iconActive : widget.iconNeutral,
          color: active ? widget.colorActive : Colors.grey,
        ),
        onPressed: () {
          if (active) {
            widget.onChange?.call(VoteDirection.neutral);
          } else {
            widget.onChange?.call(widget.action);
          }
        },
      ),
    );
  }
}

class SaveButton extends StatefulWidget {
  final bool initialValue;
  final void Function(bool)? onChange;

  const SaveButton({
    super.key,
    required this.initialValue,
    this.onChange,
  });

  @override
  State<SaveButton> createState() => _SaveButtonState();
}

class _SaveButtonState extends State<SaveButton> {
  late bool active;
  double _scale = 1;
  static const int duration = 100;

  @override
  void initState() {
    super.initState();
    active = widget.initialValue;
  }

  @override
  Widget build(BuildContext context) {
    return TweenAnimationBuilder<double>(
      tween: Tween(begin: 1.0, end: _scale),
      duration: const Duration(milliseconds: duration),
      curve: Curves.easeOut,
      builder: (context, value, child) {
        return Transform.scale(
          scale: value,
          alignment: Alignment.center,
          child: child,
        );
      },
      child: IconButton(
        icon: Icon(
          active ? Icons.bookmark : Icons.bookmark_outline,
          color: active ? Colors.yellow : Colors.grey,
        ),
        onPressed: () {
          setState(() {
            active = !active;
            if (active) {
              _scale = 1.2;
            }
          });
          widget.onChange?.call(active);
          Future.delayed(const Duration(milliseconds: duration), () {
            if (mounted) {
              setState(() {
                _scale = 1;
              });
            }
          });
        },
      ),
    );
  }
}
