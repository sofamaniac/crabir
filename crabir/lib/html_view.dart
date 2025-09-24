import 'package:auto_route/auto_route.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:url_launcher/url_launcher.dart';

/// Render HTML with some styling already done.
class StyledHtml extends StatelessWidget {
  /// Raw HTML content to render
  final String htmlContent;

  /// Additional styles to apply.
  final Map<String, Style> additionalStyles;

  final OnTap? onLinkTap;

  /// Whether to show images or not.
  final bool showImages;

  const StyledHtml({
    super.key,
    required this.htmlContent,
    this.additionalStyles = const {},
    this.onLinkTap,
    this.showImages = true,
  });

  String sanitize(String htmlContent) {
    final htmlContent = this
        .htmlContent
        .replaceAll('<p>', '')
        .replaceAll('</p>', '')
        .replaceAll('\n\n', '<br>');
    return htmlContent;
  }

  @override
  Widget build(BuildContext context) {
    final theme = CrabirTheme.of(context);

    final htmlContent = sanitize(this.htmlContent);

    final style = {
      // Style for divider
      "hr": Style(
        margin: Margins.symmetric(vertical: 4), // reduce vertical space

        padding: HtmlPaddings.zero,
        height: Height(1), // control thickness
        backgroundColor: theme.secondaryText, // ensure it's visible
      ),
      "a": Style(color: theme.linkColor, textDecorationColor: theme.linkColor),
      'blockquote': Style(
        margin: Margins.symmetric(horizontal: 0, vertical: 8),
        backgroundColor: theme.secondaryText,
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
      shrinkWrap: true,
      extensions: [
        if (showImages) RedditImageExtension(),
        if (!showImages)
          ImageExtension(builder: (context) {
            final src = context.attributes['src'] ?? '';
            // Replace image with its link text
            return Html(
              data: '<a href="$src">$src</a>',
            );
          }),
        TextExtension(),
      ],
    );
  }
}

Future<void> defaultLinkHandler(
  StackRouter router,
  String? url,
  Map<String, String> attributes,
  __,
) async {
  Uri uri = Uri.parse(url!);
  await launchUrl(uri);
}

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
