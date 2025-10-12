part of 'media.dart';

class FullscreenMediaView extends StatefulWidget {
  final Widget Function(VoidCallback) builder;
  final Widget? trailing;
  final String? title;
  final Post? post;
  final Object Function()? onPop;
  const FullscreenMediaView({
    super.key,
    required this.builder,
    this.trailing,
    this.title,
    this.post,
    this.onPop,
  });

  @override
  State<FullscreenMediaView> createState() => _FullscreenMediaViewState();
}

class _FullscreenMediaViewState extends State<FullscreenMediaView> {
  bool _showBars = true;
  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        extendBodyBehindAppBar: true,
        backgroundColor: Colors.black,
        body: Stack(
          children: [
            Dismissible(
              key: Key("_FullscreenMediaViewState"),
              direction: DismissDirection.vertical,
              resizeDuration: null,
              onDismissed: (_) => context.pop(widget.onPop?.call()),
              child: PhotoViewGestureDetectorScope(
                axis: Axis.vertical,
                child: widget.builder(_toggleBars),
              ),
            ),
            _topBar(),
            _bottomBar(),
          ],
        ),
      ),
    );
  }

  void _toggleBars() {
    setState(() {
      _showBars = !_showBars;
    });
  }

  Widget _bottomBar() {
    final post = widget.post;
    if (post == null) {
      return SizedBox.shrink();
    }
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
                likes: post.likes.toVoteDirection(),
                colorActive: theme.primaryColor,
                onChange: (dir) async {
                  await post.vote(direction: dir, client: RedditAPI.client());
                  setState(() {});
                },
              ),
              VoteButton.dislike(
                likes: post.likes.toVoteDirection(),
                colorActive: theme.downvoteContent,
                onChange: (dir) async {
                  await post.vote(direction: dir, client: RedditAPI.client());
                  setState(() {});
                },
              ),
              SaveButton(
                initialValue: post.saved,
                onChange: (save) async {
                  if (save) {
                    await post.save(client: RedditAPI.client());
                  } else {
                    await post.unsave(client: RedditAPI.client());
                  }
                  setState(() {});
                },
              ),
              IconButton(
                icon: Icon(Icons.comment_outlined),
                color: theme.secondaryText,
                onPressed: () {
                  context.go(post.permalink, extra: post);
                },
              ),
              IconButton(
                icon: Icon(Icons.share),
                color: theme.secondaryText,
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
