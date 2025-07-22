import 'package:chewie/chewie.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';

class GifContent extends StatefulWidget {
  final Post post;
  const GifContent({super.key, required this.post});

  @override
  State<GifContent> createState() => _GifContentState();
}

class _GifContentState extends State<GifContent> {
  late VideoPlayerController _controller;
  late ChewieController _chewieController;
  int width = 1;
  int height = 1;

  Widget placeholder() {
    if (widget.post.thumbnail?.url != null) {
      return Image.network(widget.post.thumbnail!.url);
    } else {
      return Container();
    }
  }

  @override
  void initState() {
    super.initState();
    final source = widget.post.preview!.images[0].variants.mp4!.source;
    final url = source.url;
    width = source.width;
    height = source.height;
    _controller = VideoPlayerController.networkUrl(Uri.parse(url));
    _chewieController = ChewieController(
      videoPlayerController: _controller,
      autoPlay: true,
      looping: true,
      aspectRatio: width / height,
      placeholder: placeholder(),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: AspectRatio(
        aspectRatio: width / height,
        child: Chewie(
          controller: _chewieController,
        ),
      ),
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    _chewieController.dispose();
    super.dispose();
  }
}
