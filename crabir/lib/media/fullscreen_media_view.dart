part of 'media.dart';

class FullscreenMediaView extends StatefulWidget {
  final Widget Function(VoidCallback) builder;
  final Widget? trailing;
  final String? title;
  const FullscreenMediaView({
    super.key,
    required this.builder,
    this.trailing,
    this.title,
  });

  @override
  State<FullscreenMediaView> createState() => _FullscreenMediaViewState();
}

class _FullscreenMediaViewState extends State<FullscreenMediaView> {
  bool _showBars = false;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      backgroundColor: Colors.black,
      body: Stack(
        children: [
          Dismissible(
            key: Key("_FullscreenMediaViewState"),
            direction: DismissDirection.vertical,
            onDismissed: (_) => context.pop(),
            child: PhotoViewGestureDetectorScope(
              axis: Axis.vertical,
              child: widget.builder(_toggleBars),
            ),
          ),
          _topBar(),
          _bottomBar(),
        ],
      ),
    );
  }

  void _toggleBars() {
    setState(() {
      _showBars = !_showBars;
    });
  }

  Widget _bottomBar() {
    final theme = CrabirTheme.of(context);
    final bar = SafeArea(
      top: false,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          if (widget.title != null) Text(widget.title!),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              VoteButton.like(
                likes: ValueNotifier(VoteDirection.neutral),
                colorActive: theme.primaryColor,
              ),
              IconButton(
                icon: const Icon(Icons.favorite_border, color: Colors.white),
                onPressed: () {},
              ),
              IconButton(
                icon: const Icon(Icons.share, color: Colors.white),
                onPressed: () {},
              ),
              IconButton(
                icon: const Icon(Icons.download, color: Colors.white),
                onPressed: () {},
              ),
            ],
          ),
        ],
      ),
    );
    return _slidingBar(bar, Offset(0, 1), Alignment.bottomCenter);
  }

  Widget _topBar() {
    final bar = SafeArea(
      bottom: false,
      child: Row(
        children: [
          IconButton(
            icon: const Icon(Icons.close, color: Colors.white),
            onPressed: () => Navigator.pop(context),
          ),
          const Spacer(),
          if (widget.trailing != null) widget.trailing!,
          IconButton(
            icon: const Icon(Icons.more_vert, color: Colors.white),
            onPressed: () {},
          ),
        ],
      ),
    );
    return _slidingBar(bar, Offset(0, -1), Alignment.topCenter);
  }

  Widget _slidingBar(Widget child, Offset offset, Alignment alignment) {
    return AnimatedSlide(
      offset: _showBars ? Offset.zero : offset,
      duration: const Duration(milliseconds: 250),
      child: AnimatedOpacity(
        opacity: _showBars ? 1 : 0,
        duration: const Duration(milliseconds: 250),
        child: Align(
          alignment: alignment,
          child: Container(
            color: Colors.black54,
            padding: const EdgeInsets.all(12),
            child: child,
          ),
        ),
      ),
    );
  }
}
