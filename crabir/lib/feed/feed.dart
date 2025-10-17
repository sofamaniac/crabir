import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/common.dart';
import 'package:crabir/feed/top_bar.dart';
import 'package:crabir/html_view.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/rule.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    hide Icon, SubredditIcon;
import 'package:crabir/subreddit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

part 'feed.go_route_ext.dart';

@CrabirRoute()
class FeedView extends StatefulWidget {
  const FeedView({
    super.key,
    this.feed,
    this.initialSort,
    this.subreddit,
  }) : assert(feed != null || subreddit != null);
  final Feed? feed;
  final String? subreddit;

  /// Override the setting-defined preferred sort.
  final FeedSort? initialSort;

  @override
  State<FeedView> createState() => _FeedViewState();
}

class _FeedViewState extends State<FeedView> {
  Subreddit? subredditAbout;
  late Feed feed;

  @override
  void initState() {
    super.initState();
    if (widget.subreddit != null) {
      feed = Feed.subreddit(widget.subreddit!);
    } else {
      feed = widget.feed!;
    }
    _loadAbout();
  }

  Future<void> _loadAbout() async {
    switch (feed) {
      case Feed_Subreddit(field0: final subreddit):
        subredditAbout =
            await RedditAPI.client().subredditAbout(subreddit: subreddit);
        if (!mounted) return;
        setState(() {});
      default:
    }
  }

  @override
  Widget build(BuildContext context) {
    final _ = context.watch<AccountsBloc>().state;
    final bloc = context.read<PostsSettingsCubit>();
    final settings = context.read<PostsSettingsCubit>().state;
    final defaultSort = switch (widget.feed) {
      Feed_Home() => settings.defaultHomeSort,
      _ => settings.defaultSort
    };
    final FeedSort sort;
    if (settings.rememberSortByCommunity) {
      sort = settings.rememberedSorts.containsFeed(feed) ??
          widget.initialSort ??
          defaultSort;
    } else {
      sort = widget.initialSort ?? defaultSort;
    }
    return CommonFeedView(
      key: ValueKey(widget.feed),
      title: (sort) => FeedTitle(feed: feed, sort: sort),
      newStream: (sort) => RedditAPI.client().feedStream(
        feed: feed,
        sort: sort,
      ),
      onSortChanged: (sort) {
        if (settings.rememberSortByCommunity) {
          bloc.updateRememberedSorts(
              settings.rememberedSorts.addFeed(feed, sort));
        }
      },
      initialSort: sort,
      subredditAbout: subredditAbout,
      endDrawer: subredditAbout != null
          ? Drawer(child: RightSide(infos: subredditAbout!))
          : null,
    );
  }
}

class SubredditInfoView extends StatefulWidget {
  final Subreddit infos;

  const SubredditInfoView({super.key, required this.infos});

  @override
  State<SubredditInfoView> createState() => _SubredditInfoViewState();
}

class _SubredditInfoViewState extends State<SubredditInfoView> {
  late bool subscribed;

  @override
  void initState() {
    super.initState();
    subscribed = widget.infos.other.userIsSubscriber;
  }

