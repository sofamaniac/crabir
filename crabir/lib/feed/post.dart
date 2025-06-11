import 'dart:math';

import 'package:crabir/drawer.dart';
import 'package:crabir/feed/feed.dart';
import 'package:crabir/feed/gif.dart';
import 'package:crabir/feed/image.dart';
import 'package:crabir/feed/video.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/flair.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:logging/logging.dart';

final horizontalPadding = 16.0;

class _PostTitle extends StatelessWidget {
  final Post post;

  const _PostTitle({required this.post});

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Expanded(
          flex: 3,
          child: Text(
            post.title,
            textAlign: TextAlign.start,
            style: Theme.of(context).textTheme.titleMedium,
          ),
        ),
        Expanded(
          flex: 1,
          child: AspectRatio(
            aspectRatio: 1.0,
            child: ClipRect(
              child: Image.network(post.thumbnail!.url, fit: BoxFit.cover),
            ),
          ),
        ),
      ],
    );
  }
}

class _PostFlair extends StatelessWidget {
  final Post post;
  final Logger log = Logger("PostFlair");
  _PostFlair({required this.post});

  Widget richtext(BuildContext context, Richtext richtext,
      Color backgroundColor, Color textColor) {
    return switch (richtext) {
      Richtext_Text(t: var text) => RichText(
          text: WidgetSpan(
            child: Container(
              decoration: BoxDecoration(
                color: backgroundColor,
                borderRadius: BorderRadius.all(
                  Radius.circular(8),
                ),
              ),
              padding: EdgeInsetsDirectional.symmetric(horizontal: 4),
              child: Text(
                text,
                semanticsLabel: "flair",
                style: TextStyle(
                  color: textColor,
                  //backgroundColor: backgroundColor,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            style: TextStyle(
              color: textColor,
              backgroundColor: backgroundColor,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      Richtext_Emoji(a: var text, u: var url) => Image.network(
          url,
          semanticLabel: text,
        )
    };
  }

  @override
  Widget build(BuildContext context) {
    final flair = post.linkFlair;
    return Row(
      children: flair.richtext
          .map(
            (e) => richtext(
              context,
              e,
              stringToColor(flair.backgroundColor ?? "gray"),
              stringToColor(flair.textColor ?? "black"),
            ),
          )
          .toList(),
    );
  }
}

Color stringToColor(String s) {
  final log = Logger("stringToColor");
  if (s.startsWith("#")) {
    return HexColor.fromHex(s);
  } else {
    return switch (s) {
      "black" => Colors.black,
      "transparent" => Colors.transparent,
      "light" => Colors.white,
      "dark" => Colors.black,
      "gray" => Colors.grey,
      _ => () {
          log.info("Unknown color $s");
          return Colors.grey;
        }()
    };
  }
}

class RedditPostCard extends StatefulWidget {
  final Post post;

  final Future<void> Function(VoteDirection) onLike;
  final Future<void> Function(bool) onSave;

  const RedditPostCard(
      {super.key,
      required this.post,
      required this.onLike,
      required this.onSave});
  @override
  State<StatefulWidget> createState() => _RedditPostCardState();
}

class _RedditPostCardState extends State<RedditPostCard> {
  late bool? likes;
  late bool saved;

  _RedditPostCardState();

  @override
  void initState() {
    super.initState();
    likes = widget.post.likes;
    saved = widget.post.saved;
  }

  Widget _wrap(Widget widget) {
    return Padding(padding: EdgeInsets.symmetric(horizontal: 8), child: widget);
  }

  Widget header(BuildContext context) {
    final labelStyle = Theme.of(context).textTheme.labelSmall;
    final subreddit = widget.post.subreddit.subreddit;
    return Row(
      children: [
        InkWell(
          onTap: () => Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => FeedView(
                feed: Feed.subreddit(subreddit),
                initialSort: Sort.best(),
              ),
            ),
          ),
          child: Text(
            'r/$subreddit',
            style: labelStyle?.copyWith(
              color: Theme.of(context).colorScheme.primary,
            ),
          ),
        ),
        const Text(' • '),
        InkWell(
          onTap: () => (),
          child: Text(
            'u/${widget.post.author?.username ?? "[deleted]"}',
            style: labelStyle?.copyWith(
              color: Theme.of(context).colorScheme.primary,
            ),
          ),
        ),
      ],
    );
  }

  Widget footer(BuildContext context) {
    final likeColor = Theme.of(context).colorScheme.primary;
    final dislikeColor = Theme.of(context).colorScheme.secondary;
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        IconButton(
          icon: Icon(Icons.thumb_up, color: (likes == true) ? likeColor : null),
          tooltip: 'Upvote',
          onPressed: () async {
            await widget.onLike(
              !(likes == true) ? VoteDirection.up : VoteDirection.neutral,
            );
            setState(() {
              if (likes == true) {
                likes = null;
              } else {
                likes = true;
              }
            });
          },
        ),
        IconButton(
          icon: Icon(Icons.thumb_down,
              color: (likes == false) ? dislikeColor : null),
          tooltip: 'Downvote',
          onPressed: () async {
            await widget.onLike(
              !(likes == false) ? VoteDirection.down : VoteDirection.neutral,
            );
            setState(() {
              if (likes == false) {
                likes = null;
              } else {
                likes = false;
              }
            });
          },
        ),
        IconButton(
          onPressed: () async {
            await widget.onSave(!saved);
            saved = !saved;
          },
          icon: const Icon(Icons.bookmark_outlined),
          tooltip: "Save",
        ),
        IconButton(
          icon: const Icon(Icons.comment),
          tooltip: 'Comments',
          onPressed: () {},
        ),
        IconButton(
          icon: const Icon(Icons.exit_to_app),
          tooltip: 'Open in',
          onPressed: () {},
        ),
        IconButton(
          icon: const Icon(Icons.share),
          tooltip: 'Share',
          onPressed: () {
            debugPost(post: widget.post);
          },
        ),
      ],
    );
  }

  bool showThumbnail() {
    if (widget.post.thumbnail == null) {
      return false;
    } else {
      return switch (widget.post.kind) {
        Kind.link || Kind.unknown => true,
        _ => false,
      };
    }
  }

  Widget title(BuildContext context) {
    if (showThumbnail()) {
      return _PostTitle(post: widget.post);
    } else {
      return Align(
        alignment: AlignmentDirectional.centerStart,
        child: Text(
          widget.post.title,
          style: Theme.of(context).textTheme.titleMedium,
        ),
      );
    }
  }

  Widget _contentWrap(Widget widget) {
    return switch (this.widget.post.kind) {
      Kind.image || Kind.gallery || Kind.video || Kind.selftext => widget,
      _ => _wrap(widget),
    };
  }

  Widget content(BuildContext context) {
    final fontSize = Theme.of(context).textTheme.bodyMedium?.fontSize ?? 15;
    final widget = switch (this.widget.post.kind) {
      Kind.selftext => HtmlWithConditionalFade(
          htmlContent: this.widget.post.selftextHtml ?? "",
          maxLines: 5,
          backgroundColor: Colors.black,
          fontSize: fontSize,
        ),
      Kind.meta => Text("meta"),
      Kind.video => VideoContent(post: this.widget.post),
      Kind.gallery => Text("gallery"),
      Kind.image => switch (this.widget.post.url.endsWith(".gif")) {
          false => ImageContent(
              post: this.widget.post,
              fullscreen: (context, post) =>
                  FullscreenImageViewer(imageUrl: post.url),
            ),
          true => ImageContent(
              post: this.widget.post,
              fullscreen: (context, post) => GifContentFullscreen(post: post),
            ),
        },
      Kind.link => Container(),
      Kind.unknown => Text("unknown"),
    };

    return _contentWrap(widget);
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      elevation: 1,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        spacing: 8,
        children: [
          _wrap(header(context)),
          _wrap(title(context)),
          _wrap(_PostFlair(post: widget.post)),
          content(context),
          _wrap(footer(context)),
        ],
      ),
    );
  }
}

