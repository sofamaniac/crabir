part of 'search_bloc.dart';

enum StreamStatus { initial, success, failure }

@freezed
abstract class SubredditSearchState with _$SubredditSearchState {
  const factory SubredditSearchState({
    @Default(StreamStatus.initial) StreamStatus status,
    @Default([]) List<Subreddit> items,
    @Default(false) bool hasReachedMax,
  }) = _SubredditSearchState;
}
