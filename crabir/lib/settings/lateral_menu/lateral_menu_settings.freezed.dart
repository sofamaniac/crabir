// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'lateral_menu_settings.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$LateralMenuSettings {
  @Category(name: "Items to show")
  @Setting(hasDescription: true, icon: HOME_PAGE_ICON)
  bool get showHome;
  @Setting(icon: HOME_FEED_ICON, hasDescription: true)
  bool get showHomeFeed;
  @Setting(icon: POPULAR_ICON)
  bool get showPopular;
  @Setting(icon: ALL_ICON)
  bool get showAll;
  @Setting(icon: SAVED_FEED_ICON)
  bool get showSaved;
  @Setting(icon: HISTORY_ICON)
  bool get showHistory;
  @Setting(icon: PROFILE_ICON)
  bool get showProfile;
  @Setting(icon: INBOX_ICON)
  bool get showInbox;
  @Setting(icon: FRIENDS_ICON)
  bool get showFriends;
  @Setting(icon: DRAFTS_ICON)
  bool get showDrafts;
  @Setting(icon: MODERATION_ICON)
  bool get showModeration;
  @Setting(icon: SEARCH_ICON)
  bool get showSearch;
  @Setting(icon: GOTO_ICON)
  bool get showGoToDropdown;
  @Setting(icon: GOTO_COMMUNITY_ICON)
  bool get showGoToCommunity;
  @Setting(icon: GOTO_USER_ICON)
  bool get showGoToUser;
  @Setting(icon: DARK_MODE_ICON)
  bool get darkMode;
  @Setting(icon: SHOW_NSFW_ICON)
  bool get showNSFW;
  @Setting(icon: BLUR_NSFW_ICON)
  bool get blurNSFW;
  @Category(name: "Subscription list")
  @Setting()
  bool get showSubsInMenu;
  @Setting()
  bool get showSubsIcon;
  @Setting()
  bool get showSubsFavOnly;
  @Category(name: "Account switcher")
  @Setting()
  bool get showAccountAvatar;
  @Setting()
  bool get showAccountUsername;

  /// Create a copy of LateralMenuSettings
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $LateralMenuSettingsCopyWith<LateralMenuSettings> get copyWith =>
      _$LateralMenuSettingsCopyWithImpl<LateralMenuSettings>(
          this as LateralMenuSettings, _$identity);

  /// Serializes this LateralMenuSettings to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is LateralMenuSettings &&
            (identical(other.showHome, showHome) ||
                other.showHome == showHome) &&
            (identical(other.showHomeFeed, showHomeFeed) ||
                other.showHomeFeed == showHomeFeed) &&
            (identical(other.showPopular, showPopular) ||
                other.showPopular == showPopular) &&
            (identical(other.showAll, showAll) || other.showAll == showAll) &&
            (identical(other.showSaved, showSaved) ||
                other.showSaved == showSaved) &&
            (identical(other.showHistory, showHistory) ||
                other.showHistory == showHistory) &&
            (identical(other.showProfile, showProfile) ||
                other.showProfile == showProfile) &&
            (identical(other.showInbox, showInbox) ||
                other.showInbox == showInbox) &&
            (identical(other.showFriends, showFriends) ||
                other.showFriends == showFriends) &&
            (identical(other.showDrafts, showDrafts) ||
                other.showDrafts == showDrafts) &&
            (identical(other.showModeration, showModeration) ||
                other.showModeration == showModeration) &&
            (identical(other.showSearch, showSearch) ||
                other.showSearch == showSearch) &&
            (identical(other.showGoToDropdown, showGoToDropdown) ||
                other.showGoToDropdown == showGoToDropdown) &&
            (identical(other.showGoToCommunity, showGoToCommunity) ||
                other.showGoToCommunity == showGoToCommunity) &&
            (identical(other.showGoToUser, showGoToUser) ||
                other.showGoToUser == showGoToUser) &&
            (identical(other.darkMode, darkMode) ||
                other.darkMode == darkMode) &&
            (identical(other.showNSFW, showNSFW) ||
                other.showNSFW == showNSFW) &&
            (identical(other.blurNSFW, blurNSFW) ||
                other.blurNSFW == blurNSFW) &&
            (identical(other.showSubsInMenu, showSubsInMenu) ||
                other.showSubsInMenu == showSubsInMenu) &&
            (identical(other.showSubsIcon, showSubsIcon) ||
                other.showSubsIcon == showSubsIcon) &&
            (identical(other.showSubsFavOnly, showSubsFavOnly) ||
                other.showSubsFavOnly == showSubsFavOnly) &&
            (identical(other.showAccountAvatar, showAccountAvatar) ||
                other.showAccountAvatar == showAccountAvatar) &&
            (identical(other.showAccountUsername, showAccountUsername) ||
                other.showAccountUsername == showAccountUsername));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hashAll([
        runtimeType,
        showHome,
        showHomeFeed,
        showPopular,
        showAll,
        showSaved,
        showHistory,
        showProfile,
        showInbox,
        showFriends,
        showDrafts,
        showModeration,
        showSearch,
        showGoToDropdown,
        showGoToCommunity,
        showGoToUser,
        darkMode,
        showNSFW,
        blurNSFW,
        showSubsInMenu,
        showSubsIcon,
        showSubsFavOnly,
        showAccountAvatar,
        showAccountUsername
      ]);

  @override
  String toString() {
    return 'LateralMenuSettings(showHome: $showHome, showHomeFeed: $showHomeFeed, showPopular: $showPopular, showAll: $showAll, showSaved: $showSaved, showHistory: $showHistory, showProfile: $showProfile, showInbox: $showInbox, showFriends: $showFriends, showDrafts: $showDrafts, showModeration: $showModeration, showSearch: $showSearch, showGoToDropdown: $showGoToDropdown, showGoToCommunity: $showGoToCommunity, showGoToUser: $showGoToUser, darkMode: $darkMode, showNSFW: $showNSFW, blurNSFW: $blurNSFW, showSubsInMenu: $showSubsInMenu, showSubsIcon: $showSubsIcon, showSubsFavOnly: $showSubsFavOnly, showAccountAvatar: $showAccountAvatar, showAccountUsername: $showAccountUsername)';
  }
}

