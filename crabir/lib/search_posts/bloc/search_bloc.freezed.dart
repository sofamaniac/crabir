// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'search_bloc.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$PostSearchEvent {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is PostSearchEvent);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'PostSearchEvent()';
  }
}

/// @nodoc
class $PostSearchEventCopyWith<$Res> {
  $PostSearchEventCopyWith(
      PostSearchEvent _, $Res Function(PostSearchEvent) __);
}

/// Adds pattern-matching-related methods to [PostSearchEvent].
extension PostSearchEventPatterns on PostSearchEvent {
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
    TResult Function(Query value)? query,
    TResult Function(SetSort value)? setSort,
    TResult Function(Fetch value)? fetch,
    TResult Function(RemoveSubreddit value)? removeSubreddit,
    TResult Function(Save value)? save,
    TResult Function(Vote value)? vote,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Query() when query != null:
        return query(_that);
      case SetSort() when setSort != null:
        return setSort(_that);
      case Fetch() when fetch != null:
        return fetch(_that);
      case RemoveSubreddit() when removeSubreddit != null:
        return removeSubreddit(_that);
      case Save() when save != null:
        return save(_that);
      case Vote() when vote != null:
        return vote(_that);
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
    required TResult Function(Query value) query,
    required TResult Function(SetSort value) setSort,
    required TResult Function(Fetch value) fetch,
    required TResult Function(RemoveSubreddit value) removeSubreddit,
    required TResult Function(Save value) save,
    required TResult Function(Vote value) vote,
  }) {
    final _that = this;
    switch (_that) {
      case Query():
        return query(_that);
      case SetSort():
        return setSort(_that);
      case Fetch():
        return fetch(_that);
      case RemoveSubreddit():
        return removeSubreddit(_that);
      case Save():
        return save(_that);
      case Vote():
        return vote(_that);
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
    TResult? Function(Query value)? query,
    TResult? Function(SetSort value)? setSort,
    TResult? Function(Fetch value)? fetch,
    TResult? Function(RemoveSubreddit value)? removeSubreddit,
    TResult? Function(Save value)? save,
    TResult? Function(Vote value)? vote,
  }) {
    final _that = this;
    switch (_that) {
      case Query() when query != null:
        return query(_that);
      case SetSort() when setSort != null:
        return setSort(_that);
      case Fetch() when fetch != null:
        return fetch(_that);
      case RemoveSubreddit() when removeSubreddit != null:
        return removeSubreddit(_that);
      case Save() when save != null:
        return save(_that);
      case Vote() when vote != null:
        return vote(_that);
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
    TResult Function(String query, String? subreddit, String? flair)? query,
    TResult Function(PostSearchSort sort)? setSort,
    TResult Function()? fetch,
    TResult Function()? removeSubreddit,
    TResult Function(bool save, Fullname name)? save,
    TResult Function(VoteDirection direction, Fullname name)? vote,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Query() when query != null:
        return query(_that.query, _that.subreddit, _that.flair);
      case SetSort() when setSort != null:
        return setSort(_that.sort);
      case Fetch() when fetch != null:
        return fetch();
      case RemoveSubreddit() when removeSubreddit != null:
        return removeSubreddit();
      case Save() when save != null:
        return save(_that.save, _that.name);
      case Vote() when vote != null:
        return vote(_that.direction, _that.name);
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
    required TResult Function(String query, String? subreddit, String? flair)
        query,
    required TResult Function(PostSearchSort sort) setSort,
    required TResult Function() fetch,
    required TResult Function() removeSubreddit,
    required TResult Function(bool save, Fullname name) save,
    required TResult Function(VoteDirection direction, Fullname name) vote,
  }) {
    final _that = this;
    switch (_that) {
      case Query():
        return query(_that.query, _that.subreddit, _that.flair);
      case SetSort():
        return setSort(_that.sort);
      case Fetch():
        return fetch();
      case RemoveSubreddit():
        return removeSubreddit();
      case Save():
        return save(_that.save, _that.name);
      case Vote():
        return vote(_that.direction, _that.name);
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
    TResult? Function(String query, String? subreddit, String? flair)? query,
    TResult? Function(PostSearchSort sort)? setSort,
    TResult? Function()? fetch,
    TResult? Function()? removeSubreddit,
    TResult? Function(bool save, Fullname name)? save,
    TResult? Function(VoteDirection direction, Fullname name)? vote,
  }) {
    final _that = this;
    switch (_that) {
      case Query() when query != null:
        return query(_that.query, _that.subreddit, _that.flair);
      case SetSort() when setSort != null:
        return setSort(_that.sort);
      case Fetch() when fetch != null:
        return fetch();
      case RemoveSubreddit() when removeSubreddit != null:
        return removeSubreddit();
      case Save() when save != null:
        return save(_that.save, _that.name);
      case Vote() when vote != null:
        return vote(_that.direction, _that.name);
      case _:
        return null;
    }
  }
}

