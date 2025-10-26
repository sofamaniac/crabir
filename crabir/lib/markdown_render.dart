import 'dart:math' show min;

import 'package:crabir/media/media.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/gallery.dart'
    as gallery;
import 'package:flutter/material.dart';
import 'package:flutter_markdown_plus/flutter_markdown_plus.dart';
import 'package:go_router/go_router.dart';
import 'package:logging/logging.dart';
import 'package:markdown/markdown.dart' as md;
import 'package:url_launcher/url_launcher.dart';

class SpoilerSyntax extends md.InlineSyntax {
  SpoilerSyntax() : super(r'\|!([\s\S]+?)!\|');

  static final tag = "spoiler";

  @override
  bool onMatch(md.InlineParser parser, Match match) {
    if (match.group(1) == null) return true;
    final text = match[1]!;
    final element = md.Element(tag, [md.Text(text)]);
    parser.addNode(element);
    return true;
  }
}

class RedditLinkSyntax extends md.InlineSyntax {
  RedditLinkSyntax() : super(r'\b/?([ur]/[a-zA-Z0-9]+)');
  static final tag = "redditLink";
  @override
  bool onMatch(md.InlineParser parser, Match match) {
    final link = match[1]!;
    final element = md.Element("a", [md.Text(link)]);
    element.attributes["href"] = "/$link";
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
  final Map<String, gallery.GalleryMedia>? images;

  const RedditMarkdown({
    super.key,
    required this.markdown,
    this.showImages = true,
    this.images,
  });

  @override
  Widget build(BuildContext context) {
    return MarkdownBody(
      data: preprocessRedditComment(markdown),
      selectable: false,
      shrinkWrap: true,
      styleSheet: redditMarkdownStyle(context),
      inlineSyntaxes: [
        SpoilerSyntax(),
        RedditLinkSyntax(),
      ],
      builders: {
        SpoilerSyntax.tag: SpoilerBuilder(),
      },
      onTapLink: (text, href, title) async {
        if (href == null) {
          Logger("RedditMarkdown")
              .warning(": trying to open link without href");
          return;
        } else if (href.startsWith("/")) {
          context.push(href);
        } else {
          final url = Uri.parse(href);
          await launchUrl(url);
        }
      },
      imageBuilder: showImages
          ? (uri, _, alt) {
              if (uri.scheme.isEmpty) {
                final name = uri.toString().replaceAll("%7C", "|");
                if (images?.containsKey(name) ?? false) {
                  final image = images![name]!;
                  switch (image.source) {
                    case gallery.Source_Image():
                      return Image.network(
                        image.withResolution(Resolution.source, false).u,
                        semanticLabel: alt,
                      );
                    case gallery.Source_AnimatedImage(:final source):
                      return Image.network(
                        source.gif,
                        semanticLabel: alt,
                      );
                  }
                }
              }
              return Image.network(
                uri.toString(),
                semanticLabel: alt,
              );
            }
          : null,
    );
  }
}

String preprocessRedditComment(String text) {
  final imageUrlRegex = RegExp(
    // match on `https://preview.redd.it/*`
    r'(https?:\/\/preview\.redd\.it\/[^\s]+(?:\?(?:[^\s]*))?)',
    caseSensitive: false,
  );
  final spoilerRegex = RegExp(r'>!([\s\S]*?)!<');

  // Replace raw image URLs with markdown image syntax

  final result = text.replaceAllMapped(imageUrlRegex, (match) {
    final url = match.group(1)!;
    return '\n\n![]($url)\n\n';
  }).replaceAllMapped(spoilerRegex, (match) {
    final spoil = match.group(1)!;
    return '|! $spoil !|';
  });
  return result;
}

MarkdownStyleSheet redditMarkdownStyle(BuildContext context) {
  final theme = Theme.of(context);
  final crabirTheme = CrabirTheme.of(context);
  return MarkdownStyleSheet.fromTheme(theme).copyWith(
    blockquoteDecoration: BoxDecoration(
      color: crabirTheme.background,
      border: Border(
        left: BorderSide(
          color: crabirTheme.highlight,
          width: 4,
        ),
      ),
    ),
    blockquotePadding: const EdgeInsets.fromLTRB(16, 0, 0, 0),
  );
}

class RedditMarkdownWithOverfow extends StatefulWidget {
  final int? maxLines;
  final String text;
  final int width;
  final int fontSize;
  final TextStyle style;

  const RedditMarkdownWithOverfow({
    super.key,
    this.maxLines,
    required this.text,
    required this.width,
    required this.fontSize,
    required this.style,
  });

  @override
  State<RedditMarkdownWithOverfow> createState() =>
      _RedditMarkdownWithOverfowState();
}

class _RedditMarkdownWithOverfowState extends State<RedditMarkdownWithOverfow> {
  String text = "";

  double measureAverageCharWidth(TextStyle style) {
    final tp = TextPainter(
      text: TextSpan(
          text: 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',
          style: style),
      textDirection: TextDirection.ltr,
    )..layout();
    return tp.width / 52.0;
  }

  @override
  void initState() {
    super.initState();
    final charWidth = measureAverageCharWidth(widget.style);
    final lineLength = (widget.width / charWidth).ceil();
    if (widget.maxLines != null) {
      text = _truncateToLength(widget.text, lineLength, widget.maxLines!);
      if (text != widget.text) {
        text += "...";
      }
    } else {
      text = widget.text;
    }
  }

  @override
  Widget build(BuildContext context) {
    return RedditMarkdown(markdown: text);
  }
}

String _truncateToLength(String text, int lineLength, int lines) {
  if (text.isEmpty || lines == 0 || lineLength == 0) return "";
  int end = 0;
  bool done = false;
  int completedLines = 0;
  int currentLineLength = 0;
  while (!done) {
    if (end == text.length - 1) {
      done = true;
      end += 1;
      break;
    } else if (currentLineLength > lineLength) {
      currentLineLength = 0;
      completedLines += 1;
    } else if (text[end] == '\n' && text[end + 1] == '\n') {
      currentLineLength = 0;
      completedLines += 1;
      end += 2;
    } else {
      currentLineLength += 1;
      end += 1;
    }
    done = completedLines == lines;
  }
  return text.substring(0, min(text.length, end));
}
