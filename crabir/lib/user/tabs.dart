part of 'user.dart';

enum UserTabs {
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
      UserTabs.overview => "Overview",
      UserTabs.about => "About",
      UserTabs.posts => "Posts",
      UserTabs.comments => "Comments",
      UserTabs.saved => "Saved",
      UserTabs.upvoted => "Upvoted",
      UserTabs.downvoted => "Downvoted",
      UserTabs.hidden => "Hidden",
      UserTabs.gilded => "Gilded",
      UserTabs.friends => "Friends",
      UserTabs.blocked => "Blocked",
    };
  }

  PageRouteInfo route(String username) {
    return switch (this) {
      UserTabs.overview => UserOverviewRoute(username: username),
      UserTabs.about => UserAboutRoute(username: username),
      UserTabs.posts => UserPostsRoute(username: username),
      UserTabs.comments => UserCommentsRoute(username: username),
      UserTabs.saved => UserSavedRoute(username: username),
      UserTabs.upvoted => UserUpvotedRoute(username: username),
      UserTabs.downvoted => UserDownvotedRoute(username: username),
      UserTabs.hidden => UserHiddenRoute(username: username),
      UserTabs.gilded => UserGildedRoute(username: username),
      UserTabs.friends => UserFriendsRoute(username: username),
      UserTabs.blocked => UserBlockedRoute(username: username),
    };
  }

  PageInfo page() {
    return switch (this) {
      UserTabs.overview => UserOverviewRoute.page,
      UserTabs.about => UserAboutRoute.page,
      UserTabs.posts => UserPostsRoute.page,
      UserTabs.comments => UserCommentsRoute.page,
      UserTabs.saved => UserSavedRoute.page,
      UserTabs.upvoted => UserUpvotedRoute.page,
      UserTabs.downvoted => UserDownvotedRoute.page,
      UserTabs.hidden => UserHiddenRoute.page,
      UserTabs.gilded => UserGildedRoute.page,
      UserTabs.friends => UserFriendsRoute.page,
      UserTabs.blocked => UserBlockedRoute.page,
    };
  }
}

final allUserTabs = [
  UserTabs.overview,
  UserTabs.about,
  UserTabs.posts,
  UserTabs.comments,
  UserTabs.saved,
  UserTabs.upvoted,
  UserTabs.downvoted,
  UserTabs.hidden,
  UserTabs.gilded,
  UserTabs.friends,
  UserTabs.blocked,
];

final publicUserTabs = [
  UserTabs.overview,
  UserTabs.about,
  UserTabs.posts,
  UserTabs.comments,
  UserTabs.gilded,
];

final currentUserRoute = AutoRoute(
  page: UserRoute.page,
  children: allUserTabs
      .map(
        (tab) => AutoRoute(
          page: tab.page(),
        ),
      )
      .toList(),
);

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
    onTap: () => context.router.navigate(
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
  const UserAboutView({
    super.key,
    required this.username,
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
      RedditAPI.client().userSubmitted(username: username, sort: sort),
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
    return _scaffold(
      RedditAPI.client().userComments(username: username, sort: sort),
    );
  }
}

@RoutePage()
class UserSavedView extends StatelessWidget {
  final String username;
  const UserSavedView({
    super.key,
    required this.username,
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
  const UserUpvotedView({
    super.key,
    required this.username,
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
  const UserDownvotedView({
    super.key,
    required this.username,
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
  }
}
