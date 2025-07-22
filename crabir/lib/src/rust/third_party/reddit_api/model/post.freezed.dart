// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'post.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$Media {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Media);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'Media()';
  }
}

/// @nodoc
class $MediaCopyWith<$Res> {
  $MediaCopyWith(Media _, $Res Function(Media) __);
}

/// @nodoc

class Media_RedditVideo extends Media {
  const Media_RedditVideo(this.field0) : super._();

  final RedditVideo field0;

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Media_RedditVideoCopyWith<Media_RedditVideo> get copyWith =>
      _$Media_RedditVideoCopyWithImpl<Media_RedditVideo>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Media_RedditVideo &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'Media.redditVideo(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $Media_RedditVideoCopyWith<$Res>
    implements $MediaCopyWith<$Res> {
  factory $Media_RedditVideoCopyWith(
          Media_RedditVideo value, $Res Function(Media_RedditVideo) _then) =
      _$Media_RedditVideoCopyWithImpl;
  @useResult
  $Res call({RedditVideo field0});
}

/// @nodoc
class _$Media_RedditVideoCopyWithImpl<$Res>
    implements $Media_RedditVideoCopyWith<$Res> {
  _$Media_RedditVideoCopyWithImpl(this._self, this._then);

  final Media_RedditVideo _self;
  final $Res Function(Media_RedditVideo) _then;

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(Media_RedditVideo(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as RedditVideo,
    ));
  }
}

/// @nodoc

class Media_Oembed extends Media {
  const Media_Oembed({required this.oembed, required this.typeField})
      : super._();

  final Oembed oembed;
  final String typeField;

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Media_OembedCopyWith<Media_Oembed> get copyWith =>
      _$Media_OembedCopyWithImpl<Media_Oembed>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Media_Oembed &&
            (identical(other.oembed, oembed) || other.oembed == oembed) &&
            (identical(other.typeField, typeField) ||
                other.typeField == typeField));
  }

  @override
  int get hashCode => Object.hash(runtimeType, oembed, typeField);

  @override
  String toString() {
    return 'Media.oembed(oembed: $oembed, typeField: $typeField)';
  }
}

/// @nodoc
abstract mixin class $Media_OembedCopyWith<$Res>
    implements $MediaCopyWith<$Res> {
  factory $Media_OembedCopyWith(
          Media_Oembed value, $Res Function(Media_Oembed) _then) =
      _$Media_OembedCopyWithImpl;
  @useResult
  $Res call({Oembed oembed, String typeField});
}

/// @nodoc
class _$Media_OembedCopyWithImpl<$Res> implements $Media_OembedCopyWith<$Res> {
  _$Media_OembedCopyWithImpl(this._self, this._then);

  final Media_Oembed _self;
  final $Res Function(Media_Oembed) _then;

  /// Create a copy of Media
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? oembed = null,
    Object? typeField = null,
  }) {
    return _then(Media_Oembed(
      oembed: null == oembed
          ? _self.oembed
          : oembed // ignore: cast_nullable_to_non_nullable
              as Oembed,
      typeField: null == typeField
          ? _self.typeField
          : typeField // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

// dart format on
