part of 'thread.dart';

class ThreadEntry extends StatelessWidget {
  final Thing comment;
  const ThreadEntry({super.key, required this.comment});

  @override
  Widget build(BuildContext context) {
    final thread = context.watch<ThreadBloc>();
    final theme = context.watch<ThemeBloc>().state;
    final locales = AppLocalizations.of(context);
    final inner = switch (comment) {
      Thing_Comment(field0: final comment) => IndentedBox(
          depth: comment.depth,
          child: CommentView(
            comment: comment,
            onLongPress: () {
              thread.add(Collapse(comment));
              HapticFeedback.selectionClick();
            },
            animateBottomBar: true,
          ),
        ),
      Thing_More(count: final count, depth: final depth) => IndentedBox(
          depth: depth,
          child: TextButton(
            onPressed: () => thread.add(LoadMore(comment as Thing_More)),
            child: Text(
              locales.loadMoreComments(count),
              style: TextStyle(color: theme.primaryColor),
            ),
          ),
        ),
      _ => SizedBox.shrink()
    };

    return AnimatedSize(
      duration: Duration(milliseconds: 200),
      alignment: Alignment.topCenter,
      child: thread.state.hidden.contains(commentId(comment))
          ? SizedBox.shrink()
          : inner,
    );
  }
}
