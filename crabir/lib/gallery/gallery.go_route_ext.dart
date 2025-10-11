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
  Map<String, dynamic> get extra => {
        'gallery': gallery,
        'initialPage': initialPage,
        'post': post,
        'controller': controller
      };

  void goNamed(BuildContext context) => context.goNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  Future<T?> pushNamed<T extends Object?>(BuildContext context) =>
      context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  static FullScreenGalleryView fromExtra(Map<String, dynamic> extra) {
    return FullScreenGalleryView(
        gallery: extra['gallery'] as Gallery,
        initialPage: extra['initialPage'] as int,
        post: extra['post'] as Post?,
        controller: extra['controller'] as PageController?);
  }
}
