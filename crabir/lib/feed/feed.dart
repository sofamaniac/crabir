import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/feed/top_bar.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/stream/bloc/stream_bloc.dart';
import 'package:crabir/stream/things_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_speed_dial/flutter_speed_dial.dart';

@RoutePage()
class FeedView extends StatelessWidget {
  const FeedView({super.key, required this.feed, this.initialSort});
  final Feed feed;
  final FeedSort? initialSort;

  @override
  Widget build(BuildContext context) {
    return FeedViewBody(
      feed: feed,
      initialSort: initialSort,
    );
  }
}

class FeedViewBody extends StatefulWidget {
  final Feed feed;
  final FeedSort? initialSort;

  const FeedViewBody({super.key, required this.feed, this.initialSort});

  @override
  State<FeedViewBody> createState() => _FeedViewBodyState();
}

class _FeedViewBodyState extends State<FeedViewBody>
    with AutomaticKeepAliveClientMixin {
  FeedSort? sort;

  late final ScrollController _scrollController;
  bool _showScrollToTop = false;

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();
    _initializeSort();
    _scrollController = ScrollController();
    _scrollController.addListener(() {
      final shouldShow = _scrollController.offset > 300;
      if (_showScrollToTop != shouldShow) {
        setState(() {
          _showScrollToTop = shouldShow;
        });
      }
    });
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  void _initializeSort() {
    // TODO: read from user option for preferred sort.
    sort = widget.initialSort ?? FeedSort.best();
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);

    final account = context.read<AccountsBloc>().state;

    if (sort == null) {
      return Center(child: CircularProgressIndicator());
    }

    if (account.status == AccountStatus.uninit) {
      return Container();
    } else if (account.status != AccountStatus.failure) {
      return Scaffold(
        floatingActionButton: ActionButton(
          scrollController: _scrollController,
        ),
        body: NestedScrollView(
          controller: _scrollController,
          headerSliverBuilder: (context, __) {
            return [
              FeedTopBar(
                feed: widget.feed,
                sort: sort!,
                setSort: (sort) => setState(() {
                  this.sort = sort;
                }),
              ),
            ];
          },
          floatHeaderSlivers: true,
          body: ThingsScaffold(
            stream: RedditAPI.client().feedStream(
              feed: widget.feed,
              sort: sort!,
            ),
            postView: (context, post) {
              final state = context.read<StreamBloc>();
              return RedditPostCard(
                maxLines: 5,
                post: post,
                onLike: (direction) async {
                  state.add(Vote(direction: direction, name: post.name));
                },
                onSave: (save) async {
                  state.add(Save(saved: save, name: post.name));
                },
                onTap: () => context.pushRoute(
                  ThreadRoute(permalink: post.permalink, post: post),
                ),
              );
            },
          ),
        ),
      );
    }
    return Center(child: Text("Failure in account manager"));
  }
}

class ActionButton extends StatelessWidget {
  final ScrollController scrollController;
  const ActionButton({super.key, required this.scrollController});
  @override
  Widget build(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    return SpeedDial(
      icon: Icons.add,
      activeIcon: Icons.close,
      backgroundColor: theme.primaryColor,
      children: [
        SpeedDialChild(
          backgroundColor: theme.primaryColor,
          child: Icon(Icons.clear_all),
          label: 'Clear read',
          onTap: () {},
        ),
        SpeedDialChild(
          backgroundColor: theme.primaryColor,
          child: Icon(Icons.edit),
          label: 'Create Post',
          onTap: () {},
        ),
        SpeedDialChild(
          backgroundColor: theme.primaryColor,
          child: Icon(Icons.keyboard_arrow_up),
          label: 'Scroll to Top',
          onTap: () {
            scrollController.animateTo(
              0,
              duration: Duration(milliseconds: 300),
              curve: Curves.easeOut,
            );
          },
        ),
      ],
    );
  }
}

const timeOptions = [
  Timeframe.hour,
  Timeframe.day,
  Timeframe.week,
  Timeframe.month,
  Timeframe.year,
  Timeframe.all
];

String timeframeToString(Timeframe timeframe) {
  return switch (timeframe) {
    Timeframe.hour => "Hour",
    Timeframe.day => "Day",
    Timeframe.week => "Week",
    Timeframe.month => "Month",
    Timeframe.year => "Year",
    Timeframe.all => "All",
  };
}
