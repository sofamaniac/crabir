// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'layout_settings.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Layout _$LayoutFromJson(Map<String, dynamic> json) => Layout(
      view: $enumDecode(_$ViewKindEnumMap, json['view']),
      columns: (json['columns'] as num).toInt(),
    );

Map<String, dynamic> _$LayoutToJson(Layout instance) => <String, dynamic>{
      'view': _$ViewKindEnumMap[instance.view]!,
      'columns': instance.columns,
    };

const _$ViewKindEnumMap = {
  ViewKind.card: 'card',
  ViewKind.compact: 'compact',
  ViewKind.dense: 'dense',
};

RememberedView _$RememberedViewFromJson(Map<String, dynamic> json) =>
    RememberedView(
      data: (json['data'] as Map<String, dynamic>?)?.map(
            (k, e) => MapEntry(k, Layout.fromJson(e as Map<String, dynamic>)),
          ) ??
          const {},
    );

Map<String, dynamic> _$RememberedViewToJson(RememberedView instance) =>
    <String, dynamic>{
      'data': instance.data,
    };

_LayoutSettings _$LayoutSettingsFromJson(Map<String, dynamic> json) =>
    _LayoutSettings(
      defaultView:
          $enumDecodeNullable(_$ViewKindEnumMap, json['defaultView']) ??
              ViewKind.card,
      rememberByCommunity: json['rememberByCommunity'] as bool? ?? false,
      rememberedView: json['rememberedView'] == null
          ? const RememberedView()
          : RememberedView.fromJson(
              json['rememberedView'] as Map<String, dynamic>),
      font: () ?? (),
      showThumbnail: json['showThumbnail'] as bool? ?? true,
      thumbnailOnLeft: json['thumbnailOnLeft'] as bool? ?? false,
      prefixCommunities: json['prefixCommunities'] as bool? ?? false,
      previewText: json['previewText'] as bool? ?? true,
      previewTextLength: (json['previewTextLength'] as num?)?.toInt() ?? 5,
    );

Map<String, dynamic> _$LayoutSettingsToJson(_LayoutSettings instance) =>
    <String, dynamic>{
      'defaultView': _$ViewKindEnumMap[instance.defaultView]!,
      'rememberByCommunity': instance.rememberByCommunity,
      'rememberedView': instance.rememberedView,
      'font': <String, dynamic>{},
      'showThumbnail': instance.showThumbnail,
      'thumbnailOnLeft': instance.thumbnailOnLeft,
      'prefixCommunities': instance.prefixCommunities,
      'previewText': instance.previewText,
      'previewTextLength': instance.previewTextLength,
    };
