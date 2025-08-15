import 'package:auto_route/auto_route.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/flair.dart';
import 'package:crabir/stream/bloc/stream_bloc.dart';
import 'package:crabir/stream/things_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

@RoutePage()
class SearchView extends StatelessWidget {
  final Flair flair;
  final String subreddit;
  const SearchView({super.key, required this.subreddit, required this.flair});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ThingsScaffold(
        stream: RedditAPI.client().searchPost(
          subreddit: subreddit,
          flair: flair,
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
}
