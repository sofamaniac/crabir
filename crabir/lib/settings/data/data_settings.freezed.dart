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
  @Category(name: "Data Saver")
  @Setting(hasDescription: true)
  bool get mobileDataSaver;
  @Setting(hasDescription: true)
  bool get wifiDataSaver;
  @Category(name: "Videos")
  @Setting(widget: ImageLoadingSelect)
  ImageLoading get autoplay;
  @Setting()
  Resolution get videoQuality;
  @Setting()
  Resolution get minimumQuality;
  @Setting()
  Resolution get maximumQuality;
  @Category(name: "Images")
  @Setting(widget: ImageLoadingSelect)
  ImageLoading get loadImages;
  @Setting()
  Resolution get preferredQuality;

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
            (identical(other.mobileDataSaver, mobileDataSaver) ||
                other.mobileDataSaver == mobileDataSaver) &&
            (identical(other.wifiDataSaver, wifiDataSaver) ||
                other.wifiDataSaver == wifiDataSaver) &&
            (identical(other.autoplay, autoplay) ||
                other.autoplay == autoplay) &&
            (identical(other.videoQuality, videoQuality) ||
                other.videoQuality == videoQuality) &&
            (identical(other.minimumQuality, minimumQuality) ||
                other.minimumQuality == minimumQuality) &&
            (identical(other.maximumQuality, maximumQuality) ||
                other.maximumQuality == maximumQuality) &&
            (identical(other.loadImages, loadImages) ||
                other.loadImages == loadImages) &&
            (identical(other.preferredQuality, preferredQuality) ||
                other.preferredQuality == preferredQuality));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      mobileDataSaver,
      wifiDataSaver,
      autoplay,
      videoQuality,
      minimumQuality,
      maximumQuality,
      loadImages,
      preferredQuality);

  @override
  String toString() {
    return 'DataSettings(mobileDataSaver: $mobileDataSaver, wifiDataSaver: $wifiDataSaver, autoplay: $autoplay, videoQuality: $videoQuality, minimumQuality: $minimumQuality, maximumQuality: $maximumQuality, loadImages: $loadImages, preferredQuality: $preferredQuality)';
  }
}

