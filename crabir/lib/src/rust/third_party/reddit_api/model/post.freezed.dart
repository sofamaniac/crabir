// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'post.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

/// @nodoc
mixin _$Media {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(RedditVideo field0) redditVideo,
    required TResult Function(Oembed oembed, String typeField) oembed,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(RedditVideo field0)? redditVideo,
    TResult? Function(Oembed oembed, String typeField)? oembed,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(RedditVideo field0)? redditVideo,
    TResult Function(Oembed oembed, String typeField)? oembed,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Media_RedditVideo value) redditVideo,
    required TResult Function(Media_Oembed value) oembed,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Media_RedditVideo value)? redditVideo,
    TResult? Function(Media_Oembed value)? oembed,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Media_RedditVideo value)? redditVideo,
    TResult Function(Media_Oembed value)? oembed,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $MediaCopyWith<$Res> {
  factory $MediaCopyWith(Media value, $Res Function(Media) then) =
      _$MediaCopyWithImpl<$Res, Media>;
}

/// @nodoc
class _$MediaCopyWithImpl<$Res, $Val extends Media>
    implements $MediaCopyWith<$Res> {
  _$MediaCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
}

/// @nodoc
abstract class _$$Media_RedditVideoImplCopyWith<$Res> {
  factory _$$Media_RedditVideoImplCopyWith(_$Media_RedditVideoImpl value,
          $Res Function(_$Media_RedditVideoImpl) then) =
      __$$Media_RedditVideoImplCopyWithImpl<$Res>;
  @useResult
  $Res call({RedditVideo field0});
}

/// @nodoc
class __$$Media_RedditVideoImplCopyWithImpl<$Res>
    extends _$MediaCopyWithImpl<$Res, _$Media_RedditVideoImpl>
    implements _$$Media_RedditVideoImplCopyWith<$Res> {
  __$$Media_RedditVideoImplCopyWithImpl(_$Media_RedditVideoImpl _value,
      $Res Function(_$Media_RedditVideoImpl) _then)
      : super(_value, _then);

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? field0 = null,
  }) {
    return _then(_$Media_RedditVideoImpl(
      null == field0
          ? _value.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as RedditVideo,
    ));
  }
}

/// @nodoc

class _$Media_RedditVideoImpl extends Media_RedditVideo {
  const _$Media_RedditVideoImpl(this.field0) : super._();

  @override
  final RedditVideo field0;

  @override
  String toString() {
    return 'Media.redditVideo(field0: $field0)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$Media_RedditVideoImpl &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$Media_RedditVideoImplCopyWith<_$Media_RedditVideoImpl> get copyWith =>
      __$$Media_RedditVideoImplCopyWithImpl<_$Media_RedditVideoImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(RedditVideo field0) redditVideo,
    required TResult Function(Oembed oembed, String typeField) oembed,
  }) {
    return redditVideo(field0);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(RedditVideo field0)? redditVideo,
    TResult? Function(Oembed oembed, String typeField)? oembed,
  }) {
    return redditVideo?.call(field0);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(RedditVideo field0)? redditVideo,
    TResult Function(Oembed oembed, String typeField)? oembed,
    required TResult orElse(),
  }) {
    if (redditVideo != null) {
      return redditVideo(field0);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Media_RedditVideo value) redditVideo,
    required TResult Function(Media_Oembed value) oembed,
  }) {
    return redditVideo(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Media_RedditVideo value)? redditVideo,
    TResult? Function(Media_Oembed value)? oembed,
  }) {
    return redditVideo?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Media_RedditVideo value)? redditVideo,
    TResult Function(Media_Oembed value)? oembed,
    required TResult orElse(),
  }) {
    if (redditVideo != null) {
      return redditVideo(this);
    }
    return orElse();
  }
}

abstract class Media_RedditVideo extends Media {
  const factory Media_RedditVideo(final RedditVideo field0) =
      _$Media_RedditVideoImpl;
  const Media_RedditVideo._() : super._();

  RedditVideo get field0;

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$Media_RedditVideoImplCopyWith<_$Media_RedditVideoImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$Media_OembedImplCopyWith<$Res> {
  factory _$$Media_OembedImplCopyWith(
          _$Media_OembedImpl value, $Res Function(_$Media_OembedImpl) then) =
      __$$Media_OembedImplCopyWithImpl<$Res>;
  @useResult
  $Res call({Oembed oembed, String typeField});
}

/// @nodoc
class __$$Media_OembedImplCopyWithImpl<$Res>
    extends _$MediaCopyWithImpl<$Res, _$Media_OembedImpl>
    implements _$$Media_OembedImplCopyWith<$Res> {
  __$$Media_OembedImplCopyWithImpl(
      _$Media_OembedImpl _value, $Res Function(_$Media_OembedImpl) _then)
      : super(_value, _then);

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? oembed = null,
    Object? typeField = null,
  }) {
    return _then(_$Media_OembedImpl(
      oembed: null == oembed
          ? _value.oembed
          : oembed // ignore: cast_nullable_to_non_nullable
              as Oembed,
      typeField: null == typeField
          ? _value.typeField
          : typeField // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$Media_OembedImpl extends Media_Oembed {
  const _$Media_OembedImpl({required this.oembed, required this.typeField})
      : super._();

  @override
  final Oembed oembed;
  @override
  final String typeField;

  @override
  String toString() {
    return 'Media.oembed(oembed: $oembed, typeField: $typeField)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$Media_OembedImpl &&
            (identical(other.oembed, oembed) || other.oembed == oembed) &&
            (identical(other.typeField, typeField) ||
                other.typeField == typeField));
  }

  @override
  int get hashCode => Object.hash(runtimeType, oembed, typeField);

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$Media_OembedImplCopyWith<_$Media_OembedImpl> get copyWith =>
      __$$Media_OembedImplCopyWithImpl<_$Media_OembedImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(RedditVideo field0) redditVideo,
    required TResult Function(Oembed oembed, String typeField) oembed,
  }) {
    return oembed(this.oembed, typeField);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(RedditVideo field0)? redditVideo,
    TResult? Function(Oembed oembed, String typeField)? oembed,
  }) {
    return oembed?.call(this.oembed, typeField);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(RedditVideo field0)? redditVideo,
    TResult Function(Oembed oembed, String typeField)? oembed,
    required TResult orElse(),
  }) {
    if (oembed != null) {
      return oembed(this.oembed, typeField);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Media_RedditVideo value) redditVideo,
    required TResult Function(Media_Oembed value) oembed,
  }) {
    return oembed(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Media_RedditVideo value)? redditVideo,
    TResult? Function(Media_Oembed value)? oembed,
  }) {
    return oembed?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Media_RedditVideo value)? redditVideo,
    TResult Function(Media_Oembed value)? oembed,
    required TResult orElse(),
  }) {
    if (oembed != null) {
      return oembed(this);
    }
    return orElse();
  }
}

abstract class Media_Oembed extends Media {
  const factory Media_Oembed(
      {required final Oembed oembed,
      required final String typeField}) = _$Media_OembedImpl;
  const Media_Oembed._() : super._();

  Oembed get oembed;
  String get typeField;

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$Media_OembedImplCopyWith<_$Media_OembedImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
