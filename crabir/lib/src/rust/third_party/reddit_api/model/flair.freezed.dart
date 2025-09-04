// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
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
