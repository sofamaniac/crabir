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
  final Resolution resolution;
  const VideoContent(
      {super.key, required this.post, this.resolution = Resolution.source});
  @override
  Widget build(BuildContext context) {
    final media = post.secureMedia;
    final preview = post.preview?.images[0];
    switch (media) {
      case Media_RedditVideo():
        return AnimatedContent.fromRedditVideo(
          media: media,
          placeholderUrl: preview?.resolutions[0].url,
        );
      case Media_Oembed():
        return AnimatedContent.fromMediaOEmbed(
          media: media,
          placeholderUrl: preview?.resolutions[0].url,
        );
      case null when preview?.variants.mp4 != null:
        return AnimatedContent.fromImageBase(
          image: preview!.variants.mp4!.withResolution(resolution),
          placeholderUrl: preview.resolutions[0].url,
        );
      case null when preview?.variants.gif != null:
        return AnimatedContent.fromImageBase(
          image: preview!.variants.gif!.withResolution(resolution),
          placeholderUrl: preview.resolutions[0].url,
        );
      default:
        return Center(child: Text("Error while loading video"));
    }
  }
}
