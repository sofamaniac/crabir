// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'data_settings.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$DataSettings {
  @Category(name: "Videos")
  @Setting(widget: ImageLoadingSelect)
  ImageLoading get autoplay;
  @Setting(widget: ResolutionSelect)
  Resolution get videoQualityWifi;
  @Setting(widget: ResolutionSelect)
  Resolution get videoQualityCellular;
  @Category(name: "Images")
  @Setting(widget: ImageLoadingSelect)
  ImageLoading get loadImages;
  @Setting(widget: ResolutionSelect)
  Resolution get imageQualityWifi;
  @Setting(widget: ResolutionSelect)
  Resolution get imageQualityCellular;

  /// Create a copy of DataSettings
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $DataSettingsCopyWith<DataSettings> get copyWith =>
      _$DataSettingsCopyWithImpl<DataSettings>(
          this as DataSettings, _$identity);

  /// Serializes this DataSettings to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is DataSettings &&
            (identical(other.autoplay, autoplay) ||
                other.autoplay == autoplay) &&
            (identical(other.videoQualityWifi, videoQualityWifi) ||
                other.videoQualityWifi == videoQualityWifi) &&
            (identical(other.videoQualityCellular, videoQualityCellular) ||
                other.videoQualityCellular == videoQualityCellular) &&
            (identical(other.loadImages, loadImages) ||
                other.loadImages == loadImages) &&
            (identical(other.imageQualityWifi, imageQualityWifi) ||
                other.imageQualityWifi == imageQualityWifi) &&
            (identical(other.imageQualityCellular, imageQualityCellular) ||
                other.imageQualityCellular == imageQualityCellular));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, autoplay, videoQualityWifi,
      videoQualityCellular, loadImages, imageQualityWifi, imageQualityCellular);

  @override
  String toString() {
    return 'DataSettings(autoplay: $autoplay, videoQualityWifi: $videoQualityWifi, videoQualityCellular: $videoQualityCellular, loadImages: $loadImages, imageQualityWifi: $imageQualityWifi, imageQualityCellular: $imageQualityCellular)';
  }
}

/// @nodoc
abstract mixin class $DataSettingsCopyWith<$Res> {
  factory $DataSettingsCopyWith(
          DataSettings value, $Res Function(DataSettings) _then) =
      _$DataSettingsCopyWithImpl;
  @useResult
  $Res call(
      {@Category(name: "Videos")
      @Setting(widget: ImageLoadingSelect)
      ImageLoading autoplay,
      @Setting(widget: ResolutionSelect) Resolution videoQualityWifi,
      @Setting(widget: ResolutionSelect) Resolution videoQualityCellular,
      @Category(name: "Images")
      @Setting(widget: ImageLoadingSelect)
      ImageLoading loadImages,
      @Setting(widget: ResolutionSelect) Resolution imageQualityWifi,
      @Setting(widget: ResolutionSelect) Resolution imageQualityCellular});
}

/// @nodoc
class _$DataSettingsCopyWithImpl<$Res> implements $DataSettingsCopyWith<$Res> {
  _$DataSettingsCopyWithImpl(this._self, this._then);

  final DataSettings _self;
  final $Res Function(DataSettings) _then;

  /// Create a copy of DataSettings
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? autoplay = null,
    Object? videoQualityWifi = null,
    Object? videoQualityCellular = null,
    Object? loadImages = null,
    Object? imageQualityWifi = null,
    Object? imageQualityCellular = null,
  }) {
    return _then(_self.copyWith(
      autoplay: null == autoplay
          ? _self.autoplay
          : autoplay // ignore: cast_nullable_to_non_nullable
              as ImageLoading,
      videoQualityWifi: null == videoQualityWifi
          ? _self.videoQualityWifi
          : videoQualityWifi // ignore: cast_nullable_to_non_nullable
              as Resolution,
      videoQualityCellular: null == videoQualityCellular
          ? _self.videoQualityCellular
          : videoQualityCellular // ignore: cast_nullable_to_non_nullable
              as Resolution,
      loadImages: null == loadImages
          ? _self.loadImages
          : loadImages // ignore: cast_nullable_to_non_nullable
              as ImageLoading,
      imageQualityWifi: null == imageQualityWifi
          ? _self.imageQualityWifi
          : imageQualityWifi // ignore: cast_nullable_to_non_nullable
              as Resolution,
      imageQualityCellular: null == imageQualityCellular
          ? _self.imageQualityCellular
          : imageQualityCellular // ignore: cast_nullable_to_non_nullable
              as Resolution,
    ));
  }
}