/// @nodoc
abstract mixin class $LateralMenuSettingsCopyWith<$Res> {
  factory $LateralMenuSettingsCopyWith(
          LateralMenuSettings value, $Res Function(LateralMenuSettings) _then) =
      _$LateralMenuSettingsCopyWithImpl;
  @useResult
  $Res call(
      {@Category(name: "Items to show")
      @Setting(hasDescription: true, icon: HOME_PAGE_ICON)
      bool showHome,
      @Setting(icon: HOME_FEED_ICON, hasDescription: true) bool showHomeFeed,
      @Setting(icon: POPULAR_ICON) bool showPopular,
      @Setting(icon: ALL_ICON) bool showAll,
      @Setting(icon: SAVED_FEED_ICON) bool showSaved,
      @Setting(icon: HISTORY_ICON) bool showHistory,
      @Setting(icon: PROFILE_ICON) bool showProfile,
      @Setting(icon: INBOX_ICON) bool showInbox,
      @Setting(icon: FRIENDS_ICON) bool showFriends,
      @Setting(icon: DRAFTS_ICON) bool showDrafts,
      @Setting(icon: MODERATION_ICON) bool showModeration,
      @Setting(icon: SEARCH_ICON) bool showSearch,
      @Setting(icon: GOTO_ICON) bool showGoToDropdown,
      @Setting(icon: GOTO_COMMUNITY_ICON) bool showGoToCommunity,
      @Setting(icon: GOTO_USER_ICON) bool showGoToUser,
      @Setting(icon: DARK_MODE_ICON) bool darkMode,
      @Setting(icon: SHOW_NSFW_ICON) bool showNSFW,
      @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW,
      @Category(name: "Subscription list") @Setting() bool showSubsInMenu,
      @Setting() bool showSubsIcon,
      @Setting() bool showSubsFavOnly,
      @Category(name: "Account switcher") @Setting() bool showAccountAvatar,
      @Setting() bool showAccountUsername});
}

