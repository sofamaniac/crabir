import 'package:auto_route/annotations.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/thread/bloc/thread_bloc.dart';
import 'package:crabir/thread/widgets/comment.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

part 'thread_entry.dart';
part 'comments_list.dart';
part 'indented_box.dart';

final commentSorts = [
  CommentSort.confidence,
  CommentSort.top,
  CommentSort.new_,
  CommentSort.controversial,
  CommentSort.old,
  CommentSort.qa,
  CommentSort.random,
];

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
          return Scaffold(
            backgroundColor: Theme.of(context)
                .scaffoldBackgroundColor, // Ensure the background is opaque
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
                      SliverToBoxAdapter(child: _PostView(post: post))
                    else
                      SliverToBoxAdapter(
                        child: Center(child: LoadingIndicator()),
                      ),
                    CommentsList(),
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

class _PostView extends RedditPostCard {
  const _PostView({
    required super.post,
  }) : super(showMedia: false);

  @override
  bool showThumbnail() {
    return super.showThumbnail() && post.crosspostParentList.isEmpty;
  }

  @override
  Widget build(BuildContext context) {
    final contentWidget = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      spacing: 8,
      children: [
        wrap(Header(post: post)),
        wrap(title(context)),
        if (post.crosspostParentList.isNotEmpty)
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: DecoratedBox(
              decoration: BoxDecoration(
                border: Border.all(color: Colors.white54),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: DenseCard(post: post.crosspostParentList.first),
              ),
            ),
          )
        else
          content(context),
        wrap(
          Footer(
            post: post,
            onLike: (direction) async {
              await post.vote(direction: direction, client: RedditAPI.client());
            },
            onSave: (save) async {
              if (save) {
                await post.save(client: RedditAPI.client());
              } else {
                await post.unsave(client: RedditAPI.client());
              }
            },
          ),
        ),
      ],
    );
    final theme = context.watch<ThemeBloc>().state;
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      color: theme.background,
      elevation: 1,
      child: InkWell(
        onTap: onTap,
        child: contentWidget,
      ),
    );
  }
}
