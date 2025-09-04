import 'package:crabir/settings/theme/color_converter.dart';
import 'package:flutter/material.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'theme.freezed.dart';
part 'theme.g.dart';

final CrabirTheme carbirTheme = CrabirTheme();

enum ThemeField {
  background,
  cardBackground,
  toolBarBackground,
  toolBarText,
  primaryColor,
  highlight,
  postTitle,
  readPost,
  announcement,
  contentText,
  linkColor,
}

@freezed
abstract class CrabirTheme with _$CrabirTheme {
  CrabirTheme._();
  factory CrabirTheme({
    /// Color of the foundation
    @ColorConverter() @Default(Colors.black) Color background,
    @ColorConverter() @Default(Colors.black) Color cardBackground,
    @ColorConverter() @Default(Colors.black) Color toolBarBackground,
    @ColorConverter() @Default(Colors.white) Color toolBarText,

    /// Buttons and widgets
    @ColorConverter() @Default(Color(0xffff6e40)) Color primaryColor,

    /// Communities and usernames
    @ColorConverter() @Default(Color(0xffff0000)) Color highlight,
    @ColorConverter() @Default(Color(0xFFF5F6F8)) Color postTitle,
    @ColorConverter() @Default(Color(0xFFB7B8BC)) Color readPost,
    @ColorConverter() @Default(Color(0xFF00FF00)) Color announcement,
    @ColorConverter() @Default(Color(0xFFF5F6F8)) Color contentText,
    @ColorConverter() @Default(Color(0xFF4B91E2)) Color linkColor,
  }) = _CrabirTheme;

  factory CrabirTheme.fromJson(Map<String, dynamic> json) =>
      _$CrabirThemeFromJson(json);
}
