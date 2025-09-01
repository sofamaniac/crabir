import 'package:crabir/bool_to_vote.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:flutter/material.dart';

class VoteButton extends StatefulWidget {
  final bool? initialValue;
  final void Function(VoteDirection)? onChange;
  final VoteDirection action;
  final IconData iconNeutral;
  final IconData iconActive;
  final Color colorActive;
  const VoteButton({
    super.key,
    required this.initialValue,
    required this.action,
    required this.iconNeutral,
    required this.iconActive,
    required this.colorActive,
    this.onChange,
  });
  const VoteButton.like({
    super.key,
    required this.initialValue,
    required this.colorActive,
    this.onChange,
  })  : iconNeutral = Icons.thumb_up_outlined,
        iconActive = Icons.thumb_up,
        action = VoteDirection.up;
  const VoteButton.dislike({
    super.key,
    required this.initialValue,
    required this.colorActive,
    this.onChange,
  })  : iconNeutral = Icons.thumb_down_outlined,
        iconActive = Icons.thumb_down,
        action = VoteDirection.down;
  @override
  State<VoteButton> createState() => _VoteButtonState();
}

class _VoteButtonState extends State<VoteButton> {
  late bool active;

  @override
  void initState() {
    super.initState();
    active = widget.initialValue.toVoteDirection() == widget.action;
  }

  @override
  Widget build(BuildContext context) {
    return IconButton(
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
        setState(() {
          active = !active;
        });
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
