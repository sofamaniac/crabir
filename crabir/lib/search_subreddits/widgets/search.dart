import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/search_posts/widgets/search.dart';
import 'package:crabir/search_subreddits/bloc/search_bloc.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:crabir/src/rust/third_party/reddit_api/search.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

part 'search.go_route_ext.dart';

@CrabirRoute()
class SearchSubredditsView extends StatelessWidget {
  final bool enableUser;
  final bool enablePost;

  /// When set to true, navigate to the selected subreddit, otherwise calls `context.pop(subreddit)`.
  final bool navigateOnTap;
  const SearchSubredditsView({
    super.key,
    this.enablePost = true,
    this.enableUser = true,
    this.navigateOnTap = true,
  });

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (_) => SubredditSearchBloc(query: ""),
      child: _SearchViewBody(
        enablePost: enablePost,
        enableUser: enableUser,
        navigateOnTap: navigateOnTap,
      ),
    );
  }
}

class _SearchViewBody extends StatefulWidget {
  final bool enableUser;
  final bool enablePost;
  final bool navigateOnTap;

  const _SearchViewBody({
    required this.enableUser,
    required this.enablePost,
    required this.navigateOnTap,
  });
  @override
  State<StatefulWidget> createState() => _SearchViewBodyState();
}

class _SearchViewBodyState extends State<_SearchViewBody> {
  _SearchViewBodyState();

  final TextEditingController _controller = TextEditingController();

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<SubredditSearchBloc>();
    final state = bloc.state;
    final subs = context.watch<AccountsBloc>().state.subscriptions;
    final locales = AppLocalizations.of(context);
    return Scaffold(
      appBar: AppBar(
        title: SearchBar(
          controller: _controller,
          hintText: locales.subredditsSearchBar,
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
          if (state.query.isNotEmpty && widget.enablePost)
            ListTile(
              leading: Icon(Icons.search),
              title: Text("Search for posts with \"${state.query}\""),
              onTap: () =>
                  SearchPostsView(query: state.query).pushNamed(context),
            ),
          if (state.query.isNotEmpty && widget.enableUser)
            ListTile(
              leading: Icon(Icons.person),
              title: Text("Go to user u/\"${state.query}\""),
              onTap: () => ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text("Not implemented"),
                ),
              ),
            ),
          if (widget.enableUser || widget.enablePost) Divider(),
          if (state.query.isNotEmpty)
            Expanded(
              child: ListView.builder(
                itemCount: state.items.length + 1,
                itemBuilder: (context, index) {
                  if (index < state.items.length) {
                    return SubredditTile(
                      state.items[index],
                      onTap: () {
                        if (widget.navigateOnTap) {
                          context
                              .go("/r/${state.items[index].other.displayName}");
                        } else {
                          context.pop(state.items[index]);
                        }
                      },
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
            )
          else
            Expanded(
              child: ListView.builder(
                itemCount: subs.length,
                itemBuilder: (context, index) {
                  return SubredditTile(
                    subs[index],
                    onTap: () {
                      if (widget.navigateOnTap) {
                        context.go("/r/${subs[index].other.displayName}");
                      } else {
                        context.pop(subs[index]);
                      }
                    },
                  );
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
    final locales = AppLocalizations.of(context);
    return MenuAnchor(
      menuChildren: [
        MenuItemButton(
          onPressed: () => bloc.add(
            SetSort(SubredditSearchSort.activity),
          ),
          child: Text(locales.sortActivity),
        ),
        MenuItemButton(
          onPressed: () => bloc.add(
            SetSort(SubredditSearchSort.relevance),
          ),
          child: Text(locales.sortRelevance),
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
