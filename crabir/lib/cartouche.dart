import 'package:flutter/material.dart';

class Cartouche extends StatelessWidget {
  final String text;
  final Color background;
  final Color foreground;
  final double fontSize;
  final Color? borderColor;

  const Cartouche(
    this.text, {
    super.key,
    this.background = Colors.transparent,
    this.foreground = Colors.white,
    this.fontSize = 12,
    this.borderColor,
  });
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 4),
      decoration: BoxDecoration(
          color: background,
          borderRadius: BorderRadius.circular(4),
          border: BoxBorder.all(color: borderColor ?? background)),
      child: Text(
        text,
        style: TextStyle(
          color: foreground,
          fontSize: fontSize,
        ),
      ),
    );
  }
}
