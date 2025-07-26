import 'dart:math';
import 'package:crabir/media/media.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/gallery.dart';

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
    double aspectRatio = double.infinity;
    for (int i = 0; i < widget.gallery.length; i++) {
      final image = widget.gallery.get_(index: i);
      aspectRatio = min(aspectRatio, image.width / image.height);
    }

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
                return ImageThumbnail.imageBase(image);
              },
              itemCount: widget.gallery.length,
            ),
          ),
        ),
        Positioned(
          top: 4,
          right: 4,
          child: Container(
            padding: EdgeInsets.all(4),
            decoration: BoxDecoration(
              color: Colors.black54,
              borderRadius: BorderRadius.circular(6),
            ),
            child: Text(
              '${_currentPage + 1} / ${widget.gallery.length}',
              style: TextStyle(
                color: Colors.white,
                fontSize: 12,
              ),
            ),
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
              return ImageThumbnail.imageBase(
                image,
              );
            },
            itemCount: widget.gallery.length,
          ),
        ),
      ),
    );
  }
}
