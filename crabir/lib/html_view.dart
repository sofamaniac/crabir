import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:url_launcher/url_launcher.dart';

/// Render HTML with some styling already done.
class StyledHtml extends StatelessWidget {
  /// Raw HTML content to render
  final String htmlContent;

  /// Additional styles to apply.
  final Map<String, Style> additionalStyles;

  final OnTap? onLinkTap;

  const StyledHtml({
    super.key,
    required this.htmlContent,
    this.additionalStyles = const {},
    this.onLinkTap,
  });

  @override
  Widget build(BuildContext context) {
    final htmlContent = this
        .htmlContent
        .replaceAll('<p>', '')
        .replaceAll('</p>', '<br>')
        .replaceAll('\n\n', '<br>');
    final theme = context.watch<ThemeBloc>().state;
    final style = {
      // Style for divider
      "hr": Style(
        margin: Margins.symmetric(vertical: 4), // reduce vertical space

        padding: HtmlPaddings.zero,
        height: Height(1), // control thickness
        backgroundColor: Colors.grey, // ensure it's visible
      ),
      "a": Style(color: theme.linkColor),
      'blockquote': Style(
        margin: Margins.symmetric(horizontal: 0, vertical: 8),
        backgroundColor: Colors.grey.shade900,
        padding: HtmlPaddings.all(12),
        border: Border(
          left: BorderSide(
            color: theme.highlight,
            width: 2,
          ),
        ),
      ),
      "body": Style(
        margin: Margins.zero,
        padding: HtmlPaddings.symmetric(vertical: 0),
      ),
      "ul": Style(
        margin: Margins.symmetric(horizontal: 4.0, vertical: 4.0),
        padding: HtmlPaddings.all(12),
      ),
      "li": Style(
        margin: Margins.symmetric(horizontal: 8.0, vertical: 8.0),
      ),
      'img': Style(
        width: Width(
          MediaQuery.of(context).size.width,
          Unit.auto,
        ),
      ),
    };
    style.addAll(additionalStyles);
    return Html(
      style: style,
      data: htmlContent,
      onLinkTap: onLinkTap,
      extensions: [
        RedditImageExtension(),
        TextExtension(),
      ],
    );
  }
}

OnTap defaultLinkHandler = (
  String? url,
  Map<String, String> attributes,
  __,
) async {
  final uri = Uri.parse(url!);
  await launchUrl(uri);
  if (await canLaunchUrl(uri)) {}
};

/// Extension that turns reddit preview links into inline images
class RedditImageExtension extends HtmlExtension {
  const RedditImageExtension();

  @override
  bool matches(ExtensionContext context) {
    if (!super.matches(context)) return false;
    if (context.element?.localName != "a") return false;

    final href = context.attributes["href"];
    if (href == null) return false;

    // Match only reddit preview links
    return href.startsWith("https://preview.redd.it/");
  }

  @override
  StyledElement prepare(
      ExtensionContext context, List<StyledElement> children) {
    final uri = Uri.parse(context.attributes["href"]!);

    final parsedWidth = double.tryParse(uri.queryParameters['width'] ?? '');
    // seems to always be null
    final parsedHeight = double.tryParse(uri.queryParameters['height'] ?? '');

    final width = switch (parsedWidth) {
      double() => Width(parsedWidth),
      null => null,
    };
    final height = switch (parsedHeight) {
      double() => Height(parsedHeight),
      null => null,
    };

    return ImageElement(
      name: context.elementName,
      children: children,
      style: Style(),
      node: context.node,
      elementId: context.id,
      src: context.attributes["href"]!,
      alt: context.attributes["alt"],
      width: width,
      height: height,
    );
  }

  @override
  InlineSpan build(ExtensionContext context) {
    final href = context.attributes["href"]!;

    return WidgetSpan(
      alignment: PlaceholderAlignment.bottom,
      child: GestureDetector(
        onTap: () {
          showDialog(
            context: context.buildContext!,
            builder: (_) => Dialog(
              child: InteractiveViewer(
                child: Image.network(href),
              ),
            ),
          );
        },
        child: Image.network(
          href,
          fit: BoxFit.fitWidth,
        ),
      ),
    );
  }

  @override
  Set<String> get supportedTags => {"a"};
}

/// Handles rendering of text nodes and <br> tags.
class TextExtension extends HtmlExtension {
  const TextExtension();

  @override
  Set<String> get supportedTags => {
        "br",
      };

  @override
  StyledElement prepare(
      ExtensionContext context, List<StyledElement> children) {
    return LinebreakContentElement(
      style: Style(whiteSpace: WhiteSpace.pre),
      node: context.node,
    );
  }

  @override
  InlineSpan build(ExtensionContext context) {
    return TextSpan(
      text: '\n',
      style: context.styledElement!.style.generateTextStyle(),
      children: const [WidgetSpan(child: SizedBox.shrink())],
    );
  }
}
