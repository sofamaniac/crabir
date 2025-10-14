// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'filters_settings.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

GlobalFilters _$GlobalFiltersFromJson(Map<String, dynamic> json) =>
    GlobalFilters(
      users:
          (json['users'] as List<dynamic>?)?.map((e) => e as String).toList() ??
              const [],
      flairs: (json['flairs'] as List<dynamic>?)
              ?.map((e) => Flair.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      subreddits: (json['subreddits'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      domains: (json['domains'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
    );

Map<String, dynamic> _$GlobalFiltersToJson(GlobalFilters instance) =>
    <String, dynamic>{
      'users': instance.users,
      'flairs': instance.flairs,
      'subreddits': instance.subreddits,
      'domains': instance.domains,
    };

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
