// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'setting.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$CommentsSettings {
  CommentSort get sort;
  @Setting()
  bool get useSuggestedSort;
  @Setting()
  bool get showNavigationBar;
  @Setting()
  bool get showUserAvatar; //@Default() MediaPreviewSize postMediaPreviewSize,
  @Setting()
  bool get buttonsAlwaysVisible;
  @Setting()
  bool get hideButtonAfterAction;
  @Setting()
  bool get collapseAutoMod;
  @Setting()
  bool get collapseDisruptiveComment;
  @Setting()
  bool get showPostUpvotePercentage; //@Default() GuideStyle threadGuide,
  @Setting()
  bool get highlightMyUsername;
  @Setting()
  bool get showFloatingButton;
  @Setting()
  bool get showAwards;
  @Setting()
  bool get clickableAwards;
  @Setting()
  bool get showUserFlair;
  @Setting()
  bool get showFlairColors;
  @Setting()
  bool get showFlairEmojis;
  @Setting()
  bool get clickToCollapse;
  @Setting()
  bool get hideTextCollapsed;
  @Setting()
  bool get loadCollapsed;
  @Setting()
  bool get animateCollapse;
  @Setting()
  bool get clickableUsername;
  @Setting()
  bool
      get highlightNewComments; //@Default() NavigationMode defaultNavigationMode,
  @Setting()
  bool get volumeRockerNavigation;
  @Setting()
  bool get animateNavigation;
  @Setting()
  bool get showSaveButton; //@Default() String goToTopButton,
  @Setting()
  bool get swipeToClose;

  /// Create a copy of CommentsSettings
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $CommentsSettingsCopyWith<CommentsSettings> get copyWith =>
      _$CommentsSettingsCopyWithImpl<CommentsSettings>(
          this as CommentsSettings, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is CommentsSettings &&
            (identical(other.sort, sort) || other.sort == sort) &&
            (identical(other.useSuggestedSort, useSuggestedSort) ||
                other.useSuggestedSort == useSuggestedSort) &&
            (identical(other.showNavigationBar, showNavigationBar) ||
                other.showNavigationBar == showNavigationBar) &&
            (identical(other.showUserAvatar, showUserAvatar) ||
                other.showUserAvatar == showUserAvatar) &&
            (identical(other.buttonsAlwaysVisible, buttonsAlwaysVisible) ||
                other.buttonsAlwaysVisible == buttonsAlwaysVisible) &&
            (identical(other.hideButtonAfterAction, hideButtonAfterAction) ||
                other.hideButtonAfterAction == hideButtonAfterAction) &&
            (identical(other.collapseAutoMod, collapseAutoMod) ||
                other.collapseAutoMod == collapseAutoMod) &&
            (identical(other.collapseDisruptiveComment,
                    collapseDisruptiveComment) ||
                other.collapseDisruptiveComment == collapseDisruptiveComment) &&
            (identical(other.showPostUpvotePercentage, showPostUpvotePercentage) ||
                other.showPostUpvotePercentage == showPostUpvotePercentage) &&
            (identical(other.highlightMyUsername, highlightMyUsername) ||
                other.highlightMyUsername == highlightMyUsername) &&
            (identical(other.showFloatingButton, showFloatingButton) ||
                other.showFloatingButton == showFloatingButton) &&
            (identical(other.showAwards, showAwards) ||
                other.showAwards == showAwards) &&
            (identical(other.clickableAwards, clickableAwards) ||
                other.clickableAwards == clickableAwards) &&
            (identical(other.showUserFlair, showUserFlair) ||
                other.showUserFlair == showUserFlair) &&
            (identical(other.showFlairColors, showFlairColors) ||
                other.showFlairColors == showFlairColors) &&
            (identical(other.showFlairEmojis, showFlairEmojis) ||
                other.showFlairEmojis == showFlairEmojis) &&
            (identical(other.clickToCollapse, clickToCollapse) ||
                other.clickToCollapse == clickToCollapse) &&
            (identical(other.hideTextCollapsed, hideTextCollapsed) ||
                other.hideTextCollapsed == hideTextCollapsed) &&
            (identical(other.loadCollapsed, loadCollapsed) ||
                other.loadCollapsed == loadCollapsed) &&
            (identical(other.animateCollapse, animateCollapse) ||
                other.animateCollapse == animateCollapse) &&
            (identical(other.clickableUsername, clickableUsername) ||
                other.clickableUsername == clickableUsername) &&
            (identical(other.highlightNewComments, highlightNewComments) ||
                other.highlightNewComments == highlightNewComments) &&
            (identical(other.volumeRockerNavigation, volumeRockerNavigation) ||
                other.volumeRockerNavigation == volumeRockerNavigation) &&
            (identical(other.animateNavigation, animateNavigation) ||
                other.animateNavigation == animateNavigation) &&
            (identical(other.showSaveButton, showSaveButton) ||
                other.showSaveButton == showSaveButton) &&
            (identical(other.swipeToClose, swipeToClose) ||
                other.swipeToClose == swipeToClose));
  }

  @override
  int get hashCode => Object.hashAll([
        runtimeType,
        sort,
        useSuggestedSort,
        showNavigationBar,
        showUserAvatar,
        buttonsAlwaysVisible,
        hideButtonAfterAction,
        collapseAutoMod,
        collapseDisruptiveComment,
        showPostUpvotePercentage,
        highlightMyUsername,
        showFloatingButton,
        showAwards,
        clickableAwards,
        showUserFlair,
        showFlairColors,
        showFlairEmojis,
        clickToCollapse,
        hideTextCollapsed,
        loadCollapsed,
        animateCollapse,
        clickableUsername,
        highlightNewComments,
        volumeRockerNavigation,
        animateNavigation,
        showSaveButton,
        swipeToClose
      ]);

  @override
  String toString() {
    return 'CommentsSettings(sort: $sort, useSuggestedSort: $useSuggestedSort, showNavigationBar: $showNavigationBar, showUserAvatar: $showUserAvatar, buttonsAlwaysVisible: $buttonsAlwaysVisible, hideButtonAfterAction: $hideButtonAfterAction, collapseAutoMod: $collapseAutoMod, collapseDisruptiveComment: $collapseDisruptiveComment, showPostUpvotePercentage: $showPostUpvotePercentage, highlightMyUsername: $highlightMyUsername, showFloatingButton: $showFloatingButton, showAwards: $showAwards, clickableAwards: $clickableAwards, showUserFlair: $showUserFlair, showFlairColors: $showFlairColors, showFlairEmojis: $showFlairEmojis, clickToCollapse: $clickToCollapse, hideTextCollapsed: $hideTextCollapsed, loadCollapsed: $loadCollapsed, animateCollapse: $animateCollapse, clickableUsername: $clickableUsername, highlightNewComments: $highlightNewComments, volumeRockerNavigation: $volumeRockerNavigation, animateNavigation: $animateNavigation, showSaveButton: $showSaveButton, swipeToClose: $swipeToClose)';
  }
}