/// @nodoc

class Query implements PostSearchEvent {
  Query(this.query, {this.subreddit, this.flair});

  final String query;
  final String? subreddit;
  final String? flair;

  /// Create a copy of PostSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $QueryCopyWith<Query> get copyWith =>
      _$QueryCopyWithImpl<Query>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Query &&
            (identical(other.query, query) || other.query == query) &&
            (identical(other.subreddit, subreddit) ||
                other.subreddit == subreddit) &&
            (identical(other.flair, flair) || other.flair == flair));
  }

  @override
  int get hashCode => Object.hash(runtimeType, query, subreddit, flair);

  @override
  String toString() {
    return 'PostSearchEvent.query(query: $query, subreddit: $subreddit, flair: $flair)';
  }
}

/// @nodoc
abstract mixin class $QueryCopyWith<$Res>
    implements $PostSearchEventCopyWith<$Res> {
  factory $QueryCopyWith(Query value, $Res Function(Query) _then) =
      _$QueryCopyWithImpl;
  @useResult
  $Res call({String query, String? subreddit, String? flair});
}

/// @nodoc
class _$QueryCopyWithImpl<$Res> implements $QueryCopyWith<$Res> {
  _$QueryCopyWithImpl(this._self, this._then);

  final Query _self;
  final $Res Function(Query) _then;

  /// Create a copy of PostSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? query = null,
    Object? subreddit = freezed,
    Object? flair = freezed,
  }) {
    return _then(Query(
      null == query
          ? _self.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
      subreddit: freezed == subreddit
          ? _self.subreddit
          : subreddit // ignore: cast_nullable_to_non_nullable
              as String?,
      flair: freezed == flair
          ? _self.flair
          : flair // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc

class SetSort implements PostSearchEvent {
  SetSort(this.sort);

  final PostSearchSort sort;

  /// Create a copy of PostSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $SetSortCopyWith<SetSort> get copyWith =>
      _$SetSortCopyWithImpl<SetSort>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is SetSort &&
            (identical(other.sort, sort) || other.sort == sort));
  }

  @override
  int get hashCode => Object.hash(runtimeType, sort);

  @override
  String toString() {
    return 'PostSearchEvent.setSort(sort: $sort)';
  }
}

/// @nodoc
abstract mixin class $SetSortCopyWith<$Res>
    implements $PostSearchEventCopyWith<$Res> {
  factory $SetSortCopyWith(SetSort value, $Res Function(SetSort) _then) =
      _$SetSortCopyWithImpl;
  @useResult
  $Res call({PostSearchSort sort});

  $PostSearchSortCopyWith<$Res> get sort;
}

/// @nodoc
class _$SetSortCopyWithImpl<$Res> implements $SetSortCopyWith<$Res> {
  _$SetSortCopyWithImpl(this._self, this._then);

  final SetSort _self;
  final $Res Function(SetSort) _then;

  /// Create a copy of PostSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? sort = null,
  }) {
    return _then(SetSort(
      null == sort
          ? _self.sort
          : sort // ignore: cast_nullable_to_non_nullable
              as PostSearchSort,
    ));
  }

  /// Create a copy of PostSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $PostSearchSortCopyWith<$Res> get sort {
    return $PostSearchSortCopyWith<$Res>(_self.sort, (value) {
      return _then(_self.copyWith(sort: value));
    });
  }
}

/// @nodoc

class Fetch implements PostSearchEvent {
  Fetch();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Fetch);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'PostSearchEvent.fetch()';
  }
}

/// @nodoc

class RemoveSubreddit implements PostSearchEvent {
  RemoveSubreddit();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is RemoveSubreddit);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'PostSearchEvent.removeSubreddit()';
  }
}

/// @nodoc

class Save implements PostSearchEvent {
  Save({required this.save, required this.name});

  final bool save;
  final Fullname name;

  /// Create a copy of PostSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $SaveCopyWith<Save> get copyWith =>
      _$SaveCopyWithImpl<Save>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Save &&
            (identical(other.save, save) || other.save == save) &&
            (identical(other.name, name) || other.name == name));
  }

  @override
  int get hashCode => Object.hash(runtimeType, save, name);

  @override
  String toString() {
    return 'PostSearchEvent.save(save: $save, name: $name)';
  }
}

/// @nodoc
abstract mixin class $SaveCopyWith<$Res>
    implements $PostSearchEventCopyWith<$Res> {
  factory $SaveCopyWith(Save value, $Res Function(Save) _then) =
      _$SaveCopyWithImpl;
  @useResult
  $Res call({bool save, Fullname name});
}

