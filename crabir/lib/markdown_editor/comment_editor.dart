part of 'editor.dart';

/// Reply to a comment
@CrabirRoute()
class CommentEditor extends StatefulWidget {
  final String title;
  final String author;
  final String text;
  final Fullname parentName;
  final int depth;
  const CommentEditor({
    super.key,
    required this.title,
    required this.author,
    required this.text,
    required this.parentName,
    required this.depth,
  });

  factory CommentEditor.reply(BuildContext context, Comment comment) {
    final locales = AppLocalizations.of(context);
    return CommentEditor(
      title: locales.replyTitle,
      author: comment.author?.username ?? locales.deletedUser,
      text: comment.body,
      parentName: comment.name,
      depth: comment.depth + 1,
    );
  }

  factory CommentEditor.comment(BuildContext context, Post post) {
    final locales = AppLocalizations.of(context);
    return CommentEditor(
      title: locales.replyTitle,
      author: post.author?.username ?? locales.deletedUser,
      text: post.selftext ?? "",
      parentName: post.name,
      depth: 0,
    );
  }

  @override
  State<CommentEditor> createState() => _CommentEditorState();
}

class _CommentEditorState extends State<CommentEditor> {
  final _controller = TextEditingController();
  final Logger log = Logger("CommentEditor");

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        actions: [
          IconButton(
            icon: Icon(Icons.send),
            onPressed: () async {
              log.info("Submitting comment to ${widget.parentName}");
              final comment = await RedditAPI.client().submitComment(
                parent: widget.parentName,
                body: _controller.text,
                depth: widget.depth,
              );
              if (context.mounted) context.pop(comment);
            },
          )
        ],
      ),
      body: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(widget.author),
          Text(widget.text),
          Expanded(
            child: MarkdownEditor(
              controller: _controller,
            ),
          ),
        ],
      ),
    );
  }
}
