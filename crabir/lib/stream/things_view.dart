import 'package:crabir/loading_indicator.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
import 'package:flutter/material.dart';
import 'package:infinite_scroll_pagination/infinite_scroll_pagination.dart';

/// Display things from a `StreamWrapper` in context.
// class ThingsScaffold extends StatefulWidget {
//   /// Function to use do build a `Post` view.
//   final Widget Function(BuildContext, Post)? postView;
//
//   final Widget? subredditInfo;
//   final String? name;
//   final reddit_stream.Streamable stream;
//
//   /// Function to use do build a `Comment` view.
//   final Widget Function(BuildContext, Comment)? commentView;
//
//   const ThingsScaffold({
//     super.key,
//     this.postView,
//     this.commentView,
//     required this.stream,
//     this.subredditInfo,
//     this.name,
//   });
//
//   @override
//   State<ThingsScaffold> createState() => _ThingsScaffoldState();
// }
//
// class _ThingsScaffoldState extends State<ThingsScaffold> {
//   bool _loadingMore = false;
//
//   List<Thing> items = [];
//
//   Future<void> _loadMore() async {
//     if (_loadingMore) return;
//     _loadingMore = true;
//     if (mounted) setState(() {});
//     await widget.stream.next(); // load more items
//     items = widget.stream.getAll();
//     if (mounted) setState(() {});
//     _loadingMore = false;
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     final itemCount = items.length;
//     final totalSliverItems = itemCount + 1;
//     return CustomScrollView(
//       key: widget.key,
//       slivers: [
//         // Optional subreddit info at the top
//         if (widget.subredditInfo != null)
//           SliverToBoxAdapter(child: widget.subredditInfo!),
//
//         // SliverList with separators
//         SliverList(
//           delegate: SliverChildBuilderDelegate(
//             (context, index) {
//               if (index < itemCount) {
//                 final thing = items[index];
//
//                 switch (thing) {
//                   case Thing_Post(field0: final post):
//                     return widget.postView?.call(context, post) ??
//                         const SizedBox.shrink();
//                   case Thing_Comment(field0: final comment):
//                     return widget.commentView?.call(context, comment) ??
//                         const SizedBox.shrink();
//                   default:
//                     return const SizedBox.shrink();
//                 }
//               } else {
//                 // Load more indicator at the end
//                 WidgetsBinding.instance.addPostFrameCallback(
//                   (_) => _loadMore(),
//                 );
//                 return _loadingMore
//                     ? const Padding(
//                         padding: EdgeInsets.all(16),
//                         child: Center(child: LoadingIndicator()),
//                       )
//                     : const SizedBox.shrink();
//               }
//             },
//             childCount: totalSliverItems,
//           ),
//         ),
//         // Ensure space at the bottom to avoid obstruction of the last element by the bottom bar.
//         SliverToBoxAdapter(
//           child: SizedBox(height: kBottomNavigationBarHeight),
//         ),
//       ],
//     );
//   }
// }

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

                // Bottom padding to avoid obstruction by bottom nav
                SliverToBoxAdapter(
                  child: SizedBox(height: kBottomNavigationBarHeight),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
