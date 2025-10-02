import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

extension CurrentBrightness on BuildContext {
  Brightness get brightness {
    return switch (watch<ThemeBloc>().state.mode) {
      ThemeMode.dark => Brightness.dark,
      ThemeMode.light => Brightness.light,
      ThemeMode.system => MediaQuery.of(this).platformBrightness,
    };
  }
}
