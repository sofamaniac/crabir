import 'package:auto_route/auto_route.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/data/data_settings.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart' hide Image;
import 'package:crabir/src/rust/third_party/reddit_api/model/gallery.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:photo_view/photo_view.dart';
import 'package:photo_view/photo_view_gallery.dart';

class GalleryView extends StatelessWidget {
  final Post post;
  final void Function(VoteDirection)? onVote;
  final void Function(bool)? onSave;
  const GalleryView({super.key, required this.post, this.onVote, this.onSave});
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
        return Text("Error: Gallery and is Crosspost but no gallery on parent");
      }
    } else {
      return Text("Error: Gallery: not Crosspost && no gallery on post");
    }
  }
}

class _GalleryView extends StatefulWidget {
  final Gallery gallery;
  final void Function(VoteDirection)? onVote;
  final void Function(bool)? onSave;

  const _GalleryView({required this.gallery, this.onVote, this.onSave});

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
    return FullscreenMediaView(
      builder: (onTap) => _GalleryPageViewer(
        gallery: widget.gallery,
        controller: controller,
        onTap: onTap,
      ),
      trailing: Text(
        '${_currentPage + 1} / ${widget.gallery.length}',
        style: TextStyle(color: Colors.white),
      ),
    );
  }
}

class _GalleryPageViewer extends StatefulWidget {
  final Gallery gallery;
  final PageController controller;
  final void Function(
    BuildContext,
    TapDownDetails,
    PhotoViewControllerValue,
  )? onTap;
  const _GalleryPageViewer({
    required this.gallery,
    required this.controller,
    this.onTap,
  });
  @override
  State<_GalleryPageViewer> createState() => _GalleryPageViewerState();
}

class _GalleryPageViewerState extends State<_GalleryPageViewer> {
  int _currentPage = 0;

  @override
  void dispose() {
    widget.controller.dispose();
    super.dispose();
  }

  void _onPageChange(int index) {
    setState(() {
      _currentPage = widget.controller.page?.round() ?? 0;
    });
  }

  Widget toWidget(
      GalleryMedia content, Resolution resolution, bool obfuscate, int index) {
    final List<Image> resolutions;
    if (obfuscate) {
      resolutions = content.obfuscated;
    } else {
      resolutions = content.previews;
    }
    final placeholder = switch (resolution) {
      Resolution.source || Resolution.high => resolutions.last,
      Resolution.medium => resolutions[(resolutions.length / 2).toInt()],
      Resolution.low => resolutions.first,
    };
    switch (content.source) {
      case Source_AnimatedImage(:final source):
        // In galleries video do not have alternate resolutions.
        return AnimatedContent(
          url: source.mp4,
          width: source.x,
          height: source.y,
          placeholderUrl: placeholder.u,
          shouldPlay: index == _currentPage,
        );
      case Source_Image(:final source):
        if (resolution case Resolution.source) {
          return ImageThumbnail.fromGalleryImage(source);
        } else {
          return ImageThumbnail.fromGalleryImage(
            placeholder,
            placeholderUrl: resolutions.first.u,
          );
        }
    }
  }

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<DataSettingsCubit>().state;
    return PhotoViewGallery.builder(
      pageController: widget.controller,
      onPageChanged: _onPageChange,
      itemCount: widget.gallery.length,
      builder: (context, index) {
        final image = widget.gallery.get_(index: index)!;
        // TODO: handle resolution
        final Widget imageWidget = toWidget(
          image,
          settings.preferredQuality,
          false,
          index,
        );
        // TODO: display title if any
        return PhotoViewGalleryPageOptions.customChild(
          child: imageWidget,
          onTapDown: widget.onTap,
          minScale: PhotoViewComputedScale.contained * 1.0,
          maxScale: PhotoViewComputedScale.contained * 5.0,
        );
      },
    );
  }
}
