import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/scroll_aware_fab.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/feed/top_bar.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
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

  /// Override the setting-defined preferred sort.
  final FeedSort? initialSort;

  @override
  Widget build(BuildContext context) {
    final _ = context.watch<AccountsBloc>().state;
    return FeedViewBody(
      key: ValueKey(feed),
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
  String? currentUser;

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
    final settings = context.read<PostsSettingsCubit>().state;
    sort = widget.initialSort ?? settings.defaultSort;
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
    } else if (currentUser != account.account?.username) {
      // reset stream when changing user.
      currentUser = account.account?.username;
      if (currentUser != null) {
        setState(() {
          _stream = RedditAPI.client().feedStream(
            feed: widget.feed,
            sort: sort!,
          );
        });
      }
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

  Widget postView(BuildContext context, Post post) {
    return RedditPostCard(
      maxLines: 5,
      post: post,
      onLike: (direction) async {
        await _stream.vote(
          name: post.name,
          direction: direction,
          client: RedditAPI.client(),
        );
      },
      onSave: (save) async {
        await _stream.save(
          name: post.name,
          save: save,
          client: RedditAPI.client(),
        );
      },
      onTap: () => context.router.navigate(
        ThreadRoute(
          post: post,
        ),
      ),
    );
  }
}
