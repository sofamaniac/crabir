import 'package:auto_route/auto_route.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/data/data_settings.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

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
          ),
          ListTile(
            leading: Icon(Icons.storage),
            title: Text("Data and Storage"),
            onTap: () => AutoRouter.of(context).navigate(DataSettingsRoute()),
          )
        ],
      ),
    );
  }
}

List<BlocProvider> initSettingsBlocs() {
  return [
    BlocProvider(create: (context) => ThemeBloc()),
    BlocProvider(create: (context) => CommentsSettingsCubit()),
    BlocProvider(create: (context) => PostsSettingsCubit()),
    BlocProvider(create: (context) => DataSettingsCubit()),
  ];
}
