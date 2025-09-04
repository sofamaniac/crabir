import 'package:auto_route/auto_route.dart';
import 'package:crabir/routes/routes.dart';
import 'package:flutter/material.dart';

@RoutePage()
class SettingsView extends StatelessWidget {
  const SettingsView({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Settings")),
      body: ListView(
        children: [
          ListTile(
            leading: Icon(Icons.article),
            title: Text("Posts"),
            onTap: () => context.router.navigate(PostsSettingsRoute()),
          ),
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
