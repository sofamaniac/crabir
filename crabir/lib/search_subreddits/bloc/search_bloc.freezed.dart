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
mixin _$SubredditSearchEvent {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is SubredditSearchEvent);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'SubredditSearchEvent()';
  }
}

/// @nodoc
class $SubredditSearchEventCopyWith<$Res> {
  $SubredditSearchEventCopyWith(
      SubredditSearchEvent _, $Res Function(SubredditSearchEvent) __);
}

/// Adds pattern-matching-related methods to [SubredditSearchEvent].
extension SubredditSearchEventPatterns on SubredditSearchEvent {
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
  }) {
    final _that = this;
    switch (_that) {
      case Query():
        return query(_that);
      case SetSort():
        return setSort(_that);
      case Fetch():
        return fetch(_that);
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
  }) {
    final _that = this;
    switch (_that) {
      case Query() when query != null:
        return query(_that);
      case SetSort() when setSort != null:
        return setSort(_that);
      case Fetch() when fetch != null:
        return fetch(_that);
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
    TResult Function(String query)? query,
    TResult Function(SubredditSearchSort sort)? setSort,
    TResult Function()? fetch,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Query() when query != null:
        return query(_that.query);
      case SetSort() when setSort != null:
        return setSort(_that.sort);
      case Fetch() when fetch != null:
        return fetch();
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
    required TResult Function(String query) query,
    required TResult Function(SubredditSearchSort sort) setSort,
    required TResult Function() fetch,
  }) {
    final _that = this;
    switch (_that) {
      case Query():
        return query(_that.query);
      case SetSort():
        return setSort(_that.sort);
      case Fetch():
        return fetch();
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
    TResult? Function(String query)? query,
    TResult? Function(SubredditSearchSort sort)? setSort,
    TResult? Function()? fetch,
  }) {
    final _that = this;
    switch (_that) {
      case Query() when query != null:
        return query(_that.query);
      case SetSort() when setSort != null:
        return setSort(_that.sort);
      case Fetch() when fetch != null:
        return fetch();
      case _:
        return null;
    }
  }
}

/// @nodoc

class Query implements SubredditSearchEvent {
  Query(this.query);

  final String query;

  /// Create a copy of SubredditSearchEvent
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
            (identical(other.query, query) || other.query == query));
  }

  @override
  int get hashCode => Object.hash(runtimeType, query);

  @override
  String toString() {
    return 'SubredditSearchEvent.query(query: $query)';
  }
}

/// @nodoc
abstract mixin class $QueryCopyWith<$Res>
    implements $SubredditSearchEventCopyWith<$Res> {
  factory $QueryCopyWith(Query value, $Res Function(Query) _then) =
      _$QueryCopyWithImpl;
  @useResult
  $Res call({String query});
}

/// @nodoc
class _$QueryCopyWithImpl<$Res> implements $QueryCopyWith<$Res> {
  _$QueryCopyWithImpl(this._self, this._then);

  final Query _self;
  final $Res Function(Query) _then;

  /// Create a copy of SubredditSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? query = null,
  }) {
    return _then(Query(
      null == query
          ? _self.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class SetSort implements SubredditSearchEvent {
  SetSort(this.sort);

  final SubredditSearchSort sort;

  /// Create a copy of SubredditSearchEvent
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
    return 'SubredditSearchEvent.setSort(sort: $sort)';
  }
}

/// @nodoc
abstract mixin class $SetSortCopyWith<$Res>
    implements $SubredditSearchEventCopyWith<$Res> {
  factory $SetSortCopyWith(SetSort value, $Res Function(SetSort) _then) =
      _$SetSortCopyWithImpl;
  @useResult
  $Res call({SubredditSearchSort sort});
}

/// @nodoc
class _$SetSortCopyWithImpl<$Res> implements $SetSortCopyWith<$Res> {
  _$SetSortCopyWithImpl(this._self, this._then);

  final SetSort _self;
  final $Res Function(SetSort) _then;

  /// Create a copy of SubredditSearchEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? sort = null,
  }) {
    return _then(SetSort(
      null == sort
          ? _self.sort
          : sort // ignore: cast_nullable_to_non_nullable
              as SubredditSearchSort,
    ));
  }
}

