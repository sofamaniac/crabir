// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// AutoRouterGenerator
// **************************************************************************

// ignore_for_file: type=lint
// coverage:ignore-file

part of 'routes.dart';

/// generated route for
/// [CommentsSettingsView]
class CommentsSettingsRoute extends PageRouteInfo<void> {
  const CommentsSettingsRoute({List<PageRouteInfo>? children})
    : super(CommentsSettingsRoute.name, initialChildren: children);

  static const String name = 'CommentsSettingsRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      return const CommentsSettingsView();
    },
  );
}

/// generated route for
/// [FeedView]
class FeedRoute extends PageRouteInfo<FeedRouteArgs> {
  FeedRoute({
    Key? key,
    required Feed feed,
    FeedSort? initialSort,
    List<PageRouteInfo>? children,
  }) : super(
         FeedRoute.name,
         args: FeedRouteArgs(key: key, feed: feed, initialSort: initialSort),
         initialChildren: children,
       );

  static const String name = 'FeedRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<FeedRouteArgs>();
      return FeedView(
        key: args.key,
        feed: args.feed,
        initialSort: args.initialSort,
      );
    },
  );
}

class FeedRouteArgs {
  const FeedRouteArgs({this.key, required this.feed, this.initialSort});

  final Key? key;

  final Feed feed;

  final FeedSort? initialSort;

  @override
  String toString() {
    return 'FeedRouteArgs{key: $key, feed: $feed, initialSort: $initialSort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! FeedRouteArgs) return false;
    return key == other.key &&
        feed == other.feed &&
        initialSort == other.initialSort;
  }

  @override
  int get hashCode => key.hashCode ^ feed.hashCode ^ initialSort.hashCode;
}

/// generated route for
/// [FullscreenImageView]
class FullscreenImageRoute extends PageRouteInfo<FullscreenImageRouteArgs> {
  FullscreenImageRoute({
    Key? key,
    required String imageUrl,
    String? title,
    List<PageRouteInfo>? children,
  }) : super(
         FullscreenImageRoute.name,
         args: FullscreenImageRouteArgs(
           key: key,
           imageUrl: imageUrl,
           title: title,
         ),
         initialChildren: children,
       );

  static const String name = 'FullscreenImageRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<FullscreenImageRouteArgs>();
      return FullscreenImageView(
        key: args.key,
        imageUrl: args.imageUrl,
        title: args.title,
      );
    },
  );
}

class FullscreenImageRouteArgs {
  const FullscreenImageRouteArgs({
    this.key,
    required this.imageUrl,
    this.title,
  });

  final Key? key;

  final String imageUrl;

  final String? title;

  @override
  String toString() {
    return 'FullscreenImageRouteArgs{key: $key, imageUrl: $imageUrl, title: $title}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! FullscreenImageRouteArgs) return false;
    return key == other.key &&
        imageUrl == other.imageUrl &&
        title == other.title;
  }

  @override
  int get hashCode => key.hashCode ^ imageUrl.hashCode ^ title.hashCode;
}

/// generated route for
/// [InboxView]
class InboxRoute extends PageRouteInfo<void> {
  const InboxRoute({List<PageRouteInfo>? children})
    : super(InboxRoute.name, initialChildren: children);

  static const String name = 'InboxRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      return const InboxView();
    },
  );
}

/// generated route for
/// [MainScreenView]
class MainScreenRoute extends PageRouteInfo<void> {
  const MainScreenRoute({List<PageRouteInfo>? children})
    : super(MainScreenRoute.name, initialChildren: children);

  static const String name = 'MainScreenRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      return const MainScreenView();
    },
  );
}

/// generated route for
/// [MultiView]
class MultiRoute extends PageRouteInfo<MultiRouteArgs> {
  MultiRoute({
    Key? key,
    required Multi multi,
    required FeedSort initialSort,
    List<PageRouteInfo>? children,
  }) : super(
         MultiRoute.name,
         args: MultiRouteArgs(key: key, multi: multi, initialSort: initialSort),
         initialChildren: children,
       );

  static const String name = 'MultiRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<MultiRouteArgs>();
      return MultiView(
        key: args.key,
        multi: args.multi,
        initialSort: args.initialSort,
      );
    },
  );
}

class MultiRouteArgs {
  const MultiRouteArgs({
    this.key,
    required this.multi,
    required this.initialSort,
  });

  final Key? key;

  final Multi multi;

  final FeedSort initialSort;

  @override
  String toString() {
    return 'MultiRouteArgs{key: $key, multi: $multi, initialSort: $initialSort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! MultiRouteArgs) return false;
    return key == other.key &&
        multi == other.multi &&
        initialSort == other.initialSort;
  }

  @override
  int get hashCode => key.hashCode ^ multi.hashCode ^ initialSort.hashCode;
}

/// generated route for
/// [SearchPage]
class SearchPageRoute extends PageRouteInfo<void> {
  const SearchPageRoute({List<PageRouteInfo>? children})
    : super(SearchPageRoute.name, initialChildren: children);