/// @nodoc
class _$SaveCopyWithImpl<$Res> implements $SaveCopyWith<$Res> {
  _$SaveCopyWithImpl(this._self, this._then);

  final Save _self;
  final $Res Function(Save) _then;

  /// Create a copy of PostSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? save = null,
    Object? name = null,
  }) {
    return _then(Save(
      save: null == save
          ? _self.save
          : save // ignore: cast_nullable_to_non_nullable
              as bool,
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as Fullname,
    ));
  }
}

/// @nodoc

class Vote implements PostSearchEvent {
  Vote({required this.direction, required this.name});

  final VoteDirection direction;
  final Fullname name;

  /// Create a copy of PostSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $VoteCopyWith<Vote> get copyWith =>
      _$VoteCopyWithImpl<Vote>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Vote &&
            (identical(other.direction, direction) ||
                other.direction == direction) &&
            (identical(other.name, name) || other.name == name));
  }

  @override
  int get hashCode => Object.hash(runtimeType, direction, name);

  @override
  String toString() {
    return 'PostSearchEvent.vote(direction: $direction, name: $name)';
  }
}

/// @nodoc
abstract mixin class $VoteCopyWith<$Res>
    implements $PostSearchEventCopyWith<$Res> {
  factory $VoteCopyWith(Vote value, $Res Function(Vote) _then) =
      _$VoteCopyWithImpl;
  @useResult
  $Res call({VoteDirection direction, Fullname name});
}

/// @nodoc
class _$VoteCopyWithImpl<$Res> implements $VoteCopyWith<$Res> {
  _$VoteCopyWithImpl(this._self, this._then);

  final Vote _self;
  final $Res Function(Vote) _then;

  /// Create a copy of PostSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? direction = null,
    Object? name = null,
  }) {
    return _then(Vote(
      direction: null == direction
          ? _self.direction
          : direction // ignore: cast_nullable_to_non_nullable
              as VoteDirection,
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as Fullname,
    ));
  }
}

/// @nodoc
mixin _$PostSearchState {
  StreamStatus get status;
  List<Post> get items;
  bool get hasReachedMax;
  String get query;
  PostSearchSort get sort;

  /// Create a copy of PostSearchState
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $PostSearchStateCopyWith<PostSearchState> get copyWith =>
      _$PostSearchStateCopyWithImpl<PostSearchState>(
          this as PostSearchState, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is PostSearchState &&
            (identical(other.status, status) || other.status == status) &&
            const DeepCollectionEquality().equals(other.items, items) &&
            (identical(other.hasReachedMax, hasReachedMax) ||
                other.hasReachedMax == hasReachedMax) &&
            (identical(other.query, query) || other.query == query) &&
            (identical(other.sort, sort) || other.sort == sort));
  }

  @override
  int get hashCode => Object.hash(runtimeType, status,
      const DeepCollectionEquality().hash(items), hasReachedMax, query, sort);

  @override
  String toString() {
    return 'PostSearchState(status: $status, items: $items, hasReachedMax: $hasReachedMax, query: $query, sort: $sort)';
  }
}

/// @nodoc
abstract mixin class $PostSearchStateCopyWith<$Res> {
  factory $PostSearchStateCopyWith(
          PostSearchState value, $Res Function(PostSearchState) _then) =
      _$PostSearchStateCopyWithImpl;
  @useResult
  $Res call(
      {StreamStatus status,
      List<Post> items,
      bool hasReachedMax,
      String query,
      PostSearchSort sort});

  $PostSearchSortCopyWith<$Res> get sort;
}

/// @nodoc
class _$PostSearchStateCopyWithImpl<$Res>
    implements $PostSearchStateCopyWith<$Res> {
  _$PostSearchStateCopyWithImpl(this._self, this._then);

  final PostSearchState _self;
  final $Res Function(PostSearchState) _then;

  /// Create a copy of PostSearchState
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? status = null,
    Object? items = null,
    Object? hasReachedMax = null,
    Object? query = null,
    Object? sort = null,
  }) {
    return _then(_self.copyWith(
      status: null == status
          ? _self.status
          : status // ignore: cast_nullable_to_non_nullable
              as StreamStatus,
      items: null == items
          ? _self.items
          : items // ignore: cast_nullable_to_non_nullable
              as List<Post>,
      hasReachedMax: null == hasReachedMax
          ? _self.hasReachedMax
          : hasReachedMax // ignore: cast_nullable_to_non_nullable
              as bool,
      query: null == query
          ? _self.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
      sort: null == sort
          ? _self.sort
          : sort // ignore: cast_nullable_to_non_nullable
              as PostSearchSort,
    ));
  }

  /// Create a copy of PostSearchState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $PostSearchSortCopyWith<$Res> get sort {
    return $PostSearchSortCopyWith<$Res>(_self.sort, (value) {
      return _then(_self.copyWith(sort: value));
    });
  }
}

