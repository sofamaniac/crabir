part of 'media.dart';

class AnimatedContentController {
  static ValueNotifier<String?> currentlyPlaying = ValueNotifier(null);
}

class AnimatedContent extends StatefulWidget {
  final String url;
  final int width;
  final int height;
  final String? placeholderUrl;
  final Resolution preferredResolution;

  /// If provided, overrides global feed logic.
  /// Useful for carousels where PageView knows the active page.
  final bool? shouldPlay;

  const AnimatedContent({
    super.key,
    required this.url,
    required this.width,
    required this.height,
    this.placeholderUrl,
    this.preferredResolution = Resolution.source,
    this.shouldPlay,
  });

  // convenience constructors…
  AnimatedContent.fromVariantInner({
    super.key,
    required VariantInner mp4,
    this.placeholderUrl,
    this.preferredResolution = Resolution.source,
    this.shouldPlay,
  })  : url = mp4.source.url,
        width = mp4.source.width,
        height = mp4.source.height;

  AnimatedContent.fromMediaOEmbed({
    super.key,
    required Media_Oembed media,
    this.placeholderUrl,
    this.preferredResolution = Resolution.source,
    this.shouldPlay,
  })  : url = media.oembed.providerUrl,
        width = media.oembed.width,
        height = media.oembed.height;

  AnimatedContent.fromRedditVideo({
    super.key,
    required Media_RedditVideo media,
    this.placeholderUrl,
    this.preferredResolution = Resolution.source,
    this.shouldPlay,
  })  : url = media.field0.dashUrl,
        width = media.field0.width,
        height = media.field0.height;

  @override
  State<AnimatedContent> createState() => _AnimatedContentState();
}

class _AnimatedContentState extends State<AnimatedContent> {
  VideoPlayerController? _controller;
  bool _isInitialized = false;
  bool _showControls = false;
  bool _muted = false;

  Timer? _hideControlsTimer;
  Timer? _debounceTimer;

  // Stores all registered videos in feed mode
  static final Map<String, _AnimatedContentState> _allVideos = {};

  ScrollController? _scrollController;

  bool get _isCarouselMode => widget.shouldPlay != null;

