part of 'editor.dart';

AlertDialog postEditorDialog(BuildContext context, {Subreddit? subreddit}) {
  return AlertDialog(
    title: Text("Create Post"),
    content: ListView(
      children: [
        ListTile(
          title: Text("Text post"),
          onTap: () => PostEditor.selftext(
            initialSubreddit: subreddit,
          ).pushNamed(context),
        ),
        ListTile(
          title: Text("Link post"),
          onTap: () => PostEditor.link(
            initialSubreddit: subreddit,
          ).pushNamed(context),
        ),
      ],
    ),
  );
}

/// Reply to a comment
class PostEditor extends StatefulWidget {
  final Widget Function(PostSubmitBuilder) _builder;
  final Kind _kind;
  final Subreddit? initialSubreddit;

  const PostEditor._({
    super.key,
    required Widget Function(PostSubmitBuilder) builder,
    required Kind kind,
    this.initialSubreddit,
  })  : _builder = builder,
        _kind = kind;

  factory PostEditor.selftext({Key? key, Subreddit? initialSubreddit}) {
    return PostEditor._(
      key: key,
      builder: (submission) => MarkdownEditor(
        onChanged: (body) => submission.text = body,
      ),
      kind: Kind.selftext,
      initialSubreddit: initialSubreddit,
    );
  }

  factory PostEditor.link({Key? key, Subreddit? initialSubreddit}) {
    return PostEditor._(
      key: key,
      builder: (submission) => Column(
        children: [
          UrlTextField(
            onChanged: (url) => submission.url = url,
          ),
          TextField(onChanged: (title) => submission.text = title)
        ],
      ),
      kind: Kind.link,
      initialSubreddit: initialSubreddit,
    );
  }

  @override
  State<PostEditor> createState() => _PostEditorState();
}

class _PostEditorState extends State<PostEditor> {
  final _titleController = TextEditingController();
  PostSubmitBuilder submission = PostSubmitBuilder();
  late Subreddit? subreddit = widget.initialSubreddit;
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
              try {
                submission.kind = widget._kind;
                await RedditAPI.client().submitPost(post: submission.build());
                if (context.mounted) context.pop();
              } catch (e) {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Failed to create post"),
                  ),
                );
                return;
              }
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
                    submission.subreddit = newSubreddit;
                  }),
                );
              }
            },
            trailing: Icon(Icons.arrow_downward_sharp),
          ),
          TextField(
            controller: _titleController,
            onChanged: (title) => submission.title = title,
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
          Expanded(
            child: widget._builder(submission),
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

extension PostEditorBuilder on PostEditor {
  static const String name = 'PostEditor';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra => {
        '_builder': _builder,
        '_kind': _kind,
        'initialSubreddit': initialSubreddit
      };

  void goNamed(BuildContext context) => context.goNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  Future<T?> pushNamed<T extends Object?>(BuildContext context) =>
      context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  static PostEditor fromExtra(Map<String, dynamic> extra) {
    return PostEditor._(
        builder: extra['_builder'] as Widget Function(PostSubmitBuilder),
        kind: extra['_kind'] as Kind,
        initialSubreddit: extra['initialSubreddit'] as Subreddit?);
  }
}

class UrlTextField extends StatelessWidget {
  final TextEditingController? controller;
  final void Function(String)? onChanged;

  const UrlTextField({
    super.key,
    this.controller,
    this.onChanged,
  });

  bool _isValidUrl(String input) {
    final uri = Uri.tryParse(input);
    return uri != null &&
        (uri.hasScheme && uri.hasAuthority) &&
        (uri.scheme == 'http' || uri.scheme == 'https');
  }

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: controller,
      keyboardType: TextInputType.url,
      decoration: const InputDecoration(
        labelText: 'Enter URL',
        hintText: 'https://example.com',
        prefixIcon: Icon(Icons.link),
        border: OutlineInputBorder(),
      ),
      autovalidateMode: AutovalidateMode.onUserInteraction,
      onChanged: onChanged,
      validator: (value) {
        if (value == null || value.isEmpty) {
          return 'Please enter a URL';
        }
        if (!_isValidUrl(value)) {
          return 'Invalid URL format';
        }
        return null;
      },
    );
  }
}
