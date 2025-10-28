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
    return Scaffold(
      extendBodyBehindAppBar: true,
      backgroundColor: Colors.black,
      appBar: TopBar(show: _showBars),
      bottomNavigationBar: BottomBar(
        show: _showBars,
        post: widget.post,
      ),
      body: Dismissible(
        key: Key("_FullscreenMediaViewState"),
        direction: DismissDirection.vertical,
        resizeDuration: null,
        onDismissed: (_) => context.pop(widget.onPop?.call()),
        child: SafeArea(
          child: PhotoViewGestureDetectorScope(
            axis: Axis.vertical,
            child: widget.builder(_toggleBars),
          ),
        ),
      ),
    );
  }

  void _toggleBars() {
    setState(() {
      _showBars = !_showBars;
    });
  }
}

class TopBar extends StatelessWidget implements PreferredSizeWidget {
  final List<Widget> actions;
  final bool show;

  const TopBar({
    super.key,
    required this.show,
    this.actions = const [],
  });

  @override
  Widget build(BuildContext context) {
    final bar = Column(
      children: [
        SizedBox(
          height: MediaQuery.of(context).padding.top,
        ),
        Row(
          children: [
            IconButton(
              icon: const Icon(Icons.close, color: Colors.white),
              onPressed: () => Navigator.pop(context),
            ),
            const Spacer(),
            ...actions,
            IconButton(
              icon: const Icon(Icons.more_vert, color: Colors.white),
              onPressed: () {},
            ),
          ],
        ),
      ],
    );
    return SlidingBar(
      offset: Offset(0, -1),
      show: show,
      child: bar,
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}

class BottomBar extends StatelessWidget {
  final List<Widget> actions;
  final bool show;
  final Post? post;
  final String? title;

  const BottomBar({
    super.key,
    required this.show,
    this.actions = const [],
    this.post,
    this.title,
  });

  @override
  Widget build(BuildContext context) {
    if (post == null) {
      return SizedBox.shrink();
    }
    final inThread = GoRouter.of(context).inThread(post!);
    final theme = CrabirTheme.of(context);
    final bar = Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        if (title != null) Text(title!),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            VoteButton.like(
              likes: post!.likes.toVoteDirection(),
              colorActive: theme.primaryColor,
              onChange: (dir) async {
                await post!.vote(direction: dir, client: RedditAPI.client());
              },
            ),
            VoteButton.dislike(
              likes: post!.likes.toVoteDirection(),
              colorActive: theme.downvoteContent,
              onChange: (dir) async {
                await post!.vote(direction: dir, client: RedditAPI.client());
              },
            ),
            SaveButton(
              initialValue: post!.saved,
              onChange: (save) async {
                if (save) {
                  await post?.save(client: RedditAPI.client());
                } else {
                  await post?.unsave(client: RedditAPI.client());
                }
              },
            ),
            if (!inThread)
              IconButton(
                icon: Icon(Icons.comment_outlined),
                color: theme.secondaryText,
                onPressed: () {
                  context.replace(post!.permalink, extra: post);
                },
              ),
            ShareButton(post: post!, short: true)
          ],
        ),
        SizedBox(height: MediaQuery.of(context).padding.bottom),
      ],
    );
    return SlidingBar(
      offset: Offset(0, 1),
      show: show,
      child: bar,
    );
  }
}

class SlidingBar extends StatelessWidget {
  final Widget child;
  final Offset offset;
  final bool show;

  const SlidingBar({
    super.key,
    required this.offset,
    required this.child,
    required this.show,
  });

  @override
  Widget build(BuildContext context) {
    return AnimatedSlide(
      offset: show ? Offset.zero : offset,
      duration: const Duration(milliseconds: 250),
      child: AnimatedOpacity(
        opacity: show ? 1 : 0,
        duration: const Duration(milliseconds: 250),
        child: Container(
          color: Colors.black54,
          child: child,
        ),
      ),
    );
  }
}
