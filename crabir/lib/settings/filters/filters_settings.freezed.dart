// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'filters_settings.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$FiltersSettings {
  @Category(name: "Muting options")
  @Setting(widget: SubredditsFilterButton, hasDescription: true)
  () get manageHiddenCommunities;
  @Setting(widget: DomainsFilterButton, hasDescription: true)
  () get manageHiddenDomains;
  @Setting(widget: UserFilterButton, hasDescription: true)
  () get manageHiddenUsers;
  @Setting(widget: FlairFilterButton, hasDescription: true)
  () get manageHiddenFlairs;
  @Category(name: "More options")
  @Setting(hasDescription: true)
  bool get showNSFW;
  @Setting(icon: SHOW_NSFW_ICON)
  bool get showImageInNSFW;
  @Setting(icon: BLUR_NSFW_ICON)
  bool get blurNSFW;

  /// Create a copy of FiltersSettings
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $FiltersSettingsCopyWith<FiltersSettings> get copyWith =>
      _$FiltersSettingsCopyWithImpl<FiltersSettings>(
          this as FiltersSettings, _$identity);

  /// Serializes this FiltersSettings to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is FiltersSettings &&
            (identical(
                    other.manageHiddenCommunities, manageHiddenCommunities) ||
                other.manageHiddenCommunities == manageHiddenCommunities) &&
            (identical(other.manageHiddenDomains, manageHiddenDomains) ||
                other.manageHiddenDomains == manageHiddenDomains) &&
            (identical(other.manageHiddenUsers, manageHiddenUsers) ||
                other.manageHiddenUsers == manageHiddenUsers) &&
            (identical(other.manageHiddenFlairs, manageHiddenFlairs) ||
                other.manageHiddenFlairs == manageHiddenFlairs) &&
            (identical(other.showNSFW, showNSFW) ||
                other.showNSFW == showNSFW) &&
            (identical(other.showImageInNSFW, showImageInNSFW) ||
                other.showImageInNSFW == showImageInNSFW) &&
            (identical(other.blurNSFW, blurNSFW) ||
                other.blurNSFW == blurNSFW));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      manageHiddenCommunities,
      manageHiddenDomains,
      manageHiddenUsers,
      manageHiddenFlairs,
      showNSFW,
      showImageInNSFW,
      blurNSFW);

  @override
  String toString() {
    return 'FiltersSettings(manageHiddenCommunities: $manageHiddenCommunities, manageHiddenDomains: $manageHiddenDomains, manageHiddenUsers: $manageHiddenUsers, manageHiddenFlairs: $manageHiddenFlairs, showNSFW: $showNSFW, showImageInNSFW: $showImageInNSFW, blurNSFW: $blurNSFW)';
  }
}

/// @nodoc
abstract mixin class $FiltersSettingsCopyWith<$Res> {
  factory $FiltersSettingsCopyWith(
          FiltersSettings value, $Res Function(FiltersSettings) _then) =
      _$FiltersSettingsCopyWithImpl;
  @useResult
  $Res call(
      {@Category(name: "Muting options")
      @Setting(widget: SubredditsFilterButton, hasDescription: true)
      () manageHiddenCommunities,
      @Setting(widget: DomainsFilterButton, hasDescription: true)
      () manageHiddenDomains,
      @Setting(widget: UserFilterButton, hasDescription: true)
      () manageHiddenUsers,
      @Setting(widget: FlairFilterButton, hasDescription: true)
      () manageHiddenFlairs,
      @Category(name: "More options")
      @Setting(hasDescription: true)
      bool showNSFW,
      @Setting(icon: SHOW_NSFW_ICON) bool showImageInNSFW,
      @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW});
}

