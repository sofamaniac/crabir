import 'package:flutter/material.dart';

/// Text with an outline
class BorderedText extends StatelessWidget {
  final String text;
  final Color borderColor;
  final TextStyle? style;
  final String? semanticLabel;

  const BorderedText({
    super.key,
    required this.text,
    required this.borderColor,
    this.style,
    this.semanticLabel,
  });

  @override
  Widget build(BuildContext context) {
    final borderStyle = (style ?? TextStyle()).copyWith(
      foreground: Paint()
        ..style = PaintingStyle.stroke
        ..strokeWidth = 1
        ..color = borderColor,
    );
    return Stack(
      children: [
        Text(
          text,
          style: borderStyle,
        ),
        Text(
          text,
          style: style,
          semanticsLabel: semanticLabel,
        )
      ],
    );
  }
}