  @override
  Widget build(BuildContext context) {
    final Widget icon;
    icon = SubredditIcon(
      icon: widget.infos.other.icon,
      radius: 40,
    );
    return Column(
      children: [
        // banner and icon
        Stack(
          clipBehavior: Clip.none,
          children: [
            _banner(),
            Positioned(
              left: 16,
              top: 50,
              child: icon,
            ),
          ],
        ),
        // Space for avatar overlap
        const SizedBox(height: 40 + 8),

        // Username + subtitle
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Expanded(
                    child: Text(
                      widget.infos.other.displayName,
                      style: Theme.of(context).textTheme.titleLarge,
                      overflow: TextOverflow.clip,
                    ),
                  ),
                  JoinButton(
                    infos: widget.infos,
                    onPressed: () => setState(() {}),
                  ),
                  MoreOptionButton(subreddit: widget.infos),
                ],
              ),
              Text(
                // TODO: localize
                "${widget.infos.other.subscribers} members",
                style: Theme.of(context)
                    .textTheme
                    .bodyMedium
                    ?.copyWith(color: Colors.white70),
              ),
              if (widget.infos.publicDescriptionHtml != null)
                StyledHtml(htmlContent: widget.infos.publicDescriptionHtml!),
            ],
          ),
        ),
        Divider(),
      ],
    );
  }

  Widget _banner() {
    if (widget.infos.bannerBackgroundImage != null &&
        widget.infos.bannerBackgroundImage!.isNotEmpty) {
      return Align(
        alignment: Alignment.topCenter,
        child: CachedNetworkImage(
          imageUrl: widget.infos.bannerBackgroundImage!,
          fit: BoxFit.cover,
          height: 100,
          width: double.infinity,
          errorWidget: (_, __, ___) => SizedBox.shrink(),
        ),
      );
    } else {
      return SizedBox(
        height: 100,
        width: double.infinity,
      );
    }
  }
}

class JoinButton extends StatelessWidget {
  final Subreddit infos;

  /// Function to call after subscribing to the subreddit.
  final VoidCallback? onPressed;

  const JoinButton({
    super.key,
    required this.infos,
    required this.onPressed,
  });
  @override
  Widget build(BuildContext context) {
    final subscribed = infos.other.userIsSubscriber;
    return ElevatedButton.icon(
      onPressed: () async {
        if (subscribed) {
          await infos.unsubscribe(client: RedditAPI.client());
        } else {
          await infos.subscribe(client: RedditAPI.client());
        }
        onPressed?.call();
      },
      icon: Icon(subscribed ? Icons.check_circle : Icons.add_circle),
      label: subscribed
          // TODO: localize
          ? Text("Joined")
          : Text("Join"),
      style: ElevatedButton.styleFrom(
        backgroundColor: subscribed ? Colors.green : Colors.transparent,
        foregroundColor: Theme.of(context).textTheme.bodySmall?.color,
        minimumSize: Size.zero,
        tapTargetSize: MaterialTapTargetSize.shrinkWrap,
        padding: EdgeInsetsGeometry.all(8),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(4),
          side: BorderSide(color: subscribed ? Colors.green : Colors.grey),
        ),
      ),
    );
  }
}

class FavButton extends StatelessWidget {
  final Subreddit infos;

  /// Function to call after favoriting the subreddit.
  final VoidCallback? onPressed;

  const FavButton({super.key, required this.infos, required this.onPressed});
  @override
  Widget build(BuildContext context) {
    final favorite = infos.userHasFavorited;
    return ElevatedButton.icon(
      onPressed: () async {
        await infos.favorite(
          favorite: !infos.userHasFavorited,
          client: RedditAPI.client(),
        );
        onPressed?.call();
      },
      icon: Icon(favorite ? Icons.check_circle : Icons.add_circle),
      // TODO: localize
      label: Text("Favorite"),
      style: ElevatedButton.styleFrom(
        backgroundColor: favorite ? Colors.yellow : Colors.transparent,
        foregroundColor: Theme.of(context).textTheme.bodySmall?.color,
        minimumSize: Size.zero,
        tapTargetSize: MaterialTapTargetSize.shrinkWrap,
        padding: EdgeInsetsGeometry.all(8),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(4),
          side: BorderSide(color: favorite ? Colors.yellow : Colors.grey),
        ),
      ),
    );
  }
}

class RulesView extends StatefulWidget {
  final Subreddit subreddit;

  const RulesView({super.key, required this.subreddit});

  @override
  State<StatefulWidget> createState() => _RulesViewState();
}

