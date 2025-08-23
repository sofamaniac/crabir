// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// SettingsPageGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
part of "comments_settings.dart";

// HydratedCubit for CommentsSettings
class CommentsSettingsCubit extends HydratedCubit<CommentsSettings> {
  CommentsSettingsCubit() : super(CommentsSettings());

  @override
  CommentsSettings? fromJson(Map<String, dynamic> json) {
    try {
      return CommentsSettings.fromJson(json);
    } catch (_) {
      return CommentsSettings();
    }
  }

  @override
  Map<String, dynamic>? toJson(CommentsSettings state) => state.toJson();
  void updateSort(CommentSort value) => emit(state.copyWith(sort: value));

  void updateUseSuggestedSort(bool value) =>
      emit(state.copyWith(useSuggestedSort: value));

  void updateShowNavigationBar(bool value) =>
      emit(state.copyWith(showNavigationBar: value));

  void updateShowUserAvatar(bool value) =>
      emit(state.copyWith(showUserAvatar: value));

  void updateButtonsAlwaysVisible(bool value) =>
      emit(state.copyWith(buttonsAlwaysVisible: value));

  void updateHideButtonAfterAction(bool value) =>
      emit(state.copyWith(hideButtonAfterAction: value));

  void updateCollapseAutoMod(bool value) =>
      emit(state.copyWith(collapseAutoMod: value));

  void updateCollapseDisruptiveComment(bool value) =>
      emit(state.copyWith(collapseDisruptiveComment: value));

  void updateShowPostUpvotePercentage(bool value) =>
      emit(state.copyWith(showPostUpvotePercentage: value));

  void updateHighlightMyUsername(bool value) =>
      emit(state.copyWith(highlightMyUsername: value));

  void updateShowFloatingButton(bool value) =>
      emit(state.copyWith(showFloatingButton: value));

  void updateShowAwards(bool value) => emit(state.copyWith(showAwards: value));

  void updateClickableAwards(bool value) =>
      emit(state.copyWith(clickableAwards: value));

  void updateShowUserFlair(bool value) =>
      emit(state.copyWith(showUserFlair: value));

  void updateShowFlairColors(bool value) =>
      emit(state.copyWith(showFlairColors: value));

  void updateShowFlairEmojis(bool value) =>
      emit(state.copyWith(showFlairEmojis: value));

  void updateClickToCollapse(bool value) =>
      emit(state.copyWith(clickToCollapse: value));

  void updateHideTextCollapsed(bool value) =>
      emit(state.copyWith(hideTextCollapsed: value));

  void updateLoadCollapsed(bool value) =>
      emit(state.copyWith(loadCollapsed: value));

  void updateAnimateCollapse(bool value) =>
      emit(state.copyWith(animateCollapse: value));

  void updateClickableUsername(bool value) =>
      emit(state.copyWith(clickableUsername: value));

  void updateHighlightNewComments(bool value) =>
      emit(state.copyWith(highlightNewComments: value));

  void updateVolumeRockerNavigation(bool value) =>
      emit(state.copyWith(volumeRockerNavigation: value));

  void updateAnimateNavigation(bool value) =>
      emit(state.copyWith(animateNavigation: value));

  void updateShowSaveButton(bool value) =>
      emit(state.copyWith(showSaveButton: value));

  void updateSwipeToClose(bool value) =>
      emit(state.copyWith(swipeToClose: value));
}

// SettingsPage for CommentsSettings
@RoutePage()
class CommentsSettingsView extends StatelessWidget {
  const CommentsSettingsView({super.key});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context)!;
    final settings = context.watch<CommentsSettingsCubit>().state;
    return Scaffold(
        body: ListView(
      children: [
        ListTile(title: Text("TODO: sort")),
        SwitchListTile(
          title: Text(locales.comments_useSuggestedSort),
          subtitle: null,
          value: settings.useSuggestedSort,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateUseSuggestedSort(val),
        ),
        Divider(),
        Text("Appearance"),
        SwitchListTile(
          title: Text(locales.comments_showNavigationBar),
          subtitle: null,
          value: settings.showNavigationBar,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateShowNavigationBar(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_showUserAvatar),
          subtitle: null,
          value: settings.showUserAvatar,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowUserAvatar(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_buttonsAlwaysVisible),
          subtitle: null,
          value: settings.buttonsAlwaysVisible,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateButtonsAlwaysVisible(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_hideButtonAfterAction),
          subtitle: null,
          value: settings.hideButtonAfterAction,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateHideButtonAfterAction(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_collapseAutoMod),
          subtitle: null,
          value: settings.collapseAutoMod,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateCollapseAutoMod(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_collapseDisruptiveComment),
          subtitle: null,
          value: settings.collapseDisruptiveComment,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateCollapseDisruptiveComment(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_showPostUpvotePercentage),
          subtitle: null,
          value: settings.showPostUpvotePercentage,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateShowPostUpvotePercentage(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_highlightMyUsername),
          subtitle: null,
          value: settings.highlightMyUsername,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateHighlightMyUsername(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_showFloatingButton),
          subtitle: null,
          value: settings.showFloatingButton,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateShowFloatingButton(val),
        ),
        Divider(),
        Text("Awards"),
        SwitchListTile(
          title: Text(locales.comments_showAwards),
          subtitle: null,
          value: settings.showAwards,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowAwards(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_clickableAwards),
          subtitle: null,
          value: settings.clickableAwards,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateClickableAwards(val),
        ),
        Divider(),
        Text("Flairs"),
        SwitchListTile(
          title: Text(locales.comments_showUserFlair),
          subtitle: null,
          value: settings.showUserFlair,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowUserFlair(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_showFlairColors),
          subtitle: null,
          value: settings.showFlairColors,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowFlairColors(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_showFlairEmojis),
          subtitle: null,
          value: settings.showFlairEmojis,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowFlairEmojis(val),
        ),
        Divider(),
        Text("Behavior"),
        SwitchListTile(
          title: Text(locales.comments_clickToCollapse),
          subtitle: null,
          value: settings.clickToCollapse,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateClickToCollapse(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_hideTextCollapsed),
          subtitle: null,
          value: settings.hideTextCollapsed,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateHideTextCollapsed(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_loadCollapsed),
          subtitle: null,
          value: settings.loadCollapsed,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateLoadCollapsed(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_animateCollapse),
          subtitle: null,
          value: settings.animateCollapse,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateAnimateCollapse(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_clickableUsername),
          subtitle: null,
          value: settings.clickableUsername,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateClickableUsername(val),
        ),
        Divider(),
        Text("Navigation"),
        SwitchListTile(
          title: Text(locales.comments_highlightNewComments),
          subtitle: null,
          value: settings.highlightNewComments,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateHighlightNewComments(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_volumeRockerNavigation),
          subtitle: null,
          value: settings.volumeRockerNavigation,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateVolumeRockerNavigation(val),
        ),
        SwitchListTile(
          title: Text(locales.comments_animateNavigation),
          subtitle: null,
          value: settings.animateNavigation,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateAnimateNavigation(val),
        ),
        Divider(),
        Text("Visible buttons"),
        SwitchListTile(
          title: Text(locales.comments_showSaveButton),
          subtitle: null,
          value: settings.showSaveButton,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowSaveButton(val),
        ),
        Divider(),
        Text("Gestures"),
        SwitchListTile(
          title: Text(locales.comments_swipeToClose),
          subtitle: null,
          value: settings.swipeToClose,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateSwipeToClose(val),
        ),
      ],
    ));
  }
}
