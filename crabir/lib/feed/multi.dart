import 'package:auto_route/auto_route.dart';
import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/post/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/stream/things_view.dart';
import 'package:flutter/material.dart';

@RoutePage()
class MultiView extends StatelessWidget {
  const MultiView({super.key, required this.multi, required this.initialSort});
  final Multi multi;
  final FeedSort initialSort;

  @override
  Widget build(BuildContext context) {
    return MultiViewBody(
      multi: multi,
      initialSort: initialSort,
    );
  }
}

class MultiViewBody extends StatefulWidget {
  final Multi multi;
  final FeedSort initialSort;

  const MultiViewBody(
      {super.key, required this.multi, required this.initialSort});

  @override
  State<MultiViewBody> createState() => _MultiViewBodyState();
}

class _MultiViewBodyState extends State<MultiViewBody>
    with AutomaticKeepAliveClientMixin {
  FeedSort sort = FeedSort.best();

  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    super.build(context);
    final stream = RedditAPI.client().multiPosts(
      multi: widget.multi,
      sort: sort,
    );
    return NestedScrollView(
      headerSliverBuilder: (context, __) {
        return [
          SliverAppBar(
            floating: true,
            title: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(widget.multi.displayName),
                SortDisplay(sort: sort),
              ],
            ),
            actions: [
              IconButton(onPressed: () {}, icon: Icon(Icons.search)),
              SortMenu(
                onSelect: (sort) => setState(
                  () {
                    this.sort = sort;
                  },
                ),
              ),
            ],
          ),
        ];
      },
      floatHeaderSlivers: true,
      body: ThingsScaffold(
        // Forces rebuild when sort changes
        key: ValueKey(sort.toString()),
        stream: stream,
        postView: (context, post) {
          return RedditPostCard(
            maxLines: 5,
            post: post,
            onLikeCallback: (direction) async {
              await stream.vote(
                name: post.name,
                direction: direction,
                client: RedditAPI.client(),
              );
            },
            onSaveCallback: (save) async {
              await stream.save(
                name: post.name,
                save: save,
                client: RedditAPI.client(),
              );
            },
            onTap: () => context.pushRoute(
              ThreadRoute(
                post: post,
              ),
            ),
          );
        },
      ),
    );
  }
}
