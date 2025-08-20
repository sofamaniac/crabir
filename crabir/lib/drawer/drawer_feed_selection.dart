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
            ...userOptions.map((option) => ListTile(title: Text(option))),
            Divider(),
            ...account.multis.map(
              (multi) => MultiRedditTile(
                multi,
                closeDrawer: true,
              ),
            ),
            Divider(),
            ...account.subscriptions.map(
              (sub) => SubredditTile(
                sub,
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
