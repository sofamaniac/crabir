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
mixin _$Theme {
  /// Color of the foundation
  Color get background;
  Color get cardBackground;
  Color get toolBarBackground;
  Color get toolBarText;

  /// Buttons and widgets
  Color get primaryColor;

  /// Communities and usernames
  Color get highlight;
  Color get postTitle;
  Color get readPost;
  Color get announcement;
  Color get contentText;
  Color get linkColor;

  /// Create a copy of Theme
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $ThemeCopyWith<Theme> get copyWith =>
      _$ThemeCopyWithImpl<Theme>(this as Theme, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Theme &&
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
    return 'Theme(background: $background, cardBackground: $cardBackground, toolBarBackground: $toolBarBackground, toolBarText: $toolBarText, primaryColor: $primaryColor, highlight: $highlight, postTitle: $postTitle, readPost: $readPost, announcement: $announcement, contentText: $contentText, linkColor: $linkColor)';
  }
}

/// @nodoc
abstract mixin class $ThemeCopyWith<$Res> {
  factory $ThemeCopyWith(Theme value, $Res Function(Theme) _then) =
      _$ThemeCopyWithImpl;
  @useResult
  $Res call(
      {Color background,
      Color cardBackground,
      Color toolBarBackground,
      Color toolBarText,
      Color primaryColor,
      Color highlight,
      Color postTitle,
      Color readPost,
      Color announcement,
      Color contentText,
      Color linkColor});
}

/// @nodoc
class _$ThemeCopyWithImpl<$Res> implements $ThemeCopyWith<$Res> {
  _$ThemeCopyWithImpl(this._self, this._then);

  final Theme _self;
  final $Res Function(Theme) _then;

  /// Create a copy of Theme
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

class _Theme extends Theme {
  _Theme(
      {this.background = Colors.black,
      this.cardBackground = Colors.black,
      this.toolBarBackground = Colors.black,
      this.toolBarText = Colors.white,
      this.primaryColor = const Color(0xffff6e40),
      this.highlight = const Color(0xffff0000),
      this.postTitle = const Color(0xFFF5F6F8),
      this.readPost = const Color(0xFFB7B8BC),
      this.announcement = const Color(0xFF00FF00),
      this.contentText = const Color(0xFFF5F6F8),
      this.linkColor = const Color(0xFF4B91E2)})
      : super._();

  /// Color of the foundation
  @override
  @JsonKey()
  final Color background;
  @override
  @JsonKey()
  final Color cardBackground;
  @override
  @JsonKey()
  final Color toolBarBackground;
  @override
  @JsonKey()
  final Color toolBarText;

  /// Buttons and widgets
  @override
  @JsonKey()
  final Color primaryColor;

  /// Communities and usernames
  @override
  @JsonKey()
  final Color highlight;
  @override
  @JsonKey()
  final Color postTitle;
  @override
  @JsonKey()
  final Color readPost;
  @override
  @JsonKey()
  final Color announcement;
  @override
  @JsonKey()
  final Color contentText;
  @override
  @JsonKey()
  final Color linkColor;

  /// Create a copy of Theme
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$ThemeCopyWith<_Theme> get copyWith =>
      __$ThemeCopyWithImpl<_Theme>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _Theme &&
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
    return 'Theme(background: $background, cardBackground: $cardBackground, toolBarBackground: $toolBarBackground, toolBarText: $toolBarText, primaryColor: $primaryColor, highlight: $highlight, postTitle: $postTitle, readPost: $readPost, announcement: $announcement, contentText: $contentText, linkColor: $linkColor)';
  }
}

/// @nodoc
abstract mixin class _$ThemeCopyWith<$Res> implements $ThemeCopyWith<$Res> {
  factory _$ThemeCopyWith(_Theme value, $Res Function(_Theme) _then) =
      __$ThemeCopyWithImpl;
  @override
  @useResult
  $Res call(
      {Color background,
      Color cardBackground,
      Color toolBarBackground,
      Color toolBarText,
      Color primaryColor,
      Color highlight,
      Color postTitle,
      Color readPost,
      Color announcement,
      Color contentText,
      Color linkColor});
}

/// @nodoc
class __$ThemeCopyWithImpl<$Res> implements _$ThemeCopyWith<$Res> {
  __$ThemeCopyWithImpl(this._self, this._then);

  final _Theme _self;
  final $Res Function(_Theme) _then;

  /// Create a copy of Theme
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
    return _then(_Theme(
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
