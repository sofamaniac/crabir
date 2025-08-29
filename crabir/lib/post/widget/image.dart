import 'package:auto_route/auto_route.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/post/widget/video.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';

class ImageContent extends StatelessWidget {
  final Post post;
  final int maxLines;
  const ImageContent({
    super.key,
    required this.post,
    this.maxLines = 2,
  });
  @override
  Widget build(BuildContext context) {
    if (post.preview != null) {
      if (post.preview!.images[0].variants.mp4 != null) {
        return VideoContent(post: post);
      }
      // TODO: resolution & blur
      final image = post.preview!.images[0];
      return InkWell(
        onTap: () => context.router.navigate(
          FullscreenImageRoute(imageUrl: image.source.url),
        ),
        child: ImageThumbnail.redditImage(
          image,
          title: post.selftext,
          maxLines: maxLines,
        ),
      );
    } else {
      return InkWell(
        onTap: () => context.router.navigate(
          FullscreenImageRoute(imageUrl: post.url),
        ),
        child: ImageThumbnail(
          imageUrl: post.url,
          maxLines: maxLines,
        ),
      );
    }
  }
}
