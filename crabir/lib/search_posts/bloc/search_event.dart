part of 'search_bloc.dart';

@freezed
sealed class PostSearchEvent with _$PostSearchEvent {
  factory PostSearchEvent.query(
    String query, {
    String? subreddit,
    String? flair,
  }) = Query;
  factory PostSearchEvent.setSort(PostSearchSort sort) = SetSort;

  /// asks for the next items
  factory PostSearchEvent.fetch() = Fetch;

  /// Remove restriction on subreddit.
  factory PostSearchEvent.removeSubreddit() = RemoveSubreddit;

  /// Save / Unsave post
  factory PostSearchEvent.save({
    required bool save,
    required Fullname name,
  }) = Save;

  /// Vote on post
  factory PostSearchEvent.vote({
    required VoteDirection direction,
    required Fullname name,
  }) = Vote;
}
