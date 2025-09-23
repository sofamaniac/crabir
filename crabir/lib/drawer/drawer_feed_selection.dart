part of 'drawer.dart';

class DrawerFeedSelection extends StatefulWidget {
  const DrawerFeedSelection({super.key});

  @override
  State<StatefulWidget> createState() => DrawerFeedSelectionState();
}

class DrawerFeedSelectionState extends State<DrawerFeedSelection> {
  final List<String> userOptions = ["Profile", "Inbox", "Moderation"];
  final log = Logger("DrawerFeedSelection");
  UserAccount? account;

  DrawerFeedSelectionState();

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<AccountsBloc>();
    final account = bloc.state;

    if (account.status case Uninit()) {
      bloc.add(Initialize());
    } else if (account.status case Unloaded()) {
      bloc.add(LoadSubscriptions());
    } else if (account.status case Loaded()) {
      return Flexible(
        fit: FlexFit.loose,
        child: ListView(
          children: [
            ...baseFeeds(context, closeDrawer: true),
            Divider(),
            ListTile(
              leading: Icon(Icons.settings),
              title: Text("Settings"),
              onTap: () {
                context.router.navigate(SettingsRoute());
              },
              trailing: IconButton(
                onPressed: () {
                  final bloc = context.read<ThemeBloc>();
                  final mode = bloc.state.mode;
                  final brightness = switch (mode) {
                    ThemeMode.dark => Brightness.dark,
                    ThemeMode.light => Brightness.light,
                    ThemeMode.system =>
                      MediaQuery.of(context).platformBrightness,
                  };
                  final newMode = switch (brightness) {
                    Brightness.light => ThemeMode.dark,
                    Brightness.dark => ThemeMode.light,
                  };
                  bloc.add(SetMode(mode: newMode));
                },
                icon: Icon(Icons.light),
              ),
            ),

            //...userOptions.map((option) => ListTile(title: Text(option))),
            Divider(),
            ...account.multis.map(
              (multi) => MultiRedditTile(
                multi,
                closeDrawer: true,
              ),
            ),
            Divider(),
            ...account.subscriptions.map(
              (sub) {
                return SubredditTile(
                  sub,
                );
              },
            ),
          ],
        ),
      );
    }
    return Container();
  }
}
