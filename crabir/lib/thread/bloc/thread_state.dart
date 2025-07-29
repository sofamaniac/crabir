part of 'thread_bloc.dart';

enum Status { unloaded, success, failure }

@freezed
abstract class ThreadState with _$ThreadState {
  factory ThreadState({
    @Default([]) List<Thing> flatComments,
    Post? post,
    @Default(Status.unloaded) Status status,
    @Default({}) Set<String> collapsed,
    @Default({}) Set<String> hidden,
  }) = _ThreadState;
}
