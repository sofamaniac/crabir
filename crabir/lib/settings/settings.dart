import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/data/data_settings.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/settings/lateral_menu/lateral_menu_settings.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/settings/theme/theme_editor.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

part 'settings.go_route_ext.dart';

@CrabirRoute()
class SettingsView extends StatelessWidget {
  const SettingsView({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Settings")),
      body: ListView(
        children: [
          ListTile(
            leading: Icon(Icons.palette_outlined),
            title: Text("Theme"),
            onTap: () => CrabirThemeEditor().goNamed(context),
          ),
          ListTile(
            leading: Icon(Icons.article),
            title: Text("Posts"),
            onTap: () => PostsSettingsView().goNamed(context),
          ),
          ListTile(
            leading: Icon(Icons.comment),
            title: Text("Comments"),
            onTap: () => CommentsSettingsView().goNamed(context),
          ),
          ListTile(
            leading: Icon(Icons.keyboard_tab),
            title: Text("Lateral Menu"),
            onTap: () => LateralMenuSettingsView().goNamed(context),
          ),
          ListTile(
            leading: Icon(Icons.storage),
            title: Text("Data and Storage"),
            onTap: () => DataSettingsView().goNamed(context),
          ),
          ListTile(
            leading: Icon(Icons.filter),
            title: Text("Content filters"),
            onTap: () => FiltersSettingsView().goNamed(context),
          ),
          ListTile(
            onTap: () => showLicensePage(context: context),
            title: Text(
              "Licenses",
              style: Theme.of(context).textTheme.bodyMedium,
            ),
            leading: Icon(Icons.info_outline),
          ),
        ],
      ),
    );
  }
}

class SettingsBlocsProviders extends StatelessWidget {
  final Widget child;
  const SettingsBlocsProviders({
    super.key,
    required this.child,
  });

  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: [
        BlocProvider(create: (_) => ThemeBloc()),
        BlocProvider(create: (_) => CommentsSettingsCubit()),
        BlocProvider(create: (_) => PostsSettingsCubit()),
        BlocProvider(create: (_) => DataSettingsCubit()),
        BlocProvider(create: (_) => FiltersSettingsCubit()),
        BlocProvider(create: (_) => ReadPosts()),
        BlocProvider(create: (_) => GlobalFiltersCubit()),
        BlocProvider(create: (_) => LateralMenuSettingsCubit()),
      ],
      child: child,
    );
  }
}
