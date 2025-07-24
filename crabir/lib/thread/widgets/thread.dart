import 'package:crabir/comment.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/thread/bloc/thread_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

part 'comment_box.dart';
part 'comments_list.dart';
part 'comment_row.dart';
part 'indent_guides_painter.dart';

class Thread extends StatelessWidget {
  final String permalink;
  const Thread({super.key, required this.permalink});

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
          return NestedScrollView(
            headerSliverBuilder: (context, innerBoxIsScrolled) => [appBar()],
            floatHeaderSlivers: true,
            body: RefreshIndicator(
              onRefresh: () async {
                context.read<ThreadBloc>().add(Refresh());
              },
              child: CustomScrollView(
                slivers: [
                  if (state.post != null)
                    SliverToBoxAdapter(
                      child: RedditPostCard(
                        post: state.post!,
                        // max int (yes this is ugly)
                        maxLines: -1 >>> 1,
                      ),
                    )
                  else
                    SliverToBoxAdapter(
                      child: Center(child: CircularProgressIndicator()),
                    ),
                  CommentsList(
                    comments: state.flatComments,
                  ),
                ],
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
