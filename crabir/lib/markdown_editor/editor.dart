import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/search_subreddits/widgets/search.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    show Subreddit;
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:logging/logging.dart';

part 'editor.go_route_ext.dart';
part 'comment_editor.dart';
part 'post_editor.dart';

@CrabirRoute()
class MarkdownEditor extends StatefulWidget {
  final TextEditingController? controller;
  final int? maxLines;
  final int? minLines;
  final String? hint;
  const MarkdownEditor({
    super.key,
    this.controller,
    this.maxLines,
    this.minLines,
    this.hint,
  });

  @override
  State<MarkdownEditor> createState() => _MarkdownEditorState();
}

class _MarkdownEditorState extends State<MarkdownEditor> {
  late final TextEditingController _controller;
  late final bool _isControllerOwned;

  @override
  void initState() {
    super.initState();
    _isControllerOwned = widget.controller == null;
    _controller = widget.controller ?? TextEditingController();
  }

  @override
  void dispose() {
    if (_isControllerOwned) {
      _controller.dispose();
    }
    super.dispose();
  }

  void _insertMarkdown(String left, String right) {
    final text = _controller.text;
    final selection = _controller.selection;

    if (!selection.isValid) return;

    final selectedText = selection.textInside(text);

    final newText = text.replaceRange(
      selection.start,
      selection.end,
      '$left$selectedText$right',
    );

    final cursorPos = selection.start + left.length + selectedText.length;

    _controller.value = _controller.value.copyWith(
      text: newText,
      selection: TextSelection.collapsed(offset: cursorPos),
    );
  }

  void _bold() => _insertMarkdown('**', '**');
  void _italic() => _insertMarkdown('_', '_');
  void _link() => _insertMarkdown('[', '](url)');

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Expanded(
          child: TextField(
            controller: _controller,
            maxLines: widget.maxLines,
            minLines: widget.minLines,
            decoration: InputDecoration(
              border: OutlineInputBorder(),
              hintText: widget.hint ?? 'Enter markdown...',
            ),
          ),
        ),
        const SizedBox(height: 10),
        Row(
          children: [
            IconButton(
              icon: const Icon(Icons.format_bold),
              onPressed: _bold,
              tooltip: 'Bold',
            ),
            IconButton(
              icon: const Icon(Icons.format_italic),
              onPressed: _italic,
              tooltip: 'Italic',
            ),
            IconButton(
              icon: const Icon(Icons.link),
              onPressed: _link,
              tooltip: 'Link',
            ),
          ],
        ),
      ],
    );
  }
}
