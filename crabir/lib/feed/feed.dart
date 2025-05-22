import 'package:crabir/drawer.dart';
import 'package:crabir/feed/post.dart';
import 'package:crabir/login.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:provider/provider.dart';

class FeedView extends StatefulWidget {
  const FeedView({super.key, required this.feed, required this.initialSort});
  final Feed feed;
  final Sort initialSort;

  @override
  State<StatefulWidget> createState() => _FeedViewState();
}

class _FeedViewState extends State<FeedView>
    with AutomaticKeepAliveClientMixin {
  final List<Post> _posts = [];
  bool _isLoading = false;
  bool _done = false;
  late FeedWrapper _feedwrapper;
  late Sort _sort;
  UserAccount? _associatedAccount;

  _FeedViewState();

  // Ensure that the tab state is preserved
  @override
  bool get wantKeepAlive => true;

  set sort(Sort sort) {
    setState(() {
      _sort = sort;
      _feedwrapper = FeedWrapper(feed: widget.feed, sort: sort);
      _posts.clear();
    });
  }

  Sort get sort {
    return _sort;
  }

  @override
  void initState() {
    super.initState();
    _sort = widget.initialSort;
    _feedwrapper = FeedWrapper(feed: widget.feed, sort: sort);
    // Preload first item
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _loadNext();
    });
  }

  Future<void> _loadNext() async {
    if (_isLoading || _done) return;
    setState(() => _isLoading = true);

    try {
      final post = await _feedwrapper.next();
      if (post != null) {
        setState(() => _posts.add(post));
      } else {
        setState(() => _done = true);
      }
    } catch (e) {
      print("Error loading post: $e");
      setState(() => _done = true);
    } finally {
      setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final actualAccount = context
        .select<DrawerModel, UserAccount?>((drawer) => drawer.currentAccount);
    if (actualAccount != _associatedAccount) {
      setState(() {
        _posts.clear();
        _associatedAccount = actualAccount;
        _feedwrapper = FeedWrapper(feed: widget.feed, sort: sort);
      });
    }
    super.build(context);
    return CustomScrollView(
      slivers: [
        SliverAppBar(title: Text("${widget.feed}")),
        SliverList.builder(
          itemCount: _posts.length + 1, // +1 for loader
          itemBuilder: (context, index) {
            if (index == _posts.length) {
              // load more when reaching end
              WidgetsBinding.instance.addPostFrameCallback((_) {
                _loadNext();
              });
              return const Center(child: CircularProgressIndicator());
            }
            final post = _posts[index];
            return RedditPostCard(post: post);
          },
        ),
      ],
    );
  }
}
