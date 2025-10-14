part of 'thread.dart';

class CommentsList extends StatelessWidget {
  const CommentsList({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final comments = context.watch<ThreadBloc>().state.flatComments;
    return SliverPadding(
      padding: EdgeInsets.only(bottom: 100),
      sliver: SliverList.builder(
        itemCount: comments.length,
        itemBuilder: (context, index) {
          final comment = comments[index];
          final commentView = ThreadEntry(
            thing: comment,
          );
          if (index < comments.length - 1 && depth(comments[index + 1]) == 0) {
            return Column(
              children: [
                commentView,
                Divider(height: 0),
              ],
            );
          }
          return commentView;
        },
      ),
    );
  }
}
