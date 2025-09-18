part of 'gallery.dart';

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
  late final PageController controller;
  @override
  void initState() {
    super.initState();
    _currentPage = widget.initialPage;
    controller = PageController(initialPage: widget.initialPage);
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FullscreenMediaView(
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
