part of 'media.dart';

class AnimatedContentController {
  static ValueNotifier<String?> currentlyPlaying = ValueNotifier(null);
}

class AnimatedContent extends StatefulWidget {
  final String? placeholderUrl;
  final Resolution preferredResolution;
  final int width;
  final int height;
  final String url;

  AnimatedContent.fromVariantInner({
    super.key,
    required VariantInner mp4,
    this.placeholderUrl,
    this.preferredResolution = Resolution.source,
  })  : url = mp4.source.url,
        width = mp4.source.width,
        height = mp4.source.height;

  AnimatedContent.fromMediaOEmbed({
    super.key,
    required Media_Oembed media,
    this.placeholderUrl,
    this.preferredResolution = Resolution.source,
  })  : url = media.oembed.providerUrl,
        width = media.oembed.width,
        height = media.oembed.height;

  AnimatedContent.fromRedditVideo({
    super.key,
    required Media_RedditVideo media,
    this.placeholderUrl,
    this.preferredResolution = Resolution.source,
  })  : url = media.field0.dashUrl,
        width = media.field0.width,
        height = media.field0.height;

  const AnimatedContent({
    super.key,
    required this.url,
    required this.width,
    required this.height,
    this.placeholderUrl,
    this.preferredResolution = Resolution.source,
  });
  @override
  State<AnimatedContent> createState() => _AnimatedContentState();
}

class _AnimatedContentState extends State<AnimatedContent> {
  late final VideoPlayerController _controller;
  late final ChewieController _chewieController;

  /// Stores all registered videos
  static final Map<String, _AnimatedContentState> _allVideos = {};

  ScrollController? _scrollController;

  @override
  void initState() {
    super.initState();

    _controller = VideoPlayerController.networkUrl(Uri.parse(widget.url));
    _controller.initialize().then((_) {
      setState(() {});
      _updateGlobalPlayState();
    });

    _chewieController = ChewieController(
      videoPlayerController: _controller,
      autoPlay: false, // manual control
      looping: true,
      autoInitialize: true,
      showControlsOnInitialize: false,
      aspectRatio: widget.width / widget.height,
      placeholder: placeholder(),
    );

    // Register this video
    _allVideos[widget.url] = this;

    // Attach scroll listener
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _scrollController = Scrollable.of(context).widget.controller;
      _scrollController ??= PrimaryScrollController.of(context);
      _scrollController?.addListener(_onScroll);
      _onScroll(); // initial check
    });

    AnimatedContentController.currentlyPlaying
        .addListener(_updateGlobalPlayState);
  }

  Widget placeholder() => widget.placeholderUrl != null
      ? Image.network(widget.placeholderUrl!)
      : const SizedBox.shrink();

  void _updateGlobalPlayState() {
    if (AnimatedContentController.currentlyPlaying.value == widget.url) {
      if (!_controller.value.isPlaying) _controller.play();
    } else {
      if (_controller.value.isPlaying) _controller.pause();
    }
  }

  void _onScroll() {
    final screenSize = MediaQuery.of(context).size;
    final bottomBarHeight = kBottomNavigationBarHeight;
    final screenCenterY = (screenSize.height - bottomBarHeight) / 2;
    final screenCenterX = screenSize.width / 2;

    String? bestUrl;
    double bestScore = double.infinity;

    for (final entry in _allVideos.entries) {
      final videoState = entry.value;
      final renderObject = videoState.context.findRenderObject();
      if (renderObject is! RenderBox || !renderObject.hasSize) continue;

      final size = renderObject.size;
      final topLeftGlobal = renderObject.localToGlobal(Offset.zero);

      // Compute visible fraction
      final visibleTop = topLeftGlobal.dy.clamp(0, screenSize.height);
      final visibleBottom =
          (topLeftGlobal.dy + size.height).clamp(0, screenSize.height);
      final visibleHeight = visibleBottom - visibleTop;
      final visibleFraction = visibleHeight / size.height;

      if (visibleFraction < 0.5) continue; // skip mostly hidden videos

      // Compute distance from screen center
      final videoCenterY = topLeftGlobal.dy + size.height / 2;
      final videoCenterX = topLeftGlobal.dx + size.width / 2;
      final distance = (videoCenterY - screenCenterY).abs() +
          (videoCenterX - screenCenterX).abs();

      // score = distance / fraction
      final score = distance / visibleFraction;

      if (score < bestScore) {
        bestScore = score;
        bestUrl = entry.key;
      }
    }

    if (AnimatedContentController.currentlyPlaying.value != bestUrl) {
      AnimatedContentController.currentlyPlaying.value = bestUrl;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: AspectRatio(
        aspectRatio: widget.width / widget.height,
        child: Chewie(
          key: ValueKey(widget.url),
          controller: _chewieController,
        ),
      ),
    );
  }

  @override
  void dispose() {
    AnimatedContentController.currentlyPlaying
        .removeListener(_updateGlobalPlayState);
    _scrollController?.removeListener(_onScroll);
    _allVideos.remove(widget.url);

    _controller.dispose();
    _chewieController.dispose();
    super.dispose();
  }
}
