import 'dart:async';

import 'package:crabir/accounts/bloc/accounts_bloc.dart' as accounts;
import 'package:crabir/accounts/widgets/account_selector.dart';
import 'package:crabir/drawer/drawer.dart';
import 'package:crabir/network_status.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/licenses_screen.dart';
import 'package:crabir/settings/settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/subscription/bloc/subscription_bloc.dart'
    as subscription;
import 'package:crabir/tabs_index.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/frb_generated.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:go_router/go_router.dart';
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

  // Add licenses rust libraries to list
  await registerRustLicenses();

  // Initializing `NetworkStatus`
  await NetworkStatus.init();

  await subscription.initRevenueCat();

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

class Crabir extends StatelessWidget {
  const Crabir({super.key});

  @override
  Widget build(BuildContext context) {
    return SettingsBlocsProviders(
      child: MultiBlocProvider(
        providers: [
          BlocProvider(
            create: (context) => accounts.AccountsBloc()
              ..add(accounts.AccountEvent.initialize()),
          )
        ],
        child: TopLevel(),
      ),
    );
  }
}

class TopLevel extends StatelessWidget {
  final _appRouter = appRouter;
  TopLevel({super.key});
  @override
  Widget build(BuildContext context) {
    final themeBloc = context.watch<ThemeBloc>().state;
    return MaterialApp.router(
      debugShowCheckedModeBanner: false,
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
      routerConfig: _appRouter,
    );
  }
}

class MainScreenView extends StatefulWidget {
  final StatefulNavigationShell
      navigationShell; // active route inside the shell

  const MainScreenView({super.key, required this.navigationShell});

  @override
  State<MainScreenView> createState() => _MainScreenViewState();
}

class _MainScreenViewState extends State<MainScreenView>
    with SingleTickerProviderStateMixin {
  bool showingDialog = false;

  Future<void> _showLoginDialog(BuildContext context) async {
    if (showingDialog) return;
    showingDialog = true;
    await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Login Required"),
        content: AccountSelector(showCurrentAccount: false),
        actions: [
          TextButton(
              onPressed: () => Navigator.of(ctx).pop(),
              child: const Text("Cancel")),
        ],
      ),
    );
    showingDialog = false;
  }

  bool _showAccountSelectionDialogue(int index) {
    final account = context.read<accounts.AccountsBloc>().state.account;
    const guardedPages = [1, 4]; // searchIndex, profileIndex
    return (account == null || account.isAnonymous) &&
        guardedPages.contains(index);
  }

  Widget _bottomBar() {
    final theme = CrabirTheme.of(context);
    return BottomNavigationBar(
      currentIndex: widget.navigationShell.currentIndex,
      type: BottomNavigationBarType.fixed,
      backgroundColor: theme.toolBarBackground,
      selectedItemColor: theme.primaryColor,
      unselectedItemColor: theme.toolBarText.withAlpha(120),
      showUnselectedLabels: false,
      showSelectedLabels: false,
      onTap: (index) {
        if (_showAccountSelectionDialogue(index)) {
          _showLoginDialog(context);
          return;
        } else if (index == subscriptionsIndex) {
          return context.go("/subscriptions");
        } else if (index == profileIndex) {
          final username =
              context.read<accounts.AccountsBloc>().state.account!.username;
          return context.go("/u/$username");
        }

        widget.navigationShell.goBranch(index);
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
    final accountsBloc = context.watch<accounts.AccountsBloc>();

    if (accountsBloc.state.status == accounts.AccountStatus.uninit()) {
      accountsBloc.add(accounts.Initialize());
    }
    return PopScope(
      canPop: widget.navigationShell.currentIndex == 0,
      onPopInvokedWithResult: (_, __) async {
        if (widget.navigationShell.currentIndex != 0) {
          context.go(DEFAULT_ROUTE);
        }
      },
      child: SafeArea(
        child: Scaffold(
          drawer: AppDrawer(onAccountChanged: () {
            widget.navigationShell.goBranch(0);
          }),
          body: widget.navigationShell,
          bottomNavigationBar: _bottomBar(),
        ),
      ),
    );
  }
}
