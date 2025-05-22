import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart' hide Image;
import 'package:flutter/material.dart' as widgets show Image;
import 'package:flutter/services.dart';

class PostView extends StatelessWidget {
  const PostView({super.key, required this.post});
  final Post post;

  @override
  Widget build(BuildContext context) {
    return Column(children: [Text(post.title), Divider()]);
  }
}

class PostHeader extends StatelessWidget {
  const PostHeader({
    super.key,
    required this.post,
    this.onSubredditTap,
    this.onUserTap,
  });
  final Post post;
  final void Function(SubredditInfo subreddit)? onSubredditTap;
  final void Function(AuthorInfo? username)? onUserTap;

  @override
  Widget build(BuildContext context) {
    final labelStyle = Theme.of(context).textTheme.labelSmall;
    return Padding(
        padding: EdgeInsets.symmetric(horizontal: 8),
        child: Row(
          children: [
            InkWell(
              onTap: () => onSubredditTap?.call(post.subreddit),
              child: Text(
                'r/${post.subreddit.subreddit}',
                style: labelStyle?.copyWith(
                  color: Theme.of(context).colorScheme.primary,
                ),
              ),
            ),
            const Text(' • '),
            InkWell(
              onTap: () => onUserTap?.call(post.author),
              child: Text(
                'u/${post.author?.username ?? "[deleted]"}',
                style: labelStyle?.copyWith(
                  color: Theme.of(context).colorScheme.primary,
                ),
              ),
            ),
          ],
        ));
  }
}

class PostBottowRow extends StatelessWidget {
  final Post post;
  const PostBottowRow({super.key, required this.post});
  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: EdgeInsets.symmetric(horizontal: 8),
        child: Row(
          children: [
            IconButton(
              icon: const Icon(Icons.arrow_upward),
              tooltip: 'Upvote',
              onPressed: () {},
            ),
            const SizedBox(width: 16),
            IconButton(
              icon: const Icon(Icons.comment),
              tooltip: 'Comments',
              onPressed: () {},
            ),
            const Spacer(),
            IconButton(
              icon: const Icon(Icons.share),
              tooltip: 'Share',
              onPressed: () {},
            ),
          ],
        ));
  }
}

class RedditPostCard extends StatelessWidget {
  final Post post;
  final void Function(String subreddit)? onSubredditTap;
  final void Function(String username)? onUserTap;

  const RedditPostCard({
    super.key,
    required this.post,
    this.onSubredditTap,
    this.onUserTap,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 0, vertical: 4),
      elevation: 1,
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 8),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            PostHeader(post: post),
            const SizedBox(height: 8),
            Text(
              post.title,
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 12),
            PostContent(post: post),
            const SizedBox(height: 12),
            PostBottowRow(post: post),
          ],
        ),
      ),
    );
  }
}

class PostContent extends StatelessWidget {
  final Post post;
  const PostContent({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    return switch (post.kind) {
      Kind.selftext => Text("selftext"),
      Kind.meta => Text("meta"),
      Kind.video => Text("video"),
      Kind.gallery => Text("gallery"),
      Kind.image => ImageContent(post: post),
      Kind.link => Text("link"),
      Kind.unknown => Text("unknown"),
    };
  }
}

class ImageContent extends StatelessWidget {
  final Post post;
  const ImageContent({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      Padding(
          padding: EdgeInsets.symmetric(horizontal: 8),
          child: Align(
              alignment: AlignmentDirectional.centerStart,
              child: Text(
                post.title,
                style: Theme.of(context).textTheme.titleMedium,
                textAlign: TextAlign.start,
              ))),
      imageView(context)
    ]);
  }

  Widget imageView(BuildContext context) {
    final image = post.preview!.images[0].source;
    return InkWell(
        onTap: () => Navigator.push(
            context,
            MaterialPageRoute(
                builder: (_) => FullscreenImageViewer(image: image))),
        child: AspectRatio(
            aspectRatio: image.width.toDouble() / image.height.toDouble(),
            child: widgets.Image.network(image.url,
                //width: image.width.toDouble(),
                width: double.infinity,
                //height: image.height.toDouble(),
                fit: BoxFit.fitWidth)));
  }
}

class FullscreenImageViewer extends StatefulWidget {
  final ImageBase image;
  const FullscreenImageViewer({super.key, required this.image});

  @override
  State<FullscreenImageViewer> createState() => _FullscreenImageViewerState();
}

class _FullscreenImageViewerState extends State<FullscreenImageViewer>
    with SingleTickerProviderStateMixin {
  final _transformationController = TransformationController();
  late AnimationController _animationController;
  Animation<Matrix4>? _animation;
  bool _dismissable = true;

  // Set to flutter's default value
  SystemUiMode _systemUiMode = SystemUiMode.edgeToEdge;

  void _onSingleTap() {
    if (_systemUiMode == SystemUiMode.edgeToEdge) {
      setState(() => _systemUiMode = SystemUiMode.leanBack);
    } else {
      setState(() => _systemUiMode = SystemUiMode.edgeToEdge);
    }
    SystemChrome.setEnabledSystemUIMode(_systemUiMode);
  }

  void _onDoubleTap(TapDownDetails details) {
    final currentScale = _transformationController.value.getMaxScaleOnAxis();
    var expectedZoom = 1.0;
    var transform = Matrix4.identity();
    if (currentScale < 1.0) {
      expectedZoom = 1.0;
    } else if (currentScale < 2.5) {
      expectedZoom = 2.5;
    } else if (currentScale < 5.0) {
      expectedZoom = 5.0;
    } else {
      expectedZoom = 1.0;
    }

    setState(() {
      _dismissable = expectedZoom == 1.0;
    });

    if (expectedZoom > currentScale) {
      // Calculate the transformation
      final focalPoint = details.localPosition;
      transform = transform
        ..translate(-focalPoint.dx * (expectedZoom - 1),
            -focalPoint.dy * (expectedZoom - 1))
        ..scale(expectedZoom);
    }

    _animation =
        Matrix4Tween(begin: _transformationController.value, end: transform)
            .animate(CurvedAnimation(
                parent: _animationController, curve: Curves.easeOut));

    _animationController.forward(from: 0);
  }

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      vsync: this,
      duration: Duration(milliseconds: 200),
    );
    _animationController.addListener(() {
      _transformationController.value = _animation!.value;
    });
  }

  @override
  void dispose() {
    _animationController.dispose();
    _transformationController.dispose();
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.edgeToEdge);
    super.dispose();
  }

  Widget _content() {
    return Stack(
      children: [
        Positioned.fill(
          child: InteractiveViewer(
            transformationController: _transformationController,
            panEnabled: true,
            minScale: 1.0,
            maxScale: 5.0,
            child: Center(
              child: widgets.Image.network(
                widget.image.url,
                fit: BoxFit.contain,
              ),
            ),
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      backgroundColor: Colors.black,
      body: Dismissible(
        key: Key("FullscreenImageViewer"),
        direction:
            _dismissable ? DismissDirection.vertical : DismissDirection.none,
        onDismissed: (_) => Navigator.pop(context),
        child: GestureDetector(
          behavior: HitTestBehavior.translucent,
          onTap: _onSingleTap,
          onDoubleTapDown: _onDoubleTap,
          onDoubleTap: () {},
          child: _content(),
        ),
      ),
    );
  }
}
