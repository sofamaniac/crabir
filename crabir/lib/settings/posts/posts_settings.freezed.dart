// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'posts_settings.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$PostsSettings {
  @Setting(widget: _SortSelection)
  FeedSort get defaultHomeSort;
  @Setting(widget: _SortSelection)
  FeedSort get defaultSort;
  @Setting(hasDescription: true)
  bool get rememberSortByCommunity; // TODO: manage saved sort
  @Category(name: "Awards")
  @Setting()
  bool get showAwards;
  @Setting()
  bool get clickableAwards;
  @Category(name: "Flairs")
  @Setting()
  bool get showPostFlair;
  @Setting()
  bool get showFlairColors;
  @Setting()
  bool get showEmojis;
  @Setting()
  bool get tapFlairToSearch;
  @Category(name: "Post info")
  @Setting()
  bool get showAuthor;
  @Setting()
  bool get clickableCommunity;
  @Setting()
  bool get clickableUser;
  @Category(name: "Visible buttons")
  @Setting()
  bool get showFloatingButton;
  @Setting()
  bool get showHideButton;
  @Setting()
  bool get showMarkAsReadButton;
  @Setting()
  bool get showShareButton;
  @Setting()
  bool get showCommentsButton;
  @Setting()
  bool get showOpenInAppButton;

  /// Create a copy of PostsSettings
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $PostsSettingsCopyWith<PostsSettings> get copyWith =>
      _$PostsSettingsCopyWithImpl<PostsSettings>(
          this as PostsSettings, _$identity);

  /// Serializes this PostsSettings to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is PostsSettings &&
            (identical(other.defaultHomeSort, defaultHomeSort) ||
                other.defaultHomeSort == defaultHomeSort) &&
            (identical(other.defaultSort, defaultSort) ||
                other.defaultSort == defaultSort) &&
            (identical(
                    other.rememberSortByCommunity, rememberSortByCommunity) ||
                other.rememberSortByCommunity == rememberSortByCommunity) &&
            (identical(other.showAwards, showAwards) ||
                other.showAwards == showAwards) &&
            (identical(other.clickableAwards, clickableAwards) ||
                other.clickableAwards == clickableAwards) &&
            (identical(other.showPostFlair, showPostFlair) ||
                other.showPostFlair == showPostFlair) &&
            (identical(other.showFlairColors, showFlairColors) ||
                other.showFlairColors == showFlairColors) &&
            (identical(other.showEmojis, showEmojis) ||
                other.showEmojis == showEmojis) &&
            (identical(other.tapFlairToSearch, tapFlairToSearch) ||
                other.tapFlairToSearch == tapFlairToSearch) &&
            (identical(other.showAuthor, showAuthor) ||
                other.showAuthor == showAuthor) &&
            (identical(other.clickableCommunity, clickableCommunity) ||
                other.clickableCommunity == clickableCommunity) &&
            (identical(other.clickableUser, clickableUser) ||
                other.clickableUser == clickableUser) &&
            (identical(other.showFloatingButton, showFloatingButton) ||
                other.showFloatingButton == showFloatingButton) &&
            (identical(other.showHideButton, showHideButton) ||
                other.showHideButton == showHideButton) &&
            (identical(other.showMarkAsReadButton, showMarkAsReadButton) ||
                other.showMarkAsReadButton == showMarkAsReadButton) &&
            (identical(other.showShareButton, showShareButton) ||
                other.showShareButton == showShareButton) &&
            (identical(other.showCommentsButton, showCommentsButton) ||
                other.showCommentsButton == showCommentsButton) &&
            (identical(other.showOpenInAppButton, showOpenInAppButton) ||
                other.showOpenInAppButton == showOpenInAppButton));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      defaultHomeSort,
      defaultSort,
      rememberSortByCommunity,
      showAwards,
      clickableAwards,
      showPostFlair,
      showFlairColors,
      showEmojis,
      tapFlairToSearch,
      showAuthor,
      clickableCommunity,
      clickableUser,
      showFloatingButton,
      showHideButton,
      showMarkAsReadButton,
      showShareButton,
      showCommentsButton,
      showOpenInAppButton);

  @override
  String toString() {
    return 'PostsSettings(defaultHomeSort: $defaultHomeSort, defaultSort: $defaultSort, rememberSortByCommunity: $rememberSortByCommunity, showAwards: $showAwards, clickableAwards: $clickableAwards, showPostFlair: $showPostFlair, showFlairColors: $showFlairColors, showEmojis: $showEmojis, tapFlairToSearch: $tapFlairToSearch, showAuthor: $showAuthor, clickableCommunity: $clickableCommunity, clickableUser: $clickableUser, showFloatingButton: $showFloatingButton, showHideButton: $showHideButton, showMarkAsReadButton: $showMarkAsReadButton, showShareButton: $showShareButton, showCommentsButton: $showCommentsButton, showOpenInAppButton: $showOpenInAppButton)';
  }
}

