import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/settings/theme/color_converter.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
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
  secondaryText,
  downvote;

  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return switch (this) {
      ThemeField.background => locales.themeBackround,
      ThemeField.cardBackground => locales.themeCardBackground,
      ThemeField.toolBarBackground => locales.themeToolbarBackground,
      ThemeField.toolBarText => locales.themeToolbarText,
      ThemeField.primaryColor => locales.themePrimaryColor,
      ThemeField.highlight => locales.themeHighlight,
      ThemeField.postTitle => locales.themePostTitle,
      ThemeField.readPost => locales.themeReadPost,
      ThemeField.announcement => locales.themeAnnouncement,
      ThemeField.contentText => locales.themeContentText,
      ThemeField.linkColor => locales.themeLinkColor,
      ThemeField.secondaryText => locales.themeSecondaryText,
      ThemeField.downvote => locales.themeDownvote,
    };
  }
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
    @ColorConverter() @Default(Color(0xFFB7B8BC)) Color secondaryText,

    /// Communities and usernames
    @ColorConverter() @Default(Color(0xffff0000)) Color highlight,
    @ColorConverter() @Default(Color(0xFFF5F6F8)) Color postTitle,
    @ColorConverter() @Default(Color(0xFFB7B8BC)) Color readPost,
    @ColorConverter() @Default(Color(0xFF00FF00)) Color announcement,
    @ColorConverter() @Default(Color(0xFFF5F6F8)) Color contentText,
    @ColorConverter() @Default(Color(0xFF4B91E2)) Color linkColor,
    @ColorConverter() @Default(Color(0xFF9580FF)) Color downvoteContent,
  }) = _CrabirTheme;

  factory CrabirTheme.light() => CrabirTheme(
        background: Colors.white,
        cardBackground: const Color(0xFFF5F5F5),
        toolBarBackground: Colors.white,
        toolBarText: Colors.black,
        primaryColor: Colors.deepOrange,
        highlight: Colors.red,
        postTitle: Colors.black87,
        readPost: Colors.grey,
        announcement: Colors.green,
        contentText: Colors.black87,
        linkColor: Colors.blue,
        downvoteContent: const Color(0XFF9580FF),
      );

  factory CrabirTheme.fromJson(Map<String, dynamic> json) =>
      _$CrabirThemeFromJson(json);

  static CrabirTheme of(BuildContext context) {
    final state = context.watch<ThemeBloc>().state;
    switch (state.mode) {
      case ThemeMode.dark:
        return state.dark;
      case ThemeMode.light:
        return state.light;
      case ThemeMode.system:
        final brightness = MediaQuery.of(context).platformBrightness;
        return switch (brightness) {
          Brightness.dark => state.dark,
          Brightness.light => state.light,
        };
    }
  }

  CrabirTheme updateColor({required ThemeField field, required Color color}) {
    final newTheme = switch (field) {
      ThemeField.background => copyWith(background: color),
      ThemeField.cardBackground => copyWith(cardBackground: color),
      ThemeField.toolBarBackground => copyWith(toolBarBackground: color),
      ThemeField.toolBarText => copyWith(toolBarText: color),
      ThemeField.primaryColor => copyWith(primaryColor: color),
      ThemeField.highlight => copyWith(highlight: color),
      ThemeField.postTitle => copyWith(postTitle: color),
      ThemeField.readPost => copyWith(readPost: color),
      ThemeField.announcement => copyWith(announcement: color),
      ThemeField.contentText => copyWith(contentText: color),
      ThemeField.linkColor => copyWith(linkColor: color),
      ThemeField.secondaryText => copyWith(secondaryText: color),
      ThemeField.downvote => copyWith(downvoteContent: color),
    };
    return newTheme;
  }

  Color fromField(ThemeField field) {
    return switch (field) {
      ThemeField.background => background,
      ThemeField.cardBackground => cardBackground,
      ThemeField.toolBarBackground => toolBarBackground,
      ThemeField.toolBarText => toolBarText,
      ThemeField.primaryColor => primaryColor,
      ThemeField.highlight => highlight,
      ThemeField.postTitle => postTitle,
      ThemeField.readPost => readPost,
      ThemeField.announcement => announcement,
      ThemeField.contentText => contentText,
      ThemeField.linkColor => linkColor,
      ThemeField.secondaryText => secondaryText,
      ThemeField.downvote => downvoteContent,
    };
  }
}
