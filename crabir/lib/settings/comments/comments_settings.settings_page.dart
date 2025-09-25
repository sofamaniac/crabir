// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// SettingsPageGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
part of "comments_settings.dart";

// HydratedCubit for CommentsSettings
class CommentsSettingsCubit extends HydratedCubit<CommentsSettings> {
  final Logger log = Logger("CommentsSettingsCubit");
  CommentsSettingsCubit() : super(CommentsSettings());

  @override
  CommentsSettings? fromJson(Map<String, dynamic> json) {
    try {
      log.info("Successfully restored CommentsSettingsCubit");
      return CommentsSettings.fromJson(json);
    } catch (err) {
      log.severe("Failed to resotre CommentsSettingsCubit: $err");
      return CommentsSettings();
    }
  }

  @override
  Map<String, dynamic>? toJson(CommentsSettings state) => state.toJson();
  void updateDefaultSort(CommentSort value) =>
      emit(state.copyWith(defaultSort: value));

  void updateUseSuggestedSort(bool value) =>
      emit(state.copyWith(useSuggestedSort: value));

  void updateShowNavigationBar(bool value) =>
      emit(state.copyWith(showNavigationBar: value));

  void updateShowUserAvatar(bool value) =>
      emit(state.copyWith(showUserAvatar: value));

  void updateShowCommentsImage(bool value) =>
      emit(state.copyWith(showCommentsImage: value));

  void updatePostMediaPreviewSize(MediaPreviewSize value) =>
      emit(state.copyWith(postMediaPreviewSize: value));

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
    final locales = AppLocalizations.of(context);
    final settings = context.watch<CommentsSettingsCubit>().state;
    return Scaffold(
        body: ListView(
      children: [
        _CommentsSortSelection(
          title: Text(locales.comments_defaultSort),
          leading: Icon(null),
          subtitle: null,
          value: settings.defaultSort,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateDefaultSort(val),
        ),
        CheckboxListTile(
          title: Text(locales.comments_useSuggestedSort),
          secondary: Icon(null),
          subtitle: null,
          value: settings.useSuggestedSort,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateUseSuggestedSort(val!),
        ),
        Divider(),
        Text("Appearance"),
        CheckboxListTile(
          title: Text(locales.comments_showNavigationBar),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showNavigationBar,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateShowNavigationBar(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_showUserAvatar),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showUserAvatar,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowUserAvatar(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_showCommentsImage),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showCommentsImage,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateShowCommentsImage(val!),
        ),
        MediaPreviewSizeSelect(
          title: Text(locales.comments_postMediaPreviewSize),
          leading: Icon(null),
          subtitle: null,
          value: settings.postMediaPreviewSize,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updatePostMediaPreviewSize(val),
        ),
        CheckboxListTile(
          title: Text(locales.comments_buttonsAlwaysVisible),
          secondary: Icon(null),
          subtitle: null,
          value: settings.buttonsAlwaysVisible,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateButtonsAlwaysVisible(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_hideButtonAfterAction),
          secondary: Icon(null),
          subtitle: null,
          value: settings.hideButtonAfterAction,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateHideButtonAfterAction(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_collapseAutoMod),
          secondary: Icon(null),
          subtitle: null,
          value: settings.collapseAutoMod,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateCollapseAutoMod(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_collapseDisruptiveComment),
          secondary: Icon(null),
          subtitle: null,
          value: settings.collapseDisruptiveComment,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateCollapseDisruptiveComment(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_showPostUpvotePercentage),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showPostUpvotePercentage,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateShowPostUpvotePercentage(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_highlightMyUsername),
          secondary: Icon(null),
          subtitle: null,
          value: settings.highlightMyUsername,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateHighlightMyUsername(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_showFloatingButton),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showFloatingButton,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateShowFloatingButton(val!),
        ),
        Divider(),
        Text("Awards"),
        CheckboxListTile(
          title: Text(locales.comments_showAwards),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showAwards,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowAwards(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_clickableAwards),
          secondary: Icon(null),
          subtitle: null,
          value: settings.clickableAwards,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateClickableAwards(val!),
        ),
        Divider(),
        Text("Flairs"),
        CheckboxListTile(
          title: Text(locales.comments_showUserFlair),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showUserFlair,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowUserFlair(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_showFlairColors),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showFlairColors,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowFlairColors(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_showFlairEmojis),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showFlairEmojis,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowFlairEmojis(val!),
        ),
        Divider(),
        Text("Behavior"),
        CheckboxListTile(
          title: Text(locales.comments_clickToCollapse),
          secondary: Icon(null),
          subtitle: null,
          value: settings.clickToCollapse,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateClickToCollapse(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_hideTextCollapsed),
          secondary: Icon(null),
          subtitle: null,
          value: settings.hideTextCollapsed,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateHideTextCollapsed(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_loadCollapsed),
          secondary: Icon(null),
          subtitle: null,
          value: settings.loadCollapsed,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateLoadCollapsed(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_animateCollapse),
          secondary: Icon(null),
          subtitle: null,
          value: settings.animateCollapse,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateAnimateCollapse(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_clickableUsername),
          secondary: Icon(null),
          subtitle: null,
          value: settings.clickableUsername,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateClickableUsername(val!),
        ),
        Divider(),
        Text("Navigation"),
        CheckboxListTile(
          title: Text(locales.comments_highlightNewComments),
          secondary: Icon(null),
          subtitle: null,
          value: settings.highlightNewComments,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateHighlightNewComments(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_volumeRockerNavigation),
          secondary: Icon(null),
          subtitle: null,
          value: settings.volumeRockerNavigation,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateVolumeRockerNavigation(val!),
        ),
        CheckboxListTile(
          title: Text(locales.comments_animateNavigation),
          secondary: Icon(null),
          subtitle: null,
          value: settings.animateNavigation,
          onChanged: (val) => context
              .read<CommentsSettingsCubit>()
              .updateAnimateNavigation(val!),
        ),
        Divider(),
        Text("Visible buttons"),
        CheckboxListTile(
          title: Text(locales.comments_showSaveButton),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showSaveButton,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateShowSaveButton(val!),
        ),
        Divider(),
        Text("Gestures"),
        CheckboxListTile(
          title: Text(locales.comments_swipeToClose),
          secondary: Icon(null),
          subtitle: null,
          value: settings.swipeToClose,
          onChanged: (val) =>
              context.read<CommentsSettingsCubit>().updateSwipeToClose(val!),
        ),
      ],
    ));
  }
}