  static const String name = 'SearchPageRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      return const SearchPage();
    },
  );
}

/// generated route for
/// [SearchPostsView]
class SearchPostsRoute extends PageRouteInfo<SearchPostsRouteArgs> {
  SearchPostsRoute({
    Key? key,
    String query = "",
    Flair? flair,
    String? subreddit,
    List<PageRouteInfo>? children,
  }) : super(
         SearchPostsRoute.name,
         args: SearchPostsRouteArgs(
           key: key,
           query: query,
           flair: flair,
           subreddit: subreddit,
         ),
         initialChildren: children,
       );

  static const String name = 'SearchPostsRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<SearchPostsRouteArgs>(
        orElse: () => const SearchPostsRouteArgs(),
      );
      return SearchPostsView(
        key: args.key,
        query: args.query,
        flair: args.flair,
        subreddit: args.subreddit,
      );
    },
  );
}

class SearchPostsRouteArgs {
  const SearchPostsRouteArgs({
    this.key,
    this.query = "",
    this.flair,
    this.subreddit,
  });

  final Key? key;

  final String query;

  final Flair? flair;

  final String? subreddit;

  @override
  String toString() {
    return 'SearchPostsRouteArgs{key: $key, query: $query, flair: $flair, subreddit: $subreddit}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! SearchPostsRouteArgs) return false;
    return key == other.key &&
        query == other.query &&
        flair == other.flair &&
        subreddit == other.subreddit;
  }

  @override
  int get hashCode =>
      key.hashCode ^ query.hashCode ^ flair.hashCode ^ subreddit.hashCode;
}

/// generated route for
/// [SearchSubredditsView]
class SearchSubredditsRoute extends PageRouteInfo<void> {
  const SearchSubredditsRoute({List<PageRouteInfo>? children})
    : super(SearchSubredditsRoute.name, initialChildren: children);

  static const String name = 'SearchSubredditsRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      return const SearchSubredditsView();
    },
  );
}

/// generated route for
/// [SettingsView]
class SettingsRoute extends PageRouteInfo<void> {
  const SettingsRoute({List<PageRouteInfo>? children})
    : super(SettingsRoute.name, initialChildren: children);

  static const String name = 'SettingsRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      return const SettingsView();
    },
  );
}

/// generated route for
/// [SubscriptionsOrFeedView]
class SubscriptionsOrFeedRoute extends PageRouteInfo<void> {
  const SubscriptionsOrFeedRoute({List<PageRouteInfo>? children})
    : super(SubscriptionsOrFeedRoute.name, initialChildren: children);

  static const String name = 'SubscriptionsOrFeedRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      return const SubscriptionsOrFeedView();
    },
  );
}

/// generated route for
/// [SubscriptionsTab]
class SubscriptionsTabRoute extends PageRouteInfo<void> {
  const SubscriptionsTabRoute({List<PageRouteInfo>? children})
    : super(SubscriptionsTabRoute.name, initialChildren: children);

  static const String name = 'SubscriptionsTabRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      return const SubscriptionsTab();
    },
  );
}

/// generated route for
/// [Thread]
class ThreadRoute extends PageRouteInfo<ThreadRouteArgs> {
  ThreadRoute({
    Key? key,
    required String permalink,
    Post? post,
    List<PageRouteInfo>? children,
  }) : super(
         ThreadRoute.name,
         args: ThreadRouteArgs(key: key, permalink: permalink, post: post),
         initialChildren: children,
       );

  static const String name = 'ThreadRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<ThreadRouteArgs>();
      return Thread(key: args.key, permalink: args.permalink, post: args.post);
    },
  );
}

class ThreadRouteArgs {
  const ThreadRouteArgs({this.key, required this.permalink, this.post});

  final Key? key;

  final String permalink;

  final Post? post;

  @override
  String toString() {
    return 'ThreadRouteArgs{key: $key, permalink: $permalink, post: $post}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! ThreadRouteArgs) return false;
    return key == other.key &&
        permalink == other.permalink &&
        post == other.post;
  }

  @override
  int get hashCode => key.hashCode ^ permalink.hashCode ^ post.hashCode;
}

