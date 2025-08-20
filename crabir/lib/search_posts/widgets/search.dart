import 'package:auto_route/annotations.dart';
import 'package:auto_route/auto_route.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/search_posts/bloc/search_bloc.dart';
import 'package:crabir/search_posts/widgets/sort_menu.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/flair.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

@RoutePage()
class SearchPostsView extends StatefulWidget {
  const SearchPostsView({
    super.key,
    this.query = "",
    this.flair,
    this.subreddit,
  });
  final String query;
  final Flair? flair;
  final String? subreddit;
  @override
  State<StatefulWidget> createState() => _SearchSubredditsViewState();
}

class _SearchSubredditsViewState extends State<SearchPostsView> {
  @override
  Widget build(BuildContext context) {
    final String query;
    if (widget.flair == null) {
      query = widget.query;
    } else {
      query = "flair:\"${widget.flair!.text!}\" ${widget.query}";
    }
    return BlocProvider(
      create: (_) => PostSearchBloc(
        query: query,
        subreddit: widget.subreddit,
      ),
      child: _SearchViewBody(
        initialQuery: query,
      ),
    );
  }
}

class _SearchViewBody extends StatefulWidget {
  final String initialQuery;
  const _SearchViewBody({required this.initialQuery});
  @override
  State<StatefulWidget> createState() => _SearchViewBodyState();
}

class _SearchViewBodyState extends State<_SearchViewBody> {
  _SearchViewBodyState();

  late final TextEditingController _controller;

  @override
  void initState() {
    super.initState();
    _controller = TextEditingController(text: widget.initialQuery);
  }

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<PostSearchBloc>();
    final state = bloc.state;
    return Scaffold(
      appBar: AppBar(
        title: SearchBar(
          controller: _controller,
          hintText: "Search for posts",
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
      body: ListView.builder(
        itemCount: state.items.length + 1,
        itemBuilder: (context, index) {
          if (index < state.items.length) {
            return RedditPostCard(post: state.items[index]);
          } else if (!state.hasReachedMax && state.query.isNotEmpty) {
            bloc.add(Fetch());
            return LoadingIndicator();
          } else if (state.hasReachedMax) {
            return Text("Nothing more to show");
          } else {
            return Container();
          }
        },
      ),
    );
  }
}
