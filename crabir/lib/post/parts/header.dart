part of '../post.dart';

class Header extends StatelessWidget {
  final Post post;
  final bool showSubredditIcon;
  final VoidCallback? onSubredditTap;
  const Header({
    super.key,
    required this.post,
    this.showSubredditIcon = true,
    this.onSubredditTap,
  });

  bool _showDomain() {
    return !post.isSelf &&
        !post.isRedditMediaDomain &&
        post.domain != "reddit.com";
  }

  Widget _subreddit(BuildContext context) {
    final theme = CrabirTheme.of(context);
    final settings = context.read<PostsSettingsCubit>().state;
    final name = post.subreddit.subreddit;
    final subreddit = Text(
      name,
      style: _labelStyle(context).copyWith(color: theme.highlight),
    );
    if (settings.clickableCommunity) {
      final onTap =
          onSubredditTap ?? () => context.go("/r/${post.subreddit.subreddit}");
      return InkWell(onTap: onTap, child: subreddit);
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
            context.go("/u/$username");
          }
        },
        child: author,
      );
    } else {
      return author;
    }
  }

  TextStyle _labelStyle(BuildContext context) {
    return Theme.of(context).textTheme.labelSmall!.copyWith(
          fontWeight: FontWeight.normal,
          color: CrabirTheme.of(context).secondaryText,
        );
  }

  @override
  Widget build(BuildContext context) {
    final icon = post.subreddit.details?.icon;
    final settings = context.watch<PostsSettingsCubit>().state;
    return Row(
      spacing: 8,
      children: [
        if (icon != null && showSubredditIcon)
          SubredditIcon(
            icon: icon,
            subredditName: post.subreddit.subreddit,
            clickable: settings.clickableCommunity,
          ),
        Expanded(
          child: SeparatedRow(
            spacing: 2,
            crossAxisAlignment: WrapCrossAlignment.center,
            separatorStyle: _labelStyle(context),
            children: [
              _subreddit(context),
              if (post.isCrosspost)
                const RotatedBox(
                  quarterTurns: 1,
                  child: Icon(Icons.alt_route, color: Colors.greenAccent),
                ),
              _author(context),
              Text(
                post.createdUtc.timeSince(context),
                style: _labelStyle(context),
              ),
              if (_showDomain()) ...[
                Text(post.domain, style: _labelStyle(context)),
                if (post.locked)
                  Icon(
                    Icons.lock_sharp,
                    size: _labelStyle(context).fontSize,
                    color: _labelStyle(context).color,
                  ),
              ],
            ],
          ),
        ),
      ],
    );
  }
}
