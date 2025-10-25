import 'dart:ui';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/network_status.dart';
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

class GalleryView extends StatefulWidget {
  final Post post;
  final void Function(VoteDirection)? onVote;
  final void Function(bool)? onSave;
  final BoxFit fit;
  final bool blurBackground;
  final double maxScale;
  final bool obfuscate;
  const GalleryView({
    super.key,
    required this.post,
    this.onVote,
    this.onSave,
    this.fit = BoxFit.contain,
    required this.blurBackground,
    required this.maxScale,
    required this.obfuscate,
  });

  @override
  State<GalleryView> createState() => _GalleryViewState();
}

class _GalleryViewState extends State<GalleryView> {
  int _currentPage = 0;
  late bool obfuscate;
  final PageController _controller = PageController(keepPage: false);
  late final Gallery? gallery;

  @override
  void initState() {
    super.initState();
    obfuscate = widget.obfuscate;
    gallery = widget.post.gallery;
  }

  void _openFullScreen(int initialPage) async {
    if (obfuscate) {
      setState(() {
        obfuscate = false;
      });
      return;
    }
    _currentPage = await FullScreenGalleryView(
      gallery: gallery!,
      initialPage: initialPage,
    ).pushNamed(context) as int;
    _controller.jumpToPage(_currentPage);
  }

  @override
  Widget build(BuildContext context) {
    if (gallery == null) {
      return Text("No Gallery found");
    } else {
      final aspectRatio = gallery!.aspectRatio;
      return Stack(
        children: [
          AspectRatio(
            aspectRatio: aspectRatio,
            child: _GalleryPageViewer(
              gallery: gallery!,
              obfuscate: obfuscate,
              controller: _controller,
              fit: widget.fit,
              maxScale: widget.maxScale,
              blurBackround: widget.blurBackground,
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
              '${_currentPage + 1} / ${gallery!.length}',
              background: Colors.black54,
              foreground: Colors.white,
            ),
          ),
        ],
      );
    }
  }
}

class _GalleryPageViewer extends StatefulWidget {
  final Gallery gallery;
  final void Function(int index)? onPageChanged;
  final PageController? controller;
  final bool obfuscate;
  final VoidCallback? onTap;
  final BoxFit fit;
  final bool blurBackround;
  final double maxScale;
  const _GalleryPageViewer({
    required this.gallery,
    required this.obfuscate,
    this.onTap,
    this.onPageChanged,
    this.controller,
    this.fit = BoxFit.contain,
    required this.blurBackround,
    required this.maxScale,
  });
  @override
  State<_GalleryPageViewer> createState() => _GalleryPageViewerState();
}

class _GalleryPageViewerState extends State<_GalleryPageViewer> {
  late final bool _ownedController;
  late final PageController _controller;

  @override
  void initState() {
    super.initState();
    _controller = widget.controller ?? PageController();
    _ownedController = widget.controller == null;
  }

  @override
  void dispose() {
    if (_ownedController) {
      _controller.dispose();
    }
    super.dispose();
  }

  void _onPageChange(int index) {
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
    Widget imageWidget;
    switch (content.source) {
      case Source_AnimatedImage(:final source):
        if (obfuscate) {
          imageWidget = ImageThumbnail(
            imageUrl: image.u,
            width: image.x,
            height: image.y,
            placeholderUrl: placeholder.u,
            fit: widget.fit,
          );
        } else {
          // In galleries video do not have alternate resolutions.
          imageWidget = AnimatedContent(
            url: source.mp4,
            width: source.x,
            height: source.y,
            placeholderUrl: placeholder.u,
            goFullScreen: () {
              widget.onTap?.call();
            },
          );
        }
      case Source_Image():
        imageWidget = ImageThumbnail.fromGalleryImage(
          image,
          placeholderUrl: placeholder.u,
          fit: widget.fit,
        );
    }
    if (widget.blurBackround) {
      imageWidget = Container(
        decoration: BoxDecoration(
          image: DecorationImage(
            image: CachedNetworkImageProvider(placeholder.u),
            fit: BoxFit.fill,
          ),
        ),
        child: BackdropFilter(
          filter: ImageFilter.blur(sigmaX: 10, sigmaY: 10),
          child: imageWidget,
        ),
      );
    }
    return PhotoViewGalleryPageOptions.customChild(
      child: imageWidget,
      minScale: PhotoViewComputedScale.contained * 1.0,
      maxScale: PhotoViewComputedScale.contained * widget.maxScale,
      disableGestures: widget.maxScale == 1,
    );
  }

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () => widget.onTap?.call(),
      child: PhotoViewGallery.builder(
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
      ),
    );
  }
}
