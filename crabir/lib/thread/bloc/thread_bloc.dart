import 'dart:collection';

import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:logging/logging.dart';

part 'thread_event.dart';
part 'thread_state.dart';

class ThreadBloc extends Bloc<ThreadEvent, ThreadState> {
  final Set<String> _collapsed = {};
  final Set<String> _hidden = {};
  final HashMap<String, List<Thing>> _moreLoaded = HashMap();
  List<Thing> _comments = [];
  bool _loaded = false;
  late final Post _post;
  final String permalink;
  final Logger log = Logger("ThreadBloc");

  ThreadBloc(this.permalink) : super(ThreadState()) {
    on<Load>(_initialize);
    on<Collapse>(_collapse);
    on<LoadMore>(_loadMore);
    on<Refresh>(_refresh);
  }

  Future<void> _initialize(Load _, Emitter<ThreadState> emit) async {
    if (_loaded) {
      return;
    }

    try {
      final things = await RedditAPI.client().comments(permalink: permalink);
      _post = things.$1;
      _comments = things.$2;
      _loaded = true;
      log.info("comments (${_comments.length}) and post loaded");
      emit(state.copyWith(post: _post, flatComments: flatten(_comments)));
    } catch (e) {
      emit(state.copyWith(status: Status.failure));
    }
  }

  Future<void> _refresh(Refresh _, Emitter<ThreadState> emit) async {
    emit(state.copyWith(status: Status.unloaded));
    try {
      final things = await RedditAPI.client().comments(permalink: permalink);
      _comments = things.$2;
      _loaded = true;
      log.info("comments (${_comments.length}) refreshed");
      emit(state.copyWith(flatComments: flatten(_comments)));
    } catch (e) {
      emit(state.copyWith(status: Status.failure));
    }
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
    emit(state.copyWith(collapsed: _collapsed, hidden: _hidden));
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
      final Logger log = Logger("_loadMore");
      final newThings = await RedditAPI.client().loadMoreComments(
        parentId: _post.name,
        children: event.more.children,
      );
      _moreLoaded[event.more.id] = newThings;
      emit(state.copyWith(flatComments: flatten(_comments)));
    } catch (e) {
      emit(state.copyWith(status: Status.failure));
    }
  }

  List<Thing> flatten(
    List<Thing> comments,
  ) {
    final result = <Thing>[];
    for (final c in comments) {
      if (c is Thing_Comment) {
        result.add(c);
        result.addAll(flatten(c.field0.replies()));
      } else if (c is Thing_More) {
        if (_moreLoaded.containsKey(c.id)) {
          result.addAll(flatten(_moreLoaded[c.id]!));
        } else {
          result.add(c);
        }
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
