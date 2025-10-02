import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/lateral_menu/lateral_menu_settings.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/utils/icons.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

List<BaseFeed> baseFeeds(BuildContext context) {
  final account = context.watch<AccountsBloc>().state;
  final locales = AppLocalizations.of(context);
  final settings = LateralMenuSettings.of(context);
  return [
    if (settings.showHome)
      BaseFeed(
        locales.feedHome,
        HOME_PAGE_ICON,
        route: NamedRoute(homeRouteName),
      ),
    if (settings.showHomeFeed)
      BaseFeed(
        locales.lateralMenu_showHomeFeed,
        HOME_FEED_ICON,
        route: NamedRoute(homeRouteName),
      ),
    if (settings.showPopular)
      BaseFeed(
        locales.feedPopular,
        POPULAR_ICON,
        route: FeedRoute(feed: Feed.popular()),
      ),
    if (settings.showAll)
      BaseFeed(
        locales.feedAll,
        ALL_ICON,
        route: FeedRoute(feed: Feed.all()),
      ),
    if (account.account != null) ...[
      if (settings.showSaved)
        BaseFeed(
          locales.feedSaved,
          SAVED_FEED_ICON,
          route: UserSavedRoute(
            username: account.account!.username,
          ),
        ),
      if (settings.showHistory)
        BaseFeed(
          locales.feedHistory,
          HISTORY_ICON,
          // TODO: history feed
        ),
    ],
    if (settings.showSearch)
      BaseFeed(
        locales.lateralMenu_showSearch,
        SEARCH_ICON,
        route: SearchPostsRoute(),
      ),
    if (account.account != null) ...[
      if (settings.showProfile)
        BaseFeed(
          locales.lateralMenu_showProfile,
          PROFILE_ICON,
        ),
      if (settings.showInbox)
        BaseFeed(
          locales.lateralMenu_showInbox,
          INBOX_ICON,
        ),
      if (settings.showFriends)
        BaseFeed(
          locales.lateralMenu_showFriends,
          FRIENDS_ICON,
        ),
      if (settings.showDrafts)
        BaseFeed(
          locales.lateralMenu_showDrafts,
          DRAFTS_ICON,
        ),
    ],
  ];
}

class BaseFeed extends StatelessWidget {
  final String title;
  final IconData icon;
  final int tabIndex;
  final bool closeDrawer;
  final PageRouteInfo? route;
  const BaseFeed(
    this.title,
    this.icon, {
    super.key,
    this.tabIndex = 2,
    this.closeDrawer = true,
    this.route,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(icon),
      title: Text(title),
      onTap: () {
        if (route != null) {
          Scaffold.of(context).closeDrawer();
          context.tabsRouter.navigate(route!);
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: const Text("Not Implemented"),
            ),
          );
        }
      },
    );
  }
}
