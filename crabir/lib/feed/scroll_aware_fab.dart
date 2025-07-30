import 'package:crabir/feed/feed.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_speed_dial/flutter_speed_dial.dart';

class ScrollAwareFab extends StatefulWidget {
  final ScrollController scrollController;

  const ScrollAwareFab({super.key, required this.scrollController});

  @override
  State<ScrollAwareFab> createState() => _ScrollAwareFabState();
}

class _ScrollAwareFabState extends State<ScrollAwareFab> {
  bool _visible = false;
  double _lastOffset = 0.0;

  @override
  void initState() {
    super.initState();
    widget.scrollController.addListener(_onScroll);
  }

  @override
  void dispose() {
    widget.scrollController.removeListener(_onScroll);
    super.dispose();
  }

  void _onScroll() {
    final offset = widget.scrollController.offset;
    final isScrollingUp = offset < _lastOffset;
    final shouldShow = isScrollingUp;

    if (_visible != shouldShow) {
      setState(() {
        _visible = shouldShow;
      });
    }

    _lastOffset = offset;
  }

  @override
  Widget build(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    return AnimatedSlide(
      duration: const Duration(milliseconds: 200),
      offset: _visible ? Offset.zero : const Offset(0, 2),
      child: AnimatedOpacity(
        duration: const Duration(milliseconds: 200),
        opacity: _visible ? 1 : 0,
        child: SpeedDial(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12), // Adjust corner roundness
          ),
          icon: Icons.add,
          activeIcon: Icons.close,
          backgroundColor: theme.primaryColor,
          children: [
            SpeedDialChild(
              backgroundColor: theme.primaryColor,
              child: const Icon(Icons.clear_all),
              label: 'Clear read',
              onTap: () {},
            ),
            SpeedDialChild(
              backgroundColor: theme.primaryColor,
              child: const Icon(Icons.edit),
              label: 'Create Post',
              onTap: () {},
            ),
            SpeedDialChild(
              backgroundColor: theme.primaryColor,
              child: const Icon(Icons.keyboard_arrow_up),
              label: 'Scroll to Top',
              onTap: () {
                widget.scrollController.animateTo(
                  0,
                  duration: const Duration(milliseconds: 300),
                  curve: Curves.easeOut,
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
