// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
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
  bool get rememberSortByCommunity;
  @Setting(widget: ManageSortButton)
  RememberedSort get rememberedSorts;
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
  bool get showFlairEmojis;
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
            (identical(other.rememberedSorts, rememberedSorts) ||
                other.rememberedSorts == rememberedSorts) &&
            (identical(other.showAwards, showAwards) ||
                other.showAwards == showAwards) &&
            (identical(other.clickableAwards, clickableAwards) ||
                other.clickableAwards == clickableAwards) &&
            (identical(other.showPostFlair, showPostFlair) ||
                other.showPostFlair == showPostFlair) &&
            (identical(other.showFlairColors, showFlairColors) ||
                other.showFlairColors == showFlairColors) &&
            (identical(other.showFlairEmojis, showFlairEmojis) ||
                other.showFlairEmojis == showFlairEmojis) &&
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
  int get hashCode => Object.hashAll([
        runtimeType,
        defaultHomeSort,
        defaultSort,
        rememberSortByCommunity,
        rememberedSorts,
        showAwards,
        clickableAwards,
        showPostFlair,
        showFlairColors,
        showFlairEmojis,
        tapFlairToSearch,
        showAuthor,
        clickableCommunity,
        clickableUser,
        showFloatingButton,
        showHideButton,
        showMarkAsReadButton,
        showShareButton,
        showCommentsButton,
        showOpenInAppButton
      ]);

  @override
  String toString() {
    return 'PostsSettings(defaultHomeSort: $defaultHomeSort, defaultSort: $defaultSort, rememberSortByCommunity: $rememberSortByCommunity, rememberedSorts: $rememberedSorts, showAwards: $showAwards, clickableAwards: $clickableAwards, showPostFlair: $showPostFlair, showFlairColors: $showFlairColors, showFlairEmojis: $showFlairEmojis, tapFlairToSearch: $tapFlairToSearch, showAuthor: $showAuthor, clickableCommunity: $clickableCommunity, clickableUser: $clickableUser, showFloatingButton: $showFloatingButton, showHideButton: $showHideButton, showMarkAsReadButton: $showMarkAsReadButton, showShareButton: $showShareButton, showCommentsButton: $showCommentsButton, showOpenInAppButton: $showOpenInAppButton)';
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
      @Setting(widget: ManageSortButton) RememberedSort rememberedSorts,
      @Category(name: "Awards") @Setting() bool showAwards,
      @Setting() bool clickableAwards,
      @Category(name: "Flairs") @Setting() bool showPostFlair,
      @Setting() bool showFlairColors,
      @Setting() bool showFlairEmojis,
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
    Object? rememberedSorts = null,
    Object? showAwards = null,
    Object? clickableAwards = null,
    Object? showPostFlair = null,
    Object? showFlairColors = null,
    Object? showFlairEmojis = null,
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
      rememberedSorts: null == rememberedSorts
          ? _self.rememberedSorts
          : rememberedSorts // ignore: cast_nullable_to_non_nullable
              as RememberedSort,
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
      showFlairEmojis: null == showFlairEmojis
          ? _self.showFlairEmojis
          : showFlairEmojis // ignore: cast_nullable_to_non_nullable
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

/// Adds pattern-matching-related methods to [PostsSettings].
extension PostsSettingsPatterns on PostsSettings {
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
    TResult Function(_PostsSettings value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _PostsSettings() when $default != null:
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
    TResult Function(_PostsSettings value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostsSettings():
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
    TResult? Function(_PostsSettings value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostsSettings() when $default != null:
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
            @Setting(widget: _SortSelection) FeedSort defaultHomeSort,
            @Setting(widget: _SortSelection) FeedSort defaultSort,
            @Setting(hasDescription: true) bool rememberSortByCommunity,
            @Setting(widget: ManageSortButton) RememberedSort rememberedSorts,
            @Category(name: "Awards") @Setting() bool showAwards,
            @Setting() bool clickableAwards,
            @Category(name: "Flairs") @Setting() bool showPostFlair,
            @Setting() bool showFlairColors,
            @Setting() bool showFlairEmojis,
            @Setting() bool tapFlairToSearch,
            @Category(name: "Post info") @Setting() bool showAuthor,
            @Setting() bool clickableCommunity,
            @Setting() bool clickableUser,
            @Category(name: "Visible buttons")
            @Setting()
            bool showFloatingButton,
            @Setting() bool showHideButton,
            @Setting() bool showMarkAsReadButton,
            @Setting() bool showShareButton,
            @Setting() bool showCommentsButton,
            @Setting() bool showOpenInAppButton)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _PostsSettings() when $default != null:
        return $default(
            _that.defaultHomeSort,
            _that.defaultSort,
            _that.rememberSortByCommunity,
            _that.rememberedSorts,
            _that.showAwards,
            _that.clickableAwards,
            _that.showPostFlair,
            _that.showFlairColors,
            _that.showFlairEmojis,
            _that.tapFlairToSearch,
            _that.showAuthor,
            _that.clickableCommunity,
            _that.clickableUser,
            _that.showFloatingButton,
            _that.showHideButton,
            _that.showMarkAsReadButton,
            _that.showShareButton,
            _that.showCommentsButton,
            _that.showOpenInAppButton);
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
            @Setting(widget: _SortSelection) FeedSort defaultHomeSort,
            @Setting(widget: _SortSelection) FeedSort defaultSort,
            @Setting(hasDescription: true) bool rememberSortByCommunity,
            @Setting(widget: ManageSortButton) RememberedSort rememberedSorts,
            @Category(name: "Awards") @Setting() bool showAwards,
            @Setting() bool clickableAwards,
            @Category(name: "Flairs") @Setting() bool showPostFlair,
            @Setting() bool showFlairColors,
            @Setting() bool showFlairEmojis,
            @Setting() bool tapFlairToSearch,
            @Category(name: "Post info") @Setting() bool showAuthor,
            @Setting() bool clickableCommunity,
            @Setting() bool clickableUser,
            @Category(name: "Visible buttons")
            @Setting()
            bool showFloatingButton,
            @Setting() bool showHideButton,
            @Setting() bool showMarkAsReadButton,
            @Setting() bool showShareButton,
            @Setting() bool showCommentsButton,
            @Setting() bool showOpenInAppButton)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostsSettings():
        return $default(
            _that.defaultHomeSort,
            _that.defaultSort,
            _that.rememberSortByCommunity,
            _that.rememberedSorts,
            _that.showAwards,
            _that.clickableAwards,
            _that.showPostFlair,
            _that.showFlairColors,
            _that.showFlairEmojis,
            _that.tapFlairToSearch,
            _that.showAuthor,
            _that.clickableCommunity,
            _that.clickableUser,
            _that.showFloatingButton,
            _that.showHideButton,
            _that.showMarkAsReadButton,
            _that.showShareButton,
            _that.showCommentsButton,
            _that.showOpenInAppButton);
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
            @Setting(widget: _SortSelection) FeedSort defaultHomeSort,
            @Setting(widget: _SortSelection) FeedSort defaultSort,
            @Setting(hasDescription: true) bool rememberSortByCommunity,
            @Setting(widget: ManageSortButton) RememberedSort rememberedSorts,
            @Category(name: "Awards") @Setting() bool showAwards,
            @Setting() bool clickableAwards,
            @Category(name: "Flairs") @Setting() bool showPostFlair,
            @Setting() bool showFlairColors,
            @Setting() bool showFlairEmojis,
            @Setting() bool tapFlairToSearch,
            @Category(name: "Post info") @Setting() bool showAuthor,
            @Setting() bool clickableCommunity,
            @Setting() bool clickableUser,
            @Category(name: "Visible buttons")
            @Setting()
            bool showFloatingButton,
            @Setting() bool showHideButton,
            @Setting() bool showMarkAsReadButton,
            @Setting() bool showShareButton,
            @Setting() bool showCommentsButton,
            @Setting() bool showOpenInAppButton)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostsSettings() when $default != null:
        return $default(
            _that.defaultHomeSort,
            _that.defaultSort,
            _that.rememberSortByCommunity,
            _that.rememberedSorts,
            _that.showAwards,
            _that.clickableAwards,
            _that.showPostFlair,
            _that.showFlairColors,
            _that.showFlairEmojis,
            _that.tapFlairToSearch,
            _that.showAuthor,
            _that.clickableCommunity,
            _that.clickableUser,
            _that.showFloatingButton,
            _that.showHideButton,
            _that.showMarkAsReadButton,
            _that.showShareButton,
            _that.showCommentsButton,
            _that.showOpenInAppButton);
      case _:
        return null;
    }
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
      @Setting(widget: ManageSortButton)
      this.rememberedSorts = const RememberedSort(),
      @Category(name: "Awards") @Setting() this.showAwards = true,
      @Setting() this.clickableAwards = true,
      @Category(name: "Flairs") @Setting() this.showPostFlair = true,
      @Setting() this.showFlairColors = true,
      @Setting() this.showFlairEmojis = true,
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
  @override
  @JsonKey()
  @Setting(widget: ManageSortButton)
  final RememberedSort rememberedSorts;
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
  final bool showFlairEmojis;
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
            (identical(other.rememberedSorts, rememberedSorts) ||
                other.rememberedSorts == rememberedSorts) &&
            (identical(other.showAwards, showAwards) ||
                other.showAwards == showAwards) &&
            (identical(other.clickableAwards, clickableAwards) ||
                other.clickableAwards == clickableAwards) &&
            (identical(other.showPostFlair, showPostFlair) ||
                other.showPostFlair == showPostFlair) &&
            (identical(other.showFlairColors, showFlairColors) ||
                other.showFlairColors == showFlairColors) &&
            (identical(other.showFlairEmojis, showFlairEmojis) ||
                other.showFlairEmojis == showFlairEmojis) &&
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
  int get hashCode => Object.hashAll([
        runtimeType,
        defaultHomeSort,
        defaultSort,
        rememberSortByCommunity,
        rememberedSorts,
        showAwards,
        clickableAwards,
        showPostFlair,
        showFlairColors,
        showFlairEmojis,
        tapFlairToSearch,
        showAuthor,
        clickableCommunity,
        clickableUser,
        showFloatingButton,
        showHideButton,
        showMarkAsReadButton,
        showShareButton,
        showCommentsButton,
        showOpenInAppButton
      ]);

  @override
  String toString() {
    return 'PostsSettings(defaultHomeSort: $defaultHomeSort, defaultSort: $defaultSort, rememberSortByCommunity: $rememberSortByCommunity, rememberedSorts: $rememberedSorts, showAwards: $showAwards, clickableAwards: $clickableAwards, showPostFlair: $showPostFlair, showFlairColors: $showFlairColors, showFlairEmojis: $showFlairEmojis, tapFlairToSearch: $tapFlairToSearch, showAuthor: $showAuthor, clickableCommunity: $clickableCommunity, clickableUser: $clickableUser, showFloatingButton: $showFloatingButton, showHideButton: $showHideButton, showMarkAsReadButton: $showMarkAsReadButton, showShareButton: $showShareButton, showCommentsButton: $showCommentsButton, showOpenInAppButton: $showOpenInAppButton)';
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
      @Setting(widget: ManageSortButton) RememberedSort rememberedSorts,
      @Category(name: "Awards") @Setting() bool showAwards,
      @Setting() bool clickableAwards,
      @Category(name: "Flairs") @Setting() bool showPostFlair,
      @Setting() bool showFlairColors,
      @Setting() bool showFlairEmojis,
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
    Object? rememberedSorts = null,
    Object? showAwards = null,
    Object? clickableAwards = null,
    Object? showPostFlair = null,
    Object? showFlairColors = null,
    Object? showFlairEmojis = null,
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
      rememberedSorts: null == rememberedSorts
          ? _self.rememberedSorts
          : rememberedSorts // ignore: cast_nullable_to_non_nullable
              as RememberedSort,
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
      showFlairEmojis: null == showFlairEmojis
          ? _self.showFlairEmojis
          : showFlairEmojis // ignore: cast_nullable_to_non_nullable
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
