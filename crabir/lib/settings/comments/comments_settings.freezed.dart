// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'comments_settings.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;
CommentsSettings _$CommentsSettingsFromJson(Map<String, dynamic> json) {
  return _CommentsSetttings.fromJson(json);
}

/// @nodoc
mixin _$CommentsSettings {
  @Setting(widget: _CommentsSortSelection)
  CommentSort get defaultSort;
  @Setting()
  bool get useSuggestedSort;
  @Category(name: "Appearance")
  @Setting()
  bool get showNavigationBar; // TODO: (requires an additional api call)
  @Setting()
  bool get showUserAvatar;
  @Setting()
  bool get showCommentsImage;
  @Setting(widget: MediaPreviewSizeSelect)
  MediaPreviewSize get postMediaPreviewSize;
  @Setting()
  bool get buttonsAlwaysVisible;
  @Setting()
  bool get hideButtonAfterAction; // TODO:
  @Setting()
  bool get collapseAutoMod; // TODO:
  @Setting()
  bool get collapseDisruptiveComment; // TODO:
  @Setting()
  bool get showPostUpvotePercentage; //@Default() GuideStyle threadGuide,
  @Setting()
  bool get highlightMyUsername; // TODO:
  @Setting()
  bool get showFloatingButton; // TODO:
  @Category(name: "Awards")
  @Setting()
  bool get showAwards; // TODO:
  @Setting()
  bool get clickableAwards;
  @Category(name: "Flairs")
  @Setting()
  bool get showUserFlair; // TODO:
  @Setting()
  bool get showFlairColors; // TODO:
  @Setting()
  bool get showFlairEmojis; // TODO:
  @Category(name: "Behavior")
  @Setting()
  bool get clickToCollapse; // TODO:
  @Setting()
  bool get hideTextCollapsed; // TODO:
  @Setting()
  bool get loadCollapsed; // TODO:
  @Setting()
  bool get animateCollapse;
  @Setting()
  bool get clickableUsername;
  @Category(name: "Navigation")
  @Setting()
  bool get highlightNewComments; // TODO:
//@Default() NavigationMode defaultNavigationMode,
// TODO:
  @Setting()
  bool get volumeRockerNavigation; // TODO:
  @Setting()
  bool get animateNavigation;
  @Category(name: "Visible buttons")
  @Setting()
  bool get showSaveButton; // TODO:
//@Default() GoToTopButtonAction goToTopButton,
  @Category(name: "Gestures")
  @Setting()
  bool get swipeToClose;

  /// Create a copy of CommentsSettings
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $CommentsSettingsCopyWith<CommentsSettings> get copyWith =>
      _$CommentsSettingsCopyWithImpl<CommentsSettings>(
          this as CommentsSettings, _$identity);

  /// Serializes this CommentsSettings to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is CommentsSettings &&
            (identical(other.defaultSort, defaultSort) ||
                other.defaultSort == defaultSort) &&
            (identical(other.useSuggestedSort, useSuggestedSort) ||
                other.useSuggestedSort == useSuggestedSort) &&
            (identical(other.showNavigationBar, showNavigationBar) ||
                other.showNavigationBar == showNavigationBar) &&
            (identical(other.showUserAvatar, showUserAvatar) ||
                other.showUserAvatar == showUserAvatar) &&
            (identical(other.showCommentsImage, showCommentsImage) ||
                other.showCommentsImage == showCommentsImage) &&
            (identical(other.postMediaPreviewSize, postMediaPreviewSize) ||
                other.postMediaPreviewSize == postMediaPreviewSize) &&
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

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hashAll([
        runtimeType,
        defaultSort,
        useSuggestedSort,
        showNavigationBar,
        showUserAvatar,
        showCommentsImage,
        postMediaPreviewSize,
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
    return 'CommentsSettings(defaultSort: $defaultSort, useSuggestedSort: $useSuggestedSort, showNavigationBar: $showNavigationBar, showUserAvatar: $showUserAvatar, showCommentsImage: $showCommentsImage, postMediaPreviewSize: $postMediaPreviewSize, buttonsAlwaysVisible: $buttonsAlwaysVisible, hideButtonAfterAction: $hideButtonAfterAction, collapseAutoMod: $collapseAutoMod, collapseDisruptiveComment: $collapseDisruptiveComment, showPostUpvotePercentage: $showPostUpvotePercentage, highlightMyUsername: $highlightMyUsername, showFloatingButton: $showFloatingButton, showAwards: $showAwards, clickableAwards: $clickableAwards, showUserFlair: $showUserFlair, showFlairColors: $showFlairColors, showFlairEmojis: $showFlairEmojis, clickToCollapse: $clickToCollapse, hideTextCollapsed: $hideTextCollapsed, loadCollapsed: $loadCollapsed, animateCollapse: $animateCollapse, clickableUsername: $clickableUsername, highlightNewComments: $highlightNewComments, volumeRockerNavigation: $volumeRockerNavigation, animateNavigation: $animateNavigation, showSaveButton: $showSaveButton, swipeToClose: $swipeToClose)';
  }
}

/// @nodoc
abstract mixin class $CommentsSettingsCopyWith<$Res> {
  factory $CommentsSettingsCopyWith(
          CommentsSettings value, $Res Function(CommentsSettings) _then) =
      _$CommentsSettingsCopyWithImpl;
  @useResult
  $Res call(
      {@Setting(widget: _CommentsSortSelection) CommentSort defaultSort,
      @Setting() bool useSuggestedSort,
      @Category(name: "Appearance") @Setting() bool showNavigationBar,
      @Setting() bool showUserAvatar,
      @Setting() bool showCommentsImage,
      @Setting(widget: MediaPreviewSizeSelect)
      MediaPreviewSize postMediaPreviewSize,
      @Setting() bool buttonsAlwaysVisible,
      @Setting() bool hideButtonAfterAction,
      @Setting() bool collapseAutoMod,
      @Setting() bool collapseDisruptiveComment,
      @Setting() bool showPostUpvotePercentage,
      @Setting() bool highlightMyUsername,
      @Setting() bool showFloatingButton,
      @Category(name: "Awards") @Setting() bool showAwards,
      @Setting() bool clickableAwards,
      @Category(name: "Flairs") @Setting() bool showUserFlair,
      @Setting() bool showFlairColors,
      @Setting() bool showFlairEmojis,
      @Category(name: "Behavior") @Setting() bool clickToCollapse,
      @Setting() bool hideTextCollapsed,
      @Setting() bool loadCollapsed,
      @Setting() bool animateCollapse,
      @Setting() bool clickableUsername,
      @Category(name: "Navigation") @Setting() bool highlightNewComments,
      @Setting() bool volumeRockerNavigation,
      @Setting() bool animateNavigation,
      @Category(name: "Visible buttons") @Setting() bool showSaveButton,
      @Category(name: "Gestures") @Setting() bool swipeToClose});
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
    Object? defaultSort = null,
    Object? useSuggestedSort = null,
    Object? showNavigationBar = null,
    Object? showUserAvatar = null,
    Object? showCommentsImage = null,
    Object? postMediaPreviewSize = null,
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
      defaultSort: null == defaultSort
          ? _self.defaultSort
          : defaultSort // ignore: cast_nullable_to_non_nullable
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
      showCommentsImage: null == showCommentsImage
          ? _self.showCommentsImage
          : showCommentsImage // ignore: cast_nullable_to_non_nullable
              as bool,
      postMediaPreviewSize: null == postMediaPreviewSize
          ? _self.postMediaPreviewSize
          : postMediaPreviewSize // ignore: cast_nullable_to_non_nullable
              as MediaPreviewSize,
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

/// Adds pattern-matching-related methods to [CommentsSettings].
extension CommentsSettingsPatterns on CommentsSettings {
  /// A variant of `map` that fallback to returning `orElse`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_CommentsSetttings value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _CommentsSetttings() when $default != null:
        return $default(_that);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// Callbacks receives the raw object, upcasted.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case final Subclass2 value:
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_CommentsSetttings value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _CommentsSetttings():
        return $default(_that);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `map` that fallback to returning `null`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_CommentsSetttings value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _CommentsSetttings() when $default != null:
        return $default(_that);
      case _:
        return null;
    }
  }

  /// A variant of `when` that fallback to an `orElse` callback.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(
            @Setting(widget: _CommentsSortSelection) CommentSort defaultSort,
            @Setting() bool useSuggestedSort,
            @Category(name: "Appearance") @Setting() bool showNavigationBar,
            @Setting() bool showUserAvatar,
            @Setting() bool showCommentsImage,
            @Setting(widget: MediaPreviewSizeSelect)
            MediaPreviewSize postMediaPreviewSize,
            @Setting() bool buttonsAlwaysVisible,
            @Setting() bool hideButtonAfterAction,
            @Setting() bool collapseAutoMod,
            @Setting() bool collapseDisruptiveComment,
            @Setting() bool showPostUpvotePercentage,
            @Setting() bool highlightMyUsername,
            @Setting() bool showFloatingButton,
            @Category(name: "Awards") @Setting() bool showAwards,
            @Setting() bool clickableAwards,
            @Category(name: "Flairs") @Setting() bool showUserFlair,
            @Setting() bool showFlairColors,
            @Setting() bool showFlairEmojis,
            @Category(name: "Behavior") @Setting() bool clickToCollapse,
            @Setting() bool hideTextCollapsed,
            @Setting() bool loadCollapsed,
            @Setting() bool animateCollapse,
            @Setting() bool clickableUsername,
            @Category(name: "Navigation") @Setting() bool highlightNewComments,
            @Setting() bool volumeRockerNavigation,
            @Setting() bool animateNavigation,
            @Category(name: "Visible buttons") @Setting() bool showSaveButton,
            @Category(name: "Gestures") @Setting() bool swipeToClose)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _CommentsSetttings() when $default != null:
        return $default(
            _that.defaultSort,
            _that.useSuggestedSort,
            _that.showNavigationBar,
            _that.showUserAvatar,
            _that.showCommentsImage,
            _that.postMediaPreviewSize,
            _that.buttonsAlwaysVisible,
            _that.hideButtonAfterAction,
            _that.collapseAutoMod,
            _that.collapseDisruptiveComment,
            _that.showPostUpvotePercentage,
            _that.highlightMyUsername,
            _that.showFloatingButton,
            _that.showAwards,
            _that.clickableAwards,
            _that.showUserFlair,
            _that.showFlairColors,
            _that.showFlairEmojis,
            _that.clickToCollapse,
            _that.hideTextCollapsed,
            _that.loadCollapsed,
            _that.animateCollapse,
            _that.clickableUsername,
            _that.highlightNewComments,
            _that.volumeRockerNavigation,
            _that.animateNavigation,
            _that.showSaveButton,
            _that.swipeToClose);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// As opposed to `map`, this offers destructuring.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case Subclass2(:final field2):
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(
            @Setting(widget: _CommentsSortSelection) CommentSort defaultSort,
            @Setting() bool useSuggestedSort,
            @Category(name: "Appearance") @Setting() bool showNavigationBar,
            @Setting() bool showUserAvatar,
            @Setting() bool showCommentsImage,
            @Setting(widget: MediaPreviewSizeSelect)
            MediaPreviewSize postMediaPreviewSize,
            @Setting() bool buttonsAlwaysVisible,
            @Setting() bool hideButtonAfterAction,
            @Setting() bool collapseAutoMod,
            @Setting() bool collapseDisruptiveComment,
            @Setting() bool showPostUpvotePercentage,
            @Setting() bool highlightMyUsername,
            @Setting() bool showFloatingButton,
            @Category(name: "Awards") @Setting() bool showAwards,
            @Setting() bool clickableAwards,
            @Category(name: "Flairs") @Setting() bool showUserFlair,
            @Setting() bool showFlairColors,
            @Setting() bool showFlairEmojis,
            @Category(name: "Behavior") @Setting() bool clickToCollapse,
            @Setting() bool hideTextCollapsed,
            @Setting() bool loadCollapsed,
            @Setting() bool animateCollapse,
            @Setting() bool clickableUsername,
            @Category(name: "Navigation") @Setting() bool highlightNewComments,
            @Setting() bool volumeRockerNavigation,
            @Setting() bool animateNavigation,
            @Category(name: "Visible buttons") @Setting() bool showSaveButton,
            @Category(name: "Gestures") @Setting() bool swipeToClose)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _CommentsSetttings():
        return $default(
            _that.defaultSort,
            _that.useSuggestedSort,
            _that.showNavigationBar,
            _that.showUserAvatar,
            _that.showCommentsImage,
            _that.postMediaPreviewSize,
            _that.buttonsAlwaysVisible,
            _that.hideButtonAfterAction,
            _that.collapseAutoMod,
            _that.collapseDisruptiveComment,
            _that.showPostUpvotePercentage,
            _that.highlightMyUsername,
            _that.showFloatingButton,
            _that.showAwards,
            _that.clickableAwards,
            _that.showUserFlair,
            _that.showFlairColors,
            _that.showFlairEmojis,
            _that.clickToCollapse,
            _that.hideTextCollapsed,
            _that.loadCollapsed,
            _that.animateCollapse,
            _that.clickableUsername,
            _that.highlightNewComments,
            _that.volumeRockerNavigation,
            _that.animateNavigation,
            _that.showSaveButton,
            _that.swipeToClose);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `when` that fallback to returning `null`
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(
            @Setting(widget: _CommentsSortSelection) CommentSort defaultSort,
            @Setting() bool useSuggestedSort,
            @Category(name: "Appearance") @Setting() bool showNavigationBar,
            @Setting() bool showUserAvatar,
            @Setting() bool showCommentsImage,
            @Setting(widget: MediaPreviewSizeSelect)
            MediaPreviewSize postMediaPreviewSize,
            @Setting() bool buttonsAlwaysVisible,
            @Setting() bool hideButtonAfterAction,
            @Setting() bool collapseAutoMod,
            @Setting() bool collapseDisruptiveComment,
            @Setting() bool showPostUpvotePercentage,
            @Setting() bool highlightMyUsername,
            @Setting() bool showFloatingButton,
            @Category(name: "Awards") @Setting() bool showAwards,
            @Setting() bool clickableAwards,
            @Category(name: "Flairs") @Setting() bool showUserFlair,
            @Setting() bool showFlairColors,
            @Setting() bool showFlairEmojis,
            @Category(name: "Behavior") @Setting() bool clickToCollapse,
            @Setting() bool hideTextCollapsed,
            @Setting() bool loadCollapsed,
            @Setting() bool animateCollapse,
            @Setting() bool clickableUsername,
            @Category(name: "Navigation") @Setting() bool highlightNewComments,
            @Setting() bool volumeRockerNavigation,
            @Setting() bool animateNavigation,
            @Category(name: "Visible buttons") @Setting() bool showSaveButton,
            @Category(name: "Gestures") @Setting() bool swipeToClose)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _CommentsSetttings() when $default != null:
        return $default(
            _that.defaultSort,
            _that.useSuggestedSort,
            _that.showNavigationBar,
            _that.showUserAvatar,
            _that.showCommentsImage,
            _that.postMediaPreviewSize,
            _that.buttonsAlwaysVisible,
            _that.hideButtonAfterAction,
            _that.collapseAutoMod,
            _that.collapseDisruptiveComment,
            _that.showPostUpvotePercentage,
            _that.highlightMyUsername,
            _that.showFloatingButton,
            _that.showAwards,
            _that.clickableAwards,
            _that.showUserFlair,
            _that.showFlairColors,
            _that.showFlairEmojis,
            _that.clickToCollapse,
            _that.hideTextCollapsed,
            _that.loadCollapsed,
            _that.animateCollapse,
            _that.clickableUsername,
            _that.highlightNewComments,
            _that.volumeRockerNavigation,
            _that.animateNavigation,
            _that.showSaveButton,
            _that.swipeToClose);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _CommentsSetttings extends CommentsSettings {
  _CommentsSetttings(
      {@Setting(widget: _CommentsSortSelection)
      this.defaultSort = CommentSort.top,
      @Setting() this.useSuggestedSort = true,
      @Category(name: "Appearance") @Setting() this.showNavigationBar = true,
      @Setting() this.showUserAvatar = true,
      @Setting() this.showCommentsImage = true,
      @Setting(widget: MediaPreviewSizeSelect)
      this.postMediaPreviewSize = MediaPreviewSize.thumbnail,
      @Setting() this.buttonsAlwaysVisible = false,
      @Setting() this.hideButtonAfterAction = true,
      @Setting() this.collapseAutoMod = true,
      @Setting() this.collapseDisruptiveComment = true,
      @Setting() this.showPostUpvotePercentage = true,
      @Setting() this.highlightMyUsername = true,
      @Setting() this.showFloatingButton = true,
      @Category(name: "Awards") @Setting() this.showAwards = true,
      @Setting() this.clickableAwards = true,
      @Category(name: "Flairs") @Setting() this.showUserFlair = true,
      @Setting() this.showFlairColors = true,
      @Setting() this.showFlairEmojis = true,
      @Category(name: "Behavior") @Setting() this.clickToCollapse = true,
      @Setting() this.hideTextCollapsed = true,
      @Setting() this.loadCollapsed = true,
      @Setting() this.animateCollapse = true,
      @Setting() this.clickableUsername = true,
      @Category(name: "Navigation") @Setting() this.highlightNewComments = true,
      @Setting() this.volumeRockerNavigation = true,
      @Setting() this.animateNavigation = true,
      @Category(name: "Visible buttons") @Setting() this.showSaveButton = true,
      @Category(name: "Gestures") @Setting() this.swipeToClose = true})
      : super._();
  factory _CommentsSetttings.fromJson(Map<String, dynamic> json) =>
      _$CommentsSetttingsFromJson(json);

  @override
  @JsonKey()
  @Setting(widget: _CommentsSortSelection)
  final CommentSort defaultSort;
  @override
  @JsonKey()
  @Setting()
  final bool useSuggestedSort;
  @override
  @JsonKey()
  @Category(name: "Appearance")
  @Setting()
  final bool showNavigationBar;
// TODO: (requires an additional api call)
  @override
  @JsonKey()
  @Setting()
  final bool showUserAvatar;
  @override
  @JsonKey()
  @Setting()
  final bool showCommentsImage;
  @override
  @JsonKey()
  @Setting(widget: MediaPreviewSizeSelect)
  final MediaPreviewSize postMediaPreviewSize;
  @override
  @JsonKey()
  @Setting()
  final bool buttonsAlwaysVisible;
  @override
  @JsonKey()
  @Setting()
  final bool hideButtonAfterAction;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool collapseAutoMod;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool collapseDisruptiveComment;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool showPostUpvotePercentage;
//@Default() GuideStyle threadGuide,
  @override
  @JsonKey()
  @Setting()
  final bool highlightMyUsername;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool showFloatingButton;
// TODO:
  @override
  @JsonKey()
  @Category(name: "Awards")
  @Setting()
  final bool showAwards;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool clickableAwards;
  @override
  @JsonKey()
  @Category(name: "Flairs")
  @Setting()
  final bool showUserFlair;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool showFlairColors;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool showFlairEmojis;
// TODO:
  @override
  @JsonKey()
  @Category(name: "Behavior")
  @Setting()
  final bool clickToCollapse;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool hideTextCollapsed;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool loadCollapsed;
// TODO:
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
  @Category(name: "Navigation")
  @Setting()
  final bool highlightNewComments;
// TODO:
//@Default() NavigationMode defaultNavigationMode,
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool volumeRockerNavigation;
// TODO:
  @override
  @JsonKey()
  @Setting()
  final bool animateNavigation;
  @override
  @JsonKey()
  @Category(name: "Visible buttons")
  @Setting()
  final bool showSaveButton;
// TODO:
//@Default() GoToTopButtonAction goToTopButton,
  @override
  @JsonKey()
  @Category(name: "Gestures")
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
  Map<String, dynamic> toJson() {
    return _$CommentsSetttingsToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _CommentsSetttings &&
            (identical(other.defaultSort, defaultSort) ||
                other.defaultSort == defaultSort) &&
            (identical(other.useSuggestedSort, useSuggestedSort) ||
                other.useSuggestedSort == useSuggestedSort) &&
            (identical(other.showNavigationBar, showNavigationBar) ||
                other.showNavigationBar == showNavigationBar) &&
            (identical(other.showUserAvatar, showUserAvatar) ||
                other.showUserAvatar == showUserAvatar) &&
            (identical(other.showCommentsImage, showCommentsImage) ||
                other.showCommentsImage == showCommentsImage) &&
            (identical(other.postMediaPreviewSize, postMediaPreviewSize) ||
                other.postMediaPreviewSize == postMediaPreviewSize) &&
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

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hashAll([
        runtimeType,
        defaultSort,
        useSuggestedSort,
        showNavigationBar,
        showUserAvatar,
        showCommentsImage,
        postMediaPreviewSize,
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
    return 'CommentsSettings(defaultSort: $defaultSort, useSuggestedSort: $useSuggestedSort, showNavigationBar: $showNavigationBar, showUserAvatar: $showUserAvatar, showCommentsImage: $showCommentsImage, postMediaPreviewSize: $postMediaPreviewSize, buttonsAlwaysVisible: $buttonsAlwaysVisible, hideButtonAfterAction: $hideButtonAfterAction, collapseAutoMod: $collapseAutoMod, collapseDisruptiveComment: $collapseDisruptiveComment, showPostUpvotePercentage: $showPostUpvotePercentage, highlightMyUsername: $highlightMyUsername, showFloatingButton: $showFloatingButton, showAwards: $showAwards, clickableAwards: $clickableAwards, showUserFlair: $showUserFlair, showFlairColors: $showFlairColors, showFlairEmojis: $showFlairEmojis, clickToCollapse: $clickToCollapse, hideTextCollapsed: $hideTextCollapsed, loadCollapsed: $loadCollapsed, animateCollapse: $animateCollapse, clickableUsername: $clickableUsername, highlightNewComments: $highlightNewComments, volumeRockerNavigation: $volumeRockerNavigation, animateNavigation: $animateNavigation, showSaveButton: $showSaveButton, swipeToClose: $swipeToClose)';
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
      {@Setting(widget: _CommentsSortSelection) CommentSort defaultSort,
      @Setting() bool useSuggestedSort,
      @Category(name: "Appearance") @Setting() bool showNavigationBar,
      @Setting() bool showUserAvatar,
      @Setting() bool showCommentsImage,
      @Setting(widget: MediaPreviewSizeSelect)
      MediaPreviewSize postMediaPreviewSize,
      @Setting() bool buttonsAlwaysVisible,
      @Setting() bool hideButtonAfterAction,
      @Setting() bool collapseAutoMod,
      @Setting() bool collapseDisruptiveComment,
      @Setting() bool showPostUpvotePercentage,
      @Setting() bool highlightMyUsername,
      @Setting() bool showFloatingButton,
      @Category(name: "Awards") @Setting() bool showAwards,
      @Setting() bool clickableAwards,
      @Category(name: "Flairs") @Setting() bool showUserFlair,
      @Setting() bool showFlairColors,
      @Setting() bool showFlairEmojis,
      @Category(name: "Behavior") @Setting() bool clickToCollapse,
      @Setting() bool hideTextCollapsed,
      @Setting() bool loadCollapsed,
      @Setting() bool animateCollapse,
      @Setting() bool clickableUsername,
      @Category(name: "Navigation") @Setting() bool highlightNewComments,
      @Setting() bool volumeRockerNavigation,
      @Setting() bool animateNavigation,
      @Category(name: "Visible buttons") @Setting() bool showSaveButton,
      @Category(name: "Gestures") @Setting() bool swipeToClose});
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
    Object? defaultSort = null,
    Object? useSuggestedSort = null,
    Object? showNavigationBar = null,
    Object? showUserAvatar = null,
    Object? showCommentsImage = null,
    Object? postMediaPreviewSize = null,
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
      defaultSort: null == defaultSort
          ? _self.defaultSort
          : defaultSort // ignore: cast_nullable_to_non_nullable
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
      showCommentsImage: null == showCommentsImage
          ? _self.showCommentsImage
          : showCommentsImage // ignore: cast_nullable_to_non_nullable
              as bool,
      postMediaPreviewSize: null == postMediaPreviewSize
          ? _self.postMediaPreviewSize
          : postMediaPreviewSize // ignore: cast_nullable_to_non_nullable
              as MediaPreviewSize,
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
