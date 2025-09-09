import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

List<BaseFeed> baseFeeds(BuildContext context, {bool closeDrawer = true}) {
  final account = context.read<AccountsBloc>().state;
  final locales = AppLocalizations.of(context);
  return [
    BaseFeed(
      locales.feedHome,
      Icons.home,
      closeDrawer: closeDrawer,
      route: NamedRoute(homeRouteName),
    ),
    BaseFeed(
      locales.feedPopular,
      Icons.arrow_outward,
      closeDrawer: closeDrawer,
      route: FeedRoute(feed: Feed.popular()),
    ),
    BaseFeed(
      locales.feedAll,
      Icons.all_inclusive_rounded,
      closeDrawer: closeDrawer,
      route: FeedRoute(feed: Feed.all()),
    ),
    if (account.account != null) ...[
      BaseFeed(
        locales.feedSaved,
        Icons.bookmark,
        closeDrawer: closeDrawer,
        route: UserSavedRoute(
          username: account.account!.username,
        ),
      ),
      BaseFeed(
        locales.feedHistory,
        Icons.history,
        closeDrawer: closeDrawer,
        // TODO: history feed
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
        context.tabsRouter.navigate(route);
      },
    );
  }
}
