part of 'media.dart';

/// Display the image at `url` and `placeholderUrl` while the image is loading.
class ImageThumbnail extends StatelessWidget {
  final String? placeholderUrl;
  final ImageBase image;

  const ImageThumbnail({
    super.key,
    required this.image,
    this.placeholderUrl,
  });

  Widget thumbnail(BuildContext context, String url) {
    if (placeholderUrl != null) {
      return Image.network(
        placeholderUrl!,
        width: double.infinity,
        fit: BoxFit.fitWidth,
      );
    } else {
      return CircularProgressIndicator();
    }
  }

  @override
  Widget build(BuildContext context) {
    return AspectRatio(
      aspectRatio: image.width / max(1, image.height),
      child: Center(
        child: CachedNetworkImage(
          imageUrl: image.url,
          placeholder: thumbnail,
          // No fade out
          fadeOutDuration: Duration(seconds: 0),
          fadeInDuration: Duration(seconds: 0),
          placeholderFadeInDuration: Duration(seconds: 0),
        ),
      ),
    );
  }
}

/// Display an image and goes fullscreen on tap.
class _ImageContent extends StatelessWidget {
  final ImageBase image;
  final String? placeholderUrl;
  final Widget Function(BuildContext, Post)? fullscreen;
  const _ImageContent({
    super.key,
    required this.image,
    this.placeholderUrl,
    this.fullscreen,
  });

  @override
  Widget build(BuildContext context) {
    late final Future? Function() onTap;
    onTap = () => Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => FullscreenImageView(imageUrl: image.url),
          ),
        );

    return InkWell(
      onTap: onTap,
      child: ImageThumbnail(
        image: image,
        placeholderUrl: placeholderUrl,
      ),
    );
  }
}

class FullscreenImageView extends StatelessWidget {
  final String imageUrl;
  const FullscreenImageView({super.key, required this.imageUrl});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      backgroundColor: Colors.black,
      body: Dismissible(
        key: UniqueKey(),
        direction: DismissDirection.vertical,
        onDismissed: (_) => Navigator.pop(context),
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
