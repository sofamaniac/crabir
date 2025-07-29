part of 'stream_bloc.dart';

@freezed
sealed class StreamEvent with _$StreamEvent {
  factory StreamEvent.fetch() = Fetch;
  factory StreamEvent.refresh() = Refresh;
  factory StreamEvent.save({
    required Fullname name,
    required bool saved,
  }) = Save;
  factory StreamEvent.vote({
    required Fullname name,
    required VoteDirection direction,
  }) = Vote;
  factory StreamEvent.setStream({
    required reddit_stream.Streamable streamable,
  }) = SetStream;
}
