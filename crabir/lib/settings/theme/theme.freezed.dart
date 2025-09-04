// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'theme.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$CrabirTheme {
  /// Color of the foundation
  @ColorConverter()
  Color get background;
  @ColorConverter()
  Color get cardBackground;
  @ColorConverter()
  Color get toolBarBackground;
  @ColorConverter()
  Color get toolBarText;

  /// Buttons and widgets
  @ColorConverter()
  Color get primaryColor;

  /// Communities and usernames
  @ColorConverter()
  Color get highlight;
  @ColorConverter()
  Color get postTitle;
  @ColorConverter()
  Color get readPost;
  @ColorConverter()
  Color get announcement;
  @ColorConverter()
  Color get contentText;
  @ColorConverter()
  Color get linkColor;

  /// Create a copy of CrabirTheme
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $CrabirThemeCopyWith<CrabirTheme> get copyWith =>
      _$CrabirThemeCopyWithImpl<CrabirTheme>(this as CrabirTheme, _$identity);

  /// Serializes this CrabirTheme to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is CrabirTheme &&
            const DeepCollectionEquality()
                .equals(other.background, background) &&
            const DeepCollectionEquality()
                .equals(other.cardBackground, cardBackground) &&
            const DeepCollectionEquality()
                .equals(other.toolBarBackground, toolBarBackground) &&
            const DeepCollectionEquality()
                .equals(other.toolBarText, toolBarText) &&
            const DeepCollectionEquality()
                .equals(other.primaryColor, primaryColor) &&
            const DeepCollectionEquality().equals(other.highlight, highlight) &&
            const DeepCollectionEquality().equals(other.postTitle, postTitle) &&
            const DeepCollectionEquality().equals(other.readPost, readPost) &&
            const DeepCollectionEquality()
                .equals(other.announcement, announcement) &&
            const DeepCollectionEquality()
                .equals(other.contentText, contentText) &&
            const DeepCollectionEquality().equals(other.linkColor, linkColor));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(background),
      const DeepCollectionEquality().hash(cardBackground),
      const DeepCollectionEquality().hash(toolBarBackground),
      const DeepCollectionEquality().hash(toolBarText),
      const DeepCollectionEquality().hash(primaryColor),
      const DeepCollectionEquality().hash(highlight),
      const DeepCollectionEquality().hash(postTitle),
      const DeepCollectionEquality().hash(readPost),
      const DeepCollectionEquality().hash(announcement),
      const DeepCollectionEquality().hash(contentText),
      const DeepCollectionEquality().hash(linkColor));

  @override
  String toString() {
    return 'CrabirTheme(background: $background, cardBackground: $cardBackground, toolBarBackground: $toolBarBackground, toolBarText: $toolBarText, primaryColor: $primaryColor, highlight: $highlight, postTitle: $postTitle, readPost: $readPost, announcement: $announcement, contentText: $contentText, linkColor: $linkColor)';
  }
}

/// @nodoc
abstract mixin class $CrabirThemeCopyWith<$Res> {
  factory $CrabirThemeCopyWith(
          CrabirTheme value, $Res Function(CrabirTheme) _then) =
      _$CrabirThemeCopyWithImpl;
  @useResult
  $Res call(
      {@ColorConverter() Color background,
      @ColorConverter() Color cardBackground,
      @ColorConverter() Color toolBarBackground,
      @ColorConverter() Color toolBarText,
      @ColorConverter() Color primaryColor,
      @ColorConverter() Color highlight,
      @ColorConverter() Color postTitle,
      @ColorConverter() Color readPost,
      @ColorConverter() Color announcement,
      @ColorConverter() Color contentText,
      @ColorConverter() Color linkColor});
}

/// @nodoc
class _$CrabirThemeCopyWithImpl<$Res> implements $CrabirThemeCopyWith<$Res> {
  _$CrabirThemeCopyWithImpl(this._self, this._then);

  final CrabirTheme _self;
  final $Res Function(CrabirTheme) _then;