/// @nodoc
class _$FiltersSettingsCopyWithImpl<$Res>
    implements $FiltersSettingsCopyWith<$Res> {
  _$FiltersSettingsCopyWithImpl(this._self, this._then);

  final FiltersSettings _self;
  final $Res Function(FiltersSettings) _then;

  /// Create a copy of FiltersSettings
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? manageHiddenCommunities = null,
    Object? manageHiddenDomains = null,
    Object? manageHiddenUsers = null,
    Object? manageHiddenFlairs = null,
    Object? showNSFW = null,
    Object? showImageInNSFW = null,
    Object? blurNSFW = null,
  }) {
    return _then(_self.copyWith(
      manageHiddenCommunities: null == manageHiddenCommunities
          ? _self.manageHiddenCommunities
          : manageHiddenCommunities // ignore: cast_nullable_to_non_nullable
              as (),
      manageHiddenDomains: null == manageHiddenDomains
          ? _self.manageHiddenDomains
          : manageHiddenDomains // ignore: cast_nullable_to_non_nullable
              as (),
      manageHiddenUsers: null == manageHiddenUsers
          ? _self.manageHiddenUsers
          : manageHiddenUsers // ignore: cast_nullable_to_non_nullable
              as (),
      manageHiddenFlairs: null == manageHiddenFlairs
          ? _self.manageHiddenFlairs
          : manageHiddenFlairs // ignore: cast_nullable_to_non_nullable
              as (),
      showNSFW: null == showNSFW
          ? _self.showNSFW
          : showNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
      showImageInNSFW: null == showImageInNSFW
          ? _self.showImageInNSFW
          : showImageInNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
      blurNSFW: null == blurNSFW
          ? _self.blurNSFW
          : blurNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

/// Adds pattern-matching-related methods to [FiltersSettings].
extension FiltersSettingsPatterns on FiltersSettings {
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
    TResult Function(_FiltersSettings value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _FiltersSettings() when $default != null:
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
    TResult Function(_FiltersSettings value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FiltersSettings():
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
    TResult? Function(_FiltersSettings value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FiltersSettings() when $default != null:
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
            @Category(name: "Muting options")
            @Setting(widget: SubredditsFilterButton, hasDescription: true)
            () manageHiddenCommunities,
            @Setting(widget: DomainsFilterButton, hasDescription: true)
            () manageHiddenDomains,
            @Setting(widget: UserFilterButton, hasDescription: true)
            () manageHiddenUsers,
            @Setting(widget: FlairFilterButton, hasDescription: true)
            () manageHiddenFlairs,
            @Category(name: "More options")
            @Setting(hasDescription: true)
            bool showNSFW,
            @Setting(icon: SHOW_NSFW_ICON) bool showImageInNSFW,
            @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _FiltersSettings() when $default != null:
        return $default(
            _that.manageHiddenCommunities,
            _that.manageHiddenDomains,
            _that.manageHiddenUsers,
            _that.manageHiddenFlairs,
            _that.showNSFW,
            _that.showImageInNSFW,
            _that.blurNSFW);
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
            @Category(name: "Muting options")
            @Setting(widget: SubredditsFilterButton, hasDescription: true)
            () manageHiddenCommunities,
            @Setting(widget: DomainsFilterButton, hasDescription: true)
            () manageHiddenDomains,
            @Setting(widget: UserFilterButton, hasDescription: true)
            () manageHiddenUsers,
            @Setting(widget: FlairFilterButton, hasDescription: true)
            () manageHiddenFlairs,
            @Category(name: "More options")
            @Setting(hasDescription: true)
            bool showNSFW,
            @Setting(icon: SHOW_NSFW_ICON) bool showImageInNSFW,
            @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FiltersSettings():
        return $default(
            _that.manageHiddenCommunities,
            _that.manageHiddenDomains,
            _that.manageHiddenUsers,
            _that.manageHiddenFlairs,
            _that.showNSFW,
            _that.showImageInNSFW,
            _that.blurNSFW);
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
            @Category(name: "Muting options")
            @Setting(widget: SubredditsFilterButton, hasDescription: true)
            () manageHiddenCommunities,
            @Setting(widget: DomainsFilterButton, hasDescription: true)
            () manageHiddenDomains,
            @Setting(widget: UserFilterButton, hasDescription: true)
            () manageHiddenUsers,
            @Setting(widget: FlairFilterButton, hasDescription: true)
            () manageHiddenFlairs,
            @Category(name: "More options")
            @Setting(hasDescription: true)
            bool showNSFW,
            @Setting(icon: SHOW_NSFW_ICON) bool showImageInNSFW,
            @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FiltersSettings() when $default != null:
        return $default(
            _that.manageHiddenCommunities,
            _that.manageHiddenDomains,
            _that.manageHiddenUsers,
            _that.manageHiddenFlairs,
            _that.showNSFW,
            _that.showImageInNSFW,
            _that.blurNSFW);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _FiltersSettings extends FiltersSettings {
  _FiltersSettings(
      {@Category(name: "Muting options")
      @Setting(widget: SubredditsFilterButton, hasDescription: true)
      this.manageHiddenCommunities = const (),
      @Setting(widget: DomainsFilterButton, hasDescription: true)
      this.manageHiddenDomains = const (),
      @Setting(widget: UserFilterButton, hasDescription: true)
      this.manageHiddenUsers = const (),
      @Setting(widget: FlairFilterButton, hasDescription: true)
      this.manageHiddenFlairs = const (),
      @Category(name: "More options")
      @Setting(hasDescription: true)
      this.showNSFW = false,
      @Setting(icon: SHOW_NSFW_ICON) this.showImageInNSFW = true,
      @Setting(icon: BLUR_NSFW_ICON) this.blurNSFW = false})
      : super._();
  factory _FiltersSettings.fromJson(Map<String, dynamic> json) =>
      _$FiltersSettingsFromJson(json);

  @override
  @JsonKey()
  @Category(name: "Muting options")
  @Setting(widget: SubredditsFilterButton, hasDescription: true)
  final () manageHiddenCommunities;
  @override
  @JsonKey()
  @Setting(widget: DomainsFilterButton, hasDescription: true)
  final () manageHiddenDomains;
  @override
  @JsonKey()
  @Setting(widget: UserFilterButton, hasDescription: true)
  final () manageHiddenUsers;
  @override
  @JsonKey()
  @Setting(widget: FlairFilterButton, hasDescription: true)
  final () manageHiddenFlairs;
  @override
  @JsonKey()
  @Category(name: "More options")
  @Setting(hasDescription: true)
  final bool showNSFW;
  @override
  @JsonKey()
  @Setting(icon: SHOW_NSFW_ICON)
  final bool showImageInNSFW;
  @override
  @JsonKey()
  @Setting(icon: BLUR_NSFW_ICON)
  final bool blurNSFW;

  /// Create a copy of FiltersSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$FiltersSettingsCopyWith<_FiltersSettings> get copyWith =>
      __$FiltersSettingsCopyWithImpl<_FiltersSettings>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$FiltersSettingsToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _FiltersSettings &&
            (identical(
                    other.manageHiddenCommunities, manageHiddenCommunities) ||
                other.manageHiddenCommunities == manageHiddenCommunities) &&
            (identical(other.manageHiddenDomains, manageHiddenDomains) ||
                other.manageHiddenDomains == manageHiddenDomains) &&
            (identical(other.manageHiddenUsers, manageHiddenUsers) ||
                other.manageHiddenUsers == manageHiddenUsers) &&
            (identical(other.manageHiddenFlairs, manageHiddenFlairs) ||
                other.manageHiddenFlairs == manageHiddenFlairs) &&
            (identical(other.showNSFW, showNSFW) ||
                other.showNSFW == showNSFW) &&
            (identical(other.showImageInNSFW, showImageInNSFW) ||
                other.showImageInNSFW == showImageInNSFW) &&
            (identical(other.blurNSFW, blurNSFW) ||
                other.blurNSFW == blurNSFW));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      manageHiddenCommunities,
      manageHiddenDomains,
      manageHiddenUsers,
      manageHiddenFlairs,
      showNSFW,
      showImageInNSFW,
      blurNSFW);

  @override
  String toString() {
    return 'FiltersSettings(manageHiddenCommunities: $manageHiddenCommunities, manageHiddenDomains: $manageHiddenDomains, manageHiddenUsers: $manageHiddenUsers, manageHiddenFlairs: $manageHiddenFlairs, showNSFW: $showNSFW, showImageInNSFW: $showImageInNSFW, blurNSFW: $blurNSFW)';
  }
}

/// @nodoc
abstract mixin class _$FiltersSettingsCopyWith<$Res>
    implements $FiltersSettingsCopyWith<$Res> {
  factory _$FiltersSettingsCopyWith(
          _FiltersSettings value, $Res Function(_FiltersSettings) _then) =
      __$FiltersSettingsCopyWithImpl;
  @override
  @useResult
  $Res call(
      {@Category(name: "Muting options")
      @Setting(widget: SubredditsFilterButton, hasDescription: true)
      () manageHiddenCommunities,
      @Setting(widget: DomainsFilterButton, hasDescription: true)
      () manageHiddenDomains,
      @Setting(widget: UserFilterButton, hasDescription: true)
      () manageHiddenUsers,
      @Setting(widget: FlairFilterButton, hasDescription: true)
      () manageHiddenFlairs,
      @Category(name: "More options")
      @Setting(hasDescription: true)
      bool showNSFW,
      @Setting(icon: SHOW_NSFW_ICON) bool showImageInNSFW,
      @Setting(icon: BLUR_NSFW_ICON) bool blurNSFW});
}

/// @nodoc
class __$FiltersSettingsCopyWithImpl<$Res>
    implements _$FiltersSettingsCopyWith<$Res> {
  __$FiltersSettingsCopyWithImpl(this._self, this._then);

  final _FiltersSettings _self;
  final $Res Function(_FiltersSettings) _then;

  /// Create a copy of FiltersSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? manageHiddenCommunities = null,
    Object? manageHiddenDomains = null,
    Object? manageHiddenUsers = null,
    Object? manageHiddenFlairs = null,
    Object? showNSFW = null,
    Object? showImageInNSFW = null,
    Object? blurNSFW = null,
  }) {
    return _then(_FiltersSettings(
      manageHiddenCommunities: null == manageHiddenCommunities
          ? _self.manageHiddenCommunities
          : manageHiddenCommunities // ignore: cast_nullable_to_non_nullable
              as (),
      manageHiddenDomains: null == manageHiddenDomains
          ? _self.manageHiddenDomains
          : manageHiddenDomains // ignore: cast_nullable_to_non_nullable
              as (),
      manageHiddenUsers: null == manageHiddenUsers
          ? _self.manageHiddenUsers
          : manageHiddenUsers // ignore: cast_nullable_to_non_nullable
              as (),
      manageHiddenFlairs: null == manageHiddenFlairs
          ? _self.manageHiddenFlairs
          : manageHiddenFlairs // ignore: cast_nullable_to_non_nullable
              as (),
      showNSFW: null == showNSFW
          ? _self.showNSFW
          : showNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
      showImageInNSFW: null == showImageInNSFW
          ? _self.showImageInNSFW
          : showImageInNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
      blurNSFW: null == blurNSFW
          ? _self.blurNSFW
          : blurNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

// dart format on
