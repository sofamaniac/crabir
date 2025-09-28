import 'package:auto_route/auto_route.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/data/data_settings.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
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

class VideoContent extends StatefulWidget {
  final Post post;
  final Resolution resolution;
  const VideoContent({
    super.key,
    required this.post,
    this.resolution = Resolution.source,
  });

  @override
  State<VideoContent> createState() => _VideoContentState();
}

class _VideoContentState extends State<VideoContent> {
  late bool obfuscate;
  @override
  void initState() {
    super.initState();
    obfuscate = widget.post.spoiler ||
        (context.read<FiltersSettingsCubit>().state.blurNSFW &&
            widget.post.over18);
  }

  @override
  Widget build(BuildContext context) {
    final media = widget.post.secureMedia;
    final preview = widget.post.preview?.images[0];
    final settings = context.read<DataSettingsCubit>().state;
    if (obfuscate) {
      final placeholder =
          preview!.withResolution(settings.preferredQuality, obfuscate);
      return InkWell(
        child: ImageThumbnail(
          imageUrl: placeholder.url,
          width: placeholder.width,
          height: placeholder.height,
        ),
        onTap: () {
          setState(() {
            obfuscate = false;
          });
        },
      );
    }
    switch (media) {
      case Media_RedditVideo():
        return AnimatedContent.fromRedditVideo(
          media: media,
          placeholderUrl: preview?.resolutions[0].url,
          goFullScreen: goFullScreen(context, widget.post, widget.resolution),
        );
      case Media_Oembed():
        return AnimatedContent.fromMediaOEmbed(
          media: media,
          placeholderUrl: preview?.resolutions[0].url,
          goFullScreen: goFullScreen(context, widget.post, widget.resolution),
        );
      case null when preview?.variants.mp4 != null:
        final image = preview!.variants.mp4!.withResolution(widget.resolution);
        return AnimatedContent.fromImageBase(
          image: image,
          placeholderUrl: preview.resolutions[0].url,
          goFullScreen: goFullScreen(context, widget.post, widget.resolution),
        );
      case null when preview?.variants.gif != null:
        final image = preview!.variants.gif!.withResolution(widget.resolution);
        return AnimatedContent.fromImageBase(
          image: image,
          placeholderUrl: preview.resolutions[0].url,
          goFullScreen: goFullScreen(context, widget.post, widget.resolution),
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
  final preview = post.preview?.images[0];
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
  return () => context.router.navigate(
        FullscreenVideoRoute(
          videoUrl: url,
          width: width,
          height: height,
          post: post,
        ),
      );
}
