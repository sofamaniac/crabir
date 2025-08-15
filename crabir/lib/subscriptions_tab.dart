import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/base_feeds.dart';
import 'package:crabir/feed_list.dart';
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
    final state = context.read<AccountsBloc>();
    return BlocBuilder<AccountsBloc, AccountState>(
      builder: (context, account) {
        if (account.status case Uninit()) {
          state.add(Initialize());
        } else if (account.status case Unloaded()) {
          state.add(LoadSubscriptions());
        } else if (account.status case Loaded()) {
          return Flexible(
            fit: FlexFit.loose,
            child: ListView(
              children: [
                ...baseFeeds(context, closeDrawer: false).where(
                  (feed) =>
                      feed.title.toLowerCase().contains(filter.toLowerCase()),
                ),
                ...account.multis
                    .where(
                      (multi) => multi.displayName
                          .toLowerCase()
                          .contains(filter.toLowerCase()),
                    )
                    .map((multi) => MultiRedditTile(multi: multi)),
                ...account.subscriptions
                    .where(
                      (sub) => sub.other.displayName
                          .toLowerCase()
                          .contains(filter.toLowerCase()),
                    )
                    .map(
                      (sub) => SubredditTile(sub: sub),
                    ),
              ],
            ),
          );
        }
        return CircularProgressIndicator();
      },
    );
  }
}
