import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';

class FeedTitle extends StatelessWidget {
  final Feed feed;
  final FeedSort sort;
  const FeedTitle({super.key, required this.feed, required this.sort});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final theme = CrabirTheme.of(context);
    final title = switch (feed) {
      Feed_Home() => locales.feedHome,
      Feed_All() => locales.feedAll,
      Feed_Subreddit(:final field0) => field0,
      Feed_Popular() => locales.feedPopular,
    };
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          title,
          style: Theme.of(context)
              .textTheme
              .titleMedium
              ?.copyWith(color: theme.toolBarText),
        ),
        SortDisplay(sort: sort),
      ],
    );
  }
}
