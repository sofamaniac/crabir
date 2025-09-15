part of 'post.dart';

class DenseCard extends RedditPostCard {
  const DenseCard({
    super.key,
    required super.post,
    super.onSave,
    super.onLike,
    super.onTap,
  }) : super(showMedia: false);
  @override
  Widget build(BuildContext context) {
    final contentWidget = Thumbnail(
      post: post,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        spacing: 8,
        children: [
          wrap(
            PostTitle(
              post: post,
            ),
          ),
          wrap(
            Header(
              post: post,
              showSubredditIcon: false,
            ),
          ),
          wrap(PostScore(post: post)),
        ],
      ),
    );
    final theme = context.watch<ThemeBloc>().state;
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      color: theme.background,
      elevation: 1,
      child: InkWell(
        onTap: onTap,
        child: contentWidget,
      ),
    );
  }
}
