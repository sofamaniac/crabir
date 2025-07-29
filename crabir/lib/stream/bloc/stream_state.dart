part of 'stream_bloc.dart';

enum StreamStatus { initial, success, failure }

@freezed
abstract class StreamState with _$StreamState {
  const factory StreamState({
    @Default(StreamStatus.initial) StreamStatus status,
    @Default([]) List<Thing> items,
    @Default(false) bool hasReachedMax,
  }) = _StreamState;
}
