// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
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

/// Adds pattern-matching-related methods to [Media].
extension MediaPatterns on Media {
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
    TResult Function(Media_RedditVideo value)? redditVideo,
    TResult Function(Media_Oembed value)? oembed,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Media_RedditVideo() when redditVideo != null:
        return redditVideo(_that);
      case Media_Oembed() when oembed != null:
        return oembed(_that);
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
    required TResult Function(Media_RedditVideo value) redditVideo,
    required TResult Function(Media_Oembed value) oembed,
  }) {
    final _that = this;
    switch (_that) {
      case Media_RedditVideo():
        return redditVideo(_that);
      case Media_Oembed():
        return oembed(_that);
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
    TResult? Function(Media_RedditVideo value)? redditVideo,
    TResult? Function(Media_Oembed value)? oembed,
  }) {
    final _that = this;
    switch (_that) {
      case Media_RedditVideo() when redditVideo != null:
        return redditVideo(_that);
      case Media_Oembed() when oembed != null:
        return oembed(_that);
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
    TResult Function(RedditVideo field0)? redditVideo,
    TResult Function(Oembed oembed, String typeField)? oembed,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Media_RedditVideo() when redditVideo != null:
        return redditVideo(_that.field0);
      case Media_Oembed() when oembed != null:
        return oembed(_that.oembed, _that.typeField);
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
    required TResult Function(RedditVideo field0) redditVideo,
    required TResult Function(Oembed oembed, String typeField) oembed,
  }) {
    final _that = this;
    switch (_that) {
      case Media_RedditVideo():
        return redditVideo(_that.field0);
      case Media_Oembed():
        return oembed(_that.oembed, _that.typeField);
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
    TResult? Function(RedditVideo field0)? redditVideo,
    TResult? Function(Oembed oembed, String typeField)? oembed,
  }) {
    final _that = this;
    switch (_that) {
      case Media_RedditVideo() when redditVideo != null:
        return redditVideo(_that.field0);
      case Media_Oembed() when oembed != null:
        return oembed(_that.oembed, _that.typeField);
      case _:
        return null;
    }
  }
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
