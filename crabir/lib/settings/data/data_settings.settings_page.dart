// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// SettingsPageGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
part of "data_settings.dart";

// HydratedCubit for DataSettings
class DataSettingsCubit extends HydratedCubit<DataSettings> {
  final Logger log = Logger("DataSettingsCubit");
  DataSettingsCubit() : super(DataSettings());

  @override
  DataSettings? fromJson(Map<String, dynamic> json) {
    try {
      log.info("Successfully restored DataSettingsCubit");
      return DataSettings.fromJson(json);
    } catch (err) {
      log.severe("Failed to resotre DataSettingsCubit: $err");
      return DataSettings();
    }
  }

  @override
  Map<String, dynamic>? toJson(DataSettings state) => state.toJson();
  void updateMobileDataSaver(bool value) =>
      emit(state.copyWith(mobileDataSaver: value));

  void updateWifiDataSaver(bool value) =>
      emit(state.copyWith(wifiDataSaver: value));

  void updateAutoplay(ImageLoading value) =>
      emit(state.copyWith(autoplay: value));

  void updateVideoQuality(Resolution value) =>
      emit(state.copyWith(videoQuality: value));

  void updateLoadImages(ImageLoading value) =>
      emit(state.copyWith(loadImages: value));

  void updatePreferredQuality(Resolution value) =>
      emit(state.copyWith(preferredQuality: value));
}

// SettingsPage for DataSettings
@RoutePage()
class DataSettingsView extends StatelessWidget {
  const DataSettingsView({super.key});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final settings = context.watch<DataSettingsCubit>().state;
    return Scaffold(
        body: ListView(
      children: [
        Divider(),
        Text("Data Saver"),
        CheckboxListTile(
          title: Text(locales.data_mobileDataSaver),
          secondary: null,
          subtitle: Text(locales.data_mobileDataSaverDescription),
          value: settings.mobileDataSaver,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateMobileDataSaver(val!),
        ),
        CheckboxListTile(
          title: Text(locales.data_wifiDataSaver),
          secondary: null,
          subtitle: Text(locales.data_wifiDataSaverDescription),
          value: settings.wifiDataSaver,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateWifiDataSaver(val!),
        ),
        Divider(),
        Text("Videos"),
        ImageLoadingSelect(
          title: Text(locales.data_autoplay),
          leading: null,
          subtitle: null,
          value: settings.autoplay,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateAutoplay(val),
        ),
        ResolutionSelect(
          title: Text(locales.data_videoQuality),
          leading: null,
          subtitle: null,
          value: settings.videoQuality,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateVideoQuality(val),
        ),
        Divider(),
        Text("Images"),
        ImageLoadingSelect(
          title: Text(locales.data_loadImages),
          leading: null,
          subtitle: null,
          value: settings.loadImages,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateLoadImages(val),
        ),
        ResolutionSelect(
          title: Text(locales.data_preferredQuality),
          leading: null,
          subtitle: null,
          value: settings.preferredQuality,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updatePreferredQuality(val),
        ),
      ],
    ));
  }
}
