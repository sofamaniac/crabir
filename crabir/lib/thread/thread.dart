import 'package:crabir/feed/post.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';

class Thread extends StatefulWidget {
  final Post post;
  const Thread({super.key, required this.post});
  @override
  State<Thread> createState() => _ThreadState();
}

class _ThreadState extends State<Thread> {
  List<Thing> comments = List.empty();
  bool loaded = false;

  @override
  Widget build(BuildContext context) {
    if (!loaded) {
      WidgetsBinding.instance.addPostFrameCallback((_) async {
        comments =
            await RedditAPI.client().comments(permalink: widget.post.permalink);
        setState(() {
          loaded = true;
        });
      });
    }
    return Column(children: [
      RedditPostCard(post: widget.post),
      ListView.builder(
        itemCount: comments.length,
        itemBuilder: (BuildContext context, int index) {
          return switch (comments[index]) {
            Thing_Comment(field0: final comment) => Text(comment.body),
            Thing_More() => Text("more"),
            // Unreachable
            _ => Container()
          };
        },
      )
    ]);
  }
}
