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
  late AnimationController _controller;
  late Animation<double> _animation;

  @override
  void initState() {
    super.initState();

    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 100),
    );

    _animation = TweenSequence([
      TweenSequenceItem(
        tween: Tween<double>(begin: 0, end: widget.translation)
            .chain(CurveTween(curve: Curves.bounceInOut)),
        weight: 50,
      ),
      TweenSequenceItem(
        tween: Tween<double>(begin: widget.translation, end: 0)
            .chain(CurveTween(curve: Curves.bounceInOut)),
        weight: 50,
      ),
    ]).animate(_controller);
  }

  final int duration = 100;

  @override
  Widget build(BuildContext context) {
    final active = widget.likes == widget.action;

    return AnimatedBuilder(
      animation: _animation,
      builder: (context, child) {
        return Transform.translate(
          offset: Offset(0, _animation.value),
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
                _controller.forward(from: 0);
              }
            },
          ),
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

class _SaveButtonState extends State<SaveButton>
    with SingleTickerProviderStateMixin {
  late bool active;
  late AnimationController _controller;
  late Animation<double> _animation;

  @override
  void initState() {
    super.initState();
    active = widget.initialValue;
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 100),
    );

    _animation = TweenSequence([
      TweenSequenceItem(
        tween: Tween<double>(begin: 1, end: 1.5)
            .chain(CurveTween(curve: Curves.easeOut)),
        weight: 50,
      ),
      TweenSequenceItem(
        tween: Tween<double>(begin: 1.5, end: 1)
            .chain(CurveTween(curve: Curves.easeOut)),
        weight: 50,
      ),
    ]).animate(_controller);
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: _animation,
      builder: (context, child) {
        return Transform.scale(
          scale: _animation.value,
          child: IconButton(
            icon: Icon(
              active ? Icons.bookmark : Icons.bookmark_outline,
              color: active ? Colors.yellow : Colors.grey,
            ),
            onPressed: () {
              setState(() {
                active = !active;
              });
              widget.onChange?.call(active);
              if (active) {
                _controller.forward(from: 0);
              }
            },
          ),
        );
      },
    );
  }
}
