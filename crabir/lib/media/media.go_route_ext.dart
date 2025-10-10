// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'media.dart';

// **************************************************************************
// GoRouteDataGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
extension FullscreenImageViewBuilder on FullscreenImageView {
  static const String name = 'FullscreenImageView';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra =>
      {'imageUrl': imageUrl, 'title': title, 'post': post};

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

  static FullscreenImageView fromExtra(Map<String, dynamic> extra) {
    return FullscreenImageView(
        imageUrl: extra['imageUrl'] as String,
        title: extra['title'] as String?,
        post: extra['post'] as Post?);
  }
}

// GENERATED CODE - DO NOT MODIFY BY HAND
extension FullscreenVideoViewBuilder on FullscreenVideoView {
  static const String name = 'FullscreenVideoView';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra => {
        'videoUrl': videoUrl,
        'title': title,
        'width': width,
        'height': height,
        'post': post
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

  static FullscreenVideoView fromExtra(Map<String, dynamic> extra) {
    return FullscreenVideoView(
        videoUrl: extra['videoUrl'] as String,
        title: extra['title'] as String?,
        width: extra['width'] as int,
        height: extra['height'] as int,
        post: extra['post'] as Post?);
  }
}
