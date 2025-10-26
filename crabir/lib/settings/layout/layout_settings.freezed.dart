// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'layout_settings.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$LayoutSettings {
  @Setting(widget: _ViewKindSelection)
  ViewKind get defaultView;
  @Setting()
  bool get rememberByCommunity;
  @Setting(widget: _ManageViewButton)
  RememberedView get rememberedView;
  @Category()
  @Setting()
  () get font;
  @Setting()
  bool get showThumbnail;
  @Setting(dependsOn: "showThumbnail")
  bool get thumbnailOnLeft;
  @Setting()
  bool get prefixCommunities;
  @Category(name: "Cards")
  @Setting()
  bool get previewText;
  @Setting(dependsOn: "previewText", widget: _LengthSelection)
  int get previewTextLength;

  /// Create a copy of LayoutSettings
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $LayoutSettingsCopyWith<LayoutSettings> get copyWith =>
      _$LayoutSettingsCopyWithImpl<LayoutSettings>(
          this as LayoutSettings, _$identity);

  /// Serializes this LayoutSettings to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is LayoutSettings &&
            (identical(other.defaultView, defaultView) ||
                other.defaultView == defaultView) &&
            (identical(other.rememberByCommunity, rememberByCommunity) ||
                other.rememberByCommunity == rememberByCommunity) &&
            (identical(other.rememberedView, rememberedView) ||
                other.rememberedView == rememberedView) &&
            (identical(other.font, font) || other.font == font) &&
            (identical(other.showThumbnail, showThumbnail) ||
                other.showThumbnail == showThumbnail) &&
            (identical(other.thumbnailOnLeft, thumbnailOnLeft) ||
                other.thumbnailOnLeft == thumbnailOnLeft) &&
            (identical(other.prefixCommunities, prefixCommunities) ||
                other.prefixCommunities == prefixCommunities) &&
            (identical(other.previewText, previewText) ||
                other.previewText == previewText) &&
            (identical(other.previewTextLength, previewTextLength) ||
                other.previewTextLength == previewTextLength));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      defaultView,
      rememberByCommunity,
      rememberedView,
      font,
      showThumbnail,
      thumbnailOnLeft,
      prefixCommunities,
      previewText,
      previewTextLength);

  @override
  String toString() {
    return 'LayoutSettings(defaultView: $defaultView, rememberByCommunity: $rememberByCommunity, rememberedView: $rememberedView, font: $font, showThumbnail: $showThumbnail, thumbnailOnLeft: $thumbnailOnLeft, prefixCommunities: $prefixCommunities, previewText: $previewText, previewTextLength: $previewTextLength)';
  }
}

/// @nodoc
abstract mixin class $LayoutSettingsCopyWith<$Res> {
  factory $LayoutSettingsCopyWith(
          LayoutSettings value, $Res Function(LayoutSettings) _then) =
      _$LayoutSettingsCopyWithImpl;
  @useResult
  $Res call(
      {@Setting(widget: _ViewKindSelection) ViewKind defaultView,
      @Setting() bool rememberByCommunity,
      @Setting(widget: _ManageViewButton) RememberedView rememberedView,
      @Category() @Setting() () font,
      @Setting() bool showThumbnail,
      @Setting(dependsOn: "showThumbnail") bool thumbnailOnLeft,
      @Setting() bool prefixCommunities,
      @Category(name: "Cards") @Setting() bool previewText,
      @Setting(dependsOn: "previewText", widget: _LengthSelection)
      int previewTextLength});
}

/// @nodoc
class _$LayoutSettingsCopyWithImpl<$Res>
    implements $LayoutSettingsCopyWith<$Res> {
  _$LayoutSettingsCopyWithImpl(this._self, this._then);

  final LayoutSettings _self;
  final $Res Function(LayoutSettings) _then;

  /// Create a copy of LayoutSettings
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? defaultView = null,
    Object? rememberByCommunity = null,
    Object? rememberedView = null,
    Object? font = null,
    Object? showThumbnail = null,
    Object? thumbnailOnLeft = null,
    Object? prefixCommunities = null,
    Object? previewText = null,
    Object? previewTextLength = null,
  }) {
    return _then(_self.copyWith(
      defaultView: null == defaultView
          ? _self.defaultView
          : defaultView // ignore: cast_nullable_to_non_nullable
              as ViewKind,
      rememberByCommunity: null == rememberByCommunity
          ? _self.rememberByCommunity
          : rememberByCommunity // ignore: cast_nullable_to_non_nullable
              as bool,
      rememberedView: null == rememberedView
          ? _self.rememberedView
          : rememberedView // ignore: cast_nullable_to_non_nullable
              as RememberedView,
      font: null == font
          ? _self.font
          : font // ignore: cast_nullable_to_non_nullable
              as (),
      showThumbnail: null == showThumbnail
          ? _self.showThumbnail
          : showThumbnail // ignore: cast_nullable_to_non_nullable
              as bool,
      thumbnailOnLeft: null == thumbnailOnLeft
          ? _self.thumbnailOnLeft
          : thumbnailOnLeft // ignore: cast_nullable_to_non_nullable
              as bool,
      prefixCommunities: null == prefixCommunities
          ? _self.prefixCommunities
          : prefixCommunities // ignore: cast_nullable_to_non_nullable
              as bool,
      previewText: null == previewText
          ? _self.previewText
          : previewText // ignore: cast_nullable_to_non_nullable
              as bool,
      previewTextLength: null == previewTextLength
          ? _self.previewTextLength
          : previewTextLength // ignore: cast_nullable_to_non_nullable
              as int,
    ));
  }
}

