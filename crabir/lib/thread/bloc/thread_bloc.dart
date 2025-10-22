import 'dart:collection';

import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:logging/logging.dart';

part 'thread_event.dart';
part 'thread_state.dart';
part 'thread_bloc.freezed.dart';

class ThreadBloc extends Bloc<ThreadEvent, ThreadState> {
  final Set<String> _collapsed = {};
  final Set<String> _hidden = {};
  final HashMap<String, List<Thing>> _moreLoaded = HashMap();
  List<Thing> _comments = [];
  late final Post _post;
  final String permalink;
  CommentSort? _sort;
  final Logger log = Logger("ThreadBloc");

  ThreadBloc(this.permalink) : super(ThreadState()) {
    on<Load>(_fetchComments);
    on<Collapse>(_collapse);
    on<LoadMore>(_loadMore);
    on<Refresh>(_refresh);
    on<SetSort>(_setSort);
    on<OpenComment>(_openComment);
    on<CloseComment>(_closeComment);
    on<InsertComment>(_insertComment);
  }

  static ThreadBloc? maybeOf(BuildContext context) {
    try {
      return context.watch<ThreadBloc>();
    } catch (_) {
      return null;
    }
  }

  Future<void> _openComment(
    OpenComment event,
    Emitter<ThreadState> emit,
  ) async {
    emit(state.copyWith(expandedComment: event.comment));
  }

  Future<void> _closeComment(
    CloseComment _,
    Emitter<ThreadState> emit,
  ) async {
    emit(state.copyWith(expandedComment: null));
  }

  Future<void> _insertComment(
    InsertComment event,
    Emitter<ThreadState> emit,
  ) async {
    final parent = event.comment.parentId;
    final position = state.flatComments.indexWhere((thing) {
      if (thing case Thing_Comment(field0: final comment)) {
        return comment.name.eq(other: parent);
      } else {
        return false;
      }
    });
    final comments = flatten(_comments);
    comments.insert(position + 1, Thing_Comment(event.comment));
    emit(state.copyWith(flatComments: comments));
  }

  Future<void> _fetchComments(Load _, Emitter<ThreadState> emit) async {
    try {
      final things = await RedditAPI.client().comments(
        permalink: permalink,
        sort: _sort,
      );
      _post = things.$1;
      _comments = things.$2;
      log.info("(${_comments.length}) comments and post loaded");
      emit(
        state.copyWith(
          status: Status.success,
          post: _post,
          flatComments: flatten(_comments),
        ),
      );
    } catch (e) {
      log.severe("Error during initial load: $e");
      emit(state.copyWith(status: Status.failure));
    }
  }

  Future<void> _setSort(SetSort sort, Emitter<ThreadState> emit) async {
    if (_sort == sort.sort) return;
    _sort = sort.sort;
    emit(
      state.copyWith(
        sort: _sort,
        status: Status.unloaded,
        flatComments: [],
      ),
    );
  }

  Future<void> _refresh(Refresh _, Emitter<ThreadState> emit) async {
    emit(state.copyWith(status: Status.unloaded, flatComments: []));
  }

  Future<void> _collapse(Collapse event, Emitter<ThreadState> emit) async {
    if (_collapsed.add(event.comment.id)) {
      for (final reply in event.comment.replies()) {
        _hide(reply);
      }
    } else {
      _collapsed.remove(event.comment.id);
      for (final reply in event.comment.replies()) {
        _reveal(reply);
      }
    }
    emit(state.copyWith(collapsed: {..._collapsed}, hidden: {..._hidden}));
  }

  void _hide(Thing comment) {
    _hidden.add(commentId(comment));
    switch (comment) {
      case Thing_Comment(field0: final comment):
        for (final reply in comment.replies()) {
          _hide(reply);
        }
      default:
    }
  }

  void _reveal(Thing comment) {
    _hidden.remove(commentId(comment));
    switch (comment) {
      case Thing_Comment(field0: final comment):
        for (final reply in comment.replies()) {
          _reveal(reply);
        }
      default:
    }
  }

  Future<void> _loadMore(LoadMore event, Emitter<ThreadState> emit) async {
    try {
      log.info("Loading comments");
      final newThings = await RedditAPI.client().loadMoreComments(
        parentId: _post.name,
        children: event.more.children,
      );
      _moreLoaded[event.more.id] = newThings;
      emit(state.copyWith(flatComments: flatten(_comments)));
    } catch (e) {
      log.severe("Error while loading comments: $e");
      emit(state.copyWith(status: Status.failure));
    }
  }

  List<Thing> flatten(
    List<Thing> comments,
  ) {
    final result = <Thing>[];
    for (final c in comments) {
      switch (c) {
        case Thing_Comment(:final field0):
          result.add(c);
          result.addAll(flatten(field0.replies()));
          break;
        case Thing_More(:final id):
          if (_moreLoaded.containsKey(id)) {
            result.addAll(flatten(_moreLoaded[id]!));
          } else {
            result.add(c);
          }
          break;
        default:
      }
    }
    return result;
  }
}

/// Returns the id of a comment.
/// Throws an error if `comment` is not a `Thing_Comment` or a `Thing_More`.
String commentId(Thing comment) {
  return switch (comment) {
    Thing_Comment(field0: final comment) => comment.id,
    Thing_More(id: final id) => id,
    _ => throw Exception("[_commentId] Expected Thing_Comment or Thing_More")
  };
}
