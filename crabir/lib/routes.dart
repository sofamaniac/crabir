import 'package:crabir/feed/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/thread/widgets/thread.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

final List<AppRoute> ROUTES = [HomeRoute(), SubredditRoute()];

sealed class AppRoute {
  final String path;
  Widget builder(BuildContext context, GoRouterState state);
  const AppRoute({required this.path});
}

class HomeRoute extends AppRoute {
  const HomeRoute() : super(path: "/home");
  @override
  Widget builder(BuildContext context, GoRouterState state) {
    return FeedView(
      feed: Feed.home(),
      initialSort: FeedSort.best(),
    );
  }
}

class SubredditRoute extends AppRoute {
  const SubredditRoute() : super(path: "/r/:subredditName");
  @override
  Widget builder(BuildContext context, GoRouterState state) {
    return FeedView(
      feed: Feed.subreddit(state.pathParameters["subredditName"]!),
      initialSort: FeedSort.best(),
    );
  }
}

class PostRoute extends AppRoute {
  const PostRoute() : super(path: "/r/:subreddit/comments/:name/:title");
  @override
  Widget builder(BuildContext context, GoRouterState state) {
    final subreddit = state.pathParameters["subreddit"]!;
    final postName = state.pathParameters["name"]!;
    final title = state.pathParameters["title"]!;
    final permalink = "/r/$subreddit/comments/$postName/$title/";
    return Thread(permalink: permalink);
  }
}
