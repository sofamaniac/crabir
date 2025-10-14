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

  /// Set to true to not change title color when post is read
  final bool ignoreRead;

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
    this.ignoreRead = false,
  });

  @override
  State<RedditPostCard> createState() => _RedditPostCardState();
}

class _RedditPostCardState extends State<RedditPostCard> {
  Future<void> _onSave(bool save) async {
    setState(() {});
    await widget.onSaveCallback?.call(save);
  }

  Future<void> _onLike(VoteDirection dir) async {
    setState(() {});
    await widget.onLikeCallback?.call(dir);
  }

  @override
  Widget build(BuildContext context) {
    final contentWidget = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      spacing: 8,
      children: [
        wrapPostElement(Header(post: widget.post)),
        PostCardTitle(
          post: widget.post,
          showMedia: widget.showMedia,
          enableThumbnail: widget.enableThumbnail,
          ignoreRead: widget.ignoreRead,
        ),
        if (widget.post.hasContent(allowMedia: widget.showMedia))
          PostCardContent(
            post: widget.post,
            showMedia: widget.showMedia,
            ignoreSelftextSpoiler: widget.ignoreSelftextSpoiler,
            maxLines: widget.maxLines,
          ),
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

class PostCardContent extends StatelessWidget {
  final bool showMedia;
  final Post post;
  final int? maxLines;
  final bool ignoreSelftextSpoiler;
  const PostCardContent({
    super.key,
    required this.post,
    required this.showMedia,
    this.maxLines,
    required this.ignoreSelftextSpoiler,
  });

  @override
  Widget build(BuildContext context) {
    return content(context) ?? SizedBox.shrink();
  }

  Widget? media(BuildContext context) {
    if (!showMedia) {
      return null;
    }
    return switch (post.kind) {
      // Media types
      Kind.video => VideoContent(post: post),
      Kind.streamableVideo => StreamableVideo(post: post),
      Kind.youtubeVideo => YoutubeContent(post: post),
      (Kind.mediaGallery || Kind.gallery) => GalleryView(post: post),
      Kind.image => ImageContent(
          post: post,
        ),
      _ => null,
    };
  }

  Widget? selftext(BuildContext context) {
    if ((!post.spoiler || ignoreSelftextSpoiler) && post.selftextHtml != null) {
      return wrapPostElement(
        HtmlWithConditionalFade(
          htmlContent: post.selftextHtml ?? "",
          maxLines: maxLines,
        ),
      );
    } else {
      return null;
    }
  }

  Widget? content(BuildContext context) {
    final media = this.media(context);
    final selftext = this.selftext(context);

    if (media == null && selftext == null) {
      return null;
    }

    return Column(
      spacing: 8,
      mainAxisSize: MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        if (media != null) media,
        if (selftext != null) selftext,
      ],
    );
  }
}

class PostCardTitle extends StatelessWidget {
  final Post post;
  final bool ignoreRead;
  final bool showMedia;
  final bool enableThumbnail;

  const PostCardTitle({
    super.key,
    required this.post,
    required this.ignoreRead,
    required this.showMedia,
    required this.enableThumbnail,
  });
  @override
  Widget build(BuildContext context) {
    final title = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      spacing: 4,
      children: [
        PostTitle(
          post: post,
          ignoredRead: ignoreRead,
        ),
        PostFlair(post: post),
        PostScore(post: post),
      ],
    );
    if (enableThumbnail && post.kind.showThumbnail(showMedia)) {
      return wrapPostElement(
        Thumbnail(
          post: post,
          child: title,
        ),
      );
    }
    return wrapPostElement(title);
  }
}
