import 'package:crabir/feed/post.dart';
import 'package:crabir/flair.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:logging/logging.dart';

class Thread extends StatefulWidget {
  final Post post;
  const Thread({super.key, required this.post});
  @override
  State<Thread> createState() => _ThreadState();
}

class _ThreadState extends State<Thread> {
  List<Thing> comments = List.empty();
  final Set<String> collapsedIds = {};
  List<Thing> flatComments = [];
  bool loaded = false;
  final log = Logger("Thread");

  void toggle(String id) {
    setState(() {
      if (!collapsedIds.add(id)) collapsedIds.remove(id);
    });
    HapticFeedback.selectionClick();
  }

  Future<void> handleLoadMore(Thing_More more) async {
    final newThings = await RedditAPI.client().loadMoreComments(
      parentId: widget.post.name,
      children: more.children,
    );

    List<List<Thing>> stack = [comments];
    while (stack.isNotEmpty) {
      final current = stack.last;
      if (current.contains(more)) {
        break;
      }
      stack.removeLast();
      for (final comment in current.whereType<Thing_Comment>()) {
        stack.add(comment.field0.replies);
      }
    }

    setState(() {
      final index = flatComments.indexOf(more);
      flatComments.removeAt(index);
      flatComments.insertAll(
        index,
        flattenWithCollapse(newThings, collapsedIds),
      );
    });
  }

  PreferredSizeWidget appBar() {
    return AppBar();
  }

  @override
  Widget build(BuildContext context) {
    if (!loaded) {
      WidgetsBinding.instance.addPostFrameCallback((_) async {
        comments =
            await RedditAPI.client().comments(permalink: widget.post.permalink);
        setState(() {
          loaded = true;
          flatComments = flattenWithCollapse(comments, collapsedIds);
        });
      });
    }
    List<Thing> filteredList = [];
    for (int index = 0; index < flatComments.length; index++) {
      filteredList.add(flatComments[index]);
      final id = switch (flatComments[index]) {
        Thing_Comment(field0: final comment) => comment.id,
        Thing_More(id: final id) => id,
        _ => ""
      };
      if (collapsedIds.contains(id)) {
        final currentDepth = switch (flatComments[index]) {
          Thing_Comment(field0: final comment) => comment.depth,
          Thing_More(depth: final depth) => depth,
          _ => -1 >>> 1,
        };
        index++;
        int depth = switch (flatComments[index]) {
          Thing_Comment(field0: final comment) => comment.depth,
          Thing_More(depth: final depth) => depth,
          _ => -1 >>> 1,
        };
        while (depth > currentDepth) {
          index++;
          depth = switch (flatComments[index]) {
            Thing_Comment(field0: final comment) => comment.depth,
            Thing_More(depth: final depth) => depth,
            _ => -1 >>> 1,
          };
        }
      }
    }
    return Scaffold(
      appBar: appBar(),
      body: CustomScrollView(
        slivers: [
          SliverToBoxAdapter(
            child: RedditPostCard(
              post: widget.post,
              // max int (yes this is ugly)
              maxLines: -1 >>> 1,
            ),
          ),
          CommentsList(
            comments: flatComments,
            onLoadMore: handleLoadMore,
            onToggle: toggle,
          ),
        ],
      ),
    );
  }
}

class IndentGuidesPainter extends CustomPainter {
  final int depth;

  IndentGuidesPainter({required this.depth});

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = Colors.red
      ..strokeWidth = 2.0;

    const double spacing = 12.0;
    for (int i = 0; i < depth; i++) {
      final x = i * spacing + spacing / 2;
      canvas.drawLine(Offset(x, 0), Offset(x, size.height), paint);
    }
  }

  @override
  bool shouldRepaint(covariant IndentGuidesPainter oldDelegate) =>
      oldDelegate.depth != depth;
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
  }

  @override
  Widget build(BuildContext context) {
    final comment = widget.comment;
    return GestureDetector(
      onLongPress: widget.onLongPress,
      onTap: () => setState(
        () => showBottomBar = !showBottomBar,
      ),
      child: Card(
        color: Colors.transparent,
        child: AnimatedSize(
          duration: Duration(milliseconds: 200),
          alignment: Alignment.topCenter,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                spacing: 8,
                children: [
                  Text(
                    comment.author?.username ?? "u/[deleted]",
                  ),
                  if (comment.author?.flair != null)
                    Flexible(child: FlairView(flair: comment.author!.flair))
                ],
              ),
              Html(
                data: comment.bodyHtml,
              ),
              if (showBottomBar) bottomBar(),
            ],
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
          onPressed: () => (),
          icon: Icon(Icons.book_outlined),
        ),
        IconButton(
          onPressed: () => (),
          icon: Icon(Icons.reply_rounded),
        ),
      ],
    );
  }
}

class CommentView extends StatefulWidget {
  final Comment comment;
  final VoidCallback? onLongPress;

  const CommentView({super.key, required this.comment, this.onLongPress});

  @override
  State<StatefulWidget> createState() => _CommentViewState();
}

class CommentsList extends StatefulWidget {
  final List<Thing> comments;
  final Future<void> Function(Thing_More more) onLoadMore;
  final void Function(String id) onToggle;

  const CommentsList({
    super.key,
    required this.comments,
    required this.onLoadMore,
    required this.onToggle,
  });

  @override
  State<CommentsList> createState() => _CommentsListState();
}

class _CommentsListState extends State<CommentsList> {
  final Set<String> collapsedIds = {};

  @override
  Widget build(BuildContext context) {
    return SliverList.builder(
      itemCount: widget.comments.length,
      itemBuilder: (context, index) {
        final comment = widget.comments[index];
        return switch (comment) {
          Thing_Comment(field0: final comment) => CommentRow(
              comment: comment,
              isCollapsed: collapsedIds.contains(comment.id),
              onToggle: () => widget.onToggle(comment.id),
            ),
          Thing_More(count: final count) => TextButton(
              onPressed: () => widget.onLoadMore(comment),
              child: Text("Load more comments ($count)"),
            ),
          _ => SizedBox.shrink()
        };
      },
    );
  }
}

List<Thing> flattenWithCollapse(
  List<Thing> comments,
  Set<String> collapsedIds,
) {
  final result = <Thing>[];
  for (final c in comments) {
    result.add(c);
    final isCollapsed = switch (c) {
      Thing_Comment(field0: final comment) => collapsedIds.contains(comment.id),
      Thing_More(id: final id) => collapsedIds.contains(id),
      _ => false
    };
    if (!isCollapsed) {
      if (c is Thing_Comment) {
        result.addAll(flattenWithCollapse(c.field0.replies, collapsedIds));
      }
    }
  }
  return result;
}

class CommentRow extends StatelessWidget {
  final Comment comment;
  final bool isCollapsed;
  final VoidCallback onToggle;

  const CommentRow({
    super.key,
    required this.comment,
    required this.isCollapsed,
    required this.onToggle,
  });

  @override
  Widget build(BuildContext context) {
    final depth = comment.depth;
    final indentWidth = 12.0;

    return IntrinsicHeight(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          SizedBox(
            width: depth * indentWidth,
            child: Container(
              color: Colors.transparent,
              child: CustomPaint(
                painter: IndentGuidesPainter(depth: depth),
              ),
            ),
          ),
          Expanded(
            child: CommentView(
              comment: comment,
              onLongPress: onToggle,
            ),
          ),
        ],
      ),
    );
  }
}
