part of 'accounts_bloc.dart';

// enum AccountStatus {
//   /// Need to load accounts from disk
//   uninit,
//
//   /// Need to fetch subscriptions
//   unloaded,
//   loading,
//
//   /// All good for current account
//   loaded,
//   failure;
//
//   final String? error;
//   const AccountStatus({this.error});
// }

@freezed
class AccountStatus with _$AccountStatus {
  const factory AccountStatus.uninit() = Uninit;
  const factory AccountStatus.loading() = Loading;
  const factory AccountStatus.unloaded() = Unloaded;
  const factory AccountStatus.loaded() = Loaded;
  const factory AccountStatus.failure({String? message}) = Failure;
}

@freezed
abstract class AccountState with _$AccountState {
  factory AccountState({
    @Default(Uninit()) AccountStatus status,
    UserAccount? account,
    @Default([]) List<Subreddit> subscriptions,
    @Default([]) List<Multi> multis,
    @Default([]) List<UserAccount> allAccounts,
  }) = _AccountState;
}
