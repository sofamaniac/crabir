part of 'media.dart';

class VideoControllerPool {
  static final _controllers = <String, (int, VideoPlayerController)>{};

  static VideoPlayerController get(String url) {
    final (ref, controller) = _controllers.putIfAbsent(url, () {
      print("INSERTING $url");
      final controller = VideoPlayerController.networkUrl(
        Uri.parse(url),
        videoPlayerOptions: VideoPlayerOptions(
          mixWithOthers: true,
        ),
      );
      controller.setLooping(true);
      controller.initialize();
      return (0, controller);
    });
    _controllers[url] = (ref + 1, controller);
    return controller;
  }

  static void dispose(String url) {
    final (refcount, controller) = _controllers[url]!;
    if (refcount > 1) {
      _controllers[url] = (refcount - 1, controller);
    } else {
      _controllers[url]!.$2.dispose();
      _controllers.remove(url);
    }
  }
}

class AnimatedContentController
    extends InheritedNotifier<ValueNotifier<List<String>>> {
  const AnimatedContentController({
    super.key,
    required ValueNotifier<List<String>> super.notifier,
    required super.child,
  });

  static AnimatedContentController? maybeOf(BuildContext context) {
    return context
        .dependOnInheritedWidgetOfExactType<AnimatedContentController>();
  }

  static AnimatedContentController of(BuildContext context) {
    final controller = maybeOf(context);
    assert(controller != null, 'No AnimatedContentController found in context');
    return controller!;
  }

  ValueNotifier<List<String>> get queue => notifier!;

  void addToQueue(String url) {
    if (!queue.value.contains(url)) {
      queue.value = [...queue.value, url];
    }
  }

  void removeFromQueue(String url) {
    if (queue.value.contains(url)) {
      queue.value = queue.value.where((item) => item != url).toList();
    }
  }

  void clearQueue() {
    queue.value = [];
  }
}

class AnimatedContent extends StatefulWidget {
  final String url;
  final int width;
  final int height;
  final String? placeholderUrl;
  final Widget? cartouche;
  final bool ignoreAutoplay;
  final VoidCallback? goFullScreen;
  final Post? post;

  const AnimatedContent({
    super.key,
    required this.url,
    required this.width,
    required this.height,
    this.placeholderUrl,
    this.cartouche,
    this.ignoreAutoplay = false,
    this.goFullScreen,
    this.post,
  });

  AnimatedContent.fromImageBase({
    super.key,
    required ImageBase image,
    this.placeholderUrl,
    this.cartouche,
    this.ignoreAutoplay = false,
    this.goFullScreen,
    this.post,
  })  : url = image.url,
        width = image.width,
        height = image.height;

  factory AnimatedContent.fromVariantInner({
    required VariantInner mp4,
    required VariantInner placeholder,
    Resolution preferredResolution = Resolution.source,
    bool ignoreAutoplay = false,
    VoidCallback? goFullScreen,
    Post? post,
  }) {
    final ImageBase media;
    if (preferredResolution == Resolution.source) {
      media = mp4.source;
    } else {
      media = mp4.resolutions.withResolution(preferredResolution);
    }
    return AnimatedContent(
      url: media.url,
      placeholderUrl: placeholder.withResolution(Resolution.low).url,
      width: media.width,
      height: media.height,
      goFullScreen: goFullScreen,
      post: post,
    );
  }

  AnimatedContent.fromMediaOEmbed({
    super.key,
    required Media_Oembed media,
    this.placeholderUrl,
    this.cartouche,
    this.ignoreAutoplay = false,
    this.goFullScreen,
    this.post,
  })  : url = media.oembed.providerUrl,
        width = media.oembed.width,
        height = media.oembed.height;

  AnimatedContent.fromRedditVideo({
    super.key,
    required Media_RedditVideo media,
    this.placeholderUrl,
    this.cartouche = const Cartouche(
      "video",
      background: Colors.orange,
    ),
    this.ignoreAutoplay = false,
    this.goFullScreen,
    this.post,
  })  : url = media.field0.dashUrl,
        width = media.field0.width,
        height = media.field0.height;

  @override
  State<AnimatedContent> createState() => _AnimatedContentState();
}

class _AnimatedContentState extends State<AnimatedContent> {
  late VideoPlayerController _controller;
  late final double aspectRatio =
      widget.width.toDouble() / widget.height.toDouble();
  bool _showControls = false;
  bool _muted = true;
  bool _canAutoplay = false;
  double _visibilityFraction = 0;
  late VoidCallback _queueListener;

  Timer? _hideControlsTimer;