/// @nodoc

class Fetch implements SubredditSearchEvent {
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
    return 'SubredditSearchEvent.fetch()';
  }
}

/// @nodoc
mixin _$SubredditSearchState {
  StreamStatus get status;
  List<Subreddit> get items;
  bool get hasReachedMax;
  String get query;

  /// Create a copy of SubredditSearchState
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $SubredditSearchStateCopyWith<SubredditSearchState> get copyWith =>
      _$SubredditSearchStateCopyWithImpl<SubredditSearchState>(
          this as SubredditSearchState, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is SubredditSearchState &&
            (identical(other.status, status) || other.status == status) &&
            const DeepCollectionEquality().equals(other.items, items) &&
            (identical(other.hasReachedMax, hasReachedMax) ||
                other.hasReachedMax == hasReachedMax) &&
            (identical(other.query, query) || other.query == query));
  }

  @override
  int get hashCode => Object.hash(runtimeType, status,
      const DeepCollectionEquality().hash(items), hasReachedMax, query);

  @override
  String toString() {
    return 'SubredditSearchState(status: $status, items: $items, hasReachedMax: $hasReachedMax, query: $query)';
  }
}

/// @nodoc
abstract mixin class $SubredditSearchStateCopyWith<$Res> {
  factory $SubredditSearchStateCopyWith(SubredditSearchState value,
          $Res Function(SubredditSearchState) _then) =
      _$SubredditSearchStateCopyWithImpl;
  @useResult
  $Res call(
      {StreamStatus status,
      List<Subreddit> items,
      bool hasReachedMax,
      String query});
}

