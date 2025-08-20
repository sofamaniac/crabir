import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/scroll_aware_fab.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/feed/top_bar.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
import 'package:crabir/stream/bloc/stream_bloc.dart';
import 'package:crabir/stream/things_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

@RoutePage()
class FeedView extends StatelessWidget {
  const FeedView({
    super.key,
    required this.feed,
    this.initialSort,
  });
  final Feed feed;
  final FeedSort? initialSort;

  @override
  Widget build(BuildContext context) {
    return FeedViewBody(
      feed: feed,
      initialSort: initialSort,
    );
  }
}

class FeedViewBody extends StatefulWidget {
  final Feed feed;
  final FeedSort? initialSort;

  const FeedViewBody({
    super.key,
    required this.feed,
    this.initialSort,
  });

  @override
  State<FeedViewBody> createState() => _FeedViewBodyState();
}

class _FeedViewBodyState extends State<FeedViewBody>
    with AutomaticKeepAliveClientMixin {
  FeedSort? sort;

  late final ScrollController _scrollController;
  late reddit_stream.Streamable _stream;

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();
    _initializeSort();
    _scrollController = ScrollController();
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  void _initializeSort() {
    // TODO: read from user option for preferred sort.
    sort = widget.initialSort ?? FeedSort.best();
    _stream = RedditAPI.client().feedStream(
      feed: widget.feed,
      sort: sort!,
    );
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);

    final account = context.watch<AccountsBloc>().state;

    if (sort == null) {
      return Center(child: LoadingIndicator());
    }

    return switch (account.status) {
      Uninit() => Container(),
      Failure(:final message) =>
        Center(child: Text("Failure in Account Manager: $message")),
      Loaded() => Stack(
          children: [
            NestedScrollView(
              controller: _scrollController,
              headerSliverBuilder: (context, __) => [
                SliverAppBar(
                  floating: true,
                  title: FeedTitle(feed: widget.feed, sort: sort!),
                  actions: [
                    IconButton(
                      icon: Icon(Icons.search),
                      onPressed: () {
                        context.router.navigate(SearchSubredditsRoute());
                      },
                    ),
                    SortMenu(
                      onSelect: (sort) {
                        setState(() {
                          this.sort = sort;
                          _stream = RedditAPI.client().feedStream(
                            feed: widget.feed,
                            sort: sort,
                          );
                        });
                      },
                    ),
                  ],
                ),
              ],
              floatHeaderSlivers: true,
              body: ThingsScaffold(
                stream: _stream,
                postView: postView,
              ),
            ),
            Positioned(
              bottom: 16,
              right: 16,
              child: AnimatedSwitcher(
                duration: Duration(milliseconds: 200),
                child: ScrollAwareFab(
                  scrollController: _scrollController,
                ),
              ),
            ),
          ],
        ),
      _ => Center(child: LoadingIndicator())
    };
  }
}

Widget postView(BuildContext context, Post post) {
  final state = context.read<StreamBloc>();
  return RedditPostCard(
    maxLines: 5,
    post: post,
    onLike: (direction) async {
      state.add(Vote(direction: direction, name: post.name));
    },
    onSave: (save) async {
      state.add(Save(saved: save, name: post.name));
    },
    onTap: () => context.router.navigate(
      ThreadRoute(permalink: post.permalink, post: post),
    ),
  );
}
