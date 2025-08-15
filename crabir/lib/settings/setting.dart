import 'package:flutter/material.dart';

sealed class Setting<T> {
  /// name used in shared preferences
  String name;

  /// The name to display
  String title();

  /// An explanation of the setting
  String? subtitle();

  /// The icon to use.
  IconData? icon;

  T _value;

  Setting(T value, {required this.name}) : _value = value;
}

// class CommentsSettings {
//   final CommentSort sort;
//   final bool useSuggestedSort;
//
//   final bool showNavigationBar;
//   final bool showUserAvatar;
//   final String postMediaPreviewSize;
//   final bool buttonsAlwaysVisible;
//   final bool hideButtonAfterAction;
//   final bool collapseAutoMod;
//   final bool collapseDisruptiveComment;
//   final bool showPostUpvotePercentage;
//   final String threadGuide;
//   final bool highlightMyUsername;
//   final bool showFloatingButton;
//   final bool showAwards;
//   final bool clickableAwards;
//   final bool showUserFlair;
//   final bool showFlairColors;
//   final bool showFlairEmojis;
//
//   final bool clickToCollapse;
//   final bool hideTextCollapsed;
//   final bool loadCollapsed;
//   final bool animateCollapse;
//
//   final bool clickableUsername;
//
//   final bool highlightNewComments;
//   final String defaultNavigationMode;
//   final bool volumeRockerNavigation;
//   final bool animateNavigation;
//
//   final bool showSaveButton;
//   final String goToTopButton;
//
//   final bool swipeToClose;
// }
