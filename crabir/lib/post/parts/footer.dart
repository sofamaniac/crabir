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
    final likes = ValueNotifier(post.likes.toVoteDirection());
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
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
      ],
    );
  }
}
