import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/settings/theme/theme_event.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';

class ThemeBloc extends HydratedBloc<ThemeEvent, Theme> {
  ThemeBloc() : super(Theme()) {
    on<SetColor>(_updateColor);
  }

  @override
  Map<String, dynamic>? toJson(Theme state) => state.toJson();

  @override
  Theme? fromJson(Map<String, dynamic> json) => Theme.fromJson(json);

  Future<void> _updateColor(SetColor event, Emitter<Theme> emit) async {
    final color = event.color;
    final newState = switch (event.field) {
      ThemeField.background => state.copyWith(background: color),
      ThemeField.cardBackground => state.copyWith(cardBackground: color),
      ThemeField.toolBarBackground => state.copyWith(toolBarBackground: color),
      ThemeField.toolBarText => state.copyWith(toolBarText: color),
      ThemeField.primaryColor => state.copyWith(primaryColor: color),
      ThemeField.highlight => state.copyWith(highlight: color),
      ThemeField.postTitle => state.copyWith(postTitle: color),
      ThemeField.readPost => state.copyWith(readPost: color),
      ThemeField.announcement => state.copyWith(announcement: color),
      ThemeField.contentText => state.copyWith(contentText: color),
      ThemeField.linkColor => state.copyWith(linkColor: color),
    };
    emit(newState);
  }
}
