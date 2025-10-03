// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'comments_settings.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_CommentsSetttings _$CommentsSetttingsFromJson(Map<String, dynamic> json) =>
    _CommentsSetttings(
      defaultSort:
          $enumDecodeNullable(_$CommentSortEnumMap, json['defaultSort']) ??
              CommentSort.top,
      useSuggestedSort: json['useSuggestedSort'] as bool? ?? true,
      showNavigationBar: json['showNavigationBar'] as bool? ?? true,
      showCommentsImage: json['showCommentsImage'] as bool? ?? true,
      postMediaPreviewSize: $enumDecodeNullable(
              _$MediaPreviewSizeEnumMap, json['postMediaPreviewSize']) ??
          MediaPreviewSize.thumbnail,
      buttonsAlwaysVisible: json['buttonsAlwaysVisible'] as bool? ?? false,
      hideButtonAfterAction: json['hideButtonAfterAction'] as bool? ?? true,
      collapseAutoMod: json['collapseAutoMod'] as bool? ?? true,
      collapseDisruptiveComment:
          json['collapseDisruptiveComment'] as bool? ?? true,
      showPostUpvotePercentage:
          json['showPostUpvotePercentage'] as bool? ?? true,
      threadGuide:
          $enumDecodeNullable(_$IndentationStyleEnumMap, json['threadGuide']) ??
              IndentationStyle.lines,
      highlightMyUsername: json['highlightMyUsername'] as bool? ?? true,
      showUserFlair: json['showUserFlair'] as bool? ?? true,
      showFlairColors: json['showFlairColors'] as bool? ?? true,
      showFlairEmojis: json['showFlairEmojis'] as bool? ?? true,
      clickToCollapse: $enumDecodeNullable(
              _$CollapseActionEnumMap, json['clickToCollapse']) ??
          CollapseAction.hold,
      hideTextCollapsed: json['hideTextCollapsed'] as bool? ?? true,
      loadCollapsed: json['loadCollapsed'] as bool? ?? true,
      animateCollapse: json['animateCollapse'] as bool? ?? true,
      clickableUsername: json['clickableUsername'] as bool? ?? true,
      showSaveButton: json['showSaveButton'] as bool? ?? true,
      swipeToClose: json['swipeToClose'] as bool? ?? true,
      distanceThreshold: (json['distanceThreshold'] as num?)?.toInt() ?? 35,
    );

Map<String, dynamic> _$CommentsSetttingsToJson(_CommentsSetttings instance) =>
    <String, dynamic>{
      'defaultSort': _$CommentSortEnumMap[instance.defaultSort]!,
      'useSuggestedSort': instance.useSuggestedSort,
      'showNavigationBar': instance.showNavigationBar,
      'showCommentsImage': instance.showCommentsImage,
      'postMediaPreviewSize':
          _$MediaPreviewSizeEnumMap[instance.postMediaPreviewSize]!,
      'buttonsAlwaysVisible': instance.buttonsAlwaysVisible,
      'hideButtonAfterAction': instance.hideButtonAfterAction,
      'collapseAutoMod': instance.collapseAutoMod,
      'collapseDisruptiveComment': instance.collapseDisruptiveComment,
      'showPostUpvotePercentage': instance.showPostUpvotePercentage,
      'threadGuide': _$IndentationStyleEnumMap[instance.threadGuide]!,
      'highlightMyUsername': instance.highlightMyUsername,
      'showUserFlair': instance.showUserFlair,
      'showFlairColors': instance.showFlairColors,
      'showFlairEmojis': instance.showFlairEmojis,
      'clickToCollapse': _$CollapseActionEnumMap[instance.clickToCollapse]!,
      'hideTextCollapsed': instance.hideTextCollapsed,
      'loadCollapsed': instance.loadCollapsed,
      'animateCollapse': instance.animateCollapse,
      'clickableUsername': instance.clickableUsername,
      'showSaveButton': instance.showSaveButton,
      'swipeToClose': instance.swipeToClose,
      'distanceThreshold': instance.distanceThreshold,
    };

const _$CommentSortEnumMap = {
  CommentSort.confidence: 'confidence',
  CommentSort.top: 'top',
  CommentSort.new_: 'new_',
  CommentSort.controversial: 'controversial',
  CommentSort.old: 'old',
  CommentSort.random: 'random',
  CommentSort.qa: 'qa',
  CommentSort.live: 'live',
};

const _$MediaPreviewSizeEnumMap = {
  MediaPreviewSize.none: 'none',
  MediaPreviewSize.thumbnail: 'thumbnail',
  MediaPreviewSize.fullPreview: 'fullPreview',
};

const _$IndentationStyleEnumMap = {
  IndentationStyle.lines: 'lines',
  IndentationStyle.coloredLines: 'coloredLines',
  IndentationStyle.bars: 'bars',
};

const _$CollapseActionEnumMap = {
  CollapseAction.tap: 'tap',
  CollapseAction.hold: 'hold',
};
