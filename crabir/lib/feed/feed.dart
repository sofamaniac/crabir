import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/stream/bloc/stream_bloc.dart';
import 'package:crabir/stream/things_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

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
  late FeedSort sort;

  @override
  bool get wantKeepAlive => true;

  void _initializeSort() {
    // TODO: read from user option for preferred sort.
    sort = widget.initialSort ?? FeedSort.best();
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);

    _initializeSort();

    return BlocBuilder<AccountsBloc, AccountState>(
      builder: (context, account) {
        if (account.status == AccountStatus.uninit) {
          return Container();
        } else if (account.status != AccountStatus.failure) {
          return NestedScrollView(
            headerSliverBuilder: (context, __) {
              return [
                SliverAppBar(
                  floating: true,
                  title: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text("Feed title"),
                      SortDisplay(sort: sort),
                    ],
                  ),
                  actions: [
                    IconButton(
                        onPressed: () {
                          //TODO:
                        },
                        icon: Icon(Icons.search)),
                    SortMenu(
                      onSelect: (sort) => setState(
                        () {
                          this.sort = sort;
                        },
                      ),
                    ),
                  ],
                ),
              ];
            },
            floatHeaderSlivers: true,
            body: ThingsScaffold(
              // Forces rebuild when sort changes
              key: ValueKey(sort),
              stream: RedditAPI.client().feedStream(
                feed: widget.feed,
                sort: sort,
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
          );
        }
        return Center(child: Text("Failure in account manager"));
      },
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
