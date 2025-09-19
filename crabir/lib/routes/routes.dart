import 'package:auto_route/auto_route.dart';
import 'package:crabir/feed/feed.dart';
import 'package:crabir/feed/multi.dart';
import 'package:crabir/gallery/gallery.dart';
import 'package:crabir/main.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/search_posts/widgets/search.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/data/data_settings.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/settings/settings.dart';
import 'package:crabir/settings/theme/theme_editor.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/flair.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/gallery.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/user/model.dart';
import 'package:crabir/subscriptions_tab.dart';
import 'package:crabir/thread/widgets/thread.dart';
import 'package:crabir/user/user.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:swipeable_page_route/swipeable_page_route.dart';
import 'package:crabir/search_subreddits/widgets/search.dart';

part 'routes.gr.dart';

const String homeRouteName = "HomeFeedRoute";
const String homeRoutePath = "home";

@AutoRouterConfig(replaceInRouteName: 'View|Page,Route')
class AppRouter extends RootStackRouter {
  @override
  List<AutoRoute> get routes => [
        AutoRoute(
          initial: true,
          page: MainScreenRoute.page,
          children: [
            NamedRouteDef(
              name: homeRouteName,
              path: homeRoutePath,
              builder: (context, data) => FeedView(
                feed: Feed.home(),
                initialSort: FeedSort.best(),
              ),
              initial: true,
            ),
            AutoRoute(
              page: ProfilePageRoute.page,
              children: [
                currentUserRoute,
                // you could add other user-related routes here too
              ],
            ),
            AutoRoute(
              page: SubscriptionsOrFeedRoute.page,
              children: [
                AutoRoute(page: FeedRoute.page),
                AutoRoute(page: MultiRoute.page),
                AutoRoute(
                  page: SubscriptionsTabRoute.page,
                  initial: true,
                ),
              ],
            ),
            AutoRoute(
              page: SearchPageRoute.page,
              children: [
                AutoRoute(page: SearchSubredditsRoute.page, initial: true),
                AutoRoute(page: SearchPostsRoute.page),
              ],
            ),
            AutoRoute(page: InboxRoute.page),
          ],
        ),
        threadRoute,
        AutoRoute(page: FullscreenImageRoute.page),
        AutoRoute(page: FullScreenGalleryRoute.page),

        // Settings
        AutoRoute(page: SettingsRoute.page),
        AutoRoute(page: CrabirThemeEditorPage.page),
        AutoRoute(page: CommentsSettingsRoute.page),
        AutoRoute(page: PostsSettingsRoute.page),
        AutoRoute(page: DataSettingsRoute.page),
        AutoRoute(page: FiltersSettingsRoute.page),
      ];
}

/// Route with swipe to go back
final threadRoute = CustomRoute(
  path: "/r/:subreddit/comments/:id/:title",
  page: ThreadRoute.page,
  customRouteBuilder: <T>(
    BuildContext context,
    Widget child,
    AutoRoutePage<T> page,
  ) {
    if (context.read<CommentsSettingsCubit>().state.swipeToClose) {
      return SwipeablePageRoute(
        settings: page,
        builder: (BuildContext context) {
          return child;
        },
      );
    } else {
      return PageRouteBuilder<T>(
        fullscreenDialog: page.fullscreenDialog,
        settings: page,
        pageBuilder: (_, __, ___) => child,
      );
    }
  },
);

@RoutePage()
class SubscriptionsOrFeedView extends AutoRouter {
  const SubscriptionsOrFeedView({super.key});
}

@RoutePage(name: "SearchPageRoute")
class SearchPage extends AutoRouter {
  const SearchPage({super.key});
}

@RoutePage()
class InboxView extends AutoRouter {
  const InboxView({super.key});
}
