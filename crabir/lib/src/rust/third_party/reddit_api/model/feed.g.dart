// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'feed.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

FeedSort_Best _$FeedSort_BestFromJson(Map<String, dynamic> json) =>
    FeedSort_Best(
      $type: json['runtimeType'] as String?,
    );

Map<String, dynamic> _$FeedSort_BestToJson(FeedSort_Best instance) =>
    <String, dynamic>{
      'runtimeType': instance.$type,
    };

FeedSort_Hot _$FeedSort_HotFromJson(Map<String, dynamic> json) => FeedSort_Hot(
      $type: json['runtimeType'] as String?,
    );

Map<String, dynamic> _$FeedSort_HotToJson(FeedSort_Hot instance) =>
    <String, dynamic>{
      'runtimeType': instance.$type,
    };

FeedSort_New _$FeedSort_NewFromJson(Map<String, dynamic> json) => FeedSort_New(
      $enumDecode(_$TimeframeEnumMap, json['field0']),
      $type: json['runtimeType'] as String?,
    );

Map<String, dynamic> _$FeedSort_NewToJson(FeedSort_New instance) =>
    <String, dynamic>{
      'field0': _$TimeframeEnumMap[instance.field0]!,
      'runtimeType': instance.$type,
    };

const _$TimeframeEnumMap = {
  Timeframe.hour: 'hour',
  Timeframe.day: 'day',
  Timeframe.week: 'week',
  Timeframe.month: 'month',
  Timeframe.year: 'year',
  Timeframe.all: 'all',
};

FeedSort_Top _$FeedSort_TopFromJson(Map<String, dynamic> json) => FeedSort_Top(
      $enumDecode(_$TimeframeEnumMap, json['field0']),
      $type: json['runtimeType'] as String?,
    );

Map<String, dynamic> _$FeedSort_TopToJson(FeedSort_Top instance) =>
    <String, dynamic>{
      'field0': _$TimeframeEnumMap[instance.field0]!,
      'runtimeType': instance.$type,
    };

FeedSort_Rising _$FeedSort_RisingFromJson(Map<String, dynamic> json) =>
    FeedSort_Rising(
      $type: json['runtimeType'] as String?,
    );

Map<String, dynamic> _$FeedSort_RisingToJson(FeedSort_Rising instance) =>
    <String, dynamic>{
      'runtimeType': instance.$type,
    };

FeedSort_Controversial _$FeedSort_ControversialFromJson(
        Map<String, dynamic> json) =>
    FeedSort_Controversial(
      $enumDecode(_$TimeframeEnumMap, json['field0']),
      $type: json['runtimeType'] as String?,
    );

Map<String, dynamic> _$FeedSort_ControversialToJson(
        FeedSort_Controversial instance) =>
    <String, dynamic>{
      'field0': _$TimeframeEnumMap[instance.field0]!,
      'runtimeType': instance.$type,
    };
