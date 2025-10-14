import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/common.dart';
import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

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
    return CommonFeedView(
      key: ValueKey(widget.multi),
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
      initialSort: sort,
    );
  }
}
