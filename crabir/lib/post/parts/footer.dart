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
    final dislikeColor = Colors.cyanAccent;
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
            color: Colors.grey,
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
            color: Colors.grey,
            tooltip: 'Open in',
            onPressed: () async {
              final url = Uri.parse(
                post.urlOverriddenByDest ?? post.url,
              );
              await launchUrl(url);
            },
          ),
        if (settings.showShareButton) ShareButton(post: post),
        // TODO: add missing buttons
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
            onTap: () => filters.hideSubreddit(post.subreddit.subreddit),
          ),
          ListTile(
            leading: Icon(Icons.link),
            title: Text("Mute ${post.domain}"),
            onTap: () => filters.hideDomain(post.domain),
          ),
          if (post.author != null)
            ListTile(
              leading: Icon(Icons.person),
              title: Text("Mute ${post.author!.username}"),
              onTap: () => filters.hideUser(post.author!.username),
            ),
          if (post.linkFlair.text != null && post.linkFlair.text!.isNotEmpty)
            ListTile(
              leading: Icon(Icons.label_outlined),
              title: Text("Mute ${post.linkFlair.text!}"),
              onTap: () => filters.hideFlair(post.linkFlair.text!),
            ),
        ],
      ),
    );
  }

  Dialog dialog(BuildContext context) {
    return Dialog(
      elevation: 1,
      child: ListView(
        shrinkWrap: true,
        children: [
          Text("Go to subreddit"),
          // When crosspost
          Text("View original post"),
          Text("Go to user"),
          Text("search flair"),
          // Optional
          Text("mark read / unread"),
          // Optional
          Text("hide"),
          Text("Report"),
          ListTile(
            leading: Icon(Icons.volume_mute),
            title: Text("Mute..."),
            trailing: Icon(Icons.more),
            onTap: () {
              Navigator.pop(context);
              showDialog(context: context, builder: muteDialog);
            },
          ),
          // Optional
          Text("share"),
          Text("copy"),
          Text("add to custom feed")
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return IconButton(
      icon: Icon(Icons.more_vert),
      onPressed: () => showDialog(context: context, builder: dialog),
    );
  }
}
