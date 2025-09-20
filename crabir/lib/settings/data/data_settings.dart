import 'package:auto_route/auto_route.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';

part 'data_settings.settings_page.dart';
part 'data_settings.freezed.dart';
part 'data_settings.g.dart';

@freezed
@SettingsPage(prefix: "data_", useFieldName: true)
abstract class DataSettings with _$DataSettings {
  DataSettings._();
  factory DataSettings({
    @Category(name: "Data Saver")
    @Setting(hasDescription: true)
    @Default(false)
    bool mobileDataSaver,
    @Setting(hasDescription: true) @Default(false) bool wifiDataSaver,
    @Category(name: "Videos")
    @Setting(widget: ImageLoadingSelect)
    @Default(ImageLoading.always)
    ImageLoading autoplay,
    @Setting(widget: ResolutionSelect)
    @Default(Resolution.source)
    Resolution videoQuality,
    @Category(name: "Images")
    @Setting(widget: ImageLoadingSelect)
    @Default(ImageLoading.always)
    ImageLoading loadImages,
    @Setting(widget: ResolutionSelect)
    @Default(Resolution.source)
    Resolution preferredQuality,
  }) = _DataSettings;
  factory DataSettings.fromJson(Map<String, dynamic> json) =>
      _$DataSettingsFromJson(json);
}

class ImageLoadingSelect extends StatelessWidget {
  final Widget title;
  final Widget? subtitle;
  final ImageLoading value;
  final void Function(ImageLoading) onChanged;
  const ImageLoadingSelect({
    super.key,
    required this.title,
    this.subtitle,
    required this.onChanged,
    required this.value,
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
      title: title,
      subtitle: subtitle,
      trailing: DropdownMenu(
        dropdownMenuEntries: ImageLoading.values
            .map(
              (mode) => DropdownMenuEntry(
                value: mode,
                leadingIcon: Icon(_icon(mode)),
                label: _label(mode),
              ),
            )
            .toList(),
        onSelected: (mode) => onChanged(mode!),
        initialSelection: value,
      ),
    );
  }
}

class ResolutionSelect extends StatelessWidget {
  final Widget title;
  final Widget? subtitle;
  final Resolution value;
  final void Function(Resolution) onChanged;
  const ResolutionSelect({
    super.key,
    required this.title,
    required this.onChanged,
    required this.value,
    this.subtitle,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: title,
      subtitle: subtitle,
      trailing: DropdownMenu(
        dropdownMenuEntries: Resolution.values
            .map(
              (res) => DropdownMenuEntry(
                value: res,
                label: res.label(context),
              ),
            )
            .toList(),
        onSelected: (res) => onChanged(res!),
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
