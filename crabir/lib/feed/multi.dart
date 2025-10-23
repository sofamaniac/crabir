import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/common.dart';
import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/settings/layout/layout_settings.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/subreddit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

class MultiView extends StatefulWidget {
  const MultiView({
    super.key,
    required this.multi,
    this.initialSort,
  });
  final Multi multi;

  /// Override the setting-defined preferred sort.
  final FeedSort? initialSort;

  @override
  State<MultiView> createState() => _MultiViewState();
}

class _MultiViewState extends State<MultiView> {
  @override
  Widget build(BuildContext context) {
    final _ = context.watch<AccountsBloc>().state;
    final bloc = context.read<PostsSettingsCubit>();
    final settings = context.read<PostsSettingsCubit>().state;
    final FeedSort sort;
    if (settings.rememberSortByCommunity) {
      sort = settings.rememberedSorts.containsMulti(widget.multi) ??
          widget.initialSort ??
          settings.defaultSort;
    } else {
      sort = widget.initialSort ?? settings.defaultSort;
    }
    final layout = LayoutSettings.of(context);
    final ViewKind view;
    if (layout.rememberByCommunity) {
      view = layout.rememberedView.containsMulti(widget.multi) ??
          layout.defaultView;
    } else {
      view = layout.defaultView;
    }
    return CommonFeedView(
      key: ValueKey(widget.multi),
      endDrawer: RightSide(multi: widget.multi),
      title: (sort) => Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(widget.multi.displayName),
          SortDisplay(sort: sort),
        ],
      ),
      newStream: (sort) => RedditAPI.client().multiPosts(
        multi: widget.multi,
        sort: sort,
      ),
      onSortChanged: (sort) {
        if (settings.rememberSortByCommunity) {
          bloc.updateRememberedSorts(
            settings.rememberedSorts.addMulti(widget.multi, sort),
          );
        }
      },
      onViewChanged: (view) {
        if (layout.rememberByCommunity) {
          context
              .read<LayoutSettingsCubit>()
              .state
              .rememberedView
              .addMulti(widget.multi, view);
        } else {
          context.read<LayoutSettingsCubit>().updateDefaultView(view);
        }
      },
      view: view,
      initialSort: sort,
    );
  }
}

class RightSide extends StatefulWidget {
  final Multi multi;

  const RightSide({super.key, required this.multi});

  @override
  State<RightSide> createState() => _RightSideState();
}

class _RightSideState extends State<RightSide> {
  Widget infoView(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: MultiRedditTile(
        widget.multi,
        showSubtitle: true,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Drawer(
        child: ListView(
          children: [
            // TODO:
            Text("Search post"),
            Divider(),
            infoView(context),
            Divider(),
            ...widget.multi.subreddits.map(
              (SubredditDetails sub) => ListTile(
                leading: SubredditIcon(icon: sub.data.icon),
                title: Text(sub.name),
                subtitle: Text("${sub.data.other.subscribers} members"),
                onTap: () => context.push("/r/${sub.name}"),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
