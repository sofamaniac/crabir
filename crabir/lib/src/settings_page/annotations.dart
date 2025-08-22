/// Mark a class as a setting page to generate.
class SettingsPage {
  const SettingsPage();
}

class Setting {
  /// key in `AppLocalizations` to use for the name of the setting.
  final String titleKey;

  /// Optional description.
  final String? descriptionKey;

  /// Override the widget.
  final Type? widget;

  /// Optional icon.
  final String? icon;

  const Setting(
    this.titleKey, {
    this.descriptionKey,
    this.widget,
    this.icon,
  });
}