/// Adds pattern-matching-related methods to [LayoutSettings].
extension LayoutSettingsPatterns on LayoutSettings {
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
    TResult Function(_LayoutSettings value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _LayoutSettings() when $default != null:
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
    TResult Function(_LayoutSettings value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _LayoutSettings():
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
    TResult? Function(_LayoutSettings value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _LayoutSettings() when $default != null:
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
            @Setting(widget: _ViewKindSelection) ViewKind defaultView,
            @Setting() bool rememberByCommunity,
            @Setting(widget: _ManageViewButton) RememberedView rememberedView,
            @Category() @Setting() () font,
            @Setting() bool showThumbnail,
            @Setting(dependsOn: "showThumbnail") bool thumbnailOnLeft,
            @Setting() bool prefixCommunities,
            @Category(name: "Cards") @Setting() bool previewText,
            @Setting(dependsOn: "previewText", widget: _LengthSelection)
            int previewTextLength)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _LayoutSettings() when $default != null:
        return $default(
            _that.defaultView,
            _that.rememberByCommunity,
            _that.rememberedView,
            _that.font,
            _that.showThumbnail,
            _that.thumbnailOnLeft,
            _that.prefixCommunities,
            _that.previewText,
            _that.previewTextLength);
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
            @Setting(widget: _ViewKindSelection) ViewKind defaultView,
            @Setting() bool rememberByCommunity,
            @Setting(widget: _ManageViewButton) RememberedView rememberedView,
            @Category() @Setting() () font,
            @Setting() bool showThumbnail,
            @Setting(dependsOn: "showThumbnail") bool thumbnailOnLeft,
            @Setting() bool prefixCommunities,
            @Category(name: "Cards") @Setting() bool previewText,
            @Setting(dependsOn: "previewText", widget: _LengthSelection)
            int previewTextLength)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _LayoutSettings():
        return $default(
            _that.defaultView,
            _that.rememberByCommunity,
            _that.rememberedView,
            _that.font,
            _that.showThumbnail,
            _that.thumbnailOnLeft,
            _that.prefixCommunities,
            _that.previewText,
            _that.previewTextLength);
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
            @Setting(widget: _ViewKindSelection) ViewKind defaultView,
            @Setting() bool rememberByCommunity,
            @Setting(widget: _ManageViewButton) RememberedView rememberedView,
            @Category() @Setting() () font,
            @Setting() bool showThumbnail,
            @Setting(dependsOn: "showThumbnail") bool thumbnailOnLeft,
            @Setting() bool prefixCommunities,
            @Category(name: "Cards") @Setting() bool previewText,
            @Setting(dependsOn: "previewText", widget: _LengthSelection)
            int previewTextLength)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _LayoutSettings() when $default != null:
        return $default(
            _that.defaultView,
            _that.rememberByCommunity,
            _that.rememberedView,
            _that.font,
            _that.showThumbnail,
            _that.thumbnailOnLeft,
            _that.prefixCommunities,
            _that.previewText,
            _that.previewTextLength);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _LayoutSettings extends LayoutSettings {
  _LayoutSettings(
      {@Setting(widget: _ViewKindSelection) this.defaultView = ViewKind.card,
      @Setting() this.rememberByCommunity = false,
      @Setting(widget: _ManageViewButton)
      this.rememberedView = const RememberedView(),
      @Category() @Setting() this.font = const (),
      @Setting() this.showThumbnail = true,
      @Setting(dependsOn: "showThumbnail") this.thumbnailOnLeft = false,
      @Setting() this.prefixCommunities = false,
      @Category(name: "Cards") @Setting() this.previewText = true,
      @Setting(dependsOn: "previewText", widget: _LengthSelection)
      this.previewTextLength = 5})
      : super._();
  factory _LayoutSettings.fromJson(Map<String, dynamic> json) =>
      _$LayoutSettingsFromJson(json);

  @override
  @JsonKey()
  @Setting(widget: _ViewKindSelection)
  final ViewKind defaultView;
  @override
  @JsonKey()
  @Setting()
  final bool rememberByCommunity;
  @override
  @JsonKey()
  @Setting(widget: _ManageViewButton)
  final RememberedView rememberedView;
  @override
  @JsonKey()
  @Category()
  @Setting()
  final () font;
  @override
  @JsonKey()
  @Setting()
  final bool showThumbnail;
  @override
  @JsonKey()
  @Setting(dependsOn: "showThumbnail")
  final bool thumbnailOnLeft;
  @override
  @JsonKey()
  @Setting()
  final bool prefixCommunities;
  @override
  @JsonKey()
  @Category(name: "Cards")
  @Setting()
  final bool previewText;
  @override
  @JsonKey()
  @Setting(dependsOn: "previewText", widget: _LengthSelection)
  final int previewTextLength;

  /// Create a copy of LayoutSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$LayoutSettingsCopyWith<_LayoutSettings> get copyWith =>
      __$LayoutSettingsCopyWithImpl<_LayoutSettings>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$LayoutSettingsToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _LayoutSettings &&
            (identical(other.defaultView, defaultView) ||
                other.defaultView == defaultView) &&
            (identical(other.rememberByCommunity, rememberByCommunity) ||
                other.rememberByCommunity == rememberByCommunity) &&
            (identical(other.rememberedView, rememberedView) ||
                other.rememberedView == rememberedView) &&
            (identical(other.font, font) || other.font == font) &&
            (identical(other.showThumbnail, showThumbnail) ||
                other.showThumbnail == showThumbnail) &&
            (identical(other.thumbnailOnLeft, thumbnailOnLeft) ||
                other.thumbnailOnLeft == thumbnailOnLeft) &&
            (identical(other.prefixCommunities, prefixCommunities) ||
                other.prefixCommunities == prefixCommunities) &&
            (identical(other.previewText, previewText) ||
                other.previewText == previewText) &&
            (identical(other.previewTextLength, previewTextLength) ||
                other.previewTextLength == previewTextLength));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      defaultView,
      rememberByCommunity,
      rememberedView,
      font,
      showThumbnail,
      thumbnailOnLeft,
      prefixCommunities,
      previewText,
      previewTextLength);

  @override
  String toString() {
    return 'LayoutSettings(defaultView: $defaultView, rememberByCommunity: $rememberByCommunity, rememberedView: $rememberedView, font: $font, showThumbnail: $showThumbnail, thumbnailOnLeft: $thumbnailOnLeft, prefixCommunities: $prefixCommunities, previewText: $previewText, previewTextLength: $previewTextLength)';
  }
}

