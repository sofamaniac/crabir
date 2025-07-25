import 'dart:math';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:chewie/chewie.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:photo_view/photo_view.dart';
import 'package:video_player/video_player.dart';

part 'image.dart';
part 'gif.dart';
part 'resolution.dart';

/// Display a `RedditImage`
/// It handles every specificities (like if it is a gif, if it should be blurred, ...).
class RedditImageView extends StatelessWidget {
  final RedditImage image;
  final bool blur;
  final Resolution preferredResolution = Resolution.source;
  const RedditImageView({super.key, required this.image, this.blur = false});

  bool _shouldBlur() {
    return blur &&
        (image.variants.obfuscated != null || image.variants.nsfw != null);
  }

  bool _isAnimated() {
    return image.variants.gif != null || image.variants.mp4 != null;
  }

  @override
  Widget build(BuildContext context) {
    if (_shouldBlur()) {
      return Text("blurred image");
    } else if (_isAnimated()) {
      return _GifContent(mp4: image.variants.mp4, gif: image.variants.gif!);
    } else {
      // TODO: resolution
      // TODO: placeholder
      return _ImageContent(image: image.source);
    }
  }
}
