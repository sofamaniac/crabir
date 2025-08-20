import 'package:auto_route/auto_route.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/search_subreddits/bloc/search_bloc.dart';
import 'package:crabir/src/rust/third_party/reddit_api/search.dart';
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

class _SearchViewBody extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _SearchViewBodyState();
}

class _SearchViewBodyState extends State<_SearchViewBody> {
  _SearchViewBodyState();

  final TextEditingController _controller = TextEditingController();

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<SubredditSearchBloc>();
    final state = bloc.state;
    return Scaffold(
      appBar: AppBar(
        title: SearchBar(
          controller: _controller,
          hintText: "Search for subreddits",
          onSubmitted: (query) => bloc.add(Query(query)),
          onChanged: (query) => bloc.add(Query(query)),
          trailing: [
            if (_controller.text.isNotEmpty)
              IconButton(
                icon: const Icon(Icons.clear),
                onPressed: () {
                  _controller.clear();
                  setState(() {}); // rebuild so the button disappears
                },
              ),
          ],
        ),
        actions: [SortMenu()],
      ),
      body: Column(
        children: [
          if (state.query.isNotEmpty)
            InkWell(
              onTap: () => context.pushRoute(
                SearchPostsRoute(query: state.query),
              ),
              child: Text("Search post with \"${state.query}\""),
            ),
          if (state.query.isNotEmpty) Text("Go to user u/\"${state.query}\""),
          if (state.query.isNotEmpty) Divider(),
          Expanded(
            child: ListView.builder(
              itemCount: state.items.length + 1,
              itemBuilder: (context, index) {
                if (index < state.items.length) {
                  return SubredditTile(
                    state.items[index],
                  );
                } else {
                  if (!state.hasReachedMax && state.query.isNotEmpty) {
                    bloc.add(Fetch());
                    return LoadingIndicator();
                  } else if (state.hasReachedMax) {
                    return Text("Nothing more to show");
                  } else {
                    return Container();
                  }
                }
              },
            ),
          ),
        ],
      ),
    );
  }
}

class SortMenu extends StatelessWidget {
  const SortMenu({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<SubredditSearchBloc>();
    return MenuAnchor(
      menuChildren: [
        MenuItemButton(
          onPressed: () => bloc.add(
            SetSort(SubredditSearchSort.activity),
          ),
          child: Text("Activity"),
        ),
        MenuItemButton(
          onPressed: () => bloc.add(
            SetSort(SubredditSearchSort.relevance),
          ),
          child: Text("Relevance"),
        ),
      ],
      builder: (_, MenuController controller, Widget? child) {
        return IconButton(
          onPressed: () {
            if (controller.isOpen) {
              controller.close();
            } else {
              controller.open();
            }
          },
          icon: const Icon(Icons.sort),
        );
      },
    );
  }
}
