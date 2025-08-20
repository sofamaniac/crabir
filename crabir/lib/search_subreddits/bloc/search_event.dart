part of 'search_bloc.dart';

@freezed
sealed class SubredditSearchEvent with _$SubredditSearchEvent {
  factory SubredditSearchEvent.query(String query) = Query;
  factory SubredditSearchEvent.setSort(SearchSort sort) = SetSort;

  /// asks for the next items
  factory SubredditSearchEvent.fetch() = Fetch;
}
