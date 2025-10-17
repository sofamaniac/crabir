import 'package:crabir/settings/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_markdown_plus/flutter_markdown_plus.dart';
import 'package:go_router/go_router.dart';
import 'package:markdown/markdown.dart' as md;
import 'package:url_launcher/url_launcher.dart';

class SpoilerSyntax extends md.InlineSyntax {
  SpoilerSyntax() : super(r'>!([\s\S]+?)!<');

  static final tag = "spoiler";

  @override
  bool onMatch(md.InlineParser parser, Match match) {
    final text = match[1]!;
    final element = md.Element(tag, [md.Text(text)]);
    parser.addNode(element);
    return true;
  }
}

class SpoilerBuilder extends MarkdownElementBuilder {
  @override
  Widget visitElementAfter(md.Element element, TextStyle? preferredStyle) {
    return _Spoiler(element.textContent);
  }
}

class _Spoiler extends StatefulWidget {
  final String text;
  const _Spoiler(this.text);

  @override
  State<_Spoiler> createState() => _SpoilerState();
}

class _SpoilerState extends State<_Spoiler> {
  bool _revealed = false;

  @override
  Widget build(BuildContext context) {
    final hiddenColor = CrabirTheme.of(context).secondaryText;
    return GestureDetector(
      onTap: () => setState(() => _revealed = !_revealed),
      child: Container(
        decoration: BoxDecoration(
          color: _revealed ? Colors.transparent : hiddenColor,
          borderRadius: BorderRadius.circular(4),
        ),
        padding: const EdgeInsets.symmetric(horizontal: 2),
        child: Text(
          widget.text,
          style: TextStyle(
            color: _revealed
                ? Theme.of(context).colorScheme.onSurface
                : hiddenColor,
          ),
        ),
      ),
    );
  }
}

class RedditMarkdown extends StatelessWidget {
  final String markdown;
  final bool showImages;

  const RedditMarkdown({
    super.key,
    required this.markdown,
    this.showImages = true,
  });

  @override
  Widget build(BuildContext context) {
    return MarkdownBody(
      data: preprocessRedditComment(markdown),
      selectable: false,
      shrinkWrap: true,
      inlineSyntaxes: [
        SpoilerSyntax(),
      ],
      builders: {
        SpoilerSyntax.tag: SpoilerBuilder(),
      },
      onTapLink: (url, _, __) async {
        Uri uri = Uri.parse(url);
        if (url.startsWith("/")) {
          context.push(url);
        } else {
          await launchUrl(uri);
        }
      },
      imageBuilder:
          showImages ? (uri, _, __) => Image.network(uri.toString()) : null,
    );
  }
}

String preprocessRedditComment(String text) {
  final imageUrlRegex = RegExp(
    // match on `https://preview.redd.it/*`
    r'(https?:\/\/preview\.redd\.it\/[^\s]+(?:\?(?:[^\s]*))?)',
    caseSensitive: false,
  );

  // Replace raw image URLs with markdown image syntax
  return text.replaceAllMapped(imageUrlRegex, (match) {
    final url = match.group(1)!;
    return '\n\n![]($url)\n\n';
  });
}
