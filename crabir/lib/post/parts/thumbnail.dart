part of '../post.dart';

class Thumbnail extends StatelessWidget {
  final Post post;
  final Widget child;

  const Thumbnail({super.key, required this.post, required this.child});

  void onTap(BuildContext context) async {
    switch (post.kind) {
      case Kind.link:
      case Kind.unknown:
      case Kind.selftext:
      case Kind.meta:
      case Kind.image:
      case Kind.youtubeVideo:
        await launchUrl(Uri.parse(post.url));
        break;
      case Kind.video:
        final settings = context.read<DataSettingsCubit>().state;
        goFullScreen(context, post, settings.videoQuality);
      case Kind.streamableVideo:
        throw Exception("TODO: fullscreen streamable.com");
      case Kind.gallery:
      case Kind.mediaGallery:
        final gallery =
            post.gallery ?? post.crosspostParentList.firstOrNull?.gallery;
        if (gallery != null) {
          context.router.navigate(
            FullScreenGalleryRoute(gallery: gallery, initialPage: 0),
          );
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
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      spacing: 8,
      children: [
        Expanded(
          flex: 3,
          child: child,
        ),
        InkWell(
          onTap: () => onTap(context),
          child: thumbnail(),
        ),
      ],
    );
  }
}
