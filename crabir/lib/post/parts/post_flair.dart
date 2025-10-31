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
        onTap: () => SearchPostsView(
          subreddit: post.subreddit.subreddit,
          flair: post.linkFlair,
        ).goNamed(context),
        child: FlairView(
          flair: post.linkFlair,
          showColor: settings.showFlairColors,
          showEmoji: settings.showFlairEmojis,
        ),
      );
    } else {
      flairWidget = FlairView(
        flair: post.linkFlair,
        showColor: settings.showFlairColors,
        showEmoji: settings.showFlairEmojis,
      );
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
