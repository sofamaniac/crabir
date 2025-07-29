// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'stream_bloc.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$StreamState {
  StreamStatus get status;
  List<Thing> get items;
  bool get hasReachedMax;

  /// Create a copy of StreamState
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $StreamStateCopyWith<StreamState> get copyWith =>
      _$StreamStateCopyWithImpl<StreamState>(this as StreamState, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is StreamState &&
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
    return 'StreamState(status: $status, items: $items, hasReachedMax: $hasReachedMax)';
  }
}

/// @nodoc
abstract mixin class $StreamStateCopyWith<$Res> {
  factory $StreamStateCopyWith(
          StreamState value, $Res Function(StreamState) _then) =
      _$StreamStateCopyWithImpl;
  @useResult
  $Res call({StreamStatus status, List<Thing> items, bool hasReachedMax});
}

/// @nodoc
class _$StreamStateCopyWithImpl<$Res> implements $StreamStateCopyWith<$Res> {
  _$StreamStateCopyWithImpl(this._self, this._then);

  final StreamState _self;
  final $Res Function(StreamState) _then;

  /// Create a copy of StreamState
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
              as List<Thing>,
      hasReachedMax: null == hasReachedMax
          ? _self.hasReachedMax
          : hasReachedMax // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

/// @nodoc

class _StreamState implements StreamState {
  const _StreamState(
      {this.status = StreamStatus.initial,
      final List<Thing> items = const [],
      this.hasReachedMax = false})
      : _items = items;

  @override
  @JsonKey()
  final StreamStatus status;
  final List<Thing> _items;
  @override
  @JsonKey()
  List<Thing> get items {
    if (_items is EqualUnmodifiableListView) return _items;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_items);
  }

  @override
  @JsonKey()
  final bool hasReachedMax;

  /// Create a copy of StreamState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$StreamStateCopyWith<_StreamState> get copyWith =>
      __$StreamStateCopyWithImpl<_StreamState>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _StreamState &&
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
    return 'StreamState(status: $status, items: $items, hasReachedMax: $hasReachedMax)';
  }
}

/// @nodoc
abstract mixin class _$StreamStateCopyWith<$Res>
    implements $StreamStateCopyWith<$Res> {
  factory _$StreamStateCopyWith(
          _StreamState value, $Res Function(_StreamState) _then) =
      __$StreamStateCopyWithImpl;
  @override
  @useResult
  $Res call({StreamStatus status, List<Thing> items, bool hasReachedMax});
}

/// @nodoc
class __$StreamStateCopyWithImpl<$Res> implements _$StreamStateCopyWith<$Res> {
  __$StreamStateCopyWithImpl(this._self, this._then);

  final _StreamState _self;
  final $Res Function(_StreamState) _then;

  /// Create a copy of StreamState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? status = null,
    Object? items = null,
    Object? hasReachedMax = null,
  }) {
    return _then(_StreamState(
      status: null == status
          ? _self.status
          : status // ignore: cast_nullable_to_non_nullable
              as StreamStatus,
      items: null == items
          ? _self._items
          : items // ignore: cast_nullable_to_non_nullable
              as List<Thing>,
      hasReachedMax: null == hasReachedMax
          ? _self.hasReachedMax
          : hasReachedMax // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

/// @nodoc
mixin _$StreamEvent {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is StreamEvent);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'StreamEvent()';
  }
}

/// @nodoc
class $StreamEventCopyWith<$Res> {
  $StreamEventCopyWith(StreamEvent _, $Res Function(StreamEvent) __);
}

/// @nodoc

class Fetch implements StreamEvent {
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
    return 'StreamEvent.fetch()';
  }
}

/// @nodoc

class Refresh implements StreamEvent {
  Refresh();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Refresh);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'StreamEvent.refresh()';
  }
}

/// @nodoc

class Save implements StreamEvent {
  Save({required this.name, required this.saved});

  final Fullname name;
  final bool saved;

