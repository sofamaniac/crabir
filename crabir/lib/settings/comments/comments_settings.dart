import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:crabir/thread/widgets/thread.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:auto_route/annotations.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';

part 'comments_settings.freezed.dart';
part 'comments_settings.settings_page.dart';
part 'comments_settings.g.dart';
part '__comments_sort_selection.dart';

@freezed
@SettingsPage(prefix: "comments_", useFieldName: true)
abstract class CommentsSettings with _$CommentsSettings {
  CommentsSettings._();
  factory CommentsSettings({
    @Setting(widget: _CommentsSortSelection)
    @Default(CommentSort.top)
    CommentSort defaultSort,
    @Setting() @Default(true) bool useSuggestedSort,
    @Category(name: "Appearance")
    @Setting()
    @Default(true)
    // TODO:
    bool showNavigationBar,
    // TODO: (requires an additional api call)
    @Setting() @Default(true) bool showUserAvatar,
    // TODO: I don't understand how `flutter_html` renders images.
    @Setting() @Default(true) bool showCommentsImage,
    //@Default() MediaPreviewSize postMediaPreviewSize,
    @Setting() @Default(false) bool buttonsAlwaysVisible,
    @Setting() @Default(true) bool hideButtonAfterAction,
    // TODO:
    @Setting() @Default(true) bool collapseAutoMod,
    // TODO:
    @Setting() @Default(true) bool collapseDisruptiveComment,
    // TODO:
    @Setting() @Default(true) bool showPostUpvotePercentage,
    //@Default() GuideStyle threadGuide,
    @Setting() @Default(true) bool highlightMyUsername,
    // TODO:
    @Setting() @Default(true) bool showFloatingButton,
    // TODO:
    @Category(name: "Awards") @Setting() @Default(true) bool showAwards,
    // TODO:
    @Setting() @Default(true) bool clickableAwards,
    @Category(name: "Flairs") @Setting() @Default(true) bool showUserFlair,
    // TODO:
    @Setting() @Default(true) bool showFlairColors,
    // TODO:
    @Setting() @Default(true) bool showFlairEmojis,
    // TODO:
    @Category(name: "Behavior") @Setting() @Default(true) bool clickToCollapse,
    // TODO:
    @Setting() @Default(true) bool hideTextCollapsed,
    // TODO:
    @Setting() @Default(true) bool loadCollapsed,
    // TODO:
    @Setting() @Default(true) bool animateCollapse,
    @Setting() @Default(true) bool clickableUsername,
    @Category(name: "Navigation")
    @Setting()
    @Default(true)
    // TODO:
    bool highlightNewComments,
    // TODO:
    //@Default() NavigationMode defaultNavigationMode,
    // TODO:
    @Setting() @Default(true) bool volumeRockerNavigation,
    // TODO:
    @Setting() @Default(true) bool animateNavigation,
    @Category(name: "Visible buttons")
    @Setting()
    @Default(true)
    bool showSaveButton,
    // TODO:
    //@Default() GoToTopButtonAction goToTopButton,
    @Category(name: "Gestures") @Setting() @Default(true) bool swipeToClose,
  }) = _CommentsSetttings;
  factory CommentsSettings.fromJson(Map<String, dynamic> json) =>
      _$CommentsSetttingsFromJson(json);
}
