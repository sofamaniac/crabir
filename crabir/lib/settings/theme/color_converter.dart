import 'package:crabir/hexcolor.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

class ColorConverter implements JsonConverter<Color, String> {
  const ColorConverter();

  @override
  Color fromJson(String hex) {
    hex = hex.replaceFirst('#', '');
    final val = int.parse(hex, radix: 16);
    return Color(val);
  }

  @override
  String toJson(Color color) => color.toHex();
}