  late final animationController = AnimatedContentController.maybeOf(context);

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    WidgetsBinding.instance.addPostFrameCallback(
      (_) {
        AnimatedContentController.maybeOf(context)
            ?.queue
            .addListener(_queueListener);
        animationController?.removeFromQueue(widget.url);
      },
    );
  }

  @override
  void initState() {
    super.initState();
    _controller = VideoControllerPool.get(widget.url);
    _controller.setLooping(true);
    _controller.setVolume(0);

    ImageLoading setting = context.read<DataSettingsCubit>().state.autoplay;

    _canAutoplay = NetworkStatus.canAutoplay(setting);
    _queueListener = () {
      final first = animationController?.queue.value.firstOrNull;
      final isTopOfQueue = (animationController == null ||
              first == widget.url ||
              first == null) &&
          _visibilityFraction == 1 &&
          !_controller.value.isPlaying;
      if (isTopOfQueue) {
        _controller.play();
      }
    };
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
    if (mounted) {
      setState(() {
        _muted = !_muted;
        _controller.setVolume(_muted ? 0 : 1);
      });
    }
  }

  @override
  void dispose() {
    _hideControlsTimer?.cancel();
    VideoControllerPool.dispose(widget.url);

    // Ensure we're not in the queue anymore
    WidgetsBinding.instance.addPostFrameCallback(
      (_) {
        AnimatedContentController.maybeOf(context)
            ?.queue
            .removeListener(_queueListener);
        animationController?.removeFromQueue(widget.url);
      },
    );
    super.dispose();
  }

  Widget placeholder() {
    final Widget child;
    if (widget.placeholderUrl != null) {
      child = Center(
        child: AspectRatio(
          aspectRatio: aspectRatio,
          child: CachedNetworkImage(
            imageUrl: widget.placeholderUrl!,
            fit: BoxFit.cover,
            errorWidget: (_, __, ___) => defaultThumbnail,
          ),
        ),
      );
    } else {
      child = const LoadingIndicator();
    }
    return Stack(
      children: [
        child,
        if (widget.cartouche != null)
          Positioned(
            top: 8,
            right: 8,
            child: widget.cartouche!,
          ),
        if (!_controller.value.isInitialized)
          Positioned(
            bottom: 8,
            right: 8,
            child: CircularProgressIndicator(color: Colors.white),
          )
      ],
    );
  }

  Widget videoPlayer() {
    return Stack(
      children: [
        VideoPlayer(_controller),
        if (widget.cartouche != null && !_controller.value.isPlaying)
          Positioned(
            top: 8,
            right: 8,
            child: widget.cartouche!,
          ),
        Positioned(
          bottom: 8,
          left: 8,
          right: 8,
          child: ValueListenableBuilder<VideoPlayerValue>(
            valueListenable: _controller,
            builder: (context, value, _) {
              if (_showControls) {
                return _controls(context, value);
              } else {
                return timeRemaining(context, value);
              }
            },
          ),
        ),
      ],
    );
  }

  Widget _controls(BuildContext context, VideoPlayerValue value) {
    return Align(
      alignment: Alignment.bottomCenter,
      child: Container(
        padding: const EdgeInsets.all(8),
        decoration: BoxDecoration(
          color: Colors.black45,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Row(
          children: [
            IconButton(
              icon: Icon(
                value.isPlaying ? Icons.pause : Icons.play_arrow,
                color: Colors.white,
              ),
              onPressed: () {
                if (value.isPlaying) {
                  _controller.pause();
                } else {
                  _controller.play();
                }
                if (mounted) setState(() {});
              },
            ),
            Expanded(
              child: Slider(
                value: value.position.inSeconds.toDouble(),
                max: value.duration.inSeconds.toDouble(),
                onChanged: (newValue) {
                  _controller.seekTo(Duration(seconds: newValue.toInt()));
                },
                activeColor: Colors.redAccent,
                inactiveColor: Colors.white30,
              ),
            ),
            IconButton(
              icon: Icon(
                _muted ? Icons.volume_off : Icons.volume_up,
                color: Colors.white,
              ),
              onPressed: _toggleMute,
            ),
            if (widget.goFullScreen != null)
              IconButton(
                icon: Icon(Icons.fullscreen),
                onPressed: widget.goFullScreen?.call,
              )
          ],
        ),
      ),
    );
  }

  Widget timeRemaining(BuildContext context, VideoPlayerValue value) {
    // Remaining time overlay
    final remaining = value.duration - value.position;
    final minutes =
        remaining.inMinutes.remainder(60).toString().padLeft(2, '0');
    final seconds =
        remaining.inSeconds.remainder(60).toString().padLeft(2, '0');
    return Align(
      alignment: Alignment.bottomRight,
      child: Cartouche(
        '$minutes:$seconds',
        background: Colors.black54,
        foreground: Colors.white,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onTap: _toggleControls,
      child: Center(
        child: VisibilityDetector(
          key: ValueKey(widget.url),
          child: AspectRatio(
            aspectRatio: aspectRatio,
            // Show placeholder until we start playing the video
            child: (_controller.value.isInitialized)
                ? videoPlayer()
                : placeholder(),
          ),
          onVisibilityChanged: (visibilityInfo) {
            _visibilityFraction = visibilityInfo.visibleFraction;
            if (mounted) {
              setState(() {});
            }
            final bool inQueue =
                animationController?.queue.value.contains(widget.url) ?? true;
            if (_visibilityFraction > 0.5 && _canAutoplay) {
              animationController?.addToQueue(widget.url);
            } else if ((inQueue && _visibilityFraction < 0.4) ||
                (_controller.value.isPlaying && _visibilityFraction < 0.8)) {
              _controller.pause();
              animationController?.removeFromQueue(widget.url);
            }
          },
        ),
      ),
    );
  }
}
