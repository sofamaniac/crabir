part of 'post.dart';

class RedditPostCard extends StatefulWidget {
  final Post post;

  /// Function to call after saving / unsaving the post.
  final SaveCallback? onSaveCallback;

  /// Function to call after liking / disliking the post.
  final LikeCallback? onLikeCallback;

  /// Function to call when the post is tapped.
  final VoidCallback? onTap;

  /// Maximum number of lines of the body to show.
  final int? maxLines;

  /// Whether to show media in full size.
  /// If set to false, show a thumbnail instead.
  final bool showMedia;

  const RedditPostCard({
    super.key,
    required this.post,
    this.onLikeCallback,
    this.onSaveCallback,
    this.onTap,
    this.maxLines,
    this.showMedia = true,
  });

  @override
  State<RedditPostCard> createState() => _RedditPostCardState();
}

class _RedditPostCardState extends State<RedditPostCard> {
  Widget _contentWrap(Widget widget) {
    if (this.widget.post.kind.isMedia()) {
      return widget;
    } else {
      return wrapPostElement(widget);
    }
  }

  Future<void> _onSave(bool save) async {
    setState(() {});
    await widget.onSaveCallback?.call(save);
  }

  Future<void> _onLike(VoteDirection dir) async {
    setState(() {});
    await widget.onLikeCallback?.call(dir);
  }

  Widget content(BuildContext context) {
    final res = switch (widget.post.kind) {
      // Media types
      Kind.video when widget.showMedia => VideoContent(post: widget.post),
      Kind.youtubeVideo when widget.showMedia =>
        YoutubeContent(post: widget.post),
      (Kind.mediaGallery || Kind.gallery) when widget.showMedia =>
        GalleryView(post: widget.post),
      Kind.image when widget.showMedia => ImageContent(
          post: widget.post,
        ),
      // Text types
      Kind.meta => Text("meta"),
      Kind.link || Kind.unknown => Container(),
      Kind.selftext || _ => HtmlWithConditionalFade(
          htmlContent: widget.post.selftextHtml ?? "",
          maxLines: widget.maxLines,
        ),
    };

    return _contentWrap(res);
  }

  Widget title() {
    final title = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        PostTitle(post: widget.post),
        PostFlair(post: widget.post),
        PostScore(post: widget.post),
      ],
    );
    if (widget.post.kind.showThumbnail(widget.showMedia)) {
      return Thumbnail(
        post: widget.post,
        child: title,
      );
    }
    return wrapPostElement(title);
  }

  @override
  Widget build(BuildContext context) {
    final contentWidget = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      spacing: 8,
      children: [
        wrapPostElement(Header(post: widget.post)),
        title(),
        content(context),
        wrapPostElement(
          Footer(
            post: widget.post,
            onLike: _onLike,
            onSave: _onSave,
          ),
        ),
      ],
    );
    final theme = context.watch<ThemeBloc>().state;
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      color: theme.background,
      elevation: 1,
      child: InkWell(
        onTap: widget.onTap,
        child: contentWidget,
      ),
    );
  }
}
