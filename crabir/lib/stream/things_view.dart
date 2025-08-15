import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
import 'package:crabir/stream/bloc/stream_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class ThingsScaffold extends StatelessWidget {
  final reddit_stream.Streamable stream;
  final Widget Function(BuildContext, Post)? postView;
  final Widget Function(BuildContext, Comment)? commentView;

  const ThingsScaffold({
    super.key,
    required this.stream,
    this.postView,
    this.commentView,
  });

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      key: ValueKey(stream),
      create: (_) => StreamBloc(streamable: stream)..add(Fetch()),
      child: ThingsView(
        postView: postView,
        commentView: commentView,
      ),
    );
  }
}

/// Display things from a `StreamWrapper` in context.
class ThingsView extends StatelessWidget {
  /// Function to use do build a `Post` view.
  final Widget Function(BuildContext, Post)? postView;

  /// Function to use do build a `Comment` view.
  final Widget Function(BuildContext, Comment)? commentView;
  const ThingsView({
    super.key,
    this.postView,
    this.commentView,
  });

  @override
  Widget build(BuildContext context) {
    final bloc = context.watch<StreamBloc>();
    final state = bloc.state;
    switch (state.status) {
      case StreamStatus.failure:
        return const Center(child: Text("Failed to load items."));
      case StreamStatus.initial:
        bloc.add(Fetch());
        return const Center(child: CircularProgressIndicator());
      case StreamStatus.success:
        if (state.items.isEmpty) {
          return const Center(child: Text("Nothing to see here"));
        }
        return RefreshIndicator(
          onRefresh: () async {
            bloc.add(Refresh());
          },
          child: Scrollbar(
            child: ListView.separated(
              itemCount: state.items.length + 1,
              itemBuilder: (context, index) {
                if (index < state.items.length) {
                  switch (state.items[index]) {
                    case Thing_Post(field0: final post):
                      if (postView != null) {
                        return postView!(context, post);
                      } else {
                        return Container();
                      }
                    case Thing_Comment(field0: final comment):
                      if (commentView != null) {
                        return commentView!(context, comment);
                      } else {
                        return Container();
                      }
                    default:
                      return SizedBox.shrink();
                  }
                } else {
                  bloc.add(Fetch());
                  return Center(child: CircularProgressIndicator());
                }
              },
              separatorBuilder: (context, _) => Divider(),
            ),
          ),
        );
    }
  }
}
