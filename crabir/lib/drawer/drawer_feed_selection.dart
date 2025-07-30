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

    if (account.status == AccountStatus.uninit) {
      bloc.add(Initialize());
    } else if (account.status == AccountStatus.unloaded) {
      bloc.add(LoadSubscriptions());
    } else if (account.status == AccountStatus.loaded) {
      return Flexible(
        fit: FlexFit.loose,
        child: ListView(
          children: [
            ...baseFeeds.map(
              (feed) => feed.toTile(context),
            ),
            Divider(),
            ...userOptions.map((option) => ListTile(title: Text(option))),
            Divider(),
            ...account.multis.map((multi) => MultiRedditTile(
                  multi: multi,
                  closeDrawer: true,
                )),
            Divider(),
            ...account.subscriptions.map(
              (sub) => SubredditTile(
                sub: sub,
                closeDrawer: true,
              ),
            ),
            Divider(),
            ListTile(
              onTap: () => showLicensePage(context: context),
              title: Text(
                "Licenses",
                style: Theme.of(context).textTheme.bodyMedium,
              ),
              leading: Icon(Icons.info_outline),
            ),
          ],
        ),
      );
    }
    return Container();
  }
}
