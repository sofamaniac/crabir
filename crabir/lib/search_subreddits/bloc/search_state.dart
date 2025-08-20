part of 'search_bloc.dart';

enum StreamStatus { initial, success, failure }

@freezed
abstract class SubredditSearchState with _$SubredditSearchState {
  const factory SubredditSearchState({
    @Default(StreamStatus.initial) StreamStatus status,
    @Default([]) List<Subreddit> items,
    @Default(false) bool hasReachedMax,
    @Default("") String query,
  }) = _SubredditSearchState;
}

@freezed
abstract class PostSearchState with _$PostSearchState {
  const factory PostSearchState({
    @Default(StreamStatus.initial) StreamStatus status,
    @Default([]) List<Post> items,
    @Default(false) bool hasReachedMax,
    @Default("") String query,
  }) = _PostSearchState;
}
