// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
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

/// Adds pattern-matching-related methods to [Feed].
extension FeedPatterns on Feed {
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
    TResult Function(Feed_Home value)? home,
    TResult Function(Feed_All value)? all,
    TResult Function(Feed_Popular value)? popular,
    TResult Function(Feed_Subreddit value)? subreddit,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Feed_Home() when home != null:
        return home(_that);
      case Feed_All() when all != null:
        return all(_that);
      case Feed_Popular() when popular != null:
        return popular(_that);
      case Feed_Subreddit() when subreddit != null:
        return subreddit(_that);
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
    required TResult Function(Feed_Home value) home,
    required TResult Function(Feed_All value) all,
    required TResult Function(Feed_Popular value) popular,
    required TResult Function(Feed_Subreddit value) subreddit,
  }) {
    final _that = this;
    switch (_that) {
      case Feed_Home():
        return home(_that);
      case Feed_All():
        return all(_that);
      case Feed_Popular():
        return popular(_that);
      case Feed_Subreddit():
        return subreddit(_that);
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
    TResult? Function(Feed_Home value)? home,
    TResult? Function(Feed_All value)? all,
    TResult? Function(Feed_Popular value)? popular,
    TResult? Function(Feed_Subreddit value)? subreddit,
  }) {
    final _that = this;
    switch (_that) {
      case Feed_Home() when home != null:
        return home(_that);
      case Feed_All() when all != null:
        return all(_that);
      case Feed_Popular() when popular != null:
        return popular(_that);
      case Feed_Subreddit() when subreddit != null:
        return subreddit(_that);
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
    TResult Function()? home,
    TResult Function()? all,
    TResult Function()? popular,
    TResult Function(String field0)? subreddit,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Feed_Home() when home != null:
        return home();
      case Feed_All() when all != null:
        return all();
      case Feed_Popular() when popular != null:
        return popular();
      case Feed_Subreddit() when subreddit != null:
        return subreddit(_that.field0);
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
    required TResult Function() home,
    required TResult Function() all,
    required TResult Function() popular,
    required TResult Function(String field0) subreddit,
  }) {
    final _that = this;
    switch (_that) {
      case Feed_Home():
        return home();
      case Feed_All():
        return all();
      case Feed_Popular():
        return popular();
      case Feed_Subreddit():
        return subreddit(_that.field0);
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
    TResult? Function()? home,
    TResult? Function()? all,
    TResult? Function()? popular,
    TResult? Function(String field0)? subreddit,
  }) {
    final _that = this;
    switch (_that) {
      case Feed_Home() when home != null:
        return home();
      case Feed_All() when all != null:
        return all();
      case Feed_Popular() when popular != null:
        return popular();
      case Feed_Subreddit() when subreddit != null:
        return subreddit(_that.field0);
      case _:
        return null;
    }
  }
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

/// Adds pattern-matching-related methods to [FeedSort].
extension FeedSortPatterns on FeedSort {
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
    TResult Function(FeedSort_Best value)? best,
    TResult Function(FeedSort_Hot value)? hot,
    TResult Function(FeedSort_New value)? new_,
    TResult Function(FeedSort_Top value)? top,
    TResult Function(FeedSort_Rising value)? rising,
    TResult Function(FeedSort_Controversial value)? controversial,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case FeedSort_Best() when best != null:
        return best(_that);
      case FeedSort_Hot() when hot != null:
        return hot(_that);
      case FeedSort_New() when new_ != null:
        return new_(_that);
      case FeedSort_Top() when top != null:
        return top(_that);
      case FeedSort_Rising() when rising != null:
        return rising(_that);
      case FeedSort_Controversial() when controversial != null:
        return controversial(_that);
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
    required TResult Function(FeedSort_Best value) best,
    required TResult Function(FeedSort_Hot value) hot,
    required TResult Function(FeedSort_New value) new_,
    required TResult Function(FeedSort_Top value) top,
    required TResult Function(FeedSort_Rising value) rising,
    required TResult Function(FeedSort_Controversial value) controversial,
  }) {
    final _that = this;
    switch (_that) {
      case FeedSort_Best():
        return best(_that);
      case FeedSort_Hot():
        return hot(_that);
      case FeedSort_New():
        return new_(_that);
      case FeedSort_Top():
        return top(_that);
      case FeedSort_Rising():
        return rising(_that);
      case FeedSort_Controversial():
        return controversial(_that);
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
    TResult? Function(FeedSort_Best value)? best,
    TResult? Function(FeedSort_Hot value)? hot,
    TResult? Function(FeedSort_New value)? new_,
    TResult? Function(FeedSort_Top value)? top,
    TResult? Function(FeedSort_Rising value)? rising,
    TResult? Function(FeedSort_Controversial value)? controversial,
  }) {
    final _that = this;
    switch (_that) {
      case FeedSort_Best() when best != null:
        return best(_that);
      case FeedSort_Hot() when hot != null:
        return hot(_that);
      case FeedSort_New() when new_ != null:
        return new_(_that);
      case FeedSort_Top() when top != null:
        return top(_that);
      case FeedSort_Rising() when rising != null:
        return rising(_that);
      case FeedSort_Controversial() when controversial != null:
        return controversial(_that);
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
    TResult Function()? best,
    TResult Function()? hot,
    TResult Function(Timeframe field0)? new_,
    TResult Function(Timeframe field0)? top,
    TResult Function()? rising,
    TResult Function(Timeframe field0)? controversial,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case FeedSort_Best() when best != null:
        return best();
      case FeedSort_Hot() when hot != null:
        return hot();
      case FeedSort_New() when new_ != null:
        return new_(_that.field0);
      case FeedSort_Top() when top != null:
        return top(_that.field0);
      case FeedSort_Rising() when rising != null:
        return rising();
      case FeedSort_Controversial() when controversial != null:
        return controversial(_that.field0);
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
    required TResult Function() best,
    required TResult Function() hot,
    required TResult Function(Timeframe field0) new_,
    required TResult Function(Timeframe field0) top,
    required TResult Function() rising,
    required TResult Function(Timeframe field0) controversial,
  }) {
    final _that = this;
    switch (_that) {
      case FeedSort_Best():
        return best();
      case FeedSort_Hot():
        return hot();
      case FeedSort_New():
        return new_(_that.field0);
      case FeedSort_Top():
        return top(_that.field0);
      case FeedSort_Rising():
        return rising();
      case FeedSort_Controversial():
        return controversial(_that.field0);
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
    TResult? Function()? best,
    TResult? Function()? hot,
    TResult? Function(Timeframe field0)? new_,
    TResult? Function(Timeframe field0)? top,
    TResult? Function()? rising,
    TResult? Function(Timeframe field0)? controversial,
  }) {
    final _that = this;
    switch (_that) {
      case FeedSort_Best() when best != null:
        return best();
      case FeedSort_Hot() when hot != null:
        return hot();
      case FeedSort_New() when new_ != null:
        return new_(_that.field0);
      case FeedSort_Top() when top != null:
        return top(_that.field0);
      case FeedSort_Rising() when rising != null:
        return rising();
      case FeedSort_Controversial() when controversial != null:
        return controversial(_that.field0);
      case _:
        return null;
    }
  }
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
