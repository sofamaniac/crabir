import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/post.dart';
import 'package:crabir/search_posts/bloc/search_bloc.dart';
import 'package:crabir/search_posts/widgets/sort_menu.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/settings/layout/layout_settings.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/flair.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/search.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

part 'search.go_route_ext.dart';

@CrabirRoute()
class SearchPostsView extends StatefulWidget {
  const SearchPostsView({
    super.key,
    this.query = "",
    this.flair,
    this.subreddit,
    this.initialSort,
  });
  final String query;
  final Flair? flair;
  final String? subreddit;
  final PostSearchSort? initialSort;
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
        sort: widget.initialSort ?? PostSearchSort_Hot(),
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
  State<_SearchViewBody> createState() => _SearchViewBodyState();
}

class _SearchViewBodyState extends State<_SearchViewBody> {
  late final TextEditingController _controller;
  bool _isSearching = false;

  @override
  void initState() {
    super.initState();
    _controller = TextEditingController(text: widget.initialQuery);
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<PostSearchBloc>();
    final state = bloc.state;
    final locales = AppLocalizations.of(context);

    return Scaffold(
      body: NestedScrollView(
        floatHeaderSlivers: true,
        headerSliverBuilder: (context, _) => [
          SliverAppBar(
            floating: true,
            title: AnimatedContainer(
              duration: const Duration(milliseconds: 250),
              curve: Curves.easeInOut,
              child: _isSearching
                  ? SearchBar(
                      controller: _controller,
                      hintText: locales.postSearchBar,
                      onSubmitted: (query) {
                        bloc.add(Query(query));
                        setState(() => _isSearching = false);
                      },
                      onChanged: (query) => bloc.add(Query(query)),
                      onTapOutside: (_) => setState(() => _isSearching = false),
                      trailing: [
                        if (_controller.text.isNotEmpty)
                          IconButton(
                            icon: const Icon(Icons.clear),
                            onPressed: () {
                              _controller.clear();
                              setState(() {});
                            },
                          ),
                      ],
                    )
                  : Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        // Top row: query text + search icon always visible
                        Row(
                          children: [
                            Expanded(
                              child: RichText(
                                text: TextSpan(
                                  text: state.query,
                                  style: TextStyle(
                                    fontSize: 20,
                                    fontWeight: FontWeight.w500,
                                  ),
                                  children: <TextSpan>[
                                    TextSpan(
                                      text:
                                          '\n${state.sort.labelWithTimeframe(context)}',
                                      style: TextStyle(
                                        fontSize: 16,
                                        fontWeight: FontWeight.normal,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                            IconButton(
                              icon: const Icon(Icons.search),
                              onPressed: () {
                                setState(() => _isSearching = true);
                              },
                            ),
                          ],
                        ),
                      ],
                    ),
            ),
            actions: [SortMenu()],
          ),
        ],
        body: ListView.builder(
          padding: const EdgeInsets.all(8),
          itemCount: state.items.length + 1,
          itemBuilder: (context, index) {
            if (index < state.items.length) {
              return _postView(context, state.items[index]);
            } else if (!state.hasReachedMax && state.query.isNotEmpty) {
              bloc.add(Fetch());
              return const LoadingIndicator();
            } else if (state.hasReachedMax) {
              return const Center(child: Text("Nothing more to show"));
            } else {
              return const SizedBox.shrink();
            }
          },
        ),
      ),
    );
  }
}

extension PostSearchSortString on PostSearchSort {
  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return switch (this) {
      PostSearchSort_Relevance() => locales.sortRelevance,
      PostSearchSort_Hot() => locales.sortHot,
      PostSearchSort_Top() => locales.sortTop,
      PostSearchSort_New() => locales.sortNew,
      PostSearchSort_Comments() => locales.sortComments,
    };
  }

  String labelWithTimeframe(BuildContext context) {
    final sort = label(context);
    final timeframe = _getTimeframe();
    if (timeframe != null) {
      final time = timeframe.label(context);
      return "$sort  · $time";
    } else {
      return sort;
    }
  }

  Timeframe? _getTimeframe() {
    return switch (this) {
      PostSearchSort_Relevance(field0: final timeframe) ||
      PostSearchSort_Top(field0: final timeframe) ||
      PostSearchSort_Comments(field0: final timeframe) =>
        timeframe,
      _ => null,
    };
  }
}

Widget _postView(BuildContext context, Post post) {
  final kind = LayoutSettings.of(context).defaultView;
  final enableThumbnail = LayoutSettings.of(context).showThumbnail;
  final state = context.read<PostSearchBloc>();
  void onTap() {
    context.read<ReadPosts>().mark(post.id);
    context.push(post.permalink, extra: post);
  }

  Future<void> onLikeCallback(VoteDirection direction) async {
    state.add(Vote(direction: direction, name: post.name));
  }

  Future<void> onSaveCallback(bool save) async {
    state.add(Save(save: save, name: post.name));
  }

  return switch (kind) {
    ViewKind.card => RedditPostCard(
        maxLines: 5,
        post: post,
        onTap: onTap,
        onLikeCallback: onLikeCallback,
        onSaveCallback: onSaveCallback,
        enableThumbnail: enableThumbnail,
        ignoreSelftextSpoiler: false,
        ignoreRead: false,
      ),
    ViewKind.compact => DenseCard(
        post: post,
        onTap: onTap,
        onLikeCallback: onLikeCallback,
        onSaveCallback: onSaveCallback,
        enableThumbnail: enableThumbnail,
        ignoreRead: false,
      ),
    ViewKind.dense => DenseCard(
        post: post,
        onTap: onTap,
        onLikeCallback: onLikeCallback,
        onSaveCallback: onSaveCallback,
        enableThumbnail: enableThumbnail,
        ignoreRead: false,
      )
  };
}
