import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:flutter/material.dart';

class VoteButton extends StatelessWidget {
  final ValueNotifier<VoteDirection> likes;
  final void Function(VoteDirection)? onChange;
  final VoteDirection action;
  final IconData iconNeutral;
  final IconData iconActive;
  final Color colorActive;
  const VoteButton({
    super.key,
    required this.likes,
    required this.action,
    required this.iconNeutral,
    required this.iconActive,
    required this.colorActive,
    this.onChange,
  });

  const VoteButton.like({
    super.key,
    required this.likes,
    required this.colorActive,
    this.onChange,
  })  : iconNeutral = Icons.thumb_up_outlined,
        iconActive = Icons.thumb_up,
        action = VoteDirection.up;

  const VoteButton.dislike({
    super.key,
    required this.likes,
    required this.colorActive,
    this.onChange,
  })  : iconNeutral = Icons.thumb_down_outlined,
        iconActive = Icons.thumb_down,
        action = VoteDirection.down;

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder(
      valueListenable: likes,
      builder: (context, value, child) {
        final active = value == action;
        return IconButton(
          icon: Icon(
            active ? iconActive : iconNeutral,
            color: active ? colorActive : Colors.grey,
          ),
          onPressed: () {
            if (active) {
              onChange?.call(VoteDirection.neutral);
              likes.value = VoteDirection.neutral;
            } else {
              onChange?.call(action);
              likes.value = action;
            }
          },
        );
      },
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

  @override
  void initState() {
    super.initState();
    active = widget.initialValue;
  }

  @override
  Widget build(BuildContext context) {
    return IconButton(
      icon: Icon(
        active ? Icons.bookmark : Icons.bookmark_outline,
        color: active ? Colors.yellow : Colors.grey,
      ),
      onPressed: () {
        setState(() {
          active = !active;
        });
        widget.onChange?.call(active);
      },
    );
  }
}
