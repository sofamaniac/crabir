import 'package:crabir/drawer.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/frb_generated.dart';
import 'package:crabir/feed/feed.dart';
import 'package:logging/logging.dart';
import 'package:provider/provider.dart';

Future<void> main() async {
  await RustLib.init();
  Logger.root.level = Level.ALL; // defaults to Level.INFO
  Logger.root.onRecord.listen((record) {
    debugPrint('${record.level.name}: ${record.time}: ${record.message}');
  });

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => DrawerModel()..loadAccounts(),
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
                // feedView(
                //     context,
                //     FeedState(
                //       feed: Feed.home(),
                //       sort: Sort.best(),
                //     )),
                FeedView(feed: Feed.home(), initialSort: Sort.best()),
                FeedView(feed: Feed.home(), initialSort: Sort.best()),
                FeedView(feed: Feed.home(), initialSort: Sort.best()),
                FeedView(feed: Feed.home(), initialSort: Sort.best()),
                FeedView(feed: Feed.home(), initialSort: Sort.best()),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
