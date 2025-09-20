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
        CheckboxListTile(
          title: Text(locales.filters_blurNSFW),
          subtitle: null,
          value: settings.blurNSFW,
          onChanged: (val) =>
              context.read<FiltersSettingsCubit>().updateBlurNSFW(val!),
        ),
      ],
    ));
  }
}
