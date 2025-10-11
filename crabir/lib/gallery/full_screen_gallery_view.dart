part of 'gallery.dart';

@CrabirRoute()
class FullScreenGalleryView extends StatefulWidget {
  final Gallery gallery;
  final int initialPage;
  final Post? post;
  final PageController? controller;

  const FullScreenGalleryView({
    super.key,
    required this.gallery,
    required this.initialPage,
    this.post,
    this.controller,
  });

  @override
  State<FullScreenGalleryView> createState() => _FullScreenGalleryViewState();
}

class _FullScreenGalleryViewState extends State<FullScreenGalleryView> {
  late int _currentPage;
  late final PageController controller;
  late final bool _ownController;
  @override
  void initState() {
    super.initState();
    _currentPage = widget.initialPage;
    controller =
        widget.controller ?? PageController(initialPage: widget.initialPage);
  }

  @override
  void dispose() {
    if (_ownController) {
      controller.dispose();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FullscreenMediaView(
      post: widget.post,
      builder: (onTap) {
        return _GalleryPageViewer(
          controller: controller,
          gallery: widget.gallery,
          onTap: onTap,
          initialPage: widget.initialPage,
          obfuscate: false,
          onPageChanged: (index) {
            setState(() {
              _currentPage = index;
            });
          },
        );
      },
      trailing: Text(
        '${_currentPage + 1} / ${widget.gallery.length}',
        style: TextStyle(color: Colors.white),
      ),
      title: widget.gallery.getTitle(index: _currentPage),
    );
  }
}
