import 'package:crabir/loading_indicator.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;
import 'package:flutter/material.dart';

/// Display things from a `StreamWrapper` in context.
class ThingsScaffold extends StatefulWidget {
  /// Function to use do build a `Post` view.
  final Widget Function(BuildContext, Post)? postView;

  /// Function to use do build a `Comment` view.
  final Widget Function(BuildContext, Comment)? commentView;
  const ThingsScaffold({
    super.key,
    this.postView,
    this.commentView,
    required this.stream,
  });

  final reddit_stream.Streamable stream;

  @override
  State<ThingsScaffold> createState() => _ThingsScaffoldState();
}

class _ThingsScaffoldState extends State<ThingsScaffold> {
  Future<void> _loadMore() async {
    final _ = await widget.stream.next();
    if (mounted) setState(() {}); // force rebuild so new items show
  }

  @override
  Widget build(BuildContext context) {
    return RefreshIndicator(
      onRefresh: () async {
        await widget.stream.refresh();
      },
      child: Scrollbar(
        child: ListView.separated(
          itemCount: widget.stream.length + 1,
          itemBuilder: (context, index) {
            if (index < widget.stream.length) {
              switch (widget.stream.nth(n: index)) {
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
              return FutureBuilder(
                future: _loadMore(),
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.active) {
                    return Center(child: LoadingIndicator());
                  } else if (snapshot.hasError) {
                    return Center(child: Text("${snapshot.error}"));
                  } else if (snapshot.hasData) {
                    return Center(child: Text("noting more to show"));
                  } else {
                    return Center(child: LoadingIndicator());
                  }
                },
              );
            }
          },
          separatorBuilder: (context, _) => Divider(),
        ),
      ),
    );
  }
}
