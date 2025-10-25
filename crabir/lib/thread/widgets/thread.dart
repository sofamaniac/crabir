import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/post.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/layout/layout_settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart'
    hide Thumbnail;
import 'package:crabir/thread/bloc/thread_bloc.dart';
import 'package:crabir/thread/widgets/comment.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:go_router/go_router.dart';
import 'package:share_plus/share_plus.dart';

part 'thread_entry.dart';
part 'comments_list.dart';
part 'indented_box.dart';
part 'post_view.dart';
part 'share_button.dart';

final commentSorts = [
  CommentSort.confidence,
  CommentSort.top,
  CommentSort.new_,
  CommentSort.controversial,
  CommentSort.old,
  CommentSort.qa,
  CommentSort.random,
];

class Thread extends StatelessWidget {
  final Post? post;
  final String permalink;

  /// requires either `permalink` to be set or `post`.
  Thread({
    super.key,
    this.post,
    String? permalink,
  })  : assert(post != null || permalink != null),
        permalink = post?.permalink ?? permalink!;
  factory Thread.fromPost(Post post) {
    return Thread();
  }

  Widget appBar(BuildContext context, ThreadBloc bloc) {
    final locales = AppLocalizations.of(context);
    return SliverAppBar(
      title: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(locales.comments),
          Text(
            (bloc.state.sort ??
                    bloc.state.post?.suggestedSort ??
                    CommentSort.confidence)
                .label(context),
            style: Theme.of(context).textTheme.labelSmall,
          ),
        ],
      ),
      actions: [
        MenuAnchor(
          menuChildren: commentSorts
              .map(
                (sort) => MenuItemButton(
                  onPressed: () => bloc.add(SetSort(sort)),
                  leadingIcon: Icon(sort.icon()),
                  child: Text(sort.label(context)),
                ),
              )
              .toList(),
          builder: (_, MenuController controller, Widget? child) {
            return IconButton(
              onPressed: () {
                if (controller.isOpen) {
                  controller.close();
                } else {
                  controller.open();
                }
              },
              icon: const Icon(Icons.sort),
            );
          },
        )
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    final settings = context.read<CommentsSettingsCubit>().state;
    final CommentSort? sort = settings.useSuggestedSort
        ? (post?.suggestedSort)
        : settings.defaultSort;
    return BlocProvider(
      create: (context) => ThreadBloc(permalink)
        ..add(Load())
        ..add(SetSort(sort)),
      child: BlocBuilder<ThreadBloc, ThreadState>(
        builder: (BuildContext context, ThreadState state) {
          final post = this.post ?? state.post;
          return SafeArea(
            child: Scaffold(
              drawerEnableOpenDragGesture: false,
              body: NestedScrollView(
                headerSliverBuilder: (context, innerBoxIsScrolled) => [
                  appBar(context, context.read()),
                ],
                floatHeaderSlivers: true,
                body: RefreshIndicator(
                  onRefresh: () async {
                    context.read<ThreadBloc>().add(Refresh());
                  },
                  child: CustomScrollView(
                    slivers: [
                      if (post != null)
                        SliverToBoxAdapter(child: _PostView(post: post)),
                      CommentsList(),
                      if (state.status case Status.unloaded)
                        SliverToBoxAdapter(
                          child: Center(child: LoadingIndicator()),
                        ),
                    ],
                  ),
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}

int depth(Thing comment) {
  return switch (comment) {
    Thing_Comment(field0: final comment) => comment.depth,
    Thing_More(depth: final depth) => depth,
    _ => -1,
  };
}
