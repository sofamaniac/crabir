// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
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

/// Adds pattern-matching-related methods to [Source].
extension SourcePatterns on Source {
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
    TResult Function(Source_Image value)? image,
    TResult Function(Source_AnimatedImage value)? animatedImage,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Source_Image() when image != null:
        return image(_that);
      case Source_AnimatedImage() when animatedImage != null:
        return animatedImage(_that);
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
    required TResult Function(Source_Image value) image,
    required TResult Function(Source_AnimatedImage value) animatedImage,
  }) {
    final _that = this;
    switch (_that) {
      case Source_Image():
        return image(_that);
      case Source_AnimatedImage():
        return animatedImage(_that);
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
    TResult? Function(Source_Image value)? image,
    TResult? Function(Source_AnimatedImage value)? animatedImage,
  }) {
    final _that = this;
    switch (_that) {
      case Source_Image() when image != null:
        return image(_that);
      case Source_AnimatedImage() when animatedImage != null:
        return animatedImage(_that);
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
    TResult Function(Image source)? image,
    TResult Function(AnimatedImage source)? animatedImage,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Source_Image() when image != null:
        return image(_that.source);
      case Source_AnimatedImage() when animatedImage != null:
        return animatedImage(_that.source);
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
    required TResult Function(Image source) image,
    required TResult Function(AnimatedImage source) animatedImage,
  }) {
    final _that = this;
    switch (_that) {
      case Source_Image():
        return image(_that.source);
      case Source_AnimatedImage():
        return animatedImage(_that.source);
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
    TResult? Function(Image source)? image,
    TResult? Function(AnimatedImage source)? animatedImage,
  }) {
    final _that = this;
    switch (_that) {
      case Source_Image() when image != null:
        return image(_that.source);
      case Source_AnimatedImage() when animatedImage != null:
        return animatedImage(_that.source);
      case _:
        return null;
    }
  }
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
