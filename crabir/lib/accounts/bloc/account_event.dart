part of 'accounts_bloc.dart';

@freezed
sealed class AccountEvent with _$AccountEvent {
  const factory AccountEvent.initialize() = Initialize;
  const factory AccountEvent.selectAccount(int index) = SelectAccount;
  const factory AccountEvent.loadSubscriptions() = LoadSubscriptions;
}

// class Initialize extends AccountEvent {}
//
// class SelectAccount extends AccountEvent {
//   final int index;
//   SelectAccount({required this.index});
// }
//
// class LoadSubscriptions extends AccountEvent {}
