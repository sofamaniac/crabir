import 'package:crabir/media/media.dart';
import 'package:crabir/network_status.dart';
import 'package:crabir/post/parts/video.dart';
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
  bool tapped = false;
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final obfuscate = widget.post.spoiler ||
        (widget.post.over18 &&
            context.watch<FiltersSettingsCubit>().state.blurNSFW);
    final preview = widget.post.preview;
    if (preview == null) {
      return InkWell(
        onTap: () => FullscreenImageView(
          imageUrl: widget.post.url,
          post: widget.post,
        ).pushNamed(context),
        child: ImageThumbnail(
          imageUrl: widget.post.url,
        ),
      );
    } else {
      final resolution = NetworkStatus.imageQuality(context);

      if (preview.images.firstOrNull?.variants.mp4 != null) {
        return VideoContent(
          post: widget.post,
          resolution: resolution,
        );
      }
      final RedditImage image = preview.images[0];
      return InkWell(
        onTap: () {
          if (!obfuscate || tapped) {
            FullscreenImageView(
              imageUrl: image
                  .withResolution(
                    resolution,
                    false,
                  )
                  .url,
              post: widget.post,
            ).pushNamed(context);
          } else {
            setState(() {
              tapped = true;
            });
          }
        },
        child: ImageThumbnail.redditImage(
          image,
          blur: obfuscate && !tapped,
          resolution: resolution,
        ),
      );
    }
  }
}
