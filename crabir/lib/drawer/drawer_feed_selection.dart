part of 'drawer.dart';

class DrawerFeedSelection extends StatefulWidget {
  const DrawerFeedSelection({super.key});

  @override
  State<StatefulWidget> createState() => DrawerFeedSelectionState();
}

class DrawerFeedSelectionState extends State<DrawerFeedSelection> {
  final log = Logger("DrawerFeedSelection");
  UserAccount? account;

  DrawerFeedSelectionState();

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<AccountsBloc>();
    final account = bloc.state;
    final theme = CrabirTheme.of(context);

    if (account.status case Uninit()) {
      bloc.add(Initialize());
    } else if (account.status case Unloaded()) {
      bloc.add(LoadSubscriptions());
    } else if (account.status case Loaded()) {
      return Expanded(
        child: ListView(
          children: [
            ...baseFeeds(
              context,
            ),
            Divider(),
            ...options(context),
            ListTile(
              leading: Icon(Icons.settings),
              title: Text("Settings"),
              onTap: () {
                SettingsView().pushNamed(context);
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
            Divider(),
            ...account.multis.map(
              (multi) => MultiRedditTile(
                multi,
                style: Theme.of(context).textTheme.bodyMedium!.copyWith(
                      color: theme.secondaryText,
                      fontWeight: FontWeight.bold,
                    ),
              ),
            ),
            Divider(),
            ...account.subscriptions.map(
              (sub) {
                return SubredditTile(
                  sub,
                  style: Theme.of(context).textTheme.bodyMedium!.copyWith(
                        color: theme.secondaryText,
                        fontWeight: FontWeight.bold,
                      ),
                  onTap: () {
                    Scaffold.of(context).closeDrawer();
                    context.go("/r/${sub.other.displayName}");
                  },
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
