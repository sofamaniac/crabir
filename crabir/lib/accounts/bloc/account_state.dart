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

@freezed
abstract class AccountState with _$AccountState {
  factory AccountState({
    @Default(AccountStatus.uninit) AccountStatus status,
    UserAccount? account,
    @Default([]) List<Subreddit> subscriptions,
    @Default([]) List<Multi> multis,
    @Default([]) List<UserAccount> allAccounts,
  }) = _AccountState;
}
