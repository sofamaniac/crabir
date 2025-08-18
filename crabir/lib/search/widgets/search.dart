import 'dart:async';

import 'package:auto_route/auto_route.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/search/bloc/search_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

@RoutePage()
class SearchSubredditsView extends StatefulWidget {
  const SearchSubredditsView({super.key});
  @override
  State<StatefulWidget> createState() => _SearchSubredditsViewState();
}

class _SearchSubredditsViewState extends State<SearchSubredditsView> {
  String query = "";
  @override
  Widget build(BuildContext context) {
    return BlocProvider(
        create: (_) => SubredditSearchBloc(query: query),
        child: _SearchViewBody());
  }
}

class _SearchViewBody extends StatelessWidget {
  const _SearchViewBody();

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<SubredditSearchBloc>();
    final state = bloc.state;
    return Scaffold(
      appBar: AppBar(
        title: SearchBar(
          hintText: "Search for subreddits",
          onSubmitted: (query) => bloc.add(Query(query)),
          onChanged: (query) => bloc.add(Query(query)),
        ),
      ),
      body: ListView.builder(
          itemCount: state.items.length + 1,
          itemBuilder: (context, index) {
            if (index < state.items.length) {
              return SubredditTile(
                sub: state.items[index],
              );
            } else {
              if (!state.hasReachedMax) {
                bloc.add(Fetch());
                return LoadingIndicator();
              } else {
                return Text("Nothing more to show");
              }
            }
          }),
    );
  }
}
