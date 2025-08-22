import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'setting.freezed.dart';

class BoolSetting extends StatelessWidget {
  /// The name to display
  final String Function(AppLocalizations locales) title;

  /// An explanation of the setting
  final String Function(AppLocalizations locales)? description;

  /// The icon to use.
  final IconData? icon;

  final bool value;

  final void Function(bool)? onChange;
  const BoolSetting({
    super.key,
    required this.title,
    this.description,
    this.icon,
    this.onChange,
    this.value = true,
  });

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context)!;
    final title = this.title(locales);
    final desc = description != null ? description!(locales) : null;
    return SwitchListTile(
      title: Text(title),
      subtitle: desc != null ? Text(desc) : null,
      isThreeLine: desc != null,
      value: value,
      onChanged: onChange,
    );
  }
}

@freezed
@SettingsPage()
abstract class CommentsSettings with _$CommentsSettings {
  CommentsSettings._();
  factory CommentsSettings({
    @Default(CommentSort.top) CommentSort sort,
    @Default(true) bool useSuggestedSort,
    @Default(true) bool showNavigationBar,
    @Default(true) bool showUserAvatar,
    //@Default() MediaPreviewSize postMediaPreviewSize,
    @Default(true) bool buttonsAlwaysVisible,
    @Default(true) bool hideButtonAfterAction,
    @Default(true) bool collapseAutoMod,
    @Default(true) bool collapseDisruptiveComment,
    @Default(true) bool showPostUpvotePercentage,
    //@Default() GuideStyle threadGuide,
    @Default(true) bool highlightMyUsername,
    @Default(true) bool showFloatingButton,
    @Default(true) bool showAwards,
    @Default(true) bool clickableAwards,
    @Default(true) bool showUserFlair,
    @Default(true) bool showFlairColors,
    @Default(true) bool showFlairEmojis,
    @Default(true) bool clickToCollapse,
    @Default(true) bool hideTextCollapsed,
    @Default(true) bool loadCollapsed,
    @Default(true) bool animateCollapse,
    @Default(true) bool clickableUsername,
    @Default(true) bool highlightNewComments,
    //@Default() NavigationMode defaultNavigationMode,
    @Default(true) bool volumeRockerNavigation,
    @Default(true) bool animateNavigation,
    @Default(true) bool showSaveButton,
    //@Default() String goToTopButton,
    @Default(true) bool swipeToClose,
  }) = _CommentsSetttings;
}
