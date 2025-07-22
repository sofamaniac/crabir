import 'dart:collection';

import 'package:crabir/login.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart';
import 'package:flutter/material.dart';
import 'package:logging/logging.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AccountsManager extends ChangeNotifier {
  List<UserAccount> _accounts = [];
  bool _isLoading = false;
  bool get isLoading => _isLoading || !_isInit;
  int? _currentAccount;
  bool _isInit = false;
  bool get isInit => _isInit;
  UnmodifiableListView<UserAccount> get accounts =>
      UnmodifiableListView(_accounts);
  UserAccount? get currentUser {
    if (_currentAccount == null) {
      return null;
    } else if (_currentAccount! >= _accounts.length) {
      return null;
    } else {
      return _accounts[_currentAccount!];
    }
  }

  final log = Logger("AccountsManager");

  List<Subreddit> _subreddits = [];
  UnmodifiableListView<Subreddit> get subreddits =>
      UnmodifiableListView(_subreddits);

  Future init() async {
    await _loadAccounts();
    await _readAccount();
    _isInit = true;
    notifyListeners();
  }

  Future _saveAccount() async {
    if (_currentAccount == null) return;
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt("currentAccount", _currentAccount!);
  }

  Future _readAccount() async {
    final prefs = await SharedPreferences.getInstance();
    // if no account was set default to anonymous
    await selectAccount(prefs.getInt("currentAccount") ?? 0);
  }

  Future selectAccount(int index) async {
    if (!_isInit) {
      return;
    }
    if (index < _accounts.length && index != _currentAccount) {
      _currentAccount = index;
      await _saveAccount();
      if (_accounts[index].id != UserAccount.anonymous().id) {
        await RedditAPI.client().authenticate(
          refreshToken: _accounts[index].refreshToken!,
        );
        await _loadSubreddits();
      }
      notifyListeners();
    }
  }

  /// load all accounts and select the last one added
  Future<void> _loadAccounts() async {
    if (_isLoading) {
      return;
    }
    _isLoading = true;

    return AccountDatabase().getAccounts().then((accounts) {
      _accounts = accounts;
      _accounts.add(UserAccount.anonymous());
      _isLoading = false;
    }).catchError((err) {
      _isLoading = false;
    });
  }

  Future _loadSubreddits() async {
    if (currentUser == UserAccount.anonymous() || _currentAccount == null) {
      return;
    }
    log.info("Requesting subscriptions for ${currentUser!.username}");
    try {
      _subreddits = await RedditAPI.client().subsriptions();
      _subreddits.sort((a, b) => a.other.displayName
          .toLowerCase()
          .compareTo(b.other.displayName.toLowerCase()));
      notifyListeners();
    } catch (err) {
      log.severe("Error while loading subscriptions: $err");
      _subreddits = [];
    }
  }
}
