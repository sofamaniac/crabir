import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/feed/sort_menu.dart';
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
      title: Column(
        children: [
          // TODO styling
          Text("TODO"),
          SortDisplay(sort: sort),
        ],
      ),
      actions: [
        IconButton(onPressed: () => (), icon: Icon(Icons.search)),
        SortMenu(
          onSelect: (sort) => (),
        ),
      ],
      floating: true,
    );
  }
}
