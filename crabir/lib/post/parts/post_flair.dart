part of '../post.dart';

class PostFlair extends StatelessWidget {
  final Post post;

  const PostFlair({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    final settings = context.read<PostsSettingsCubit>().state;
    final Widget flairWidget;
    if (settings.tapFlairToSearch) {
      flairWidget = InkWell(
        onTap: () => context.router.root.navigate(
          SearchPostsRoute(
            // provide key to differentiate the pages
            // but prevents rebuilding when clicking the same flair again
            key: ValueKey("${post.subreddit.subreddit}-${post.linkFlair}"),
            subreddit: post.subreddit.subreddit,
            flair: post.linkFlair,
          ),
        ),
        child: FlairView(flair: post.linkFlair),
      );
    } else {
      flairWidget = FlairView(flair: post.linkFlair);
    }

    if (post.spoiler) {
      return Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        spacing: 8,
        children: [SpoilerTag(), flairWidget],
      );
    } else {
      return flairWidget;
    }
  }
}
