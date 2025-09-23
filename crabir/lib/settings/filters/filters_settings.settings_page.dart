// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// SettingsPageGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
part of "filters_settings.dart";

// HydratedCubit for FiltersSettings
class FiltersSettingsCubit extends HydratedCubit<FiltersSettings> {
  final Logger log = Logger("FiltersSettingsCubit");
  FiltersSettingsCubit() : super(FiltersSettings());

  @override
  FiltersSettings? fromJson(Map<String, dynamic> json) {
    try {
      log.info("Successfully restored FiltersSettingsCubit");
      return FiltersSettings.fromJson(json);
    } catch (err) {
      log.severe("Failed to resotre FiltersSettingsCubit: $err");
      return FiltersSettings();
    }
  }

  @override
  Map<String, dynamic>? toJson(FiltersSettings state) => state.toJson();
  void updateManageHiddenCommunities(() value) =>
      emit(state.copyWith(manageHiddenCommunities: value));

  void updateManageHiddenDomains(() value) =>
      emit(state.copyWith(manageHiddenDomains: value));

  void updateManageHiddenUsers(() value) =>
      emit(state.copyWith(manageHiddenUsers: value));

  void updateManageHiddenFlairs(() value) =>
      emit(state.copyWith(manageHiddenFlairs: value));

  void updateShowNSFW(bool value) => emit(state.copyWith(showNSFW: value));

  void updateShowImageInNSFW(bool value) =>
      emit(state.copyWith(showImageInNSFW: value));

  void updateBlurNSFW(bool value) => emit(state.copyWith(blurNSFW: value));
}

// SettingsPage for FiltersSettings
@RoutePage()
class FiltersSettingsView extends StatelessWidget {
  const FiltersSettingsView({super.key});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final settings = context.watch<FiltersSettingsCubit>().state;
    return Scaffold(
        body: ListView(
      children: [
        Divider(),
        Text("Muting options"),
        SubredditsFilterButton(
          title: Text(locales.filters_manageHiddenCommunities),
          leading: null,
          subtitle: Text(locales.filters_manageHiddenCommunitiesDescription),
          value: settings.manageHiddenCommunities,
          onChanged: (val) => context
              .read<FiltersSettingsCubit>()
              .updateManageHiddenCommunities(val),
        ),
        DomainsFilterButton(
          title: Text(locales.filters_manageHiddenDomains),
          leading: null,
          subtitle: Text(locales.filters_manageHiddenDomainsDescription),
          value: settings.manageHiddenDomains,
          onChanged: (val) => context
              .read<FiltersSettingsCubit>()
              .updateManageHiddenDomains(val),
        ),
        UserFilterButton(
          title: Text(locales.filters_manageHiddenUsers),
          leading: null,
          subtitle: Text(locales.filters_manageHiddenUsersDescription),
          value: settings.manageHiddenUsers,
          onChanged: (val) =>
              context.read<FiltersSettingsCubit>().updateManageHiddenUsers(val),
        ),
        FlairFilterButton(
          title: Text(locales.filters_manageHiddenFlairs),
          leading: null,
          subtitle: Text(locales.filters_manageHiddenFlairsDescription),
          value: settings.manageHiddenFlairs,
          onChanged: (val) => context
              .read<FiltersSettingsCubit>()
              .updateManageHiddenFlairs(val),
        ),
        Divider(),
        Text("More options"),
        CheckboxListTile(
          title: Text(locales.filters_showNSFW),
          secondary: null,
          subtitle: Text(locales.filters_showNSFWDescription),
          value: settings.showNSFW,
          onChanged: (val) =>
              context.read<FiltersSettingsCubit>().updateShowNSFW(val!),
        ),
        CheckboxListTile(
          title: Text(locales.filters_showImageInNSFW),
          secondary: Icon(Icons.image),
          subtitle: null,
          value: settings.showImageInNSFW,
          onChanged: (val) =>
              context.read<FiltersSettingsCubit>().updateShowImageInNSFW(val!),
        ),
        CheckboxListTile(
          title: Text(locales.filters_blurNSFW),
          secondary: Icon(Icons.blur_on),
          subtitle: null,
          value: settings.blurNSFW,
          onChanged: (val) =>
              context.read<FiltersSettingsCubit>().updateBlurNSFW(val!),
        ),
      ],
    ));
  }
}