  @override
  void initState() {
    super.initState();
    _allVideos[widget.url] = this;

    if (_isCarouselMode) {
      // Carousel mode: initialize only if shouldPlay is true
      if (widget.shouldPlay == true) _initialize();
    } else {
      // Feed mode: attach scroll listener
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _scrollController = Scrollable.of(context).widget.controller ??
            PrimaryScrollController.of(context);
        _scrollController?.addListener(_onScroll);
        _onScroll();
      });
      AnimatedContentController.currentlyPlaying
          .addListener(_updateGlobalPlayState);
    }
  }

  Future<void> _initialize() async {
    if (_isInitialized) return;

    _disposeControllers();

    _controller = VideoPlayerController.network(widget.url);
    await _controller!.initialize();
    _controller!.setLooping(true);

    // Play based on mode
    if (_isCarouselMode ? widget.shouldPlay! : _isActiveFeedVideo()) {
      _controller!.play();
    }

    if (!mounted) return;
    setState(() => _isInitialized = true);
  }

  bool _isActiveFeedVideo() =>
      AnimatedContentController.currentlyPlaying.value == widget.url;

  void _updateGlobalPlayState() {
    if (_isCarouselMode) return;

    if (_isActiveFeedVideo()) {
      _initialize();
      _controller?.play();
    } else {
      _controller?.pause();
      _disposeControllers();
      if (mounted) setState(() {});
    }
  }

  void _onScroll() {
    if (_isCarouselMode) return;

    _debounceTimer?.cancel();
    _debounceTimer = Timer(const Duration(milliseconds: 200), () {
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

        final visibleTop = topLeftGlobal.dy.clamp(0, screenSize.height);
        final visibleBottom =
            (topLeftGlobal.dy + size.height).clamp(0, screenSize.height);
        final visibleHeight = visibleBottom - visibleTop;
        final visibleFraction = visibleHeight / size.height;

        if (visibleFraction < 0.5) continue;

        final videoCenterY = topLeftGlobal.dy + size.height / 2;
        final videoCenterX = topLeftGlobal.dx + size.width / 2;
        final distance = (videoCenterY - screenCenterY).abs() +
            (videoCenterX - screenCenterX).abs();

        final score = distance / visibleFraction;
        if (score < bestScore) {
          bestScore = score;
          bestUrl = entry.key;
        }
      }

      if (AnimatedContentController.currentlyPlaying.value != bestUrl) {
        AnimatedContentController.currentlyPlaying.value = bestUrl;
      }
    });
  }

  void _toggleControls() {
    if (mounted) setState(() => _showControls = !_showControls);
    _hideControlsTimer?.cancel();
    if (_showControls) {
      _hideControlsTimer = Timer(const Duration(seconds: 3), () {
        if (mounted) setState(() => _showControls = false);
      });
    }
  }

  void _toggleMute() {
    if (mounted)
      setState(() {
        _muted = !_muted;
        _controller!.setVolume(_muted ? 0 : 1);
      });
  }

  void _disposeControllers() {
    _controller?.dispose();
    _controller = null;
    _isInitialized = false;
  }

  @override
  void didUpdateWidget(covariant AnimatedContent oldWidget) {
    super.didUpdateWidget(oldWidget);

    // Carousel mode: play/pause based on shouldPlay
    if (_isCarouselMode && widget.shouldPlay != oldWidget.shouldPlay) {
      if (widget.shouldPlay == true) {
        _initialize();
        _controller?.play();
      } else {
        _controller?.pause();
        _showControls = false;
      }
    }
  }

  @override
  void dispose() {
    _debounceTimer?.cancel();
    _hideControlsTimer?.cancel();
    _allVideos.remove(widget.url);
    if (!_isCarouselMode) {
      AnimatedContentController.currentlyPlaying
          .removeListener(_updateGlobalPlayState);
      _scrollController?.removeListener(_onScroll);
    }
    _disposeControllers();
    super.dispose();
  }

  Widget placeholder() {
    return Center(
      child: AspectRatio(
        aspectRatio: widget.width / widget.height.toDouble(),
        child: widget.placeholderUrl != null
            ? Image.network(widget.placeholderUrl!)
            : const LoadingIndicator(),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    if (!_isInitialized) {
      return placeholder();
    }
    return GestureDetector(
      behavior: HitTestBehavior.translucent,
      onTap: _toggleControls,
      child: Stack(
        children: [
          Center(
            child: AspectRatio(
              aspectRatio: widget.width / widget.height.toDouble(),
              child: VideoPlayer(_controller!),
            ),
          ),
          Positioned(
            bottom: 8,
            left: 8,
            right: 8,
            child: ValueListenableBuilder<VideoPlayerValue>(
              valueListenable: _controller!,
              builder: (context, value, _) {
                if (_showControls) {
                  return Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: Colors.black45,
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Row(
                      children: [
                        // Play/Pause
                        IconButton(
                          icon: Icon(
                            value.isPlaying ? Icons.pause : Icons.play_arrow,
                            color: Colors.white,
                          ),
                          onPressed: () {
                            if (value.isPlaying) {
                              _controller!.pause();
                            } else {
                              _controller!.play();
                            }
                            setState(() {});
                          },
                        ),
                        // Scrubber
                        Expanded(
                          child: Slider(
                            value: value.position.inSeconds.toDouble(),
                            max: value.duration.inSeconds.toDouble(),
                            onChanged: (newValue) {
                              _controller!
                                  .seekTo(Duration(seconds: newValue.toInt()));
                            },
                            activeColor: Colors.redAccent,
                            inactiveColor: Colors.white30,
                          ),
                        ),
                        // Mute/Unmute
                        IconButton(
                          icon: Icon(
                            _muted ? Icons.volume_off : Icons.volume_up,
                            color: Colors.white,
                          ),
                          onPressed: _toggleMute,
                        ),
                      ],
                    ),
                  );
                } else {
                  // Remaining time overlay
                  final remaining = value.duration - value.position;
                  final minutes = remaining.inMinutes
                      .remainder(60)
                      .toString()
                      .padLeft(2, '0');
                  final seconds = remaining.inSeconds
                      .remainder(60)
                      .toString()
                      .padLeft(2, '0');
                  return Align(
                    alignment: Alignment.bottomRight,
                    child: Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 6, vertical: 2),
                      decoration: BoxDecoration(
                        color: Colors.black54,
                        borderRadius: BorderRadius.circular(4),
                      ),
                      child: Text(
                        '$minutes:$seconds',
                        style:
                            const TextStyle(color: Colors.white, fontSize: 12),
                      ),
                    ),
                  );
                }
              },
            ),
          ),
        ],
      ),
    );
  }
}
