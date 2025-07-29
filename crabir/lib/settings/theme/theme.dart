import 'package:flutter/material.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'theme.freezed.dart';

final Theme carbirTheme = Theme();

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
abstract class Theme with _$Theme {
  Theme._();
  factory Theme({
    /// Color of the foundation
    @Default(Colors.black) Color background,
    @Default(Colors.black) Color cardBackground,
    @Default(Colors.black) Color toolBarBackground,
    @Default(Colors.white) Color toolBarText,

    /// Buttons and widgets
    @Default(Color(0xffff6e40)) Color primaryColor,

    /// Communities and usernames
    @Default(Color(0xffff0000)) Color highlight,
    @Default(Color(0xFFF5F6F8)) Color postTitle,
    @Default(Color(0xFFB7B8BC)) Color readPost,
    @Default(Color(0xFF00FF00)) Color announcement,
    @Default(Color(0xFFF5F6F8)) Color contentText,
    @Default(Color(0xFF4B91E2)) Color linkColor,
  }) = _Theme;
}
