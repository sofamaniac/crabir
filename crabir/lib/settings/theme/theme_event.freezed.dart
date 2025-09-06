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
  ThemeField get field;
  Color get color;

  /// Create a copy of ThemeEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $ThemeEventCopyWith<ThemeEvent> get copyWith =>
      _$ThemeEventCopyWithImpl<ThemeEvent>(this as ThemeEvent, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is ThemeEvent &&
            (identical(other.field, field) || other.field == field) &&
            const DeepCollectionEquality().equals(other.color, color));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType, field, const DeepCollectionEquality().hash(color));

  @override
  String toString() {
    return 'ThemeEvent(field: $field, color: $color)';
  }
}

/// @nodoc
abstract mixin class $ThemeEventCopyWith<$Res> {
  factory $ThemeEventCopyWith(
          ThemeEvent value, $Res Function(ThemeEvent) _then) =
      _$ThemeEventCopyWithImpl;
  @useResult
  $Res call({ThemeField field, Color color});
}

/// @nodoc
class _$ThemeEventCopyWithImpl<$Res> implements $ThemeEventCopyWith<$Res> {
  _$ThemeEventCopyWithImpl(this._self, this._then);

  final ThemeEvent _self;
  final $Res Function(ThemeEvent) _then;

  /// Create a copy of ThemeEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? field = null,
    Object? color = freezed,
  }) {
    return _then(_self.copyWith(
      field: null == field
          ? _self.field
          : field // ignore: cast_nullable_to_non_nullable
              as ThemeField,
      color: freezed == color
          ? _self.color
          : color // ignore: cast_nullable_to_non_nullable
              as Color,
    ));
  }
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
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case SetColor() when setColor != null:
        return setColor(_that);
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
  }) {
    final _that = this;
    switch (_that) {
      case SetColor():
        return setColor(_that);
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
  }) {
    final _that = this;
    switch (_that) {
      case SetColor() when setColor != null:
        return setColor(_that);
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
    TResult Function(ThemeField field, Color color)? setColor,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case SetColor() when setColor != null:
        return setColor(_that.field, _that.color);
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
    required TResult Function(ThemeField field, Color color) setColor,
  }) {
    final _that = this;
    switch (_that) {
      case SetColor():
        return setColor(_that.field, _that.color);
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
    TResult? Function(ThemeField field, Color color)? setColor,
  }) {
    final _that = this;
    switch (_that) {
      case SetColor() when setColor != null:
        return setColor(_that.field, _that.color);
      case _:
        return null;
    }
  }
}

/// @nodoc

class SetColor implements ThemeEvent {
  SetColor({required this.field, required this.color});

  @override
  final ThemeField field;
  @override
  final Color color;

  /// Create a copy of ThemeEvent
  /// with the given fields replaced by the non-null parameter values.
  @override
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
            const DeepCollectionEquality().equals(other.color, color));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType, field, const DeepCollectionEquality().hash(color));

  @override
  String toString() {
    return 'ThemeEvent.setColor(field: $field, color: $color)';
  }
}

/// @nodoc
abstract mixin class $SetColorCopyWith<$Res>
    implements $ThemeEventCopyWith<$Res> {
  factory $SetColorCopyWith(SetColor value, $Res Function(SetColor) _then) =
      _$SetColorCopyWithImpl;
  @override
  @useResult
  $Res call({ThemeField field, Color color});
}

/// @nodoc
class _$SetColorCopyWithImpl<$Res> implements $SetColorCopyWith<$Res> {
  _$SetColorCopyWithImpl(this._self, this._then);

  final SetColor _self;
  final $Res Function(SetColor) _then;

  /// Create a copy of ThemeEvent
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field = null,
    Object? color = freezed,
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
    ));
  }
}

// dart format on