/// generated route for
/// [UserAboutView]
class UserAboutRoute extends PageRouteInfo<UserAboutRouteArgs> {
  UserAboutRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserAboutRoute.name,
         args: UserAboutRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserAboutRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserAboutRouteArgs>();
      return UserAboutView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserAboutRouteArgs {
  const UserAboutRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserAboutRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserAboutRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserBlockedView]
class UserBlockedRoute extends PageRouteInfo<UserBlockedRouteArgs> {
  UserBlockedRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserBlockedRoute.name,
         args: UserBlockedRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserBlockedRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserBlockedRouteArgs>();
      return UserBlockedView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserBlockedRouteArgs {
  const UserBlockedRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserBlockedRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserBlockedRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserCommentsView]
class UserCommentsRoute extends PageRouteInfo<UserCommentsRouteArgs> {
  UserCommentsRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserCommentsRoute.name,
         args: UserCommentsRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserCommentsRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserCommentsRouteArgs>();
      return UserCommentsView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserCommentsRouteArgs {
  const UserCommentsRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserCommentsRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserCommentsRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserDownvotedView]
class UserDownvotedRoute extends PageRouteInfo<UserDownvotedRouteArgs> {
  UserDownvotedRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserDownvotedRoute.name,
         args: UserDownvotedRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserDownvotedRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserDownvotedRouteArgs>();
      return UserDownvotedView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserDownvotedRouteArgs {
  const UserDownvotedRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserDownvotedRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserDownvotedRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserFriendsView]
class UserFriendsRoute extends PageRouteInfo<UserFriendsRouteArgs> {
  UserFriendsRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserFriendsRoute.name,
         args: UserFriendsRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserFriendsRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserFriendsRouteArgs>();
      return UserFriendsView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserFriendsRouteArgs {
  const UserFriendsRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserFriendsRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserFriendsRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserGildedView]
class UserGildedRoute extends PageRouteInfo<UserGildedRouteArgs> {
  UserGildedRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserGildedRoute.name,
         args: UserGildedRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserGildedRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserGildedRouteArgs>();
      return UserGildedView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserGildedRouteArgs {
  const UserGildedRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserGildedRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserGildedRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserHiddenView]
class UserHiddenRoute extends PageRouteInfo<UserHiddenRouteArgs> {
  UserHiddenRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserHiddenRoute.name,
         args: UserHiddenRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserHiddenRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserHiddenRouteArgs>();
      return UserHiddenView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserHiddenRouteArgs {
  const UserHiddenRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserHiddenRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserHiddenRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserOverviewView]
class UserOverviewRoute extends PageRouteInfo<UserOverviewRouteArgs> {
  UserOverviewRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserOverviewRoute.name,
         args: UserOverviewRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserOverviewRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserOverviewRouteArgs>();
      return UserOverviewView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserOverviewRouteArgs {
  const UserOverviewRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserOverviewRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserOverviewRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserPostsView]
class UserPostsRoute extends PageRouteInfo<UserPostsRouteArgs> {
  UserPostsRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserPostsRoute.name,
         args: UserPostsRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserPostsRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserPostsRouteArgs>();
      return UserPostsView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserPostsRouteArgs {
  const UserPostsRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserPostsRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserPostsRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserSavedView]
class UserSavedRoute extends PageRouteInfo<UserSavedRouteArgs> {
  UserSavedRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserSavedRoute.name,
         args: UserSavedRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserSavedRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserSavedRouteArgs>();
      return UserSavedView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserSavedRouteArgs {
  const UserSavedRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserSavedRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserSavedRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserUpvotedView]
class UserUpvotedRoute extends PageRouteInfo<UserUpvotedRouteArgs> {
  UserUpvotedRoute({
    Key? key,
    required String username,
    UserStreamSort sort = const UserStreamSort.new_(),
    List<PageRouteInfo>? children,
  }) : super(
         UserUpvotedRoute.name,
         args: UserUpvotedRouteArgs(key: key, username: username, sort: sort),
         initialChildren: children,
       );

  static const String name = 'UserUpvotedRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserUpvotedRouteArgs>();
      return UserUpvotedView(
        key: args.key,
        username: args.username,
        sort: args.sort,
      );
    },
  );
}

class UserUpvotedRouteArgs {
  const UserUpvotedRouteArgs({
    this.key,
    required this.username,
    this.sort = const UserStreamSort.new_(),
  });

  final Key? key;

  final String username;

  final UserStreamSort sort;

  @override
  String toString() {
    return 'UserUpvotedRouteArgs{key: $key, username: $username, sort: $sort}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserUpvotedRouteArgs) return false;
    return key == other.key && username == other.username && sort == other.sort;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode ^ sort.hashCode;
}

/// generated route for
/// [UserView]
class UserRoute extends PageRouteInfo<UserRouteArgs> {
  UserRoute({Key? key, String? username, List<PageRouteInfo>? children})
    : super(
        UserRoute.name,
        args: UserRouteArgs(key: key, username: username),
        initialChildren: children,
      );

  static const String name = 'UserRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      final args = data.argsAs<UserRouteArgs>(
        orElse: () => const UserRouteArgs(),
      );
      return UserView(key: args.key, username: args.username);
    },
  );
}

class UserRouteArgs {
  const UserRouteArgs({this.key, this.username});

  final Key? key;

  final String? username;

  @override
  String toString() {
    return 'UserRouteArgs{key: $key, username: $username}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! UserRouteArgs) return false;
    return key == other.key && username == other.username;
  }

  @override
  int get hashCode => key.hashCode ^ username.hashCode;
}
