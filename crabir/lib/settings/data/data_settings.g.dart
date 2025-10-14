// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'data_settings.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_DataSettings _$DataSettingsFromJson(Map<String, dynamic> json) =>
    _DataSettings(
      autoplay: $enumDecodeNullable(_$ImageLoadingEnumMap, json['autoplay']) ??
          ImageLoading.always,
      videoQualityWifi:
          $enumDecodeNullable(_$ResolutionEnumMap, json['videoQualityWifi']) ??
              Resolution.source,
      videoQualityCellular: $enumDecodeNullable(
              _$ResolutionEnumMap, json['videoQualityCellular']) ??
          Resolution.source,
      loadImages:
          $enumDecodeNullable(_$ImageLoadingEnumMap, json['loadImages']) ??
              ImageLoading.always,
      imageQualityWifi:
          $enumDecodeNullable(_$ResolutionEnumMap, json['imageQualityWifi']) ??
              Resolution.source,
      imageQualityCellular: $enumDecodeNullable(
              _$ResolutionEnumMap, json['imageQualityCellular']) ??
          Resolution.source,
    );

Map<String, dynamic> _$DataSettingsToJson(_DataSettings instance) =>
    <String, dynamic>{
      'autoplay': _$ImageLoadingEnumMap[instance.autoplay]!,
      'videoQualityWifi': _$ResolutionEnumMap[instance.videoQualityWifi]!,
      'videoQualityCellular':
          _$ResolutionEnumMap[instance.videoQualityCellular]!,
      'loadImages': _$ImageLoadingEnumMap[instance.loadImages]!,
      'imageQualityWifi': _$ResolutionEnumMap[instance.imageQualityWifi]!,
      'imageQualityCellular':
          _$ResolutionEnumMap[instance.imageQualityCellular]!,
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
