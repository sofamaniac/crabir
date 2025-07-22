import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/drawer.dart';
import 'package:crabir/login.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/user.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/frb_generated.dart';
import 'package:crabir/feed/feed.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:logging/logging.dart';

Future<void> main() async {
  await RustLib.init();
  Logger.root.level = Level.ALL; // defaults to Level.INFO
  Logger.root.onRecord.listen((record) {
    debugPrint('${record.level.name}: ${record.time}: ${record.message}');
  });

  runApp(const Crabir());
}

class AppState extends ChangeNotifier {
  UserAccount? _currentUser;
  UserAccount? get currentUser => _currentUser;
  set currentUser(UserAccount? account) {
    if (account != _currentUser) {
      _currentUser = account;
      notifyListeners();
    }
  }

  int _currentTab = 0;
  int get currentTab => _currentTab;
  set currentTab(int index) {
    if (index != _currentTab) {
      _currentTab = index;
      notifyListeners();
    }
  }
}

class Crabir extends StatelessWidget {
  const Crabir({super.key});

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => AccountsBloc(),
      child: MaterialApp(
        themeMode: ThemeMode.system,
        theme: ThemeData.light(useMaterial3: true),
        darkTheme: ThemeData.dark(useMaterial3: true),
        home: DefaultTabController(
          length: 5,
          child: Scaffold(
            drawer: AppDrawer(),
            bottomNavigationBar: const TabBar(
              tabs: [
                Tab(icon: Icon(Icons.home)),
                Tab(icon: Icon(Icons.search)),
                Tab(icon: Icon(Icons.list)),
                Tab(icon: Icon(Icons.mail)),
                Tab(icon: Icon(Icons.person))
              ],
            ),
            body: TabBarView(
              physics: NeverScrollableScrollPhysics(),
              children: [
                FeedView(feed: Feed.home(), initialSort: FeedSort.best()),
                FeedView(feed: Feed.home(), initialSort: FeedSort.best()),
                FeedView(feed: Feed.home(), initialSort: FeedSort.best()),
                FeedView(feed: Feed.home(), initialSort: FeedSort.best()),
                CurrentUserView(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
