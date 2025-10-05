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
  void updateAutoplay(ImageLoading value) =>
      emit(state.copyWith(autoplay: value));

  void updateVideoQualityWifi(Resolution value) =>
      emit(state.copyWith(videoQualityWifi: value));

  void updateVideoQualityCellular(Resolution value) =>
      emit(state.copyWith(videoQualityCellular: value));

  void updateLoadImages(ImageLoading value) =>
      emit(state.copyWith(loadImages: value));

  void updateImageQualityWifi(Resolution value) =>
      emit(state.copyWith(imageQualityWifi: value));

  void updateImageQualityCellular(Resolution value) =>
      emit(state.copyWith(imageQualityCellular: value));
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
        ListTile(
            leading: Icon(null),
            title: Text("Videos",
                style: Theme.of(context)
                    .textTheme
                    .labelMedium
                    ?.copyWith(color: CrabirTheme.of(context).highlight))),
        ImageLoadingSelect(
          title: Text(locales.data_autoplay),
          leading: Icon(null),
          subtitle: null,
          value: settings.autoplay,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateAutoplay(val),
        ),
        ResolutionSelect(
          title: Text(locales.data_videoQualityWifi),
          leading: Icon(null),
          subtitle: null,
          value: settings.videoQualityWifi,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateVideoQualityWifi(val),
        ),
        ResolutionSelect(
          title: Text(locales.data_videoQualityCellular),
          leading: Icon(null),
          subtitle: null,
          value: settings.videoQualityCellular,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateVideoQualityCellular(val),
        ),
        Divider(),
        ListTile(
            leading: Icon(null),
            title: Text("Images",
                style: Theme.of(context)
                    .textTheme
                    .labelMedium
                    ?.copyWith(color: CrabirTheme.of(context).highlight))),
        ImageLoadingSelect(
          title: Text(locales.data_loadImages),
          leading: Icon(null),
          subtitle: null,
          value: settings.loadImages,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateLoadImages(val),
        ),
        ResolutionSelect(
          title: Text(locales.data_imageQualityWifi),
          leading: Icon(null),
          subtitle: null,
          value: settings.imageQualityWifi,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateImageQualityWifi(val),
        ),
        ResolutionSelect(
          title: Text(locales.data_imageQualityCellular),
          leading: Icon(null),
          subtitle: null,
          value: settings.imageQualityCellular,
          onChanged: (val) =>
              context.read<DataSettingsCubit>().updateImageQualityCellular(val),
        ),
      ],
    ));
  }
}
