import 'package:collection/collection.dart';
import 'package:crabir/accounts_manager.dart';
import 'package:crabir/feed/feed.dart';
import 'package:crabir/login.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';
import 'package:logging/logging.dart';
import 'package:provider/provider.dart';

// Taken from https://stackoverflow.com/a/50081214
extension HexColor on Color {
  /// String is in the format "aabbcc" or "ffaabbcc" with an optional leading "#".
  static Color fromHex(String hexString) {
    if (hexString.isEmpty) {
      return Colors.black;
    }
    final buffer = StringBuffer();
    if (hexString.length == 6 || hexString.length == 7) buffer.write('ff');
    buffer.write(hexString.replaceFirst('#', ''));
    try {
      return Color(int.parse(buffer.toString(), radix: 16));
    } catch (e) {
      print("Invalid Color $hexString.");
      return Color(0x00000000);
    }
  }

  /// Prefixes a hash sign if [leadingHashSign] is set to `true` (default is `true`).
  String toHex({bool leadingHashSign = true}) => '${leadingHashSign ? '#' : ''}'
      '${(255 * a).toInt().toRadixString(16).padLeft(2, '0')}'
      '${(255 * r).toInt().toRadixString(16).padLeft(2, '0')}'
      '${(255 * g).toInt().toRadixString(16).padLeft(2, '0')}'
      '${(255 * b).toInt().toRadixString(16).padLeft(2, '0')}';
}

class DrawerState extends State<AppDrawer> {
  bool _isSelectingAccount = false;

  bool get isSelectingAccount => _isSelectingAccount;

  Logger log = Logger("DrawerModel");

  DrawerState();

  void changeMode({bool? isSelectingAccount}) {
    setState(() {
      _isSelectingAccount = isSelectingAccount ?? !_isSelectingAccount;
    });
  }

  Widget currentAccountView(BuildContext context) {
    final icon =
        isSelectingAccount ? Icons.arrow_drop_up : Icons.arrow_drop_down;
    final state = context.watch<AccountsManager>();
    return DrawerHeader(
      child: Row(
        children: [
          state.currentUser?.build(context) ??
              const CircularProgressIndicator(),
          IconButton(onPressed: () => changeMode(), icon: Icon(icon))
        ],
      ),
    );
  }

  Widget accountSelector(BuildContext context) {
    final state = context.watch<AccountsManager>();
    if (state.currentUser == null) {
      return const CircularProgressIndicator();
    }
    return Expanded(
      child: ListView(
        children: [
          ...state.accounts.mapIndexed((index, account) => InkWell(
              onTap: () async {
                state.selectAccount(index);
              },
              child: account.build(context))),
          InkWell(
            onTap: () async {
              if (await loginToReddit()) {
                await state.init();
              }
            },
            child: Text("Add an account"),
          )
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Drawer(
        child: Padding(
          padding: EdgeInsetsDirectional.symmetric(horizontal: 8.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              currentAccountView(context),
              isSelectingAccount
                  ? accountSelector(context)
                  : DrawerFeedSelection()
            ],
          ),
        ),
      ),
    );
  }
}

class AppDrawer extends StatefulWidget {
  const AppDrawer({super.key});
  @override
  State<StatefulWidget> createState() => DrawerState();
}

class DrawerFeedSelection extends StatefulWidget {
  const DrawerFeedSelection({super.key});

  @override
  State<StatefulWidget> createState() => DrawerFeedSelectionState();
}

class DrawerFeedSelectionState extends State<DrawerFeedSelection> {
  final List<String> feeds = ["Home", "Popular", "All", "Saved", "History"];
  final List<String> userOptions = ["Profile", "Inbox", "Moderation"];
  final log = Logger("DrawerFeedSelection");
  bool _isInit = false;
  UserAccount? account;
  final List<Feed> baseSubscriptions = [
    Feed.home(),
    Feed.popular(),
    Feed.all()
    //"Saved",
    //"History",
  ];

  DrawerFeedSelectionState();

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AccountsManager>();
    if (account != state.currentUser) {
      _isInit = false;
      account = state.currentUser;
    }
    return Flexible(
      fit: FlexFit.loose,
      child: ListView(
        children: [
          ...feeds.map((feed) => Text(feed)),
          Divider(),
          ...userOptions.map((option) => Text(option)),
          Divider(),
          Column(
            spacing: 8.0,
            children: [
              ...state.subreddits.map(
                (sub) {
                  final icon = sub.icon;
                  late final CircleAvatar iconWidget;
                  if (icon?.url.toString() != null) {
                    iconWidget = CircleAvatar(
                      radius: 16,
                      foregroundImage: NetworkImage(icon!.url.toString()),
                      backgroundColor: Colors.transparent,
                    );
                  } else {
                    final keyColor = sub.keyColor;
                    final color = keyColor.isEmpty ? keyColor : "#000000";
                    iconWidget = CircleAvatar(
                      radius: 16,
                      backgroundColor: HexColor.fromHex(color),
                      child: Text("r/"),
                    );
                  }
                  return InkWell(
                    onTap: () => Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => FeedView(
                          feed: Feed.subreddit(sub.displayName),
                          initialSort: Sort.best(),
                        ),
                      ),
                    ),
                    child: Row(
                      spacing: 8.0,
                      children: [
                        iconWidget,
                        Text(sub.displayNamePrefixed,
                            style: Theme.of(context).textTheme.bodyMedium)
                      ],
                    ),
                  );
                },
              ),
            ],
          ),
          Divider(),
          InkWell(
              onTap: () => showLicensePage(context: context),
              child: Row(spacing: 8.0, children: [
                Icon(Icons.info_outline),
                Text("Licenses", style: Theme.of(context).textTheme.bodyMedium)
              ]))
        ],
      ),
    );
  }
}
