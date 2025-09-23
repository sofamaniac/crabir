import 'package:auto_route/auto_route.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    as subreddit;
import 'package:crabir/subreddit.dart';
import 'package:flutter/material.dart';

void navigateToSubscriptionsTab(BuildContext context, destination) {
  context.tabsRouter.navigate(destination);
}

/// Widget to display a subreddit in a list
class SubredditTile extends StatelessWidget {
  final subreddit.Subreddit sub;
  final TextStyle? style;

  const SubredditTile(
    this.sub, {
    super.key,
    this.style,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: () {
        final destination = FeedRoute(
          feed: Feed.subreddit(sub.other.displayName),
          initialSort: FeedSort.best(),
        );
        Scaffold.of(context).closeDrawer();
        navigateToSubscriptionsTab(context, destination);
      },
      leading: SubredditIcon(icon: sub.icon, radius: 12),
      title: Text(
        sub.other.displayNamePrefixed,
        // style:
        //     Theme.of(context).textTheme.bodyMedium!.copyWith(color: textColor),
        style: style,
        maxLines: 1,
      ),
    );
  }
}

/// Widget to display a multireddit in a list
class MultiRedditTile extends StatelessWidget {
  final Multi multi;
  final TextStyle? style;

  const MultiRedditTile(
    this.multi, {
    super.key,
    this.style,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: () {
        final destination = MultiRoute(
          multi: multi,
        );
        Navigator.pop(context);
        navigateToSubscriptionsTab(context, destination);
      },
      leading: CircleAvatar(
        radius: 12,
        foregroundImage: NetworkImage(multi.iconUrl),
      ),
      title: Text(
        multi.displayName,
        style: style,
        maxLines: 1,
      ),
    );
  }
}
