// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'filters_settings.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_FiltersSettings _$FiltersSettingsFromJson(Map<String, dynamic> json) =>
    _FiltersSettings(
      manageHiddenCommunities: () ?? (),
      manageHiddenDomains: () ?? (),
      manageHiddenUsers: () ?? (),
      manageHiddenFlairs: () ?? (),
      showNSFW: json['showNSFW'] as bool? ?? false,
      showImageInNSFW: json['showImageInNSFW'] as bool? ?? true,
      blurNSFW: json['blurNSFW'] as bool? ?? false,
    );

Map<String, dynamic> _$FiltersSettingsToJson(_FiltersSettings instance) =>
    <String, dynamic>{
      'manageHiddenCommunities': <String, dynamic>{},
      'manageHiddenDomains': <String, dynamic>{},
      'manageHiddenUsers': <String, dynamic>{},
      'manageHiddenFlairs': <String, dynamic>{},
      'showNSFW': instance.showNSFW,
      'showImageInNSFW': instance.showImageInNSFW,
      'blurNSFW': instance.blurNSFW,
    };
