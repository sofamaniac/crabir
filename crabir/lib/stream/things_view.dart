import 'package:crabir/loading_indicator.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
import 'package:flutter/material.dart';

/// Display things from a `StreamWrapper` in context.
class ThingsScaffold extends StatefulWidget {
  /// Function to use do build a `Post` view.
  final Widget Function(BuildContext, Post)? postView;

  final Widget? subredditInfo;

  /// Function to use do build a `Comment` view.
  final Widget Function(BuildContext, Comment)? commentView;
  const ThingsScaffold({
    super.key,
    this.postView,
    this.commentView,
    required this.stream,
    this.subredditInfo,
  });

  final reddit_stream.Streamable stream;

  @override
  State<ThingsScaffold> createState() => _ThingsScaffoldState();
}

class _ThingsScaffoldState extends State<ThingsScaffold> {
  bool _loadingMore = false;

  List<Thing> items = [];

  Future<void> _loadMore() async {
    if (_loadingMore) return;
    _loadingMore = true;
    await widget.stream.next(); // load more items
    items = widget.stream.getAll();
    if (mounted) setState(() {});
    _loadingMore = false;
  }

  @override
  Widget build(BuildContext context) {
    final itemCount = items.length;
    final totalSliverItems =
        (itemCount + 1) * 2 - 1; // double count for separators
    return CustomScrollView(
      slivers: [
        // Optional subreddit info at the top
        if (widget.subredditInfo != null)
          SliverToBoxAdapter(child: widget.subredditInfo!),

        // SliverList with separators
        SliverList(
          delegate: SliverChildBuilderDelegate(
            (context, index) {
              if (index.isOdd) {
                // Divider between items
                return const Divider(height: 1);
              }

              final dataIndex = index ~/ 2;
              if (dataIndex < itemCount) {
                final thing = items[dataIndex];

                switch (thing) {
                  case Thing_Post(field0: final post):
                    return widget.postView?.call(context, post) ??
                        const SizedBox.shrink();
                  case Thing_Comment(field0: final comment):
                    return widget.commentView?.call(context, comment) ??
                        const SizedBox.shrink();
                  default:
                    return const SizedBox.shrink();
                }
              } else {
                // Load more indicator at the end
                WidgetsBinding.instance.addPostFrameCallback(
                  (_) => _loadMore(),
                );
                return Center(child: LoadingIndicator());
                // return _loadingMore
                //     ? const Padding(
                //         padding: EdgeInsets.all(16),
                //         child: Center(child: CircularProgressIndicator()),
                //       )
                //     : const SizedBox.shrink();
                // return FutureBuilder(
                //   future: _loadMore(),
                //   builder: (context, snapshot) {
                //     if (snapshot.connectionState == ConnectionState.waiting) {
                //       return const Center(child: CircularProgressIndicator());
                //     } else if (snapshot.hasError) {
                //       return Center(child: Text("Error: ${snapshot.error}"));
                //     } else {
                //       return const Center(child: Text("Nothing more to show"));
                //     }
                //   },
                // );
              }
            },
            childCount: totalSliverItems,
          ),
        ),
      ],
    );
  }
}
