part of 'thread.dart';

class _PostView extends StatelessWidget {
  final Post post;
  const _PostView({
    required this.post,
  });

  Widget title(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        PostTitle(post: post),
        PostFlair(post: post),
        PostScore(post: post),
      ],
    );
  }

  Widget nestedCard(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      spacing: 8,
      children: [
        wrapPostElement(
          Header(
            post: post,
            onSubredditTap: () => context.push(
              "/r/${post.crosspostParentList.first.subreddit.subreddit}",
            ),
          ),
        ),
        wrapPostElement(title(context)),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: DecoratedBox(
            decoration: BoxDecoration(
              border: Border.all(color: Colors.white54),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: DenseCard(
                post: post.crosspostParentList.first,
                pushSubreddit: true,
                // Disable shadow
                elevation: 0,
                onTap: () {
                  context.push(
                    post.crosspostParentList.first.permalink,
                    extra: post.crosspostParentList.first,
                  );
                },
              ),
            ),
          ),
        ),
        wrapPostElement(
          Footer(
            post: post,
            onLike: (direction) async {
              await post.vote(direction: direction, client: RedditAPI.client());
            },
            onSave: (save) async {
              if (save) {
                await post.save(client: RedditAPI.client());
              } else {
                await post.unsave(client: RedditAPI.client());
              }
            },
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    if (post.crosspostParentList.isNotEmpty) {
      final contentWidget = nestedCard(context);
      final theme = CrabirTheme.of(context);
      return Card(
        margin: const EdgeInsets.symmetric(vertical: 4),
        color: theme.background,
        elevation: 1,
        child: InkWell(
          onTap: () => context.push(post.crosspostParentList.first.permalink),
          child: contentWidget,
        ),
      );
    } else {
      final settings = context.watch<CommentsSettingsCubit>().state;
      final showMedia =
          settings.postMediaPreviewSize == MediaPreviewSize.fullPreview;
      final showThumbnail =
          settings.postMediaPreviewSize == MediaPreviewSize.thumbnail &&
              !post.kind.isMedia();
      return RedditPostCard(
        post: post,
        maxLines: null,
        showMedia: showMedia,
        enableThumbnail: showThumbnail,
        ignoreSelftextSpoiler: true,
        ignoreRead: true,
      );
    }
  }
}
