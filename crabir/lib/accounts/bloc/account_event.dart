part of 'accounts_bloc.dart';

@freezed
sealed class AccountEvent with _$AccountEvent {
  const factory AccountEvent.initialize() = Initialize;
  const factory AccountEvent.selectAccount(int index) = SelectAccount;
  const factory AccountEvent.loadSubscriptions() = LoadSubscriptions;
  const factory AccountEvent.addAccount() = AddAccount;
  const factory AccountEvent.logout() = Logout;
}
