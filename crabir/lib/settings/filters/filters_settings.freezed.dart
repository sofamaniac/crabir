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
  @Setting(icon: "Icons.blur_on")
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
            (identical(other.blurNSFW, blurNSFW) ||
                other.blurNSFW == blurNSFW));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, blurNSFW);

  @override
  String toString() {
    return 'FiltersSettings(blurNSFW: $blurNSFW)';
  }
}

/// @nodoc
abstract mixin class $FiltersSettingsCopyWith<$Res> {
  factory $FiltersSettingsCopyWith(
          FiltersSettings value, $Res Function(FiltersSettings) _then) =
      _$FiltersSettingsCopyWithImpl;
  @useResult
  $Res call({@Setting(icon: "Icons.blur_on") bool blurNSFW});
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
    Object? blurNSFW = null,
  }) {
    return _then(_self.copyWith(
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
    TResult Function(@Setting(icon: "Icons.blur_on") bool blurNSFW)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _FiltersSettings() when $default != null:
        return $default(_that.blurNSFW);
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
    TResult Function(@Setting(icon: "Icons.blur_on") bool blurNSFW) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FiltersSettings():
        return $default(_that.blurNSFW);
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
    TResult? Function(@Setting(icon: "Icons.blur_on") bool blurNSFW)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FiltersSettings() when $default != null:
        return $default(_that.blurNSFW);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _FiltersSettings extends FiltersSettings {
  _FiltersSettings({@Setting(icon: "Icons.blur_on") this.blurNSFW = false})
      : super._();
  factory _FiltersSettings.fromJson(Map<String, dynamic> json) =>
      _$FiltersSettingsFromJson(json);

  @override
  @JsonKey()
  @Setting(icon: "Icons.blur_on")
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
            (identical(other.blurNSFW, blurNSFW) ||
                other.blurNSFW == blurNSFW));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, blurNSFW);

  @override
  String toString() {
    return 'FiltersSettings(blurNSFW: $blurNSFW)';
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
  $Res call({@Setting(icon: "Icons.blur_on") bool blurNSFW});
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
    Object? blurNSFW = null,
  }) {
    return _then(_FiltersSettings(
      blurNSFW: null == blurNSFW
          ? _self.blurNSFW
          : blurNSFW // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

// dart format on
