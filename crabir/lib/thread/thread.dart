import 'package:crabir/comment.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:logging/logging.dart';
import 'package:provider/provider.dart';

/// Returns the id of a comment.
/// Throws an error if `comment` is not a `Thing_Comment` or a `Thing_More`.
String _commentId(Thing comment) {
  return switch (comment) {
    Thing_Comment(field0: final comment) => comment.id,
    Thing_More(id: final id) => id,
    _ => throw Exception("[_commentId] Expected Thing_Comment or Thing_More")
  };
}

class _CommentsState extends ChangeNotifier {
  final Set<String> _collapsed = {};
  final Set<String> _hidden = {};

  bool collapsed(Thing comment) {
    return _collapsed.contains(_commentId(comment));
  }

  bool hidden(Thing comment) {
    return _hidden.contains(_commentId(comment));
  }

  void collapse(Comment comment) {
    if (_collapsed.add(comment.id)) {
      for (final reply in comment.replies) {
        _hide(reply);
      }
    } else {
      _collapsed.remove(comment.id);
      for (final reply in comment.replies) {
        _reveal(reply);
      }
    }
    notifyListeners();
  }

  void _hide(Thing comment) {
    _hidden.add(_commentId(comment));
    switch (comment) {
      case Thing_Comment(field0: final comment):
        for (final reply in comment.replies) {
          _hide(reply);
        }
      default:
    }
  }

  void _reveal(Thing comment) {
    _hidden.remove(_commentId(comment));
    switch (comment) {
      case Thing_Comment(field0: final comment):
        for (final reply in comment.replies) {
          _reveal(reply);
        }
      default:
    }
  }
}

class Thread extends StatefulWidget {
  final Post post;
  const Thread({super.key, required this.post});
  @override
  State<Thread> createState() => _ThreadState();
}

class _ThreadState extends State<Thread> {
  List<Thing> comments = List.empty();
  List<Thing> flatComments = [];
  bool loaded = false;
  final log = Logger("Thread");

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
        flatten(newThings),
      );
    });
  }

  Widget appBar() {
    return SliverAppBar();
  }

  @override
  Widget build(BuildContext context) {
    if (!loaded) {
      WidgetsBinding.instance.addPostFrameCallback((_) async {
        comments =
            await RedditAPI.client().comments(permalink: widget.post.permalink);
        setState(() {
          loaded = true;
          flatComments = flatten(comments);
        });
      });
    }
    return NestedScrollView(
      headerSliverBuilder: (context, innerBoxIsScrolled) => [appBar()],
      floatHeaderSlivers: true,
      body: RefreshIndicator(
        onRefresh: () async {
          setState(() {
            comments = [];
            loaded = false;
          });
        },
        child: CustomScrollView(
          slivers: [
            SliverToBoxAdapter(
              child: RedditPostCard(
                post: widget.post,
                // max int (yes this is ugly)
                maxLines: -1 >>> 1,
              ),
            ),
            if (!loaded)
              SliverToBoxAdapter(
                child: Center(
                  child: CircularProgressIndicator(),
                ),
              ),
            if (loaded)
              ChangeNotifierProvider(
                create: (context) => _CommentsState(),
                child: CommentsList(
                  comments: flatComments,
                  onLoadMore: handleLoadMore,
                ),
              ),
          ],
        ),
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
      ..color = Colors.grey.shade800
      ..strokeWidth = 1.0;

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

class CommentBox extends StatelessWidget {
  final Thing comment;
  final void Function(Thing_More more) loadMore;
  const CommentBox({super.key, required this.comment, required this.loadMore});

  @override
  Widget build(BuildContext context) {
    final state = context.watch<_CommentsState>();
    final inner = switch (comment) {
      Thing_Comment(field0: final comment) => CommentRow(comment: comment),
      Thing_More(count: final count) => TextButton(
          onPressed: () => loadMore(comment as Thing_More),
          child: Text("Load more comments ($count)"),
        ),
      _ => Container()
    };

    return AnimatedSize(
      duration: Duration(milliseconds: 200),
      alignment: Alignment.topCenter,
      child: state.hidden(comment) ? SizedBox.shrink() : inner,
    );
  }
}

class _CommentViewHandlerState extends State<_CommentViewHandler> {
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
    final _CommentsState state = context.watch();
    return CommentView(
      comment: comment,
      onLongPress: () {
        state.collapse(comment);
        HapticFeedback.selectionClick();
      },
      animateBottomBar: true,
    );
  }
}

class _CommentViewHandler extends StatefulWidget {
  final Comment comment;

  const _CommentViewHandler({super.key, required this.comment});

  @override
  State<StatefulWidget> createState() => _CommentViewHandlerState();
}

class CommentsList extends StatefulWidget {
  final List<Thing> comments;
  final Future<void> Function(Thing_More more) onLoadMore;

  const CommentsList({
    super.key,
    required this.comments,
    required this.onLoadMore,
  });

  @override
  State<CommentsList> createState() => _CommentsListState();
}

class _CommentsListState extends State<CommentsList> {
  @override
  Widget build(BuildContext context) {
    return SliverList.builder(
      itemCount: widget.comments.length,
      itemBuilder: (context, index) {
        final comment = widget.comments[index];
        final commentView = CommentBox(
          comment: comment,
          loadMore: widget.onLoadMore,
        );
        if (index < widget.comments.length - 1 &&
            depth(widget.comments[index + 1]) == 0) {
          return Column(
            children: [
              commentView,
              Divider(height: 0),
            ],
          );
        }
        return commentView;
      },
    );
  }
}

List<Thing> flatten(
  List<Thing> comments,
) {
  final result = <Thing>[];
  for (final c in comments) {
    result.add(c);
    if (c is Thing_Comment) {
      result.addAll(flatten(c.field0.replies));
    }
  }
  return result;
}

int depth(Thing comment) {
  return switch (comment) {
    Thing_Comment(field0: final comment) => comment.depth,
    Thing_More(depth: final depth) => depth,
    _ => -1,
  };
}

class CommentRow extends StatelessWidget {
  final Comment comment;

  const CommentRow({
    super.key,
    required this.comment,
  });

  @override
  Widget build(BuildContext context) {
    final depth = comment.depth;
    final indentWidth = 16.0;

    final List<Widget> dividers = [];

    for (int i = 0; i < depth; i++) {
      dividers.add(
        VerticalDivider(
          width: indentWidth,
        ),
      );
    }

    return IntrinsicHeight(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          ...dividers,
          Expanded(
            child: _CommentViewHandler(
              comment: comment,
            ),
          ),
        ],
      ),
    );
  }
}
