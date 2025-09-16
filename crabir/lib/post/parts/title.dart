part of '../post.dart';

class PostTitle extends StatelessWidget {
  final Post post;

  const PostTitle({
    super.key,
    required this.post,
  });

  @override
  Widget build(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    final Color titleColor;
    if (post.pinned || post.distinguished == "moderator") {
      titleColor = theme.announcement;
    } else if (post.visited) {
      titleColor = theme.readPost;
    } else {
      titleColor = theme.postTitle;
    }
    return Text(
      post.title,
      textAlign: TextAlign.start,
      style:
          Theme.of(context).textTheme.titleMedium!.copyWith(color: titleColor),
    );
  }
}
