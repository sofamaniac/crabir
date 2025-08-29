part of 'media.dart';

/// Display the image at `url` and `placeholderUrl` while the image is loading.
class ImageThumbnail extends StatelessWidget {
  final String? placeholderUrl;
  //final ImageBase image;
  final String? title;
  final String imageUrl;
  final int? width;
  final int? height;

  /// Maximum number of lines of the title to show.
  final int maxLines;

  const ImageThumbnail({
    super.key,
    required this.imageUrl,
    required this.maxLines,
    //required this.image,
    this.placeholderUrl,
    this.title,
    this.width,
    this.height,
  });

  static ImageThumbnail redditImage(
    RedditImage image, {
    Resolution resolution = Resolution.source,
    bool blur = false,
    String? title,
    required int maxLines,
  }) {
    final length = image.resolutions.length;
    final ImageBase imageBase = switch (resolution) {
      Resolution.source => image.source,
      Resolution.high => image.resolutions[length - 2],
      Resolution.medium => image.resolutions[(length / 2).toInt()],
      Resolution.low => image.resolutions[0],
    };
    return ImageThumbnail(
      imageUrl: imageBase.url,
      width: imageBase.width,
      height: imageBase.height,
      title: title,
      maxLines: maxLines,
    );
  }

  static ImageThumbnail imageBase(
    ImageBase image, {
    String? title,
    required int maxLines,
  }) {
    return ImageThumbnail(
      imageUrl: image.url,
      width: image.width,
      height: image.height,
      title: title,
      maxLines: maxLines,
    );
  }

  static ImageThumbnail fromGalleryImage(
    gallery.Image image, {
    int maxLines = 2,
  }) {
    return ImageThumbnail(
      imageUrl: image.u,
      width: image.x,
      height: image.y,
      maxLines: maxLines,
    );
  }

  Widget _thumbnail(BuildContext context, String url) {
    if (placeholderUrl != null) {
      return Image.network(
        placeholderUrl!,
        width: double.infinity,
        fit: BoxFit.fitWidth,
      );
    } else {
      return LoadingIndicator();
    }
  }

  @override
  Widget build(BuildContext context) {
    return AspectRatio(
      aspectRatio: (width ?? 1) / max(1, (height ?? 1)),
      child: Column(
        children: [
          Center(
            child: CachedNetworkImage(
              imageUrl: imageUrl,
              placeholder: _thumbnail,
              // No fade out
              fadeOutDuration: Duration(seconds: 0),
              fadeInDuration: Duration(seconds: 0),
              placeholderFadeInDuration: Duration(seconds: 0),
            ),
          ),
          if (title != null && title!.isNotEmpty)
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 8),
              child: Text(
                title!,
                maxLines: maxLines,
                overflow: TextOverflow.ellipsis,
              ),
            ),
        ],
      ),
    );
  }
}

@RoutePage()
class FullscreenImageView extends StatelessWidget {
  final String imageUrl;
  final String? title;
  const FullscreenImageView({super.key, required this.imageUrl, this.title});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      backgroundColor: Colors.black,
      body: Dismissible(
        key: UniqueKey(),
        direction: DismissDirection.vertical,
        onDismissed: (_) => context.pop(),
        child: PhotoViewGestureDetectorScope(
          axis: Axis.vertical,
          child: PhotoView(
            imageProvider: NetworkImage(imageUrl),
            minScale: PhotoViewComputedScale.contained * 1.0,
            maxScale: PhotoViewComputedScale.contained * 5.0,
          ),
        ),
      ),
    );
  }
}
