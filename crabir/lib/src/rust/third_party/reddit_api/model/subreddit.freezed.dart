// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'subreddit.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$SubredditIcon {
  Object get field0;

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is SubredditIcon &&
            const DeepCollectionEquality().equals(other.field0, field0));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(field0));

  @override
  String toString() {
    return 'SubredditIcon(field0: $field0)';
  }
}

/// @nodoc
class $SubredditIconCopyWith<$Res> {
  $SubredditIconCopyWith(SubredditIcon _, $Res Function(SubredditIcon) __);
}

/// Adds pattern-matching-related methods to [SubredditIcon].
extension SubredditIconPatterns on SubredditIcon {
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
    TResult Function(SubredditIcon_Image value)? image,
    TResult Function(SubredditIcon_Color value)? color,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case SubredditIcon_Image() when image != null:
        return image(_that);
      case SubredditIcon_Color() when color != null:
        return color(_that);
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
    required TResult Function(SubredditIcon_Image value) image,
    required TResult Function(SubredditIcon_Color value) color,
  }) {
    final _that = this;
    switch (_that) {
      case SubredditIcon_Image():
        return image(_that);
      case SubredditIcon_Color():
        return color(_that);
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
    TResult? Function(SubredditIcon_Image value)? image,
    TResult? Function(SubredditIcon_Color value)? color,
  }) {
    final _that = this;
    switch (_that) {
      case SubredditIcon_Image() when image != null:
        return image(_that);
      case SubredditIcon_Color() when color != null:
        return color(_that);
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
    TResult Function(Icon field0)? image,
    TResult Function(String field0)? color,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case SubredditIcon_Image() when image != null:
        return image(_that.field0);
      case SubredditIcon_Color() when color != null:
        return color(_that.field0);
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
    required TResult Function(Icon field0) image,
    required TResult Function(String field0) color,
  }) {
    final _that = this;
    switch (_that) {
      case SubredditIcon_Image():
        return image(_that.field0);
      case SubredditIcon_Color():
        return color(_that.field0);
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
    TResult? Function(Icon field0)? image,
    TResult? Function(String field0)? color,
  }) {
    final _that = this;
    switch (_that) {
      case SubredditIcon_Image() when image != null:
        return image(_that.field0);
      case SubredditIcon_Color() when color != null:
        return color(_that.field0);
      case _:
        return null;
    }
  }
}

/// @nodoc

class SubredditIcon_Image extends SubredditIcon {
  const SubredditIcon_Image(this.field0) : super._();

  @override
  final Icon field0;

  /// Create a copy of SubredditIcon
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $SubredditIcon_ImageCopyWith<SubredditIcon_Image> get copyWith =>
      _$SubredditIcon_ImageCopyWithImpl<SubredditIcon_Image>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is SubredditIcon_Image &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'SubredditIcon.image(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $SubredditIcon_ImageCopyWith<$Res>
    implements $SubredditIconCopyWith<$Res> {
  factory $SubredditIcon_ImageCopyWith(
          SubredditIcon_Image value, $Res Function(SubredditIcon_Image) _then) =
      _$SubredditIcon_ImageCopyWithImpl;
  @useResult
  $Res call({Icon field0});
}

/// @nodoc
class _$SubredditIcon_ImageCopyWithImpl<$Res>
    implements $SubredditIcon_ImageCopyWith<$Res> {
  _$SubredditIcon_ImageCopyWithImpl(this._self, this._then);

  final SubredditIcon_Image _self;
  final $Res Function(SubredditIcon_Image) _then;

  /// Create a copy of SubredditIcon
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(SubredditIcon_Image(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Icon,
    ));
  }
}

/// @nodoc

class SubredditIcon_Color extends SubredditIcon {
  const SubredditIcon_Color(this.field0) : super._();

  @override
  final String field0;

  /// Create a copy of SubredditIcon
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $SubredditIcon_ColorCopyWith<SubredditIcon_Color> get copyWith =>
      _$SubredditIcon_ColorCopyWithImpl<SubredditIcon_Color>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is SubredditIcon_Color &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'SubredditIcon.color(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $SubredditIcon_ColorCopyWith<$Res>
    implements $SubredditIconCopyWith<$Res> {
  factory $SubredditIcon_ColorCopyWith(
          SubredditIcon_Color value, $Res Function(SubredditIcon_Color) _then) =
      _$SubredditIcon_ColorCopyWithImpl;
  @useResult
  $Res call({String field0});
}

/// @nodoc
class _$SubredditIcon_ColorCopyWithImpl<$Res>
    implements $SubredditIcon_ColorCopyWith<$Res> {
  _$SubredditIcon_ColorCopyWithImpl(this._self, this._then);

  final SubredditIcon_Color _self;
  final $Res Function(SubredditIcon_Color) _then;

  /// Create a copy of SubredditIcon
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(SubredditIcon_Color(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

// dart format on
