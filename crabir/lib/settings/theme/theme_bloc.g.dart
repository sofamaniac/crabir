// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'theme_bloc.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_ThemeState _$ThemeStateFromJson(Map<String, dynamic> json) => _ThemeState(
      dark: CrabirTheme.fromJson(json['dark'] as Map<String, dynamic>),
      light: CrabirTheme.fromJson(json['light'] as Map<String, dynamic>),
      mode: $enumDecode(_$ThemeModeEnumMap, json['mode']),
    );

Map<String, dynamic> _$ThemeStateToJson(_ThemeState instance) =>
    <String, dynamic>{
      'dark': instance.dark,
      'light': instance.light,
      'mode': _$ThemeModeEnumMap[instance.mode]!,
    };

const _$ThemeModeEnumMap = {
  ThemeMode.system: 'system',
  ThemeMode.light: 'light',
  ThemeMode.dark: 'dark',
};
