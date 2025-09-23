import 'package:flutter/material.dart';

class SeparatedRow extends StatelessWidget {
  final String separator;
  final TextStyle? separatorStyle;
  final List<Widget> children;
  final WrapCrossAlignment crossAxisAlignment;
  final double spacing;
  const SeparatedRow({
    super.key,
    required this.children,
    this.separator = '•',
    this.separatorStyle,
    this.spacing = 0.0,
    this.crossAxisAlignment = WrapCrossAlignment.start,
  });

  @override
  Widget build(BuildContext context) {
    List<Widget> elements = [];
    for (final child in children) {
      elements.add(child);
      elements.add(Text(separator, style: separatorStyle));
    }
    // Remove last separator
    elements.removeLast();
    return Wrap(
      crossAxisAlignment: crossAxisAlignment,
      spacing: spacing,
      children: elements,
    );
  }
}