/// @nodoc
abstract mixin class $PostsSettingsCopyWith<$Res> {
  factory $PostsSettingsCopyWith(
          PostsSettings value, $Res Function(PostsSettings) _then) =
      _$PostsSettingsCopyWithImpl;
  @useResult
  $Res call(
      {@Setting(widget: _SortSelection) FeedSort defaultHomeSort,
      @Setting(widget: _SortSelection) FeedSort defaultSort,
      @Setting(hasDescription: true) bool rememberSortByCommunity,
      @Category(name: "Awards") @Setting() bool showAwards,
      @Setting() bool clickableAwards,
      @Category(name: "Flairs") @Setting() bool showPostFlair,
      @Setting() bool showFlairColors,
      @Setting() bool showEmojis,
      @Setting() bool tapFlairToSearch,
      @Category(name: "Post info") @Setting() bool showAuthor,
      @Setting() bool clickableCommunity,
      @Setting() bool clickableUser,
      @Category(name: "Visible buttons") @Setting() bool showFloatingButton,
      @Setting() bool showHideButton,
      @Setting() bool showMarkAsReadButton,
      @Setting() bool showShareButton,
      @Setting() bool showCommentsButton,
      @Setting() bool showOpenInAppButton});

  $FeedSortCopyWith<$Res> get defaultHomeSort;
  $FeedSortCopyWith<$Res> get defaultSort;
}

/// @nodoc
class _$PostsSettingsCopyWithImpl<$Res>
    implements $PostsSettingsCopyWith<$Res> {
  _$PostsSettingsCopyWithImpl(this._self, this._then);

  final PostsSettings _self;
  final $Res Function(PostsSettings) _then;

  /// Create a copy of PostsSettings
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? defaultHomeSort = null,
    Object? defaultSort = null,
    Object? rememberSortByCommunity = null,
    Object? showAwards = null,
    Object? clickableAwards = null,
    Object? showPostFlair = null,
    Object? showFlairColors = null,
    Object? showEmojis = null,
    Object? tapFlairToSearch = null,
    Object? showAuthor = null,
    Object? clickableCommunity = null,
    Object? clickableUser = null,
    Object? showFloatingButton = null,
    Object? showHideButton = null,
    Object? showMarkAsReadButton = null,
    Object? showShareButton = null,
    Object? showCommentsButton = null,
    Object? showOpenInAppButton = null,
  }) {
    return _then(_self.copyWith(
      defaultHomeSort: null == defaultHomeSort
          ? _self.defaultHomeSort
          : defaultHomeSort // ignore: cast_nullable_to_non_nullable
              as FeedSort,
      defaultSort: null == defaultSort
          ? _self.defaultSort
          : defaultSort // ignore: cast_nullable_to_non_nullable
              as FeedSort,
      rememberSortByCommunity: null == rememberSortByCommunity
          ? _self.rememberSortByCommunity
          : rememberSortByCommunity // ignore: cast_nullable_to_non_nullable
              as bool,
      showAwards: null == showAwards
          ? _self.showAwards
          : showAwards // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableAwards: null == clickableAwards
          ? _self.clickableAwards
          : clickableAwards // ignore: cast_nullable_to_non_nullable
              as bool,
      showPostFlair: null == showPostFlair
          ? _self.showPostFlair
          : showPostFlair // ignore: cast_nullable_to_non_nullable
              as bool,
      showFlairColors: null == showFlairColors
          ? _self.showFlairColors
          : showFlairColors // ignore: cast_nullable_to_non_nullable
              as bool,
      showEmojis: null == showEmojis
          ? _self.showEmojis
          : showEmojis // ignore: cast_nullable_to_non_nullable
              as bool,
      tapFlairToSearch: null == tapFlairToSearch
          ? _self.tapFlairToSearch
          : tapFlairToSearch // ignore: cast_nullable_to_non_nullable
              as bool,
      showAuthor: null == showAuthor
          ? _self.showAuthor
          : showAuthor // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableCommunity: null == clickableCommunity
          ? _self.clickableCommunity
          : clickableCommunity // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableUser: null == clickableUser
          ? _self.clickableUser
          : clickableUser // ignore: cast_nullable_to_non_nullable
              as bool,
      showFloatingButton: null == showFloatingButton
          ? _self.showFloatingButton
          : showFloatingButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showHideButton: null == showHideButton
          ? _self.showHideButton
          : showHideButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showMarkAsReadButton: null == showMarkAsReadButton
          ? _self.showMarkAsReadButton
          : showMarkAsReadButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showShareButton: null == showShareButton
          ? _self.showShareButton
          : showShareButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showCommentsButton: null == showCommentsButton
          ? _self.showCommentsButton
          : showCommentsButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showOpenInAppButton: null == showOpenInAppButton
          ? _self.showOpenInAppButton
          : showOpenInAppButton // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }

  /// Create a copy of PostsSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $FeedSortCopyWith<$Res> get defaultHomeSort {
    return $FeedSortCopyWith<$Res>(_self.defaultHomeSort, (value) {
      return _then(_self.copyWith(defaultHomeSort: value));
    });
  }

  /// Create a copy of PostsSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $FeedSortCopyWith<$Res> get defaultSort {
    return $FeedSortCopyWith<$Res>(_self.defaultSort, (value) {
      return _then(_self.copyWith(defaultSort: value));
    });
  }
}

