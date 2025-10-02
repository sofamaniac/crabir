import 'package:auto_route/auto_route.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:crabir/utils/icons.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';

part 'lateral_menu_settings.settings_page.dart';
part 'lateral_menu_settings.freezed.dart';
part 'lateral_menu_settings.g.dart';

@freezed
@SettingsPage(prefix: "lateralMenu_", useFieldName: true)
abstract class LateralMenuSettings with _$LateralMenuSettings {
  LateralMenuSettings._();
  factory LateralMenuSettings({
    @Category(name: "Items to show")
    @Default(true)
    @Setting(hasDescription: true, icon: HOME_PAGE_ICON)
    bool showHome,
    @Default(true)
    @Setting(icon: HOME_FEED_ICON, hasDescription: true)
    bool showHomeFeed,
    @Default(true) @Setting(icon: POPULAR_ICON) bool showPopular,
    @Default(true) @Setting(icon: ALL_ICON) bool showAll,
    @Default(true) @Setting(icon: SAVED_FEED_ICON) bool showSaved,
    @Default(true) @Setting(icon: HISTORY_ICON) bool showHistory,
    @Default(true) @Setting(icon: PROFILE_ICON) bool showProfile,
    @Default(true) @Setting(icon: INBOX_ICON) bool showInbox,
    @Default(true) @Setting(icon: FRIENDS_ICON) bool showFriends,
    @Default(true) @Setting(icon: DRAFTS_ICON) bool showDrafts,
    @Default(true) @Setting(icon: MODERATION_ICON) bool showModeration,
    @Default(true) @Setting(icon: SEARCH_ICON) bool showSearch,
    @Default(true) @Setting(icon: GOTO_ICON) bool showGoToDropdown,
    @Default(true) @Setting(icon: GOTO_COMMUNITY_ICON) bool showGoToCommunity,
    @Default(true) @Setting(icon: GOTO_USER_ICON) bool showGoToUser,
    @Default(true) @Setting(icon: DARK_MODE_ICON) bool darkMode,
    @Default(true) @Setting(icon: SHOW_NSFW_ICON) bool showNSFW,
    @Default(true) @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW,
    @Category(name: "Subscription list")
    @Default(true)
    @Setting()
    bool showSubsInMenu,
    @Default(true) @Setting() bool showSubsIcon,
    @Default(false) @Setting() bool showSubsFavOnly,
    @Category(name: "Account switcher")
    @Default(true)
    @Setting()
    bool showAccountAvatar,
    @Default(true) @Setting() bool showAccountUsername,
  }) = _LateralMenuSettings;

  factory LateralMenuSettings.fromJson(Map<String, dynamic> json) =>
      _$LateralMenuSettingsFromJson(json);

  factory LateralMenuSettings.of(BuildContext context) =>
      context.watch<LateralMenuSettingsCubit>().state;
}
