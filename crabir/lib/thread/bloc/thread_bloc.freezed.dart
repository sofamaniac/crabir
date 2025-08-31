// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'thread_bloc.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$ThreadEvent {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is ThreadEvent);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'ThreadEvent()';
  }
}

/// @nodoc
class $ThreadEventCopyWith<$Res> {
  $ThreadEventCopyWith(ThreadEvent _, $Res Function(ThreadEvent) __);
}

/// @nodoc

class Load implements ThreadEvent {
  const Load();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Load);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'ThreadEvent.load()';
  }
}

/// @nodoc

class Refresh implements ThreadEvent {
  const Refresh();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Refresh);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'ThreadEvent.refresh()';
  }
}

/// @nodoc

class Collapse implements ThreadEvent {
  Collapse(this.comment);

  final Comment comment;

  /// Create a copy of ThreadEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $CollapseCopyWith<Collapse> get copyWith =>
      _$CollapseCopyWithImpl<Collapse>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Collapse &&
            (identical(other.comment, comment) || other.comment == comment));
  }

  @override
  int get hashCode => Object.hash(runtimeType, comment);

  @override
  String toString() {
    return 'ThreadEvent.collapse(comment: $comment)';
  }
}

/// @nodoc
abstract mixin class $CollapseCopyWith<$Res>
    implements $ThreadEventCopyWith<$Res> {
  factory $CollapseCopyWith(Collapse value, $Res Function(Collapse) _then) =
      _$CollapseCopyWithImpl;
  @useResult
  $Res call({Comment comment});
}

/// @nodoc
class _$CollapseCopyWithImpl<$Res> implements $CollapseCopyWith<$Res> {
  _$CollapseCopyWithImpl(this._self, this._then);

  final Collapse _self;
  final $Res Function(Collapse) _then;

  /// Create a copy of ThreadEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? comment = null,
  }) {
    return _then(Collapse(
      null == comment
          ? _self.comment
          : comment // ignore: cast_nullable_to_non_nullable
              as Comment,
    ));
  }
}

/// @nodoc

class LoadMore implements ThreadEvent {
  LoadMore(this.more);

  final Thing_More more;

  /// Create a copy of ThreadEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $LoadMoreCopyWith<LoadMore> get copyWith =>
      _$LoadMoreCopyWithImpl<LoadMore>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is LoadMore &&
            const DeepCollectionEquality().equals(other.more, more));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(more));

  @override
  String toString() {
    return 'ThreadEvent.loadMore(more: $more)';
  }
}

/// @nodoc
abstract mixin class $LoadMoreCopyWith<$Res>
    implements $ThreadEventCopyWith<$Res> {
  factory $LoadMoreCopyWith(LoadMore value, $Res Function(LoadMore) _then) =
      _$LoadMoreCopyWithImpl;
  @useResult
  $Res call({Thing_More more});
}

/// @nodoc
class _$LoadMoreCopyWithImpl<$Res> implements $LoadMoreCopyWith<$Res> {
  _$LoadMoreCopyWithImpl(this._self, this._then);

  final LoadMore _self;
  final $Res Function(LoadMore) _then;

  /// Create a copy of ThreadEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? more = freezed,
  }) {
    return _then(LoadMore(
      freezed == more
          ? _self.more
          : more // ignore: cast_nullable_to_non_nullable
              as Thing_More,
    ));
  }
}

/// @nodoc

class SetSort implements ThreadEvent {
  SetSort(this.sort);

  final CommentSort? sort;

  /// Create a copy of ThreadEvent
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
    return 'ThreadEvent.setSort(sort: $sort)';
  }
}

/// @nodoc
abstract mixin class $SetSortCopyWith<$Res>
    implements $ThreadEventCopyWith<$Res> {
  factory $SetSortCopyWith(SetSort value, $Res Function(SetSort) _then) =
      _$SetSortCopyWithImpl;
  @useResult
  $Res call({CommentSort? sort});
}

/// @nodoc
class _$SetSortCopyWithImpl<$Res> implements $SetSortCopyWith<$Res> {
  _$SetSortCopyWithImpl(this._self, this._then);

  final SetSort _self;
  final $Res Function(SetSort) _then;

  /// Create a copy of ThreadEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? sort = freezed,
  }) {
    return _then(SetSort(
      freezed == sort
          ? _self.sort
          : sort // ignore: cast_nullable_to_non_nullable
              as CommentSort?,
    ));
  }
}

/// @nodoc
mixin _$ThreadState {
  List<Thing> get flatComments;
  Post? get post;
  Status get status;
  Set<String> get collapsed;
  Set<String> get hidden;
  CommentSort? get sort;

  /// Create a copy of ThreadState
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $ThreadStateCopyWith<ThreadState> get copyWith =>
      _$ThreadStateCopyWithImpl<ThreadState>(this as ThreadState, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is ThreadState &&
            const DeepCollectionEquality()
                .equals(other.flatComments, flatComments) &&
            (identical(other.post, post) || other.post == post) &&
            (identical(other.status, status) || other.status == status) &&
            const DeepCollectionEquality().equals(other.collapsed, collapsed) &&
            const DeepCollectionEquality().equals(other.hidden, hidden) &&
            (identical(other.sort, sort) || other.sort == sort));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(flatComments),
      post,
      status,
      const DeepCollectionEquality().hash(collapsed),
      const DeepCollectionEquality().hash(hidden),
      sort);

  @override
  String toString() {
    return 'ThreadState(flatComments: $flatComments, post: $post, status: $status, collapsed: $collapsed, hidden: $hidden, sort: $sort)';
  }
}

