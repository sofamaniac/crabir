// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'feed.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$Feed {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Feed);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'Feed()';
  }
}

/// @nodoc
class $FeedCopyWith<$Res> {
  $FeedCopyWith(Feed _, $Res Function(Feed) __);
}

/// @nodoc

class Feed_Home extends Feed {
  const Feed_Home() : super._();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Feed_Home);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'Feed.home()';
  }
}

/// @nodoc

class Feed_All extends Feed {
  const Feed_All() : super._();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Feed_All);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'Feed.all()';
  }
}

/// @nodoc

class Feed_Popular extends Feed {
  const Feed_Popular() : super._();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Feed_Popular);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'Feed.popular()';
  }
}

/// @nodoc

class Feed_Subreddit extends Feed {
  const Feed_Subreddit(this.field0) : super._();

  final String field0;

  /// Create a copy of Feed
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Feed_SubredditCopyWith<Feed_Subreddit> get copyWith =>
      _$Feed_SubredditCopyWithImpl<Feed_Subreddit>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Feed_Subreddit &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'Feed.subreddit(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $Feed_SubredditCopyWith<$Res>
    implements $FeedCopyWith<$Res> {
  factory $Feed_SubredditCopyWith(
          Feed_Subreddit value, $Res Function(Feed_Subreddit) _then) =
      _$Feed_SubredditCopyWithImpl;
  @useResult
  $Res call({String field0});
}

/// @nodoc
class _$Feed_SubredditCopyWithImpl<$Res>
    implements $Feed_SubredditCopyWith<$Res> {
  _$Feed_SubredditCopyWithImpl(this._self, this._then);

  final Feed_Subreddit _self;
  final $Res Function(Feed_Subreddit) _then;

  /// Create a copy of Feed
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(Feed_Subreddit(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

FeedSort _$FeedSortFromJson(Map<String, dynamic> json) {
  switch (json['runtimeType']) {
    case 'best':
      return FeedSort_Best.fromJson(json);
    case 'hot':
      return FeedSort_Hot.fromJson(json);
    case 'new_':
      return FeedSort_New.fromJson(json);
    case 'top':
      return FeedSort_Top.fromJson(json);
    case 'rising':
      return FeedSort_Rising.fromJson(json);
    case 'controversial':
      return FeedSort_Controversial.fromJson(json);

    default:
      throw CheckedFromJsonException(json, 'runtimeType', 'FeedSort',
          'Invalid union type "${json['runtimeType']}"!');
  }
}

/// @nodoc
mixin _$FeedSort {
  /// Serializes this FeedSort to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is FeedSort);
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'FeedSort()';
  }
}

/// @nodoc
class $FeedSortCopyWith<$Res> {
  $FeedSortCopyWith(FeedSort _, $Res Function(FeedSort) __);
}

/// @nodoc
@JsonSerializable()
class FeedSort_Best extends FeedSort {
  const FeedSort_Best({final String? $type})
      : $type = $type ?? 'best',
        super._();
  factory FeedSort_Best.fromJson(Map<String, dynamic> json) =>
      _$FeedSort_BestFromJson(json);

  @JsonKey(name: 'runtimeType')
  final String $type;

  @override
  Map<String, dynamic> toJson() {
    return _$FeedSort_BestToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is FeedSort_Best);
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'FeedSort.best()';
  }
}

/// @nodoc
@JsonSerializable()
class FeedSort_Hot extends FeedSort {
  const FeedSort_Hot({final String? $type})
      : $type = $type ?? 'hot',
        super._();
  factory FeedSort_Hot.fromJson(Map<String, dynamic> json) =>
      _$FeedSort_HotFromJson(json);

  @JsonKey(name: 'runtimeType')
  final String $type;

  @override
  Map<String, dynamic> toJson() {
    return _$FeedSort_HotToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is FeedSort_Hot);
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'FeedSort.hot()';
  }
}

/// @nodoc
@JsonSerializable()
class FeedSort_New extends FeedSort {
  const FeedSort_New(this.field0, {final String? $type})
      : $type = $type ?? 'new_',
        super._();
  factory FeedSort_New.fromJson(Map<String, dynamic> json) =>
      _$FeedSort_NewFromJson(json);

  final Timeframe field0;

  @JsonKey(name: 'runtimeType')
  final String $type;

