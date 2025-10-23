// GENERATED CODE - DO NOT MODIFY BY HAND
// dart format width=80

// **************************************************************************
// SettingsPageGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
part of "layout_settings.dart";

// HydratedCubit for LayoutSettings
class LayoutSettingsCubit extends HydratedCubit<LayoutSettings> {
  final Logger log = Logger("LayoutSettingsCubit");
  LayoutSettingsCubit() : super(LayoutSettings());

  @override
  LayoutSettings? fromJson(Map<String, dynamic> json) {
    try {
      log.info("Successfully restored LayoutSettingsCubit");
      return LayoutSettings.fromJson(json);
    } catch (err) {
      log.severe("Failed to resotre LayoutSettingsCubit: $err");
      return LayoutSettings();
    }
  }

  @override
  Map<String, dynamic>? toJson(LayoutSettings state) => state.toJson();
  void updateDefaultView(ViewKind value) =>
      emit(state.copyWith(defaultView: value));

  void updateRememberByCommunity(bool value) =>
      emit(state.copyWith(rememberByCommunity: value));

  void updateRememberedView(RememberedView value) =>
      emit(state.copyWith(rememberedView: value));

  void updateFont(() value) => emit(state.copyWith(font: value));

  void updateShowThumbnail(bool value) =>
      emit(state.copyWith(showThumbnail: value));

  void updateThumbnailOnLeft(bool value) =>
      emit(state.copyWith(thumbnailOnLeft: value));

  void updatePrefixCommunities(bool value) =>
      emit(state.copyWith(prefixCommunities: value));
}

@CrabirRoute()
// SettingsPage for LayoutSettings
class LayoutSettingsView extends StatelessWidget {
  const LayoutSettingsView({super.key});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final settings = context.watch<LayoutSettingsCubit>().state;
    return Scaffold(
        body: ListView(
      children: [
        _ViewKindSelection(
          title: Text(locales.layout_defaultView),
          leading: Icon(null),
          subtitle: null,
          value: settings.defaultView,
          onChanged: (val) =>
              context.read<LayoutSettingsCubit>().updateDefaultView(val),
        ),
        CheckboxListTile(
          title: Text(locales.layout_rememberByCommunity),
          secondary: Icon(null),
          subtitle: null,
          value: settings.rememberByCommunity,
          onChanged: (val) => context
              .read<LayoutSettingsCubit>()
              .updateRememberByCommunity(val!),
        ),
        _ManageViewButton(
          title: Text(locales.layout_rememberedView),
          leading: Icon(null),
          subtitle: null,
          value: settings.rememberedView,
          onChanged: (val) =>
              context.read<LayoutSettingsCubit>().updateRememberedView(val),
        ),
        Divider(),
        ListTile(
          title: Text("TODO: font"),
          leading: null,
          subtitle: null,
        ),
        CheckboxListTile(
          title: Text(locales.layout_showThumbnail),
          secondary: Icon(null),
          subtitle: null,
          value: settings.showThumbnail,
          onChanged: (val) =>
              context.read<LayoutSettingsCubit>().updateShowThumbnail(val!),
        ),
        CheckboxListTile(
          title: Text(locales.layout_thumbnailOnLeft),
          secondary: Icon(null),
          subtitle: null,
          value: settings.thumbnailOnLeft,
          onChanged: settings.showThumbnail
              ? (val) => context
                  .read<LayoutSettingsCubit>()
                  .updateThumbnailOnLeft(val!)
              : null,
        ),
        CheckboxListTile(
          title: Text(locales.layout_prefixCommunities),
          secondary: Icon(null),
          subtitle: null,
          value: settings.prefixCommunities,
          onChanged: (val) =>
              context.read<LayoutSettingsCubit>().updatePrefixCommunities(val!),
        ),
      ],
    ));
  }
}
