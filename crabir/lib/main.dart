import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/accounts/widgets/account_selector.dart';
import 'package:crabir/drawer/drawer.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
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
import 'package:crabir/l10n/app_localizations.dart';

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
        BlocProvider(create: (context) => CommentsSettingsCubit()),
        BlocProvider(create: (context) => PostsSettingsCubit())
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
      supportedLocales: AppLocalizations.supportedLocales,
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
        cardTheme: CardThemeData(color: theme.cardBackground),
      ),
      routerConfig: _appRouter.config(),
    );
  }
}

@RoutePage()
class MainScreenView extends StatefulWidget {
  const MainScreenView({super.key});

  @override
  State<MainScreenView> createState() => _MainScreenViewState();
}

class _MainScreenViewState extends State<MainScreenView> {
  bool addListener = true;
  bool showingDialog = false;

  bool showAccountSelectionDialogue(int index) {
    final account = context.read<AccountsBloc>().state.account;
    return (account == null || account.isAnonymous) &&
        (index == profileIndex || index == inboxIndex);
  }

  void _showLoginDialog(BuildContext context) async {
    if (showingDialog) {
      return;
    }
    showingDialog = true;
    await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Login Required"),
        content: AccountSelector(
          showCurrentAccount: false,
        ),
        actions: [
          TextButton(
            child: const Text("Cancel"),
            onPressed: () => Navigator.of(ctx).pop(),
          ),
        ],
      ),
    );
    showingDialog = false;
  }

  @override
  Widget build(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    final _ = context.watch<AccountsBloc>().state.account;
    final routes = <PageRouteInfo>[
      NamedRoute(homeRouteName),
      SearchSubredditsRoute(),
      SubscriptionsTabRoute(),
      InboxRoute(),
      UserRoute(),
    ];
    return AutoTabsRouter.tabBar(
      homeIndex: 0,
      physics: NeverScrollableScrollPhysics(),
      routes: routes,
      builder: (context, child, tabController) {
        void listener() {
          final index = tabController.index;
          if (showAccountSelectionDialogue(index) &&
              tabController.indexIsChanging &&
              tabController.index > tabController.previousIndex) {
            // reset to previous tab
            tabController.index = tabController.previousIndex;

            WidgetsBinding.instance.addPostFrameCallback(
              (_) => _showLoginDialog(context),
            );
          }
        }

        if (addListener) {
          tabController.addListener(listener);
          addListener = false;
        }

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
              tabsRouter.setActiveIndex(index);
              final rootRoutes = {
                subscriptionsIndex: const SubscriptionsTabRoute(),
                searchIndex: const SearchSubredditsRoute(),
                profileIndex: UserRoute(),
              };

              final route = rootRoutes[index];
              if (route != null) {
                print("REPLACEING ROUTE");
                tabsRouter.stackRouterOfIndex(index)!.popUntilRoot();
                tabsRouter.stackRouterOfIndex(index)?.replaceAll([route]);
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
