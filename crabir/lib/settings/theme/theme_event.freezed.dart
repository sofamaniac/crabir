// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'theme_event.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$ThemeEvent {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is ThemeEvent);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'ThemeEvent()';
  }
}

/// @nodoc
class $ThemeEventCopyWith<$Res> {
  $ThemeEventCopyWith(ThemeEvent _, $Res Function(ThemeEvent) __);
}

/// Adds pattern-matching-related methods to [ThemeEvent].
extension ThemeEventPatterns on ThemeEvent {
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
  TResult maybeMap<TResult extends Object?>({
    TResult Function(SetColor value)? setColor,
    TResult Function(SetMode value)? setMode,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case SetColor() when setColor != null:
        return setColor(_that);
      case SetMode() when setMode != null:
        return setMode(_that);
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
  TResult map<TResult extends Object?>({
    required TResult Function(SetColor value) setColor,
    required TResult Function(SetMode value) setMode,
  }) {
    final _that = this;
    switch (_that) {
      case SetColor():
        return setColor(_that);
      case SetMode():
        return setMode(_that);
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
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(SetColor value)? setColor,
    TResult? Function(SetMode value)? setMode,
  }) {
    final _that = this;
    switch (_that) {
      case SetColor() when setColor != null:
        return setColor(_that);
      case SetMode() when setMode != null:
        return setMode(_that);
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
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(ThemeField field, Color color, Brightness brightness)?
        setColor,
    TResult Function(ThemeMode mode)? setMode,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case SetColor() when setColor != null:
        return setColor(_that.field, _that.color, _that.brightness);
      case SetMode() when setMode != null:
        return setMode(_that.mode);
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
  TResult when<TResult extends Object?>({
    required TResult Function(
            ThemeField field, Color color, Brightness brightness)
        setColor,
    required TResult Function(ThemeMode mode) setMode,
  }) {
    final _that = this;
    switch (_that) {
      case SetColor():
        return setColor(_that.field, _that.color, _that.brightness);
      case SetMode():
        return setMode(_that.mode);
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
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(ThemeField field, Color color, Brightness brightness)?
        setColor,
    TResult? Function(ThemeMode mode)? setMode,
  }) {
    final _that = this;
    switch (_that) {
      case SetColor() when setColor != null:
        return setColor(_that.field, _that.color, _that.brightness);
      case SetMode() when setMode != null:
        return setMode(_that.mode);
      case _:
        return null;
    }
  }
}

/// @nodoc

class SetColor implements ThemeEvent {
  SetColor(
      {required this.field, required this.color, required this.brightness});

  final ThemeField field;
  final Color color;
  final Brightness brightness;

  /// Create a copy of ThemeEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $SetColorCopyWith<SetColor> get copyWith =>
      _$SetColorCopyWithImpl<SetColor>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is SetColor &&
            (identical(other.field, field) || other.field == field) &&
            const DeepCollectionEquality().equals(other.color, color) &&
            const DeepCollectionEquality()
                .equals(other.brightness, brightness));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      field,
      const DeepCollectionEquality().hash(color),
      const DeepCollectionEquality().hash(brightness));

  @override
  String toString() {
    return 'ThemeEvent.setColor(field: $field, color: $color, brightness: $brightness)';
  }
}

/// @nodoc
abstract mixin class $SetColorCopyWith<$Res>
    implements $ThemeEventCopyWith<$Res> {
  factory $SetColorCopyWith(SetColor value, $Res Function(SetColor) _then) =
      _$SetColorCopyWithImpl;
  @useResult
  $Res call({ThemeField field, Color color, Brightness brightness});
}

/// @nodoc
class _$SetColorCopyWithImpl<$Res> implements $SetColorCopyWith<$Res> {
  _$SetColorCopyWithImpl(this._self, this._then);

  final SetColor _self;
  final $Res Function(SetColor) _then;

  /// Create a copy of ThemeEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field = null,
    Object? color = freezed,
    Object? brightness = freezed,
  }) {
    return _then(SetColor(
      field: null == field
          ? _self.field
          : field // ignore: cast_nullable_to_non_nullable
              as ThemeField,
      color: freezed == color
          ? _self.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
      brightness: freezed == brightness
          ? _self.brightness
          : brightness // ignore: cast_nullable_to_non_nullable
              as Brightness,
    ));
  }
}

/// @nodoc

class SetMode implements ThemeEvent {
  SetMode({required this.mode});

  final ThemeMode mode;

  /// Create a copy of ThemeEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $SetModeCopyWith<SetMode> get copyWith =>
      _$SetModeCopyWithImpl<SetMode>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is SetMode &&
            (identical(other.mode, mode) || other.mode == mode));
  }

  @override
  int get hashCode => Object.hash(runtimeType, mode);

  @override
  String toString() {
    return 'ThemeEvent.setMode(mode: $mode)';
  }
}

/// @nodoc
abstract mixin class $SetModeCopyWith<$Res>
    implements $ThemeEventCopyWith<$Res> {
  factory $SetModeCopyWith(SetMode value, $Res Function(SetMode) _then) =
      _$SetModeCopyWithImpl;
  @useResult
  $Res call({ThemeMode mode});
}

/// @nodoc
class _$SetModeCopyWithImpl<$Res> implements $SetModeCopyWith<$Res> {
  _$SetModeCopyWithImpl(this._self, this._then);

  final SetMode _self;
  final $Res Function(SetMode) _then;

  /// Create a copy of ThemeEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? mode = null,
  }) {
    return _then(SetMode(
      mode: null == mode
          ? _self.mode
          : mode // ignore: cast_nullable_to_non_nullable
              as ThemeMode,
    ));
  }
}

// dart format on