/// Adds pattern-matching-related methods to [PostSearchState].
extension PostSearchStatePatterns on PostSearchState {
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
    TResult Function(_PostSearchState value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _PostSearchState() when $default != null:
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
    TResult Function(_PostSearchState value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostSearchState():
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
    TResult? Function(_PostSearchState value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostSearchState() when $default != null:
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
    TResult Function(StreamStatus status, List<Post> items, bool hasReachedMax,
            String query, PostSearchSort sort)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _PostSearchState() when $default != null:
        return $default(_that.status, _that.items, _that.hasReachedMax,
            _that.query, _that.sort);
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
    TResult Function(StreamStatus status, List<Post> items, bool hasReachedMax,
            String query, PostSearchSort sort)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostSearchState():
        return $default(_that.status, _that.items, _that.hasReachedMax,
            _that.query, _that.sort);
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
    TResult? Function(StreamStatus status, List<Post> items, bool hasReachedMax,
            String query, PostSearchSort sort)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostSearchState() when $default != null:
        return $default(_that.status, _that.items, _that.hasReachedMax,
            _that.query, _that.sort);
      case _:
        return null;
    }
  }
}

/// @nodoc

class _PostSearchState implements PostSearchState {
  const _PostSearchState(
      {this.status = StreamStatus.initial,
      final List<Post> items = const [],
      this.hasReachedMax = false,
      this.query = "",
      this.sort = const PostSearchSort.relevance(Timeframe.all)})
      : _items = items;

  @override
  @JsonKey()
  final StreamStatus status;
  final List<Post> _items;
  @override
  @JsonKey()
  List<Post> get items {
    if (_items is EqualUnmodifiableListView) return _items;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_items);
  }

  @override
  @JsonKey()
  final bool hasReachedMax;
  @override
  @JsonKey()
  final String query;
  @override
  @JsonKey()
  final PostSearchSort sort;

  /// Create a copy of PostSearchState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$PostSearchStateCopyWith<_PostSearchState> get copyWith =>
      __$PostSearchStateCopyWithImpl<_PostSearchState>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _PostSearchState &&
            (identical(other.status, status) || other.status == status) &&
            const DeepCollectionEquality().equals(other._items, _items) &&
            (identical(other.hasReachedMax, hasReachedMax) ||
                other.hasReachedMax == hasReachedMax) &&
            (identical(other.query, query) || other.query == query) &&
            (identical(other.sort, sort) || other.sort == sort));
  }

  @override
  int get hashCode => Object.hash(runtimeType, status,
      const DeepCollectionEquality().hash(_items), hasReachedMax, query, sort);

  @override
  String toString() {
    return 'PostSearchState(status: $status, items: $items, hasReachedMax: $hasReachedMax, query: $query, sort: $sort)';
  }
}

/// @nodoc
abstract mixin class _$PostSearchStateCopyWith<$Res>
    implements $PostSearchStateCopyWith<$Res> {
  factory _$PostSearchStateCopyWith(
          _PostSearchState value, $Res Function(_PostSearchState) _then) =
      __$PostSearchStateCopyWithImpl;
  @override
  @useResult
  $Res call(
      {StreamStatus status,
      List<Post> items,
      bool hasReachedMax,
      String query,
      PostSearchSort sort});

  @override
  $PostSearchSortCopyWith<$Res> get sort;
}

/// @nodoc
class __$PostSearchStateCopyWithImpl<$Res>
    implements _$PostSearchStateCopyWith<$Res> {
  __$PostSearchStateCopyWithImpl(this._self, this._then);

  final _PostSearchState _self;
  final $Res Function(_PostSearchState) _then;

  /// Create a copy of PostSearchState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? status = null,
    Object? items = null,
    Object? hasReachedMax = null,
    Object? query = null,
    Object? sort = null,
  }) {
    return _then(_PostSearchState(
      status: null == status
          ? _self.status
          : status // ignore: cast_nullable_to_non_nullable
              as StreamStatus,
      items: null == items
          ? _self._items
          : items // ignore: cast_nullable_to_non_nullable
              as List<Post>,
      hasReachedMax: null == hasReachedMax
          ? _self.hasReachedMax
          : hasReachedMax // ignore: cast_nullable_to_non_nullable
              as bool,
      query: null == query
          ? _self.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
      sort: null == sort
          ? _self.sort
          : sort // ignore: cast_nullable_to_non_nullable
              as PostSearchSort,
    ));
  }

  /// Create a copy of PostSearchState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $PostSearchSortCopyWith<$Res> get sort {
    return $PostSearchSortCopyWith<$Res>(_self.sort, (value) {
      return _then(_self.copyWith(sort: value));
    });
  }
}

// dart format on
