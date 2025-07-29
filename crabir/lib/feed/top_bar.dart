import 'package:auto_route/auto_route.dart';
import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';

class FeedTopBar extends StatelessWidget {
  // Callback when changing sort.
  final void Function(FeedSort) setSort;
  // Current sort.
  final FeedSort sort;
  // Current feed.
  final Feed feed;
  const FeedTopBar({
    super.key,
    required this.feed,
    required this.sort,
    required this.setSort,
  });
  @override
  Widget build(BuildContext context) {
    return SliverAppBar(
      floating: true,
      title: feedTitle(),
      actions: [
        IconButton(
          icon: Icon(Icons.search),
          onPressed: () {
            context.pushRoute(SearchRoute());
          },
        ),
        SortMenu(
          onSelect: setSort,
        ),
      ],
    );
  }

  Widget feedTitle() {
    final title = switch (feed) {
      Feed_Home() => "Home",
      Feed_All() => "All",
      Feed_Subreddit(:final field0) => field0,
      Feed_Popular() => "Popular",
    };
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(title),
        SortDisplay(sort: sort),
      ],
    );
  }
}