/// @nodoc
class _$LateralMenuSettingsCopyWithImpl<$Res>
    implements $LateralMenuSettingsCopyWith<$Res> {
  _$LateralMenuSettingsCopyWithImpl(this._self, this._then);

  final LateralMenuSettings _self;
  final $Res Function(LateralMenuSettings) _then;

  /// Create a copy of LateralMenuSettings
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? showHome = null,
    Object? showHomeFeed = null,
    Object? showPopular = null,
    Object? showAll = null,
    Object? showSaved = null,
    Object? showHistory = null,
    Object? showProfile = null,
    Object? showInbox = null,
    Object? showFriends = null,
    Object? showDrafts = null,
    Object? showModeration = null,
    Object? showSearch = null,
    Object? showGoToDropdown = null,
    Object? showGoToCommunity = null,
    Object? showGoToUser = null,
    Object? darkMode = null,
    Object? showNSFW = null,
    Object? blurNSFW = null,
    Object? showSubsInMenu = null,
    Object? showSubsIcon = null,
    Object? showSubsFavOnly = null,
    Object? showAccountAvatar = null,
    Object? showAccountUsername = null,
  }) {
    return _then(_self.copyWith(
      showHome: null == showHome
          ? _self.showHome
          : showHome // ignore: cast_nullable_to_non_nullable
              as bool,
      showHomeFeed: null == showHomeFeed
          ? _self.showHomeFeed
          : showHomeFeed // ignore: cast_nullable_to_non_nullable
              as bool,
      showPopular: null == showPopular
          ? _self.showPopular
          : showPopular // ignore: cast_nullable_to_non_nullable
              as bool,
      showAll: null == showAll
          ? _self.showAll
          : showAll // ignore: cast_nullable_to_non_nullable
              as bool,
      showSaved: null == showSaved
          ? _self.showSaved
          : showSaved // ignore: cast_nullable_to_non_nullable
              as bool,
      showHistory: null == showHistory
          ? _self.showHistory
          : showHistory // ignore: cast_nullable_to_non_nullable
              as bool,
      showProfile: null == showProfile
          ? _self.showProfile
          : showProfile // ignore: cast_nullable_to_non_nullable
              as bool,
      showInbox: null == showInbox
          ? _self.showInbox
          : showInbox // ignore: cast_nullable_to_non_nullable
              as bool,
      showFriends: null == showFriends
          ? _self.showFriends
          : showFriends // ignore: cast_nullable_to_non_nullable
              as bool,
      showDrafts: null == showDrafts
          ? _self.showDrafts
          : showDrafts // ignore: cast_nullable_to_non_nullable
              as bool,
      showModeration: null == showModeration
          ? _self.showModeration
          : showModeration // ignore: cast_nullable_to_non_nullable
              as bool,
      showSearch: null == showSearch
          ? _self.showSearch
          : showSearch // ignore: cast_nullable_to_non_nullable
              as bool,
      showGoToDropdown: null == showGoToDropdown
          ? _self.showGoToDropdown
          : showGoToDropdown // ignore: cast_nullable_to_non_nullable
              as bool,
      showGoToCommunity: null == showGoToCommunity
          ? _self.showGoToCommunity
          : showGoToCommunity // ignore: cast_nullable_to_non_nullable
              as bool,
      showGoToUser: null == showGoToUser
          ? _self.showGoToUser
          : showGoToUser // ignore: cast_nullable_to_non_nullable
              as bool,
      darkMode: null == darkMode
          ? _self.darkMode
          : darkMode // ignore: cast_nullable_to_non_nullable
              as bool,
      showNSFW: null == showNSFW
          ? _self.showNSFW
          : showNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
      blurNSFW: null == blurNSFW
          ? _self.blurNSFW
          : blurNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
      showSubsInMenu: null == showSubsInMenu
          ? _self.showSubsInMenu
          : showSubsInMenu // ignore: cast_nullable_to_non_nullable
              as bool,
      showSubsIcon: null == showSubsIcon
          ? _self.showSubsIcon
          : showSubsIcon // ignore: cast_nullable_to_non_nullable
              as bool,
      showSubsFavOnly: null == showSubsFavOnly
          ? _self.showSubsFavOnly
          : showSubsFavOnly // ignore: cast_nullable_to_non_nullable
              as bool,
      showAccountAvatar: null == showAccountAvatar
          ? _self.showAccountAvatar
          : showAccountAvatar // ignore: cast_nullable_to_non_nullable
              as bool,
      showAccountUsername: null == showAccountUsername
          ? _self.showAccountUsername
          : showAccountUsername // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

/// Adds pattern-matching-related methods to [LateralMenuSettings].
extension LateralMenuSettingsPatterns on LateralMenuSettings {
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
    TResult Function(_LateralMenuSettings value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _LateralMenuSettings() when $default != null:
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
    TResult Function(_LateralMenuSettings value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _LateralMenuSettings():
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
    TResult? Function(_LateralMenuSettings value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _LateralMenuSettings() when $default != null:
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
            @Category(name: "Items to show")
            @Setting(hasDescription: true, icon: HOME_PAGE_ICON)
            bool showHome,
            @Setting(icon: HOME_FEED_ICON, hasDescription: true)
            bool showHomeFeed,
            @Setting(icon: POPULAR_ICON) bool showPopular,
            @Setting(icon: ALL_ICON) bool showAll,
            @Setting(icon: SAVED_FEED_ICON) bool showSaved,
            @Setting(icon: HISTORY_ICON) bool showHistory,
            @Setting(icon: PROFILE_ICON) bool showProfile,
            @Setting(icon: INBOX_ICON) bool showInbox,
            @Setting(icon: FRIENDS_ICON) bool showFriends,
            @Setting(icon: DRAFTS_ICON) bool showDrafts,
            @Setting(icon: MODERATION_ICON) bool showModeration,
            @Setting(icon: SEARCH_ICON) bool showSearch,
            @Setting(icon: GOTO_ICON) bool showGoToDropdown,
            @Setting(icon: GOTO_COMMUNITY_ICON) bool showGoToCommunity,
            @Setting(icon: GOTO_USER_ICON) bool showGoToUser,
            @Setting(icon: DARK_MODE_ICON) bool darkMode,
            @Setting(icon: SHOW_NSFW_ICON) bool showNSFW,
            @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW,
            @Category(name: "Subscription list") @Setting() bool showSubsInMenu,
            @Setting() bool showSubsIcon,
            @Setting() bool showSubsFavOnly,
            @Category(name: "Account switcher")
            @Setting()
            bool showAccountAvatar,
            @Setting() bool showAccountUsername)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _LateralMenuSettings() when $default != null:
        return $default(
            _that.showHome,
            _that.showHomeFeed,
            _that.showPopular,
            _that.showAll,
            _that.showSaved,
            _that.showHistory,
            _that.showProfile,
            _that.showInbox,
            _that.showFriends,
            _that.showDrafts,
            _that.showModeration,
            _that.showSearch,
            _that.showGoToDropdown,
            _that.showGoToCommunity,
            _that.showGoToUser,
            _that.darkMode,
            _that.showNSFW,
            _that.blurNSFW,
            _that.showSubsInMenu,
            _that.showSubsIcon,
            _that.showSubsFavOnly,
            _that.showAccountAvatar,
            _that.showAccountUsername);
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
            @Category(name: "Items to show")
            @Setting(hasDescription: true, icon: HOME_PAGE_ICON)
            bool showHome,
            @Setting(icon: HOME_FEED_ICON, hasDescription: true)
            bool showHomeFeed,
            @Setting(icon: POPULAR_ICON) bool showPopular,
            @Setting(icon: ALL_ICON) bool showAll,
            @Setting(icon: SAVED_FEED_ICON) bool showSaved,
            @Setting(icon: HISTORY_ICON) bool showHistory,
            @Setting(icon: PROFILE_ICON) bool showProfile,
            @Setting(icon: INBOX_ICON) bool showInbox,
            @Setting(icon: FRIENDS_ICON) bool showFriends,
            @Setting(icon: DRAFTS_ICON) bool showDrafts,
            @Setting(icon: MODERATION_ICON) bool showModeration,
            @Setting(icon: SEARCH_ICON) bool showSearch,
            @Setting(icon: GOTO_ICON) bool showGoToDropdown,
            @Setting(icon: GOTO_COMMUNITY_ICON) bool showGoToCommunity,
            @Setting(icon: GOTO_USER_ICON) bool showGoToUser,
            @Setting(icon: DARK_MODE_ICON) bool darkMode,
            @Setting(icon: SHOW_NSFW_ICON) bool showNSFW,
            @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW,
            @Category(name: "Subscription list") @Setting() bool showSubsInMenu,
            @Setting() bool showSubsIcon,
            @Setting() bool showSubsFavOnly,
            @Category(name: "Account switcher")
            @Setting()
            bool showAccountAvatar,
            @Setting() bool showAccountUsername)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _LateralMenuSettings():
        return $default(
            _that.showHome,
            _that.showHomeFeed,
            _that.showPopular,
            _that.showAll,
            _that.showSaved,
            _that.showHistory,
            _that.showProfile,
            _that.showInbox,
            _that.showFriends,
            _that.showDrafts,
            _that.showModeration,
            _that.showSearch,
            _that.showGoToDropdown,
            _that.showGoToCommunity,
            _that.showGoToUser,
            _that.darkMode,
            _that.showNSFW,
            _that.blurNSFW,
            _that.showSubsInMenu,
            _that.showSubsIcon,
            _that.showSubsFavOnly,
            _that.showAccountAvatar,
            _that.showAccountUsername);
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
            @Category(name: "Items to show")
            @Setting(hasDescription: true, icon: HOME_PAGE_ICON)
            bool showHome,
            @Setting(icon: HOME_FEED_ICON, hasDescription: true)
            bool showHomeFeed,
            @Setting(icon: POPULAR_ICON) bool showPopular,
            @Setting(icon: ALL_ICON) bool showAll,
            @Setting(icon: SAVED_FEED_ICON) bool showSaved,
            @Setting(icon: HISTORY_ICON) bool showHistory,
            @Setting(icon: PROFILE_ICON) bool showProfile,
            @Setting(icon: INBOX_ICON) bool showInbox,
            @Setting(icon: FRIENDS_ICON) bool showFriends,
            @Setting(icon: DRAFTS_ICON) bool showDrafts,
            @Setting(icon: MODERATION_ICON) bool showModeration,
            @Setting(icon: SEARCH_ICON) bool showSearch,
            @Setting(icon: GOTO_ICON) bool showGoToDropdown,
            @Setting(icon: GOTO_COMMUNITY_ICON) bool showGoToCommunity,
            @Setting(icon: GOTO_USER_ICON) bool showGoToUser,
            @Setting(icon: DARK_MODE_ICON) bool darkMode,
            @Setting(icon: SHOW_NSFW_ICON) bool showNSFW,
            @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW,
            @Category(name: "Subscription list") @Setting() bool showSubsInMenu,
            @Setting() bool showSubsIcon,
            @Setting() bool showSubsFavOnly,
            @Category(name: "Account switcher")
            @Setting()
            bool showAccountAvatar,
            @Setting() bool showAccountUsername)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _LateralMenuSettings() when $default != null:
        return $default(
            _that.showHome,
            _that.showHomeFeed,
            _that.showPopular,
            _that.showAll,
            _that.showSaved,
            _that.showHistory,
            _that.showProfile,
            _that.showInbox,
            _that.showFriends,
            _that.showDrafts,
            _that.showModeration,
            _that.showSearch,
            _that.showGoToDropdown,
            _that.showGoToCommunity,
            _that.showGoToUser,
            _that.darkMode,
            _that.showNSFW,
            _that.blurNSFW,
            _that.showSubsInMenu,
            _that.showSubsIcon,
            _that.showSubsFavOnly,
            _that.showAccountAvatar,
            _that.showAccountUsername);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _LateralMenuSettings extends LateralMenuSettings {
  _LateralMenuSettings(
      {@Category(name: "Items to show")
      @Setting(hasDescription: true, icon: HOME_PAGE_ICON)
      this.showHome = true,
      @Setting(icon: HOME_FEED_ICON, hasDescription: true)
      this.showHomeFeed = true,
      @Setting(icon: POPULAR_ICON) this.showPopular = true,
      @Setting(icon: ALL_ICON) this.showAll = true,
      @Setting(icon: SAVED_FEED_ICON) this.showSaved = true,
      @Setting(icon: HISTORY_ICON) this.showHistory = true,
      @Setting(icon: PROFILE_ICON) this.showProfile = true,
      @Setting(icon: INBOX_ICON) this.showInbox = true,
      @Setting(icon: FRIENDS_ICON) this.showFriends = true,
      @Setting(icon: DRAFTS_ICON) this.showDrafts = true,
      @Setting(icon: MODERATION_ICON) this.showModeration = true,
      @Setting(icon: SEARCH_ICON) this.showSearch = true,
      @Setting(icon: GOTO_ICON) this.showGoToDropdown = true,
      @Setting(icon: GOTO_COMMUNITY_ICON) this.showGoToCommunity = true,
      @Setting(icon: GOTO_USER_ICON) this.showGoToUser = true,
      @Setting(icon: DARK_MODE_ICON) this.darkMode = true,
      @Setting(icon: SHOW_NSFW_ICON) this.showNSFW = true,
      @Setting(icon: BLUR_NSFW_ICON) this.blurNSFW = true,
      @Category(name: "Subscription list")
      @Setting()
      this.showSubsInMenu = true,
      @Setting() this.showSubsIcon = true,
      @Setting() this.showSubsFavOnly = false,
      @Category(name: "Account switcher")
      @Setting()
      this.showAccountAvatar = true,
      @Setting() this.showAccountUsername = true})
      : super._();
  factory _LateralMenuSettings.fromJson(Map<String, dynamic> json) =>
      _$LateralMenuSettingsFromJson(json);

  @override
  @JsonKey()
  @Category(name: "Items to show")
  @Setting(hasDescription: true, icon: HOME_PAGE_ICON)
  final bool showHome;
  @override
  @JsonKey()
  @Setting(icon: HOME_FEED_ICON, hasDescription: true)
  final bool showHomeFeed;
  @override
  @JsonKey()
  @Setting(icon: POPULAR_ICON)
  final bool showPopular;
  @override
  @JsonKey()
  @Setting(icon: ALL_ICON)
  final bool showAll;
  @override
  @JsonKey()
  @Setting(icon: SAVED_FEED_ICON)
  final bool showSaved;
  @override
  @JsonKey()
  @Setting(icon: HISTORY_ICON)
  final bool showHistory;
  @override
  @JsonKey()
  @Setting(icon: PROFILE_ICON)
  final bool showProfile;
  @override
  @JsonKey()
  @Setting(icon: INBOX_ICON)
  final bool showInbox;
  @override
  @JsonKey()
  @Setting(icon: FRIENDS_ICON)
  final bool showFriends;
  @override
  @JsonKey()
  @Setting(icon: DRAFTS_ICON)
  final bool showDrafts;
  @override
  @JsonKey()
  @Setting(icon: MODERATION_ICON)
  final bool showModeration;
  @override
  @JsonKey()
  @Setting(icon: SEARCH_ICON)
  final bool showSearch;
  @override
  @JsonKey()
  @Setting(icon: GOTO_ICON)
  final bool showGoToDropdown;
  @override
  @JsonKey()
  @Setting(icon: GOTO_COMMUNITY_ICON)
  final bool showGoToCommunity;
  @override
  @JsonKey()
  @Setting(icon: GOTO_USER_ICON)
  final bool showGoToUser;
  @override
  @JsonKey()
  @Setting(icon: DARK_MODE_ICON)
  final bool darkMode;
  @override
  @JsonKey()
  @Setting(icon: SHOW_NSFW_ICON)
  final bool showNSFW;
  @override
  @JsonKey()
  @Setting(icon: BLUR_NSFW_ICON)
  final bool blurNSFW;
  @override
  @JsonKey()
  @Category(name: "Subscription list")
  @Setting()
  final bool showSubsInMenu;
  @override
  @JsonKey()
  @Setting()
  final bool showSubsIcon;
  @override
  @JsonKey()
  @Setting()
  final bool showSubsFavOnly;
  @override
  @JsonKey()
  @Category(name: "Account switcher")
  @Setting()
  final bool showAccountAvatar;
  @override
  @JsonKey()
  @Setting()
  final bool showAccountUsername;

  /// Create a copy of LateralMenuSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$LateralMenuSettingsCopyWith<_LateralMenuSettings> get copyWith =>
      __$LateralMenuSettingsCopyWithImpl<_LateralMenuSettings>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$LateralMenuSettingsToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _LateralMenuSettings &&
            (identical(other.showHome, showHome) ||
                other.showHome == showHome) &&
            (identical(other.showHomeFeed, showHomeFeed) ||
                other.showHomeFeed == showHomeFeed) &&
            (identical(other.showPopular, showPopular) ||
                other.showPopular == showPopular) &&
            (identical(other.showAll, showAll) || other.showAll == showAll) &&
            (identical(other.showSaved, showSaved) ||
                other.showSaved == showSaved) &&
            (identical(other.showHistory, showHistory) ||
                other.showHistory == showHistory) &&
            (identical(other.showProfile, showProfile) ||
                other.showProfile == showProfile) &&
            (identical(other.showInbox, showInbox) ||
                other.showInbox == showInbox) &&
            (identical(other.showFriends, showFriends) ||
                other.showFriends == showFriends) &&
            (identical(other.showDrafts, showDrafts) ||
                other.showDrafts == showDrafts) &&
            (identical(other.showModeration, showModeration) ||
                other.showModeration == showModeration) &&
            (identical(other.showSearch, showSearch) ||
                other.showSearch == showSearch) &&
            (identical(other.showGoToDropdown, showGoToDropdown) ||
                other.showGoToDropdown == showGoToDropdown) &&
            (identical(other.showGoToCommunity, showGoToCommunity) ||
                other.showGoToCommunity == showGoToCommunity) &&
            (identical(other.showGoToUser, showGoToUser) ||
                other.showGoToUser == showGoToUser) &&
            (identical(other.darkMode, darkMode) ||
                other.darkMode == darkMode) &&
            (identical(other.showNSFW, showNSFW) ||
                other.showNSFW == showNSFW) &&
            (identical(other.blurNSFW, blurNSFW) ||
                other.blurNSFW == blurNSFW) &&
            (identical(other.showSubsInMenu, showSubsInMenu) ||
                other.showSubsInMenu == showSubsInMenu) &&
            (identical(other.showSubsIcon, showSubsIcon) ||
                other.showSubsIcon == showSubsIcon) &&
            (identical(other.showSubsFavOnly, showSubsFavOnly) ||
                other.showSubsFavOnly == showSubsFavOnly) &&
            (identical(other.showAccountAvatar, showAccountAvatar) ||
                other.showAccountAvatar == showAccountAvatar) &&
            (identical(other.showAccountUsername, showAccountUsername) ||
                other.showAccountUsername == showAccountUsername));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hashAll([
        runtimeType,
        showHome,
        showHomeFeed,
        showPopular,
        showAll,
        showSaved,
        showHistory,
        showProfile,
        showInbox,
        showFriends,
        showDrafts,
        showModeration,
        showSearch,
        showGoToDropdown,
        showGoToCommunity,
        showGoToUser,
        darkMode,
        showNSFW,
        blurNSFW,
        showSubsInMenu,
        showSubsIcon,
        showSubsFavOnly,
        showAccountAvatar,
        showAccountUsername
      ]);

  @override
  String toString() {
    return 'LateralMenuSettings(showHome: $showHome, showHomeFeed: $showHomeFeed, showPopular: $showPopular, showAll: $showAll, showSaved: $showSaved, showHistory: $showHistory, showProfile: $showProfile, showInbox: $showInbox, showFriends: $showFriends, showDrafts: $showDrafts, showModeration: $showModeration, showSearch: $showSearch, showGoToDropdown: $showGoToDropdown, showGoToCommunity: $showGoToCommunity, showGoToUser: $showGoToUser, darkMode: $darkMode, showNSFW: $showNSFW, blurNSFW: $blurNSFW, showSubsInMenu: $showSubsInMenu, showSubsIcon: $showSubsIcon, showSubsFavOnly: $showSubsFavOnly, showAccountAvatar: $showAccountAvatar, showAccountUsername: $showAccountUsername)';
  }
}

/// @nodoc
abstract mixin class _$LateralMenuSettingsCopyWith<$Res>
    implements $LateralMenuSettingsCopyWith<$Res> {
  factory _$LateralMenuSettingsCopyWith(_LateralMenuSettings value,
          $Res Function(_LateralMenuSettings) _then) =
      __$LateralMenuSettingsCopyWithImpl;
  @override
  @useResult
  $Res call(
      {@Category(name: "Items to show")
      @Setting(hasDescription: true, icon: HOME_PAGE_ICON)
      bool showHome,
      @Setting(icon: HOME_FEED_ICON, hasDescription: true) bool showHomeFeed,
      @Setting(icon: POPULAR_ICON) bool showPopular,
      @Setting(icon: ALL_ICON) bool showAll,
      @Setting(icon: SAVED_FEED_ICON) bool showSaved,
      @Setting(icon: HISTORY_ICON) bool showHistory,
      @Setting(icon: PROFILE_ICON) bool showProfile,
      @Setting(icon: INBOX_ICON) bool showInbox,
      @Setting(icon: FRIENDS_ICON) bool showFriends,
      @Setting(icon: DRAFTS_ICON) bool showDrafts,
      @Setting(icon: MODERATION_ICON) bool showModeration,
      @Setting(icon: SEARCH_ICON) bool showSearch,
      @Setting(icon: GOTO_ICON) bool showGoToDropdown,
      @Setting(icon: GOTO_COMMUNITY_ICON) bool showGoToCommunity,
      @Setting(icon: GOTO_USER_ICON) bool showGoToUser,
      @Setting(icon: DARK_MODE_ICON) bool darkMode,
      @Setting(icon: SHOW_NSFW_ICON) bool showNSFW,
      @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW,
      @Category(name: "Subscription list") @Setting() bool showSubsInMenu,
      @Setting() bool showSubsIcon,
      @Setting() bool showSubsFavOnly,
      @Category(name: "Account switcher") @Setting() bool showAccountAvatar,
      @Setting() bool showAccountUsername});
}

/// @nodoc
class __$LateralMenuSettingsCopyWithImpl<$Res>
    implements _$LateralMenuSettingsCopyWith<$Res> {
  __$LateralMenuSettingsCopyWithImpl(this._self, this._then);

  final _LateralMenuSettings _self;
  final $Res Function(_LateralMenuSettings) _then;

  /// Create a copy of LateralMenuSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? showHome = null,
    Object? showHomeFeed = null,
    Object? showPopular = null,
    Object? showAll = null,
    Object? showSaved = null,
    Object? showHistory = null,
    Object? showProfile = null,
    Object? showInbox = null,
    Object? showFriends = null,
    Object? showDrafts = null,
    Object? showModeration = null,
    Object? showSearch = null,
    Object? showGoToDropdown = null,
    Object? showGoToCommunity = null,
    Object? showGoToUser = null,
    Object? darkMode = null,
    Object? showNSFW = null,
    Object? blurNSFW = null,
    Object? showSubsInMenu = null,
    Object? showSubsIcon = null,
    Object? showSubsFavOnly = null,
    Object? showAccountAvatar = null,
    Object? showAccountUsername = null,
  }) {
    return _then(_LateralMenuSettings(
      showHome: null == showHome
          ? _self.showHome
          : showHome // ignore: cast_nullable_to_non_nullable
              as bool,
      showHomeFeed: null == showHomeFeed
          ? _self.showHomeFeed
          : showHomeFeed // ignore: cast_nullable_to_non_nullable
              as bool,
      showPopular: null == showPopular
          ? _self.showPopular
          : showPopular // ignore: cast_nullable_to_non_nullable
              as bool,
      showAll: null == showAll
          ? _self.showAll
          : showAll // ignore: cast_nullable_to_non_nullable
              as bool,
      showSaved: null == showSaved
          ? _self.showSaved
          : showSaved // ignore: cast_nullable_to_non_nullable
              as bool,
      showHistory: null == showHistory
          ? _self.showHistory
          : showHistory // ignore: cast_nullable_to_non_nullable
              as bool,
      showProfile: null == showProfile
          ? _self.showProfile
          : showProfile // ignore: cast_nullable_to_non_nullable
              as bool,
      showInbox: null == showInbox
          ? _self.showInbox
          : showInbox // ignore: cast_nullable_to_non_nullable
              as bool,
      showFriends: null == showFriends
          ? _self.showFriends
          : showFriends // ignore: cast_nullable_to_non_nullable
              as bool,
      showDrafts: null == showDrafts
          ? _self.showDrafts
          : showDrafts // ignore: cast_nullable_to_non_nullable
              as bool,
      showModeration: null == showModeration
          ? _self.showModeration
          : showModeration // ignore: cast_nullable_to_non_nullable
              as bool,
      showSearch: null == showSearch
          ? _self.showSearch
          : showSearch // ignore: cast_nullable_to_non_nullable
              as bool,
      showGoToDropdown: null == showGoToDropdown
          ? _self.showGoToDropdown
          : showGoToDropdown // ignore: cast_nullable_to_non_nullable
              as bool,
      showGoToCommunity: null == showGoToCommunity
          ? _self.showGoToCommunity
          : showGoToCommunity // ignore: cast_nullable_to_non_nullable
              as bool,
      showGoToUser: null == showGoToUser
          ? _self.showGoToUser
          : showGoToUser // ignore: cast_nullable_to_non_nullable
              as bool,
      darkMode: null == darkMode
          ? _self.darkMode
          : darkMode // ignore: cast_nullable_to_non_nullable
              as bool,
      showNSFW: null == showNSFW
          ? _self.showNSFW
          : showNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
      blurNSFW: null == blurNSFW
          ? _self.blurNSFW
          : blurNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
      showSubsInMenu: null == showSubsInMenu
          ? _self.showSubsInMenu
          : showSubsInMenu // ignore: cast_nullable_to_non_nullable
              as bool,
      showSubsIcon: null == showSubsIcon
          ? _self.showSubsIcon
          : showSubsIcon // ignore: cast_nullable_to_non_nullable
              as bool,
      showSubsFavOnly: null == showSubsFavOnly
          ? _self.showSubsFavOnly
          : showSubsFavOnly // ignore: cast_nullable_to_non_nullable
              as bool,
      showAccountAvatar: null == showAccountAvatar
          ? _self.showAccountAvatar
          : showAccountAvatar // ignore: cast_nullable_to_non_nullable
              as bool,
      showAccountUsername: null == showAccountUsername
          ? _self.showAccountUsername
          : showAccountUsername // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

// dart format on
