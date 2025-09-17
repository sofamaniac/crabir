// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'data_settings.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_DataSettings _$DataSettingsFromJson(Map<String, dynamic> json) =>
    _DataSettings(
      mobileDataSaver: json['mobileDataSaver'] as bool? ?? false,
      wifiDataSaver: json['wifiDataSaver'] as bool? ?? false,
      autoplay: $enumDecodeNullable(_$ImageLoadingEnumMap, json['autoplay']) ??
          ImageLoading.always,
      videoQuality:
          $enumDecodeNullable(_$ResolutionEnumMap, json['videoQuality']) ??
              Resolution.source,
      loadImages:
          $enumDecodeNullable(_$ImageLoadingEnumMap, json['loadImages']) ??
              ImageLoading.always,
      preferredQuality:
          $enumDecodeNullable(_$ResolutionEnumMap, json['preferredQuality']) ??
              Resolution.source,
    );

Map<String, dynamic> _$DataSettingsToJson(_DataSettings instance) =>
    <String, dynamic>{
      'mobileDataSaver': instance.mobileDataSaver,
      'wifiDataSaver': instance.wifiDataSaver,
      'autoplay': _$ImageLoadingEnumMap[instance.autoplay]!,
      'videoQuality': _$ResolutionEnumMap[instance.videoQuality]!,
      'loadImages': _$ImageLoadingEnumMap[instance.loadImages]!,
      'preferredQuality': _$ResolutionEnumMap[instance.preferredQuality]!,
    };

const _$ImageLoadingEnumMap = {
  ImageLoading.onWifiOnly: 'onWifiOnly',
  ImageLoading.always: 'always',
  ImageLoading.disable: 'disable',
};

const _$ResolutionEnumMap = {
  Resolution.source: 'source',
  Resolution.high: 'high',
  Resolution.medium: 'medium',
  Resolution.low: 'low',
};
