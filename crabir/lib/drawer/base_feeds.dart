import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/search_subreddits/widgets/search.dart';
import 'package:crabir/settings/lateral_menu/lateral_menu_settings.dart';
import 'package:crabir/utils/icons.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

List<Widget> baseFeeds(BuildContext context) {
  final account = context.watch<AccountsBloc>().state;
  final locales = AppLocalizations.of(context);
  final settings = LateralMenuSettings.of(context);
  return [
    if (settings.showHome)
      BaseFeed(
        locales.feedHome,
        HOME_PAGE_ICON,
        destination: "/default",
      ),
    if (settings.showHomeFeed)
      BaseFeed(
        locales.lateralMenu_showHomeFeed,
        HOME_FEED_ICON,
        destination: "/home",
      ),
    if (settings.showPopular)
      BaseFeed(
        locales.feedPopular,
        POPULAR_ICON,
        destination: "/r/popular",
      ),
    if (settings.showAll)
      BaseFeed(
        locales.feedAll,
        ALL_ICON,
        destination: "/r/all",
      ),
    if (account.account != null)
      if (settings.showSaved)
        ListTile(
          leading: Icon(SAVED_FEED_ICON),
          title: Text(locales.feedSaved),
          onTap: () {
            final account = context.read<AccountsBloc>().state.account;
            Scaffold.of(context).closeDrawer();
            if (account != null && !account.isAnonymous) {
              context.go("/u/${account.username}/saved");
            } else {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text("Please login to continue"),
                ),
              );
            }
          },
        ),
    if (settings.showHistory)
      BaseFeed(
        locales.feedHistory,
        HISTORY_ICON,
        // TODO: history feed
      ),
    ListTile(
      leading: Icon(SEARCH_ICON),
      title: Text(locales.lateralMenu_showSearch),
      onTap: () {
        Scaffold.of(context).closeDrawer();
        SearchSubredditsView().goNamed(context);
      },
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
  final String? destination;
  const BaseFeed(
    this.title,
    this.icon, {
    super.key,
    this.tabIndex = 2,
    this.closeDrawer = true,
    this.destination,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(icon),
      title: Text(title),
      onTap: () {
        Scaffold.of(context).closeDrawer();
        if (destination != null) {
          context.go(destination!);
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
