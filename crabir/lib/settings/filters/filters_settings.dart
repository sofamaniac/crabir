import 'package:auto_route/auto_route.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';

part 'filters_settings.settings_page.dart';
part 'filters_settings.freezed.dart';
part 'filters_settings.g.dart';

@freezed
@SettingsPage(prefix: "filters_", useFieldName: true)
abstract class FiltersSettings with _$FiltersSettings {
  FiltersSettings._();
  factory FiltersSettings({
    @Setting(icon: "Icons.blur_on") @Default(false) bool blurNSFW,
  }) = _FiltersSettings;

  factory FiltersSettings.fromJson(Map<String, dynamic> json) =>
      _$FiltersSettingsFromJson(json);
}
