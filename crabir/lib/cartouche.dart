import 'package:flutter/material.dart';

class Cartouche extends StatelessWidget {
  final String text;
  final Color background;
  final Color foreground;
  final double fontSize;

  const Cartouche(
    this.text, {
    super.key,
    this.background = Colors.black,
    this.foreground = Colors.white,
    this.fontSize = 12,
  });
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 4),
      decoration: BoxDecoration(
        color: background,
        borderRadius: BorderRadius.circular(2),
      ),
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
