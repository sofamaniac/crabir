import 'package:auto_route/auto_route.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';

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
    @Setting() @Default(Resolution.source) Resolution videoQuality,
    @Setting() @Default(Resolution.source) Resolution minimumQuality,
    @Setting() @Default(Resolution.source) Resolution maximumQuality,
    @Category(name: "Images")
    @Setting(widget: ImageLoadingSelect)
    @Default(ImageLoading.always)
    ImageLoading loadImages,
    @Setting() @Default(Resolution.source) Resolution preferredQuality,
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
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: ImageLoading.values.map((mode) {
        return RadioListTile<ImageLoading>(
          value: mode,
          groupValue: value,
          onChanged: (newValue) {
            if (newValue != null) onChanged(newValue);
          },
          title: Text(_label(mode)),
          secondary: Icon(_icon(mode)),
        );
      }).toList(),
    );
  }
}

class ResoltuionSelect extends StatelessWidget {
  final Widget title;
  final Widget? subtitle;
  final Resolution value;
  final void Function(Resolution) onChanged;
  const ResoltuionSelect({
    super.key,
    required this.title,
    this.subtitle,
    required this.onChanged,
    required this.value,
  });
  @override
  Widget build(BuildContext context) {
    return Text("TODO");
  }
}

enum ImageLoading {
  onWifiOnly,
  always,
  disable,
}
