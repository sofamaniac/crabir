import 'package:crabir/drawer.dart';
import 'package:crabir/feed/post.dart';
import 'package:crabir/login.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:provider/provider.dart';

class FeedView extends StatelessWidget {
  const FeedView({super.key, required this.feed, required this.initialSort});
  final Feed feed;
  final Sort initialSort;

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) =>
          FeedStateProvider(state: FeedState(feed: feed, sort: initialSort)),
      child: const FeedViewBody(),
    );
  }
}

class FeedStateProvider extends ChangeNotifier {
  bool done = false;
  final FeedState state;

  FeedStateProvider({required this.state});

  int get length => state.length;

  Post? nth({required int n}) => state.nth(n: n);

  Future<void> next() async {
    if (done) return;

    final newDone = await state.next();
    done = newDone;

    notifyListeners();
  }

  String get title => state.title;
  String get sortString => state.sortString;

  set sort(Sort sort) {
    state.sort = sort;
    notifyListeners();
  }

  set feed(Feed feed) {
    state.feed = feed;
    notifyListeners();
  }

  void reset() {
    state.refresh();
    done = false;
    notifyListeners();
  }
}

class FeedViewBody extends StatefulWidget {
  const FeedViewBody({super.key});

  @override
  State<FeedViewBody> createState() => _FeedViewBodyState();
}

class _FeedViewBodyState extends State<FeedViewBody>
    with AutomaticKeepAliveClientMixin {
  bool _isLoading = false;
  Future<void>? _nextFuture;
  String _error = "";
  UserAccount? account;

  @override
  bool get wantKeepAlive => true;

  void _triggerLoadNext(FeedStateProvider state) {
    if (!_isLoading && !state.done && _nextFuture == null) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _isLoading = true;
        _error = "";
        _nextFuture = state.next().catchError((error) {
          _error = "$error";
        }).whenComplete(() {
          setState(() {
            _isLoading = false;
            _nextFuture = null;
          });
        });
      });
    }
  }

  void _triggerReset(FeedStateProvider state) {
    _nextFuture?.ignore();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      state.reset();
    });
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    final state = context.watch<FeedStateProvider>();
    final drawerModel = context.watch<DrawerModel>();

    if (drawerModel.currentAccount != account) {
      _triggerReset(state);
      account = drawerModel.currentAccount;
    }

    return CustomScrollView(
      slivers: [
        FeedTopBar(),
        SliverList.builder(
          itemCount: state.length + 1,
          itemBuilder: (context, index) {
            if (index == state.length) {
              _triggerLoadNext(state);
              if (_error.isNotEmpty) {
                return Padding(
                  padding: const EdgeInsets.all(16),
                  child: Text("Error: $_error",
                      style: const TextStyle(color: Colors.red)),
                );
              }
              return const Padding(
                padding: EdgeInsets.all(16),
                child: Center(child: CircularProgressIndicator()),
              );
            }

            final post = state.nth(n: index);
            return post != null
                ? RedditPostCard(post: post)
                : Text("${state.done}, ${state.length}");
          },
        ),
      ],
    );
  }
}

class FeedTopBar extends StatelessWidget {
  const FeedTopBar({super.key});
  @override
  Widget build(BuildContext context) {
    final state = context.watch<FeedStateProvider>().state;
    final title = state.title;
    final sort = state.sortString;

    return SliverAppBar(
      title: Column(
        children: [
          // TODO styling
          Text(title),
          Text(sort),
        ],
      ),
      actions: [
        IconButton(onPressed: () => (), icon: Icon(Icons.search)),
        SortMenu(),
      ],
      floating: true,
    );
  }
}

class SortMenu extends StatelessWidget {
  const SortMenu({super.key});
  @override
  Widget build(BuildContext context) {
    final state = context.watch<FeedStateProvider>();
    return PopupMenuButton(
      itemBuilder: (context) => [
        const PopupMenuItem(value: Sort.best(), child: Text("Best")),
        const PopupMenuItem(value: Sort.hot(), child: Text("Hot")),
        const PopupMenuItem(value: Sort.rising(), child: Text("Rising")),
      ],
      onSelected: (sort) => state.sort = sort,
    );
  }
}
