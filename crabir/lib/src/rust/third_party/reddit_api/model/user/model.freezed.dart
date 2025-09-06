// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$UserStreamSort {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is UserStreamSort);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'UserStreamSort()';
  }
}

/// @nodoc
class $UserStreamSortCopyWith<$Res> {
  $UserStreamSortCopyWith(UserStreamSort _, $Res Function(UserStreamSort) __);
}

/// Adds pattern-matching-related methods to [UserStreamSort].
extension UserStreamSortPatterns on UserStreamSort {
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
    TResult Function(UserStreamSort_Hot value)? hot,
    TResult Function(UserStreamSort_Top value)? top,
    TResult Function(UserStreamSort_New value)? new_,
    TResult Function(UserStreamSort_Controversial value)? controversial,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case UserStreamSort_Hot() when hot != null:
        return hot(_that);
      case UserStreamSort_Top() when top != null:
        return top(_that);
      case UserStreamSort_New() when new_ != null:
        return new_(_that);
      case UserStreamSort_Controversial() when controversial != null:
        return controversial(_that);
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
    required TResult Function(UserStreamSort_Hot value) hot,
    required TResult Function(UserStreamSort_Top value) top,
    required TResult Function(UserStreamSort_New value) new_,
    required TResult Function(UserStreamSort_Controversial value) controversial,
  }) {
    final _that = this;
    switch (_that) {
      case UserStreamSort_Hot():
        return hot(_that);
      case UserStreamSort_Top():
        return top(_that);
      case UserStreamSort_New():
        return new_(_that);
      case UserStreamSort_Controversial():
        return controversial(_that);
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
    TResult? Function(UserStreamSort_Hot value)? hot,
    TResult? Function(UserStreamSort_Top value)? top,
    TResult? Function(UserStreamSort_New value)? new_,
    TResult? Function(UserStreamSort_Controversial value)? controversial,
  }) {
    final _that = this;
    switch (_that) {
      case UserStreamSort_Hot() when hot != null:
        return hot(_that);
      case UserStreamSort_Top() when top != null:
        return top(_that);
      case UserStreamSort_New() when new_ != null:
        return new_(_that);
      case UserStreamSort_Controversial() when controversial != null:
        return controversial(_that);
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
    TResult Function()? hot,
    TResult Function(Timeframe field0)? top,
    TResult Function()? new_,
    TResult Function(Timeframe field0)? controversial,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case UserStreamSort_Hot() when hot != null:
        return hot();
      case UserStreamSort_Top() when top != null:
        return top(_that.field0);
      case UserStreamSort_New() when new_ != null:
        return new_();
      case UserStreamSort_Controversial() when controversial != null:
        return controversial(_that.field0);
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
    required TResult Function() hot,
    required TResult Function(Timeframe field0) top,
    required TResult Function() new_,
    required TResult Function(Timeframe field0) controversial,
  }) {
    final _that = this;
    switch (_that) {
      case UserStreamSort_Hot():
        return hot();
      case UserStreamSort_Top():
        return top(_that.field0);
      case UserStreamSort_New():
        return new_();
      case UserStreamSort_Controversial():
        return controversial(_that.field0);
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
    TResult? Function()? hot,
    TResult? Function(Timeframe field0)? top,
    TResult? Function()? new_,
    TResult? Function(Timeframe field0)? controversial,
  }) {
    final _that = this;
    switch (_that) {
      case UserStreamSort_Hot() when hot != null:
        return hot();
      case UserStreamSort_Top() when top != null:
        return top(_that.field0);
      case UserStreamSort_New() when new_ != null:
        return new_();
      case UserStreamSort_Controversial() when controversial != null:
        return controversial(_that.field0);
      case _:
        return null;
    }
  }
}

/// @nodoc

class UserStreamSort_Hot extends UserStreamSort {
  const UserStreamSort_Hot() : super._();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is UserStreamSort_Hot);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'UserStreamSort.hot()';
  }
}

/// @nodoc

class UserStreamSort_Top extends UserStreamSort {
  const UserStreamSort_Top(this.field0) : super._();

  final Timeframe field0;

  /// Create a copy of UserStreamSort
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $UserStreamSort_TopCopyWith<UserStreamSort_Top> get copyWith =>
      _$UserStreamSort_TopCopyWithImpl<UserStreamSort_Top>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is UserStreamSort_Top &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'UserStreamSort.top(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $UserStreamSort_TopCopyWith<$Res>
    implements $UserStreamSortCopyWith<$Res> {
  factory $UserStreamSort_TopCopyWith(
          UserStreamSort_Top value, $Res Function(UserStreamSort_Top) _then) =
      _$UserStreamSort_TopCopyWithImpl;
  @useResult
  $Res call({Timeframe field0});
}

/// @nodoc
class _$UserStreamSort_TopCopyWithImpl<$Res>
    implements $UserStreamSort_TopCopyWith<$Res> {
  _$UserStreamSort_TopCopyWithImpl(this._self, this._then);

  final UserStreamSort_Top _self;
  final $Res Function(UserStreamSort_Top) _then;

  /// Create a copy of UserStreamSort
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(UserStreamSort_Top(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Timeframe,
    ));
  }
}

/// @nodoc

class UserStreamSort_New extends UserStreamSort {
  const UserStreamSort_New() : super._();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is UserStreamSort_New);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'UserStreamSort.new_()';
  }
}

/// @nodoc

class UserStreamSort_Controversial extends UserStreamSort {
  const UserStreamSort_Controversial(this.field0) : super._();

  final Timeframe field0;

  /// Create a copy of UserStreamSort
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $UserStreamSort_ControversialCopyWith<UserStreamSort_Controversial>
      get copyWith => _$UserStreamSort_ControversialCopyWithImpl<
          UserStreamSort_Controversial>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is UserStreamSort_Controversial &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'UserStreamSort.controversial(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $UserStreamSort_ControversialCopyWith<$Res>
    implements $UserStreamSortCopyWith<$Res> {
  factory $UserStreamSort_ControversialCopyWith(
          UserStreamSort_Controversial value,
          $Res Function(UserStreamSort_Controversial) _then) =
      _$UserStreamSort_ControversialCopyWithImpl;
  @useResult
  $Res call({Timeframe field0});
}

/// @nodoc
class _$UserStreamSort_ControversialCopyWithImpl<$Res>
    implements $UserStreamSort_ControversialCopyWith<$Res> {
  _$UserStreamSort_ControversialCopyWithImpl(this._self, this._then);

  final UserStreamSort_Controversial _self;
  final $Res Function(UserStreamSort_Controversial) _then;

  /// Create a copy of UserStreamSort
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(UserStreamSort_Controversial(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Timeframe,
    ));
  }
}

// dart format on
