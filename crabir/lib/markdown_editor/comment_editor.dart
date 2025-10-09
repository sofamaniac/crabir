part of 'editor.dart';

@CrabirRoute()

/// Reply to a comment
class CommentEditor extends StatefulWidget {
  final String title;
  final String author;
  final String text;
  final Fullname parentName;
  const CommentEditor({
    super.key,
    required this.title,
    required this.author,
    required this.text,
    required this.parentName,
  });

  factory CommentEditor.reply(BuildContext context, Comment comment) {
    final locales = AppLocalizations.of(context);
    return CommentEditor(
      title: locales.replyTitle,
      author: comment.author?.username ?? locales.deletedUser,
      text: comment.body,
      parentName: comment.name,
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
              await RedditAPI.client().submitComment(
                parent: widget.parentName,
                body: _controller.text,
              );
              if (context.mounted) context.pop();
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
