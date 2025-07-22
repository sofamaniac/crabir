part of 'stream_bloc.dart';

sealed class StreamEvent {}

final class Fetch extends StreamEvent {}

final class Refresh extends StreamEvent {}

final class Save extends StreamEvent {
  final bool saved;
  final Fullname name;
  Save({required this.name, required this.saved});
}

final class Vote extends StreamEvent {
  final VoteDirection direction;
  final Fullname name;
  Vote({required this.name, required this.direction});
}

final class SetStream extends StreamEvent {
  final reddit_stream.Streamable streamable;
  SetStream({required this.streamable});
}
