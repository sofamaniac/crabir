part of '../post.dart';

class Thumbnail extends StatelessWidget {
  final Post post;
  final Widget child;

  const Thumbnail({super.key, required this.post, required this.child});

  Widget thumbnail() {
    final defaultThumbnail = ColoredBox(
      color: Colors.grey,
      child: Center(
        child: Icon(Icons.link),
      ),
    );

    if (post.thumbnail != null) {
      return SizedBox(
        width: 72,
        child: AspectRatio(
          aspectRatio: 1.0,
          child: ClipRRect(
            borderRadius: BorderRadiusGeometry.circular(8),
            child: CachedNetworkImage(
              imageUrl: post.thumbnail!.url,
              fit: BoxFit.cover,
              errorWidget: (_, __, ___) => defaultThumbnail,
            ),
          ),
        ),
      );
    } else {
      return Expanded(
        flex: 1,
        child: AspectRatio(
          aspectRatio: 1.0,
          child: ClipRect(
            child: defaultThumbnail,
          ),
        ),
      );
    }
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
        thumbnail(),
      ],
    );
  }
}
