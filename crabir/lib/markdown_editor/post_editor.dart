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
  bool nsfw = false;
  bool spoiler = false;
  bool sendReplyNotification = true;
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
              submission.nsfw = nsfw;
              submission.spoiler = spoiler;
              submission.sendreplies = sendReplyNotification;
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
            maxLength: 300,
          ),
          Row(
            children: [
              _PropertyButton(
                onChanged: (value) {
                  setState(() {
                    nsfw = value;
                  });
                },
                label: "NSFW",
              ),
              _PropertyButton(
                onChanged: (value) {
                  setState(() {
                    spoiler = value;
                  });
                },
                label: "Spoiler",
              )
            ],
          ),
          CheckboxListTile(
            value: sendReplyNotification,
            onChanged: (value) {
              setState(() {
                sendReplyNotification = value!;
              });
            },
            title: Text("Send reply notification"),
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

class _PropertyButton extends StatefulWidget {
  final void Function(bool) onChanged;
  final String label;
  const _PropertyButton({
    required this.onChanged,
    required this.label,
  });

  @override
  State<_PropertyButton> createState() => _PropertyButtonState();
}

class _PropertyButtonState extends State<_PropertyButton> {
  bool _isActive = false;

  @override
  Widget build(BuildContext context) {
    final Color bgColor = _isActive ? Colors.red : Colors.transparent;
    final Color textColor = CrabirTheme.of(context).secondaryText;

    return OutlinedButton(
      onPressed: () {
        setState(() => _isActive = !_isActive);
      },
      style: OutlinedButton.styleFrom(
        backgroundColor: bgColor,
        side: const BorderSide(color: Colors.grey),
        foregroundColor: textColor,
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
      ),
      child: Text(widget.label),
    );
  }
}
