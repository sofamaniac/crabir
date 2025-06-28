import 'dart:math';

import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

/// Display the image at `url` and `thumbnailUrl` while the image is loading.
class ImageThumbnail extends StatelessWidget {
  final String url;
  final String? thumbnailUrl;
  final double width;
  final double height;

  const ImageThumbnail({
    super.key,
    required this.url,
    required this.width,
    required this.height,
    this.thumbnailUrl,
  });

  Widget thumbnail() {
    if (thumbnailUrl != null) {
      return Image.network(
        thumbnailUrl!,
        width: double.infinity,
        fit: BoxFit.fitWidth,
      );
    } else {
      return Container();
    }
  }

  @override
  Widget build(BuildContext context) {
    return AspectRatio(
      aspectRatio: width / max(1, height),
      child: Center(
        child: Image.network(
          url,
          width: double.infinity,
          fit: BoxFit.fitWidth,
          frameBuilder: (context, child, frame, _) {
            if (frame != null) {
              return child;
            } else {
              return thumbnail();
            }
          },
        ),
      ),
    );
  }
}

/// Display an image and goes fullscreen on tap.
class ImageContent extends StatelessWidget {
  final Post post;
  final Widget Function(BuildContext, Post)? fullscreen;
  const ImageContent({super.key, required this.post, this.fullscreen});

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

/// Fullscreen image view with swipe to dismiss and pinch to zoom.
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
