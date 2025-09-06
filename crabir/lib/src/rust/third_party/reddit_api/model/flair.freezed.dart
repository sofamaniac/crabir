// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'flair.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$Richtext {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Richtext);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'Richtext()';
  }
}

/// @nodoc
class $RichtextCopyWith<$Res> {
  $RichtextCopyWith(Richtext _, $Res Function(Richtext) __);
}

/// Adds pattern-matching-related methods to [Richtext].
extension RichtextPatterns on Richtext {
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
    TResult Function(Richtext_Text value)? text,
    TResult Function(Richtext_Emoji value)? emoji,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Richtext_Text() when text != null:
        return text(_that);
      case Richtext_Emoji() when emoji != null:
        return emoji(_that);
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
    required TResult Function(Richtext_Text value) text,
    required TResult Function(Richtext_Emoji value) emoji,
  }) {
    final _that = this;
    switch (_that) {
      case Richtext_Text():
        return text(_that);
      case Richtext_Emoji():
        return emoji(_that);
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
    TResult? Function(Richtext_Text value)? text,
    TResult? Function(Richtext_Emoji value)? emoji,
  }) {
    final _that = this;
    switch (_that) {
      case Richtext_Text() when text != null:
        return text(_that);
      case Richtext_Emoji() when emoji != null:
        return emoji(_that);
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
    TResult Function(String t)? text,
    TResult Function(String a, String u)? emoji,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Richtext_Text() when text != null:
        return text(_that.t);
      case Richtext_Emoji() when emoji != null:
        return emoji(_that.a, _that.u);
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
    required TResult Function(String t) text,
    required TResult Function(String a, String u) emoji,
  }) {
    final _that = this;
    switch (_that) {
      case Richtext_Text():
        return text(_that.t);
      case Richtext_Emoji():
        return emoji(_that.a, _that.u);
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
    TResult? Function(String t)? text,
    TResult? Function(String a, String u)? emoji,
  }) {
    final _that = this;
    switch (_that) {
      case Richtext_Text() when text != null:
        return text(_that.t);
      case Richtext_Emoji() when emoji != null:
        return emoji(_that.a, _that.u);
      case _:
        return null;
    }
  }
}

/// @nodoc

class Richtext_Text extends Richtext {
  const Richtext_Text({required this.t}) : super._();

  final String t;

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Richtext_TextCopyWith<Richtext_Text> get copyWith =>
      _$Richtext_TextCopyWithImpl<Richtext_Text>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Richtext_Text &&
            (identical(other.t, t) || other.t == t));
  }

  @override
  int get hashCode => Object.hash(runtimeType, t);

  @override
  String toString() {
    return 'Richtext.text(t: $t)';
  }
}

/// @nodoc
abstract mixin class $Richtext_TextCopyWith<$Res>
    implements $RichtextCopyWith<$Res> {
  factory $Richtext_TextCopyWith(
          Richtext_Text value, $Res Function(Richtext_Text) _then) =
      _$Richtext_TextCopyWithImpl;
  @useResult
  $Res call({String t});
}

/// @nodoc
class _$Richtext_TextCopyWithImpl<$Res>
    implements $Richtext_TextCopyWith<$Res> {
  _$Richtext_TextCopyWithImpl(this._self, this._then);

  final Richtext_Text _self;
  final $Res Function(Richtext_Text) _then;

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? t = null,
  }) {
    return _then(Richtext_Text(
      t: null == t
          ? _self.t
          : t // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class Richtext_Emoji extends Richtext {
  const Richtext_Emoji({required this.a, required this.u}) : super._();

  final String a;
  final String u;

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Richtext_EmojiCopyWith<Richtext_Emoji> get copyWith =>
      _$Richtext_EmojiCopyWithImpl<Richtext_Emoji>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Richtext_Emoji &&
            (identical(other.a, a) || other.a == a) &&
            (identical(other.u, u) || other.u == u));
  }

  @override
  int get hashCode => Object.hash(runtimeType, a, u);

  @override
  String toString() {
    return 'Richtext.emoji(a: $a, u: $u)';
  }
}

/// @nodoc
abstract mixin class $Richtext_EmojiCopyWith<$Res>
    implements $RichtextCopyWith<$Res> {
  factory $Richtext_EmojiCopyWith(
          Richtext_Emoji value, $Res Function(Richtext_Emoji) _then) =
      _$Richtext_EmojiCopyWithImpl;
  @useResult
  $Res call({String a, String u});
}

/// @nodoc
class _$Richtext_EmojiCopyWithImpl<$Res>
    implements $Richtext_EmojiCopyWith<$Res> {
  _$Richtext_EmojiCopyWithImpl(this._self, this._then);

  final Richtext_Emoji _self;
  final $Res Function(Richtext_Emoji) _then;

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? a = null,
    Object? u = null,
  }) {
    return _then(Richtext_Emoji(
      a: null == a
          ? _self.a
          : a // ignore: cast_nullable_to_non_nullable
              as String,
      u: null == u
          ? _self.u
          : u // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

// dart format on
