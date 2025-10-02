// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// SettingsPageGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
part of "lateral_menu_settings.dart";

// HydratedCubit for LateralMenuSettings
class LateralMenuSettingsCubit extends HydratedCubit<LateralMenuSettings> {
  final Logger log = Logger("LateralMenuSettingsCubit");
  LateralMenuSettingsCubit() : super(LateralMenuSettings());

  @override
  LateralMenuSettings? fromJson(Map<String, dynamic> json) {
    try {
      log.info("Successfully restored LateralMenuSettingsCubit");
      return LateralMenuSettings.fromJson(json);
    } catch (err) {
      log.severe("Failed to resotre LateralMenuSettingsCubit: $err");
      return LateralMenuSettings();
    }
  }

  @override
  Map<String, dynamic>? toJson(LateralMenuSettings state) => state.toJson();
  void updateShowHome(bool value) => emit(state.copyWith(showHome: value));

  void updateShowHomeFeed(bool value) =>
      emit(state.copyWith(showHomeFeed: value));

  void updateShowPopular(bool value) =>
      emit(state.copyWith(showPopular: value));

  void updateShowAll(bool value) => emit(state.copyWith(showAll: value));

  void updateShowSaved(bool value) => emit(state.copyWith(showSaved: value));

  void updateShowHistory(bool value) =>
      emit(state.copyWith(showHistory: value));

  void updateShowProfile(bool value) =>
      emit(state.copyWith(showProfile: value));

  void updateShowInbox(bool value) => emit(state.copyWith(showInbox: value));

  void updateShowFriends(bool value) =>
      emit(state.copyWith(showFriends: value));

  void updateShowDrafts(bool value) => emit(state.copyWith(showDrafts: value));

  void updateShowModeration(bool value) =>
      emit(state.copyWith(showModeration: value));

  void updateShowSearch(bool value) => emit(state.copyWith(showSearch: value));

  void updateShowGoToDropdown(bool value) =>
      emit(state.copyWith(showGoToDropdown: value));

  void updateShowGoToCommunity(bool value) =>
      emit(state.copyWith(showGoToCommunity: value));

  void updateShowGoToUser(bool value) =>
      emit(state.copyWith(showGoToUser: value));

  void updateDarkMode(bool value) => emit(state.copyWith(darkMode: value));

  void updateShowNSFW(bool value) => emit(state.copyWith(showNSFW: value));

  void updateBlurNSFW(bool value) => emit(state.copyWith(blurNSFW: value));

  void updateShowSubsInMenu(bool value) =>
      emit(state.copyWith(showSubsInMenu: value));

  void updateShowSubsIcon(bool value) =>
      emit(state.copyWith(showSubsIcon: value));

  void updateShowSubsFavOnly(bool value) =>
      emit(state.copyWith(showSubsFavOnly: value));

  void updateShowAccountAvatar(bool value) =>
      emit(state.copyWith(showAccountAvatar: value));

  void updateShowAccountUsername(bool value) =>
      emit(state.copyWith(showAccountUsername: value));
}

// SettingsPage for LateralMenuSettings
@RoutePage()
class LateralMenuSettingsView extends StatelessWidget {
  const LateralMenuSettingsView({super.key});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final settings = context.watch<LateralMenuSettingsCubit>().state;
    return Scaffold(
        body: ListView(
      children: [
        Divider(),
        Text("Items to show"),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showHome),
          secondary: Icon(HOME_PAGE_ICON),
          subtitle: Text(locales.lateralMenu_showHomeDescription),
          value: settings.showHome,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowHome(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showHomeFeed),
          secondary: Icon(HOME_FEED_ICON),
          subtitle: Text(locales.lateralMenu_showHomeFeedDescription),
          value: settings.showHomeFeed,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowHomeFeed(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showPopular),
          secondary: Icon(POPULAR_ICON),
          subtitle: null,
          value: settings.showPopular,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowPopular(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showAll),
          secondary: Icon(ALL_ICON),
          subtitle: null,
          value: settings.showAll,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowAll(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showSaved),
          secondary: Icon(SAVED_FEED_ICON),
          subtitle: null,
          value: settings.showSaved,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowSaved(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showHistory),
          secondary: Icon(HISTORY_ICON),
          subtitle: null,
          value: settings.showHistory,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowHistory(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showProfile),
          secondary: Icon(PROFILE_ICON),
          subtitle: null,
          value: settings.showProfile,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowProfile(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showInbox),
          secondary: Icon(INBOX_ICON),
          subtitle: null,
          value: settings.showInbox,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowInbox(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showFriends),
          secondary: Icon(FRIENDS_ICON),
          subtitle: null,
          value: settings.showFriends,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowFriends(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showDrafts),
          secondary: Icon(DRAFTS_ICON),
          subtitle: null,
          value: settings.showDrafts,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowDrafts(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showModeration),
          secondary: Icon(MODERATION_ICON),
          subtitle: null,
          value: settings.showModeration,
          onChanged: (val) => context
              .read<LateralMenuSettingsCubit>()
              .updateShowModeration(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showSearch),
          secondary: Icon(SEARCH_ICON),
          subtitle: null,
          value: settings.showSearch,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowSearch(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showGoToDropdown),
          secondary: Icon(GOTO_ICON),
          subtitle: null,
          value: settings.showGoToDropdown,
          onChanged: (val) => context
              .read<LateralMenuSettingsCubit>()
              .updateShowGoToDropdown(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showGoToCommunity),
          secondary: Icon(GOTO_COMMUNITY_ICON),
          subtitle: null,
          value: settings.showGoToCommunity,
          onChanged: (val) => context
              .read<LateralMenuSettingsCubit>()
              .updateShowGoToCommunity(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showGoToUser),
          secondary: Icon(GOTO_USER_ICON),
          subtitle: null,
          value: settings.showGoToUser,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowGoToUser(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_darkMode),
          secondary: Icon(DARK_MODE_ICON),
          subtitle: null,
          value: settings.darkMode,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateDarkMode(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showNSFW),
          secondary: Icon(SHOW_NSFW_ICON),
          subtitle: null,
          value: settings.showNSFW,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowNSFW(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_blurNSFW),
          secondary: Icon(BLUR_NSFW_ICON),
          subtitle: null,
          value: settings.blurNSFW,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateBlurNSFW(val!),
        ),
        Divider(),
        Text("Subscription list"),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showSubsInMenu),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showSubsInMenu,
          onChanged: (val) => context
              .read<LateralMenuSettingsCubit>()
              .updateShowSubsInMenu(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showSubsIcon),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showSubsIcon,
          onChanged: (val) =>
              context.read<LateralMenuSettingsCubit>().updateShowSubsIcon(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showSubsFavOnly),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showSubsFavOnly,
          onChanged: (val) => context
              .read<LateralMenuSettingsCubit>()
              .updateShowSubsFavOnly(val!),
        ),
        Divider(),
        Text("Account switcher"),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showAccountAvatar),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showAccountAvatar,
          onChanged: (val) => context
              .read<LateralMenuSettingsCubit>()
              .updateShowAccountAvatar(val!),
        ),
        CheckboxListTile(
          title: Text(locales.lateralMenu_showAccountUsername),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showAccountUsername,
          onChanged: (val) => context
              .read<LateralMenuSettingsCubit>()
              .updateShowAccountUsername(val!),
        ),
      ],
    ));
  }
}
