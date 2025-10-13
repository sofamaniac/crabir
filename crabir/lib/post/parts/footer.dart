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
    final settings = context.watch<PostsSettingsCubit>().state;
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
            color: theme.secondaryText,
            tooltip: 'Comments',
            // onPressed: () => navigateToSubscriptionsTab(
            //   context,
            //   ThreadRoute(
            //     post: post,
            //   ),
            // ),
            onPressed: () => context.go(post.permalink, extra: post),
          ),
        if (settings.showOpenInAppButton)
          IconButton(
            icon: const Icon(Icons.exit_to_app),
            color: theme.secondaryText,
            tooltip: 'Open in',
            onPressed: () async {
              final url = Uri.parse(
                post.urlOverriddenByDest ?? post.url,
              );
              await launchUrl(url);
            },
          ),
        if (settings.showMarkAsReadButton) ReadButton(post: post, short: true),
        if (settings.showShareButton) ShareButton(post: post, short: true),
        if (settings.showHideButton) HideButton(post: post, short: true),
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
                filters.hideFlair(post.linkFlair);
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
                context.go(
                  post.subreddit.subredditNamePrefixed,
                  extra: Feed.subreddit(post.subreddit.subreddit),
                );
              },
            ),
            if (post.isCrosspost)
              ListTile(
                leading: Icon(Icons.link),
                title: Text("Go to original post"),
                onTap: () {
                  context.go(post.crosspostParentList.first.permalink,
                      extra: post.crosspostParentList.first);
                },
              ),
            if (post.author?.username != null)
              ListTile(
                leading: Icon(Icons.person),
                title: Text("See ${post.author!.username}'s info"),
                onTap: () {
                  // Navigator.pop(context);
                  // context.router
                  //     .navigate(UserRoute(username: post.author!.username));
                },
              ),
            if (post.linkFlair.text != null)
              ListTile(
                leading: Icon(Icons.label_outline),
                title: Text("Search ${post.linkFlair.text} posts"),
                onTap: () {
                  SearchPostsView(flair: post.linkFlair).goNamed(context);
                },
              ),
            if (!settings.showMarkAsReadButton)
              ReadButton(post: post, short: false),
            if (!settings.showHideButton)
              HideButton(
                post: post,
                short: false,
              ),
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
              ShareButton(post: post, short: false),
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
      color: theme.secondaryText,
      onPressed: () => showDialog(context: context, builder: dialog),
    );
  }
}

abstract class LongShortButton extends StatelessWidget {
  final Post post;
  final bool short;

  const LongShortButton({super.key, required this.post, required this.short});

  Widget icon(BuildContext context);
  String label(BuildContext context);
  void onTap(BuildContext context);

  Widget long(BuildContext context) {
    return ListTile(
      leading: icon(context),
      title: Text(label(context)),
      onTap: () => onTap(context),
    );
  }

  Widget _short(BuildContext context) {
    final readPosts = ReadPosts.of(context);
    final theme = CrabirTheme.of(context);
    return IconButton(
      color: theme.secondaryText,
      onPressed: () {
        if (readPosts.isRead(post.id)) {
          readPosts.unmark(post.id);
        } else {
          readPosts.mark(post.id);
        }
      },
      icon: icon(context),
      tooltip: label(context),
    );
  }

  @override
  Widget build(BuildContext context) {
    if (short) {
      return _short(context);
    } else {
      return long(context);
    }
  }
}

class ReadButton extends LongShortButton {
  const ReadButton({super.key, required super.post, required super.short});

  @override
  onTap(BuildContext context) {
    final readPosts = ReadPosts.of(context);
    if (readPosts.isRead(post.id)) {
      readPosts.unmark(post.id);
    } else {
      readPosts.mark(post.id);
    }
  }

  @override
  Widget icon(BuildContext context) {
    final readPosts = ReadPosts.of(context);
    if (readPosts.isRead(post.id)) {
      return Icon(Icons.mark_chat_unread);
    } else {
      return Icon(Icons.mark_chat_read);
    }
  }

  @override
  String label(BuildContext context) {
    final readPosts = ReadPosts.of(context);
    final locales = AppLocalizations.of(context);
    if (readPosts.isRead(post.id)) {
      return locales.markUnread;
    } else {
      return locales.markRead;
    }
  }
}

class HideButton extends LongShortButton {
  const HideButton({super.key, required super.post, required super.short});

  @override
  onTap(BuildContext context) async {
    if (post.hidden) {
      await post.unhide(client: RedditAPI.client());
    } else {
      await post.hide_(client: RedditAPI.client());
    }
  }

  @override
  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    if (post.hidden) {
      return locales.unhidePost;
    } else {
      return locales.hidePost;
    }
  }

  @override
  Widget icon(BuildContext context) {
    if (post.hidden) {
      return Icon(Icons.add);
    } else {
      return Icon(Icons.close);
    }
  }
}