/// @nodoc
class _$SubredditSearchStateCopyWithImpl<$Res>
    implements $SubredditSearchStateCopyWith<$Res> {
  _$SubredditSearchStateCopyWithImpl(this._self, this._then);

  final SubredditSearchState _self;
  final $Res Function(SubredditSearchState) _then;

  /// Create a copy of SubredditSearchState
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? status = null,
    Object? items = null,
    Object? hasReachedMax = null,
    Object? query = null,
  }) {
    return _then(_self.copyWith(
      status: null == status
          ? _self.status
          : status // ignore: cast_nullable_to_non_nullable
              as StreamStatus,
      items: null == items
          ? _self.items
          : items // ignore: cast_nullable_to_non_nullable
              as List<Subreddit>,
      hasReachedMax: null == hasReachedMax
          ? _self.hasReachedMax
          : hasReachedMax // ignore: cast_nullable_to_non_nullable
              as bool,
      query: null == query
          ? _self.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// Adds pattern-matching-related methods to [SubredditSearchState].
extension SubredditSearchStatePatterns on SubredditSearchState {
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
    TResult Function(_SubredditSearchState value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _SubredditSearchState() when $default != null:
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
    TResult Function(_SubredditSearchState value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _SubredditSearchState():
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
    TResult? Function(_SubredditSearchState value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _SubredditSearchState() when $default != null:
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
    TResult Function(StreamStatus status, List<Subreddit> items,
            bool hasReachedMax, String query)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _SubredditSearchState() when $default != null:
        return $default(
            _that.status, _that.items, _that.hasReachedMax, _that.query);
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
    TResult Function(StreamStatus status, List<Subreddit> items,
            bool hasReachedMax, String query)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _SubredditSearchState():
        return $default(
            _that.status, _that.items, _that.hasReachedMax, _that.query);
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
    TResult? Function(StreamStatus status, List<Subreddit> items,
            bool hasReachedMax, String query)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _SubredditSearchState() when $default != null:
        return $default(
            _that.status, _that.items, _that.hasReachedMax, _that.query);
      case _:
        return null;
    }
  }
}

/// @nodoc

class _SubredditSearchState implements SubredditSearchState {
  const _SubredditSearchState(
      {this.status = StreamStatus.initial,
      final List<Subreddit> items = const [],
      this.hasReachedMax = false,
      this.query = ""})
      : _items = items;

  @override
  @JsonKey()
  final StreamStatus status;
  final List<Subreddit> _items;
  @override
  @JsonKey()
  List<Subreddit> get items {
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

  /// Create a copy of SubredditSearchState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$SubredditSearchStateCopyWith<_SubredditSearchState> get copyWith =>
      __$SubredditSearchStateCopyWithImpl<_SubredditSearchState>(
          this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _SubredditSearchState &&
            (identical(other.status, status) || other.status == status) &&
            const DeepCollectionEquality().equals(other._items, _items) &&
            (identical(other.hasReachedMax, hasReachedMax) ||
                other.hasReachedMax == hasReachedMax) &&
            (identical(other.query, query) || other.query == query));
  }

  @override
  int get hashCode => Object.hash(runtimeType, status,
      const DeepCollectionEquality().hash(_items), hasReachedMax, query);

  @override
  String toString() {
    return 'SubredditSearchState(status: $status, items: $items, hasReachedMax: $hasReachedMax, query: $query)';
  }
}

/// @nodoc
abstract mixin class _$SubredditSearchStateCopyWith<$Res>
    implements $SubredditSearchStateCopyWith<$Res> {
  factory _$SubredditSearchStateCopyWith(_SubredditSearchState value,
          $Res Function(_SubredditSearchState) _then) =
      __$SubredditSearchStateCopyWithImpl;
  @override
  @useResult
  $Res call(
      {StreamStatus status,
      List<Subreddit> items,
      bool hasReachedMax,
      String query});
}

/// @nodoc
class __$SubredditSearchStateCopyWithImpl<$Res>
    implements _$SubredditSearchStateCopyWith<$Res> {
  __$SubredditSearchStateCopyWithImpl(this._self, this._then);

  final _SubredditSearchState _self;
  final $Res Function(_SubredditSearchState) _then;

  /// Create a copy of SubredditSearchState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? status = null,
    Object? items = null,
    Object? hasReachedMax = null,
    Object? query = null,
  }) {
    return _then(_SubredditSearchState(
      status: null == status
          ? _self.status
          : status // ignore: cast_nullable_to_non_nullable
              as StreamStatus,
      items: null == items
          ? _self._items
          : items // ignore: cast_nullable_to_non_nullable
              as List<Subreddit>,
      hasReachedMax: null == hasReachedMax
          ? _self.hasReachedMax
          : hasReachedMax // ignore: cast_nullable_to_non_nullable
              as bool,
      query: null == query
          ? _self.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
mixin _$PostSearchState {
  StreamStatus get status;
  List<Post> get items;
  bool get hasReachedMax;
  String get query;

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
            (identical(other.query, query) || other.query == query));
  }

  @override
  int get hashCode => Object.hash(runtimeType, status,
      const DeepCollectionEquality().hash(items), hasReachedMax, query);

  @override
  String toString() {
    return 'PostSearchState(status: $status, items: $items, hasReachedMax: $hasReachedMax, query: $query)';
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
      String query});
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
    ));
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
            String query)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _PostSearchState() when $default != null:
        return $default(
            _that.status, _that.items, _that.hasReachedMax, _that.query);
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
            String query)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostSearchState():
        return $default(
            _that.status, _that.items, _that.hasReachedMax, _that.query);
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
            String query)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _PostSearchState() when $default != null:
        return $default(
            _that.status, _that.items, _that.hasReachedMax, _that.query);
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
      this.query = ""})
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
            (identical(other.query, query) || other.query == query));
  }

  @override
  int get hashCode => Object.hash(runtimeType, status,
      const DeepCollectionEquality().hash(_items), hasReachedMax, query);

  @override
  String toString() {
    return 'PostSearchState(status: $status, items: $items, hasReachedMax: $hasReachedMax, query: $query)';
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
      String query});
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
    ));
  }
}

// dart format on
