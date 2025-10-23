import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/settings/settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:go_router/go_router.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';

part 'data_settings.settings_page.dart';
part 'data_settings.freezed.dart';
part 'data_settings.g.dart';
part 'data_settings.go_route_ext.dart';

@freezed
@SettingsPage(prefix: "data_", useFieldName: true)
abstract class DataSettings with _$DataSettings {
  DataSettings._();
  factory DataSettings({
    @Category(name: "Videos")
    @Setting(widget: ImageLoadingSelect)
    @Default(ImageLoading.always)
    ImageLoading autoplay,
    @Setting(widget: ResolutionSelect)
    @Default(Resolution.source)
    Resolution videoQualityWifi,
    @Setting(widget: ResolutionSelect)
    @Default(Resolution.source)
    Resolution videoQualityCellular,
    @Category(name: "Images")
    @Setting(widget: ImageLoadingSelect)
    @Default(ImageLoading.always)
    ImageLoading loadImages,
    @Setting(widget: ResolutionSelect)
    @Default(Resolution.source)
    Resolution imageQualityWifi,
    @Setting(widget: ResolutionSelect)
    @Default(Resolution.source)
    Resolution imageQualityCellular,
  }) = _DataSettings;
  factory DataSettings.fromJson(Map<String, dynamic> json) =>
      _$DataSettingsFromJson(json);

  factory DataSettings.of(BuildContext context) =>
      context.watch<DataSettingsCubit>().state;
}

class ImageLoadingSelect extends SettingButton<ImageLoading> {
  const ImageLoadingSelect({
    super.key,
    required super.title,
    super.subtitle,
    required super.onChanged,
    required super.value,
    super.leading,
  });
  String _label(ImageLoading mode) {
    switch (mode) {
      case ImageLoading.onWifiOnly:
        return "Only on Wi-Fi";
      case ImageLoading.always:
        return "Always";
      case ImageLoading.disable:
        return "Disabled";
    }
  }

  IconData _icon(ImageLoading mode) {
    switch (mode) {
      case ImageLoading.onWifiOnly:
        return Icons.wifi;
      case ImageLoading.always:
        return Icons.cloud_download;
      case ImageLoading.disable:
        return Icons.block;
    }
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: leading,
      title: title,
      subtitle: subtitle,
      trailing: DropdownMenu<ImageLoading>(
        dropdownMenuEntries: ImageLoading.values
            .map(
              (mode) => DropdownMenuEntry(
                value: mode,
                leadingIcon: Icon(_icon(mode)),
                label: _label(mode),
              ),
            )
            .toList(),
        onSelected: onChanged != null ? (mode) => onChanged!(mode!) : null,
        initialSelection: value,
      ),
    );
  }
}

class ResolutionSelect extends SettingButton<Resolution> {
  const ResolutionSelect({
    super.key,
    required super.title,
    required super.onChanged,
    required super.value,
    super.subtitle,
    super.leading,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: title,
      leading: leading,
      subtitle: subtitle,
      trailing: DropdownMenu<Resolution>(
        dropdownMenuEntries: Resolution.values
            .map(
              (res) => DropdownMenuEntry(
                value: res,
                label: res.label(context),
              ),
            )
            .toList(),
        onSelected: onChanged != null ? (res) => onChanged!(res!) : null,
        initialSelection: value,
      ),
    );
  }
}

enum ImageLoading {
  onWifiOnly,
  always,
  disable,
}
