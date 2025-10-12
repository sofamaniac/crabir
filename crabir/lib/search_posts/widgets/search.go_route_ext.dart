// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'search.dart';

// **************************************************************************
// GoRouteDataGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
extension SearchPostsViewBuilder on SearchPostsView {
  static const String name = 'SearchPostsView';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra =>
      {'query': query, 'flair': flair, 'subreddit': subreddit};

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

  static SearchPostsView fromExtra(Map<String, dynamic> extra) {
    return SearchPostsView(
        query: extra['query'] as String,
        flair: extra['flair'] as Flair?,
        subreddit: extra['subreddit'] as String?);
  }
}
