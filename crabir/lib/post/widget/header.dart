part of 'post.dart';

class Header extends StatelessWidget {
  final Post post;
  final bool showSubredditIcon;
  const Header({
    super.key,
    required this.post,
    this.showSubredditIcon = true,
  });

  bool _showDomain() {
    return !post.isSelf &&
        !post.isRedditMediaDomain &&
        post.domain != "reddit.com";
  }

  Widget _subreddit(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    final settings = context.read<PostsSettingsCubit>().state;
    final icon = post.subreddit.details?.icon;
    final name = post.subreddit.subreddit;
    final subreddit = Row(
      mainAxisSize: MainAxisSize.min,
      spacing: 8,
      children: [
        if (icon != null && showSubredditIcon) SubredditIcon(icon: icon),
        Text(
          name,
          style: _labelStyle(context).copyWith(
            color: theme.highlight,
          ),
        ),
      ],
    );
    if (settings.clickableCommunity) {
      return InkWell(
        onTap: () => context.router.root.navigate(
          FeedRoute(
            feed: Feed.subreddit(name),
          ),
        ),
        child: subreddit,
      );
    } else {
      return subreddit;
    }
  }

  Widget _author(BuildContext context) {
    final settings = context.watch<PostsSettingsCubit>().state;
    final author = Text(
      post.author?.username ?? "[deleted]",
      style: _labelStyle(context),
    );
    if (settings.clickableUser) {
      return InkWell(
        onTap: () {
          final username = post.author?.username;
          if (username != null) {
            context.router.navigate(UserRoute(username: username));
          }
        },
        child: author,
      );
    } else {
      return author;
    }
  }

  TextStyle _labelStyle(BuildContext context) {
    return Theme.of(context)
        .textTheme
        .labelSmall!
        .copyWith(fontWeight: FontWeight.normal);
  }

  @override
  Widget build(BuildContext context) {
    return Wrap(
      crossAxisAlignment: WrapCrossAlignment.center,
      children: [
        _subreddit(context),
        if (post.isCrosspost)
          const RotatedBox(
            quarterTurns: 1,
            child: Icon(Icons.alt_route, color: Colors.greenAccent),
          ),
        const Text(' • '),
        _author(context),
        const Text(' • '),
        Text(post.createdUtc.timeSince(), style: _labelStyle(context)),
        if (_showDomain()) ...[
          const Text(' • '),
          Text(
            post.domain,
            style: _labelStyle(context),
          ),
        ],
      ],
    );
  }
}
