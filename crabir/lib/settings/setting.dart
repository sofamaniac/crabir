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
@SettingsPage(prefix: "comments_", useFieldName: true)
abstract class CommentsSettings with _$CommentsSettings {
  CommentsSettings._();
  factory CommentsSettings({
    @Default(CommentSort.top) CommentSort sort,
    @Setting() @Default(true) bool useSuggestedSort,
    @Setting() @Default(true) bool showNavigationBar,
    @Setting() @Default(true) bool showUserAvatar,
    //@Default() MediaPreviewSize postMediaPreviewSize,
    @Setting() @Default(true) bool buttonsAlwaysVisible,
    @Setting() @Default(true) bool hideButtonAfterAction,
    @Setting() @Default(true) bool collapseAutoMod,
    @Setting() @Default(true) bool collapseDisruptiveComment,
    @Setting() @Default(true) bool showPostUpvotePercentage,
    //@Default() GuideStyle threadGuide,
    @Setting() @Default(true) bool highlightMyUsername,
    @Setting() @Default(true) bool showFloatingButton,
    @Setting() @Default(true) bool showAwards,
    @Setting() @Default(true) bool clickableAwards,
    @Setting() @Default(true) bool showUserFlair,
    @Setting() @Default(true) bool showFlairColors,
    @Setting() @Default(true) bool showFlairEmojis,
    @Setting() @Default(true) bool clickToCollapse,
    @Setting() @Default(true) bool hideTextCollapsed,
    @Setting() @Default(true) bool loadCollapsed,
    @Setting() @Default(true) bool animateCollapse,
    @Setting() @Default(true) bool clickableUsername,
    @Setting() @Default(true) bool highlightNewComments,
    //@Default() NavigationMode defaultNavigationMode,
    @Setting() @Default(true) bool volumeRockerNavigation,
    @Setting() @Default(true) bool animateNavigation,
    @Setting() @Default(true) bool showSaveButton,
    //@Default() String goToTopButton,
    @Setting() @Default(true) bool swipeToClose,
  }) = _CommentsSetttings;
}
