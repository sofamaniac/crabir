import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

final baseFeeds = const [
  HomeFeed(),
  PopularFeed(),
  AllFeed(),
  SavedFeed(),
  HistoryFeed()
];

sealed class BaseFeed {
  final String title;
  final IconData icon;
  PageRouteInfo get route;
  const BaseFeed(this.title, this.icon);

  Widget toTile(BuildContext context) {
    return ListTile(
      leading: Icon(icon),
      title: Text(title),
      onTap: () => context.pushRoute(route),
    );
  }
}

class HomeFeed extends BaseFeed {
  const HomeFeed() : super("Home", Icons.home);
  @override
  PageRouteInfo get route => FeedRoute(feed: Feed.home());
}

class AllFeed extends BaseFeed {
  const AllFeed() : super("All", Icons.all_inclusive_rounded);
  @override
  PageRouteInfo get route => FeedRoute(feed: Feed.all());
}

class PopularFeed extends BaseFeed {
  const PopularFeed() : super("Popular", Icons.arrow_outward);
  @override
  PageRouteInfo get route => FeedRoute(feed: Feed.popular());
}

class SavedFeed extends BaseFeed {
  const SavedFeed() : super("Saved", Icons.bookmark_rounded);
  @override
  PageRouteInfo get route => UserSavedRoute(username: "me");

  @override
  Widget toTile(BuildContext context) {
    return BlocBuilder<AccountsBloc, AccountState>(
      builder: (context, state) => ListTile(
        leading: Icon(icon),
        title: Text(title),
        onTap: () => context.pushRoute(UserSavedRoute(
          username: state.account!.username,
        )),
      ),
    );
  }
}

class HistoryFeed extends BaseFeed {
  const HistoryFeed() : super("History", Icons.history_outlined);
  @override
  PageRouteInfo get route => FeedRoute(feed: Feed.popular());
}
