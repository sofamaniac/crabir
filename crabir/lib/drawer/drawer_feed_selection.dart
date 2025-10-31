part of 'drawer.dart';

class DrawerFeedSelection extends StatefulWidget {
  const DrawerFeedSelection({super.key});

  @override
  State<StatefulWidget> createState() => DrawerFeedSelectionState();
}

class DrawerFeedSelectionState extends State<DrawerFeedSelection>
    with AutomaticKeepAliveClientMixin {
  final log = Logger("DrawerFeedSelection");
  final controller = ScrollController();
  UserAccount? account;

  DrawerFeedSelectionState();

  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    super.build(context);
    final bloc = context.watch<AccountsBloc>();
    final account = bloc.state;
    final theme = CrabirTheme.of(context);

    switch (account.status) {
      case Uninit():
        bloc.add(Initialize());
      case Unloaded():
        bloc.add(LoadSubscriptions());
      case Loaded():
        return Expanded(
          child: ListView(
            key: PageStorageKey("SubscriptionDrawerState"),
            controller: controller,
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
                  Scaffold.of(context).closeDrawer();
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
