import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';

class HtmlWithConditionalFade extends StatefulWidget {
  final String htmlContent;
  final int maxLines;
  final double fontSize;
  final Color backgroundColor;
  final double height;

  const HtmlWithConditionalFade({
    super.key,
    required this.htmlContent,
    required this.maxLines,
    required this.fontSize,
    required this.backgroundColor,
    required this.height,
  });

  @override
  State<HtmlWithConditionalFade> createState() =>
      _HtmlWithConditionalFadeState();
}

class _HtmlWithConditionalFadeState extends State<HtmlWithConditionalFade> {
  final GlobalKey _key = GlobalKey();
  bool _overflowing = false;
  double _contentHeight = 0;

  @override
  void initState() {
    super.initState();
    // Wait for first layout to check overflow
    WidgetsBinding.instance.addPostFrameCallback((_) => _checkOverflow());
  }

  void _checkOverflow() {
    final context = _key.currentContext;
    if (context == null) return;
    final renderBox = context.findRenderObject() as RenderBox;
    final contentHeight = renderBox.size.height;

    final maxHeight = widget.maxLines * widget.fontSize * widget.height;

    if (mounted) {
      setState(() {
        _overflowing = contentHeight > maxHeight;
        _contentHeight = contentHeight;
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
    final maxHeight =
        min(_contentHeight, widget.maxLines * widget.fontSize * widget.height);

    return ConstrainedBox(
      constraints: BoxConstraints(
        maxHeight: maxHeight,
        maxWidth: MediaQuery.of(context).size.width,
      ),
      child: Stack(
        children: [
          Positioned.fill(
            child: SingleChildScrollView(
              physics: const NeverScrollableScrollPhysics(),
              child: Html(
                key: _key,
                data: widget.htmlContent,
              ),
            ),
          ),
          if (_overflowing) gradient(),
        ],
      ),
    );
  }
}