/// @nodoc
@JsonSerializable()
class _PostsSettings extends PostsSettings {
  _PostsSettings(
      {@Setting(widget: _SortSelection)
      this.defaultHomeSort = const FeedSort.best(),
      @Setting(widget: _SortSelection) this.defaultSort = const FeedSort.hot(),
      @Setting(hasDescription: true) this.rememberSortByCommunity = true,
      @Category(name: "Awards") @Setting() this.showAwards = true,
      @Setting() this.clickableAwards = true,
      @Category(name: "Flairs") @Setting() this.showPostFlair = true,
      @Setting() this.showFlairColors = true,
      @Setting() this.showEmojis = true,
      @Setting() this.tapFlairToSearch = true,
      @Category(name: "Post info") @Setting() this.showAuthor = true,
      @Setting() this.clickableCommunity = true,
      @Setting() this.clickableUser = true,
      @Category(name: "Visible buttons")
      @Setting()
      this.showFloatingButton = true,
      @Setting() this.showHideButton = false,
      @Setting() this.showMarkAsReadButton = false,
      @Setting() this.showShareButton = false,
      @Setting() this.showCommentsButton = true,
      @Setting() this.showOpenInAppButton = true})
      : super._();
  factory _PostsSettings.fromJson(Map<String, dynamic> json) =>
      _$PostsSettingsFromJson(json);

  @override
  @JsonKey()
  @Setting(widget: _SortSelection)
  final FeedSort defaultHomeSort;
  @override
  @JsonKey()
  @Setting(widget: _SortSelection)
  final FeedSort defaultSort;
  @override
  @JsonKey()
  @Setting(hasDescription: true)
  final bool rememberSortByCommunity;
// TODO: manage saved sort
  @override
  @JsonKey()
  @Category(name: "Awards")
  @Setting()
  final bool showAwards;
  @override
  @JsonKey()
  @Setting()
  final bool clickableAwards;
  @override
  @JsonKey()
  @Category(name: "Flairs")
  @Setting()
  final bool showPostFlair;
  @override
  @JsonKey()
  @Setting()
  final bool showFlairColors;
  @override
  @JsonKey()
  @Setting()
  final bool showEmojis;
  @override
  @JsonKey()
  @Setting()
  final bool tapFlairToSearch;
  @override
  @JsonKey()
  @Category(name: "Post info")
  @Setting()
  final bool showAuthor;
  @override
  @JsonKey()
  @Setting()
  final bool clickableCommunity;
  @override
  @JsonKey()
  @Setting()
  final bool clickableUser;
  @override
  @JsonKey()
  @Category(name: "Visible buttons")
  @Setting()
  final bool showFloatingButton;
  @override
  @JsonKey()
  @Setting()
  final bool showHideButton;
  @override
  @JsonKey()
  @Setting()
  final bool showMarkAsReadButton;
  @override
  @JsonKey()
  @Setting()
  final bool showShareButton;
  @override
  @JsonKey()
  @Setting()
  final bool showCommentsButton;
  @override
  @JsonKey()
  @Setting()
  final bool showOpenInAppButton;

