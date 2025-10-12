import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/accounts/user_account.dart';
import 'package:crabir/accounts/widgets/account_selector.dart';
import 'package:crabir/drawer/base_feeds.dart';
import 'package:crabir/drawer/options.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/settings/lateral_menu/lateral_menu_settings.dart';
import 'package:crabir/settings/settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/settings/theme/theme_event.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:logging/logging.dart';

part 'drawer_state.dart';
part 'drawer_feed_selection.dart';

class AppDrawer extends StatefulWidget {
  final VoidCallback? onAccountChanged;
  const AppDrawer({super.key, this.onAccountChanged});
  @override
  State<StatefulWidget> createState() => DrawerState();
}
