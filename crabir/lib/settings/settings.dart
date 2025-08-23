import 'package:auto_route/auto_route.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

@RoutePage()
class SettingsView extends StatelessWidget {
  const SettingsView({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView(
        children: [
          ListTile(
            leading: Icon(Icons.comment),
            title: Text("Comments"),
            onTap: () =>
                AutoRouter.of(context).navigate(CommentsSettingsRoute()),
          )
        ],
      ),
    );
  }
}
