// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
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

  final SearchSort sort;

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
  $Res call({SearchSort sort});
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
              as SearchSort,
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
                other.hasReachedMax == hasReachedMax));
  }

  @override
  int get hashCode => Object.hash(runtimeType, status,
      const DeepCollectionEquality().hash(items), hasReachedMax);

  @override
  String toString() {
    return 'SubredditSearchState(status: $status, items: $items, hasReachedMax: $hasReachedMax)';
  }
}

/// @nodoc
abstract mixin class $SubredditSearchStateCopyWith<$Res> {
  factory $SubredditSearchStateCopyWith(SubredditSearchState value,
          $Res Function(SubredditSearchState) _then) =
      _$SubredditSearchStateCopyWithImpl;
  @useResult
  $Res call({StreamStatus status, List<Subreddit> items, bool hasReachedMax});
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
    ));
  }
}

/// @nodoc

class _SubredditSearchState implements SubredditSearchState {
  const _SubredditSearchState(
      {this.status = StreamStatus.initial,
      final List<Subreddit> items = const [],
      this.hasReachedMax = false})
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
                other.hasReachedMax == hasReachedMax));
  }

  @override
  int get hashCode => Object.hash(runtimeType, status,
      const DeepCollectionEquality().hash(_items), hasReachedMax);

  @override
  String toString() {
    return 'SubredditSearchState(status: $status, items: $items, hasReachedMax: $hasReachedMax)';
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
  $Res call({StreamStatus status, List<Subreddit> items, bool hasReachedMax});
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
    ));
  }
}

// dart format on
