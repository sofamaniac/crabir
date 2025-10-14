import 'dart:async';

import 'package:crabir/accounts/login.dart';
import 'package:crabir/accounts/user_account.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:logging/logging.dart';
import 'package:shared_preferences/shared_preferences.dart';

part 'account_event.dart';
part 'account_state.dart';
part 'accounts_bloc.freezed.dart';

class AccountsBloc extends Bloc<AccountEvent, AccountState> {
  List<UserAccount> _accounts = [];
  int? _selectedAccount;

  final Logger log = Logger("AccountsBloc");

  AccountsBloc() : super(AccountState()) {
    on<Initialize>(_initialize);
    on<SelectAccount>(_selectAccount);
    on<LoadSubscriptions>(_onLoadSubscriptions);
    on<AddAccount>(_addAccount);
    on<Logout>(_logout);
  }

  /// Load accounts into _accounts
  /// Throws an exception when failing to access the database.
  Future<void> _loadAccounts() async {
    try {
      _accounts = await AccountDatabase().getAccounts();
    } catch (err) {
      log.severe("Error while loading accounts: $err");
      rethrow;
    }
    _accounts.add(UserAccount.anonymous());
  }

  Future<void> _initialize(Initialize _, Emitter<AccountState> emit) async {
    if (_accounts.isNotEmpty) return;

    try {
      await _loadAccounts();
    } catch (err) {
      log.severe("Error while loading accounts: $err");
      emit(state.copyWith(status: Failure(message: "$err")));
    }

    // Restore account index from shared preferences
    // if no account was set default to anonymous
    final prefs = await SharedPreferences.getInstance();
    _selectedAccount = prefs.getInt("currentAccount") ?? _accounts.length - 1;
    if (_currentAccount == null) {
      emit(state.copyWith(
        status: Uninit(),
      ));
    } else {
      if (_currentAccount?.isAnonymous == true) {
        await RedditAPI.client().newLoggedOutUserToken();
      } else {
        await RedditAPI.client().authenticate(
          refreshToken: _currentAccount!.refreshToken!,
        );
      }
      emit(
        state.copyWith(
          status: Unloaded(),
          account: _currentAccount,
          allAccounts: _accounts,
        ),
      );
      await _onLoadSubscriptions(LoadSubscriptions(), emit);
    }
  }

  UserAccount? get _currentAccount {
    if (_selectedAccount == null || _selectedAccount! >= _accounts.length) {
      return null;
    }
    return _accounts[_selectedAccount!];
  }

  Future<void> _selectAccount(
    SelectAccount event,
    Emitter<AccountState> emit,
  ) async {
    // Save account index to disk
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt("currentAccount", event.index);
    _selectedAccount = event.index;

    // Authenticate client
    if (_currentAccount!.id != UserAccount.anonymous().id) {
      await RedditAPI.client().authenticate(
        refreshToken: _currentAccount!.refreshToken!,
      );
    } else {
      await RedditAPI.client().newLoggedOutUserToken();
    }
    emit(state.copyWith(
      status: Unloaded(),
      account: _currentAccount,
    ));
    await _onLoadSubscriptions(LoadSubscriptions(), emit);
  }

  Future<void> _onLoadSubscriptions(
    LoadSubscriptions _,
    Emitter<AccountState> emit,
  ) async {
    if (_currentAccount?.isAnonymous == true) {
      emit(state.copyWith(status: Loaded(), subscriptions: [], multis: []));
      return;
    }
    log.info("Requesting subscriptions for ${_currentAccount!.username}");
    try {
      List<Subreddit> subreddits = await RedditAPI.client().subscriptions();
      subreddits.sort(
        (a, b) => a.other.displayName.toLowerCase().compareTo(
              b.other.displayName.toLowerCase(),
            ),
      );
      List<Multi> multis = await RedditAPI.client().multis();
      multis.sort(
        (a, b) => a.displayName.toLowerCase().compareTo(
              b.displayName.toLowerCase(),
            ),
      );
      emit(state.copyWith(
        status: Loaded(),
        subscriptions: subreddits,
        multis: multis,
      ));
    } catch (err) {
      log.severe("Error while loading subscriptions: $err");
      emit(state.copyWith(status: Failure(message: "$err")));
    }
  }

  FutureOr<void> _addAccount(
    AddAccount event,
    Emitter<AccountState> emit,
  ) async {
    if (await loginToReddit()) {
      await _loadAccounts();
      final index = _accounts.length - 2;
      await _selectAccount(SelectAccount(index), emit);
    }
  }

  FutureOr<void> _logout(Logout event, Emitter<AccountState> emit) async {
    if (_currentAccount != null) {
      try {
        await RedditAPI.client().logout();
      } catch (err) {
        return;
      }
      await AccountDatabase().removeAccount(_currentAccount!);
      await _loadAccounts();
      final index = _accounts.length - 1;
      await _selectAccount(SelectAccount(index), emit);
    }
  }
}
