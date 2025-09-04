part of 'thread.dart';

/// Display a widget with some indentation guides.
class IndentedBox extends StatelessWidget {
  final int depth;
  final Widget child;

  const IndentedBox({
    super.key,
    required this.depth,
    required this.child,
  });

  @override
  Widget build(BuildContext context) {
    final indentWidth = 16.0;

    final List<Widget> dividers = [];

    for (int i = 0; i < depth; i++) {
      dividers.add(
        VerticalDivider(
          width: indentWidth,
        ),
      );
    }

    return IntrinsicHeight(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          ...dividers,
          Expanded(child: child),
        ],
      ),
    );
  }
}
