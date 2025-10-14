part of 'post.dart';

class DenseCard extends StatelessWidget {
  final Post post;
  final SaveCallback? onSaveCallback;
  final LikeCallback? onLikeCallback;
  final VoidCallback? onTap;
  final double? elevation;
  final bool pushSubreddit;
  const DenseCard({
    super.key,
    required this.post,
    this.onSaveCallback,
    this.onLikeCallback,
    this.onTap,
    this.elevation,
    this.pushSubreddit = false,
  });
  Widget wrap(Widget widget) {
    return Padding(
        padding: EdgeInsets.symmetric(horizontal: 16), child: widget);
  }

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
              push: pushSubreddit,
            ),
          ),
          wrap(PostScore(post: post)),
        ],
      ),
    );
    final theme = CrabirTheme.of(context);
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      color: theme.background,
      elevation: elevation,
      child: InkWell(
        onTap: onTap,
        child: contentWidget,
      ),
    );
  }
}
