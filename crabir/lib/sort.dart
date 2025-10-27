import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';
import 'package:crabir/l10n/app_localizations.dart';

const timeOptions = [
  Timeframe.hour,
  Timeframe.day,
  Timeframe.week,
  Timeframe.month,
  Timeframe.year,
  Timeframe.all
];

extension TimeframeLabel on Timeframe {
  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return switch (this) {
      Timeframe.hour => locales.timeframeHour,
      Timeframe.day => locales.timeframeDay,
      Timeframe.week => locales.timeframeWeek,
      Timeframe.month => locales.timeframeMonth,
      Timeframe.year => locales.timeframeYear,
      Timeframe.all => locales.timeframeAll,
    };
  }
}

List<Widget> menu<T>(
  T Function(Timeframe) sort,
  Function(T) onSelect,
  BuildContext context,
) {
  return timeOptions
      .map(
        (timeframe) => MenuItemButton(
          onPressed: () => onSelect(sort(timeframe)),
          child: Text(timeframe.label(context)),
        ),
      )
      .toList();
}

extension FeedSortLabel on FeedSort {
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
      FeedSort_Controversial(field0: final timeframe) =>
        timeframe,
      _ => null
    };
  }

  IconData icon() {
    return switch (this) {
      FeedSort_Best() => Icons.rocket,
      FeedSort_Hot() => Icons.local_fire_department,
      FeedSort_New() => Icons.fiber_new,
      FeedSort_Top() => Icons.bar_chart,
      FeedSort_Rising() => Icons.show_chart,
      FeedSort_Controversial() => Icons.chat,
    };
  }
}

extension Label on CommentSort {
  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return switch (this) {
      CommentSort.confidence => locales.sortBest,
      CommentSort.top => locales.sortTop,
      CommentSort.new_ => locales.sortNew,
      CommentSort.controversial => locales.sortControversial,
      CommentSort.old => locales.sortOld,
      CommentSort.qa => locales.sortQA,
      CommentSort.random => locales.sortRandom,
      CommentSort.live => locales.sortLive
    };
  }

  IconData icon() {
    return switch (this) {
      CommentSort.confidence => Icons.rocket,
      CommentSort.top => Icons.bar_chart,
      CommentSort.new_ => Icons.fiber_new,
      CommentSort.controversial => Icons.chat,
      CommentSort.old => Icons.history,
      CommentSort.qa => Icons.forum,
      CommentSort.random => Icons.shuffle,
      CommentSort.live => Icons.whatshot,
    };
  }
}
