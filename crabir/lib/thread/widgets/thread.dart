import 'package:auto_route/annotations.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/thread/bloc/thread_bloc.dart';
import 'package:crabir/thread/widgets/comment.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

part 'comment_box.dart';
part 'comments_list.dart';
part 'comment_row.dart';
part 'indent_guides_painter.dart';

final commentSorts = [
  CommentSort.confidence,
  CommentSort.top,
  CommentSort.new_,
  CommentSort.controversial,
  CommentSort.old,
  CommentSort.qa,
  CommentSort.random,
];

extension Label on CommentSort {
  String label() {
    return switch (this) {
      CommentSort.confidence => "Best",
      CommentSort.top => "Top",
      CommentSort.new_ => "New",
      CommentSort.controversial => "Controversial",
      CommentSort.old => "Old",
      CommentSort.qa => "Q&A",
      CommentSort.random => "Random",
      CommentSort.live => "Live"
    };
  }

  IconData icon() {
    return switch (this) {
      CommentSort.confidence => Icons.rocket,
      CommentSort.top => Icons.bar_chart,
      CommentSort.new_ => Icons.fiber_new,
      CommentSort.controversial => Icons.chat,
      CommentSort.old => Icons.history,
      CommentSort.qa => Icons.forum,
      CommentSort.random => Icons.shuffle,
      CommentSort.live => Icons.whatshot,
    };
  }
}

@RoutePage(name: "ThreadRoute")
class Thread extends StatelessWidget {
  final String permalink;
  final Post? post;
  const Thread({super.key, required this.permalink, this.post});

  Widget appBar(BuildContext context, ThreadBloc bloc) {
    return SliverAppBar(
      title: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // TODO: localization
          Text("Comments"),
          Text(
            (bloc.state.sort ??
                    bloc.state.post?.suggestedSort ??
                    CommentSort.confidence)
                .label(),
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
                  child: Text(sort.label()),
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
    final CommentSort? sort =
        settings.useSuggestedSort ? (post?.suggestedSort) : settings.sort;
    return BlocProvider(
      create: (context) => ThreadBloc(permalink)
        ..add(Load())
        ..add(SetSort(sort)),
      child: BlocBuilder<ThreadBloc, ThreadState>(
        builder: (BuildContext context, ThreadState state) {
          final post = this.post ?? state.post;
          return Scaffold(
            backgroundColor: Theme.of(context)
                .scaffoldBackgroundColor, // Ensure the background is opaque
            body: NestedScrollView(
              headerSliverBuilder: (context, innerBoxIsScrolled) => [
                appBar(context, context.read()), // Your custom sliver app bar
              ],
              floatHeaderSlivers: true,
              body: RefreshIndicator(
                onRefresh: () async {
                  context.read<ThreadBloc>().add(Refresh());
                },
                child: CustomScrollView(
                  slivers: [
                    if (post != null)
                      SliverToBoxAdapter(
                        child: RedditPostCard(
                          post: post,
                          showMedia: false,
                        ),
                      )
                    else
                      SliverToBoxAdapter(
                        child: Center(child: LoadingIndicator()),
                      ),
                    CommentsList(
                      comments: state.flatComments,
                    ),
                  ],
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
