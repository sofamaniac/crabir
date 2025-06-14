import 'package:crabir/accounts_manager.dart';
import 'package:crabir/feed/post.dart';
import 'package:crabir/login.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/thread/thread.dart';
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

  Future<void> vote(int index, VoteDirection direction) =>
      state.vote(index: index, direction: direction);
  Future<void> save(int index, bool save) =>
      state.save(index: index, save: save);

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

  Widget itemView(BuildContext context, int index) {
    final state = context.watch<FeedStateProvider>();
    final appState = context.watch<AccountsManager>();
    if (appState.currentUser == null) {
      return const Padding(
        padding: EdgeInsets.all(16),
        child: Center(child: CircularProgressIndicator()),
      );
    }
    if (index == state.length) {
      _triggerLoadNext(state);
      if (_error.isNotEmpty) {
        return Padding(
          padding: const EdgeInsets.all(16),
          child:
              Text("Error: $_error", style: const TextStyle(color: Colors.red)),
        );
      }
      return const Padding(
        padding: EdgeInsets.all(16),
        child: Center(child: CircularProgressIndicator()),
      );
    }
    final post = state.nth(n: index);
    return post != null
        ? InkWell(
            onTap: () => Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => Thread(post: post),
                  ),
                ),
            child: RedditPostCard(
              post: post,
              onLike: (direction) => state.vote(index, direction),
              onSave: (save) => state.save(index, save),
            ))
        : Text("${state.done}, ${state.length}");
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    final state = context.watch<FeedStateProvider>();
    final appState = context.watch<AccountsManager>();

    if (appState.currentUser == null) {
      appState.init();
      return Center(child: CircularProgressIndicator());
    }

    if (appState.currentUser != account) {
      _triggerReset(state);
      account = appState.currentUser;
    }

    if (account == null) {
      return Center(child: CircularProgressIndicator());
    }
    return NestedScrollView(
      headerSliverBuilder: (context, _) => [FeedTopBar()],
      floatHeaderSlivers: true,
      body: RefreshIndicator(
        onRefresh: () async {
          state.reset();
        },
        child: ListView.builder(
          itemCount: state.length + 1,
          itemBuilder: itemView,
        ),
      ),
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
        // TODO: timed sort
      ],
      onSelected: (sort) => state.sort = sort,
    );
  }
}
