import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class LoginButton extends StatelessWidget {
  const LoginButton({super.key});

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<AccountsBloc>();
    final locales = AppLocalizations.of(context);
    return ListTile(
      leading: Icon(Icons.add),
      title: Text(locales.loginButtonLabel),
      onTap: () {
        bloc.add(AddAccount());
      },
    );
  }
}

class LogoutButton extends StatelessWidget {
  const LogoutButton({super.key});

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<AccountsBloc>();
    final locales = AppLocalizations.of(context);
    return ListTile(
      leading: Icon(Icons.logout),
      title: Text(locales.logoutButtonLabel),
      onTap: () {
        bloc.add(Logout());
      },
    );
  }
}
