import 'package:collection/collection.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/accounts_manager.dart';
import 'package:crabir/feed/feed.dart';
import 'package:crabir/login.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/subreddit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:logging/logging.dart';

class DrawerState extends State<AppDrawer> {
  bool _isSelectingAccount = false;

  bool get isSelectingAccount => _isSelectingAccount;

  Logger log = Logger("DrawerModel");

  DrawerState();

  void changeMode({bool? isSelectingAccount}) {
    setState(() {
      _isSelectingAccount = isSelectingAccount ?? !_isSelectingAccount;
    });
  }

  Widget currentAccountView(BuildContext context) {
    final icon =
        isSelectingAccount ? Icons.arrow_drop_up : Icons.arrow_drop_down;
    return BlocBuilder<AccountsBloc, AccountState>(
      builder: (context, account) => DrawerHeader(
        child: Row(
          children: [
            account.account?.build(context) ??
                const CircularProgressIndicator(),
            IconButton(onPressed: () => changeMode(), icon: Icon(icon))
          ],
        ),
      ),
    );
  }

  Widget accountSelector(BuildContext context) {
    final state = context.watch<AccountsBloc>();
    if (state.selectedAccount == null) {
      return const CircularProgressIndicator();
    }
    return Expanded(
      child: ListView(
        children: [
          ...state.accounts.mapIndexed((index, account) => InkWell(
              onTap: () async {
                state.add(SelectAccount(index: index));
              },
              child: account.build(context))),
          InkWell(
            onTap: () async {
              if (await loginToReddit()) {
                state.add(Initialize());
              }
            },
            child: Text("Add an account"),
          )
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Drawer(
        child: Padding(
          padding: EdgeInsetsDirectional.symmetric(horizontal: 8.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              currentAccountView(context),
              isSelectingAccount
                  ? accountSelector(context)
                  : DrawerFeedSelection()
            ],
          ),
        ),
      ),
    );
  }
}

class AppDrawer extends StatefulWidget {
  const AppDrawer({super.key});
  @override
  State<StatefulWidget> createState() => DrawerState();
}

class DrawerFeedSelection extends StatefulWidget {
  const DrawerFeedSelection({super.key});

  @override
  State<StatefulWidget> createState() => DrawerFeedSelectionState();
}

class DrawerFeedSelectionState extends State<DrawerFeedSelection> {
  final List<String> feeds = ["Home", "Popular", "All", "Saved", "History"];
  final List<String> userOptions = ["Profile", "Inbox", "Moderation"];
  final log = Logger("DrawerFeedSelection");
  UserAccount? account;
  final List<Feed> baseSubscriptions = [
    Feed.home(),
    Feed.popular(),
    Feed.all()
    //"Saved",
    //"History",
  ];

  DrawerFeedSelectionState();

  @override
  Widget build(BuildContext context) {
    final state = context.read<AccountsBloc>();
    return BlocBuilder<AccountsBloc, AccountState>(
      builder: (context, account) {
        if (account.status == AccountStatus.uninit) {
          state.add(Initialize());
        } else if (account.status == AccountStatus.unloaded) {
          state.add(LoadSubscriptions());
        } else if (account.status == AccountStatus.loaded) {
          return Flexible(
            fit: FlexFit.loose,
            child: ListView(
              children: [
                ...feeds.map((feed) => Text(feed)),
                Divider(),
                ...userOptions.map((option) => Text(option)),
                Divider(),
                Column(
                  spacing: 8.0,
                  children: [
                    ...account.subscriptions.map(
                      (sub) {
                        return InkWell(
                          onTap: () => Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (_) => FeedView(
                                feed: Feed.subreddit(sub.other.displayName),
                                initialSort: FeedSort.best(),
                              ),
                            ),
                          ),
                          child: Row(
                            spacing: 8.0,
                            children: [
                              SubredditIcon(icon: sub.icon, radius: 16),
                              Text(
                                sub.other.displayNamePrefixed,
                                style: Theme.of(context).textTheme.bodyMedium,
                              )
                            ],
                          ),
                        );
                      },
                    ),
                  ],
                ),
                Divider(),
                InkWell(
                  onTap: () => showLicensePage(context: context),
                  child: Row(
                    spacing: 8.0,
                    children: [
                      Icon(Icons.info_outline),
                      Text("Licenses",
                          style: Theme.of(context).textTheme.bodyMedium)
                    ],
                  ),
                )
              ],
            ),
          );
        }
        return Container();
      },
    );
  }
}
