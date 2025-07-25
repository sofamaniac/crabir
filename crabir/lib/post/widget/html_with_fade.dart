import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:url_launcher/url_launcher.dart';

class HtmlWithConditionalFade extends StatelessWidget {
  /// Raw HTML content to render
  final String htmlContent;

  /// Maximum number of line to display.
  final int? maxLines;
  final double fontSize;
  final Color backgroundColor;

  /// See text_style
  final double height;

  const HtmlWithConditionalFade({
    super.key,
    required this.htmlContent,
    required this.fontSize,
    required this.backgroundColor,
    required this.height,
    this.maxLines,
  });

  @override
  Widget build(BuildContext context) {
    if (maxLines != null) {
      return Html(
        data: htmlContent.replaceAll('<p>', '').replaceAll('</p>', '<br>'),
        style: {
          '.md': Style(
            fontSize: FontSize(fontSize),
            maxLines: maxLines,
            textOverflow: TextOverflow.ellipsis,
          ),
        },
      );
    } else {
      return Html(
        data: htmlContent,
        onLinkTap: (url, _, __) async {
          final uri = Uri.parse(url!);
          if (await canLaunchUrl(uri)) {
            await launchUrl(uri);
          }
        },
      );
    }
  }
}
