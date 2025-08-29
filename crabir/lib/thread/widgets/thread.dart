import 'package:auto_route/annotations.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/widget/post.dart';
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

@RoutePage(name: "ThreadRoute")
class Thread extends StatelessWidget {
  final String permalink;
  final Post? post;
  const Thread({super.key, required this.permalink, this.post});

  Widget appBar() {
    // TODO: add sort options
    return SliverAppBar();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => ThreadBloc(permalink)..add(Load()),
      child: BlocBuilder<ThreadBloc, ThreadState>(
        builder: (BuildContext context, ThreadState state) {
          final post = this.post ?? state.post;
          return Scaffold(
            backgroundColor: Theme.of(context)
                .scaffoldBackgroundColor, // Ensure the background is opaque
            body: NestedScrollView(
              headerSliverBuilder: (context, innerBoxIsScrolled) => [
                appBar(), // Your custom sliver app bar
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
