import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
import 'package:flutter/material.dart';
import 'package:infinite_scroll_pagination/infinite_scroll_pagination.dart';

class ThingsScaffold extends StatefulWidget {
  /// Function to use do build a `Post` view.
  final Widget Function(BuildContext, Post)? postView;

  final Widget? subredditInfo;
  final String? name;
  final reddit_stream.Streamable stream;

  /// Function to use do build a `Comment` view.
  final Widget Function(BuildContext, Comment)? commentView;
  const ThingsScaffold({
    super.key,
    this.postView,
    this.commentView,
    required this.stream,
    this.subredditInfo,
    this.name,
  });

  @override
  State<ThingsScaffold> createState() => _ThingsScaffoldState();
}

class _ThingsScaffoldState extends State<ThingsScaffold> {
  late final _pagingController = PagingController<bool, Thing>(
    getNextPageKey: (state) => state.lastPageIsEmpty ? null : state.hasNextPage,
    fetchPage: (pageKey) async {
      if (await widget.stream.next()) {
        final length = widget.stream.length;
        return widget.stream.nth(n: length - 1)!;
      } else {
        return [];
      }
    },
  );

  @override
  void dispose() {
    _pagingController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return PagingListener(
      controller: _pagingController,
      builder: (context, state, fetchNextPage) {
        return RefreshIndicator(
          onRefresh: () async {
            _pagingController.refresh();
          },
          child: Scrollbar(
            child: CustomScrollView(
              slivers: [
                // Optional subreddit info at the top
                if (widget.subredditInfo != null)
                  SliverToBoxAdapter(child: widget.subredditInfo!),

                // The main paginated list
                PagedSliverList<bool, Thing>(
                  fetchNextPage: fetchNextPage,
                  state: state,
                  builderDelegate: PagedChildBuilderDelegate(
                    noMoreItemsIndicatorBuilder: (context) =>
                        Center(child: Text("You've reached the end")),
                    itemBuilder: (context, item, index) {
                      final thing = item;

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
                    },
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