/// @nodoc
abstract mixin class $DataSettingsCopyWith<$Res> {
  factory $DataSettingsCopyWith(
          DataSettings value, $Res Function(DataSettings) _then) =
      _$DataSettingsCopyWithImpl;
  @useResult
  $Res call(
      {@Category(name: "Data Saver")
      @Setting(hasDescription: true)
      bool mobileDataSaver,
      @Setting(hasDescription: true) bool wifiDataSaver,
      @Category(name: "Videos")
      @Setting(widget: ImageLoadingSelect)
      ImageLoading autoplay,
      @Setting() Resolution videoQuality,
      @Setting() Resolution minimumQuality,
      @Setting() Resolution maximumQuality,
      @Category(name: "Images")
      @Setting(widget: ImageLoadingSelect)
      ImageLoading loadImages,
      @Setting() Resolution preferredQuality});
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
    Object? mobileDataSaver = null,
    Object? wifiDataSaver = null,
    Object? autoplay = null,
    Object? videoQuality = null,
    Object? minimumQuality = null,
    Object? maximumQuality = null,
    Object? loadImages = null,
    Object? preferredQuality = null,
  }) {
    return _then(_self.copyWith(
      mobileDataSaver: null == mobileDataSaver
          ? _self.mobileDataSaver
          : mobileDataSaver // ignore: cast_nullable_to_non_nullable
              as bool,
      wifiDataSaver: null == wifiDataSaver
          ? _self.wifiDataSaver
          : wifiDataSaver // ignore: cast_nullable_to_non_nullable
              as bool,
      autoplay: null == autoplay
          ? _self.autoplay
          : autoplay // ignore: cast_nullable_to_non_nullable
              as ImageLoading,
      videoQuality: null == videoQuality
          ? _self.videoQuality
          : videoQuality // ignore: cast_nullable_to_non_nullable
              as Resolution,
      minimumQuality: null == minimumQuality
          ? _self.minimumQuality
          : minimumQuality // ignore: cast_nullable_to_non_nullable
              as Resolution,
      maximumQuality: null == maximumQuality
          ? _self.maximumQuality
          : maximumQuality // ignore: cast_nullable_to_non_nullable
              as Resolution,
      loadImages: null == loadImages
          ? _self.loadImages
          : loadImages // ignore: cast_nullable_to_non_nullable
              as ImageLoading,
      preferredQuality: null == preferredQuality
          ? _self.preferredQuality
          : preferredQuality // ignore: cast_nullable_to_non_nullable
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
            @Category(name: "Data Saver")
            @Setting(hasDescription: true)
            bool mobileDataSaver,
            @Setting(hasDescription: true) bool wifiDataSaver,
            @Category(name: "Videos")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading autoplay,
            @Setting() Resolution videoQuality,
            @Setting() Resolution minimumQuality,
            @Setting() Resolution maximumQuality,
            @Category(name: "Images")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading loadImages,
            @Setting() Resolution preferredQuality)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _DataSettings() when $default != null:
        return $default(
            _that.mobileDataSaver,
            _that.wifiDataSaver,
            _that.autoplay,
            _that.videoQuality,
            _that.minimumQuality,
            _that.maximumQuality,
            _that.loadImages,
            _that.preferredQuality);
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
            @Category(name: "Data Saver")
            @Setting(hasDescription: true)
            bool mobileDataSaver,
            @Setting(hasDescription: true) bool wifiDataSaver,
            @Category(name: "Videos")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading autoplay,
            @Setting() Resolution videoQuality,
            @Setting() Resolution minimumQuality,
            @Setting() Resolution maximumQuality,
            @Category(name: "Images")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading loadImages,
            @Setting() Resolution preferredQuality)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _DataSettings():
        return $default(
            _that.mobileDataSaver,
            _that.wifiDataSaver,
            _that.autoplay,
            _that.videoQuality,
            _that.minimumQuality,
            _that.maximumQuality,
            _that.loadImages,
            _that.preferredQuality);
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
            @Category(name: "Data Saver")
            @Setting(hasDescription: true)
            bool mobileDataSaver,
            @Setting(hasDescription: true) bool wifiDataSaver,
            @Category(name: "Videos")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading autoplay,
            @Setting() Resolution videoQuality,
            @Setting() Resolution minimumQuality,
            @Setting() Resolution maximumQuality,
            @Category(name: "Images")
            @Setting(widget: ImageLoadingSelect)
            ImageLoading loadImages,
            @Setting() Resolution preferredQuality)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _DataSettings() when $default != null:
        return $default(
            _that.mobileDataSaver,
            _that.wifiDataSaver,
            _that.autoplay,
            _that.videoQuality,
            _that.minimumQuality,
            _that.maximumQuality,
            _that.loadImages,
            _that.preferredQuality);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _DataSettings extends DataSettings {
  _DataSettings(
      {@Category(name: "Data Saver")
      @Setting(hasDescription: true)
      this.mobileDataSaver = false,
      @Setting(hasDescription: true) this.wifiDataSaver = false,
      @Category(name: "Videos")
      @Setting(widget: ImageLoadingSelect)
      this.autoplay = ImageLoading.always,
      @Setting() this.videoQuality = Resolution.source,
      @Setting() this.minimumQuality = Resolution.source,
      @Setting() this.maximumQuality = Resolution.source,
      @Category(name: "Images")
      @Setting(widget: ImageLoadingSelect)
      this.loadImages = ImageLoading.always,
      @Setting() this.preferredQuality = Resolution.source})
      : super._();
  factory _DataSettings.fromJson(Map<String, dynamic> json) =>
      _$DataSettingsFromJson(json);

  @override
  @JsonKey()
  @Category(name: "Data Saver")
  @Setting(hasDescription: true)
  final bool mobileDataSaver;
  @override
  @JsonKey()
  @Setting(hasDescription: true)
  final bool wifiDataSaver;
  @override
  @JsonKey()
  @Category(name: "Videos")
  @Setting(widget: ImageLoadingSelect)
  final ImageLoading autoplay;
  @override
  @JsonKey()
  @Setting()
  final Resolution videoQuality;
  @override
  @JsonKey()
  @Setting()
  final Resolution minimumQuality;
  @override
  @JsonKey()
  @Setting()
  final Resolution maximumQuality;
  @override
  @JsonKey()
  @Category(name: "Images")
  @Setting(widget: ImageLoadingSelect)
  final ImageLoading loadImages;
  @override
  @JsonKey()
  @Setting()
  final Resolution preferredQuality;

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
            (identical(other.mobileDataSaver, mobileDataSaver) ||
                other.mobileDataSaver == mobileDataSaver) &&
            (identical(other.wifiDataSaver, wifiDataSaver) ||
                other.wifiDataSaver == wifiDataSaver) &&
            (identical(other.autoplay, autoplay) ||
                other.autoplay == autoplay) &&
            (identical(other.videoQuality, videoQuality) ||
                other.videoQuality == videoQuality) &&
            (identical(other.minimumQuality, minimumQuality) ||
                other.minimumQuality == minimumQuality) &&
            (identical(other.maximumQuality, maximumQuality) ||
                other.maximumQuality == maximumQuality) &&
            (identical(other.loadImages, loadImages) ||
                other.loadImages == loadImages) &&
            (identical(other.preferredQuality, preferredQuality) ||
                other.preferredQuality == preferredQuality));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      mobileDataSaver,
      wifiDataSaver,
      autoplay,
      videoQuality,
      minimumQuality,
      maximumQuality,
      loadImages,
      preferredQuality);

  @override
  String toString() {
    return 'DataSettings(mobileDataSaver: $mobileDataSaver, wifiDataSaver: $wifiDataSaver, autoplay: $autoplay, videoQuality: $videoQuality, minimumQuality: $minimumQuality, maximumQuality: $maximumQuality, loadImages: $loadImages, preferredQuality: $preferredQuality)';
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
      {@Category(name: "Data Saver")
      @Setting(hasDescription: true)
      bool mobileDataSaver,
      @Setting(hasDescription: true) bool wifiDataSaver,
      @Category(name: "Videos")
      @Setting(widget: ImageLoadingSelect)
      ImageLoading autoplay,
      @Setting() Resolution videoQuality,
      @Setting() Resolution minimumQuality,
      @Setting() Resolution maximumQuality,
      @Category(name: "Images")
      @Setting(widget: ImageLoadingSelect)
      ImageLoading loadImages,
      @Setting() Resolution preferredQuality});
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
    Object? mobileDataSaver = null,
    Object? wifiDataSaver = null,
    Object? autoplay = null,
    Object? videoQuality = null,
    Object? minimumQuality = null,
    Object? maximumQuality = null,
    Object? loadImages = null,
    Object? preferredQuality = null,
  }) {
    return _then(_DataSettings(
      mobileDataSaver: null == mobileDataSaver
          ? _self.mobileDataSaver
          : mobileDataSaver // ignore: cast_nullable_to_non_nullable
              as bool,
      wifiDataSaver: null == wifiDataSaver
          ? _self.wifiDataSaver
          : wifiDataSaver // ignore: cast_nullable_to_non_nullable
              as bool,
      autoplay: null == autoplay
          ? _self.autoplay
          : autoplay // ignore: cast_nullable_to_non_nullable
              as ImageLoading,
      videoQuality: null == videoQuality
          ? _self.videoQuality
          : videoQuality // ignore: cast_nullable_to_non_nullable
              as Resolution,
      minimumQuality: null == minimumQuality
          ? _self.minimumQuality
          : minimumQuality // ignore: cast_nullable_to_non_nullable
              as Resolution,
      maximumQuality: null == maximumQuality
          ? _self.maximumQuality
          : maximumQuality // ignore: cast_nullable_to_non_nullable
              as Resolution,
      loadImages: null == loadImages
          ? _self.loadImages
          : loadImages // ignore: cast_nullable_to_non_nullable
              as ImageLoading,
      preferredQuality: null == preferredQuality
          ? _self.preferredQuality
          : preferredQuality // ignore: cast_nullable_to_non_nullable
              as Resolution,
    ));
  }
}

// dart format on
