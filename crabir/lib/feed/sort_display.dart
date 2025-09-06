import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';

extension SortLabel on FeedSort {
  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return switch (this) {
      FeedSort_Best() => locales.sortBest,
      FeedSort_Hot() => locales.sortHot,
      FeedSort_New() => locales.sortNew,
      FeedSort_Top() => locales.sortTop,
      FeedSort_Rising() => locales.sortRising,
      FeedSort_Controversial() => locales.sortControversial,
    };
  }

  String labelWithTimeframe(BuildContext context) {
    final sort = label(context);
    final timeframe = _getTimeframe();
    if (timeframe != null) {
      final time = timeframe.label(context);
      return "$sort · $time";
    } else {
      return sort;
    }
  }

  Timeframe? _getTimeframe() {
    return switch (this) {
      FeedSort_Top(field0: final timeframe) ||
      FeedSort_New(field0: final timeframe) ||
      FeedSort_Controversial(field0: final timeframe) =>
        timeframe,
      _ => null
    };
  }
}

class SortDisplay extends StatelessWidget {
  final FeedSort sort;
  const SortDisplay({super.key, required this.sort});

  @override
  Widget build(BuildContext context) {
    return Text(
      sort.labelWithTimeframe(context),
      style: Theme.of(context).textTheme.labelSmall,
    );
  }
}
