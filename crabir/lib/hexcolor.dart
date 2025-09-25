// Taken from https://stackoverflow.com/a/50081214
import 'package:flutter/material.dart';
import 'package:logging/logging.dart';

/// Convert a string of the form "#ffffff" or of the form "black" to a `Color`.
/// If the string could not be converted, return `defaultColor`.
extension StringToColor on String {
  Color stringToColor({Color defaultColor = Colors.grey}) {
    final log = Logger("stringToColor");
    if (startsWith("#")) {
      return HexColor.fromHex(this);
    } else {
      return switch (this) {
        "black" => Colors.grey[850]!,
        "transparent" => Colors.transparent,
        "light" => Colors.white,
        "dark" => Colors.grey,
        "gray" => Colors.grey[500]!,
        "" => defaultColor,
        _ => () {
            log.warning("Unknown color \"$this\"");
            return defaultColor;
          }()
      };
    }
  }
}

extension StringToColorNull on String? {
  Color stringToColor({Color defaultColor = Colors.grey}) {
    if (this == null) {
      return defaultColor;
    } else {
      return this!.stringToColor(defaultColor: defaultColor);
    }
  }
}

extension HexColor on Color {
  /// String is in the format "aabbcc" or "ffaabbcc" with an optional leading "#".
  static Color fromHex(String hexString) {
    final log = Logger("HexColor");
    if (hexString.isEmpty) {
      return Colors.black;
    }
    final buffer = StringBuffer();
    if (hexString.length == 6 || hexString.length == 7) buffer.write('ff');
    buffer.write(hexString.replaceFirst('#', ''));
    try {
      return Color(int.parse(buffer.toString(), radix: 16));
    } catch (e) {
      log.warning("Failed to parse $hexString ($e)");
      return Color(0x00000000);
    }
  }

  /// Prefixes a hash sign if [leadingHashSign] is set to `true` (default is `true`).
  String toHex({bool leadingHashSign = true}) => '${leadingHashSign ? '#' : ''}'
      '${(255 * a).toInt().toRadixString(16).padLeft(2, '0')}'
      '${(255 * r).toInt().toRadixString(16).padLeft(2, '0')}'
      '${(255 * g).toInt().toRadixString(16).padLeft(2, '0')}'
      '${(255 * b).toInt().toRadixString(16).padLeft(2, '0')}';
}
