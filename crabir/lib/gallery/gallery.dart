import 'package:crabir/cartouche.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/network_status.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart' hide Image;
import 'package:crabir/src/rust/third_party/reddit_api/model/gallery.dart';
import 'package:go_router/go_router.dart';
import 'package:photo_view/photo_view.dart';
import 'package:photo_view/photo_view_gallery.dart';

part 'full_screen_gallery_view.dart';
part 'gallery.go_route_ext.dart';

class GalleryView extends StatelessWidget {
  final Post post;
  final void Function(VoteDirection)? onVote;
  final void Function(bool)? onSave;
  const GalleryView({super.key, required this.post, this.onVote, this.onSave});
  @override
  Widget build(BuildContext context) {
    final bool obfuscate =
        post.spoiler || (FiltersSettings.of(context).blurNSFW && post.over18);
    if (post.gallery != null) {
      return _GalleryView(
        post: post,
        gallery: post.gallery!,
        onVote: onVote,
        onSave: onSave,
        obfuscate: obfuscate,
      );
    } else if (post.isCrosspost) {
      final gallery = post.crosspostParentList[0].gallery;
      if (gallery != null) {
        return _GalleryView(
          post: post,
          gallery: gallery,
          onVote: onVote,
          onSave: onSave,
          obfuscate: obfuscate,
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
  final bool obfuscate;
  final Post post;

  const _GalleryView({
    required this.gallery,
    required this.obfuscate,
    this.onVote,
    this.onSave,
    required this.post,
  });

  @override
  State<_GalleryView> createState() => _GalleryViewState();
}

class _GalleryViewState extends State<_GalleryView> {
  int _currentPage = 0;
  late bool obfuscate;
  final PageController _controller = PageController();

  @override
  void initState() {
    super.initState();
    obfuscate = widget.obfuscate;
  }

  void _openFullScreen(int initialPage) {
    if (obfuscate) {
      setState(() {
        obfuscate = false;
      });
      return;
    }
    FullScreenGalleryView(
      gallery: widget.gallery,
      initialPage: initialPage,
      controller: _controller,
    ).pushNamed(context);
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
            obfuscate: obfuscate,
            controller: _controller,
            onPageChanged: (index) {
              setState(() {
                _currentPage = index;
              });
            },
            onTap: () {
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
  final bool obfuscate;
  final VoidCallback? onTap;
  const _GalleryPageViewer({
    required this.gallery,
    required this.obfuscate,
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
  late final bool _ownedController;
  late final PageController _controller;

  @override
  void initState() {
    super.initState();
    _controller =
        widget.controller ?? PageController(initialPage: widget.initialPage);
    _ownedController = widget.controller == null;

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
    if (_ownedController) {
      _controller.dispose();
    }
    super.dispose();
  }

  void _onPageChange(int index) {
    _currentPage = index;
    widget.onPageChanged?.call(index);
  }

  PhotoViewGalleryPageOptions toWidget(
    GalleryMedia content,
    Resolution resolution,
    bool obfuscate,
    int index,
  ) {
    final image = content.withResolution(resolution, obfuscate);
    final placeholder = content.withResolution(Resolution.low, obfuscate);
    final Widget imageWidget;
    switch (content.source) {
      case Source_AnimatedImage(:final source):
        if (obfuscate) {
          imageWidget = ImageThumbnail(
            imageUrl: image.u,
            width: image.x,
            height: image.y,
            placeholderUrl: placeholder.u,
          );
        }
        // In galleries video do not have alternate resolutions.
        return PhotoViewGalleryPageOptions.customChild(
          child: AnimatedContent(
            url: source.mp4,
            width: source.x,
            height: source.y,
            placeholderUrl: placeholder.u,
            goFullScreen: () {
              widget.onTap?.call();
            },
          ),
          onTapDown: (_, __, ___) => widget.onTap?.call(),
          minScale: PhotoViewComputedScale.contained * 1.0,
          maxScale: PhotoViewComputedScale.contained * 1.0,
        );
      case Source_Image():
        imageWidget = ImageThumbnail.fromGalleryImage(
          image,
          placeholderUrl: placeholder.u,
        );
    }
    return PhotoViewGalleryPageOptions.customChild(
      child: imageWidget,
      onTapDown: (_, __, ___) => widget.onTap?.call(),
      minScale: PhotoViewComputedScale.contained * 1.0,
      maxScale: PhotoViewComputedScale.contained * 5.0,
    );
  }

  @override
  Widget build(BuildContext context) {
    return PhotoViewGallery.builder(
      pageController: _controller,
      onPageChanged: _onPageChange,
      itemCount: widget.gallery.length,
      builder: (context, index) {
        final image = widget.gallery.get_(index: index);
        if (image == null) {
          return PhotoViewGalleryPageOptions.customChild(
            child: Text("Image not found"),
          );
        } else {
          return toWidget(
            image,
            NetworkStatus.imageQuality(context),
            widget.obfuscate,
            index,
          );
        }
      },
    );
  }
}
