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
    final htmlContent = this
        .htmlContent
        .replaceAll('<p>', '')
        .replaceAll('</p>', '<br>')
        .replaceAll('\n\n', '<br>');
    if (maxLines != null) {
      return Html(
        data: htmlContent,
        style: {
          '.md': Style(
            fontSize: FontSize(fontSize),
            maxLines: maxLines,
            textOverflow: TextOverflow.ellipsis,
          ),
          "hr": Style(
            margin: Margins.symmetric(vertical: 4), // reduce vertical space

            padding: HtmlPaddings.zero,
            height: Height(1), // control thickness
            backgroundColor: Colors.grey, // ensure it's visible
          ),
        },
      );
    } else {
      return Html(
        data: htmlContent,
        style: {
          "hr": Style(
            margin: Margins.symmetric(vertical: 4), // reduce vertical space

            padding: HtmlPaddings.zero,
            height: Height(1), // control thickness
            backgroundColor: Colors.grey, // ensure it's visible
          ),
        },
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
