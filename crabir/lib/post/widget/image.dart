import 'dart:math';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:photo_view/photo_view.dart';

class ImageContent extends StatelessWidget {
  final Post post;
  const ImageContent({super.key, required this.post});
  @override
  Widget build(BuildContext context) {
    if (post.preview != null) {
      return RedditImageView(image: post.preview!.images[0]);
    } else {
      return CachedNetworkImage(
        imageUrl: post.url,
        // No fade out
        fadeOutDuration: Duration(seconds: 0),
        fadeInDuration: Duration(seconds: 0),
        placeholderFadeInDuration: Duration(seconds: 0),
      );
    }
  }
}

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

