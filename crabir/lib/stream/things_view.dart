import 'package:crabir/loading_indicator.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/paging_handler.dart'
    as reddit_stream;
import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';

class ThingsScaffold extends StatefulWidget {
  final reddit_stream.PagingHandler stream;

  /// If provided show the widget at the top of the view.
  final Widget? subredditInfo;

  /// Function to use do build a `Comment` view.
  final Widget Function(BuildContext, Comment)? commentView;

  /// Function to use do build a `Post` view.
  final Widget Function(
    BuildContext context,
    Post post,
    bool respectHide,
  )? postView;

  /// When set to true show post that were hidden
  final bool showHidden;

  /// Number of column
  final int columnCount;

  const ThingsScaffold({
    super.key,
    this.postView,
    this.commentView,
    required this.stream,
    this.subredditInfo,
    this.showHidden = false,
    this.columnCount = 1,
  });

  @override
  State<ThingsScaffold> createState() => _ThingsScaffoldState();
}

class _ThingsScaffoldState extends State<ThingsScaffold>
    with AutomaticKeepAliveClientMixin {
  @override
  bool get wantKeepAlive => true;
  List<Thing> items = [];

  bool hasErrored = false;

  /// return true when the post should be hidden
  bool hide(Post post) {
    return GlobalFilters.of(context).shouldHidePost(post) ||
        (post.over18 && !FiltersSettings.of(context).showNSFW);
  }

  Future<void> _refresh() async {
    await widget.stream.refresh();
    items.clear();
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return RefreshIndicator(
      onRefresh: _refresh,
      child: AnimatedContentController(
        notifier: ValueNotifier([]),
        child: Scrollbar(
          child: CustomScrollView(
            slivers: [
              // Optional subreddit info at the top
              if (widget.subredditInfo != null)
                SliverToBoxAdapter(child: widget.subredditInfo!),

              SliverMasonryGrid.count(
                crossAxisCount: widget.columnCount,
                childCount: items.length + 1,
                itemBuilder: (context, index) {
                  if ((index == items.length - 10 || items.length < 10) &&
                      !widget.stream.done) {
                    WidgetsBinding.instance.addPostFrameCallback(
                      (_) async {
                        try {
                          items.addAll(await widget.stream.next());
                          setState(() {});
                        } catch (e) {
                          if (mounted) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(
                                content: Text("Something went wrong: $e"),
                              ),
                            );
                          }
                        }
                      },
                    );
                  }
                  if (index == items.length) {
                    if (widget.stream.done) {
                      return Center(child: Text("Nothing more to show"));
                    } else if (hasErrored) {
                      return Center(
                        child: ListTile(
                          title: Text("Something went wrong. Tap to refresh"),
                          onTap: _refresh,
                        ),
                      );
                    } else {
                      return Center(child: LoadingIndicator());
                    }
                  }
                  final thing = items[index];
                  switch (thing) {
                    case Thing_Post(field0: final post):
                      if (widget.postView == null) {
                        return const SizedBox.shrink();
                      }
                      return widget.postView!.call(
                        context,
                        post,
                        !widget.showHidden,
                      );
                    case Thing_Comment(field0: final comment):
                      return widget.commentView?.call(context, comment) ??
                          const SizedBox.shrink();
                    default:
                      return const SizedBox.shrink();
                  }
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
