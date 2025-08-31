import 'package:crabir/login.dart';
import 'package:flutter/material.dart';

class LoginButton extends StatelessWidget {
  const LoginButton({super.key});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(Icons.add),
      // TODO: localization
      title: Text("Add account"),
      onTap: () {
        loginToReddit();
      },
    );
  }
}
