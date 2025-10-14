/// Mark a class as a setting page to generate.
class SettingsPage {
  /// Prefix to add to `Setting.titleKey` and `Setting.descriptionKey` (works with `useFieldName`).
  final String? prefix;

  /// Whether to use the fields name as key for localization. If active, uses `locales.$prefix$fieldName` as title and `locales.$prefix${fieldName}Description`  for the description if it exists.
  final bool useFieldName;
  const SettingsPage({this.prefix, this.useFieldName = false});
}

class Setting {
  /// key in `AppLocalizations` to use for the name of the setting.
  final String? titleKey;

  final bool hasDescription;

  /// Optional description.
  final String? descriptionKey;

  /// Override the widget.
  final Type? widget;

  /// Optional icon.
  final Object? icon;

  const Setting({
    this.titleKey,
    this.hasDescription = false,
    this.descriptionKey,
    this.widget,
    this.icon,
  });
}

class Category {
  final String? name;
  final bool divider;
  const Category({
    this.name,
    this.divider = true,
  });
}