  /// Create a copy of FeedSort
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $FeedSort_NewCopyWith<FeedSort_New> get copyWith =>
      _$FeedSort_NewCopyWithImpl<FeedSort_New>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$FeedSort_NewToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is FeedSort_New &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'FeedSort.new_(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $FeedSort_NewCopyWith<$Res>
    implements $FeedSortCopyWith<$Res> {
  factory $FeedSort_NewCopyWith(
          FeedSort_New value, $Res Function(FeedSort_New) _then) =
      _$FeedSort_NewCopyWithImpl;
  @useResult
  $Res call({Timeframe field0});
}

/// @nodoc
class _$FeedSort_NewCopyWithImpl<$Res> implements $FeedSort_NewCopyWith<$Res> {
  _$FeedSort_NewCopyWithImpl(this._self, this._then);

  final FeedSort_New _self;
  final $Res Function(FeedSort_New) _then;

  /// Create a copy of FeedSort
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(FeedSort_New(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Timeframe,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class FeedSort_Top extends FeedSort {
  const FeedSort_Top(this.field0, {final String? $type})
      : $type = $type ?? 'top',
        super._();
  factory FeedSort_Top.fromJson(Map<String, dynamic> json) =>
      _$FeedSort_TopFromJson(json);

  final Timeframe field0;

  @JsonKey(name: 'runtimeType')
  final String $type;

  /// Create a copy of FeedSort
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $FeedSort_TopCopyWith<FeedSort_Top> get copyWith =>
      _$FeedSort_TopCopyWithImpl<FeedSort_Top>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$FeedSort_TopToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is FeedSort_Top &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'FeedSort.top(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $FeedSort_TopCopyWith<$Res>
    implements $FeedSortCopyWith<$Res> {
  factory $FeedSort_TopCopyWith(
          FeedSort_Top value, $Res Function(FeedSort_Top) _then) =
      _$FeedSort_TopCopyWithImpl;
  @useResult
  $Res call({Timeframe field0});
}

/// @nodoc
class _$FeedSort_TopCopyWithImpl<$Res> implements $FeedSort_TopCopyWith<$Res> {
  _$FeedSort_TopCopyWithImpl(this._self, this._then);

  final FeedSort_Top _self;
  final $Res Function(FeedSort_Top) _then;

  /// Create a copy of FeedSort
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(FeedSort_Top(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Timeframe,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class FeedSort_Rising extends FeedSort {
  const FeedSort_Rising({final String? $type})
      : $type = $type ?? 'rising',
        super._();
  factory FeedSort_Rising.fromJson(Map<String, dynamic> json) =>
      _$FeedSort_RisingFromJson(json);

  @JsonKey(name: 'runtimeType')
  final String $type;

  @override
  Map<String, dynamic> toJson() {
    return _$FeedSort_RisingToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is FeedSort_Rising);
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'FeedSort.rising()';
  }
}

/// @nodoc
@JsonSerializable()
class FeedSort_Controversial extends FeedSort {
  const FeedSort_Controversial(this.field0, {final String? $type})
      : $type = $type ?? 'controversial',
        super._();
  factory FeedSort_Controversial.fromJson(Map<String, dynamic> json) =>
      _$FeedSort_ControversialFromJson(json);

  final Timeframe field0;

  @JsonKey(name: 'runtimeType')
  final String $type;

  /// Create a copy of FeedSort
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $FeedSort_ControversialCopyWith<FeedSort_Controversial> get copyWith =>
      _$FeedSort_ControversialCopyWithImpl<FeedSort_Controversial>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$FeedSort_ControversialToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is FeedSort_Controversial &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'FeedSort.controversial(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $FeedSort_ControversialCopyWith<$Res>
    implements $FeedSortCopyWith<$Res> {
  factory $FeedSort_ControversialCopyWith(FeedSort_Controversial value,
          $Res Function(FeedSort_Controversial) _then) =
      _$FeedSort_ControversialCopyWithImpl;
  @useResult
  $Res call({Timeframe field0});
}

/// @nodoc
class _$FeedSort_ControversialCopyWithImpl<$Res>
    implements $FeedSort_ControversialCopyWith<$Res> {
  _$FeedSort_ControversialCopyWithImpl(this._self, this._then);

  final FeedSort_Controversial _self;
  final $Res Function(FeedSort_Controversial) _then;

  /// Create a copy of FeedSort
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(FeedSort_Controversial(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Timeframe,
    ));
  }
}

// dart format on
