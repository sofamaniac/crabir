part of 'editor.dart';

@CrabirRoute()
class CrosspostEditor extends StatefulWidget {
  final Post post;
  const CrosspostEditor({super.key, required this.post});

  @override
  State<CrosspostEditor> createState() => _CrosspostEditorState();
}

class _CrosspostEditorState extends State<CrosspostEditor> {
  final _titleController = TextEditingController();
  CrosspostBuilder submission = CrosspostBuilder();
  Subreddit? subreddit;
  final Logger log = Logger("PostEditor");

  @override
  void initState() {
    super.initState();
    _titleController.text = widget.post.title;
    submission.postFullname = widget.post;
    submission.title = widget.post.title;
  }

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
              try {
                await RedditAPI.client().crosspost(post: submission.build());
                if (context.mounted) context.pop();
              } catch (e) {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Failed to create post $e"),
                  ),
                );
                return;
              }
            },
          )
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
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
                  navigateOnTap: false,
                ).pushNamed(context) as Subreddit?;
                if (newSubreddit != null) {
                  WidgetsBinding.instance.addPostFrameCallback(
                    (_) => setState(() {
                      subreddit = newSubreddit;
                      submission.subreddit = newSubreddit;
                    }),
                  );
                }
              },
              trailing: Icon(Icons.arrow_drop_down_sharp),
            ),
            TextField(
              controller: _titleController,
              onChanged: (title) => submission.title = title,
              maxLines: 1,
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                hintText: "Title",
              ),
              maxLength: 300,
            ),
            Row(
              spacing: 8,
              children: [
                _PropertyButton(
                  onChanged: (value) {
                    setState(() {
                      submission.nsfw = value;
                    });
                  },
                  label: "NSFW",
                ),
                _PropertyButton(
                  onChanged: (value) {
                    setState(() {
                      submission.spoiler = value;
                    });
                  },
                  label: "Spoiler",
                )
              ],
            ),
            CheckboxListTile(
              value: submission.sendreplies,
              onChanged: (value) {
                setState(() {
                  submission.sendreplies = value!;
                });
              },
              title: Text("Send reply notification"),
            ),
            DenseCard(post: widget.post),
          ],
        ),
      ),
    );
  }
}
