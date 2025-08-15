import 'package:auto_route/auto_route.dart';
import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/flair.dart';
import 'package:flutter/material.dart';

class FeedTitle extends StatelessWidget {
  final Feed feed;
  final FeedSort sort;
  const FeedTitle({super.key, required this.feed, required this.sort});

  @override
  Widget build(BuildContext context) {
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
