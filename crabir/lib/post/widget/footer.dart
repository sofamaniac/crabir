part of 'post.dart';

class Footer extends StatelessWidget {
  final Post post;
  final Future<void> Function(bool)? onSave;
  final Future<void> Function(VoteDirection)? onLike;
  const Footer({super.key, required this.post, this.onLike, this.onSave});
  @override
  Widget build(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    final likeColor = theme.primaryColor;
    final dislikeColor = Colors.cyanAccent;
    final settings = context.read<PostsSettingsCubit>().state;
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        VoteButton.like(
          initialValue: post.likes,
          colorActive: likeColor,
          onChange: onLike?.call,
        ),
        VoteButton.dislike(
          initialValue: post.likes,
          colorActive: dislikeColor,
          onChange: onLike?.call,
        ),
        SaveButton(
          initialValue: post.saved,
          onChange: onSave?.call,
        ),
        if (settings.showCommentsButton)
          IconButton(
            icon: const Icon(Icons.comment),
            tooltip: 'Comments',
            onPressed: () => navigateToSubscriptionsTab(
              context,
              ThreadRoute(
                permalink: post.permalink,
                post: post,
              ),
            ),
          ),
        if (settings.showOpenInAppButton)
          IconButton(
            icon: const Icon(Icons.exit_to_app),
            tooltip: 'Open in',
            onPressed: () async {
              final url = Uri.parse(
                post.urlOverriddenByDest ?? post.url,
              );
              await launchUrl(url);
            },
          ),
        if (settings.showShareButton)
          IconButton(
            icon: const Icon(Icons.share),
            tooltip: 'Share',
            onPressed: () {
              debugPost(post: post);
            },
          ),
      ],
    );
  }
}
