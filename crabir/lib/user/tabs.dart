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
  friends,
  blocked;

  String label() {
    return switch (this) {
      UserTabs.overview => "Overview",
      UserTabs.about => "About",
      UserTabs.posts => "Posts",
      UserTabs.comments => "Comments",
      UserTabs.saved => "Saved",
      UserTabs.upvoted => "Upvoted",
      UserTabs.downvoted => "Downvoted",
      UserTabs.hidden => "Hidden",
      UserTabs.friends => "Friends",
      UserTabs.blocked => "Blocked",
    };
  }

  Widget content(String username) {
    return switch (this) {
      UserTabs.overview => UserOverviewView(username: username),
      UserTabs.about => UserAboutView(username: username),
      UserTabs.posts => UserPostsView(username: username),
      UserTabs.comments => UserCommentsView(username: username),
      UserTabs.saved => UserSavedView(username: username),
      UserTabs.upvoted => UserUpvotedView(username: username),
      UserTabs.downvoted => UserDownvotedView(username: username),
      UserTabs.hidden => UserHiddenView(username: username),
      UserTabs.friends => UserFriendsView(username: username),
      UserTabs.blocked => UserBlockedView(username: username),
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
  // UserTabs.friends,
  // UserTabs.blocked,
];

final publicUserTabs = [
  UserTabs.overview,
  UserTabs.about,
  UserTabs.posts,
  UserTabs.comments,
];

Widget _scaffold(
  BuildContext context,
  reddit_stream.PagingHandler stream, {
  String? key,
  bool showHidden = false,
}) {
  return ThingsScaffold(
    key: PageStorageKey(key),
    stream: stream,
    postView: postView,
    commentView: _commentView,
    showHidden: showHidden,
  );
}

Widget postView(BuildContext context, Post post, bool hide) {
  final layout = LayoutSettings.of(context);
  return RedditPostCard(
    maxLines: layout.previewTextLength,
    showSelfText: layout.previewText,
    post: post,
    onTap: () {
      context.push(post.permalink, extra: post);
    },
    enableThumbnail: LayoutSettings.of(context).showThumbnail,
    respectHidden: hide,
  );
}

Widget _commentView(BuildContext context, Comment comment) {
  return CommentView(
    comment: comment,
    animateBottomBar: false,
  );
}

class UserOverviewView extends StatelessWidget {
  @PathParam()
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
      context,
      RedditAPI.client().userOverview(username: username, sort: sort),
      key: "userOverview",
    );
  }
}

class UserAboutView extends StatelessWidget {
  final UserInfo? about;
  @PathParam()
  final String username;
  const UserAboutView({
    super.key,
    required this.username,
    this.about,
  });

  @override
  Widget build(BuildContext context) {
    if (about != null) {
      return Text("Karma: ${about!.totalKarma}");
    }
    return Text("TODO");
  }
}

class UserPostsView extends StatelessWidget {
  @PathParam()
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
      context,
      RedditAPI.client().userSubmitted(username: username, sort: sort),
    );
  }
}

class UserCommentsView extends StatelessWidget {
  @PathParam()
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
      context,
      RedditAPI.client().userComments(username: username, sort: sort),
      key: "userComments",
    );
  }
}

class UserSavedView extends StatelessWidget {
  @PathParam()
  final String username;
  const UserSavedView({
    super.key,
    required this.username,
  });

  @override
  Widget build(BuildContext context) {
    return _scaffold(
      context,
      RedditAPI.client().userSaved(username: username),
      key: "userSaved",
    );
  }
}

class UserUpvotedView extends StatelessWidget {
  @PathParam()
  final String username;
  const UserUpvotedView({
    super.key,
    required this.username,
  });

  @override
  Widget build(BuildContext context) {
    return _scaffold(
      context,
      RedditAPI.client().userUpvoted(username: username),
      key: "userUps",
    );
  }
}

class UserDownvotedView extends StatelessWidget {
  @PathParam()
  final String username;
  const UserDownvotedView({
    super.key,
    required this.username,
  });

  @override
  Widget build(BuildContext context) {
    return _scaffold(
      context,
      RedditAPI.client().userDownvoted(username: username),
      key: "userDowns",
    );
  }
}

class UserHiddenView extends StatelessWidget {
  @PathParam()
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
      context,
      RedditAPI.client().userHidden(username: username),
      key: "userHidden",
      showHidden: true,
    );
  }
}

class UserFriendsView extends StatelessWidget {
  @PathParam()
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

class UserBlockedView extends StatelessWidget {
  @PathParam()
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
