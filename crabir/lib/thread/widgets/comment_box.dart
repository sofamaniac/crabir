part of 'thread.dart';

class CommentBox extends StatelessWidget {
  final Thing comment;
  const CommentBox({super.key, required this.comment});

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<ThreadBloc>();
    final inner = switch (comment) {
      Thing_Comment(field0: final comment) => CommentRow(comment: comment),
      Thing_More(count: final count) => TextButton(
          onPressed: () => bloc.add(LoadMore(comment as Thing_More)),
          child: Text("Load more comments ($count)"),
        ),
      _ => Container()
    };

    return BlocBuilder<ThreadBloc, ThreadState>(
      builder: (context, state) => AnimatedSize(
        duration: Duration(milliseconds: 200),
        alignment: Alignment.topCenter,
        child: state.hidden.contains(commentId(comment))
            ? SizedBox.shrink()
            : inner,
      ),
    );
  }
}
