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
  late final PageController _controller;
  late final bool _ownController;
  @override
  void initState() {
    super.initState();
    _currentPage = widget.initialPage;
    _controller =
        widget.controller ?? PageController(initialPage: widget.initialPage);
    _ownController = widget.controller == null;
  }

  @override
  void dispose() {
    if (_ownController) {
      _controller.dispose();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FullscreenMediaView(
      post: widget.post,
      onPop: () {
        return _currentPage;
      },
      builder: (onTap) {
        return _GalleryPageViewer(
          controller: _controller,
          gallery: widget.gallery,
          onTap: onTap,
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
