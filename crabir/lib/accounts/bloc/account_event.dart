part of 'accounts_bloc.dart';

sealed class AccountEvent {}

class Initialize extends AccountEvent {}

class SelectAccount extends AccountEvent {
  final int index;
  SelectAccount({required this.index});
}

class LoadSubscriptions extends AccountEvent {}
