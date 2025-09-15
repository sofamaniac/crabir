part of '../post.dart';

class PostTitle extends StatelessWidget {
  final Post post;

  const PostTitle({
    super.key,
    required this.post,
  });

  Widget thumbnail() {
    if (post.thumbnail != null) {
      return Expanded(
        flex: 1,
        child: AspectRatio(
          aspectRatio: 1.0,
          child: ClipRect(
            child: Image.network(post.thumbnail!.url, fit: BoxFit.cover),
          ),
        ),
      );
    } else {
      return Expanded(
        flex: 1,
        child: AspectRatio(
          aspectRatio: 1.0,
          child: ClipRect(
            child: ColoredBox(
              color: Colors.grey,
              child: Center(
                child: Icon(Icons.link),
              ),
            ),
          ),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    final Color titleColor;
    // TODO: add if post read.
    if (post.pinned) {
      titleColor = theme.announcement;
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
