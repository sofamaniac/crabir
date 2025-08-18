import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/feed/sort_menu.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/stream/bloc/stream_bloc.dart';
import 'package:crabir/stream/things_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

@RoutePage()
class MultiView extends StatelessWidget {
  const MultiView({super.key, required this.multi, required this.initialSort});
  final Multi multi;
  final FeedSort initialSort;

  @override
  Widget build(BuildContext context) {
    return MultiViewBody(
      multi: multi,
      initialSort: initialSort,
    );
  }
}

class MultiViewBody extends StatefulWidget {
  final Multi multi;
  final FeedSort initialSort;

  const MultiViewBody(
      {super.key, required this.multi, required this.initialSort});

  @override
  State<MultiViewBody> createState() => _MultiViewBodyState();
}

class _MultiViewBodyState extends State<MultiViewBody>
    with AutomaticKeepAliveClientMixin {
  FeedSort sort = FeedSort.best();

  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    super.build(context);

    return BlocBuilder<AccountsBloc, AccountState>(
      builder: (context, account) {
        return switch (account.status) {
          Uninit() => Center(child: LoadingIndicator()),
          Failure(:final message) =>
            Center(child: Text("Failure in Account Manager: $message")),
          Loaded() => NestedScrollView(
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
                      IconButton(onPressed: () {}, icon: Icon(Icons.search)),
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
                key: ValueKey(sort.toString()),
                stream: RedditAPI.client().multiPosts(
                  multi: widget.multi,
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
            ),
          _ => Center(child: LoadingIndicator())
        };
      },
    );
  }
}
