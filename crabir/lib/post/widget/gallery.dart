import 'dart:math';
import 'package:chewie/chewie.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/gallery.dart';
import 'package:video_player/video_player.dart';

class MediaGalleryView extends StatelessWidget {
  final Post post;
  const MediaGalleryView({super.key, required this.post});
  @override
  Widget build(BuildContext context) {
    if (post.gallery != null) {
      return GalleryView(
        gallery: post.gallery!,
      );
    } else if (post.isCrosspost) {
      final gallery = post.crosspostParentList[0].gallery;
      if (gallery != null) {
        return GalleryView(
          gallery: gallery,
        );
      } else {
        return Text("Gallery and is Crosspost but no gallery on parent");
      }
    } else {
      return Text("Gallery: not Crosspost && no gallery on post");
    }
  }
}

class GalleryView extends StatefulWidget {
  final Gallery gallery;

  const GalleryView({super.key, required this.gallery});

  @override
  State<GalleryView> createState() => _GalleryViewState();
}

class _GalleryViewState extends State<GalleryView> {
  late PageController _pageController;
  int _currentPage = 0;

  @override
  void initState() {
    super.initState();
    _pageController = PageController();
    _pageController.addListener(() {
      setState(() {
        _currentPage = _pageController.page?.round() ?? 0;
      });
    });
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  void _openFullScreen(int initialPage) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => FullScreenGalleryView(
          gallery: widget.gallery,
          initialPage: initialPage,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final aspectRatio = widget.gallery.aspectRatio;
    return Stack(
      children: [
        AspectRatio(
          aspectRatio: aspectRatio,
          child: GestureDetector(
            onTap: () {
              _openFullScreen(_currentPage);
            },
            child: PageView.builder(
              controller: _pageController,
              itemBuilder: (context, index) {
                final image = widget.gallery.get_(index: index);
                return switch (image) {
                  Source_Image() =>
                    ImageThumbnail.fromGalleryImage(image.source),
                  Source_AnimatedImage() => _GifContent(
                      url: image.source.mp4,
                      width: image.source.x,
                      height: image.source.y),
                };

                //return ImageThumbnail.imageBase(image);
              },
              itemCount: widget.gallery.length,
            ),
          ),
        ),
        Positioned(
          top: 4,
          right: 4,
          child: Cartouche(
            '${_currentPage + 1} / ${widget.gallery.length}',
            background: Colors.black54,
            foreground: Colors.white,
          ),
        ),
      ],
    );
  }
}

class FullScreenGalleryView extends StatefulWidget {
  final Gallery gallery;
  final int initialPage;

  const FullScreenGalleryView({
    super.key,
    required this.gallery,
    required this.initialPage,
  });

  @override
  State<FullScreenGalleryView> createState() => _FullScreenGalleryViewState();
}

class _FullScreenGalleryViewState extends State<FullScreenGalleryView> {
  late PageController _pageController;
  int _currentPage = 0;

  @override
  void initState() {
    super.initState();
    _currentPage = widget.initialPage;
    _pageController = PageController(initialPage: widget.initialPage);
    _pageController.addListener(() {
      setState(() {
        _currentPage = _pageController.page?.round() ?? 0;
      });
    });
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        leading: IconButton(
          icon: Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.of(context).pop(),
        ),
        title: Text(
          '${_currentPage + 1} / ${widget.gallery.length}',
          style: TextStyle(color: Colors.white),
        ),
      ),
      body: Dismissible(
        key: Key("FullScreenGalleryView"),
        direction: DismissDirection.vertical,
        onDismissed: (_) => Navigator.pop(context),
        child: Center(
          child: PageView.builder(
            controller: _pageController,
            itemBuilder: (context, index) {
              final image = widget.gallery.get_(index: index);
              return switch (image) {
                Source_Image() => ImageThumbnail.fromGalleryImage(image.source),
                Source_AnimatedImage() => _GifContent(
                    url: image.source.mp4,
                    width: image.source.x,
                    height: image.source.y),
              };
            },
            itemCount: widget.gallery.length,
          ),
        ),
      ),
    );
  }
}

class _GifContent extends StatefulWidget {
  final String url;
  final int width;
  final int height;
  const _GifContent({
    super.key,
    required this.url,
    required this.width,
    required this.height,
  });

  @override
  State<_GifContent> createState() => _GifContentState();
}

class _GifContentState extends State<_GifContent> {
  late VideoPlayerController _controller;
  late ChewieController _chewieController;
  int width = 1;
  int height = 1;

  @override
  void initState() {
    super.initState();
    final url = widget.url;
    _controller = VideoPlayerController.networkUrl(Uri.parse(url));
    _chewieController = ChewieController(
      videoPlayerController: _controller,
      autoPlay: true,
      looping: true,
      aspectRatio: widget.width / widget.height,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: AspectRatio(
        aspectRatio: width / height,
        child: Chewie(
          controller: _chewieController,
        ),
      ),
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    _chewieController.dispose();
    super.dispose();
  }
}