class ImageContent extends StatelessWidget {
  final Post post;
  final Widget Function(BuildContext, Post)? fullscreen;
  const ImageContent({super.key, required this.post, this.fullscreen});

  Widget thumbnail(
      BuildContext context, Widget child, ImageChunkEvent? loadingProgress) {
    final thumbnail = post.thumbnail;
    if (thumbnail == null) {
      return child;
    } else if (loadingProgress != null) {
      return Image.network(
        thumbnail.url,
        width: double.infinity,
        fit: BoxFit.fitWidth,
      );
    } else {
      return child;
    }
  }

  @override
  Widget build(BuildContext context) {
    // If the post is an image, the url should point to the image.
    final imageUrl = post.url;
    // We look into the previews to get the source size
    // It does not need to match exactly as it is used to
    // prevent posts from jumping around when loading the image.
    final image = post.preview!.images[0].source;

    late final Future? Function() onTap;
    if (fullscreen != null) {
      onTap = () => Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => fullscreen!(context, post),
            ),
          );
    } else {
      onTap = () {
        return;
      };
    }

    return InkWell(
      onTap: onTap,
      child: ImageThumbnail(
        url: imageUrl,
        width: image.width.toDouble(),
        height: image.height.toDouble(),
        thumbnailUrl: post.thumbnail?.url,
      ),
    );
  }
}

