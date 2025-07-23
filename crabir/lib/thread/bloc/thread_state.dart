part of 'thread_bloc.dart';

enum Status { unloaded, success, failure }

class ThreadState {
  final List<Thing> flatComments;
  final Post? post;
  final Status status;
  final Set<String> collapsed;
  final Set<String> hidden;
  ThreadState({
    this.post,
    this.status = Status.unloaded,
    this.flatComments = const [],
    this.collapsed = const {},
    this.hidden = const {},
  });

  ThreadState copyWith({
    Post? post,
    Status? status,
    List<Thing>? flatComments,
    Set<String>? collapsed,
    Set<String>? hidden,
  }) {
    return ThreadState(
      post: post ?? this.post,
      status: status ?? this.status,
      flatComments: flatComments ?? this.flatComments,
      collapsed: collapsed ?? this.collapsed,
      hidden: hidden ?? this.hidden,
    );
  }
}
