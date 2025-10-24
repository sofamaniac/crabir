part of 'post.dart';

class CompactCard extends PostView {
  final double? elevation;
  final bool pushSubreddit;
  const CompactCard({
    super.key,
    required super.post,
    super.onSaveCallback,
    super.onLikeCallback,
    super.onTap,
    super.respectHidden = true,
    required super.enableThumbnail,
    required super.ignoreRead,
    this.elevation,
    this.pushSubreddit = false,
  }) : super(ignoreSelftextSpoiler: false);

  @override
  State<StatefulWidget> createState() => _CompactCardState();
}

class _CompactCardState extends State<CompactCard> {
  Widget wrap(Widget widget) {
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 16),
      child: widget,
    );
  }

  Future<void> _onLike(VoteDirection dir) async {
    await widget.post.vote(direction: dir, client: RedditAPI.client());
    await widget.onLikeCallback?.call(dir);
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    if (widget.respectHidden && widget.post.hidden) {
      return SizedBox.shrink();
    }
    final Widget content = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      spacing: 8,
      children: [
        wrap(
          PostTitle(
            post: widget.post,
            ignoredRead: widget.ignoreRead,
          ),
        ),
        wrap(
          Header(
            post: widget.post,
            showSubredditIcon: false,
            push: widget.pushSubreddit,
          ),
        ),
      ],
    );
    final Widget contentWidget;
    if (LayoutSettings.of(context).showThumbnail) {
      contentWidget = Thumbnail(
        post: widget.post,
        child: content,
      );
    } else {
      contentWidget = content;
    }
    final theme = CrabirTheme.of(context);
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      color: theme.background,
      elevation: widget.elevation,
      child: InkWell(
        onTap: widget.onTap,
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _Score(
              post: widget.post,
              onLike: _onLike,
            ),
            Expanded(child: contentWidget),
          ],
        ),
      ),
    );
  }
}

class _Score extends StatelessWidget {
  final Post post;
  final LikeCallback? onLike;

  const _Score({
    required this.post,
    this.onLike,
  });

  @override
  Widget build(BuildContext context) {
    final theme = CrabirTheme.of(context);
    return Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        VoteButton.like(
          likes: post.likes.toVoteDirection(),
          colorActive: theme.primaryColor,
          onChange: onLike,
        ),
        LikeText(
          score: post.score,
          likes: post.likes,
          style: Theme.of(context).textTheme.titleMedium!,
          scaling: 1.5,
        ),
        VoteButton.dislike(
          likes: post.likes.toVoteDirection(),
          colorActive: theme.downvoteContent,
          onChange: onLike,
        ),
      ],
    );
  }
}
