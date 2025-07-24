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

  List<Widget> baseFeeds(BuildContext context) {
    return [
      ListTile(
        title: Text("Home"),
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => FeedView(
              feed: Feed.home(),
              initialSort: FeedSort.best(),
            ),
          ),
        ),
      ),
      ListTile(
        title: Text("Popular"),
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => FeedView(
              feed: Feed.popular(),
              initialSort: FeedSort.best(),
            ),
          ),
        ),
      ),
      ListTile(
        title: Text("All"),
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => FeedView(
              feed: Feed.all(),
              initialSort: FeedSort.best(),
            ),
          ),
        ),
      ),
    ];
  }

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
                ...baseFeeds(context),
                Divider(),
                ...userOptions.map((option) => ListTile(title: Text(option))),
                Divider(),
                ...account.multis.map((multi) => _multiView(context, multi)),
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

void _navigate(context, destination) {
  // close drawer
  Navigator.pop(context);

  final tabsRouter = AutoTabsRouter.of(context);
  // Tabs are lazily loaded, so if it was never visited the tab router does not exists
  tabsRouter.setActiveIndex(2);
  final subscriptionsTabRouter = tabsRouter.stackRouterOfIndex(2);
  subscriptionsTabRouter?.replaceAll([destination, SubscriptionsOrFeedRoute()]);
}

Widget _subredditView(BuildContext context, subreddit.Subreddit sub) {
  return ListTile(
    onTap: () {
      final destination = FeedRoute(
        feed: Feed.subreddit(sub.other.displayName),
        initialSort: FeedSort.best(),
      );
      _navigate(context, destination);
    },
    leading: SubredditIcon(icon: sub.icon, radius: 16),
    title: Text(
      sub.other.displayNamePrefixed,
      style: Theme.of(context).textTheme.bodyMedium,
      maxLines: 1,
    ),
  );
}

Widget _multiView(BuildContext context, Multi multi) {
  return ListTile(
    onTap: () {
      final destination = MultiRoute(
        multi: multi,
        initialSort: FeedSort.best(),
      );
      _navigate(context, destination);
    },
    leading: CircleAvatar(
      radius: 16,
      foregroundImage: NetworkImage(multi.iconUrl),
    ),
    title: Text(
      multi.displayName,
      style: Theme.of(context).textTheme.bodyMedium,
      maxLines: 1,
    ),
  );
}
