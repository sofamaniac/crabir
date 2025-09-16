import 'package:auto_route/auto_route.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/scroll_aware_fab.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/feed/top_bar.dart';
import 'package:crabir/html_view.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/main.dart';
import 'package:crabir/post/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    hide Icon, SubredditIcon;
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
import 'package:crabir/stream/things_view.dart';
import 'package:crabir/subreddit.dart';
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
  Subreddit? subredditAbout;

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();
    _initializeSort();
    _scrollController = ScrollController();
    _loadAbout();
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  Future<void> _loadAbout() async {
    switch (widget.feed) {
      case Feed_Subreddit(field0: final subreddit):
        subredditAbout =
            await RedditAPI.client().subredditAbout(subreddit: subreddit);
        if (!mounted) return;
        setState(() {});
      default:
    }
  }

  void _initializeSort() {
    final settings = context.read<PostsSettingsCubit>().state;
    sort = widget.initialSort ?? settings.defaultSort;
    _stream = RedditAPI.client().feedStream(
      feed: widget.feed,
      sort: sort!,
    );
  }

  Widget _appBar() {
    final title = FeedTitle(feed: widget.feed, sort: sort!);
    return SliverAppBar(
      floating: true,
      title: title,
      backgroundColor:
          subredditAbout != null ? Colors.transparent : Colors.black,
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
        _moreOptions(),
      ],
    );
  }

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
          _stream = RedditAPI.client().feedStream(
            feed: widget.feed,
            sort: sort!,
          );
        });
      }
    }

    // Update end drawer after build
    WidgetsBinding.instance.addPostFrameCallback((_) {
      DrawerHost.of(context).setEndDrawer(endDrawer());
    });

    return switch (account.status) {
      Uninit() => Container(),
      Failure(:final message) =>
        Center(child: Text("Failure in Account Manager: $message")),
      Loaded() => Stack(
          children: [
            // NestedScrollView with sliver-safe body
            NestedScrollView(
              controller: _scrollController,
              floatHeaderSlivers: true,
              headerSliverBuilder: (context, __) => [
                _appBar(), // your SliverAppBar
              ],
              body: RefreshIndicator(
                onRefresh: _stream.refresh,
                child: Scrollbar(
                  child: ThingsScaffold(
                    // Ensure rebuild when sort changes.
                    key: ValueKey(sort),
                    stream: _stream,
                    postView: postView,
                    subredditInfo: subredditAbout != null
                        ? SubredditInfoView(infos: subredditAbout!)
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

  Widget? endDrawer() {
    if (subredditAbout != null) {
      return Drawer(
        child: StyledHtml(
          htmlContent: subredditAbout?.descriptionHtml ?? "",
        ),
      );
    } else {
      return null;
    }
  }
}

class SubredditInfoView extends StatelessWidget {
  final Subreddit infos;

  const SubredditInfoView({super.key, required this.infos});

  @override
  Widget build(BuildContext context) {
    final Widget icon;
    icon = SubredditIcon(
      icon: infos.other.icon,
      radius: 40,
    );
    return Column(
      children: [
        // banner and icon
        Stack(
          clipBehavior: Clip.none,
          children: [
            _banner(),
            Positioned(
              left: 16,
              top: 50,
              child: icon,
            ),
          ],
        ),
        // Space for avatar overlap
        const SizedBox(height: 40 + 8),

        // Username + subtitle
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Expanded(
                    child: Text(
                      infos.other.displayName,
                      style: Theme.of(context).textTheme.titleLarge,
                      overflow: TextOverflow.clip,
                    ),
                  ),
                  ElevatedButton.icon(
                    onPressed: () {},
                    icon: const Icon(Icons.check_circle),
                    label: const Text("Joined"),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.green,
                      foregroundColor: Colors.white,
                      minimumSize: Size.zero,
                      tapTargetSize: MaterialTapTargetSize.shrinkWrap,
                      padding: EdgeInsetsGeometry.all(8),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(4),
                      ),
                    ),
                  ),
                  IconButton.outlined(
                    onPressed: () {},
                    icon: const Icon(Icons.more_vert),
                  ),
                ],
              ),
              Text(
                "${infos.other.subscribers} members · ${infos.activeUserCount} online",
                style: Theme.of(context)
                    .textTheme
                    .bodyMedium
                    ?.copyWith(color: Colors.white70),
              ),
              if (infos.publicDescriptionHtml != null)
                StyledHtml(htmlContent: infos.publicDescriptionHtml!),
              Divider(),
            ],
          ),
        ),
      ],
    );
  }

  Widget _banner() {
    if (infos.bannerBackgroundImage != null &&
        infos.bannerBackgroundImage!.isNotEmpty) {
      return Align(
        alignment: Alignment.topCenter,
        child: CachedNetworkImage(
          imageUrl: infos.bannerBackgroundImage!,
          fit: BoxFit.cover,
          height: 100,
          width: double.infinity,
        ),
      );
    } else {
      return SizedBox(
        height: 100,
        width: double.infinity,
      );
    }
  }
}
