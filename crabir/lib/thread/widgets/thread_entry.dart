part of 'thread.dart';

class ThreadEntry extends StatelessWidget {
  final Thing thing;
  const ThreadEntry({super.key, required this.thing});

  @override
  Widget build(BuildContext context) {
    final thread = context.read<ThreadBloc>();
    return BlocSelector<ThreadBloc, ThreadState, (bool, bool)>(
      selector: (state) {
        final id = commentId(thing);
        return (state.hidden.contains(id), state.collapsed.contains(id));
      },
      builder: (context, state) {
        final (hidden, collapsed) = state;
        final theme = CrabirTheme.of(context);
        final locales = AppLocalizations.of(context);
        final inner = switch (thing) {
          _ when hidden => SizedBox.shrink(),
          Thing_Comment(field0: final comment) when collapsed => IndentedBox(
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
          Thing_Comment(field0: final comment) when !collapsed && !hidden =>
            IndentedBox(
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
                onPressed: () => thread.add(LoadMore(thing as Thing_More)),
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
          child: inner,
        );
      },
    );
  }
}
