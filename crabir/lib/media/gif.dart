part of 'media.dart';

class _GifContent extends StatefulWidget {
  final VariantInner? mp4;

  /// Used as a fallback if `mp4` is not available.
  final VariantInner gif;
  final String? placeholderUrl;
  final Resolution preferredResolution;

  const _GifContent(
      {super.key,
      required this.mp4,
      required this.gif,
      this.placeholderUrl,
      this.preferredResolution = Resolution.source});
  @override
  State<_GifContent> createState() => _GifContentState();
}

class _GifContentState extends State<_GifContent> {
  late VideoPlayerController _controller;
  late ChewieController _chewieController;
  int width = 1;
  int height = 1;

  Widget placeholder() {
    if (widget.placeholderUrl != null) {
      return Image.network(widget.placeholderUrl!);
    } else {
      return Container();
    }
  }

  @override
  void initState() {
    // TODO: only play when visible
    // TODO: support resolution selection
    super.initState();
    final source = widget.mp4?.source ?? widget.gif.source;
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
