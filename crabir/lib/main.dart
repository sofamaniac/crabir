import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/drawer/drawer.dart';
import 'package:crabir/routes.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/frb_generated.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:logging/logging.dart';

final _router = GoRouter(
  initialLocation: "/home",
  routes: [
    ShellRoute(
      builder: (context, state, child) => Scaffold(
        drawer: AppDrawer(),
        body: child,
      ),
      routes: ROUTES
          .map(
            (route) => GoRoute(path: route.path, builder: route.builder),
          )
          .toList(),
    ),
    // No drawer nor bottom bar when in post
    GoRoute(
      path: PostRoute().path,
      builder: PostRoute().builder,
    )
  ],
);

Future<void> main() async {
  await RustLib.init();
  Logger.root.level = Level.ALL; // defaults to Level.INFO
  Logger.root.onRecord.listen((record) {
    debugPrint('${record.level.name}: ${record.time}: ${record.message}');
  });

  runApp(const Crabir());
}

class Crabir extends StatelessWidget {
  const Crabir({super.key});

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => AccountsBloc()..add(Initialize()),
      child: MaterialApp.router(
        themeMode: ThemeMode.system,
        theme: ThemeData.light(useMaterial3: true),
        //darkTheme: ThemeData.dark(useMaterial3: true),
        darkTheme: ThemeData(
          colorScheme: ColorScheme.fromSeed(
            seedColor: Colors.orange,
            brightness: Brightness.dark,
          ),
        ),
        routerConfig: _router,
        // home: DefaultTabController(
        //   length: 5,
        //   child: Scaffold(
        //     drawer: AppDrawer(),
        //     bottomNavigationBar: const TabBar(
        //       tabs: [
        //         Tab(icon: Icon(Icons.home)),
        //         Tab(icon: Icon(Icons.search)),
        //         Tab(icon: Icon(Icons.list)),
        //         Tab(icon: Icon(Icons.mail)),
        //         Tab(icon: Icon(Icons.person))
        //       ],
        //     ),
        //     body: TabBarView(
        //       physics: NeverScrollableScrollPhysics(),
        //       children: [
        //         FeedView(feed: Feed.home(), initialSort: FeedSort.best()),
        //         FeedView(feed: Feed.home(), initialSort: FeedSort.best()),
        //         FeedView(feed: Feed.home(), initialSort: FeedSort.best()),
        //         FeedView(feed: Feed.home(), initialSort: FeedSort.best()),
        //         CurrentUserView(),
        //       ],
        //     ),
        //   ),
        // ),
      ),
    );
  }
}
