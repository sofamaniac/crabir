import 'dart:async';

import 'package:app_links/app_links.dart';
import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/accounts/widgets/account_selector.dart';
import 'package:crabir/drawer/drawer.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/network_status.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/settings.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/tabs_index.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/frb_generated.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:http/http.dart' as http;
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';
import 'package:path_provider/path_provider.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:url_launcher/url_launcher.dart';

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

  // Initializing `NetworkStatus`
  await NetworkStatus.init();

  runApp(Crabir());
}

void _handleRedditLink(StackRouter router, String url) async {
  final uri = Uri.parse(url);
  if (uri.host.contains('reddit.com') || uri.host == 'redd.it') {
    // Navigate using AutoRoute
    final Uri destination;
    if (uri.pathSegments.contains("s")) {
      destination = await _resolveRedditShortlink(uri);
    } else {
      destination = uri;
    }
    router.pushPath(destination.toString());
  } else {
    // Open in external browser
    launchUrl(uri);
  }
}

class Crabir extends StatelessWidget {
  const Crabir({super.key});

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => AccountsBloc()..add(Initialize()),
      child: SettingsBlocsProviders(
        child: TopLevel(),
      ),
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
      routerConfig: _appRouter.config(deepLinkTransformer: (deepLink) {
        if (deepLink.path.contains('s')) {
          // continue with the platform link
          return _resolveRedditShortlink(deepLink);
        } else {
          return SynchronousFuture(deepLink);
        }
      }),
    );
  }
}

/// Reddit share links (e.g. https://reddit.com/python/s/SOME_ID) are simple redirect.
Future<Uri> _resolveRedditShortlink(Uri uri) async {
  final client = http.Client();
  try {
    final req = http.Request('GET', uri)..followRedirects = false;
    final resp = await client.send(req);

    final location = resp.headers['location'];
    if (location != null && location.isNotEmpty) {
      return uri.resolve(location);
    }
    return uri; // no redirect
  } finally {
    client.close();
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
  StreamSubscription<Uri?>? _linkSub;

  // Scaffold key to control the drawers
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();
  Widget? _endDrawer; // dynamic end drawer

  @override
  void initState() {
    super.initState();

    final router = AutoRouter.of(context);

    // Subscribe to app links
    _linkSub = AppLinks().uriLinkStream.listen((uri) {
      _handleRedditLink(router, uri.toString());
    });

    // Handle initial link
    AppLinks().getInitialLink().then((uri) {
      if (uri != null) {
        _handleRedditLink(router, uri.toString());
      }
    });
  }

  @override
  void dispose() {
    _linkSub?.cancel();
    super.dispose();
  }

  bool showAccountSelectionDialogue(int index) {
    final account = context.read<AccountsBloc>().state.account;
    return (account == null || account.isAnonymous) &&
        (index == profileIndex || index == inboxIndex);
  }

  void _showLoginDialog(BuildContext context) async {
    if (showingDialog) return;
    showingDialog = true;
    await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Login Required"),
        content: AccountSelector(showCurrentAccount: false),
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

  // -------- Dynamic End Drawer API --------
  void setEndDrawer(Widget? drawer) {
    setState(() => _endDrawer = drawer);
  }

  void openEndDrawer(Widget drawer) {
    setEndDrawer(drawer);
    WidgetsBinding.instance.addPostFrameCallback(
      (_) => _scaffoldKey.currentState?.openEndDrawer(),
    );
  }

  void closeEndDrawer() {
    _scaffoldKey.currentState?.closeEndDrawer();
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
      physics: const NeverScrollableScrollPhysics(),
      routes: routes,
      builder: (context, child, tabController) {
        // -------- Tab change listener --------
        void listener() {
          final index = tabController.index;
          if (showAccountSelectionDialogue(index) &&
              tabController.indexIsChanging &&
              tabController.index > tabController.previousIndex) {
            tabController.index = tabController.previousIndex;
            WidgetsBinding.instance.addPostFrameCallback(
              (_) => _showLoginDialog(context),
            );
          }
        }

        void disableVideoOnChange() {
          AnimatedContentController.currentlyPlaying.value = null;
        }

        if (addListener) {
          tabController.addListener(listener);
          tabController.addListener(disableVideoOnChange);
          addListener = false;
        }

        final tabsRouter = AutoTabsRouter.of(context);

        // -------- Single Scaffold with dynamic end drawer --------
        return DrawerHost(
          setEndDrawer: setEndDrawer,
          openEndDrawer: openEndDrawer,
          closeEndDrawer: closeEndDrawer,
          child: Scaffold(
            key: _scaffoldKey,
            backgroundColor: theme.background,
            drawer: const AppDrawer(), // global left drawer
            endDrawer: _endDrawer, // dynamic per-screen right drawer
            body: child,
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
                  tabsRouter.stackRouterOfIndex(index)?.replaceAll([route]);
                }
              },
              tabs: const [
                Tab(icon: Icon(Icons.home)),
                Tab(icon: Icon(Icons.search)),
                Tab(icon: Icon(Icons.list)),
                Tab(icon: Icon(Icons.mail)),
                Tab(icon: Icon(Icons.person)),
              ],
            ),
          ),
        );
      },
    );
  }
}

// -------- DrawerHost InheritedWidget --------
class DrawerHost extends InheritedWidget {
  final void Function(Widget?) setEndDrawer;
  final void Function(Widget) openEndDrawer;
  final VoidCallback closeEndDrawer;

  const DrawerHost({
    super.key,
    required this.setEndDrawer,
    required this.openEndDrawer,
    required this.closeEndDrawer,
    required super.child,
  });

  static DrawerHost of(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<DrawerHost>()!;

  @override
  bool updateShouldNotify(DrawerHost oldWidget) => false;
}
