import 'package:auto_route/auto_route.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/gallery.dart';

class GalleryView extends StatelessWidget {
  final Post post;
  const GalleryView({super.key, required this.post});
  @override
  Widget build(BuildContext context) {
    if (post.gallery != null) {
      return _GalleryView(
        gallery: post.gallery!,
      );
    } else if (post.isCrosspost) {
      final gallery = post.crosspostParentList[0].gallery;
      if (gallery != null) {
        return _GalleryView(
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

class _GalleryView extends StatefulWidget {
  final Gallery gallery;

  const _GalleryView({required this.gallery});

  @override
  State<_GalleryView> createState() => _GalleryViewState();
}

class _GalleryViewState extends State<_GalleryView> {
  int _currentPage = 0;

  void _openFullScreen(int initialPage) {
    context.router.navigate(
      FullScreenGalleryRoute(
        gallery: widget.gallery,
        initialPage: initialPage,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final aspectRatio = widget.gallery.aspectRatio;
    final controller = PageController(initialPage: 0);
    controller.addListener(() {
      setState(() {
        _currentPage = controller.page?.round() ?? 0;
      });
    });
    return Stack(
      children: [
        AspectRatio(
          aspectRatio: aspectRatio,
          child: GestureDetector(
            onTap: () {
              _openFullScreen(_currentPage);
            },
            child: _GalleryPageViewer(
              gallery: widget.gallery,
              controller: controller,
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

@RoutePage()
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
  late int _currentPage;
  @override
  void initState() {
    super.initState();
    _currentPage = widget.initialPage;
  }

  @override
  Widget build(BuildContext context) {
    final controller = PageController(initialPage: widget.initialPage);
    controller.addListener(() {
      setState(() {
        _currentPage = controller.page?.round() ?? 0;
      });
    });
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
          child: _GalleryPageViewer(
            gallery: widget.gallery,
            controller: controller,
          )),
    );
  }
}

class _GalleryPageViewer extends StatefulWidget {
  final Gallery gallery;
  final PageController controller;
  const _GalleryPageViewer({
    required this.gallery,
    required this.controller,
  });
  @override
  State<_GalleryPageViewer> createState() => _GalleryPageViewerState();
}

class _GalleryPageViewerState extends State<_GalleryPageViewer> {
  @override
  void dispose() {
    widget.controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: PageView.builder(
        controller: widget.controller,
        itemBuilder: (context, index) {
          final image = widget.gallery.get_(index: index);
          // TODO: handle resolution
          // TODO: display title if any
          return switch (image) {
            Source_Image() => ImageThumbnail.fromGalleryImage(image.source),
            Source_AnimatedImage() => AnimatedContent(
                url: image.source.mp4,
                width: image.source.x,
                height: image.source.y,
              ),
          };
        },
        itemCount: widget.gallery.length,
      ),
    );
  }
}
