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

part 'full_screen_gallery_view.dart';

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
          onVote: onVote,
          onSave: onSave,
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
    return Stack(
      children: [
        AspectRatio(
          aspectRatio: aspectRatio,
          child: _GalleryPageViewer(
            gallery: widget.gallery,
            onPageChanged: (index) {
              setState(() {
                _currentPage = index;
              });
            },
            onTap: (_, __, ___) {
              _openFullScreen(_currentPage);
            },
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

class _GalleryPageViewer extends StatefulWidget {
  final Gallery gallery;
  final void Function(int index)? onPageChanged;
  final int initialPage;
  final PageController? controller;
  final void Function(
    BuildContext,
    TapDownDetails,
    PhotoViewControllerValue,
  )? onTap;
  const _GalleryPageViewer({
    required this.gallery,
    this.onTap,
    this.onPageChanged,
    this.initialPage = 0,
    this.controller,
  });
  @override
  State<_GalleryPageViewer> createState() => _GalleryPageViewerState();
}

class _GalleryPageViewerState extends State<_GalleryPageViewer> {
  int _currentPage = 0;
  late final PageController _controller;

  @override
  void initState() {
    super.initState();
    _controller = PageController(initialPage: widget.initialPage);

    if (widget.controller == null) {
      // Ensure first frame jumps to the correct page, without that it jumps to
      // the last page when scrolling into view.
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _controller.jumpToPage(_currentPage);
      });
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  void _onPageChange(int index) {
    _currentPage = index;
    widget.onPageChanged?.call(index);
  }

  Widget toWidget(
    GalleryMedia content,
    Resolution resolution,
    bool obfuscate,
    int index,
  ) {
    final List<Image> resolutions;
    if (obfuscate) {
      resolutions = content.obfuscated;
    } else {
      resolutions = content.previews;
    }
    final placeholder = resolutions.withResolution(resolution);
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
          return ImageThumbnail.fromGalleryImage(
            source,
            placeholderUrl: placeholder.u,
          );
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
    return PhotoViewGallery.builder(
      pageController: widget.controller ?? _controller,
      onPageChanged: _onPageChange,
      itemCount: widget.gallery.length,
      builder: (context, index) {
        final settings = context.watch<DataSettingsCubit>().state;
        final image = widget.gallery.get_(index: index)!;
        final Widget imageWidget = toWidget(
          image,
          settings.preferredQuality,
          false,
          index,
        );
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
