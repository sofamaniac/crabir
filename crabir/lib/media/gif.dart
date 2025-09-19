part of 'media.dart';

class AnimatedContentController {
  static ValueNotifier<List<String>> queue = ValueNotifier([]);
  static void addToQueue(String url) {
    if (!queue.value.contains(url)) {
      queue.value = [...queue.value, url];
    }
  }

  static void removeFromQueue(String url) {
    if (queue.value.contains(url)) {
      queue.value = queue.value.where((item) => item != url).toList();
    }
  }

  static void clearQueue() {
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

  const AnimatedContent({
    super.key,
    required this.url,
    required this.width,
    required this.height,
    this.placeholderUrl,
    this.cartouche,
    this.ignoreAutoplay = false,
    this.goFullScreen,
  });

  AnimatedContent.fromImageBase({
    super.key,
    required ImageBase image,
    this.placeholderUrl,
    this.cartouche,
    this.ignoreAutoplay = false,
    this.goFullScreen,
  })  : url = image.url,
        width = image.width,
        height = image.height;

  factory AnimatedContent.fromVariantInner({
    required VariantInner mp4,
    required VariantInner placeholder,
    Resolution preferredResolution = Resolution.source,
    bool ignoreAutoplay = false,
    VoidCallback? goFullScreen,
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
    );
  }

  AnimatedContent.fromMediaOEmbed({
    super.key,
    required Media_Oembed media,
    this.placeholderUrl,
    this.cartouche,
    this.ignoreAutoplay = false,
    this.goFullScreen,
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
  })  : url = media.field0.dashUrl,
        width = media.field0.width,
        height = media.field0.height;

  @override
  State<AnimatedContent> createState() => _AnimatedContentState();
}

class _AnimatedContentState extends State<AnimatedContent> {
  late VideoPlayerController _controller;
  late final VoidCallback _queueListener;
  bool _showControls = false;
  bool _muted = true;
  bool _canAutoplay = false;
  double _visibilityFraction = 0;

  Timer? _hideControlsTimer;

  @override
  void initState() {
    super.initState();
    _controller = VideoPlayerController.networkUrl(Uri.parse(widget.url));
    _controller.setLooping(true);
    _controller.setVolume(0);
    WidgetsBinding.instance.addPostFrameCallback(
      (_) => _controller.initialize(),
    );

    ImageLoading setting = context.read<DataSettingsCubit>().state.autoplay;

    _canAutoplay = NetworkStatus.canAutoplay(setting);
    _queueListener = () {
      if (_visibilityFraction == 1 &&
          AnimatedContentController.queue.value.first == widget.url &&
          !_controller.value.isPlaying) {
        _controller.play();
        setState(() {});
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
    _controller.dispose();

    // Ensure we're not in the queue anymore
    AnimatedContentController.queue.removeListener(_queueListener);
    AnimatedContentController.removeFromQueue(widget.url);
    super.dispose();
  }

  Widget placeholder() {
    final defaultThumbnail = ColoredBox(
      color: Colors.grey,
      child: Center(
        child: Icon(Icons.warning),
      ),
    );
    return Stack(
      children: [
        if (widget.placeholderUrl != null)
          Center(
            child: AspectRatio(
              aspectRatio: widget.width / widget.height.toDouble(),
              child: CachedNetworkImage(
                imageUrl: widget.placeholderUrl!,
                fit: BoxFit.cover,
                errorWidget: (_, __, ___) => defaultThumbnail,
              ),
            ),
          )
        else
          const LoadingIndicator(),
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

  Widget _controls(BuildContext context, VideoPlayerValue value) {
    return Container(
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
              setState(() {});
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
    );
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      behavior: HitTestBehavior.translucent,
      onTap: _toggleControls,
      child: Stack(
        children: [
          Center(
            child: VisibilityDetector(
              key: ValueKey(widget.url),
              child: AspectRatio(
                aspectRatio: widget.width / widget.height.toDouble(),
                // // Show placeholder until we start playing the video
                child: (_controller.value.isPlaying)
                    ? VideoPlayer(_controller)
                    : placeholder(),
              ),
              onVisibilityChanged: (visibilityInfo) {
                setState(() {
                  _visibilityFraction = visibilityInfo.visibleFraction;
                });
                final bool inQueue =
                    AnimatedContentController.queue.value.contains(widget.url);
                if (_visibilityFraction > 0.5 && _canAutoplay && !inQueue) {
                  AnimatedContentController.queue.addListener(_queueListener);
                  AnimatedContentController.addToQueue(widget.url);
                } else if ((inQueue && _visibilityFraction < 0.4) ||
                    (_controller.value.isPlaying &&
                        _visibilityFraction < 0.8)) {
                  _controller.pause();
                  AnimatedContentController.queue
                      .removeListener(_queueListener);
                  AnimatedContentController.removeFromQueue(widget.url);
                }
                _queueListener();
              },
            ),
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
                    child: Cartouche(
                      '$minutes:$seconds',
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