/// @nodoc
abstract mixin class $ThreadStateCopyWith<$Res> {
  factory $ThreadStateCopyWith(
          ThreadState value, $Res Function(ThreadState) _then) =
      _$ThreadStateCopyWithImpl;
  @useResult
  $Res call(
      {List<Thing> flatComments,
      Post? post,
      Status status,
      Set<String> collapsed,
      Set<String> hidden,
      CommentSort? sort});
}

/// @nodoc
class _$ThreadStateCopyWithImpl<$Res> implements $ThreadStateCopyWith<$Res> {
  _$ThreadStateCopyWithImpl(this._self, this._then);

  final ThreadState _self;
  final $Res Function(ThreadState) _then;

  /// Create a copy of ThreadState
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? flatComments = null,
    Object? post = freezed,
    Object? status = null,
    Object? collapsed = null,
    Object? hidden = null,
    Object? sort = freezed,
  }) {
    return _then(_self.copyWith(
      flatComments: null == flatComments
          ? _self.flatComments
          : flatComments // ignore: cast_nullable_to_non_nullable
              as List<Thing>,
      post: freezed == post
          ? _self.post
          : post // ignore: cast_nullable_to_non_nullable
              as Post?,
      status: null == status
          ? _self.status
          : status // ignore: cast_nullable_to_non_nullable
              as Status,
      collapsed: null == collapsed
          ? _self.collapsed
          : collapsed // ignore: cast_nullable_to_non_nullable
              as Set<String>,
      hidden: null == hidden
          ? _self.hidden
          : hidden // ignore: cast_nullable_to_non_nullable
              as Set<String>,
      sort: freezed == sort
          ? _self.sort
          : sort // ignore: cast_nullable_to_non_nullable
              as CommentSort?,
    ));
  }
}

/// @nodoc

class _ThreadState implements ThreadState {
  _ThreadState(
      {final List<Thing> flatComments = const [],
      this.post,
      this.status = Status.unloaded,
      final Set<String> collapsed = const {},
      final Set<String> hidden = const {},
      this.sort = null})
      : _flatComments = flatComments,
        _collapsed = collapsed,
        _hidden = hidden;

  final List<Thing> _flatComments;
  @override
  @JsonKey()
  List<Thing> get flatComments {
    if (_flatComments is EqualUnmodifiableListView) return _flatComments;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_flatComments);
  }

  @override
  final Post? post;
  @override
  @JsonKey()
  final Status status;
  final Set<String> _collapsed;
  @override
  @JsonKey()
  Set<String> get collapsed {
    if (_collapsed is EqualUnmodifiableSetView) return _collapsed;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableSetView(_collapsed);
  }

  final Set<String> _hidden;
  @override
  @JsonKey()
  Set<String> get hidden {
    if (_hidden is EqualUnmodifiableSetView) return _hidden;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableSetView(_hidden);
  }

  @override
  @JsonKey()
  final CommentSort? sort;

  /// Create a copy of ThreadState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$ThreadStateCopyWith<_ThreadState> get copyWith =>
      __$ThreadStateCopyWithImpl<_ThreadState>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _ThreadState &&
            const DeepCollectionEquality()
                .equals(other._flatComments, _flatComments) &&
            (identical(other.post, post) || other.post == post) &&
            (identical(other.status, status) || other.status == status) &&
            const DeepCollectionEquality()
                .equals(other._collapsed, _collapsed) &&
            const DeepCollectionEquality().equals(other._hidden, _hidden) &&
            (identical(other.sort, sort) || other.sort == sort));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(_flatComments),
      post,
      status,
      const DeepCollectionEquality().hash(_collapsed),
      const DeepCollectionEquality().hash(_hidden),
      sort);

  @override
  String toString() {
    return 'ThreadState(flatComments: $flatComments, post: $post, status: $status, collapsed: $collapsed, hidden: $hidden, sort: $sort)';
  }
}

/// @nodoc
abstract mixin class _$ThreadStateCopyWith<$Res>
    implements $ThreadStateCopyWith<$Res> {
  factory _$ThreadStateCopyWith(
          _ThreadState value, $Res Function(_ThreadState) _then) =
      __$ThreadStateCopyWithImpl;
  @override
  @useResult
  $Res call(
      {List<Thing> flatComments,
      Post? post,
      Status status,
      Set<String> collapsed,
      Set<String> hidden,
      CommentSort? sort});
}

/// @nodoc
class __$ThreadStateCopyWithImpl<$Res> implements _$ThreadStateCopyWith<$Res> {
  __$ThreadStateCopyWithImpl(this._self, this._then);

  final _ThreadState _self;
  final $Res Function(_ThreadState) _then;

  /// Create a copy of ThreadState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? flatComments = null,
    Object? post = freezed,
    Object? status = null,
    Object? collapsed = null,
    Object? hidden = null,
    Object? sort = freezed,
  }) {
    return _then(_ThreadState(
      flatComments: null == flatComments
          ? _self._flatComments
          : flatComments // ignore: cast_nullable_to_non_nullable
              as List<Thing>,
      post: freezed == post
          ? _self.post
          : post // ignore: cast_nullable_to_non_nullable
              as Post?,
      status: null == status
          ? _self.status
          : status // ignore: cast_nullable_to_non_nullable
              as Status,
      collapsed: null == collapsed
          ? _self._collapsed
          : collapsed // ignore: cast_nullable_to_non_nullable
              as Set<String>,
      hidden: null == hidden
          ? _self._hidden
          : hidden // ignore: cast_nullable_to_non_nullable
              as Set<String>,
      sort: freezed == sort
          ? _self.sort
          : sort // ignore: cast_nullable_to_non_nullable
              as CommentSort?,
    ));
  }
}

// dart format on
