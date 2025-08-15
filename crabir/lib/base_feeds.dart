import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

List<BaseFeed> baseFeeds(BuildContext context, {bool closeDrawer = true}) {
  final account = context.read<AccountsBloc>().state;
  return [
    BaseFeed(
      "Home",
      Icons.home,
      closeDrawer: closeDrawer,
      route: FeedRoute(feed: Feed.home()),
    ),
    BaseFeed(
      "Popular",
      Icons.arrow_outward,
      closeDrawer: closeDrawer,
      route: FeedRoute(feed: Feed.popular()),
    ),
    BaseFeed(
      "All",
      Icons.all_inclusive_rounded,
      closeDrawer: closeDrawer,
      route: FeedRoute(feed: Feed.all()),
    ),
    if (account.account != null) ...[
      BaseFeed(
        "Saved",
        Icons.bookmark,
        closeDrawer: closeDrawer,
        route: UserSavedRoute(
          username: account.account!.username,
        ),
      ),
      BaseFeed(
        "History",
        Icons.history,
        closeDrawer: closeDrawer,
        // TODO:
        route: FeedRoute(feed: Feed.popular()),
      ),
    ],
  ];
}

class BaseFeed extends StatelessWidget {
  final String title;
  final IconData icon;
  final int tabIndex;
  final bool closeDrawer;
  final PageRouteInfo route;
  const BaseFeed(
    this.title,
    this.icon, {
    super.key,
    this.tabIndex = 2,
    this.closeDrawer = true,
    required this.route,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(icon),
      title: Text(title),
      onTap: () {
        final tabsRouter = AutoTabsRouter.of(context);
        tabsRouter.setActiveIndex(tabIndex);
        context.router.navigate(route);
      },
    );
  }
}
