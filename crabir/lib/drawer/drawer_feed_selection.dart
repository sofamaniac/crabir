part of 'drawer.dart';

class DrawerFeedSelection extends StatefulWidget {
  const DrawerFeedSelection({super.key});

  @override
  State<StatefulWidget> createState() => DrawerFeedSelectionState();
}

class DrawerFeedSelectionState extends State<DrawerFeedSelection> {
  final List<String> feeds = ["Home", "Popular", "All", "Saved", "History"];
  final List<String> userOptions = ["Profile", "Inbox", "Moderation"];
  final log = Logger("DrawerFeedSelection");
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
    final state = context.read<AccountsBloc>();
    return BlocBuilder<AccountsBloc, AccountState>(
      builder: (context, account) {
        if (account.status == AccountStatus.uninit) {
          state.add(Initialize());
        } else if (account.status == AccountStatus.unloaded) {
          state.add(LoadSubscriptions());
        } else if (account.status == AccountStatus.loaded) {
          return Flexible(
            fit: FlexFit.loose,
            child: ListView(
              children: [
                ...feeds.map((feed) => ListTile(title: Text(feed))),
                Divider(),
                ...userOptions.map((option) => ListTile(title: Text(option))),
                Divider(),
                ...account.subscriptions.map(
                  (sub) => _subredditView(context, sub),
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
      },
    );
  }
}

Widget _subredditView(BuildContext context, subreddit.Subreddit sub) {
  return ListTile(
    onTap: () => Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => FeedView(
          feed: Feed.subreddit(sub.other.displayName),
          initialSort: FeedSort.best(),
        ),
      ),
    ),
    leading: SubredditIcon(icon: sub.icon, radius: 16),
    title: Text(
      sub.other.displayNamePrefixed,
      style: Theme.of(context).textTheme.bodyMedium,
      maxLines: 1,
    ),
  );
}