class _RulesViewState extends State<RulesView> {
  List<Rule> rules = [];

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) async {
      rules = await RedditAPI.client()
          .rules(name: widget.subreddit.other.displayName);
      setState(() {});
    });
  }

  String kindToText(RuleKind kind) {
    return switch (kind) {
      RuleKind.all => "Post and Comment",
      RuleKind.link => "Post",
      RuleKind.comment => "Post",
    };
  }

  Widget ruleView(BuildContext context, Rule rule) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(rule.shortName, style: Theme.of(context).textTheme.titleMedium),
        Text(
          kindToText(rule.kind),
          style: Theme.of(context).textTheme.labelMedium,
        ),
        SizedBox(height: 8),
        StyledHtml(htmlContent: rule.descriptionHtml),
      ],
    );
  }

  Widget rulesView() {
    return ListView.builder(
      itemCount: rules.length,
      itemBuilder: (context, index) => ruleView(context, rules[index]),
    );
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
        title: Text("Rules"),
        content:
            rules.isEmpty ? Center(child: LoadingIndicator()) : rulesView(),
        actions: [
          InkWell(child: Text("Got it"), onTap: () => Navigator.pop(context)),
        ]);
  }
}

class MoreOptionButton extends StatelessWidget {
  final Subreddit subreddit;

  const MoreOptionButton({super.key, required this.subreddit});

  Widget dialog(BuildContext context) {
    return SimpleDialog(
      children: [
        ListTile(leading: Icon(Icons.create), title: Text("Create post")),
        ListTile(leading: Icon(Icons.label), title: Text("Change user flair")),
        ListTile(leading: Icon(Icons.person_outline), title: Text("View mods")),
        ListTile(leading: Icon(Icons.mail), title: Text("Contact mods")),
        ListTile(leading: Icon(Icons.public), title: Text("View wiki")),
        ListTile(
          leading: Icon(Icons.list),
          title: Text("View rules"),
          onTap: () {
            showDialog(
              context: context,
              builder: (context) => RulesView(subreddit: subreddit),
            );
          },
        ),
        ListTile(leading: Icon(Icons.share), title: Text("Share community")),
        ListTile(
            leading: Icon(Icons.format_list_bulleted_add),
            title: Text("Add to custom feed")),
        ListTile(
            leading: Icon(Icons.add_to_home_screen),
            title: Text("Add to home screen")),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return IconButton.outlined(
      onPressed: () {
        showDialog(context: context, builder: dialog);
      },
      icon: const Icon(Icons.more_vert),
    );
  }
}

class RightSide extends StatefulWidget {
  final Subreddit infos;

  const RightSide({super.key, required this.infos});

  @override
  State<RightSide> createState() => _RightSideState();
}

class _RightSideState extends State<RightSide> {
  Widget infoView(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        spacing: 8,
        children: [
          Row(
            spacing: 16,
            children: [
              SubredditIcon(
                icon: widget.infos.other.icon,
                radius: 40,
              ),
              Column(
                children: [
                  Text(
                    widget.infos.other.displayName,
                    style: Theme.of(context).textTheme.titleLarge,
                    overflow: TextOverflow.clip,
                  ),
                  Text(
                    // TODO: localize
                    "${widget.infos.other.subscribers} members",
                    style: Theme.of(context)
                        .textTheme
                        .bodyMedium
                        ?.copyWith(color: Colors.white70),
                  ),
                  Text("Edit flair"),
                ],
              ),
            ],
          ),
          Row(
            spacing: 16,
            children: [
              JoinButton(
                infos: widget.infos,
                onPressed: () => setState(() {}),
              ),
              FavButton(
                infos: widget.infos,
                onPressed: () => setState(() {}),
              ),
              // TODO:
              MoreOptionButton(subreddit: widget.infos),
            ],
          ),
          if (widget.infos.publicDescriptionHtml != null)
            StyledHtml(htmlContent: widget.infos.publicDescriptionHtml!),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: ListView(children: [
        // TODO:
        Text("Search post"),
        Divider(),
        infoView(context),
        Divider(),
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: StyledHtml(htmlContent: widget.infos.descriptionHtml ?? ""),
        ),
      ]),
    );
  }
}
