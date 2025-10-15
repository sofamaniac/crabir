// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'search.dart';

// **************************************************************************
// GoRouteDataGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
extension SearchSubredditsViewBuilder on SearchSubredditsView {
  static const String name = 'SearchSubredditsView';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra => {
        'enableUser': enableUser,
        'enablePost': enablePost,
        'navigateOnTap': navigateOnTap
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

  static SearchSubredditsView fromExtra(Map<String, dynamic> extra) {
    return SearchSubredditsView(
        enableUser: extra['enableUser'] as bool,
        enablePost: extra['enablePost'] as bool,
        navigateOnTap: extra['navigateOnTap'] as bool);
  }
}
