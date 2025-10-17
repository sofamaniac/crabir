import 'package:crabir/cartouche.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/network_status.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
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
          ImageThumbnail.redditImage(
            post.preview!.images[0],
            resolution: Resolution.medium,
          ),
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

class VideoContent extends StatefulWidget {
  final Post post;
  final Resolution resolution;
  const VideoContent({
    super.key,
    required this.post,
    required this.resolution,
  });

  @override
  State<VideoContent> createState() => _VideoContentState();
}

class _VideoContentState extends State<VideoContent> {
  bool tapped = false;
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final media = widget.post.secureMedia;
    final preview = widget.post.preview?.images[0];
    final obfuscate =
        widget.post.spoiler || (FiltersSettings.of(context).blurNSFW);
    if (obfuscate && !tapped) {
      final placeholder = preview!
          .withResolution(NetworkStatus.videoQuality(context), obfuscate);
      return InkWell(
        child: ImageThumbnail(
          imageUrl: placeholder.url,
          width: placeholder.width,
          height: placeholder.height,
        ),
        onTap: () {
          setState(() {
            tapped = true;
          });
        },
      );
    }
    switch (media) {
      case Media_RedditVideo():
        return AnimatedContent.fromRedditVideo(
          media: media,
          placeholderUrl: preview?.resolutions.firstOrNull?.url,
          goFullScreen: goFullScreen(context, widget.post, widget.resolution),
          cartouche: Cartouche(
            "VIDEO",
            background: Colors.orange,
          ),
        );
      case Media_Oembed():
        return AnimatedContent.fromMediaOEmbed(
          media: media,
          placeholderUrl: preview?.resolutions.firstOrNull?.url,
          goFullScreen: goFullScreen(context, widget.post, widget.resolution),
          cartouche: Cartouche(
            "VIDEO",
            background: Colors.orange,
          ),
        );
      case null when preview?.variants.mp4 != null:
        final image = preview!.variants.mp4!.withResolution(widget.resolution);
        return AnimatedContent.fromImageBase(
          image: image,
          placeholderUrl: preview.resolutions.firstOrNull?.url,
          goFullScreen: goFullScreen(context, widget.post, widget.resolution),
          cartouche: Cartouche(
            "VIDEO",
            background: Colors.orange,
          ),
        );
      case null when preview?.variants.gif != null:
        final image = preview!.variants.gif!.withResolution(widget.resolution);
        return AnimatedContent.fromImageBase(
          image: image,
          placeholderUrl: preview.resolutions.firstOrNull?.url,
          goFullScreen: goFullScreen(context, widget.post, widget.resolution),
          cartouche: Cartouche(
            "GIF",
            background: Colors.cyan,
          ),
        );
      default:
        return Center(child: Text("Error while loading video"));
    }
  }
}

VoidCallback goFullScreen(
    BuildContext context, Post post, Resolution resolution) {
  final String url;
  final int width;
  final int height;
  final media = post.secureMedia;
  final preview = post.preview?.images.firstOrNull;
  switch (media) {
    case Media_RedditVideo():
      url = media.field0.dashUrl;
      width = media.field0.width;
      height = media.field0.height;
    case Media_Oembed():
      url = media.oembed.providerUrl;
      width = media.oembed.width;
      height = media.oembed.height;
    case null when preview?.variants.mp4 != null:
      final image = preview!.variants.mp4!.withResolution(resolution);
      url = image.url;
      width = image.width;
      height = image.height;
    case null when preview?.variants.gif != null:
      final image = preview!.variants.gif!.withResolution(resolution);
      url = image.url;
      width = image.width;
      height = image.height;
    default:
      return () {};
  }
  return () => FullscreenVideoView(
        videoUrl: url,
        width: width,
        height: height,
        post: post,
      ).pushNamed(context);
}
