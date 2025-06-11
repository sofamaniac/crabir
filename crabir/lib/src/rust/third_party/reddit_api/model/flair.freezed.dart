// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'flair.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

/// @nodoc
mixin _$Richtext {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(String t) text,
    required TResult Function(String a, String u) emoji,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(String t)? text,
    TResult? Function(String a, String u)? emoji,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(String t)? text,
    TResult Function(String a, String u)? emoji,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Richtext_Text value) text,
    required TResult Function(Richtext_Emoji value) emoji,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Richtext_Text value)? text,
    TResult? Function(Richtext_Emoji value)? emoji,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Richtext_Text value)? text,
    TResult Function(Richtext_Emoji value)? emoji,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $RichtextCopyWith<$Res> {
  factory $RichtextCopyWith(Richtext value, $Res Function(Richtext) then) =
      _$RichtextCopyWithImpl<$Res, Richtext>;
}

/// @nodoc
class _$RichtextCopyWithImpl<$Res, $Val extends Richtext>
    implements $RichtextCopyWith<$Res> {
  _$RichtextCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
}

/// @nodoc
abstract class _$$Richtext_TextImplCopyWith<$Res> {
  factory _$$Richtext_TextImplCopyWith(
          _$Richtext_TextImpl value, $Res Function(_$Richtext_TextImpl) then) =
      __$$Richtext_TextImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String t});
}

/// @nodoc
class __$$Richtext_TextImplCopyWithImpl<$Res>
    extends _$RichtextCopyWithImpl<$Res, _$Richtext_TextImpl>
    implements _$$Richtext_TextImplCopyWith<$Res> {
  __$$Richtext_TextImplCopyWithImpl(
      _$Richtext_TextImpl _value, $Res Function(_$Richtext_TextImpl) _then)
      : super(_value, _then);

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? t = null,
  }) {
    return _then(_$Richtext_TextImpl(
      t: null == t
          ? _value.t
          : t // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$Richtext_TextImpl extends Richtext_Text {
  const _$Richtext_TextImpl({required this.t}) : super._();

  @override
  final String t;

  @override
  String toString() {
    return 'Richtext.text(t: $t)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$Richtext_TextImpl &&
            (identical(other.t, t) || other.t == t));
  }

  @override
  int get hashCode => Object.hash(runtimeType, t);

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$Richtext_TextImplCopyWith<_$Richtext_TextImpl> get copyWith =>
      __$$Richtext_TextImplCopyWithImpl<_$Richtext_TextImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(String t) text,
    required TResult Function(String a, String u) emoji,
  }) {
    return text(t);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(String t)? text,
    TResult? Function(String a, String u)? emoji,
  }) {
    return text?.call(t);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(String t)? text,
    TResult Function(String a, String u)? emoji,
    required TResult orElse(),
  }) {
    if (text != null) {
      return text(t);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Richtext_Text value) text,
    required TResult Function(Richtext_Emoji value) emoji,
  }) {
    return text(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Richtext_Text value)? text,
    TResult? Function(Richtext_Emoji value)? emoji,
  }) {
    return text?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Richtext_Text value)? text,
    TResult Function(Richtext_Emoji value)? emoji,
    required TResult orElse(),
  }) {
    if (text != null) {
      return text(this);
    }
    return orElse();
  }
}

abstract class Richtext_Text extends Richtext {
  const factory Richtext_Text({required final String t}) = _$Richtext_TextImpl;
  const Richtext_Text._() : super._();

  String get t;

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$Richtext_TextImplCopyWith<_$Richtext_TextImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$Richtext_EmojiImplCopyWith<$Res> {
  factory _$$Richtext_EmojiImplCopyWith(_$Richtext_EmojiImpl value,
          $Res Function(_$Richtext_EmojiImpl) then) =
      __$$Richtext_EmojiImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String a, String u});
}

/// @nodoc
class __$$Richtext_EmojiImplCopyWithImpl<$Res>
    extends _$RichtextCopyWithImpl<$Res, _$Richtext_EmojiImpl>
    implements _$$Richtext_EmojiImplCopyWith<$Res> {
  __$$Richtext_EmojiImplCopyWithImpl(
      _$Richtext_EmojiImpl _value, $Res Function(_$Richtext_EmojiImpl) _then)
      : super(_value, _then);

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? a = null,
    Object? u = null,
  }) {
    return _then(_$Richtext_EmojiImpl(
      a: null == a
          ? _value.a
          : a // ignore: cast_nullable_to_non_nullable
              as String,
      u: null == u
          ? _value.u
          : u // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$Richtext_EmojiImpl extends Richtext_Emoji {
  const _$Richtext_EmojiImpl({required this.a, required this.u}) : super._();

  @override
  final String a;
  @override
  final String u;

  @override
  String toString() {
    return 'Richtext.emoji(a: $a, u: $u)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$Richtext_EmojiImpl &&
            (identical(other.a, a) || other.a == a) &&
            (identical(other.u, u) || other.u == u));
  }

  @override
  int get hashCode => Object.hash(runtimeType, a, u);

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$Richtext_EmojiImplCopyWith<_$Richtext_EmojiImpl> get copyWith =>
      __$$Richtext_EmojiImplCopyWithImpl<_$Richtext_EmojiImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(String t) text,
    required TResult Function(String a, String u) emoji,
  }) {
    return emoji(a, u);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(String t)? text,
    TResult? Function(String a, String u)? emoji,
  }) {
    return emoji?.call(a, u);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(String t)? text,
    TResult Function(String a, String u)? emoji,
    required TResult orElse(),
  }) {
    if (emoji != null) {
      return emoji(a, u);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Richtext_Text value) text,
    required TResult Function(Richtext_Emoji value) emoji,
  }) {
    return emoji(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Richtext_Text value)? text,
    TResult? Function(Richtext_Emoji value)? emoji,
  }) {
    return emoji?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Richtext_Text value)? text,
    TResult Function(Richtext_Emoji value)? emoji,
    required TResult orElse(),
  }) {
    if (emoji != null) {
      return emoji(this);
    }
    return orElse();
  }
}

abstract class Richtext_Emoji extends Richtext {
  const factory Richtext_Emoji(
      {required final String a,
      required final String u}) = _$Richtext_EmojiImpl;
  const Richtext_Emoji._() : super._();

  String get a;
  String get u;

  /// Create a copy of Richtext
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$Richtext_EmojiImplCopyWith<_$Richtext_EmojiImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
