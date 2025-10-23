part of 'post.dart';

class DenseCard extends PostView {
  final double? elevation;
  final bool pushSubreddit;
  const DenseCard({
    super.key,
    required super.post,
    super.onSaveCallback,
    super.onLikeCallback,
    super.onTap,
    this.elevation,
    this.pushSubreddit = false,
    super.respectHidden = true,
    required super.enableThumbnail,
    required super.ignoreRead,
  }) : super(ignoreSelftextSpoiler: false);

  @override
  State<StatefulWidget> createState() => _DenseCardState();
}

class _DenseCardState extends State<DenseCard> {
  Widget wrap(Widget widget) {
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 16),
      child: widget,
    );
  }

  @override
  Widget build(BuildContext context) {
    if (widget.respectHidden && widget.post.hidden) {
      return SizedBox.shrink();
    }
    final content = Column(
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
        wrap(PostScore(post: widget.post)),
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
        child: contentWidget,
      ),
    );
  }
}