  /// Create a copy of PostsSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$PostsSettingsCopyWith<_PostsSettings> get copyWith =>
      __$PostsSettingsCopyWithImpl<_PostsSettings>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$PostsSettingsToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _PostsSettings &&
            (identical(other.defaultHomeSort, defaultHomeSort) ||
                other.defaultHomeSort == defaultHomeSort) &&
            (identical(other.defaultSort, defaultSort) ||
                other.defaultSort == defaultSort) &&
            (identical(
                    other.rememberSortByCommunity, rememberSortByCommunity) ||
                other.rememberSortByCommunity == rememberSortByCommunity) &&
            (identical(other.showAwards, showAwards) ||
                other.showAwards == showAwards) &&
            (identical(other.clickableAwards, clickableAwards) ||
                other.clickableAwards == clickableAwards) &&
            (identical(other.showPostFlair, showPostFlair) ||
                other.showPostFlair == showPostFlair) &&
            (identical(other.showFlairColors, showFlairColors) ||
                other.showFlairColors == showFlairColors) &&
            (identical(other.showEmojis, showEmojis) ||
                other.showEmojis == showEmojis) &&
            (identical(other.tapFlairToSearch, tapFlairToSearch) ||
                other.tapFlairToSearch == tapFlairToSearch) &&
            (identical(other.showAuthor, showAuthor) ||
                other.showAuthor == showAuthor) &&
            (identical(other.clickableCommunity, clickableCommunity) ||
                other.clickableCommunity == clickableCommunity) &&
            (identical(other.clickableUser, clickableUser) ||
                other.clickableUser == clickableUser) &&
            (identical(other.showFloatingButton, showFloatingButton) ||
                other.showFloatingButton == showFloatingButton) &&
            (identical(other.showHideButton, showHideButton) ||
                other.showHideButton == showHideButton) &&
            (identical(other.showMarkAsReadButton, showMarkAsReadButton) ||
                other.showMarkAsReadButton == showMarkAsReadButton) &&
            (identical(other.showShareButton, showShareButton) ||
                other.showShareButton == showShareButton) &&
            (identical(other.showCommentsButton, showCommentsButton) ||
                other.showCommentsButton == showCommentsButton) &&
            (identical(other.showOpenInAppButton, showOpenInAppButton) ||
                other.showOpenInAppButton == showOpenInAppButton));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      defaultHomeSort,
      defaultSort,
      rememberSortByCommunity,
      showAwards,
      clickableAwards,
      showPostFlair,
      showFlairColors,
      showEmojis,
      tapFlairToSearch,
      showAuthor,
      clickableCommunity,
      clickableUser,
      showFloatingButton,
      showHideButton,
      showMarkAsReadButton,
      showShareButton,
      showCommentsButton,
      showOpenInAppButton);

  @override
  String toString() {
    return 'PostsSettings(defaultHomeSort: $defaultHomeSort, defaultSort: $defaultSort, rememberSortByCommunity: $rememberSortByCommunity, showAwards: $showAwards, clickableAwards: $clickableAwards, showPostFlair: $showPostFlair, showFlairColors: $showFlairColors, showEmojis: $showEmojis, tapFlairToSearch: $tapFlairToSearch, showAuthor: $showAuthor, clickableCommunity: $clickableCommunity, clickableUser: $clickableUser, showFloatingButton: $showFloatingButton, showHideButton: $showHideButton, showMarkAsReadButton: $showMarkAsReadButton, showShareButton: $showShareButton, showCommentsButton: $showCommentsButton, showOpenInAppButton: $showOpenInAppButton)';
  }
}

/// @nodoc
abstract mixin class _$PostsSettingsCopyWith<$Res>
    implements $PostsSettingsCopyWith<$Res> {
  factory _$PostsSettingsCopyWith(
          _PostsSettings value, $Res Function(_PostsSettings) _then) =
      __$PostsSettingsCopyWithImpl;
  @override
  @useResult
  $Res call(
      {@Setting(widget: _SortSelection) FeedSort defaultHomeSort,
      @Setting(widget: _SortSelection) FeedSort defaultSort,
      @Setting(hasDescription: true) bool rememberSortByCommunity,
      @Category(name: "Awards") @Setting() bool showAwards,
      @Setting() bool clickableAwards,
      @Category(name: "Flairs") @Setting() bool showPostFlair,
      @Setting() bool showFlairColors,
      @Setting() bool showEmojis,
      @Setting() bool tapFlairToSearch,
      @Category(name: "Post info") @Setting() bool showAuthor,
      @Setting() bool clickableCommunity,
      @Setting() bool clickableUser,
      @Category(name: "Visible buttons") @Setting() bool showFloatingButton,
      @Setting() bool showHideButton,
      @Setting() bool showMarkAsReadButton,
      @Setting() bool showShareButton,
      @Setting() bool showCommentsButton,
      @Setting() bool showOpenInAppButton});

  @override
  $FeedSortCopyWith<$Res> get defaultHomeSort;
  @override
  $FeedSortCopyWith<$Res> get defaultSort;
}

