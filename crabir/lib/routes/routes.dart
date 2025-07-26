import 'package:auto_route/auto_route.dart';
import 'package:crabir/feed/feed.dart';
import 'package:crabir/feed/multi.dart';
import 'package:crabir/main.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/user/model.dart';
import 'package:crabir/subscriptions_tab.dart';
import 'package:crabir/thread/widgets/thread.dart';
import 'package:crabir/user/user.dart';
import 'package:flutter/material.dart';
import 'package:swipeable_page_route/swipeable_page_route.dart';

part 'routes.gr.dart';

@AutoRouterConfig(replaceInRouteName: 'View|Page,Route')
class AppRouter extends RootStackRouter {
  @override
  List<AutoRoute> get routes => [
        AutoRoute(
          path: "/",
          page: MainScreenRoute.page,
          children: [
            NamedRouteDef(
              name: "HomeFeedRoute",
              path: "home",
              builder: (context, data) => FeedView(
                feed: Feed.home(),
                initialSort: FeedSort.best(),
              ),
            ),
            RedirectRoute(path: "", redirectTo: "home"),
            //AutoRoute(page: CurrentUserRoute.page),
            currentUserRoute,
            AutoRoute(
              page: SubscriptionsOrFeedRoute.page,
              children: [
                AutoRoute(page: FeedRoute.page),
                AutoRoute(page: MultiRoute.page),
                AutoRoute(page: SubscriptionsTabRoute.page),
              ],
            ),
            AutoRoute(page: SearchRoute.page),
            AutoRoute(page: InboxRoute.page),
          ],
        ),
        threadRoute,
        AutoRoute(page: FullscreenImageRoute.page)
      ];
}

/// Route with swipe to go back
final threadRoute = CustomRoute(
  page: ThreadRoute.page,
  customRouteBuilder:
      <T>(BuildContext context, Widget child, AutoRoutePage<T> page) {
    return SwipeablePageRoute(
      settings: page,
      builder: (BuildContext context) {
        return child;
      },
    );
  },
);
