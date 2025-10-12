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
  Subreddit? subreddit;
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
              if (subreddit == null) {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Please select community"),
                  ),
                );
                return;
              }
              if (_titleController.text.isEmpty) {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Please enter title"),
                  ),
                );
                return;
              }
              PostSubmitBuilder submission = PostSubmitBuilder();
              submission.title = _titleController.text;
              submission.text = _bodyController.text;
              submission.subreddit = subreddit!;
              await RedditAPI.client().submitPost(post: submission.build());
              if (context.mounted) context.pop();
            },
          )
        ],
      ),
      body: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          ListTile(
            title: subreddit != null
                ? Text(subreddit!.other.displayNamePrefixed)
                : Text("Select a community"),
            onTap: () async {
              final newSubreddit = await SearchSubredditsView(
                enableUser: false,
                enablePost: false,
              ).pushNamed(context) as Subreddit?;
              if (newSubreddit != null) {
                WidgetsBinding.instance.addPostFrameCallback(
                  (_) => setState(() {
                    subreddit = newSubreddit;
                  }),
                );
              }
            },
            trailing: Icon(Icons.arrow_downward_sharp),
          ),
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
