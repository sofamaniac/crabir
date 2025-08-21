import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/drawer/drawer.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/tabs_index.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/frb_generated.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';
import 'package:path_provider/path_provider.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

Future<void> main() async {
  await RustLib.init();
  Logger.root.level = Level.ALL; // defaults to Level.INFO
  Logger.root.onRecord.listen((record) {
    debugPrint('${record.level.name}: ${record.time}: ${record.message}');
  });

  // Initializing `HydratedStorage`
  WidgetsFlutterBinding.ensureInitialized();
  HydratedBloc.storage = await HydratedStorage.build(
    storageDirectory: kIsWeb
        ? HydratedStorageDirectory.web
        : HydratedStorageDirectory(
            (await getApplicationDocumentsDirectory()).path,
          ),
  );

  runApp(Crabir());
}

class Crabir extends StatelessWidget {
  const Crabir({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: [
        BlocProvider(create: (context) => AccountsBloc()..add(Initialize())),
        BlocProvider(create: (context) => ThemeBloc()),
      ],
      child: TopLevel(),
    );
  }
}

class TopLevel extends StatelessWidget {
  final _appRouter = AppRouter();
  TopLevel({super.key});
  @override
  Widget build(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    return MaterialApp.router(
      localizationsDelegates: [
        AppLocalizations.delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      supportedLocales: const [
        Locale('en'),
      ],
      themeMode: ThemeMode.system,
      theme: ThemeData.light(useMaterial3: true),
      darkTheme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.dark(
          primary: theme.primaryColor,
          surface: theme.background,
          secondary: theme.highlight,
          outlineVariant: Colors.white24,
        ),
        cardTheme: CardTheme(color: theme.cardBackground),
      ),
      routerConfig: _appRouter.config(),
    );
  }
}

@RoutePage()
class MainScreenView extends StatelessWidget {
  const MainScreenView({super.key});
  @override
  Widget build(BuildContext context) {
    final routes = <PageRouteInfo>[
      NamedRoute(homeRouteName),
      SearchSubredditsRoute(),
      SubscriptionsTabRoute(),
      InboxRoute(),
      CurrentUserRoute(),
    ];
    //final accounts = context.watch<AccountsBloc>().state;
    final theme = context.watch<ThemeBloc>().state;
    return AutoTabsRouter.tabBar(
      homeIndex: 0,
      physics: NeverScrollableScrollPhysics(),
      routes: routes,
      builder: (context, child, tabController) {
        final tabsRouter = AutoTabsRouter.of(context);
        return Scaffold(
          backgroundColor: theme.background,
          body: child,
          drawer: AppDrawer(),
          bottomNavigationBar: TabBar(
            labelColor: Theme.of(context).primaryTextTheme.bodyLarge!.color,
            indicatorColor: theme.primaryColor,
            controller: tabController,
            onTap: (index) {
              tabController.animateTo(index);
              //tabsRouter.setActiveIndex(index);
              if (index == subscriptionsIndex) {
                tabsRouter
                    .stackRouterOfIndex(index)
                    ?.replaceAll([SubscriptionsTabRoute()]);
              } else if (index == searchIndex) {
                tabsRouter
                    .stackRouterOfIndex(index)
                    ?.replaceAll([SearchSubredditsRoute()]);
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
