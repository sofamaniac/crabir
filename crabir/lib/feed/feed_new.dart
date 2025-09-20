import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/feed.dart';
import 'package:crabir/feed/scroll_aware_fab.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    hide Icon;
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
import 'package:crabir/stream/things_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class FeedViewBodyNew extends StatefulWidget {
  final reddit_stream.Streamable Function(FeedSort) newStream;
  final Widget Function(FeedSort) title;
  final Subreddit? subredditAbout;
  final FeedSort? initialSort;
  final void Function(FeedSort) onSortChanged;
  final Widget? endDrawer;

  const FeedViewBodyNew({
    super.key,
    required this.newStream,
    required this.title,
    this.initialSort,
    this.subredditAbout,
    required this.onSortChanged,
    this.endDrawer,
  });

  @override
  State<FeedViewBodyNew> createState() => _FeedViewBodyNewState();
}

class _FeedViewBodyNewState extends State<FeedViewBodyNew>
    with AutomaticKeepAliveClientMixin {
  FeedSort? sort;

  late final ScrollController _scrollController;
  late reddit_stream.Streamable _stream;
  String? currentUser;
  int _forceRebuild = 0;

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
    _stream = widget.newStream(sort!);
  }

  Widget _appBar() {
    final theme = CrabirTheme.of(context);
    return SliverAppBar(
      floating: true,
      leading: IconButton(
        icon: const Icon(Icons.menu),
        onPressed: () {
          Scaffold.of(context).openDrawer();
        },
      ),
      title: widget.title(sort!),
      backgroundColor: theme.toolBarBackground,
      actions: [
        IconButton(
          icon: Icon(Icons.search),
          onPressed: () {
            context.router.navigate(SearchSubredditsRoute());
          },
        ),
        SortMenu(
          onSelect: (sort) {
            widget.onSortChanged(sort);
            setState(() {
              this.sort = sort;
              _forceRebuild += 1;
              _stream = widget.newStream(sort);
            });
          },
        ),
        _moreOptions(),
      ],
    );
  }

  // TODO: allow for custom actions to be passed
  Widget _moreOptions() {
    return MenuAnchor(
      builder: (BuildContext context, controller, _) {
        return IconButton(
          icon: const Icon(Icons.more_vert),
          onPressed: () {
            if (controller.isOpen) {
              controller.close();
            } else {
              controller.open();
            }
          },
        );
      },
      menuChildren: [
        MenuItemButton(onPressed: _stream.refresh, child: Text("Refresh")),
        MenuItemButton(
          onPressed: () {
            context.router.navigate(SettingsRoute());
          },
          child: Text("Settings"),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);

    final account = context.watch<AccountsBloc>().state;

    if (sort == null) {
      return const Center(child: LoadingIndicator());
    }

    // Reset stream when changing user
    if (currentUser != account.account?.username) {
      currentUser = account.account?.username;
      if (currentUser != null) {
        setState(() {
          _stream = widget.newStream(sort!);
        });
      }
    }

    return switch (account.status) {
      Uninit() => Container(),
      Failure(:final message) =>
        Center(child: Text("Failure in Account Manager: $message")),
      Loaded() => Scaffold(
          endDrawer: widget.endDrawer,
          body: Stack(
            children: [
              NestedScrollView(
                controller: _scrollController,
                floatHeaderSlivers: true,
                headerSliverBuilder: (context, __) => [
                  _appBar(), // your SliverAppBar
                ],
                body: RefreshIndicator(
                  onRefresh: () async {
                    await _stream.refresh();
                    setState(() {
                      _forceRebuild += 1;
                    });
                  },
                  child: Scrollbar(
                    child: ThingsScaffold(
                      key: ValueKey(_forceRebuild),
                      stream: _stream,
                      postView: postView,
                      subredditInfo: widget.subredditAbout != null
                          ? SubredditInfoView(infos: widget.subredditAbout!)
                          : null,
                    ),
                  ),
                ),
              ),

              // Floating action button overlay
              Positioned(
                bottom: 16,
                right: 16,
                child: AnimatedSwitcher(
                  duration: const Duration(milliseconds: 200),
                  child: ScrollAwareFab(
                    scrollController: _scrollController,
                  ),
                ),
              ),
            ],
          ),
        ),
      _ => const Center(child: LoadingIndicator()),
    };
  }

  Widget postView(BuildContext context, Post post) {
    return RedditPostCard(
      maxLines: 5,
      post: post,
      onLikeCallback: (direction) async {
        await _stream.vote(
          name: post.name,
          direction: direction,
          client: RedditAPI.client(),
        );
      },
      onSaveCallback: (save) async {
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
