import 'package:crabir/cartouche.dart';
import 'package:crabir/media/media.dart';
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
  const VideoContent(
      {super.key, required this.post, this.resolution = Resolution.source});

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
        );
      case Media_Oembed():
        return AnimatedContent.fromMediaOEmbed(
          media: media,
          placeholderUrl: preview?.resolutions[0].url,
        );
      case null when preview?.variants.mp4 != null:
        return AnimatedContent.fromImageBase(
          image: preview!.variants.mp4!.withResolution(widget.resolution),
          placeholderUrl: preview.resolutions[0].url,
        );
      case null when preview?.variants.gif != null:
        return AnimatedContent.fromImageBase(
          image: preview!.variants.gif!.withResolution(widget.resolution),
          placeholderUrl: preview.resolutions[0].url,
        );
      default:
        return Center(child: Text("Error while loading video"));
    }
  }
}
