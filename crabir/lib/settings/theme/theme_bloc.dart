import 'dart:ui';

import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/settings/theme/theme_event.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';

part 'theme_bloc.freezed.dart';
part 'theme_bloc.g.dart';

@freezed
abstract class ThemeState with _$ThemeState {
  ThemeState._();
  factory ThemeState({
    required CrabirTheme dark,
    required CrabirTheme light,
  }) = _ThemeState;
  factory ThemeState.fromJson(Map<String, dynamic> json) =>
      _$ThemeStateFromJson(json);
}

class ThemeBloc extends HydratedBloc<ThemeEvent, ThemeState> {
  ThemeBloc() : super(ThemeState(dark: CrabirTheme(), light: CrabirTheme())) {
    on<SetColor>(_updateColor);
  }

  @override
  Map<String, dynamic>? toJson(ThemeState state) => state.toJson();

  @override
  ThemeState? fromJson(Map<String, dynamic> json) => ThemeState.fromJson(json);

  Future<void> _updateColor(SetColor event, Emitter<ThemeState> emit) async {
    final theme = switch (event.brightness) {
      Brightness.dark => state.dark,
      Brightness.light => state.light,
    };
    final newTheme = theme.updateColor(field: event.field, color: event.color);
    final newState = switch (event.brightness) {
      Brightness.dark => state.copyWith(dark: newTheme),
      Brightness.light => state.copyWith(light: newTheme),
    };
    emit(newState);
  }
}
