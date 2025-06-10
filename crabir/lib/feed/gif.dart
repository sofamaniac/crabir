import 'dart:math';

import 'package:crabir/feed/image.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';
import 'package:gif_view/gif_view.dart';

class GifContentFullscreen extends StatefulWidget {
  final Post post;
  const GifContentFullscreen({super.key, required this.post});

  @override
  State<StatefulWidget> createState() => _GifContentFullscreenState();
}

class _GifContentFullscreenState extends State<GifContentFullscreen>
    with SingleTickerProviderStateMixin {
  late GifController _controller;
  bool _isCenter = false;
  late final Ticker _ticker;
  int framerate = 1;
  int lastIndex = 0;
  int index = 0;
  int lastEllapsed = 0;
  int dampen = 0;
  int currentFrame = 0;
  int totalFrames = 1;

  @override
  void initState() {
    super.initState();
    _controller = GifController();
    _ticker = createTicker((ellapsed) {
      if (lastIndex != index) {
        final frametime = ellapsed.inMilliseconds - lastEllapsed;
        framerate =
            ((framerate * dampen + (1000 / max(frametime, 1))) / (dampen + 1))
                .round();
        // Clamp framerate to 60 fps
        framerate = framerate.clamp(1, 60);
        lastIndex = index;
        lastEllapsed = ellapsed.inMilliseconds;
        dampen += 1;
      }
      final newIsCenter = _shouldPlay(context);
      if (newIsCenter != _isCenter) {
        setState(() {
          _isCenter = _shouldPlay(context);
        });
      }
    });
    _ticker.start();
  }

  @override
  void dispose() {
    _controller.dispose();
    _ticker.dispose();
    super.dispose();
  }

  /// return true if the gif should play.
  /// Currently check if the gif is fully visible on screen.
  bool _shouldPlay(BuildContext context) {
    final boxOption = context.findRenderObject();
    if (boxOption == null) {
      return false;
    }
    final box = boxOption as RenderBox;
    final screenHeight = MediaQuery.of(context).size.height;
    final top = box.localToGlobal(Offset.zero).dy;
    final bottom = top + box.size.height;

    return top > 0 && bottom < screenHeight;
  }

  Widget thumbnail(BuildContext context) {
    final thumbnail = widget.post.thumbnail;
    if (thumbnail == null) {
      return const CircularProgressIndicator();
    } else {
      return Image.network(
        thumbnail.url,
        width: double.infinity,
        fit: BoxFit.fitWidth,
      );
    }
  }

  Widget timestamp(Duration time) {
    String twoDigits(int n) => n.toString().padLeft(2, "0");
    return Text(
      "${twoDigits(time.inMinutes)}:${twoDigits(time.inSeconds)}",
      style: TextStyle(backgroundColor: Colors.black45),
    );
  }

  @override
  Widget build(BuildContext context) {
    // If the post is an image, the url should point to the image.
    final imageUrl = widget.post.url;
    // We look into the previews to get the source size
    // It does not need to match exactly as it is used to
    // prevent posts from jumping around when loading the image.
    final image = widget.post.preview!.images[0].source;

    if (_isCenter) {
      final gif = GifView.network(
        imageUrl,
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
        controller: _controller,
        onFrame: (value) => setState(() {
          index = value;
        }),
        onLoaded: (value) => totalFrames = value,
        progressBuilder: (_) => ImageThumbnail(
          url: image.url,
          width: image.width.toDouble(),
          height: image.height.toDouble(),
        ),
        errorBuilder: (context, error, f) => Text("$error"),
      );
      final remainingTime =
          Duration(seconds: ((totalFrames - index) / framerate).toInt());
      return AspectRatio(
        aspectRatio: image.width.toDouble() / image.height.toDouble(),
        child: Center(
          child: Stack(
            children: [
              gif,
              Align(
                alignment: Alignment.bottomCenter,
                child: timestamp(remainingTime),
              ),
              Align(
                alignment: Alignment.bottomLeft,
                child: LinearProgressIndicator(
                  value: index / max(1, totalFrames),
                ),
              ),
            ],
          ),
        ),
      );
    } else {
      return ImageThumbnail(
        url: image.url,
        width: image.width.toDouble(),
        height: image.height.toDouble(),
      );
    }
  }
}
