// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'comments_settings.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_CommentsSetttings _$CommentsSetttingsFromJson(Map<String, dynamic> json) =>
    _CommentsSetttings(
      sort: $enumDecodeNullable(_$CommentSortEnumMap, json['sort']) ??
          CommentSort.top,
      useSuggestedSort: json['useSuggestedSort'] as bool? ?? true,
      showNavigationBar: json['showNavigationBar'] as bool? ?? true,
      showUserAvatar: json['showUserAvatar'] as bool? ?? true,
      showCommentsImage: json['showCommentsImage'] as bool? ?? true,
      buttonsAlwaysVisible: json['buttonsAlwaysVisible'] as bool? ?? true,
      hideButtonAfterAction: json['hideButtonAfterAction'] as bool? ?? true,
      collapseAutoMod: json['collapseAutoMod'] as bool? ?? true,
      collapseDisruptiveComment:
          json['collapseDisruptiveComment'] as bool? ?? true,
      showPostUpvotePercentage:
          json['showPostUpvotePercentage'] as bool? ?? true,
      highlightMyUsername: json['highlightMyUsername'] as bool? ?? true,
      showFloatingButton: json['showFloatingButton'] as bool? ?? true,
      showAwards: json['showAwards'] as bool? ?? true,
      clickableAwards: json['clickableAwards'] as bool? ?? true,
      showUserFlair: json['showUserFlair'] as bool? ?? true,
      showFlairColors: json['showFlairColors'] as bool? ?? true,
      showFlairEmojis: json['showFlairEmojis'] as bool? ?? true,
      clickToCollapse: json['clickToCollapse'] as bool? ?? true,
      hideTextCollapsed: json['hideTextCollapsed'] as bool? ?? true,
      loadCollapsed: json['loadCollapsed'] as bool? ?? true,
      animateCollapse: json['animateCollapse'] as bool? ?? true,
      clickableUsername: json['clickableUsername'] as bool? ?? true,
      highlightNewComments: json['highlightNewComments'] as bool? ?? true,
      volumeRockerNavigation: json['volumeRockerNavigation'] as bool? ?? true,
      animateNavigation: json['animateNavigation'] as bool? ?? true,
      showSaveButton: json['showSaveButton'] as bool? ?? true,
      swipeToClose: json['swipeToClose'] as bool? ?? true,
    );

Map<String, dynamic> _$CommentsSetttingsToJson(_CommentsSetttings instance) =>
    <String, dynamic>{
      'sort': _$CommentSortEnumMap[instance.sort]!,
      'useSuggestedSort': instance.useSuggestedSort,
      'showNavigationBar': instance.showNavigationBar,
      'showUserAvatar': instance.showUserAvatar,
      'showCommentsImage': instance.showCommentsImage,
      'buttonsAlwaysVisible': instance.buttonsAlwaysVisible,
      'hideButtonAfterAction': instance.hideButtonAfterAction,
      'collapseAutoMod': instance.collapseAutoMod,
      'collapseDisruptiveComment': instance.collapseDisruptiveComment,
      'showPostUpvotePercentage': instance.showPostUpvotePercentage,
      'highlightMyUsername': instance.highlightMyUsername,
      'showFloatingButton': instance.showFloatingButton,
      'showAwards': instance.showAwards,
      'clickableAwards': instance.clickableAwards,
      'showUserFlair': instance.showUserFlair,
      'showFlairColors': instance.showFlairColors,
      'showFlairEmojis': instance.showFlairEmojis,
      'clickToCollapse': instance.clickToCollapse,
      'hideTextCollapsed': instance.hideTextCollapsed,
      'loadCollapsed': instance.loadCollapsed,
      'animateCollapse': instance.animateCollapse,
      'clickableUsername': instance.clickableUsername,
      'highlightNewComments': instance.highlightNewComments,
      'volumeRockerNavigation': instance.volumeRockerNavigation,
      'animateNavigation': instance.animateNavigation,
      'showSaveButton': instance.showSaveButton,
      'swipeToClose': instance.swipeToClose,
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
