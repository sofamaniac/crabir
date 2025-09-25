part of '../post.dart';

class PostTitle extends StatelessWidget {
  final Post post;
  final bool ignoredRead;

  const PostTitle({
    super.key,
    required this.post,
    this.ignoredRead = false,
  });

  @override
  Widget build(BuildContext context) {
    final theme = CrabirTheme.of(context);
    final read = context.read<ReadPosts>().isRead(post.id);
    final Color titleColor;
    if (post.pinned || post.stickied || post.distinguished == "moderator") {
      titleColor = theme.announcement;
    } else if (read && !ignoredRead) {
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
