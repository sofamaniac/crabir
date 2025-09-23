// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'theme.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_CrabirTheme _$CrabirThemeFromJson(Map<String, dynamic> json) => _CrabirTheme(
      background: json['background'] == null
          ? Colors.black
          : const ColorConverter().fromJson(json['background'] as String),
      cardBackground: json['cardBackground'] == null
          ? Colors.black
          : const ColorConverter().fromJson(json['cardBackground'] as String),
      toolBarBackground: json['toolBarBackground'] == null
          ? Colors.black
          : const ColorConverter()
              .fromJson(json['toolBarBackground'] as String),
      toolBarText: json['toolBarText'] == null
          ? Colors.white
          : const ColorConverter().fromJson(json['toolBarText'] as String),
      primaryColor: json['primaryColor'] == null
          ? const Color(0xffff6e40)
          : const ColorConverter().fromJson(json['primaryColor'] as String),
      alternativeText: json['alternativeText'] == null
          ? const Color(0xFFB7B8BC)
          : const ColorConverter().fromJson(json['alternativeText'] as String),
      highlight: json['highlight'] == null
          ? const Color(0xffff0000)
          : const ColorConverter().fromJson(json['highlight'] as String),
      postTitle: json['postTitle'] == null
          ? const Color(0xFFF5F6F8)
          : const ColorConverter().fromJson(json['postTitle'] as String),
      readPost: json['readPost'] == null
          ? const Color(0xFFB7B8BC)
          : const ColorConverter().fromJson(json['readPost'] as String),
      announcement: json['announcement'] == null
          ? const Color(0xFF00FF00)
          : const ColorConverter().fromJson(json['announcement'] as String),
      contentText: json['contentText'] == null
          ? const Color(0xFFF5F6F8)
          : const ColorConverter().fromJson(json['contentText'] as String),
      linkColor: json['linkColor'] == null
          ? const Color(0xFF4B91E2)
          : const ColorConverter().fromJson(json['linkColor'] as String),
      downvoteContent: json['downvoteContent'] ?? const Color(0xFF9580FF),
    );

Map<String, dynamic> _$CrabirThemeToJson(_CrabirTheme instance) =>
    <String, dynamic>{
      'background': const ColorConverter().toJson(instance.background),
      'cardBackground': const ColorConverter().toJson(instance.cardBackground),
      'toolBarBackground':
          const ColorConverter().toJson(instance.toolBarBackground),
      'toolBarText': const ColorConverter().toJson(instance.toolBarText),
      'primaryColor': const ColorConverter().toJson(instance.primaryColor),
      'alternativeText':
          const ColorConverter().toJson(instance.alternativeText),
      'highlight': const ColorConverter().toJson(instance.highlight),
      'postTitle': const ColorConverter().toJson(instance.postTitle),
      'readPost': const ColorConverter().toJson(instance.readPost),
      'announcement': const ColorConverter().toJson(instance.announcement),
      'contentText': const ColorConverter().toJson(instance.contentText),
      'linkColor': const ColorConverter().toJson(instance.linkColor),
      'downvoteContent': instance.downvoteContent,
    };
