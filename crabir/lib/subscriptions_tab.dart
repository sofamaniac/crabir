import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/base_feeds.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

@RoutePage(name: "SubscriptionsTabRoute")
class SubscriptionsTab extends StatefulWidget {
  const SubscriptionsTab({super.key});
  @override
  State<StatefulWidget> createState() => _SubscriptionsTabState();
}

class _SubscriptionsTabState extends State<SubscriptionsTab> {
  String filter = "";
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: SearchBar(
          hintText: "Search community",
          onChanged: (input) => setState(() {
            filter = input;
          }),
        ),
      ),
      body: _SubscriptonsList(filter: filter),
    );
  }
}

class _SubscriptonsList extends StatelessWidget {
  final String filter;
  const _SubscriptonsList({this.filter = ""});

  @override
  Widget build(BuildContext context) {
    final bloc = context.read<AccountsBloc>();
    final account = bloc.state;
    if (account.status case Uninit()) {
      bloc.add(Initialize());
    } else if (account.status case Unloaded()) {
      bloc.add(LoadSubscriptions());
    } else if (account.status case Loaded()) {
      return ListView(
        children: [
          ...baseFeeds(context, closeDrawer: false).where(
            (feed) => feed.title.toLowerCase().contains(filter.toLowerCase()),
          ),
          ...account.multis
              .where(
                (multi) => multi.displayName
                    .toLowerCase()
                    .contains(filter.toLowerCase()),
              )
              .map((multi) => MultiRedditTile(multi)),
          ...account.subscriptions
              .where(
                (sub) => sub.other.displayName
                    .toLowerCase()
                    .contains(filter.toLowerCase()),
              )
              .map(
                (sub) => SubredditTile(sub),
              ),
        ],
      );
    }
    return LoadingIndicator();
  }
}
