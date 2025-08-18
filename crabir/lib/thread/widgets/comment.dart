import 'package:crabir/flair.dart';
import 'package:crabir/post/widget/html_with_fade.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:flutter/material.dart';

class CommentView extends StatefulWidget {
  final Comment comment;
  final bool animateBottomBar;
  final VoidCallback? onLongPress;
  final VoidCallback? onTap;
  const CommentView({
    super.key,
    required this.comment,
    this.animateBottomBar = true,
    this.onLongPress,
    this.onTap,
  });
  @override
  createState() => _CommentViewState();
}

class _CommentViewState extends State<CommentView> {
  bool showBottomBar = false;
  bool? likes;
  bool saved = false;
  @override
  void initState() {
    super.initState();
    likes = widget.comment.likes;
    saved = widget.comment.saved;
    showBottomBar = !widget.animateBottomBar;
  }

  @override
  Widget build(BuildContext context) {
    final comment = widget.comment;
    final VoidCallback? onTap = widget.onTap ??
        (widget.animateBottomBar
            ? () => setState(
                  () {
                    showBottomBar = !showBottomBar;
                  },
                )
            : null);
    return GestureDetector(
      onLongPress: widget.onLongPress,
      onTap: onTap,
      child: AnimatedSize(
        duration: Duration(milliseconds: 200),
        alignment: Alignment.topCenter,
        child: Card(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 8.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  spacing: 8,
                  children: [
                    Text(
                      comment.author?.username ?? "u/[deleted]",
                      style: Theme.of(context)
                          .textTheme
                          .labelMedium!
                          .apply(color: Colors.red),
                    ),
                    if (comment.author?.flair != null)
                      Flexible(child: FlairView(flair: comment.author!.flair))
                  ],
                ),
                HtmlWithConditionalFade(
                  htmlContent: comment.bodyHtml,
                ),
                if (showBottomBar) bottomBar(),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Future<void> upvote() async {
    final direction = switch (likes) {
      true => VoteDirection.neutral,
      _ => VoteDirection.up,
    };
    await RedditAPI.client()
        .vote(thing: widget.comment.name, direction: direction);
    setState(() {
      if (likes == true) {
        likes = null;
      } else {
        likes = true;
      }
    });
  }

  Future<void> downvote() async {
    final direction = switch (likes) {
      false => VoteDirection.neutral,
      _ => VoteDirection.down,
    };
    await RedditAPI.client()
        .vote(thing: widget.comment.name, direction: direction);
    setState(() {
      if (likes == false) {
        likes = null;
      } else {
        likes = false;
      }
    });
  }

  Widget bottomBar() {
    final likeColor = Theme.of(context).colorScheme.primary;
    final dislikeColor = Theme.of(context).colorScheme.secondary;
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      children: [
        IconButton(
          onPressed: () => upvote(),
          icon: Icon(Icons.thumb_up, color: (likes == true) ? likeColor : null),
        ),
        IconButton(
          onPressed: () => downvote(),
          icon: Icon(Icons.thumb_down,
              color: (likes == false) ? dislikeColor : null),
        ),
        IconButton(
          // TODO: save comment
          onPressed: () => (),
          icon: Icon(Icons.book_outlined),
        ),
        IconButton(
          // TODO: reply to comment
          onPressed: () => (),
          icon: Icon(Icons.reply_rounded),
        ),
        IconButton(
          // TODO: reply to comment
          onPressed: () => debugComment(comment: widget.comment),
          icon: Icon(Icons.share),
        ),
      ],
    );
  }
}
