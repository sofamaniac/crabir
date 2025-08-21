import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

const timeOptions = [
  Timeframe.hour,
  Timeframe.day,
  Timeframe.week,
  Timeframe.month,
  Timeframe.year,
  Timeframe.all
];

extension TimeframeLabel on Timeframe {
  String label(BuildContext context) {
    final locales = AppLocalizations.of(context)!;
    return switch (this) {
      Timeframe.hour => locales.timeframeHour,
      Timeframe.day => locales.timeframeDay,
      Timeframe.week => locales.timeframeWeek,
      Timeframe.month => locales.timeframeMonth,
      Timeframe.year => locales.timeframeYear,
      Timeframe.all => locales.timeframeAll,
    };
  }
}

List<Widget> menu<T>(
  T Function(Timeframe) sort,
  Function(T) onSelect,
  BuildContext context,
) {
  return timeOptions
      .map(
        (timeframe) => MenuItemButton(
          onPressed: () => onSelect(sort(timeframe)),
          child: Text(timeframe.label(context)),
        ),
      )
      .toList();
}
