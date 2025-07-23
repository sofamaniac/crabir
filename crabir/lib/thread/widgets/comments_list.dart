part of 'thread.dart';

class CommentsList extends StatefulWidget {
  final List<Thing> comments;

  const CommentsList({
    super.key,
    required this.comments,
  });

  @override
  State<CommentsList> createState() => _CommentsListState();
}

class _CommentsListState extends State<CommentsList> {
  @override
  Widget build(BuildContext context) {
    return SliverPadding(
      padding: EdgeInsets.only(bottom: 100),
      sliver: SliverList.builder(
        itemCount: widget.comments.length,
        itemBuilder: (context, index) {
          final comment = widget.comments[index];
          final commentView = CommentBox(
            comment: comment,
          );
          if (index < widget.comments.length - 1 &&
              depth(widget.comments[index + 1]) == 0) {
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
