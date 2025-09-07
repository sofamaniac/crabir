import 'package:auto_route/auto_route.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/post/widget/video.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/data/data_settings.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class ImageContent extends StatefulWidget {
  final Post post;
  const ImageContent({
    super.key,
    required this.post,
  });

  @override
  State<ImageContent> createState() => _ImageContentState();
}

class _ImageContentState extends State<ImageContent> {
  late bool obfuscate;
  @override
  void initState() {
    super.initState();
    obfuscate = widget.post.spoiler ||
        (widget.post.over18 &&
            context.read<FiltersSettingsCubit>().state.blurNSFW);
  }

  @override
  Widget build(BuildContext context) {
    if (widget.post.preview == null) {
      return InkWell(
        onTap: () => context.router.navigate(
          FullscreenImageRoute(imageUrl: widget.post.url),
        ),
        child: ImageThumbnail(
          imageUrl: widget.post.url,
        ),
      );
    } else {
      if (widget.post.preview!.images[0].variants.mp4 != null) {
        // TODO: resolution
        return VideoContent(post: widget.post);
      }
      final settings = context.read<DataSettingsCubit>().state;
      final RedditImage image = widget.post.preview!.images[0];
      return InkWell(
        onTap: () {
          if (!obfuscate) {
            context.router.navigate(
              // TODO: resolution
              FullscreenImageRoute(imageUrl: image.source.url),
            );
          } else {
            setState(() {
              obfuscate = false;
            });
          }
        },
        child: ImageThumbnail.redditImage(
          image,
          blur: obfuscate,
          resolution: settings.preferredQuality,
        ),
      );
    }
  }
}