/// @nodoc
abstract mixin class $CommentsSettingsCopyWith<$Res> {
  factory $CommentsSettingsCopyWith(
          CommentsSettings value, $Res Function(CommentsSettings) _then) =
      _$CommentsSettingsCopyWithImpl;
  @useResult
  $Res call(
      {CommentSort sort,
      @Setting() bool useSuggestedSort,
      @Setting() bool showNavigationBar,
      @Setting() bool showUserAvatar,
      @Setting() bool buttonsAlwaysVisible,
      @Setting() bool hideButtonAfterAction,
      @Setting() bool collapseAutoMod,
      @Setting() bool collapseDisruptiveComment,
      @Setting() bool showPostUpvotePercentage,
      @Setting() bool highlightMyUsername,
      @Setting() bool showFloatingButton,
      @Setting() bool showAwards,
      @Setting() bool clickableAwards,
      @Setting() bool showUserFlair,
      @Setting() bool showFlairColors,
      @Setting() bool showFlairEmojis,
      @Setting() bool clickToCollapse,
      @Setting() bool hideTextCollapsed,
      @Setting() bool loadCollapsed,
      @Setting() bool animateCollapse,
      @Setting() bool clickableUsername,
      @Setting() bool highlightNewComments,
      @Setting() bool volumeRockerNavigation,
      @Setting() bool animateNavigation,
      @Setting() bool showSaveButton,
      @Setting() bool swipeToClose});
}

