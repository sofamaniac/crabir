part of 'post.dart';

class PostTitle extends StatelessWidget {
  final Post post;

  final bool showThumbnail;

  const PostTitle({
    super.key,
    required this.post,
    this.showThumbnail = false,
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

  Widget flair(BuildContext context) {
    final settings = context.read<PostsSettingsCubit>().state;
    if (settings.tapFlairToSearch) {
      return InkWell(
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
      return FlairView(flair: post.linkFlair);
    }
  }

  Widget title(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    final settings = context.read<PostsSettingsCubit>().state;
    final title = Text(
      post.title,
      textAlign: TextAlign.start,
      style: Theme.of(context)
          .textTheme
          .titleMedium!
          .copyWith(color: theme.postTitle),
    );
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        title,
        if (settings.showPostFlair) flair(context),
        RichText(
          text: TextSpan(
            children: [
              TextSpan(
                text: "${post.ups}",
                style: Theme.of(context).textTheme.titleMedium,
              ),
              TextSpan(text: '•', style: Theme.of(context).textTheme.bodySmall),
              TextSpan(
                text: "${post.numComments} comments",
                style: Theme.of(context).textTheme.bodySmall,
              ),
              if (post.over18) WidgetSpan(child: NsfwTag()),
            ],
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Expanded(
          flex: 3,
          child: title(context),
        ),
        if (showThumbnail) thumbnail(),
      ],
    );
  }
}
