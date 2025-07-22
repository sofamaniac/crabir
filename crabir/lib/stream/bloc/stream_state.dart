part of 'stream_bloc.dart';

enum StreamStatus { initial, success, failure }

final class StreamState {
  final StreamStatus status;
  final List<Thing> items;
  final bool hasReachedMax;

  const StreamState({
    this.status = StreamStatus.initial,
    this.items = const [],
    this.hasReachedMax = false,
  });

  StreamState copyWith({
    StreamStatus? status,
    List<Thing>? items,
    bool? hasReachedMax,
  }) {
    return StreamState(
      status: status ?? this.status,
      items: items ?? this.items,
      hasReachedMax: hasReachedMax ?? this.hasReachedMax,
    );
  }
}
