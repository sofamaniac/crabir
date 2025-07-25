part of 'user.dart';

enum _Tabs {
  overview,
  about,
  posts,
  comments,
  saved,
  upvoted,
  downvoted,
  hidden,
  gilded,
  friends,
  blocked;

  String name() {
    return switch (this) {
      _Tabs.overview => "Overview",
      _Tabs.about => "About",
      _Tabs.posts => "Posts",
      _Tabs.comments => "Comments",
      _Tabs.saved => "Saved",
      _Tabs.upvoted => "Upvoted",
      _Tabs.downvoted => "Downvoted",
      _Tabs.hidden => "Hidden",
      _Tabs.gilded => "Gilded",
      _Tabs.friends => "Friends",
      _Tabs.blocked => "Blocked",
    };
  }

  PageRouteInfo route(String username) {
    return switch (this) {
      _Tabs.overview => UserOverviewRoute(username: username),
      _Tabs.about => UserAboutRoute(username: username),
      _Tabs.posts => UserPostsRoute(username: username),
      _Tabs.comments => UserCommentsRoute(username: username),
      _Tabs.saved => UserSavedRoute(username: username),
      _Tabs.upvoted => UserUpvotedRoute(username: username),
      _Tabs.downvoted => UserDownvotedRoute(username: username),
      _Tabs.hidden => UserHiddenRoute(username: username),
      _Tabs.gilded => UserGildedRoute(username: username),
      _Tabs.friends => UserFriendsRoute(username: username),
      _Tabs.blocked => UserBlockedRoute(username: username),
    };
  }

  PageInfo page() {
    return switch (this) {
      _Tabs.overview => UserOverviewRoute.page,
      _Tabs.about => UserAboutRoute.page,
      _Tabs.posts => UserPostsRoute.page,
      _Tabs.comments => UserCommentsRoute.page,
      _Tabs.saved => UserSavedRoute.page,
      _Tabs.upvoted => UserUpvotedRoute.page,
      _Tabs.downvoted => UserDownvotedRoute.page,
      _Tabs.hidden => UserHiddenRoute.page,
      _Tabs.gilded => UserGildedRoute.page,
      _Tabs.friends => UserFriendsRoute.page,
      _Tabs.blocked => UserBlockedRoute.page,
    };
  }
}

final allUserTabs = [
  _Tabs.overview,
  _Tabs.about,
  _Tabs.posts,
  _Tabs.comments,
  _Tabs.saved,
  _Tabs.upvoted,
  _Tabs.downvoted,
  _Tabs.hidden,
  _Tabs.gilded,
  _Tabs.friends,
  _Tabs.blocked,
];

Widget _scaffold(reddit_stream.Streamable stream) {
  return ThingsScaffold(
    stream: stream,
    postView: _postView,
    commentView: _commentView,
  );
}

Widget _postView(BuildContext context, Post post) {
  return RedditPostCard(
    post: post,
    onTap: () => context.pushRoute(
      ThreadRoute(
        permalink: post.permalink,
        post: post,
      ),
    ),
  );
}

Widget _commentView(BuildContext context, Comment comment) {
  return CommentView(
    comment: comment,
    animateBottomBar: false,
  );
}

@RoutePage()
class UserOverviewView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserOverviewView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return _scaffold(
      RedditAPI.client().userOverview(username: username, sort: sort),
    );
  }
}

@RoutePage()
class UserAboutView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserAboutView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return Text("TODO");
  }
}

@RoutePage()
class UserPostsView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserPostsView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return _scaffold(
      RedditAPI.client().userOverview(username: username, sort: sort),
    );
  }
}

@RoutePage()
class UserCommentsView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserCommentsView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return Text("TODO");
    return _scaffold(
      RedditAPI.client().userOverview(username: username, sort: sort),
    );
  }
}

final currentUserRoute = AutoRoute(
  page: CurrentUserRoute.page,
  children: allUserTabs
      .map(
        (tab) => AutoRoute(
          page: tab.page(),
        ),
      )
      .toList(),
);

@RoutePage()
class UserSavedView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserSavedView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return _scaffold(
      RedditAPI.client().userSaved(username: username),
    );
  }
}

@RoutePage()
class UserUpvotedView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserUpvotedView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return _scaffold(
      RedditAPI.client().userUpvoted(username: username),
    );
  }
}

@RoutePage()
class UserDownvotedView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserDownvotedView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return _scaffold(
      RedditAPI.client().userDownvoted(username: username),
    );
  }
}

@RoutePage()
class UserHiddenView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserHiddenView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return _scaffold(
      RedditAPI.client().userHidden(username: username),
    );
  }
}

@RoutePage()
class UserGildedView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserGildedView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return Text("TODO");
    return _scaffold(
      RedditAPI.client().userOverview(username: username, sort: sort),
    );
  }
}

@RoutePage()
class UserFriendsView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserFriendsView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return Text("TODO");
    return _scaffold(
      RedditAPI.client().userOverview(username: username, sort: sort),
    );
  }
}

@RoutePage()
class UserBlockedView extends StatelessWidget {
  final String username;
  final UserStreamSort sort;
  const UserBlockedView({
    super.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  @override
  Widget build(BuildContext context) {
    return Text("TODO");
    return _scaffold(
      RedditAPI.client().userOverview(username: username, sort: sort),
    );
  }
}
