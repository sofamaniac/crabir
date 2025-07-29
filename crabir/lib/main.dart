import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/drawer/drawer.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/frb_generated.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:logging/logging.dart';

Future<void> main() async {
  await RustLib.init();
  Logger.root.level = Level.ALL; // defaults to Level.INFO
  Logger.root.onRecord.listen((record) {
    debugPrint('${record.level.name}: ${record.time}: ${record.message}');
  });

  runApp(Crabir());
}

class Crabir extends StatelessWidget {
  Crabir({super.key});

  final _appRouter = AppRouter();

  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: [
        BlocProvider(create: (context) => AccountsBloc()..add(Initialize())),
        BlocProvider(create: (context) => ThemeBloc()),
      ],
      child: MaterialApp.router(
        themeMode: ThemeMode.system,
        theme: ThemeData.light(useMaterial3: true),
        darkTheme: ThemeData.dark(useMaterial3: true),
        routerConfig: _appRouter.config(),
      ),
    );
  }
}

@RoutePage()
class MainScreenView extends StatelessWidget {
  const MainScreenView({super.key});
  @override
  Widget build(BuildContext context) {
    final routes = <PageRouteInfo>[
      NamedRoute("HomeFeedRoute"),
      SearchRoute(),
      SubscriptionsTabRoute(),
      InboxRoute(),
      CurrentUserRoute(),
    ];
    final state = context.watch<AccountsBloc>().state;
    return AutoTabsRouter.tabBar(
      key: ValueKey(state.account?.username ?? ""),
      homeIndex: 0,
      physics: NeverScrollableScrollPhysics(),
      routes: routes,
      builder: (context, child, tabController) {
        final tabsRouter = AutoTabsRouter.of(context);
        return Scaffold(
          body: child,
          drawer: AppDrawer(),
          bottomNavigationBar: TabBar(
            controller: tabController,
            onTap: (index) {
              tabController.animateTo(index);
              tabsRouter.setActiveIndex(index);
              if (index == 2) {
                tabsRouter
                    .stackRouterOfIndex(index)
                    ?.replaceAll([SubscriptionsTabRoute()]);
              }
            },
            tabs: [
              Tab(icon: Icon(Icons.home)),
              Tab(icon: Icon(Icons.search)),
              Tab(icon: Icon(Icons.list)),
              Tab(icon: Icon(Icons.mail)),
              Tab(icon: Icon(Icons.person)),
            ],
          ),
        );
      },
    );
  }
}

@RoutePage()
class SubscriptionsOrFeedView extends AutoRouter {
  const SubscriptionsOrFeedView({super.key});
}

@RoutePage()
class SearchView extends AutoRouter {
  const SearchView({super.key});
}

@RoutePage()
class InboxView extends AutoRouter {
  const InboxView({super.key});
}
