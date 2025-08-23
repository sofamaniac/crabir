import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:auto_route/annotations.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';

part 'comments_settings.freezed.dart';
part 'comments_settings.settings_page.dart';
part 'comments_settings.g.dart';

@freezed
@SettingsPage(prefix: "comments_", useFieldName: true)
abstract class CommentsSettings with _$CommentsSettings {
  CommentsSettings._();
  factory CommentsSettings({
    @Default(CommentSort.top) CommentSort sort,
    @Setting() @Default(true) bool useSuggestedSort,
    @Category(name: "Appearance")
    @Setting()
    @Default(true)
    bool showNavigationBar,
    @Setting() @Default(true) bool showUserAvatar,
    //@Default() MediaPreviewSize postMediaPreviewSize,
    @Setting() @Default(true) bool buttonsAlwaysVisible,
    @Setting() @Default(true) bool hideButtonAfterAction,
    @Setting() @Default(true) bool collapseAutoMod,
    @Setting() @Default(true) bool collapseDisruptiveComment,
    @Setting() @Default(true) bool showPostUpvotePercentage,
    //@Default() GuideStyle threadGuide,
    @Setting() @Default(true) bool highlightMyUsername,
    @Setting() @Default(true) bool showFloatingButton,
    @Category(name: "Awards") @Setting() @Default(true) bool showAwards,
    @Setting() @Default(true) bool clickableAwards,
    @Category(name: "Flairs") @Setting() @Default(true) bool showUserFlair,
    @Setting() @Default(true) bool showFlairColors,
    @Setting() @Default(true) bool showFlairEmojis,
    @Category(name: "Behavior") @Setting() @Default(true) bool clickToCollapse,
    @Setting() @Default(true) bool hideTextCollapsed,
    @Setting() @Default(true) bool loadCollapsed,
    @Setting() @Default(true) bool animateCollapse,
    @Setting() @Default(true) bool clickableUsername,
    @Category(name: "Navigation")
    @Setting()
    @Default(true)
    bool highlightNewComments,
    //@Default() NavigationMode defaultNavigationMode,
    @Setting() @Default(true) bool volumeRockerNavigation,
    @Setting() @Default(true) bool animateNavigation,
    @Category(name: "Visible buttons")
    @Setting()
    @Default(true)
    bool showSaveButton,
    //@Default() String goToTopButton,
    @Category(name: "Gestures") @Setting() @Default(true) bool swipeToClose,
  }) = _CommentsSetttings;
  factory CommentsSettings.fromJson(Map<String, dynamic> json) =>
      _$CommentsSetttingsFromJson(json);
}
