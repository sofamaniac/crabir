import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:url_launcher/url_launcher.dart';

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
    if (maxLines != null) {
      style[".md"] = Style(
        // fontSize: FontSize(fontSize),
        maxLines: maxLines,
        textOverflow: TextOverflow.ellipsis,
      );
      return Html(
        data: htmlContent,
        style: style,
      );
    } else {
      return Html(
        data: htmlContent,
        style: style,
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
