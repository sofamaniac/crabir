part of 'search_bloc.dart';

@freezed
sealed class PostSearchEvent with _$PostSearchEvent {
  factory PostSearchEvent.query(
    String query, {
    String? subreddit,
    String? flair,
  }) = Query;
  factory PostSearchEvent.setSort(SearchSort sort) = SetSort;

  /// asks for the next items
  factory PostSearchEvent.fetch() = Fetch;

  /// Remove restriction on subreddit.
  factory PostSearchEvent.removeSubreddit() = RemoveSubreddit;
}
