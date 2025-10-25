part of 'post.dart';

class RedditPostCard extends PostView {
  /// Maximum number of lines of the body to show.
  final int? maxLines;

  /// Whether to show media in full size.
  /// If set to false, show a thumbnail instead (when `enableThumbnail` is true).
  final bool showMedia;

  /// Show navigate to comments button
  final bool showCommentsButton;

  /// Show reply to post button
  final bool showReplyButton;

  const RedditPostCard({
    super.key,
    required super.post,
    super.onLikeCallback,
    super.onSaveCallback,
    super.onHideCallback,
    super.onTap,
    required super.enableThumbnail,
    super.ignoreSelftextSpoiler = false,
    super.ignoreRead = false,
    super.respectHidden = true,
    this.showCommentsButton = true,
    this.showReplyButton = false,
    this.maxLines,
    this.showMedia = true,
  });

  @override
  State<StatefulWidget> createState() => _RedditPostCardState();
}

class _RedditPostCardState extends State<RedditPostCard> {
  Future<void> _onSave(bool save) async {
    await widget.onSaveCallback?.call(save);
    setState(() {});
  }

  Future<void> _onLike(VoteDirection dir) async {
    await widget.onLikeCallback?.call(dir);
    setState(() {});
  }

  void _onHide(bool hidden) async {
    widget.onHideCallback?.call(hidden);
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    if (widget.respectHidden && widget.post.hidden) {
      return SizedBox.shrink();
    }
    final contentWidget = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      spacing: 8,
      children: [
        wrapPostElement(Header(post: widget.post)),
        wrapPostElement(
          PostCardTitle(
            post: widget.post,
            showMedia: widget.showMedia,
            enableThumbnail: widget.enableThumbnail,
            ignoreRead: widget.ignoreRead,
          ),
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
          onHide: _onHide,
          showReplyButton: widget.showReplyButton,
          showCommentsButton: widget.showCommentsButton,
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
    final videoResolution = NetworkStatus.videoQuality(context);
    final obfuscate =
        post.spoiler || (post.over18 && FiltersSettings.of(context).blurNSFW);
    return switch (post.kind) {
      // Media types
      Kind.video => VideoContent(
          post: post,
          resolution: videoResolution,
        ),
      Kind.streamableVideo => StreamableVideo(post: post),
      Kind.youtubeVideo => YoutubeContent(post: post),
      (Kind.mediaGallery || Kind.gallery) => GalleryView(
          post: post,
          obfuscate: obfuscate,
          maxScale: 1,
          blurBackground: true,
        ),
      Kind.image => ImageContent(
          post: post,
        ),
      _ => null,
    };
  }

  Widget? selftext(BuildContext context) {
    if ((!post.spoiler || ignoreSelftextSpoiler) && post.selftextHtml != null) {
      if (maxLines == null) {
        return wrapPostElement(RedditMarkdown(markdown: post.selftext ?? ""));
      }
      return wrapPostElement(
        RedditMarkdownWithOverfow(
          text: post.selftext ?? "",
          maxLines: maxLines,
          fontSize:
              (Theme.of(context).textTheme.bodyMedium?.fontSize ?? 1).toInt(),
          width: MediaQuery.widthOf(context).toInt(),
          style: Theme.of(context).textTheme.bodyMedium!,
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

  /// If set to true and the post has some media content, disable the thumbnail.
  final bool showMedia;

  /// Show the thumbnail (if compatible with showMedia).
  final bool enableThumbnail;

  const PostCardTitle({
    super.key,
    required this.post,
    this.ignoreRead = false,
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
      return Thumbnail(
        post: post,
        child: title,
      );
    }
    return title;
  }
}
