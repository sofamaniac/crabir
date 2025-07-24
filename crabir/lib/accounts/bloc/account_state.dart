part of 'accounts_bloc.dart';

enum AccountStatus {
  /// Need to load accounts from disk
  uninit,

  /// Need to fetch subscriptions
  unloaded,
  loading,

  /// All good for current account
  loaded,
  failure
}

class AccountState {
  final AccountStatus status;
  final UserAccount? account;
  final List<Subreddit> subscriptions;
  final List<Multi> multis;
  final List<UserAccount> allAccounts;

  AccountState({
    this.status = AccountStatus.uninit,
    this.subscriptions = const [],
    this.multis = const [],
    this.account,
    this.allAccounts = const [],
  });

  AccountState copyWith({
    AccountStatus? status,
    List<Subreddit>? subscriptions,
    List<Multi>? multis,
    UserAccount? account,
    List<UserAccount>? allAccounts,
  }) {
    return AccountState(
      status: status ?? this.status,
      subscriptions: subscriptions ?? this.subscriptions,
      multis: multis ?? this.multis,
      account: account ?? this.account,
      allAccounts: allAccounts ?? this.allAccounts,
    );
  }
}
