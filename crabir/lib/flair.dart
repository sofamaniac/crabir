import 'dart:math';

import 'package:crabir/bordered_text.dart';
import 'package:crabir/hexcolor.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/flair.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:logging/logging.dart';

class FlairView extends StatelessWidget {
  final Flair flair;
  final Logger log = Logger("FlairView");
  FlairView({super.key, required this.flair});

  /// Compute the luminance of a color according to https://www.w3.org/WAI/WCAG22/Techniques/general/G18.html
  double _luminance(Color c) {
    final r = _linearize(c.r);
    final g = _linearize(c.g);
    final b = _linearize(c.b);

    return r * 0.2126 + g * 0.7152 + b * 0.0722;
  }

  double _linearize(double sRGB) {
    if (sRGB < 0.04045) {
      return sRGB / 12.92;
    } else {
      return pow((sRGB + 0.055) / 1.055, 2.4) as double;
    }
  }

  /// Compute the contrast between two colors according to https://www.w3.org/WAI/WCAG22/Techniques/general/G18.html
  double _colorRatio(Color c1, Color c2) {
    final r1 = _luminance(c1);
    final r2 = _luminance(c2);

    final rMin = min(r1, r2);
    final rMax = min(r1, r2);

    return (rMax + 0.05) / (rMin + 0.05);
  }

  /// Add a border around the text if the contrast between `backdgoundColor` and the `textColor`
  /// is not high enough
  InlineSpan coloredText(
    BuildContext context,
    String text, {
    required Color backgroundColor,
    required Color textColor,
  }) {
    final textStyle = Theme.of(context)
        .textTheme
        .labelSmall
        ?.copyWith(backgroundColor: backgroundColor, color: textColor);
    if (_colorRatio(textColor, backgroundColor) > 4.5) {
      return TextSpan(
        text: text,
        style: textStyle,
        semanticsLabel: "flair",
      );
    } else {
      final backgroundLuminance = _luminance(backgroundColor);
      return TextSpan(
        text: text,
        style: textStyle?.copyWith(
          color: backgroundLuminance > 0.5 ? Colors.black : Colors.white,
        ),
        semanticsLabel: "flair",
      );
    }
  }

  InlineSpan richtext(
    BuildContext context,
    Richtext richtext,
    Color backgroundColor,
    Color textColor,
  ) {
    final fontSize = Theme.of(context).textTheme.labelSmall?.fontSize ?? 15;
    final height = Theme.of(context).textTheme.labelSmall?.height ?? 1;
    final imageHeight = height * fontSize;
    return switch (richtext) {
      Richtext_Text(t: var text) => coloredText(
          context,
          text,
          textColor: textColor,
          backgroundColor: backgroundColor,
        ),
      Richtext_Emoji(a: var text, u: var url) => WidgetSpan(
          child: Image.network(url, semanticLabel: text, height: imageHeight),
          style: TextStyle(
            color: textColor,
            backgroundColor: backgroundColor,
            fontWeight: FontWeight.bold,
          ),
        )
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    // default to background color for luminance computation (should be the same as a transparent background)
    final backgroundColor = flair.backgroundColor.stringToColor(
      defaultColor: theme.cardBackground,
    );
    final foregroundColor = flair.textColor.stringToColor(
      defaultColor: Theme.of(context).textTheme.labelSmall?.foreground?.color ??
          Colors.grey,
    );
    Widget textWidget = Container();
    if (flair.richtext.isEmpty) {
      if (flair.text?.isNotEmpty == true) {
        textWidget = Text(
          flair.text!,
          style: Theme.of(context).textTheme.labelSmall!.copyWith(
                backgroundColor: backgroundColor,
                color: foregroundColor,
              ),
          semanticsLabel: "Flair",
          overflow: TextOverflow.ellipsis,
          maxLines: 1,
        );
      }
    } else {
      textWidget = RichText(
        overflow: TextOverflow.ellipsis,
        maxLines: 1,
        text: TextSpan(
          children: flair.richtext
              .map(
                (e) => richtext(
                  context,
                  e,
                  backgroundColor,
                  foregroundColor,
                ),
              )
              .toList(),
        ),
      );
    }
    if (textWidget is Container) {
      return Container();
    }
    return Container(
      padding: EdgeInsetsDirectional.symmetric(horizontal: 6, vertical: 2),
      decoration: BoxDecoration(
        color: backgroundColor,
        borderRadius: BorderRadius.circular(2),
      ),
      child: textWidget,
    );
  }
}
