import 'dart:async';

import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart';
import 'package:crabir/src/rust/third_party/reddit_api/search.dart';
import 'package:crabir/src/rust/third_party/reddit_api/paging_handler.dart'
    as reddit_api;
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:rxdart/rxdart.dart';

part 'search_event.dart';
part 'search_state.dart';
part 'search_bloc.freezed.dart';

class SubredditSearchBloc
    extends Bloc<SubredditSearchEvent, SubredditSearchState> {
  SubredditSearchBloc({this.query = ""}) : super(SubredditSearchState()) {
    on<Query>(_query);
    on<Fetch>(
      _fetch,
      transformer: (events, mapper) {
        return events
            .debounceTime(const Duration(milliseconds: 200))
            .switchMap(mapper);
      },
    );
    on<SetSort>(_setSort);
  }
  String query;
  SubredditSearchSort sort = SubredditSearchSort.relevance;

  reddit_api.PagingHandler? streamable;

  /// true if `streamable` has reached its end
  bool hasReachedMax = false;

  /// Create new stream and throttles api requests.
  void _newStream({bool debounce = true}) {
    streamable = RedditAPI.client().searchSubreddits(
      query: query,
      sort: sort,
    );
    hasReachedMax = false;
  }

  Future<void> _query(
    Query newQuery,
    Emitter<SubredditSearchState> emit,
  ) async {
    if (newQuery.query != query) {
      query = newQuery.query;
      print("QUERY: $query");
      _newStream();
      emit(state.copyWith(query: query, hasReachedMax: false));
      add(Fetch());
    }
  }

  Future<void> _fetch(Fetch _, Emitter<SubredditSearchState> emit) async {
    if (state.hasReachedMax || streamable == null) {
      return emit(state);
    }

    await streamable!.next();
    hasReachedMax = !streamable!.done;
    await _filter(emit);
  }

  Future<void> _setSort(
    SetSort sort,
    Emitter<SubredditSearchState> emit,
  ) async {
    if (sort.sort == this.sort) {
      return;
    }
    this.sort = sort.sort;
    _newStream(debounce: false);
    emit(SubredditSearchState(query: query));
  }

  Future<void> _filter(Emitter<SubredditSearchState> emit) async {
    if (streamable == null) return;
    final subreddits = (streamable!.getAll())
        .whereType<Thing_Subreddit>()
        .map((sub) => sub.field0);
    if (subreddits.length < 10 && !hasReachedMax) {
      try {
        while (!streamable!.done && streamable!.length < 20) {
          await streamable!.next();
        }
        final subreddits = (streamable!.getAll())
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
