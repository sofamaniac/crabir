part of 'thread.dart';

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

class _CommentViewHandlerState extends State<CommentViewHandler> {
  bool showBottomBar = false;
  bool? likes;
  bool saved = false;
  @override
  void initState() {
    super.initState();
    likes = widget.comment.likes;
    saved = widget.comment.saved;
  }

  @override
  Widget build(BuildContext context) {
    final comment = widget.comment;
    return BlocBuilder<ThreadBloc, ThreadState>(
      builder: (context, state) => CommentView(
        comment: comment,
        onLongPress: () {
          context.read<ThreadBloc>().add(Collapse(comment));
          HapticFeedback.selectionClick();
        },
        animateBottomBar: true,
      ),
    );
  }
}

class CommentViewHandler extends StatefulWidget {
  final Comment comment;

  const CommentViewHandler({super.key, required this.comment});

  @override
  State<StatefulWidget> createState() => _CommentViewHandlerState();
}
