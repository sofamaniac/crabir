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
    final color = event.color;
    final theme = switch (event.brightness) {
      Brightness.dark => state.dark,
      Brightness.light => state.light,
    };
    final newTheme = switch (event.field) {
      ThemeField.background => theme.copyWith(background: color),
      ThemeField.cardBackground => theme.copyWith(cardBackground: color),
      ThemeField.toolBarBackground => theme.copyWith(toolBarBackground: color),
      ThemeField.toolBarText => theme.copyWith(toolBarText: color),
      ThemeField.primaryColor => theme.copyWith(primaryColor: color),
      ThemeField.highlight => theme.copyWith(highlight: color),
      ThemeField.postTitle => theme.copyWith(postTitle: color),
      ThemeField.readPost => theme.copyWith(readPost: color),
      ThemeField.announcement => theme.copyWith(announcement: color),
      ThemeField.contentText => theme.copyWith(contentText: color),
      ThemeField.linkColor => theme.copyWith(linkColor: color),
    };
    final newState = switch (event.brightness) {
      Brightness.dark => state.copyWith(dark: newTheme),
      Brightness.light => state.copyWith(light: newTheme),
    };
    emit(newState);
  }
}
