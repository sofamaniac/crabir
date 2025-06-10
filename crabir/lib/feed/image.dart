import 'dart:math';

import 'package:flutter/material.dart';

class ImageThumbnail extends StatelessWidget {
  final String url;
  final String? thumbnailUrl;
  final double width;
  final double height;

  const ImageThumbnail({
    super.key,
    required this.url,
    required this.width,
    required this.height,
    this.thumbnailUrl,
  });

  Widget thumbnail() {
    if (thumbnailUrl != null) {
      return Image.network(
        thumbnailUrl!,
        width: double.infinity,
        fit: BoxFit.fitWidth,
      );
    } else {
      return Container();
    }
  }

  @override
  Widget build(BuildContext context) {
    return AspectRatio(
      aspectRatio: width / max(1, height),
      child: Center(
        child: Image.network(
          url,
          width: double.infinity,
          fit: BoxFit.fitWidth,
          frameBuilder: (context, child, frame, _) {
            if (frame != null) {
              return child;
            } else {
              return thumbnail();
            }
          },
        ),
      ),
    );
  }
}
