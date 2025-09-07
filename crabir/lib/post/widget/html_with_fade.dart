import 'package:auto_route/auto_route.dart';
import 'package:crabir/html_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';

class HtmlWithConditionalFade extends StatelessWidget {
  /// Raw HTML content to render
  final String htmlContent;

  /// Maximum number of line to display.
  final int? maxLines;

  const HtmlWithConditionalFade({
    super.key,
    required this.htmlContent,
    this.maxLines,
  });

  @override
  Widget build(BuildContext context) {
    if (maxLines != null) {
      return StyledHtml(
        htmlContent: htmlContent,
        additionalStyles: {
          ".md": Style(
            // fontSize: FontSize(fontSize),
            maxLines: maxLines,
            textOverflow: TextOverflow.ellipsis,
          ),
        },
      );
    } else {
      return StyledHtml(
        htmlContent: htmlContent,
        onLinkTap: (url, attributes, element) => defaultLinkHandler(
          context.router,
          url,
          attributes,
          element,
        ),
      );
    }
  }
}
