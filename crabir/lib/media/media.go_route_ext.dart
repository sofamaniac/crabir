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

  void pushNamed(BuildContext context) => context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );
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

  void pushNamed(BuildContext context) => context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );
}
