// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'layout_settings.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RememberedView _$RememberedViewFromJson(Map<String, dynamic> json) =>
    RememberedView(
      data: (json['data'] as Map<String, dynamic>?)?.map(
            (k, e) => MapEntry(k, $enumDecode(_$ViewKindEnumMap, e)),
          ) ??
          const {},
    );

Map<String, dynamic> _$RememberedViewToJson(RememberedView instance) =>
    <String, dynamic>{
      'data': instance.data.map((k, e) => MapEntry(k, _$ViewKindEnumMap[e]!)),
    };

const _$ViewKindEnumMap = {
  ViewKind.card: 'card',
  ViewKind.compact: 'compact',
};

_LayoutSettings _$LayoutSettingsFromJson(Map<String, dynamic> json) =>
    _LayoutSettings(
      defaultView:
          $enumDecodeNullable(_$ViewKindEnumMap, json['defaultView']) ??
              ViewKind.card,
      rememberByCommunity: json['rememberByCommunity'] as bool? ?? false,
      manageViews: () ?? (),
      font: () ?? (),
      thumbnailOnLeft: json['thumbnailOnLeft'] as bool? ?? false,
      prefixCommunities: json['prefixCommunities'] as bool? ?? false,
    );

Map<String, dynamic> _$LayoutSettingsToJson(_LayoutSettings instance) =>
    <String, dynamic>{
      'defaultView': _$ViewKindEnumMap[instance.defaultView]!,
      'rememberByCommunity': instance.rememberByCommunity,
      'manageViews': <String, dynamic>{},
      'font': <String, dynamic>{},
      'thumbnailOnLeft': instance.thumbnailOnLeft,
      'prefixCommunities': instance.prefixCommunities,
    };
