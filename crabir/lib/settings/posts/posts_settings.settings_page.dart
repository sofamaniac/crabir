// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// SettingsPageGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
part of "posts_settings.dart";

// HydratedCubit for PostsSettings
class PostsSettingsCubit extends HydratedCubit<PostsSettings> {
  PostsSettingsCubit() : super(PostsSettings());

  @override
  PostsSettings? fromJson(Map<String, dynamic> json) {
    try {
      return PostsSettings.fromJson(json);
    } catch (_) {
      return PostsSettings();
    }
  }

  @override
  Map<String, dynamic>? toJson(PostsSettings state) => state.toJson();
  void updateDefaultHomeSort(FeedSort value) =>
      emit(state.copyWith(defaultHomeSort: value));

  void updateDefaultSort(FeedSort value) =>
      emit(state.copyWith(defaultSort: value));

  void updateRememberSortByCommunity(bool value) =>
      emit(state.copyWith(rememberSortByCommunity: value));

  void updateRememberedSorts(RememberedSort value) =>
      emit(state.copyWith(rememberedSorts: value));

  void updateShowAwards(bool value) => emit(state.copyWith(showAwards: value));

  void updateClickableAwards(bool value) =>
      emit(state.copyWith(clickableAwards: value));

  void updateShowPostFlair(bool value) =>
      emit(state.copyWith(showPostFlair: value));

  void updateShowFlairColors(bool value) =>
      emit(state.copyWith(showFlairColors: value));

  void updateShowEmojis(bool value) => emit(state.copyWith(showEmojis: value));

  void updateTapFlairToSearch(bool value) =>
      emit(state.copyWith(tapFlairToSearch: value));

  void updateShowAuthor(bool value) => emit(state.copyWith(showAuthor: value));

  void updateClickableCommunity(bool value) =>
      emit(state.copyWith(clickableCommunity: value));

  void updateClickableUser(bool value) =>
      emit(state.copyWith(clickableUser: value));

  void updateShowFloatingButton(bool value) =>
      emit(state.copyWith(showFloatingButton: value));

  void updateShowHideButton(bool value) =>
      emit(state.copyWith(showHideButton: value));

  void updateShowMarkAsReadButton(bool value) =>
      emit(state.copyWith(showMarkAsReadButton: value));

  void updateShowShareButton(bool value) =>
      emit(state.copyWith(showShareButton: value));

  void updateShowCommentsButton(bool value) =>
      emit(state.copyWith(showCommentsButton: value));

  void updateShowOpenInAppButton(bool value) =>
      emit(state.copyWith(showOpenInAppButton: value));
}

// SettingsPage for PostsSettings
@RoutePage()
class PostsSettingsView extends StatelessWidget {
  const PostsSettingsView({super.key});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final settings = context.watch<PostsSettingsCubit>().state;
    return Scaffold(
        body: ListView(
      children: [
        _SortSelection(
          title: Text(locales.posts_defaultHomeSort),
          subtitle: null,
          value: settings.defaultHomeSort,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateDefaultHomeSort(val),
        ),
        _SortSelection(
          title: Text(locales.posts_defaultSort),
          subtitle: null,
          value: settings.defaultSort,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateDefaultSort(val),
        ),
        CheckboxListTile(
          title: Text(locales.posts_rememberSortByCommunity),
          subtitle: Text(locales.posts_rememberSortByCommunityDescription),
          value: settings.rememberSortByCommunity,
          onChanged: (val) => context
              .read<PostsSettingsCubit>()
              .updateRememberSortByCommunity(val!),
        ),
        ManageSortButton(
          title: Text(locales.posts_rememberedSorts),
          subtitle: null,
          value: settings.rememberedSorts,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateRememberedSorts(val),
        ),
        Divider(),
        Text("Awards"),
        CheckboxListTile(
          title: Text(locales.posts_showAwards),
          subtitle: null,
          value: settings.showAwards,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateShowAwards(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_clickableAwards),
          subtitle: null,
          value: settings.clickableAwards,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateClickableAwards(val!),
        ),
        Divider(),
        Text("Flairs"),
        CheckboxListTile(
          title: Text(locales.posts_showPostFlair),
          subtitle: null,
          value: settings.showPostFlair,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateShowPostFlair(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_showFlairColors),
          subtitle: null,
          value: settings.showFlairColors,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateShowFlairColors(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_showEmojis),
          subtitle: null,
          value: settings.showEmojis,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateShowEmojis(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_tapFlairToSearch),
          subtitle: null,
          value: settings.tapFlairToSearch,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateTapFlairToSearch(val!),
        ),
        Divider(),
        Text("Post info"),
        CheckboxListTile(
          title: Text(locales.posts_showAuthor),
          subtitle: null,
          value: settings.showAuthor,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateShowAuthor(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_clickableCommunity),
          subtitle: null,
          value: settings.clickableCommunity,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateClickableCommunity(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_clickableUser),
          subtitle: null,
          value: settings.clickableUser,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateClickableUser(val!),
        ),
        Divider(),
        Text("Visible buttons"),
        CheckboxListTile(
          title: Text(locales.posts_showFloatingButton),
          subtitle: null,
          value: settings.showFloatingButton,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateShowFloatingButton(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_showHideButton),
          subtitle: null,
          value: settings.showHideButton,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateShowHideButton(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_showMarkAsReadButton),
          subtitle: null,
          value: settings.showMarkAsReadButton,
          onChanged: (val) => context
              .read<PostsSettingsCubit>()
              .updateShowMarkAsReadButton(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_showShareButton),
          subtitle: null,
          value: settings.showShareButton,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateShowShareButton(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_showCommentsButton),
          subtitle: null,
          value: settings.showCommentsButton,
          onChanged: (val) =>
              context.read<PostsSettingsCubit>().updateShowCommentsButton(val!),
        ),
        CheckboxListTile(
          title: Text(locales.posts_showOpenInAppButton),
          subtitle: null,
          value: settings.showOpenInAppButton,
          onChanged: (val) => context
              .read<PostsSettingsCubit>()
              .updateShowOpenInAppButton(val!),
        ),
      ],
    ));
  }
}
