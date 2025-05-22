import 'dart:math';

import 'package:crabir/feed/feed.dart';
import 'package:crabir/login.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart';
import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class DrawerModel extends ChangeNotifier {
  List<UserAccount> _accounts = [];
  bool _isLoading = false;
  bool get isLoading => _isLoading;
  int? _currentAccount;
  bool _isSelectingAccount = false;

  bool get isSelectingAccount => _isSelectingAccount;
  UserAccount? get currentAccount =>
      _currentAccount != null ? _accounts[_currentAccount!] : null;

  List<Subreddit> _subscriptions = [];
  List<Subreddit> get subscriptions => _subscriptions;

  Future selectAccount(int index) async {
    // TODO persist to storage
    if (index < _accounts.length && index != _currentAccount) {
      _currentAccount = index;
      if (_accounts[index].id != UserAccount.anonymous().id) {
        await RedditAPI.client().authenticate(
          accessToken: _accounts[index].accessToken!,
          refreshToken: _accounts[index].refreshToken!,
        );
        await _loadSubreddits();
      }
      notifyListeners();
    }
  }

  void changeMode({bool? isSelectingAccount}) {
    _isSelectingAccount = isSelectingAccount ?? !_isSelectingAccount;
    notifyListeners();
  }

  UnmodifiableListView<UserAccount> get accounts =>
      UnmodifiableListView(_accounts);

  DrawerModel();

  /// load all accounts and select the last one added
  Future loadAccounts() {
    _isLoading = true;
    notifyListeners();

    return AccountDatabase().getAccounts().then((accounts) {
      _accounts = accounts;
      _accounts.add(UserAccount.anonymous());
      _isLoading = false;
      // Select last added account and if there is none the anonymous account will be at index 0
      // TODO load from shared_preferences
      selectAccount(max(0, _accounts.length - 2));
    }).catchError((err) {
      _isLoading = false;
      notifyListeners();
    });
  }

  Future _loadSubreddits() async {
    try {
      _subscriptions = await RedditAPI.client().subsriptions();
    } catch (err) {
      print("err while loading subreddits $err");
      _subscriptions = [];
    }
  }
}

class AppDrawer extends StatelessWidget {
  const AppDrawer({super.key});

  Widget currentAccountView(BuildContext context) {
    final state = context.watch<DrawerModel>();
    final icon =
        state.isSelectingAccount ? Icons.arrow_drop_up : Icons.arrow_drop_down;
    return Column(children: [
      Row(children: [
        state.isLoading
            ? const CircularProgressIndicator()
            : state.currentAccount!.build(context),
        IconButton(onPressed: () => state.changeMode(), icon: Icon(icon))
      ]),
      Divider()
    ]);
  }

  Widget accountSelector(BuildContext context) {
    final state = context.watch<DrawerModel>();

    if (state.isLoading) {
      return const CircularProgressIndicator();
    }
    return Expanded(
      child: ListView(
        children: [
          ...state.accounts.mapIndexed((index, account) => InkWell(
              onTap: () => state.selectAccount(index),
              child: account.build(context))),
          InkWell(
            onTap: () async {
              if (await loginToReddit()) {
                await state.loadAccounts();
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
          child: Consumer<DrawerModel>(
            builder: (context, accounts, child) {
              return Column(mainAxisSize: MainAxisSize.min, children: [
                currentAccountView(context),
                accounts.isSelectingAccount
                    ? accountSelector(context)
                    : DrawerFeedSelection().build(context)
              ]);
            },
          ),
        ),
      ),
    );
  }
}

class DrawerFeedSelection extends StatelessWidget {
  final List<String> feeds = ["Home", "Popular", "All", "Saved", "History"];
  final List<String> userOptions = ["Profile", "Inbox", "Moderation"];
  final List<String> subscriptions = [
    "Home",
    "Popular",
    "All",
    "Saved",
    "History",
  ];

  DrawerFeedSelection({super.key});

  Widget listView(List<String> elements, BuildContext context) {
    return Flexible(
        fit: FlexFit.loose,
        child: ListView.builder(
            itemCount: elements.length,
            itemBuilder: (BuildContext context, int index) {
              return Text(elements[index]);
            }));
    //return Column(children: elements.map((e) => Text(e)).toList());
  }

  @override
  Widget build(BuildContext context) {
    final state = context.watch<DrawerModel>();
    return Flexible(
        fit: FlexFit.loose,
        child: ListView(children: [
          ...feeds.map((feed) => Text(feed)),
          Divider(),
          ...userOptions.map((option) => Text(option)),
          Divider(),
          ...subscriptions.map((sub) => Text(sub)),
          ...state.subscriptions.map(
            (sub) => InkWell(
              onTap: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => FeedView(
                    feed: Feed.subreddit(sub.name),
                    initialSort: Sort.best(),
                  ),
                ),
              ),
              child: Row(children: [
                Image.network(sub.iconImg),
                Text(sub.displayNamePrefixed)
              ]),
            ),
          )
        ]));
  }
}
