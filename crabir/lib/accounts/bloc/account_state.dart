part of 'accounts_bloc.dart';

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
