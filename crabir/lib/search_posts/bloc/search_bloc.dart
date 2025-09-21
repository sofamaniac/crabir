import 'dart:async';

import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/search.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_api;
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'search_event.dart';
part 'search_state.dart';
part 'search_bloc.freezed.dart';

class PostSearchBloc extends Bloc<PostSearchEvent, PostSearchState> {
  PostSearchBloc({
    this.query = "",
    String? subreddit,
    PostSearchSort sort = const PostSearchSort.hot(),
  })  : _sort = sort,
        _subreddit = subreddit,
        super(
          PostSearchState(
            query: query,
          ),
        ) {
    on<Query>(_query);
    on<Fetch>(_fetch);
    on<SetSort>(_setSort);
    on<RemoveSubreddit>(_removeSubreddit);
    on<Save>(_save);
    on<Vote>(_vote);
    if (query.isNotEmpty) {
      _streamable = RedditAPI.client().searchPost(
        query: query,
        subreddit: _subreddit,
        sort: _sort,
      );
    }
  }
  String query;
  String? _subreddit;

  PostSearchSort _sort;

  reddit_api.Streamable? _streamable;

  /// true if `streamable` has reached its end
  bool _hasReachedMax = false;

  Timer? _debounce;

  Future<void> _removeSubreddit(
      RemoveSubreddit _, Emitter<PostSearchState> emit) async {
    _subreddit = null;
    return _query(Query(query), emit);
  }

  Future<void> _save(Save event, Emitter<PostSearchState> emit) async {
    try {
      await _filter(emit);
    } catch (_) {
      emit(state.copyWith(status: StreamStatus.failure));
    }
  }

  Future<void> _vote(Vote vote, Emitter<PostSearchState> emit) async {
    try {
      await _filter(emit);
    } catch (_) {
      emit(state.copyWith(status: StreamStatus.failure));
    }
  }

  /// Create new stream and throttles api requests.
  /// If `debounce` is true, the request will be delayed.
  void _newStream({bool debounce = true}) {
    if (!debounce) {
      _streamable = RedditAPI.client().searchPost(
        query: query,
        subreddit: _subreddit,
        sort: _sort,
      );
      _hasReachedMax = false;
      return;
    }
    if (_debounce?.isActive ?? false) {
      _debounce!.cancel();
    }
    _debounce = Timer(const Duration(milliseconds: 200), () async {
      _streamable = RedditAPI.client().searchPost(
        query: query,
        subreddit: _subreddit,
        sort: _sort,
      );
      _hasReachedMax = false;
    });
  }

  Future<void> _query(
    Query newQuery,
    Emitter<PostSearchState> emit,
  ) async {
    if (newQuery.query.isEmpty) {
      emit(state.copyWith(query: query, items: []));
      return;
    } else if (newQuery.query != query) {
      query = newQuery.query;
      emit(state.copyWith(query: query));
      _newStream();
      await _filter(emit);
    }
  }

  Future<void> _fetch(Fetch _, Emitter<PostSearchState> emit) async {
    if (state.hasReachedMax) {
      return emit(state);
    } else if (_streamable == null) {
      _newStream(debounce: false);
    } else {
      _hasReachedMax = !await _streamable!.next();
      await _filter(emit);
    }
  }

  Future<void> _setSort(
    SetSort sort,
    Emitter<PostSearchState> emit,
  ) async {
    if (sort.sort == _sort) return;
    _sort = sort.sort;
    _newStream(debounce: false);
    emit(
      PostSearchState(
        sort: _sort,
        hasReachedMax: false,
        query: query,
      ),
    );
  }

  Future<void> _filter(Emitter<PostSearchState> emit) async {
    if (_streamable == null) return;
    final posts = (_streamable!.getAll())
        .whereType<Thing_Post>()
        .map((post) => post.field0);
    if (posts.length < 20 && !_hasReachedMax) {
      try {
        while (await _streamable!.next() && _streamable!.length < 20) {}
        _hasReachedMax = !await _streamable!.next();
        final posts = (_streamable!.getAll())
            .whereType<Thing_Post>()
            .map((post) => post.field0);
        emit(
          state.copyWith(
            hasReachedMax: _hasReachedMax,
            items: posts.toList(),
            query: query,
          ),
        );
      } catch (e) {
        emit(state.copyWith(status: StreamStatus.failure));
      }
    } else {
      emit(
        state.copyWith(
          hasReachedMax: _hasReachedMax,
          items: posts.toList(),
          query: query,
        ),
      );
    }
  }
}