  /// Create a copy of StreamEvent
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
            (identical(other.name, name) || other.name == name) &&
            (identical(other.saved, saved) || other.saved == saved));
  }

  @override
  int get hashCode => Object.hash(runtimeType, name, saved);

  @override
  String toString() {
    return 'StreamEvent.save(name: $name, saved: $saved)';
  }
}

/// @nodoc
abstract mixin class $SaveCopyWith<$Res> implements $StreamEventCopyWith<$Res> {
  factory $SaveCopyWith(Save value, $Res Function(Save) _then) =
      _$SaveCopyWithImpl;
  @useResult
  $Res call({Fullname name, bool saved});
}

/// @nodoc
class _$SaveCopyWithImpl<$Res> implements $SaveCopyWith<$Res> {
  _$SaveCopyWithImpl(this._self, this._then);

  final Save _self;
  final $Res Function(Save) _then;

  /// Create a copy of StreamEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? name = null,
    Object? saved = null,
  }) {
    return _then(Save(
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as Fullname,
      saved: null == saved
          ? _self.saved
          : saved // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

/// @nodoc

class Vote implements StreamEvent {
  Vote({required this.name, required this.direction});

  final Fullname name;
  final VoteDirection direction;

  /// Create a copy of StreamEvent
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
            (identical(other.name, name) || other.name == name) &&
            (identical(other.direction, direction) ||
                other.direction == direction));
  }

  @override
  int get hashCode => Object.hash(runtimeType, name, direction);

  @override
  String toString() {
    return 'StreamEvent.vote(name: $name, direction: $direction)';
  }
}

/// @nodoc
abstract mixin class $VoteCopyWith<$Res> implements $StreamEventCopyWith<$Res> {
  factory $VoteCopyWith(Vote value, $Res Function(Vote) _then) =
      _$VoteCopyWithImpl;
  @useResult
  $Res call({Fullname name, VoteDirection direction});
}

/// @nodoc
class _$VoteCopyWithImpl<$Res> implements $VoteCopyWith<$Res> {
  _$VoteCopyWithImpl(this._self, this._then);

  final Vote _self;
  final $Res Function(Vote) _then;

  /// Create a copy of StreamEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? name = null,
    Object? direction = null,
  }) {
    return _then(Vote(
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as Fullname,
      direction: null == direction
          ? _self.direction
          : direction // ignore: cast_nullable_to_non_nullable
              as VoteDirection,
    ));
  }
}

/// @nodoc

class SetStream implements StreamEvent {
  SetStream({required this.streamable});

  final reddit_stream.Streamable streamable;

  /// Create a copy of StreamEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $SetStreamCopyWith<SetStream> get copyWith =>
      _$SetStreamCopyWithImpl<SetStream>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is SetStream &&
            (identical(other.streamable, streamable) ||
                other.streamable == streamable));
  }

  @override
  int get hashCode => Object.hash(runtimeType, streamable);

  @override
  String toString() {
    return 'StreamEvent.setStream(streamable: $streamable)';
  }
}

/// @nodoc
abstract mixin class $SetStreamCopyWith<$Res>
    implements $StreamEventCopyWith<$Res> {
  factory $SetStreamCopyWith(SetStream value, $Res Function(SetStream) _then) =
      _$SetStreamCopyWithImpl;
  @useResult
  $Res call({reddit_stream.Streamable streamable});
}

/// @nodoc
class _$SetStreamCopyWithImpl<$Res> implements $SetStreamCopyWith<$Res> {
  _$SetStreamCopyWithImpl(this._self, this._then);

  final SetStream _self;
  final $Res Function(SetStream) _then;

  /// Create a copy of StreamEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? streamable = null,
  }) {
    return _then(SetStream(
      streamable: null == streamable
          ? _self.streamable
          : streamable // ignore: cast_nullable_to_non_nullable
              as reddit_stream.Streamable,
    ));
  }
}

// dart format on
