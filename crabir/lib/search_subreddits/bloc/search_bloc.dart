import 'dart:async';

import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart';
import 'package:crabir/src/rust/third_party/reddit_api/search.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_api;
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'search_event.dart';
part 'search_state.dart';
part 'search_bloc.freezed.dart';

class SubredditSearchBloc
    extends Bloc<SubredditSearchEvent, SubredditSearchState> {
  SubredditSearchBloc({this.query = ""}) : super(SubredditSearchState()) {
    on<Query>(_query);
    on<Fetch>(_fetch);
    on<SetSort>(_setSort);
  }
  String query;
  SearchSort sort = SearchSort.relevance;

  reddit_api.Streamable? streamable;

  /// true if `streamable` has reached its end
  bool hasReachedMax = false;

  Timer? _debounce;

  Future<void> _query(
    Query newQuery,
    Emitter<SubredditSearchState> emit,
  ) async {
    if (newQuery.query.isEmpty) {
      emit(state.copyWith(query: query, items: []));
      return;
    } else if (newQuery.query != query) {
      query = newQuery.query;
      emit(state.copyWith(query: query));
      // throttle
      if (_debounce?.isActive ?? false) {
        _debounce!.cancel();
      }
      _debounce = Timer(const Duration(milliseconds: 200), () async {
        streamable = RedditAPI.client().searchSubreddits(
          query: query,
          sort: sort,
        );
        hasReachedMax = false;
      });
      await _filter(emit);
    }
  }

  Future<void> _fetch(Fetch _, Emitter<SubredditSearchState> emit) async {
    if (state.hasReachedMax || streamable == null) {
      return emit(state);
    }
    hasReachedMax = !await streamable!.next();
    await _filter(emit);
  }

  Future<void> _setSort(
    SetSort sort,
    Emitter<SubredditSearchState> emit,
  ) async {
    this.sort = sort.sort;
    await _query(Query(query), emit);
  }

  Future<void> _filter(Emitter<SubredditSearchState> emit) async {
    if (streamable == null) return;
    final subreddits = (await streamable!.getAll())
        .whereType<Thing_Subreddit>()
        .map((sub) => sub.field0);
    if (subreddits.length < 20 && !hasReachedMax) {
      try {
        while (await streamable!.next() && await streamable!.length < 20) {}
        hasReachedMax = !await streamable!.next();
        final subreddits = (await streamable!.getAll())
            .whereType<Thing_Subreddit>()
            .map((sub) => sub.field0);
        emit(
          state.copyWith(
            hasReachedMax: hasReachedMax,
            items: subreddits.toList(),
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
          items: subreddits.toList(),
          query: query,
        ),
      );
    }
  }
}
