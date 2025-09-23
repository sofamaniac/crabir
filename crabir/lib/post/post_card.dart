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
  /// If set to false, show a thumbnail instead (when `enableThumbnail` is true).
  final bool showMedia;

  /// Enable thumbnail
  final bool enableThumbnail;

  /// If set to true, show the selftext even if the post is marked as spoiler
  final bool ignoreSelftextSpoiler;

  const RedditPostCard({
    super.key,
    required this.post,
    this.onLikeCallback,
    this.onSaveCallback,
    this.onTap,
    this.maxLines,
    this.showMedia = true,
    this.enableThumbnail = true,
    this.ignoreSelftextSpoiler = false,
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

  Widget? content(BuildContext context) {
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
      Kind.selftext
          when (!widget.post.spoiler || widget.ignoreSelftextSpoiler) &&
              widget.post.selftextHtml != null =>
        HtmlWithConditionalFade(
          htmlContent: widget.post.selftextHtml ?? "",
          maxLines: widget.maxLines,
        ),
      Kind.link || Kind.unknown => null,
      _ => null,
    };

    if (res != null) {
      return _contentWrap(res);
    } else {
      return res;
    }
  }

  Widget title() {
    final title = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      spacing: 4,
      children: [
        PostTitle(post: widget.post),
        PostFlair(post: widget.post),
        PostScore(post: widget.post),
      ],
    );
    if (widget.enableThumbnail &&
        widget.post.kind.showThumbnail(widget.showMedia)) {
      return wrapPostElement(
        Thumbnail(
          post: widget.post,
          child: title,
        ),
      );
    }
    return wrapPostElement(title);
  }

  @override
  Widget build(BuildContext context) {
    final child = content(context);
    final contentWidget = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      spacing: 8,
      children: [
        wrapPostElement(Header(post: widget.post)),
        title(),
        if (child != null) child,
        Footer(
          post: widget.post,
          onLike: _onLike,
          onSave: _onSave,
        ),
      ],
    );
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      elevation: 1,
      child: InkWell(
        onTap: widget.onTap,
        child: contentWidget,
      ),
    );
  }
}
