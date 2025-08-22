// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// SettingsPageGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
import "package:crabir/settings/setting.dart";
import "package:flutter/material.dart";
import "package:auto_route/auto_route.dart";
import "package:flutter_gen/gen_l10n/app_localizations.dart";

// SettingsPage for CommentsSettings
@RoutePage()
class CommentsSettingsView extends StatefulWidget {
  final CommentsSettings settings;
  @override
  createState() => _CommentsSettingsViewState();
  const CommentsSettingsView({super.key, required this.settings});
}

class _CommentsSettingsViewState extends State<CommentsSettingsView> {
  late CommentsSettings settings;
  @override
  initState() {
    settings = widget.settings;
  }

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context)!;
    return Scaffold(
        body: ListView(
      children: [
        ListTile(title: Text("TODO: sort")),
        SwitchListTile(
          title: Text(locales.comments_useSuggestedSort),
          subtitle: null,
          value: settings.useSuggestedSort,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(useSuggestedSort: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_showNavigationBar),
          subtitle: null,
          value: settings.showNavigationBar,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(showNavigationBar: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_showUserAvatar),
          subtitle: null,
          value: settings.showUserAvatar,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(showUserAvatar: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_buttonsAlwaysVisible),
          subtitle: null,
          value: settings.buttonsAlwaysVisible,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(buttonsAlwaysVisible: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_hideButtonAfterAction),
          subtitle: null,
          value: settings.hideButtonAfterAction,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(hideButtonAfterAction: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_collapseAutoMod),
          subtitle: null,
          value: settings.collapseAutoMod,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(collapseAutoMod: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_collapseDisruptiveComment),
          subtitle: null,
          value: settings.collapseDisruptiveComment,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(collapseDisruptiveComment: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_showPostUpvotePercentage),
          subtitle: null,
          value: settings.showPostUpvotePercentage,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(showPostUpvotePercentage: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_highlightMyUsername),
          subtitle: null,
          value: settings.highlightMyUsername,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(highlightMyUsername: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_showFloatingButton),
          subtitle: null,
          value: settings.showFloatingButton,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(showFloatingButton: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_showAwards),
          subtitle: null,
          value: settings.showAwards,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(showAwards: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_clickableAwards),
          subtitle: null,
          value: settings.clickableAwards,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(clickableAwards: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_showUserFlair),
          subtitle: null,
          value: settings.showUserFlair,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(showUserFlair: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_showFlairColors),
          subtitle: null,
          value: settings.showFlairColors,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(showFlairColors: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_showFlairEmojis),
          subtitle: null,
          value: settings.showFlairEmojis,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(showFlairEmojis: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_clickToCollapse),
          subtitle: null,
          value: settings.clickToCollapse,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(clickToCollapse: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_hideTextCollapsed),
          subtitle: null,
          value: settings.hideTextCollapsed,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(hideTextCollapsed: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_loadCollapsed),
          subtitle: null,
          value: settings.loadCollapsed,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(loadCollapsed: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_animateCollapse),
          subtitle: null,
          value: settings.animateCollapse,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(animateCollapse: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_clickableUsername),
          subtitle: null,
          value: settings.clickableUsername,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(clickableUsername: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_highlightNewComments),
          subtitle: null,
          value: settings.highlightNewComments,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(highlightNewComments: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_volumeRockerNavigation),
          subtitle: null,
          value: settings.volumeRockerNavigation,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(volumeRockerNavigation: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_animateNavigation),
          subtitle: null,
          value: settings.animateNavigation,
          onChanged: (v) => setState(
              () => settings = settings.copyWith(animateNavigation: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_showSaveButton),
          subtitle: null,
          value: settings.showSaveButton,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(showSaveButton: v)),
        ),
        SwitchListTile(
          title: Text(locales.comments_swipeToClose),
          subtitle: null,
          value: settings.swipeToClose,
          onChanged: (v) =>
              setState(() => settings = settings.copyWith(swipeToClose: v)),
        ),
      ],
    ));
  }
}
