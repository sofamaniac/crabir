// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'feed.dart';

// **************************************************************************
// GoRouteDataGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
extension FeedViewBuilder on FeedView {
  static const String name = 'FeedView';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra =>
      {'feed': feed, 'subreddit': subreddit, 'initialSort': initialSort};

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
