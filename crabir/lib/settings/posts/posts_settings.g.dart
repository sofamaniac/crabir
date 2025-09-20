// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'posts_settings.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RememberedSort _$RememberedSortFromJson(Map<String, dynamic> json) =>
    RememberedSort();

Map<String, dynamic> _$RememberedSortToJson(RememberedSort instance) =>
    <String, dynamic>{};

_PostsSettings _$PostsSettingsFromJson(Map<String, dynamic> json) =>
    _PostsSettings(
      defaultHomeSort: json['defaultHomeSort'] == null
          ? const FeedSort.best()
          : FeedSort.fromJson(json['defaultHomeSort'] as Map<String, dynamic>),
      defaultSort: json['defaultSort'] == null
          ? const FeedSort.hot()
          : FeedSort.fromJson(json['defaultSort'] as Map<String, dynamic>),
      rememberSortByCommunity: json['rememberSortByCommunity'] as bool? ?? true,
      rememberedSorts: json['rememberedSorts'] == null
          ? const RememberedSort()
          : RememberedSort.fromJson(
              json['rememberedSorts'] as Map<String, dynamic>),
      showAwards: json['showAwards'] as bool? ?? true,
      clickableAwards: json['clickableAwards'] as bool? ?? true,
      showPostFlair: json['showPostFlair'] as bool? ?? true,
      showFlairColors: json['showFlairColors'] as bool? ?? true,
      showEmojis: json['showEmojis'] as bool? ?? true,
      tapFlairToSearch: json['tapFlairToSearch'] as bool? ?? true,
      showAuthor: json['showAuthor'] as bool? ?? true,
      clickableCommunity: json['clickableCommunity'] as bool? ?? true,
      clickableUser: json['clickableUser'] as bool? ?? true,
      showFloatingButton: json['showFloatingButton'] as bool? ?? true,
      showHideButton: json['showHideButton'] as bool? ?? false,
      showMarkAsReadButton: json['showMarkAsReadButton'] as bool? ?? false,
      showShareButton: json['showShareButton'] as bool? ?? false,
      showCommentsButton: json['showCommentsButton'] as bool? ?? true,
      showOpenInAppButton: json['showOpenInAppButton'] as bool? ?? true,
    );

Map<String, dynamic> _$PostsSettingsToJson(_PostsSettings instance) =>
    <String, dynamic>{
      'defaultHomeSort': instance.defaultHomeSort,
      'defaultSort': instance.defaultSort,
      'rememberSortByCommunity': instance.rememberSortByCommunity,
      'rememberedSorts': instance.rememberedSorts,
      'showAwards': instance.showAwards,
      'clickableAwards': instance.clickableAwards,
      'showPostFlair': instance.showPostFlair,
      'showFlairColors': instance.showFlairColors,
      'showEmojis': instance.showEmojis,
      'tapFlairToSearch': instance.tapFlairToSearch,
      'showAuthor': instance.showAuthor,
      'clickableCommunity': instance.clickableCommunity,
      'clickableUser': instance.clickableUser,
      'showFloatingButton': instance.showFloatingButton,
      'showHideButton': instance.showHideButton,
      'showMarkAsReadButton': instance.showMarkAsReadButton,
      'showShareButton': instance.showShareButton,
      'showCommentsButton': instance.showCommentsButton,
      'showOpenInAppButton': instance.showOpenInAppButton,
    };