  /// Create a copy of CrabirTheme
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? background = freezed,
    Object? cardBackground = freezed,
    Object? toolBarBackground = freezed,
    Object? toolBarText = freezed,
    Object? primaryColor = freezed,
    Object? highlight = freezed,
    Object? postTitle = freezed,
    Object? readPost = freezed,
    Object? announcement = freezed,
    Object? contentText = freezed,
    Object? linkColor = freezed,
  }) {
    return _then(_self.copyWith(
      background: freezed == background
          ? _self.background
          : background // ignore: cast_nullable_to_non_nullable
              as Color,
      cardBackground: freezed == cardBackground
          ? _self.cardBackground
          : cardBackground // ignore: cast_nullable_to_non_nullable
              as Color,
      toolBarBackground: freezed == toolBarBackground
          ? _self.toolBarBackground
          : toolBarBackground // ignore: cast_nullable_to_non_nullable
              as Color,
      toolBarText: freezed == toolBarText
          ? _self.toolBarText
          : toolBarText // ignore: cast_nullable_to_non_nullable
              as Color,
      primaryColor: freezed == primaryColor
          ? _self.primaryColor
          : primaryColor // ignore: cast_nullable_to_non_nullable
              as Color,
      highlight: freezed == highlight
          ? _self.highlight
          : highlight // ignore: cast_nullable_to_non_nullable
              as Color,
      postTitle: freezed == postTitle
          ? _self.postTitle
          : postTitle // ignore: cast_nullable_to_non_nullable
              as Color,
      readPost: freezed == readPost
          ? _self.readPost
          : readPost // ignore: cast_nullable_to_non_nullable
              as Color,
      announcement: freezed == announcement
          ? _self.announcement
          : announcement // ignore: cast_nullable_to_non_nullable
              as Color,
      contentText: freezed == contentText
          ? _self.contentText
          : contentText // ignore: cast_nullable_to_non_nullable
              as Color,
      linkColor: freezed == linkColor
          ? _self.linkColor
          : linkColor // ignore: cast_nullable_to_non_nullable
              as Color,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _CrabirTheme extends CrabirTheme {
  _CrabirTheme(
      {@ColorConverter() this.background = Colors.black,
      @ColorConverter() this.cardBackground = Colors.black,
      @ColorConverter() this.toolBarBackground = Colors.black,
      @ColorConverter() this.toolBarText = Colors.white,
      @ColorConverter() this.primaryColor = const Color(0xffff6e40),
      @ColorConverter() this.highlight = const Color(0xffff0000),
      @ColorConverter() this.postTitle = const Color(0xFFF5F6F8),
      @ColorConverter() this.readPost = const Color(0xFFB7B8BC),
      @ColorConverter() this.announcement = const Color(0xFF00FF00),
      @ColorConverter() this.contentText = const Color(0xFFF5F6F8),
      @ColorConverter() this.linkColor = const Color(0xFF4B91E2)})
      : super._();
  factory _CrabirTheme.fromJson(Map<String, dynamic> json) =>
      _$CrabirThemeFromJson(json);

  /// Color of the foundation
  @override
  @JsonKey()
  @ColorConverter()
  final Color background;
  @override
  @JsonKey()
  @ColorConverter()
  final Color cardBackground;
  @override
  @JsonKey()
  @ColorConverter()
  final Color toolBarBackground;
  @override
  @JsonKey()
  @ColorConverter()
  final Color toolBarText;

  /// Buttons and widgets
  @override
  @JsonKey()
  @ColorConverter()
  final Color primaryColor;

  /// Communities and usernames
  @override
  @JsonKey()
  @ColorConverter()
  final Color highlight;
  @override
  @JsonKey()
  @ColorConverter()
  final Color postTitle;
  @override
  @JsonKey()
  @ColorConverter()
  final Color readPost;
  @override
  @JsonKey()
  @ColorConverter()
  final Color announcement;
  @override
  @JsonKey()
  @ColorConverter()
  final Color contentText;
  @override
  @JsonKey()
  @ColorConverter()
  final Color linkColor;

  /// Create a copy of CrabirTheme
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$CrabirThemeCopyWith<_CrabirTheme> get copyWith =>
      __$CrabirThemeCopyWithImpl<_CrabirTheme>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$CrabirThemeToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _CrabirTheme &&
            const DeepCollectionEquality()
                .equals(other.background, background) &&
            const DeepCollectionEquality()
                .equals(other.cardBackground, cardBackground) &&
            const DeepCollectionEquality()
                .equals(other.toolBarBackground, toolBarBackground) &&
            const DeepCollectionEquality()
                .equals(other.toolBarText, toolBarText) &&
            const DeepCollectionEquality()
                .equals(other.primaryColor, primaryColor) &&
            const DeepCollectionEquality().equals(other.highlight, highlight) &&
            const DeepCollectionEquality().equals(other.postTitle, postTitle) &&
            const DeepCollectionEquality().equals(other.readPost, readPost) &&
            const DeepCollectionEquality()
                .equals(other.announcement, announcement) &&
            const DeepCollectionEquality()
                .equals(other.contentText, contentText) &&
            const DeepCollectionEquality().equals(other.linkColor, linkColor));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(background),
      const DeepCollectionEquality().hash(cardBackground),
      const DeepCollectionEquality().hash(toolBarBackground),
      const DeepCollectionEquality().hash(toolBarText),
      const DeepCollectionEquality().hash(primaryColor),
      const DeepCollectionEquality().hash(highlight),
      const DeepCollectionEquality().hash(postTitle),
      const DeepCollectionEquality().hash(readPost),
      const DeepCollectionEquality().hash(announcement),
      const DeepCollectionEquality().hash(contentText),
      const DeepCollectionEquality().hash(linkColor));

  @override
  String toString() {
    return 'CrabirTheme(background: $background, cardBackground: $cardBackground, toolBarBackground: $toolBarBackground, toolBarText: $toolBarText, primaryColor: $primaryColor, highlight: $highlight, postTitle: $postTitle, readPost: $readPost, announcement: $announcement, contentText: $contentText, linkColor: $linkColor)';
  }
}

/// @nodoc
abstract mixin class _$CrabirThemeCopyWith<$Res>
    implements $CrabirThemeCopyWith<$Res> {
  factory _$CrabirThemeCopyWith(
          _CrabirTheme value, $Res Function(_CrabirTheme) _then) =
      __$CrabirThemeCopyWithImpl;
  @override
  @useResult
  $Res call(
      {@ColorConverter() Color background,
      @ColorConverter() Color cardBackground,
      @ColorConverter() Color toolBarBackground,
      @ColorConverter() Color toolBarText,
      @ColorConverter() Color primaryColor,
      @ColorConverter() Color highlight,
      @ColorConverter() Color postTitle,
      @ColorConverter() Color readPost,
      @ColorConverter() Color announcement,
      @ColorConverter() Color contentText,
      @ColorConverter() Color linkColor});
}

/// @nodoc
class __$CrabirThemeCopyWithImpl<$Res> implements _$CrabirThemeCopyWith<$Res> {
  __$CrabirThemeCopyWithImpl(this._self, this._then);

  final _CrabirTheme _self;
  final $Res Function(_CrabirTheme) _then;

  /// Create a copy of CrabirTheme
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? background = freezed,
    Object? cardBackground = freezed,
    Object? toolBarBackground = freezed,
    Object? toolBarText = freezed,
    Object? primaryColor = freezed,
    Object? highlight = freezed,
    Object? postTitle = freezed,
    Object? readPost = freezed,
    Object? announcement = freezed,
    Object? contentText = freezed,
    Object? linkColor = freezed,
  }) {
    return _then(_CrabirTheme(
      background: freezed == background
          ? _self.background
          : background // ignore: cast_nullable_to_non_nullable
              as Color,
      cardBackground: freezed == cardBackground
          ? _self.cardBackground
          : cardBackground // ignore: cast_nullable_to_non_nullable
              as Color,
      toolBarBackground: freezed == toolBarBackground
          ? _self.toolBarBackground
          : toolBarBackground // ignore: cast_nullable_to_non_nullable
              as Color,
      toolBarText: freezed == toolBarText
          ? _self.toolBarText
          : toolBarText // ignore: cast_nullable_to_non_nullable
              as Color,
      primaryColor: freezed == primaryColor
          ? _self.primaryColor
          : primaryColor // ignore: cast_nullable_to_non_nullable
              as Color,
      highlight: freezed == highlight
          ? _self.highlight
          : highlight // ignore: cast_nullable_to_non_nullable
              as Color,
      postTitle: freezed == postTitle
          ? _self.postTitle
          : postTitle // ignore: cast_nullable_to_non_nullable
              as Color,
      readPost: freezed == readPost
          ? _self.readPost
          : readPost // ignore: cast_nullable_to_non_nullable
              as Color,
      announcement: freezed == announcement
          ? _self.announcement
          : announcement // ignore: cast_nullable_to_non_nullable
              as Color,
      contentText: freezed == contentText
          ? _self.contentText
          : contentText // ignore: cast_nullable_to_non_nullable
              as Color,
      linkColor: freezed == linkColor
          ? _self.linkColor
          : linkColor // ignore: cast_nullable_to_non_nullable
              as Color,
    ));
  }
}

// dart format on
