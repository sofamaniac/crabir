import 'package:collection/collection.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/accounts/widgets/login_button.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class AccountSelector extends StatelessWidget {
  /// When set to false, do not display a button for the current account.
  final bool showCurrentAccount;
  final void Function()? onTapCallback;
  const AccountSelector({
    super.key,
    this.showCurrentAccount = true,
    this.onTapCallback,
  });

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<AccountsBloc>();
    final state = bloc.state;
    return ListView(
      shrinkWrap: true,
      children: [
        ...state.allAccounts.mapIndexed(
          (index, account) {
            if (account == state.account && !showCurrentAccount) {
              return SizedBox.shrink();
            } else {
              return ListTile(
                onTap: () async {
                  bloc.add(SelectAccount(index));
                  onTapCallback?.call();
                  Scaffold.of(context).closeDrawer();
                },
                leading: CircleAvatar(
                  radius: 16,
                  foregroundImage: NetworkImage(account.profilePicture),
                ),
                title: Text(account.username),
              );
            }
          },
        ),
        LoginButton(),
        LogoutButton(),
      ],
    );
  }
}
