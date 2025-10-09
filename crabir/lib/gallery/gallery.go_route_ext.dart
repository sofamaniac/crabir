// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'gallery.dart';

// **************************************************************************
// GoRouteDataGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
extension FullScreenGalleryViewBuilder on FullScreenGalleryView {
  static const String name = 'FullScreenGalleryView';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra =>
      {'gallery': gallery, 'initialPage': initialPage, 'post': post};

  void goNamed(BuildContext context) => context.goNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  void pushNamed(BuildContext context) => context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );
}
