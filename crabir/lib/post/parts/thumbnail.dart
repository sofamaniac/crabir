part of '../post.dart';

class Thumbnail extends StatelessWidget {
  final Post post;
  final Widget child;
  final bool goToPostButton;

  const Thumbnail({
    super.key,
    required this.post,
    required this.child,
    this.goToPostButton = true,
  });

  void onTap(BuildContext context) async {
    switch (post.kind) {
      case Kind.link:
      case Kind.unknown:
      case Kind.selftext:
      case Kind.meta:
      case Kind.youtubeVideo:
        await launchUrl(Uri.parse(post.url));
        break;
      case Kind.image:
        FullscreenImageView(
          post: post,
          imageUrl: post.preview?.images[0]
                  .withResolution(NetworkStatus.imageQuality(context), false)
                  .url ??
              post.url,
        ).pushNamed(context);
      case Kind.video:
        goFullScreen(context, post, NetworkStatus.videoQuality(context));
      case Kind.streamableVideo:
        throw Exception("TODO: fullscreen streamable.com");
      case Kind.gallery:
      case Kind.mediaGallery:
        final gallery =
            post.gallery ?? post.crosspostParentList.firstOrNull?.gallery;
        if (gallery != null) {
          FullScreenGalleryView(gallery: gallery, initialPage: 0)
              .goNamed(context);
        }
    }
  }

  Widget thumbnail() {
    final defaultThumbnail = ColoredBox(
      color: Colors.grey,
      child: Center(
        child: Icon(Icons.link),
      ),
    );
    final Widget thumbnailWidget;

    if (post.thumbnail != null) {
      thumbnailWidget = CachedNetworkImage(
        imageUrl: post.thumbnail!.url,
        fit: BoxFit.cover,
        errorWidget: (_, __, ___) => defaultThumbnail,
      );
    } else {
      thumbnailWidget = defaultThumbnail;
    }
    return SizedBox(
      width: 72,
      child: AspectRatio(
        aspectRatio: 1.0,
        child: ClipRRect(
          borderRadius: BorderRadiusGeometry.circular(8),
          child: thumbnailWidget,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final layout = LayoutSettings.of(context);
    final thumbnailWidget = InkWell(
      onTap: () => onTap(context),
      child: thumbnail(),
    );
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      spacing: 8,
      children: [
        if (layout.thumbnailOnLeft) thumbnailWidget,
        Expanded(
          flex: 3,
          child: child,
        ),
        if (!layout.thumbnailOnLeft) thumbnailWidget,
      ],
    );
  }
}
