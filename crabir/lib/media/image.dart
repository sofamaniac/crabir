part of 'media.dart';

/// Display the image at `url` and `placeholderUrl` while the image is loading.
class ImageThumbnail extends StatelessWidget {
  final String? placeholderUrl;
  //final ImageBase image;
  final String imageUrl;
  final int? width;
  final int? height;

  const ImageThumbnail({
    super.key,
    required this.imageUrl,
    this.placeholderUrl,
    this.width,
    this.height,
  });

  factory ImageThumbnail.redditImage(
    RedditImage image, {
    Resolution resolution = Resolution.source,
    bool blur = false,
  }) {
    final variants = image.variants.nsfw ?? image.variants.obfuscated;
    final List<ImageBase> resolutions;
    final ImageBase source;
    if (variants != null && blur) {
      resolutions = variants.resolutions;
      source = variants.source;
    } else {
      resolutions = image.resolutions;
      source = image.source;
    }
    final length = image.resolutions.length;
    final ImageBase imageBase = switch (resolution) {
      Resolution.source => source,
      Resolution.high => resolutions[length - 2],
      Resolution.medium => resolutions[(length / 2).toInt()],
      Resolution.low => resolutions[0],
    };
    return ImageThumbnail(
      imageUrl: imageBase.url,
      width: imageBase.width,
      height: imageBase.height,
    );
  }

  static ImageThumbnail imageBase(
    ImageBase image,
  ) {
    return ImageThumbnail(
      imageUrl: image.url,
      width: image.width,
      height: image.height,
    );
  }

  static ImageThumbnail fromGalleryImage(
    gallery.Image image, {
    String? placeholderUrl,
  }) {
    return ImageThumbnail(
      imageUrl: image.u,
      width: image.x,
      height: image.y,
      placeholderUrl: placeholderUrl,
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
      child: CachedNetworkImage(
        imageUrl: imageUrl,
        placeholder: _thumbnail,
        // No fade out
        fadeOutDuration: Duration(seconds: 0),
        fadeInDuration: Duration(seconds: 0),
        placeholderFadeInDuration: Duration(seconds: 0),
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
    return FullscreenMediaView(
      builder: (onTap) => PhotoView(
        imageProvider: CachedNetworkImageProvider(imageUrl),
        minScale: PhotoViewComputedScale.contained * 1.0,
        maxScale: PhotoViewComputedScale.contained * 5.0,
        onTapDown: onTap,
      ),
    );
  }
}
