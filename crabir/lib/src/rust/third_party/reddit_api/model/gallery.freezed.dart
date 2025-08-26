// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'gallery.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$Source {
  Object get source;

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Source &&
            const DeepCollectionEquality().equals(other.source, source));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(source));

  @override
  String toString() {
    return 'Source(source: $source)';
  }
}

/// @nodoc
class $SourceCopyWith<$Res> {
  $SourceCopyWith(Source _, $Res Function(Source) __);
}

/// @nodoc

class Source_Image extends Source {
  const Source_Image({required this.source}) : super._();

  @override
  final Image source;

  /// Create a copy of Source
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Source_ImageCopyWith<Source_Image> get copyWith =>
      _$Source_ImageCopyWithImpl<Source_Image>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Source_Image &&
            (identical(other.source, source) || other.source == source));
  }

  @override
  int get hashCode => Object.hash(runtimeType, source);

  @override
  String toString() {
    return 'Source.image(source: $source)';
  }
}

/// @nodoc
abstract mixin class $Source_ImageCopyWith<$Res>
    implements $SourceCopyWith<$Res> {
  factory $Source_ImageCopyWith(
          Source_Image value, $Res Function(Source_Image) _then) =
      _$Source_ImageCopyWithImpl;
  @useResult
  $Res call({Image source});
}

/// @nodoc
class _$Source_ImageCopyWithImpl<$Res> implements $Source_ImageCopyWith<$Res> {
  _$Source_ImageCopyWithImpl(this._self, this._then);

  final Source_Image _self;
  final $Res Function(Source_Image) _then;

  /// Create a copy of Source
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? source = null,
  }) {
    return _then(Source_Image(
      source: null == source
          ? _self.source
          : source // ignore: cast_nullable_to_non_nullable
              as Image,
    ));
  }
}

/// @nodoc

class Source_AnimatedImage extends Source {
  const Source_AnimatedImage({required this.source}) : super._();

  @override
  final AnimatedImage source;

  /// Create a copy of Source
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Source_AnimatedImageCopyWith<Source_AnimatedImage> get copyWith =>
      _$Source_AnimatedImageCopyWithImpl<Source_AnimatedImage>(
          this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Source_AnimatedImage &&
            (identical(other.source, source) || other.source == source));
  }

  @override
  int get hashCode => Object.hash(runtimeType, source);

  @override
  String toString() {
    return 'Source.animatedImage(source: $source)';
  }
}

/// @nodoc
abstract mixin class $Source_AnimatedImageCopyWith<$Res>
    implements $SourceCopyWith<$Res> {
  factory $Source_AnimatedImageCopyWith(Source_AnimatedImage value,
          $Res Function(Source_AnimatedImage) _then) =
      _$Source_AnimatedImageCopyWithImpl;
  @useResult
  $Res call({AnimatedImage source});
}

/// @nodoc
class _$Source_AnimatedImageCopyWithImpl<$Res>
    implements $Source_AnimatedImageCopyWith<$Res> {
  _$Source_AnimatedImageCopyWithImpl(this._self, this._then);

  final Source_AnimatedImage _self;
  final $Res Function(Source_AnimatedImage) _then;

  /// Create a copy of Source
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? source = null,
  }) {
    return _then(Source_AnimatedImage(
      source: null == source
          ? _self.source
          : source // ignore: cast_nullable_to_non_nullable
              as AnimatedImage,
    ));
  }
}

// dart format on