/// @nodoc
class _$CommentsSettingsCopyWithImpl<$Res>
    implements $CommentsSettingsCopyWith<$Res> {
  _$CommentsSettingsCopyWithImpl(this._self, this._then);

  final CommentsSettings _self;
  final $Res Function(CommentsSettings) _then;

  /// Create a copy of CommentsSettings
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? sort = null,
    Object? useSuggestedSort = null,
    Object? showNavigationBar = null,
    Object? showUserAvatar = null,
    Object? buttonsAlwaysVisible = null,
    Object? hideButtonAfterAction = null,
    Object? collapseAutoMod = null,
    Object? collapseDisruptiveComment = null,
    Object? showPostUpvotePercentage = null,
    Object? highlightMyUsername = null,
    Object? showFloatingButton = null,
    Object? showAwards = null,
    Object? clickableAwards = null,
    Object? showUserFlair = null,
    Object? showFlairColors = null,
    Object? showFlairEmojis = null,
    Object? clickToCollapse = null,
    Object? hideTextCollapsed = null,
    Object? loadCollapsed = null,
    Object? animateCollapse = null,
    Object? clickableUsername = null,
    Object? highlightNewComments = null,
    Object? volumeRockerNavigation = null,
    Object? animateNavigation = null,
    Object? showSaveButton = null,
    Object? swipeToClose = null,
  }) {
    return _then(_self.copyWith(
      sort: null == sort
          ? _self.sort
          : sort // ignore: cast_nullable_to_non_nullable
              as CommentSort,
      useSuggestedSort: null == useSuggestedSort
          ? _self.useSuggestedSort
          : useSuggestedSort // ignore: cast_nullable_to_non_nullable
              as bool,
      showNavigationBar: null == showNavigationBar
          ? _self.showNavigationBar
          : showNavigationBar // ignore: cast_nullable_to_non_nullable
              as bool,
      showUserAvatar: null == showUserAvatar
          ? _self.showUserAvatar
          : showUserAvatar // ignore: cast_nullable_to_non_nullable
              as bool,
      buttonsAlwaysVisible: null == buttonsAlwaysVisible
          ? _self.buttonsAlwaysVisible
          : buttonsAlwaysVisible // ignore: cast_nullable_to_non_nullable
              as bool,
      hideButtonAfterAction: null == hideButtonAfterAction
          ? _self.hideButtonAfterAction
          : hideButtonAfterAction // ignore: cast_nullable_to_non_nullable
              as bool,
      collapseAutoMod: null == collapseAutoMod
          ? _self.collapseAutoMod
          : collapseAutoMod // ignore: cast_nullable_to_non_nullable
              as bool,
      collapseDisruptiveComment: null == collapseDisruptiveComment
          ? _self.collapseDisruptiveComment
          : collapseDisruptiveComment // ignore: cast_nullable_to_non_nullable
              as bool,
      showPostUpvotePercentage: null == showPostUpvotePercentage
          ? _self.showPostUpvotePercentage
          : showPostUpvotePercentage // ignore: cast_nullable_to_non_nullable
              as bool,
      highlightMyUsername: null == highlightMyUsername
          ? _self.highlightMyUsername
          : highlightMyUsername // ignore: cast_nullable_to_non_nullable
              as bool,
      showFloatingButton: null == showFloatingButton
          ? _self.showFloatingButton
          : showFloatingButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showAwards: null == showAwards
          ? _self.showAwards
          : showAwards // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableAwards: null == clickableAwards
          ? _self.clickableAwards
          : clickableAwards // ignore: cast_nullable_to_non_nullable
              as bool,
      showUserFlair: null == showUserFlair
          ? _self.showUserFlair
          : showUserFlair // ignore: cast_nullable_to_non_nullable
              as bool,
      showFlairColors: null == showFlairColors
          ? _self.showFlairColors
          : showFlairColors // ignore: cast_nullable_to_non_nullable
              as bool,
      showFlairEmojis: null == showFlairEmojis
          ? _self.showFlairEmojis
          : showFlairEmojis // ignore: cast_nullable_to_non_nullable
              as bool,
      clickToCollapse: null == clickToCollapse
          ? _self.clickToCollapse
          : clickToCollapse // ignore: cast_nullable_to_non_nullable
              as bool,
      hideTextCollapsed: null == hideTextCollapsed
          ? _self.hideTextCollapsed
          : hideTextCollapsed // ignore: cast_nullable_to_non_nullable
              as bool,
      loadCollapsed: null == loadCollapsed
          ? _self.loadCollapsed
          : loadCollapsed // ignore: cast_nullable_to_non_nullable
              as bool,
      animateCollapse: null == animateCollapse
          ? _self.animateCollapse
          : animateCollapse // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableUsername: null == clickableUsername
          ? _self.clickableUsername
          : clickableUsername // ignore: cast_nullable_to_non_nullable
              as bool,
      highlightNewComments: null == highlightNewComments
          ? _self.highlightNewComments
          : highlightNewComments // ignore: cast_nullable_to_non_nullable
              as bool,
      volumeRockerNavigation: null == volumeRockerNavigation
          ? _self.volumeRockerNavigation
          : volumeRockerNavigation // ignore: cast_nullable_to_non_nullable
              as bool,
      animateNavigation: null == animateNavigation
          ? _self.animateNavigation
          : animateNavigation // ignore: cast_nullable_to_non_nullable
              as bool,
      showSaveButton: null == showSaveButton
          ? _self.showSaveButton
          : showSaveButton // ignore: cast_nullable_to_non_nullable
              as bool,
      swipeToClose: null == swipeToClose
          ? _self.swipeToClose
          : swipeToClose // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

/// @nodoc

class _CommentsSetttings extends CommentsSettings {
  _CommentsSetttings(
      {this.sort = CommentSort.top,
      @Setting() this.useSuggestedSort = true,
      @Setting() this.showNavigationBar = true,
      @Setting() this.showUserAvatar = true,
      @Setting() this.buttonsAlwaysVisible = true,
      @Setting() this.hideButtonAfterAction = true,
      @Setting() this.collapseAutoMod = true,
      @Setting() this.collapseDisruptiveComment = true,
      @Setting() this.showPostUpvotePercentage = true,
      @Setting() this.highlightMyUsername = true,
      @Setting() this.showFloatingButton = true,
      @Setting() this.showAwards = true,
      @Setting() this.clickableAwards = true,
      @Setting() this.showUserFlair = true,
      @Setting() this.showFlairColors = true,
      @Setting() this.showFlairEmojis = true,
      @Setting() this.clickToCollapse = true,
      @Setting() this.hideTextCollapsed = true,
      @Setting() this.loadCollapsed = true,
      @Setting() this.animateCollapse = true,
      @Setting() this.clickableUsername = true,
      @Setting() this.highlightNewComments = true,
      @Setting() this.volumeRockerNavigation = true,
      @Setting() this.animateNavigation = true,
      @Setting() this.showSaveButton = true,
      @Setting() this.swipeToClose = true})
      : super._();

  @override
  @JsonKey()
  final CommentSort sort;
  @override
  @JsonKey()
  @Setting()
  final bool useSuggestedSort;
  @override
  @JsonKey()
  @Setting()
  final bool showNavigationBar;
  @override
  @JsonKey()
  @Setting()
  final bool showUserAvatar;
//@Default() MediaPreviewSize postMediaPreviewSize,
  @override
  @JsonKey()
  @Setting()
  final bool buttonsAlwaysVisible;
  @override
  @JsonKey()
  @Setting()
  final bool hideButtonAfterAction;
  @override
  @JsonKey()
  @Setting()
  final bool collapseAutoMod;
  @override
  @JsonKey()
  @Setting()
  final bool collapseDisruptiveComment;
  @override
  @JsonKey()
  @Setting()
  final bool showPostUpvotePercentage;
//@Default() GuideStyle threadGuide,
  @override
  @JsonKey()
  @Setting()
  final bool highlightMyUsername;
  @override
  @JsonKey()
  @Setting()
  final bool showFloatingButton;
  @override
  @JsonKey()
  @Setting()
  final bool showAwards;
  @override
  @JsonKey()
  @Setting()
  final bool clickableAwards;
  @override
  @JsonKey()
  @Setting()
  final bool showUserFlair;
  @override
  @JsonKey()
  @Setting()
  final bool showFlairColors;
  @override
  @JsonKey()
  @Setting()
  final bool showFlairEmojis;
  @override
  @JsonKey()
  @Setting()
  final bool clickToCollapse;
  @override
  @JsonKey()
  @Setting()
  final bool hideTextCollapsed;
  @override
  @JsonKey()
  @Setting()
  final bool loadCollapsed;
  @override
  @JsonKey()
  @Setting()
  final bool animateCollapse;
  @override
  @JsonKey()
  @Setting()
  final bool clickableUsername;
  @override
  @JsonKey()
  @Setting()
  final bool highlightNewComments;
//@Default() NavigationMode defaultNavigationMode,
  @override
  @JsonKey()
  @Setting()
  final bool volumeRockerNavigation;
  @override
  @JsonKey()
  @Setting()
  final bool animateNavigation;
  @override
  @JsonKey()
  @Setting()
  final bool showSaveButton;
//@Default() String goToTopButton,
  @override
  @JsonKey()
  @Setting()
  final bool swipeToClose;

  /// Create a copy of CommentsSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$CommentsSetttingsCopyWith<_CommentsSetttings> get copyWith =>
      __$CommentsSetttingsCopyWithImpl<_CommentsSetttings>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _CommentsSetttings &&
            (identical(other.sort, sort) || other.sort == sort) &&
            (identical(other.useSuggestedSort, useSuggestedSort) ||
                other.useSuggestedSort == useSuggestedSort) &&
            (identical(other.showNavigationBar, showNavigationBar) ||
                other.showNavigationBar == showNavigationBar) &&
            (identical(other.showUserAvatar, showUserAvatar) ||
                other.showUserAvatar == showUserAvatar) &&
            (identical(other.buttonsAlwaysVisible, buttonsAlwaysVisible) ||
                other.buttonsAlwaysVisible == buttonsAlwaysVisible) &&
            (identical(other.hideButtonAfterAction, hideButtonAfterAction) ||
                other.hideButtonAfterAction == hideButtonAfterAction) &&
            (identical(other.collapseAutoMod, collapseAutoMod) ||
                other.collapseAutoMod == collapseAutoMod) &&
            (identical(other.collapseDisruptiveComment,
                    collapseDisruptiveComment) ||
                other.collapseDisruptiveComment == collapseDisruptiveComment) &&
            (identical(other.showPostUpvotePercentage, showPostUpvotePercentage) ||
                other.showPostUpvotePercentage == showPostUpvotePercentage) &&
            (identical(other.highlightMyUsername, highlightMyUsername) ||
                other.highlightMyUsername == highlightMyUsername) &&
            (identical(other.showFloatingButton, showFloatingButton) ||
                other.showFloatingButton == showFloatingButton) &&
            (identical(other.showAwards, showAwards) ||
                other.showAwards == showAwards) &&
            (identical(other.clickableAwards, clickableAwards) ||
                other.clickableAwards == clickableAwards) &&
            (identical(other.showUserFlair, showUserFlair) ||
                other.showUserFlair == showUserFlair) &&
            (identical(other.showFlairColors, showFlairColors) ||
                other.showFlairColors == showFlairColors) &&
            (identical(other.showFlairEmojis, showFlairEmojis) ||
                other.showFlairEmojis == showFlairEmojis) &&
            (identical(other.clickToCollapse, clickToCollapse) ||
                other.clickToCollapse == clickToCollapse) &&
            (identical(other.hideTextCollapsed, hideTextCollapsed) ||
                other.hideTextCollapsed == hideTextCollapsed) &&
            (identical(other.loadCollapsed, loadCollapsed) ||
                other.loadCollapsed == loadCollapsed) &&
            (identical(other.animateCollapse, animateCollapse) ||
                other.animateCollapse == animateCollapse) &&
            (identical(other.clickableUsername, clickableUsername) ||
                other.clickableUsername == clickableUsername) &&
            (identical(other.highlightNewComments, highlightNewComments) ||
                other.highlightNewComments == highlightNewComments) &&
            (identical(other.volumeRockerNavigation, volumeRockerNavigation) ||
                other.volumeRockerNavigation == volumeRockerNavigation) &&
            (identical(other.animateNavigation, animateNavigation) ||
                other.animateNavigation == animateNavigation) &&
            (identical(other.showSaveButton, showSaveButton) ||
                other.showSaveButton == showSaveButton) &&
            (identical(other.swipeToClose, swipeToClose) ||
                other.swipeToClose == swipeToClose));
  }

  @override
  int get hashCode => Object.hashAll([
        runtimeType,
        sort,
        useSuggestedSort,
        showNavigationBar,
        showUserAvatar,
        buttonsAlwaysVisible,
        hideButtonAfterAction,
        collapseAutoMod,
        collapseDisruptiveComment,
        showPostUpvotePercentage,
        highlightMyUsername,
        showFloatingButton,
        showAwards,
        clickableAwards,
        showUserFlair,
        showFlairColors,
        showFlairEmojis,
        clickToCollapse,
        hideTextCollapsed,
        loadCollapsed,
        animateCollapse,
        clickableUsername,
        highlightNewComments,
        volumeRockerNavigation,
        animateNavigation,
        showSaveButton,
        swipeToClose
      ]);

  @override
  String toString() {
    return 'CommentsSettings(sort: $sort, useSuggestedSort: $useSuggestedSort, showNavigationBar: $showNavigationBar, showUserAvatar: $showUserAvatar, buttonsAlwaysVisible: $buttonsAlwaysVisible, hideButtonAfterAction: $hideButtonAfterAction, collapseAutoMod: $collapseAutoMod, collapseDisruptiveComment: $collapseDisruptiveComment, showPostUpvotePercentage: $showPostUpvotePercentage, highlightMyUsername: $highlightMyUsername, showFloatingButton: $showFloatingButton, showAwards: $showAwards, clickableAwards: $clickableAwards, showUserFlair: $showUserFlair, showFlairColors: $showFlairColors, showFlairEmojis: $showFlairEmojis, clickToCollapse: $clickToCollapse, hideTextCollapsed: $hideTextCollapsed, loadCollapsed: $loadCollapsed, animateCollapse: $animateCollapse, clickableUsername: $clickableUsername, highlightNewComments: $highlightNewComments, volumeRockerNavigation: $volumeRockerNavigation, animateNavigation: $animateNavigation, showSaveButton: $showSaveButton, swipeToClose: $swipeToClose)';
  }
}

/// @nodoc
abstract mixin class _$CommentsSetttingsCopyWith<$Res>
    implements $CommentsSettingsCopyWith<$Res> {
  factory _$CommentsSetttingsCopyWith(
          _CommentsSetttings value, $Res Function(_CommentsSetttings) _then) =
      __$CommentsSetttingsCopyWithImpl;
  @override
  @useResult
  $Res call(
      {CommentSort sort,
      @Setting() bool useSuggestedSort,
      @Setting() bool showNavigationBar,
      @Setting() bool showUserAvatar,
      @Setting() bool buttonsAlwaysVisible,
      @Setting() bool hideButtonAfterAction,
      @Setting() bool collapseAutoMod,
      @Setting() bool collapseDisruptiveComment,
      @Setting() bool showPostUpvotePercentage,
      @Setting() bool highlightMyUsername,
      @Setting() bool showFloatingButton,
      @Setting() bool showAwards,
      @Setting() bool clickableAwards,
      @Setting() bool showUserFlair,
      @Setting() bool showFlairColors,
      @Setting() bool showFlairEmojis,
      @Setting() bool clickToCollapse,
      @Setting() bool hideTextCollapsed,
      @Setting() bool loadCollapsed,
      @Setting() bool animateCollapse,
      @Setting() bool clickableUsername,
      @Setting() bool highlightNewComments,
      @Setting() bool volumeRockerNavigation,
      @Setting() bool animateNavigation,
      @Setting() bool showSaveButton,
      @Setting() bool swipeToClose});
}

/// @nodoc
class __$CommentsSetttingsCopyWithImpl<$Res>
    implements _$CommentsSetttingsCopyWith<$Res> {
  __$CommentsSetttingsCopyWithImpl(this._self, this._then);

  final _CommentsSetttings _self;
  final $Res Function(_CommentsSetttings) _then;

  /// Create a copy of CommentsSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? sort = null,
    Object? useSuggestedSort = null,
    Object? showNavigationBar = null,
    Object? showUserAvatar = null,
    Object? buttonsAlwaysVisible = null,
    Object? hideButtonAfterAction = null,
    Object? collapseAutoMod = null,
    Object? collapseDisruptiveComment = null,
    Object? showPostUpvotePercentage = null,
    Object? highlightMyUsername = null,
    Object? showFloatingButton = null,
    Object? showAwards = null,
    Object? clickableAwards = null,
    Object? showUserFlair = null,
    Object? showFlairColors = null,
    Object? showFlairEmojis = null,
    Object? clickToCollapse = null,
    Object? hideTextCollapsed = null,
    Object? loadCollapsed = null,
    Object? animateCollapse = null,
    Object? clickableUsername = null,
    Object? highlightNewComments = null,
    Object? volumeRockerNavigation = null,
    Object? animateNavigation = null,
    Object? showSaveButton = null,
    Object? swipeToClose = null,
  }) {
    return _then(_CommentsSetttings(
      sort: null == sort
          ? _self.sort
          : sort // ignore: cast_nullable_to_non_nullable
              as CommentSort,
      useSuggestedSort: null == useSuggestedSort
          ? _self.useSuggestedSort
          : useSuggestedSort // ignore: cast_nullable_to_non_nullable
              as bool,
      showNavigationBar: null == showNavigationBar
          ? _self.showNavigationBar
          : showNavigationBar // ignore: cast_nullable_to_non_nullable
              as bool,
      showUserAvatar: null == showUserAvatar
          ? _self.showUserAvatar
          : showUserAvatar // ignore: cast_nullable_to_non_nullable
              as bool,
      buttonsAlwaysVisible: null == buttonsAlwaysVisible
          ? _self.buttonsAlwaysVisible
          : buttonsAlwaysVisible // ignore: cast_nullable_to_non_nullable
              as bool,
      hideButtonAfterAction: null == hideButtonAfterAction
          ? _self.hideButtonAfterAction
          : hideButtonAfterAction // ignore: cast_nullable_to_non_nullable
              as bool,
      collapseAutoMod: null == collapseAutoMod
          ? _self.collapseAutoMod
          : collapseAutoMod // ignore: cast_nullable_to_non_nullable
              as bool,
      collapseDisruptiveComment: null == collapseDisruptiveComment
          ? _self.collapseDisruptiveComment
          : collapseDisruptiveComment // ignore: cast_nullable_to_non_nullable
              as bool,
      showPostUpvotePercentage: null == showPostUpvotePercentage
          ? _self.showPostUpvotePercentage
          : showPostUpvotePercentage // ignore: cast_nullable_to_non_nullable
              as bool,
      highlightMyUsername: null == highlightMyUsername
          ? _self.highlightMyUsername
          : highlightMyUsername // ignore: cast_nullable_to_non_nullable
              as bool,
      showFloatingButton: null == showFloatingButton
          ? _self.showFloatingButton
          : showFloatingButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showAwards: null == showAwards
          ? _self.showAwards
          : showAwards // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableAwards: null == clickableAwards
          ? _self.clickableAwards
          : clickableAwards // ignore: cast_nullable_to_non_nullable
              as bool,
      showUserFlair: null == showUserFlair
          ? _self.showUserFlair
          : showUserFlair // ignore: cast_nullable_to_non_nullable
              as bool,
      showFlairColors: null == showFlairColors
          ? _self.showFlairColors
          : showFlairColors // ignore: cast_nullable_to_non_nullable
              as bool,
      showFlairEmojis: null == showFlairEmojis
          ? _self.showFlairEmojis
          : showFlairEmojis // ignore: cast_nullable_to_non_nullable
              as bool,
      clickToCollapse: null == clickToCollapse
          ? _self.clickToCollapse
          : clickToCollapse // ignore: cast_nullable_to_non_nullable
              as bool,
      hideTextCollapsed: null == hideTextCollapsed
          ? _self.hideTextCollapsed
          : hideTextCollapsed // ignore: cast_nullable_to_non_nullable
              as bool,
      loadCollapsed: null == loadCollapsed
          ? _self.loadCollapsed
          : loadCollapsed // ignore: cast_nullable_to_non_nullable
              as bool,
      animateCollapse: null == animateCollapse
          ? _self.animateCollapse
          : animateCollapse // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableUsername: null == clickableUsername
          ? _self.clickableUsername
          : clickableUsername // ignore: cast_nullable_to_non_nullable
              as bool,
      highlightNewComments: null == highlightNewComments
          ? _self.highlightNewComments
          : highlightNewComments // ignore: cast_nullable_to_non_nullable
              as bool,
      volumeRockerNavigation: null == volumeRockerNavigation
          ? _self.volumeRockerNavigation
          : volumeRockerNavigation // ignore: cast_nullable_to_non_nullable
              as bool,
      animateNavigation: null == animateNavigation
          ? _self.animateNavigation
          : animateNavigation // ignore: cast_nullable_to_non_nullable
              as bool,
      showSaveButton: null == showSaveButton
          ? _self.showSaveButton
          : showSaveButton // ignore: cast_nullable_to_non_nullable
              as bool,
      swipeToClose: null == swipeToClose
          ? _self.swipeToClose
          : swipeToClose // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

// dart format on
