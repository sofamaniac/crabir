part of 'thread.dart';

class ThreadEntry extends StatelessWidget {
  final Thing thing;
  const ThreadEntry({super.key, required this.thing});

  @override
  Widget build(BuildContext context) {
    ThreadBloc? thread = context.watch<ThreadBloc?>();
    final (hidden, collapsed) =
        context.select<ThreadBloc?, (bool, bool)>((bloc) {
      final id = commentId(thing);
      return (
        bloc?.state.hidden.contains(id) ?? false,
        bloc?.state.collapsed.contains(id) ?? false,
      );
    });
    final settings = CommentsSettings.of(context);
    final theme = CrabirTheme.of(context);
    final locales = AppLocalizations.of(context);
    final inner = switch (thing) {
      _ when hidden => SizedBox.shrink(),
      Thing_Comment(field0: final comment)
          when collapsed && settings.hideTextCollapsed =>
        IndentedBox(
          depth: comment.depth,
          child: CollapsedComment(
            comment: comment,
            onLongPress: () {
              if (thread != null) {
                thread.add(Collapse(comment));
                HapticFeedback.selectionClick();
              }
            },
          ),
        ),
      Thing_Comment(field0: final comment)
          when (!collapsed && !hidden) ||
              (collapsed && !settings.hideTextCollapsed) =>
        IndentedBox(
          depth: comment.depth,
          child: CommentView(
            comment: comment,
            onLongPress: () {
              if (thread != null) {
                thread.add(Collapse(comment));
                HapticFeedback.selectionClick();
              }
            },
            animateBottomBar: true,
          ),
        ),
      Thing_More(count: final count, depth: final depth) => IndentedBox(
          depth: depth,
          child: TextButton(
            onPressed: () => thread?.add(LoadMore(thing as Thing_More)),
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
  }
}
