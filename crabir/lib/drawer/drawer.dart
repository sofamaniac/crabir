import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/accounts/user_account.dart';
import 'package:crabir/accounts/widgets/account_selector.dart';
import 'package:crabir/base_feeds.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/settings/theme/theme_event.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:logging/logging.dart';

part 'drawer_state.dart';
part 'drawer_feed_selection.dart';

class AppDrawer extends StatefulWidget {
  final VoidCallback? onAccountChanged;
  const AppDrawer({super.key, this.onAccountChanged});
  @override
  State<StatefulWidget> createState() => DrawerState();
}
