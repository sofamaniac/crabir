// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user.dart';

// **************************************************************************
// GoRouteDataGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
extension UserViewBuilder on UserView {
  static const String name = 'UserView';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra => {'username': username, 'tab': tab};

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

  static UserView fromExtra(Map<String, dynamic> extra) {
    return UserView(
        username: extra['username'] as String, tab: extra['tab'] as String);
  }
}
