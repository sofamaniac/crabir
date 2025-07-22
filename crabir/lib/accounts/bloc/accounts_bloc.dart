import 'package:crabir/login.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:logging/logging.dart';
import 'package:shared_preferences/shared_preferences.dart';

part 'account_event.dart';
part 'account_state.dart';

class AccountsBloc extends Bloc<AccountEvent, AccountState> {
  List<UserAccount> accounts = [];
  int? selectedAccount;

  final Logger log = Logger("AccountsBloc");

  AccountsBloc() : super(AccountState()) {
    on<Initialize>(_initialize);
    on<SelectAccount>(_selectAccount);
    on<LoadSubscriptions>(_onLoadSubscriptions);
  }

  Future<void> _initialize(Initialize _, Emitter<AccountState> emit) async {
    if (accounts.isNotEmpty) return;

    try {
      accounts = await AccountDatabase().getAccounts();
    } catch (_) {
      emit(AccountState(status: AccountStatus.failure));
      log.severe("Error while loading accounts");
    }
    accounts.add(UserAccount.anonymous());

    // Restore account index from shared preferences
    // if no account was set default to anonymous
    final prefs = await SharedPreferences.getInstance();
    selectedAccount = prefs.getInt("currentAccount") ?? accounts.length - 1;
    if (currentAccount == null) {
      emit(AccountState(
        status: AccountStatus.uninit,
      ));
    } else {
      if (currentAccount?.id == UserAccount.anonymous().id) {
        await RedditAPI.client().newLoggedOutUserToken();
      } else {
        await RedditAPI.client().authenticate(
          refreshToken: currentAccount!.refreshToken ?? "random",
        );
      }
      emit(AccountState(
        status: AccountStatus.unloaded,
        account: currentAccount,
      ));
    }
  }

  UserAccount? get currentAccount {
    if (selectedAccount == null || selectedAccount! >= accounts.length) {
      return null;
    }
    return accounts[selectedAccount!];
  }

  Future<void> _selectAccount(
    SelectAccount event,
    Emitter<AccountState> emit,
  ) async {
    // Save account index to disk
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt("currentAccount", event.index);
    selectedAccount = event.index;

    // Authenticate client
    if (currentAccount!.id != UserAccount.anonymous().id) {
      await RedditAPI.client().authenticate(
        refreshToken: currentAccount!.refreshToken!,
      );
    }
    emit(AccountState(
      status: AccountStatus.unloaded,
      account: currentAccount,
    ));
  }

  Future<void> _onLoadSubscriptions(
      LoadSubscriptions _, Emitter<AccountState> emit) async {
    if (currentAccount == UserAccount.anonymous()) {
      return;
    }
    log.info("Requesting subscriptions for ${currentAccount!.username}");
    try {
      List<Subreddit> subreddits = await RedditAPI.client().subsriptions();
      subreddits.sort((a, b) => a.other.displayName
          .toLowerCase()
          .compareTo(b.other.displayName.toLowerCase()));
      emit(state.copyWith(
        status: AccountStatus.loaded,
        subscriptions: subreddits,
      ));
    } catch (err) {
      //log.severe("Error while loading subscriptions: $err");
      emit(state.copyWith(status: AccountStatus.failure));
    }
  }
}
