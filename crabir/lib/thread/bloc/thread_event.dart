part of 'thread_bloc.dart';

sealed class ThreadEvent {}

/// Initial load of post and comments
class Load extends ThreadEvent {}

/// (Un)Collapse the specified comment
class Collapse extends ThreadEvent {
  final Comment comment;
  Collapse(this.comment);
}

/// Load more comments
class LoadMore extends ThreadEvent {
  final Thing_More more;
  LoadMore(this.more);
}

class Refresh extends ThreadEvent {}
