// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// AutoRouterGenerator
// **************************************************************************

// ignore_for_file: type=lint
// coverage:ignore-file

part of 'routes.dart';

/// generated route for
/// [CurrentUserView]
class CurrentUserRoute extends PageRouteInfo<void> {
  const CurrentUserRoute({List<PageRouteInfo>? children})
    : super(CurrentUserRoute.name, initialChildren: children);

  static const String name = 'CurrentUserRoute';

  static PageInfo page = PageInfo(
    name,
    builder: (data) {
      return const CurrentUserView();
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