/// @nodoc
class __$PostsSettingsCopyWithImpl<$Res>
    implements _$PostsSettingsCopyWith<$Res> {
  __$PostsSettingsCopyWithImpl(this._self, this._then);

  final _PostsSettings _self;
  final $Res Function(_PostsSettings) _then;

  /// Create a copy of PostsSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? defaultHomeSort = null,
    Object? defaultSort = null,
    Object? rememberSortByCommunity = null,
    Object? showAwards = null,
    Object? clickableAwards = null,
    Object? showPostFlair = null,
    Object? showFlairColors = null,
    Object? showEmojis = null,
    Object? tapFlairToSearch = null,
    Object? showAuthor = null,
    Object? clickableCommunity = null,
    Object? clickableUser = null,
    Object? showFloatingButton = null,
    Object? showHideButton = null,
    Object? showMarkAsReadButton = null,
    Object? showShareButton = null,
    Object? showCommentsButton = null,
    Object? showOpenInAppButton = null,
  }) {
    return _then(_PostsSettings(
      defaultHomeSort: null == defaultHomeSort
          ? _self.defaultHomeSort
          : defaultHomeSort // ignore: cast_nullable_to_non_nullable
              as FeedSort,
      defaultSort: null == defaultSort
          ? _self.defaultSort
          : defaultSort // ignore: cast_nullable_to_non_nullable
              as FeedSort,
      rememberSortByCommunity: null == rememberSortByCommunity
          ? _self.rememberSortByCommunity
          : rememberSortByCommunity // ignore: cast_nullable_to_non_nullable
              as bool,
      showAwards: null == showAwards
          ? _self.showAwards
          : showAwards // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableAwards: null == clickableAwards
          ? _self.clickableAwards
          : clickableAwards // ignore: cast_nullable_to_non_nullable
              as bool,
      showPostFlair: null == showPostFlair
          ? _self.showPostFlair
          : showPostFlair // ignore: cast_nullable_to_non_nullable
              as bool,
      showFlairColors: null == showFlairColors
          ? _self.showFlairColors
          : showFlairColors // ignore: cast_nullable_to_non_nullable
              as bool,
      showEmojis: null == showEmojis
          ? _self.showEmojis
          : showEmojis // ignore: cast_nullable_to_non_nullable
              as bool,
      tapFlairToSearch: null == tapFlairToSearch
          ? _self.tapFlairToSearch
          : tapFlairToSearch // ignore: cast_nullable_to_non_nullable
              as bool,
      showAuthor: null == showAuthor
          ? _self.showAuthor
          : showAuthor // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableCommunity: null == clickableCommunity
          ? _self.clickableCommunity
          : clickableCommunity // ignore: cast_nullable_to_non_nullable
              as bool,
      clickableUser: null == clickableUser
          ? _self.clickableUser
          : clickableUser // ignore: cast_nullable_to_non_nullable
              as bool,
      showFloatingButton: null == showFloatingButton
          ? _self.showFloatingButton
          : showFloatingButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showHideButton: null == showHideButton
          ? _self.showHideButton
          : showHideButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showMarkAsReadButton: null == showMarkAsReadButton
          ? _self.showMarkAsReadButton
          : showMarkAsReadButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showShareButton: null == showShareButton
          ? _self.showShareButton
          : showShareButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showCommentsButton: null == showCommentsButton
          ? _self.showCommentsButton
          : showCommentsButton // ignore: cast_nullable_to_non_nullable
              as bool,
      showOpenInAppButton: null == showOpenInAppButton
          ? _self.showOpenInAppButton
          : showOpenInAppButton // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }

  /// Create a copy of PostsSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $FeedSortCopyWith<$Res> get defaultHomeSort {
    return $FeedSortCopyWith<$Res>(_self.defaultHomeSort, (value) {
      return _then(_self.copyWith(defaultHomeSort: value));
    });
  }

  /// Create a copy of PostsSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $FeedSortCopyWith<$Res> get defaultSort {
    return $FeedSortCopyWith<$Res>(_self.defaultSort, (value) {
      return _then(_self.copyWith(defaultSort: value));
    });
  }
}

// dart format on
