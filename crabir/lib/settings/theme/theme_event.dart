import 'package:crabir/settings/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'theme_event.freezed.dart';

@freezed
abstract class ThemeEvent with _$ThemeEvent {
  factory ThemeEvent.setColor({
    required ThemeField field,
    required Color color,
    required Brightness brightness,
  }) = SetColor;

  factory ThemeEvent.setMode({required ThemeMode mode}) = SetMode;
}
