part of '../post.dart';

class Footer extends StatelessWidget {
  final Post post;
  final SaveCallback? onSave;
  final LikeCallback? onLike;
  const Footer({super.key, required this.post, this.onLike, this.onSave});
  @override
  Widget build(BuildContext context) {
    final theme = CrabirTheme.of(context);
    final likeColor = theme.primaryColor;
    final dislikeColor = theme.downvoteContent;
    final settings = context.read<PostsSettingsCubit>().state;
    final likes = post.likes.toVoteDirection();
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        VoteButton.like(
          likes: likes,
          colorActive: likeColor,
          onChange: (dir) async {
            await post.vote(direction: dir, client: RedditAPI.client());
            onLike?.call(dir);
          },
        ),
        VoteButton.dislike(
          likes: likes,
          colorActive: dislikeColor,
          onChange: (dir) async {
            await post.vote(direction: dir, client: RedditAPI.client());
            onLike?.call(dir);
          },
        ),
        SaveButton(
          initialValue: post.saved,
          onChange: (save) async {
            if (save) {
              await post.save(client: RedditAPI.client());
            } else {
              await post.unsave(client: RedditAPI.client());
            }
            onSave?.call(save);
          },
        ),
        if (settings.showCommentsButton)
          IconButton(
            icon: const Icon(Icons.comment),
            color: theme.alternativeText,
            tooltip: 'Comments',
            onPressed: () => navigateToSubscriptionsTab(
              context,
              ThreadRoute(
                post: post,
              ),
            ),
          ),
        if (settings.showOpenInAppButton)
          IconButton(
            icon: const Icon(Icons.exit_to_app),
            color: theme.alternativeText,
            tooltip: 'Open in',
            onPressed: () async {
              final url = Uri.parse(
                post.urlOverriddenByDest ?? post.url,
              );
              await launchUrl(url);
            },
          ),
        if (settings.showShareButton) ShareButton(post: post),
        MoreOptionButton(post: post),
      ],
    );
  }
}

class MoreOptionButton extends StatelessWidget {
  final Post post;
  const MoreOptionButton({super.key, required this.post});

  Dialog muteDialog(BuildContext context) {
    final icon = post.subreddit.details?.icon;
    final filters = context.read<GlobalFiltersCubit>();
    return Dialog(
      child: ListView(
        shrinkWrap: true,
        children: [
          ListTile(
            leading: icon != null ? SubredditIcon(icon: icon) : null,
            title: Text("Mute ${post.subreddit.subreddit}"),
            onTap: () {
              filters.hideSubreddit(post.subreddit.subreddit);
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: Icon(Icons.link),
            title: Text("Mute ${post.domain}"),
            onTap: () {
              filters.hideDomain(post.domain);
              Navigator.pop(context);
            },
          ),
          if (post.author != null)
            ListTile(
              leading: Icon(Icons.person),
              title: Text("Mute ${post.author!.username}"),
              onTap: () {
                filters.hideUser(post.author!.username);
                Navigator.pop(context);
              },
            ),
          if (post.linkFlair.text != null && post.linkFlair.text!.isNotEmpty)
            ListTile(
              leading: Icon(Icons.label_outlined),
              title: Text("Mute ${post.linkFlair.text!}"),
              onTap: () {
                filters.hideFlair(post.linkFlair.text!);
                Navigator.pop(context);
              },
            ),
        ],
      ),
    );
  }

  Dialog dialog(BuildContext context) {
    final settings = context.read<PostsSettingsCubit>().state;
    return Dialog(
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: ListView(
          shrinkWrap: true,
          children: [
            ListTile(
              leading: Icon(Icons.reddit),
              title: Text("Go to ${post.subreddit.subreddit}"),
              onTap: () {
                Navigator.pop(context);
                context.router.navigate(
                  FeedRoute(
                    feed: Feed.subreddit(post.subreddit.subreddit),
                  ),
                );
              },
            ),
            if (post.isCrosspost)
              ListTile(
                leading: Icon(Icons.link),
                title: Text("Go to original post"),
                onTap: () {
                  Navigator.pop(context);
                  context.router.push(
                    ThreadRoute(post: post.crosspostParentList.first),
                  );
                },
              ),
            Text("Go to user"),
            if (post.author?.username != null)
              ListTile(
                leading: Icon(Icons.person),
                title: Text("See ${post.author!.username}'s info"),
                onTap: () {
                  Navigator.pop(context);
                  context.router
                      .navigate(UserRoute(username: post.author!.username));
                },
              ),
            if (post.linkFlair.text != null)
              ListTile(
                leading: Icon(Icons.label_outline),
                title: Text("Search ${post.linkFlair.text} posts"),
                onTap: () {
                  Navigator.pop(context);
                  context.router.navigate(
                    SearchPostsRoute(flair: post.linkFlair),
                  );
                },
              ),
            if (!settings.showMarkAsReadButton) Text("mark read / unread"),
            if (!settings.showHideButton) Text("hide"),
            Text("Report"),
            ListTile(
              leading: Icon(Icons.volume_off),
              title: Text("Mute..."),
              trailing: Icon(Icons.arrow_right),
              onTap: () {
                Navigator.pop(context);
                showDialog(context: context, builder: muteDialog);
              },
            ),
            if (!settings.showShareButton)
              ListTile(
                leading: Icon(Icons.share),
                title: Text("Share..."),
                onTap: () {
                  Navigator.pop(context);
                  shareModelBottomSheet(context, post);
                },
              ),
            Text("copy"),
            Text("add to custom feed")
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = CrabirTheme.of(context);
    return IconButton(
      icon: Icon(Icons.more_vert),
      color: theme.alternativeText,
      onPressed: () => showDialog(context: context, builder: dialog),
    );
  }
}
