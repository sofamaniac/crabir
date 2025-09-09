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
          ImageThumbnail.redditImage(post.preview!.images[0]),
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
    final preview = post.preview?.images[0];
    return switch (media) {
      Media_RedditVideo() => AnimatedContent.fromRedditVideo(
          media: media,
          placeholderUrl: preview?.resolutions[0].url,
        ),
      Media_Oembed() => AnimatedContent.fromMediaOEmbed(
          media: media,
          placeholderUrl: preview?.resolutions[0].url,
        ),
      null when preview?.variants.mp4 != null =>
        AnimatedContent.fromVariantInner(
          mp4: preview!.variants.mp4!,
          placeholderUrl: preview.resolutions[0].url,
        ),
      null when preview?.variants.gif != null =>
        AnimatedContent.fromVariantInner(
          mp4: preview!.variants.gif!,
          placeholderUrl: preview.resolutions[0].url,
        ),
      _ => Center(child: Text("Error while loading video")),
    };
  }
}
