import 'dart:async';

import 'package:crabir/src/rust/api/simple.dart';
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
    this.subreddit,
    this.sort = const PostSearchSort.hot(),
  }) : super(
          PostSearchState(
            query: query,
          ),
        ) {
    on<Query>(_query);
    on<Fetch>(_fetch);
    on<SetSort>(_setSort);
    on<RemoveSubreddit>(_removeSubreddit);
    if (query.isNotEmpty) {
      streamable = RedditAPI.client().searchPost(
        query: query,
        subreddit: subreddit,
        sort: sort,
      );
    }
  }
  String query;
  String? subreddit;

  PostSearchSort sort;
  DateTime timeSinceQuery = DateTime.now();

  reddit_api.Streamable? streamable;

  /// true if `streamable` has reached its end
  bool hasReachedMax = false;

  Timer? _debounce;

  Future<void> _removeSubreddit(
      RemoveSubreddit _, Emitter<PostSearchState> emit) async {
    subreddit = null;
    return _query(Query(query), emit);
  }

  /// Create new stream and throttles api requests.
  /// If `debounce` is true, the request will be delayed.
  void _newStream({bool debounce = true}) {
    if (!debounce) {
      streamable = RedditAPI.client().searchPost(
        query: query,
        subreddit: subreddit,
        sort: sort,
      );
      hasReachedMax = false;
      return;
    }
    if (_debounce?.isActive ?? false) {
      _debounce!.cancel();
    }
    _debounce = Timer(const Duration(milliseconds: 200), () async {
      streamable = RedditAPI.client().searchPost(
        query: query,
        subreddit: subreddit,
        sort: sort,
      );
      hasReachedMax = false;
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
    } else if (streamable == null) {
      _newStream(debounce: false);
    } else {
      hasReachedMax = !await streamable!.next();
      await _filter(emit);
    }
  }

  Future<void> _setSort(
    SetSort sort,
    Emitter<PostSearchState> emit,
  ) async {
    if (sort.sort == this.sort) return;
    this.sort = sort.sort;
    _newStream(debounce: false);
    emit(PostSearchState(sort: this.sort));
  }

  Future<void> _filter(Emitter<PostSearchState> emit) async {
    if (streamable == null) return;
    final posts = (await streamable!.getAll())
        .whereType<Thing_Post>()
        .map((post) => post.field0);
    if (posts.length < 20 && !hasReachedMax) {
      try {
        while (await streamable!.next() && await streamable!.length < 20) {}
        hasReachedMax = !await streamable!.next();
        final posts = (await streamable!.getAll())
            .whereType<Thing_Post>()
            .map((post) => post.field0);
        emit(
          state.copyWith(
            hasReachedMax: hasReachedMax,
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
          hasReachedMax: hasReachedMax,
          items: posts.toList(),
          query: query,
        ),
      );
    }
  }
}
