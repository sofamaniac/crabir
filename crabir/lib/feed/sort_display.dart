import 'package:crabir/feed/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';

class SortDisplay extends StatelessWidget {
  final FeedSort sort;
  const SortDisplay({super.key, required this.sort});

  String sortString() {
    return switch (sort) {
      FeedSort_Best() => "Best",
      FeedSort_Hot() => "Hot",
      FeedSort_New() => "New",
      FeedSort_Top() => "Top",
      FeedSort_Rising() => "Rising",
      FeedSort_Controversial() => "Controversial",
    };
  }

  String? timeframeString() {
    return switch (sort) {
      FeedSort_Top(field0: final timeframe) ||
      FeedSort_Controversial(field0: final timeframe) ||
      FeedSort_New(field0: final timeframe) =>
        timeframeToString(timeframe),
      _ => null
    };
  }

  @override
  Widget build(BuildContext context) {
    final timeframe = timeframeString();
    if (timeframe != null) {
      return Text(
        "${sortString()} $timeframe",
        style: Theme.of(context).textTheme.labelSmall,
      );
    } else {
      return Text(
        sortString(),
        style: Theme.of(context).textTheme.labelSmall,
      );
    }
  }
}
