import 'package:crabir/cartouche.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher_string.dart';

class YoutubeContent extends StatelessWidget {
  final Post post;
  const YoutubeContent({super.key, required this.post});
  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () => launchUrlString(post.url),
      child: Stack(
        children: [
          ImageThumbnail.redditImage(post.preview!.images[0], maxLines: 2),
          Positioned(
            top: 8,
            right: 8,
            child: Cartouche(
              "YOUTUBE",
              background: Colors.red,
            ),
          ),
        ],
      ),
    );
  }
}

class VideoContent extends StatelessWidget {
  final Post post;
  const VideoContent({super.key, required this.post});
  @override
  Widget build(BuildContext context) {
    final media = post.secureMedia;
    return switch (media) {
      Media_RedditVideo() => AnimatedContent.fromRedditVideo(media: media),
      Media_Oembed() => AnimatedContent.fromMediaOEmbed(media: media),
      null when post.preview?.images[0].variants.mp4 != null =>
        AnimatedContent.fromVariantInner(
          mp4: post.preview!.images[0].variants.mp4!,
        ),
      null when post.preview?.images[0].variants.gif != null =>
        AnimatedContent.fromVariantInner(
          mp4: post.preview!.images[0].variants.gif!,
        ),
      _ => Center(child: Text("Error while loading video")),
    };
  }
}
