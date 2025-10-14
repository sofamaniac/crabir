part of 'thread_bloc.dart';

@freezed
sealed class ThreadEvent with _$ThreadEvent {
  /// Initial load of post and comments
  const factory ThreadEvent.load() = Load;
  const factory ThreadEvent.refresh() = Refresh;

  /// (Un)Collapse the specified comment
  factory ThreadEvent.collapse(Comment comment) = Collapse;

  /// Load more comments
  factory ThreadEvent.loadMore(Thing_More more) = LoadMore;

  /// Change sort
  factory ThreadEvent.setSort(CommentSort? sort) = SetSort;

  /// Open bar on comment
  factory ThreadEvent.openComment(String comment) = OpenComment;

  /// Close bar on currently open comment
  factory ThreadEvent.closeComment() = CloseComment;

  /// Insert a comment in the tree
  factory ThreadEvent.insertComment(Comment comment) = InsertComment;
}