/// Adds pattern-matching-related methods to [DataSettings].
extension DataSettingsPatterns on DataSettings {
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
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_DataSettings value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _DataSettings() when $default != null:
        return $default(_that);
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
  TResult map<TResult extends Object?>(
    TResult Function(_DataSettings value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _DataSettings():
        return $default(_that);
      case _:
        throw StateError('Unexpected subclass');
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
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_DataSettings value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _DataSettings() when $default != null:
        return $default(_that);
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
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(
            @Category(name: "Videos")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading autoplay,
            @Setting(widget: ResolutionSelect) Resolution videoQualityWifi,
            @Setting(widget: ResolutionSelect) Resolution videoQualityCellular,
            @Category(name: "Images")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading loadImages,
            @Setting(widget: ResolutionSelect) Resolution imageQualityWifi,
            @Setting(widget: ResolutionSelect) Resolution imageQualityCellular)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _DataSettings() when $default != null:
        return $default(
            _that.autoplay,
            _that.videoQualityWifi,
            _that.videoQualityCellular,
            _that.loadImages,
            _that.imageQualityWifi,
            _that.imageQualityCellular);
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
  TResult when<TResult extends Object?>(
    TResult Function(
            @Category(name: "Videos")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading autoplay,
            @Setting(widget: ResolutionSelect) Resolution videoQualityWifi,
            @Setting(widget: ResolutionSelect) Resolution videoQualityCellular,
            @Category(name: "Images")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading loadImages,
            @Setting(widget: ResolutionSelect) Resolution imageQualityWifi,
            @Setting(widget: ResolutionSelect) Resolution imageQualityCellular)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _DataSettings():
        return $default(
            _that.autoplay,
            _that.videoQualityWifi,
            _that.videoQualityCellular,
            _that.loadImages,
            _that.imageQualityWifi,
            _that.imageQualityCellular);
      case _:
        throw StateError('Unexpected subclass');
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
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(
            @Category(name: "Videos")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading autoplay,
            @Setting(widget: ResolutionSelect) Resolution videoQualityWifi,
            @Setting(widget: ResolutionSelect) Resolution videoQualityCellular,
            @Category(name: "Images")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading loadImages,
            @Setting(widget: ResolutionSelect) Resolution imageQualityWifi,
            @Setting(widget: ResolutionSelect) Resolution imageQualityCellular)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _DataSettings() when $default != null:
        return $default(
            _that.autoplay,
            _that.videoQualityWifi,
            _that.videoQualityCellular,
            _that.loadImages,
            _that.imageQualityWifi,
            _that.imageQualityCellular);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _DataSettings extends DataSettings {
  _DataSettings(
      {@Category(name: "Videos")
      @Setting(widget: ImageLoadingSelect)
      this.autoplay = ImageLoading.always,
      @Setting(widget: ResolutionSelect)
      this.videoQualityWifi = Resolution.source,
      @Setting(widget: ResolutionSelect)
      this.videoQualityCellular = Resolution.source,
      @Category(name: "Images")
      @Setting(widget: ImageLoadingSelect)
      this.loadImages = ImageLoading.always,
      @Setting(widget: ResolutionSelect)
      this.imageQualityWifi = Resolution.source,
      @Setting(widget: ResolutionSelect)
      this.imageQualityCellular = Resolution.source})
      : super._();
  factory _DataSettings.fromJson(Map<String, dynamic> json) =>
      _$DataSettingsFromJson(json);

  @override
  @JsonKey()
  @Category(name: "Videos")
  @Setting(widget: ImageLoadingSelect)
  final ImageLoading autoplay;
  @override
  @JsonKey()
  @Setting(widget: ResolutionSelect)
  final Resolution videoQualityWifi;
  @override
  @JsonKey()
  @Setting(widget: ResolutionSelect)
  final Resolution videoQualityCellular;
  @override
  @JsonKey()
  @Category(name: "Images")
  @Setting(widget: ImageLoadingSelect)
  final ImageLoading loadImages;
  @override
  @JsonKey()
  @Setting(widget: ResolutionSelect)
  final Resolution imageQualityWifi;
  @override
  @JsonKey()
  @Setting(widget: ResolutionSelect)
  final Resolution imageQualityCellular;

  /// Create a copy of DataSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$DataSettingsCopyWith<_DataSettings> get copyWith =>
      __$DataSettingsCopyWithImpl<_DataSettings>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$DataSettingsToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _DataSettings &&
            (identical(other.autoplay, autoplay) ||
                other.autoplay == autoplay) &&
            (identical(other.videoQualityWifi, videoQualityWifi) ||
                other.videoQualityWifi == videoQualityWifi) &&
            (identical(other.videoQualityCellular, videoQualityCellular) ||
                other.videoQualityCellular == videoQualityCellular) &&
            (identical(other.loadImages, loadImages) ||
                other.loadImages == loadImages) &&
            (identical(other.imageQualityWifi, imageQualityWifi) ||
                other.imageQualityWifi == imageQualityWifi) &&
            (identical(other.imageQualityCellular, imageQualityCellular) ||
                other.imageQualityCellular == imageQualityCellular));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, autoplay, videoQualityWifi,
      videoQualityCellular, loadImages, imageQualityWifi, imageQualityCellular);

  @override
  String toString() {
    return 'DataSettings(autoplay: $autoplay, videoQualityWifi: $videoQualityWifi, videoQualityCellular: $videoQualityCellular, loadImages: $loadImages, imageQualityWifi: $imageQualityWifi, imageQualityCellular: $imageQualityCellular)';
  }
}

/// @nodoc
abstract mixin class _$DataSettingsCopyWith<$Res>
    implements $DataSettingsCopyWith<$Res> {
  factory _$DataSettingsCopyWith(
          _DataSettings value, $Res Function(_DataSettings) _then) =
      __$DataSettingsCopyWithImpl;
  @override
  @useResult
  $Res call(
      {@Category(name: "Videos")
      @Setting(widget: ImageLoadingSelect)
      ImageLoading autoplay,
      @Setting(widget: ResolutionSelect) Resolution videoQualityWifi,
      @Setting(widget: ResolutionSelect) Resolution videoQualityCellular,
      @Category(name: "Images")
      @Setting(widget: ImageLoadingSelect)
      ImageLoading loadImages,
      @Setting(widget: ResolutionSelect) Resolution imageQualityWifi,
      @Setting(widget: ResolutionSelect) Resolution imageQualityCellular});
}

/// @nodoc
class __$DataSettingsCopyWithImpl<$Res>
    implements _$DataSettingsCopyWith<$Res> {
  __$DataSettingsCopyWithImpl(this._self, this._then);

  final _DataSettings _self;
  final $Res Function(_DataSettings) _then;

  /// Create a copy of DataSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? autoplay = null,
    Object? videoQualityWifi = null,
    Object? videoQualityCellular = null,
    Object? loadImages = null,
    Object? imageQualityWifi = null,
    Object? imageQualityCellular = null,
  }) {
    return _then(_DataSettings(
      autoplay: null == autoplay
          ? _self.autoplay
          : autoplay // ignore: cast_nullable_to_non_nullable
              as ImageLoading,
      videoQualityWifi: null == videoQualityWifi
          ? _self.videoQualityWifi
          : videoQualityWifi // ignore: cast_nullable_to_non_nullable
              as Resolution,
      videoQualityCellular: null == videoQualityCellular
          ? _self.videoQualityCellular
          : videoQualityCellular // ignore: cast_nullable_to_non_nullable
              as Resolution,
      loadImages: null == loadImages
          ? _self.loadImages
          : loadImages // ignore: cast_nullable_to_non_nullable
              as ImageLoading,
      imageQualityWifi: null == imageQualityWifi
          ? _self.imageQualityWifi
          : imageQualityWifi // ignore: cast_nullable_to_non_nullable
              as Resolution,
      imageQualityCellular: null == imageQualityCellular
          ? _self.imageQualityCellular
          : imageQualityCellular // ignore: cast_nullable_to_non_nullable
              as Resolution,
    ));
  }
}

// dart format on
