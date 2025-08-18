import 'package:collection/collection.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/base_feeds.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/login.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:logging/logging.dart';

part 'drawer_state.dart';
part 'drawer_feed_selection.dart';

class AppDrawer extends StatefulWidget {
  const AppDrawer({super.key});
  @override
  State<StatefulWidget> createState() => DrawerState();
}