class FullscreenImageViewer extends StatefulWidget {
  final String imageUrl;
  const FullscreenImageViewer({super.key, required this.imageUrl});

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
    _transformationController.addListener(() {
      // The image is dismissalbe only if it is not zoomed in
      setState(() {
        _dismissable =
            _transformationController.value.getMaxScaleOnAxis() == 1.0;
      });
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
              child: Image.network(
                widget.imageUrl,
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

class FadingHtml extends StatelessWidget {
  final String htmlContent;
  final int maxLines;

  const FadingHtml({
    super.key,
    required this.htmlContent,
    this.maxLines = 10,
  });

  @override
  Widget build(BuildContext context) {
    if (htmlContent.isEmpty) {
      return Container();
    }
    final fontSize = Theme.of(context).textTheme.bodyMedium?.fontSize ?? 15;
    final backgroundColor = Theme.of(context).colorScheme.surface;
    // return ConstrainedBox(
    //   constraints: BoxConstraints(
    //     // Approx size for `maxLines`
    //     maxHeight: maxLines * fontSize * 3 / 2,
    //   ),
    //   child: ClipRect(
    //     child: Html(
    //       data: htmlContent,
    //       shrinkWrap: true,
    //     ),
    //   ),
    // );
    return ConstrainedBox(
      constraints: BoxConstraints(
          maxHeight: maxLines * fontSize * 2,
          maxWidth: MediaQuery.of(context).size.width),
      child: Stack(
        fit: StackFit.expand,
        clipBehavior: Clip.antiAlias,
        children: [
          Positioned.fill(
            top: 0,
            child: Html(data: htmlContent),
          ),
          Positioned(
            bottom: 0,
            left: 0,
            right: 0,
            child: Container(
              height: 40, // Height of the fade effect
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                  colors: [
                    Colors.transparent,
                    backgroundColor, // Match your background color
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class HtmlWithConditionalFade extends StatefulWidget {
  final String htmlContent;
  final int maxLines;
  final double fontSize;
  final Color backgroundColor;

  const HtmlWithConditionalFade({
    super.key,
    required this.htmlContent,
    required this.maxLines,
    required this.fontSize,
    required this.backgroundColor,
  });

  @override
  State<HtmlWithConditionalFade> createState() =>
      _HtmlWithConditionalFadeState();
}

class _HtmlWithConditionalFadeState extends State<HtmlWithConditionalFade> {
  final GlobalKey _key = GlobalKey();
  bool _overflowing = false;
  double _contentHeight = 0;

  @override
  void initState() {
    super.initState();
    // Wait for first layout to check overflow
    WidgetsBinding.instance.addPostFrameCallback((_) => _checkOverflow());
  }

  void _checkOverflow() {
    final context = _key.currentContext;
    if (context == null) return;
    final renderBox = context.findRenderObject() as RenderBox;
    final contentHeight = renderBox.size.height;

    final maxHeight = widget.maxLines * widget.fontSize * 2;

    if (mounted) {
      setState(() {
        _overflowing = contentHeight > maxHeight;
        _contentHeight = contentHeight;
      });
    }
  }

  Widget gradient() {
    return Positioned(
      bottom: 0,
      left: 0,
      right: 0,
      child: IgnorePointer(
        child: Container(
          height: 40,
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              colors: [
                Colors.transparent,
                widget.backgroundColor,
              ],
            ),
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final maxHeight =
        min(_contentHeight, widget.maxLines * widget.fontSize * 2);

    return ConstrainedBox(
      constraints: BoxConstraints(
        maxHeight: maxHeight,
        maxWidth: MediaQuery.of(context).size.width,
      ),
      child: Stack(
        children: [
          Positioned.fill(
            child: SingleChildScrollView(
              physics: const NeverScrollableScrollPhysics(),
              child: Html(
                key: _key,
                data: widget.htmlContent,
              ),
            ),
          ),
          if (_overflowing) gradient(),
        ],
      ),
    );
  }
}
