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
      create: (_) => StreamBloc(streamable: stream)..add(Fetch()),
      child: ThingsView(
        postView: postView,
        commentView: commentView,
      ),
    );
  }
}

/// Display things from a `StreamWrapper` in context.
class ThingsView extends StatefulWidget {
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
  createState() => _ThingsViewState();
}

class _ThingsViewState extends State<ThingsView> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<StreamBloc, StreamState>(
      builder: (context, state) {
        switch (state.status) {
          case StreamStatus.failure:
            return const Center(child: Text("Failed to load items."));
          case StreamStatus.initial:
            context.read<StreamBloc>().add(Fetch());
            return const Center(child: CircularProgressIndicator());
          case StreamStatus.success:
            if (state.items.isEmpty) {
              return const Center(child: Text("Nothing to see here"));
            }
            return RefreshIndicator(
              onRefresh: () async {
                context.read<StreamBloc>().add(Refresh());
              },
              child: Scrollbar(
                child: ListView.builder(
                  itemCount: state.items.length + 1,
                  itemBuilder: (context, index) {
                    if (index < state.items.length) {
                      switch (state.items[index]) {
                        case Thing_Post(field0: final post):
                          if (widget.postView != null) {
                            return widget.postView!(context, post);
                          } else {
                            return Container();
                          }
                        case Thing_Comment(field0: final comment):
                          if (widget.commentView != null) {
                            return widget.commentView!(context, comment);
                          } else {
                            return Container();
                          }
                        default:
                          return SizedBox.shrink();
                      }
                    } else {
                      context.read<StreamBloc>().add(Fetch());
                      return Center(child: CircularProgressIndicator());
                    }
                  },
                ),
              ),
            );
        }
      },
    );
  }
}