/// @nodoc
abstract mixin class _$LayoutSettingsCopyWith<$Res>
    implements $LayoutSettingsCopyWith<$Res> {
  factory _$LayoutSettingsCopyWith(
          _LayoutSettings value, $Res Function(_LayoutSettings) _then) =
      __$LayoutSettingsCopyWithImpl;
  @override
  @useResult
  $Res call(
      {@Setting(widget: _ViewKindSelection) ViewKind defaultView,
      @Setting() bool rememberByCommunity,
      @Setting(widget: _ManageViewButton) RememberedView rememberedView,
      @Category() @Setting() () font,
      @Setting() bool showThumbnail,
      @Setting(dependsOn: "showThumbnail") bool thumbnailOnLeft,
      @Setting() bool prefixCommunities,
      @Category(name: "Cards") @Setting() bool previewText,
      @Setting(dependsOn: "previewText", widget: _LengthSelection)
      int previewTextLength});
}

/// @nodoc
class __$LayoutSettingsCopyWithImpl<$Res>
    implements _$LayoutSettingsCopyWith<$Res> {
  __$LayoutSettingsCopyWithImpl(this._self, this._then);

  final _LayoutSettings _self;
  final $Res Function(_LayoutSettings) _then;

  /// Create a copy of LayoutSettings
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? defaultView = null,
    Object? rememberByCommunity = null,
    Object? rememberedView = null,
    Object? font = null,
    Object? showThumbnail = null,
    Object? thumbnailOnLeft = null,
    Object? prefixCommunities = null,
    Object? previewText = null,
    Object? previewTextLength = null,
  }) {
    return _then(_LayoutSettings(
      defaultView: null == defaultView
          ? _self.defaultView
          : defaultView // ignore: cast_nullable_to_non_nullable
              as ViewKind,
      rememberByCommunity: null == rememberByCommunity
          ? _self.rememberByCommunity
          : rememberByCommunity // ignore: cast_nullable_to_non_nullable
              as bool,
      rememberedView: null == rememberedView
          ? _self.rememberedView
          : rememberedView // ignore: cast_nullable_to_non_nullable
              as RememberedView,
      font: null == font
          ? _self.font
          : font // ignore: cast_nullable_to_non_nullable
              as (),
      showThumbnail: null == showThumbnail
          ? _self.showThumbnail
          : showThumbnail // ignore: cast_nullable_to_non_nullable
              as bool,
      thumbnailOnLeft: null == thumbnailOnLeft
          ? _self.thumbnailOnLeft
          : thumbnailOnLeft // ignore: cast_nullable_to_non_nullable
              as bool,
      prefixCommunities: null == prefixCommunities
          ? _self.prefixCommunities
          : prefixCommunities // ignore: cast_nullable_to_non_nullable
              as bool,
      previewText: null == previewText
          ? _self.previewText
          : previewText // ignore: cast_nullable_to_non_nullable
              as bool,
      previewTextLength: null == previewTextLength
          ? _self.previewTextLength
          : previewTextLength // ignore: cast_nullable_to_non_nullable
              as int,
    ));
  }
}

// dart format on
