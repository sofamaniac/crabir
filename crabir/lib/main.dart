import 'dart:async';
import 'dart:io';

import 'package:app_links/app_links.dart';
import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/accounts/widgets/account_selector.dart';
import 'package:crabir/drawer/drawer.dart';
import 'package:crabir/network_status.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/licenses_screen.dart';
import 'package:crabir/settings/settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/tabs_index.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/frb_generated.dart';
import 'package:flutter/services.dart';
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

  // Add licenses rust libraries to list
  await registerRustLicenses();

  // Initializing `NetworkStatus`
  await NetworkStatus.init();

  runApp(Crabir());
}

bool _defaultOnNavigationNotification(NavigationNotification _) {
  switch (WidgetsBinding.instance.lifecycleState) {
    case null:
    case AppLifecycleState.detached:
    case AppLifecycleState.inactive:
      // Avoid updating the engine when the app isn't ready.
      return true;
    case AppLifecycleState.resumed:
    case AppLifecycleState.hidden:
    case AppLifecycleState.paused:
      SystemNavigator.setFrameworkHandlesBack(true);

      /// This must be `true` instead of `notification.canHandlePop`, otherwise application closes on back gesture.
      return true;
  }
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
    try {
      router.pushPath(destination.toString());
    } catch (_) {
      Logger("Crabir").warning(
        "Failed to navigate to $uri, opening in browser",
      );
      launchUrl(uri, mode: LaunchMode.inAppBrowserView);
    }
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
    final themeBloc = context.watch<ThemeBloc>().state;
    return MaterialApp.router(
      // required to go back to home screen before exiting the app
      // because flutter is broken.
      onNavigationNotification: _defaultOnNavigationNotification,
      localizationsDelegates: [
        AppLocalizations.delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      supportedLocales: AppLocalizations.supportedLocales,
      themeMode: themeBloc.mode,
      darkTheme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.dark(
          primary: themeBloc.dark.primaryColor,
          surface: themeBloc.dark.background,
          secondary: themeBloc.dark.highlight,
          outlineVariant: Colors.white24,
        ),
        cardTheme: CardThemeData(
          color: themeBloc.dark.cardBackground,
          shadowColor: Colors.white,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.zero, // square corners
          ),
        ),
        dialogTheme: const DialogThemeData(
          backgroundColor: Color(0xFF1E1E1E),
          shape: RoundedRectangleBorder(),
        ),
      ),
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.light(
          primary: themeBloc.light.primaryColor,
          surface: themeBloc.light.background,
          secondary: themeBloc.light.highlight,
          outlineVariant: Colors.black26,
        ),
        cardTheme: CardThemeData(
          color: themeBloc.light.cardBackground,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.zero, // square corners
          ),
        ),
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

class _MainScreenViewState extends State<MainScreenView>
    with SingleTickerProviderStateMixin {
  bool addListener = true;
  bool showingDialog = false;
  StreamSubscription<Uri?>? _linkSub;

  @override
  void initState() {
    super.initState();

    // Subscribe to app links
    final router = AutoRouter.of(context);
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

  Widget bottomBar(
    BuildContext context,
    TabsRouter tabsRouter,
    TabController tabsController,
    CrabirTheme theme,
  ) {
    void listener() {
      final index = tabsController.index;
      if (showAccountSelectionDialogue(index) &&
          tabsController.indexIsChanging &&
          tabsController.index > tabsController.previousIndex) {
        tabsController.index = tabsController.previousIndex;
        WidgetsBinding.instance.addPostFrameCallback(
          (_) => _showLoginDialog(context),
        );
      }
    }

    if (addListener) {
      tabsController.addListener(listener);
      addListener = false;
    }
    return BottomNavigationBar(
      currentIndex: tabsController.index,
      backgroundColor: theme.toolBarBackground,
      selectedItemColor: theme.primaryColor,
      unselectedItemColor: theme.toolBarText.withAlpha(120),
      showUnselectedLabels: false,
      showSelectedLabels: false,
      type: BottomNavigationBarType.fixed, // so all items show
      onTap: (index) {
        AutoTabsRouter.of(context).setActiveIndex(index);
        //tabsController.animateTo(index);

        final rootRoutes = {
          1: const SearchSubredditsRoute(),
          2: const SubscriptionsTabRoute(),
          4: UserRoute(),
        };
        final route = rootRoutes[index];
        if (route != null) {
          context.tabsRouter.stackRouterOfIndex(index)?.replaceAll([route]);
        }
      },
      items: const [
        BottomNavigationBarItem(icon: Icon(Icons.home), label: "Home"),
        BottomNavigationBarItem(icon: Icon(Icons.search), label: "Search"),
        BottomNavigationBarItem(icon: Icon(Icons.list), label: "Lists"),
        BottomNavigationBarItem(icon: Icon(Icons.mail), label: "Messages"),
        BottomNavigationBarItem(icon: Icon(Icons.person), label: "Profile"),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = CrabirTheme.of(context);
    final account = context.watch<AccountsBloc>().state.account;

    // Change system navigation bar background color.
    if (Platform.isAndroid) {
      final brightness = switch (context.read<ThemeBloc>().state.mode) {
        ThemeMode.dark => Brightness.dark,
        ThemeMode.light => Brightness.light,
        ThemeMode.system => MediaQuery.of(context).platformBrightness,
      };
      SystemChrome.setSystemUIOverlayStyle(
        SystemUiOverlayStyle(
          systemNavigationBarColor: theme.toolBarBackground,
          systemNavigationBarIconBrightness: brightness,
          statusBarColor: theme.toolBarBackground,
          statusBarBrightness: brightness,
          statusBarIconBrightness: switch (brightness) {
            Brightness.light => Brightness.dark,
            Brightness.dark => Brightness.light,
          },
        ),
      );
    }

    final routes = <PageRouteInfo>[
      NamedRoute(homeRouteName),
      SearchSubredditsRoute(),
      SubscriptionsTabRoute(),
      InboxRoute(),
      UserRoute(),
    ];

    return AutoTabsRouter.tabBar(
      key: ValueKey(account?.id),
      homeIndex: 0,
      physics: const NeverScrollableScrollPhysics(),
      routes: routes,
      builder: (context, child, tabController) {
        final tabsRouter = AutoTabsRouter.of(context);

        return PopScope(
          canPop: tabsRouter.activeIndex == 0,
          onPopInvokedWithResult: (_, __) async {
            if (tabsRouter.activeIndex != 0) {
              // If not on the home tab, go back to home tab
              tabsRouter.setActiveIndex(0);
            }
          },
          child: SafeArea(
            child: Scaffold(
              backgroundColor: theme.background,
              drawer: const AppDrawer(),
              body: child,
              bottomNavigationBar:
                  bottomBar(context, tabsRouter, tabController, theme),
            ),
          ),
        );
      },
    );
  }
}
