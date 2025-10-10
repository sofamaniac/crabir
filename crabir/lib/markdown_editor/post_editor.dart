part of 'editor.dart';

/// Reply to a comment
@CrabirRoute()
class PostEditor extends StatefulWidget {
  const PostEditor({
    super.key,
  });

  @override
  State<PostEditor> createState() => _PostEditorState();
}

class _PostEditorState extends State<PostEditor> {
  final _titleController = TextEditingController();
  final _bodyController = TextEditingController();
  final Logger log = Logger("CommentEditor");

  @override
  void dispose() {
    _titleController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return Scaffold(
      appBar: AppBar(
        title: Text(locales.postEditorTitle),
        actions: [
          IconButton(
            icon: Icon(Icons.send),
            onPressed: () async {
              if (context.mounted) context.pop();
            },
          )
        ],
      ),
      body: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          TextField(
            controller: _titleController,
            maxLines: 1,
            decoration: InputDecoration(
              border: OutlineInputBorder(),
              hintText: "Enter title",
            ),
          ),
          Expanded(
            child: MarkdownEditor(
              controller: _bodyController,
            ),
          ),
        ],
      ),
    );
  }
}
