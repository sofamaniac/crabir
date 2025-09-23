import 'package:flutter/material.dart';

class SeparatedRow extends StatelessWidget {
  final Widget separator;
  final List<Widget> children;
  final WrapCrossAlignment crossAxisAlignment;
  const SeparatedRow({
    super.key,
    required this.children,
    this.separator = const Text(' • '),
    this.crossAxisAlignment = WrapCrossAlignment.start,
  });

  @override
  Widget build(BuildContext context) {
    List<Widget> elements = [];
    for (final child in children) {
      elements.add(child);
      elements.add(separator);
    }
    // Remove last separator
    elements.removeLast();
    return Wrap(
      crossAxisAlignment: crossAxisAlignment,
      children: elements,
    );
  }
}
