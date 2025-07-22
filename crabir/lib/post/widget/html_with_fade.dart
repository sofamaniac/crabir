import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:url_launcher/url_launcher.dart';

class HtmlWithConditionalFade extends StatefulWidget {
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
  State<HtmlWithConditionalFade> createState() =>
      _HtmlWithConditionalFadeState();
}

class _HtmlWithConditionalFadeState extends State<HtmlWithConditionalFade>
    with AutomaticKeepAliveClientMixin {
  final GlobalKey _key = GlobalKey();
  bool _overflowing = false;
  double _contentHeight = 1000;
  bool _ready = false;

  // We use the AutomaticKeepAliveClientMixin to ensure that the height is computed only once.
  @override
  bool get wantKeepAlive => true;

  void _checkOverflow() {
    if (widget.maxLines == null) {
      return;
    }
    final context = _key.currentContext;
    if (context == null) return;
    final renderBox = context.findRenderObject() as RenderBox;
    final contentHeight = renderBox.size.height;

    final maxHeight = widget.maxLines! * widget.fontSize * widget.height;

    if (mounted) {
      setState(() {
        _overflowing = contentHeight > maxHeight;
        _contentHeight = contentHeight;
        _ready = true;
      });
    }
  }

  Widget gradient() {
    return Positioned(
      bottom: 0,
      left: 0,
      right: 0,
      child: IgnorePointer(
        child: Container(
          height: 40,
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              colors: [
                Colors.transparent,
                Theme.of(context).scaffoldBackgroundColor.withAlpha(127),
              ],
            ),
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    if (!_ready) {
      // Do offstage layout measurement first
      WidgetsBinding.instance.addPostFrameCallback((_) => _checkOverflow());
      return Offstage(
        child: Html(
          key: _key,
          data: widget.htmlContent,
        ),
      );
    }
    if (widget.maxLines != null) {
      final maxHeight = min(
          _contentHeight, widget.maxLines! * widget.fontSize * widget.height);

      return ConstrainedBox(
        constraints: BoxConstraints(
          maxHeight: maxHeight,
        ),
        child: Stack(
          children: [
            Positioned.fill(
              child: SingleChildScrollView(
                physics: const NeverScrollableScrollPhysics(),
                child: Html(
                  key: _key,
                  data: widget.htmlContent,
                  onLinkTap: (url, _, __) async {
                    final uri = Uri.parse(url!);
                    if (await canLaunchUrl(uri)) {
                      await launchUrl(uri);
                    }
                  },
                ),
              ),
            ),
            if (_overflowing) gradient(),
          ],
        ),
      );
    } else {
      return Html(data: widget.htmlContent);
    }
  }
}
