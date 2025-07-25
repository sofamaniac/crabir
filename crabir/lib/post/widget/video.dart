import 'package:chewie/chewie.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/widgets.dart';
import 'package:video_player/video_player.dart';

class YoutubeContent extends StatelessWidget {
  final Post post;
  const YoutubeContent({super.key, required this.post});
  @override
  Widget build(BuildContext context) {
    return Text("A Youtube Video");
  }
}

class VideoContent extends StatefulWidget {
  final Post post;
  const VideoContent({super.key, required this.post});

  @override
  State<VideoContent> createState() => _VideoContentState();
}

class _VideoContentState extends State<VideoContent> {
  late VideoPlayerController _controller;
  late ChewieController _chewieController;
  late int width;
  late int height;

  @override
  void initState() {
    super.initState();
    final String url;
    switch (widget.post.secureMedia) {
      case null:
        url = "";
        width = 1;
        height = 1;
        break;
      case Media_RedditVideo():
        final video = (widget.post.secureMedia! as Media_RedditVideo);
        url = video.field0.fallbackUrl;
        width = video.field0.width.toInt();
        height = video.field0.height.toInt();
        break;
      case Media_Oembed():
        final video = (widget.post.secureMedia! as Media_Oembed);
        url = video.oembed.providerUrl;
        width = video.oembed.width.toInt();
        height = video.oembed.height.toInt();
        break;
    }
    _controller = VideoPlayerController.networkUrl(Uri.parse(url));
    //_controller.initialize();
    _chewieController = ChewieController(
      videoPlayerController: _controller,
      autoPlay: true,
      looping: true,
      aspectRatio: width / height,
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
