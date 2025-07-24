import 'package:auto_route/auto_route.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    as subreddit;
import 'package:crabir/subreddit.dart';
import 'package:flutter/material.dart';

void navigateToSubscriptionsTab(context, destination) {
  final tabsRouter = AutoTabsRouter.of(context);
  // Tabs are lazily loaded, so if it was never visited the tab router does not exists
  tabsRouter.setActiveIndex(2);
  final subscriptionsTabRouter = tabsRouter.stackRouterOfIndex(2);
  subscriptionsTabRouter?.replaceAll([destination]);
}

class SubredditTile extends StatelessWidget {
  final subreddit.Subreddit sub;
  const SubredditTile({super.key, required this.sub});
  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: () {
        final destination = FeedRoute(
          feed: Feed.subreddit(sub.other.displayName),
          initialSort: FeedSort.best(),
        );
        navigateToSubscriptionsTab(context, destination);
      },
      leading: SubredditIcon(icon: sub.icon, radius: 16),
      title: Text(
        sub.other.displayNamePrefixed,
        style: Theme.of(context).textTheme.bodyMedium,
        maxLines: 1,
      ),
    );
  }
}

class MultiRedditTile extends StatelessWidget {
  final Multi multi;
  const MultiRedditTile({super.key, required this.multi});
  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: () {
        final destination = MultiRoute(
          multi: multi,
          initialSort: FeedSort.best(),
        );
        navigateToSubscriptionsTab(context, destination);
      },
      leading: CircleAvatar(
        radius: 16,
        foregroundImage: NetworkImage(multi.iconUrl),
      ),
      title: Text(
        multi.displayName,
        style: Theme.of(context).textTheme.bodyMedium,
        maxLines: 1,
      ),
    );
  }
}
