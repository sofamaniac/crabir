import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/feed/feed.dart';
import 'package:crabir/feed/multi.dart';
import 'package:crabir/gallery/gallery.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/main.dart';
import 'package:crabir/markdown_editor/editor.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/routes/fixed_swipe_page_route.dart';
import 'package:crabir/routes/not_found_page.dart';
import 'package:crabir/search_posts/widgets/search.dart';
import 'package:crabir/search_subreddits/widgets/search.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/data/data_settings.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/settings/lateral_menu/lateral_menu_settings.dart';
import 'package:crabir/settings/layout/layout_settings.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/settings/settings.dart';
import 'package:crabir/settings/theme/theme_editor.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/subscription/bloc/subscription_bloc.dart';
import 'package:crabir/subscription/widgets/paywall.dart';
import 'package:crabir/subscriptions_tab.dart';
import 'package:crabir/thread/widgets/thread.dart';
import 'package:crabir/user/user.dart';
import 'package:crabir/utils/post_search_parsing.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

final _rootNavigationKey = GlobalKey<NavigatorState>();
final _indexStateKey = GlobalKey<StatefulNavigationShellState>();

// ignore: constant_identifier_names
const String DEFAULT_ROUTE = "/default";

final GoRouter appRouter = GoRouter(
  debugLogDiagnostics: true,
  navigatorKey: _rootNavigationKey,
  initialLocation: DEFAULT_ROUTE,
  errorBuilder: (context, state) {
    final uri = state.uri;

    return NotFoundPage(uri: uri.toString());
  },
  redirect: (context, state) async {
    final isSubscribed = await presentPaywallIfNeeded();
    if (isSubscribed) {
      return null;
    } else {
      return "/paywall";
    }
  },
  routes: [
    StatefulShellRoute.indexedStack(
      key: _indexStateKey,
      parentNavigatorKey: _rootNavigationKey,
      builder: (context, state, navigationShell) {
        // the UI shell
        return MainScreenView(navigationShell: navigationShell);
      },
      branches: [
        // home tab
        StatefulShellBranch(
          initialLocation: DEFAULT_ROUTE,
          routes: [
            /// Home feed
            GoRoute(
              path: '/home',
              name: 'home',
              builder: (context, state) => FeedView(
                key: state.pageKey,
                feed: Feed.home(),
                initialSort: FeedSort.best(),
              ),
            ),

            /// Default feed
            GoRoute(
              path: DEFAULT_ROUTE,
              name: 'default',
              builder: (context, state) => FeedView(
                key: state.pageKey,
                feed: Feed.home(),
                initialSort: FeedSort.best(),
              ),
            ),
          ],
        ),

        // Search tab
        StatefulShellBranch(
          initialLocation: "/searchtab",
          routes: [
            GoRoute(
              path: '/searchtab',
              name: SearchSubredditsViewBuilder.name,
              builder: (context, state) => SearchSubredditsView(),
            ),
          ],
        ),
        // Feed tab
        StatefulShellBranch(
          initialLocation: "/subscriptions",
          routes: [
            GoRoute(
              path: "/subscriptions",
              builder: (context, state) => SubscriptionsTab(),
            ),

            /// Multis route
            GoRoute(
              path: "/user/:username/m/:multi",
              builder: (context, state) {
                final future = RedditAPI.client()
                    .getMulti(multiPath: state.uri.toString());
                final multi = state.extra as Multi?;
                if (multi != null) {
                  return MultiView(
                    key: ValueKey(multi.path),
                    multi: multi,
                  );
                } else {
                  return FutureBuilder(
                    future: future,
                    builder: (context, asyncSnapshot) {
                      if (asyncSnapshot.hasData) {
                        final multi = asyncSnapshot.data!;
                        return MultiView(
                          key: ValueKey(multi.path),
                          multi: multi,
                        );
                      } else {
                        return Center(
                          child: LoadingIndicator(),
                        );
                      }
                    },
                  );
                }
              },
            ),

            /// Subreddit feed
            GoRoute(
              path: '/r/:subreddit',
              name: 'feed',
              builder: (context, state) {
                final subreddit = state.pathParameters['subreddit'];
                if (subreddit == null) {
                  return NotFoundPage(uri: state.uri.toString());
                }
                return FeedView(
                  key: ValueKey(subreddit),
                  feed: Feed.subreddit(subreddit),
                );
              },
              routes: [
                /// Thread route with swipe-to-close support
                GoRoute(
                  path: 'comments/:id/:title',
                  pageBuilder: (context, state) {
                    final threadId = state.pathParameters['id']!;
                    final settings = CommentsSettings.of(context);

                    final pageChild = Thread(
                      post: state.extra as Post?,
                      permalink: state.uri.path,
                    );

                    if (settings.swipeToClose) {
                      final threshold = settings.distanceThreshold;
                      return FixedSwipePage(
                        key: ValueKey(threadId),
                        builder: (context) => pageChild,
                        dragThreshold: threshold / 100,
                      );
                    } else {
                      return MaterialPage(
                        key: ValueKey(threadId),
                        child: pageChild,
                      );
                    }
                  },
                ),

                GoRoute(
                    path: 'search',
                    builder: (context, state) {
                      final query = state.uri.queryParameters;
                      final String? subreddit;
                      if (query["restrict_sr"] == "on") {
                        subreddit = state.pathParameters["subreddit"];
                      } else {
                        subreddit = null;
                      }
                      return SearchPostsView(
                        query: query["q"] ?? "",
                        subreddit: subreddit,
                        initialSort:
                            query["sort"].parsePostSearchSort(query["t"]),
                      );
                    }),

                /// Handle short links
                GoRoute(
                  path: "s/:id",
                  builder: (context, state) {
                    final future = RedditAPI.client().resolveShortLink(
                      link: state.uri.toString(),
                    );
                    return FutureBuilder(
                      future: future,
                      builder: (context, snapshot) {
                        if (snapshot.hasData) {
                          context.replace(snapshot.data!);
                        }
                        return Scaffold(
                            body: Center(child: LoadingIndicator()));
                      },
                    );
                  },
                ),
              ],
            ),
          ],
        ),
        // Inbox tab
        StatefulShellBranch(
          initialLocation: "/inbox",
          routes: [
            GoRoute(
              path: '/inbox',
              name: 'inbox',
              builder: (context, state) => const InboxView(),
            ),
          ],
        ),
        // Profile tab
        StatefulShellBranch(
          initialLocation: "/dummy",
          routes: [
            GoRoute(
                path: "/dummy",
                builder: (context, state) {
                  return Scaffold();
                }),
            GoRoute(
              path: "/u/:username",
              // redirect `/u/alice` → `/u/alice/overview`
              redirect: (context, state) {
                final username = state.pathParameters['username']!;
                if (state.uri.pathSegments.length == 2) {
                  return '/u/$username/overview';
                }
                return null;
              },
            ),
            GoRoute(
              path: "/u/:username/:tab",
              builder: (context, state) => UserView(
                key: ValueKey(state.pathParameters["username"]),
                username: state.pathParameters['username']!,
                tab: state.pathParameters['tab']!,
              ),
            ),
          ],
        ),
      ],
    ),

    GoRoute(
      path: "/media",
      builder: (context, state) {
        final url = state.uri.queryParameters["url"] ?? "";
        return FullscreenMediaView(
          builder: (_) => CachedNetworkImage(imageUrl: url),
        );
      },
    ),

    GoRoute(
      path: '/search',
      name: SearchSubredditsViewBuilder.name,
      builder: (context, state) {
        final extra = state.extra as Map<String, dynamic>;
        return SearchSubredditsViewBuilder.fromExtra(extra);
      },
    ),

    /// Fullscreen media routes
    GoRoute(
      path: '/fullscreen-image',
      name: FullscreenImageViewBuilder.name,
      builder: (context, state) {
        final extra = state.extra as Map<String, dynamic>;
        return FullscreenImageViewBuilder.fromExtra(extra);
      },
    ),
    GoRoute(
      path: '/fullscreen-video',
      name: FullscreenVideoViewBuilder.name,
      builder: (context, state) {
        final extra = state.extra as Map<String, dynamic>;
        return FullscreenVideoViewBuilder.fromExtra(extra);
      },
    ),
    GoRoute(
      path: "/video/:id",
      builder: (context, state) {
        return Scaffold(
            body: Center(
          child: Text("TODO"),
        ));
      },
    ),
    GoRoute(
      path: '/fullscreen-gallery',
      name: FullScreenGalleryViewBuilder.name,
      builder: (context, state) {
        final extra = state.extra as Map<String, dynamic>;
        return FullScreenGalleryViewBuilder.fromExtra(extra);
      },
    ),

    // Mardown Editor
    GoRoute(
      path: "/comment-editor",
      name: CommentEditorBuilder.name,
      builder: (context, state) {
        final extra = state.extra as Map<String, dynamic>;
        return CommentEditorBuilder.fromExtra(extra);
      },
    ),

    GoRoute(
      path: "/post-editor",
      name: PostEditorBuilder.name,
      builder: (context, state) {
        final extra = state.extra as Map<String, dynamic>;
        return PostEditorBuilder.fromExtra(extra);
      },
    ),
    GoRoute(
      path: "/crosspost-editor",
      name: CrosspostEditorBuilder.name,
      builder: (context, state) {
        final extra = state.extra as Map<String, dynamic>;
        return CrosspostEditorBuilder.fromExtra(extra);
      },
    ),
    // Settings routes
    GoRoute(
      path: '/settings',
      name: SettingsViewBuilder.name,
      builder: (context, state) => SettingsView(),
      routes: [
        GoRoute(
          path: 'theme-editor',
          name: CrabirThemeEditorBuilder.name,
          builder: (context, state) => CrabirThemeEditor(),
        ),
        GoRoute(
          path: 'comments-settings',
          name: CommentsSettingsViewBuilder.name,
          builder: (context, state) => CommentsSettingsView(),
        ),
        GoRoute(
          path: 'posts-settings',
          name: PostsSettingsViewBuilder.name,
          builder: (context, state) => PostsSettingsView(),
        ),
        GoRoute(
          path: 'manage-sort',
          name: ManageSortViewBuilder.name,
          builder: (context, state) => ManageSortView(),
        ),
        GoRoute(
          path: 'data-settings',
          name: DataSettingsViewBuilder.name,
          builder: (context, state) => DataSettingsView(),
        ),
        GoRoute(
          path: 'filters-settings',
          name: FiltersSettingsViewBuilder.name,
          builder: (context, state) => FiltersSettingsView(),
        ),
        GoRoute(
          path: 'lateral-menu-settings',
          name: LateralMenuSettingsViewBuilder.name,
          builder: (context, state) => LateralMenuSettingsView(),
        ),
        GoRoute(
          path: "layout-settings",
          name: LayoutSettingsViewBuilder.name,
          builder: (context, state) => LayoutSettingsView(),
        ),
      ],
    ),
    GoRoute(path: "/paywall", builder: (context, state) => PaywallScreen())
  ],
);

class InboxView extends StatelessWidget {
  const InboxView({super.key});

  @override
  Widget build(BuildContext context) {
    return SafeArea(child: Scaffold(body: Text("WORK IN PROGRESS")));
  }
}
