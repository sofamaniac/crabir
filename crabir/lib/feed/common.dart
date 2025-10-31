import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/feed.dart';
import 'package:crabir/feed/scroll_aware_fab.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/post.dart';
import 'package:crabir/search_subreddits/widgets/search.dart' hide SortMenu;
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/settings/layout/layout_settings.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/settings/settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    hide Icon;
import 'package:crabir/src/rust/third_party/reddit_api/paging_handler.dart'
    as reddit_stream;
import 'package:crabir/stream/things_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

class CommonFeedView extends StatefulWidget {
  final reddit_stream.PagingHandler Function(FeedSort) newStream;
  final Widget Function(FeedSort) title;
  final Subreddit? subredditAbout;
  final FeedSort? initialSort;
  final Layout layout;
  final void Function(FeedSort) onSortChanged;
  final void Function(Layout) onLayoutChanged;
  final Widget? endDrawer;

  const CommonFeedView({
    super.key,
    required this.newStream,
    required this.title,
    this.initialSort,
    this.subredditAbout,
    required this.layout,
    required this.onSortChanged,
    required this.onLayoutChanged,
    this.endDrawer,
  });

  @override
  State<CommonFeedView> createState() => _CommonFeedViewState();
}

class _CommonFeedViewState extends State<CommonFeedView> {
  FeedSort? sort;

  late final ScrollController _scrollController = ScrollController();
  late final MenuController _menuController = MenuController();
  late reddit_stream.PagingHandler _stream;
  String? currentUser;
  int _forceRebuild = 0;

  @override
  void initState() {
    super.initState();
    _initializeSort();
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

  void _rebuild() {
    _forceRebuild += 1;
    setState(() {});
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
            SearchSubredditsView().goNamed(context);
          },
        ),
        SortMenu(
          onSelect: (sort) {
            widget.onSortChanged(sort);
            this.sort = sort;
            _stream = widget.newStream(sort);
            _rebuild();
          },
        ),
        _moreOptions(),
      ],
    );
  }

  Widget _moreOptions() {
    return MenuAnchor(
      controller: _menuController,
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
        MenuItemButton(
            onPressed: () {
              _stream.refresh();
              _rebuild();
              _menuController.close();
            },
            child: Text("Refresh")),
        MenuItemButton(
          onPressed: () {
            SettingsView().pushNamed(context);
            _menuController.close();
          },
          child: Text("Settings"),
        ),
        RadioGroup<ViewKind>(
          onChanged: (view) {
            final newLayout = widget.layout.withView(view);
            widget.onLayoutChanged(newLayout);
            setState(() {});
            _menuController.close();
          },
          groupValue: widget.layout.view,
          child: SubmenuButton(
            menuChildren: ViewKind.values
                .map(
                  (view) => MenuItemButton(
                    child: RadioListTile(
                      value: view,
                      title: Text(view.label(context)),
                    ),
                  ),
                )
                .toList(),
            child: Text("Post View"),
          ),
        ),
        RadioGroup<int>(
          onChanged: (columns) {
            final newLayout = widget.layout.withColumns(columns);
            widget.onLayoutChanged(newLayout);
            setState(() {});
            _menuController.close();
          },
          groupValue: widget.layout.columns,
          child: SubmenuButton(
            menuChildren: [1, 2, 3]
                .map(
                  (c) => MenuItemButton(
                    child: RadioListTile(
                      value: c,
                      title: Text("$c"),
                    ),
                  ),
                )
                .toList(),
            child: Text("Columns"),
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    final account = AccountState.of(context).account;
    final status = AccountState.of(context).status;

    if (sort == null) {
      return const Center(child: LoadingIndicator());
    }

    // Reset stream when changing user
    if (currentUser != account?.username) {
      currentUser = account?.username;
      if (currentUser != null) {
        setState(() {
          _stream = widget.newStream(sort!);
        });
      }
    }

    return switch (status) {
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
                body: ThingsScaffold(
                  key: ValueKey(_forceRebuild),
                  stream: _stream,
                  postView: (context, post, hide) => postView(
                    context,
                    post,
                    hide,
                    widget.layout.view,
                  ),
                  columnCount: widget.layout.columns,
                  subredditInfo: widget.subredditAbout != null
                      ? SubredditInfoView(infos: widget.subredditAbout!)
                      : null,
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

  Widget postView(BuildContext context, Post post, bool hide, ViewKind kind) {
    final layout = LayoutSettings.of(context);
    return switch (kind) {
      ViewKind.card => RedditPostCard(
          maxLines: layout.previewTextLength,
          showSelfText: layout.previewText,
          post: post,
          onTap: () {
            context.read<ReadPosts>().mark(post.id);
            context.push(post.permalink, extra: post);
          },
          respectHidden: hide,
          enableThumbnail: layout.showThumbnail,
          ignoreSelftextSpoiler: false,
          ignoreRead: false,
        ),
      ViewKind.compact => CompactCard(
          post: post,
          onTap: () {
            context.read<ReadPosts>().mark(post.id);
            context.push(post.permalink, extra: post);
          },
          respectHidden: hide,
          enableThumbnail: layout.showThumbnail,
          ignoreRead: false,
        ),
      ViewKind.dense => DenseCard(
          post: post,
          onTap: () {
            context.read<ReadPosts>().mark(post.id);
            context.push(post.permalink, extra: post);
          },
          respectHidden: hide,
          enableThumbnail: layout.showThumbnail,
          ignoreRead: false,
          hideBottomBar: false,
        )
    };
  }
}
