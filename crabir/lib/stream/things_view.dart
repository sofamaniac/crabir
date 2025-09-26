import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/paging_handler.dart'
    as reddit_stream;
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:infinite_scroll_pagination/infinite_scroll_pagination.dart';

class ThingsScaffold extends StatefulWidget {
  final reddit_stream.PagingHandler stream;

  /// If provided show the widget at the top of the view.
  final Widget? subredditInfo;

  /// Function to use do build a `Comment` view.
  final Widget Function(BuildContext, Comment)? commentView;

  /// Function to use do build a `Post` view.
  final Widget Function(BuildContext, Post)? postView;

  const ThingsScaffold({
    super.key,
    this.postView,
    this.commentView,
    required this.stream,
    this.subredditInfo,
  });

  @override
  State<ThingsScaffold> createState() => _ThingsScaffoldState();
}

class _ThingsScaffoldState extends State<ThingsScaffold> {
  late final _pagingController = PagingController<bool, Thing>(
    getNextPageKey: (state) => state.lastPageIsEmpty ? null : state.hasNextPage,
    fetchPage: (pageKey) async {
      if (await widget.stream.next()) {
        return widget.stream.last();
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
            await widget.stream.refresh();
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
                          if (context
                              .read<GlobalFiltersCubit>()
                              .shouldHidePost(post)) {
                            return const SizedBox.shrink();
                          }
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
