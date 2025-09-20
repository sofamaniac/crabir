import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';

class SortDisplay extends StatelessWidget {
  final FeedSort sort;
  const SortDisplay({super.key, required this.sort});

  @override
  Widget build(BuildContext context) {
    final theme = CrabirTheme.of(context);
    return Text(
      sort.labelWithTimeframe(context),
      style: Theme.of(context)
          .textTheme
          .labelSmall
          ?.copyWith(color: theme.toolBarText.withAlpha(128)),
    );
  }
}
