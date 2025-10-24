import 'package:crabir/l10n/app_localizations.dart';
import 'package:flutter/material.dart';

extension TimeEllapsed on DateTime {
  String _message(BuildContext context, Duration delta) {
    if (delta.inDays > 365) {
      final years = (delta.inDays / 365).toInt();
      return AppLocalizations.of(context).yearsAgo(years);
    } else if (delta.inDays >= 30) {
      final months = (delta.inDays / 30).toInt();
      return AppLocalizations.of(context).monthsAgo(months);
    } else if (delta.inDays >= 1) {
      return AppLocalizations.of(context).daysAgo(delta.inDays);
    } else if (delta.inHours >= 1) {
      return AppLocalizations.of(context).hoursAgo(delta.inHours);
    } else if (delta.inMinutes >= 1) {
      return AppLocalizations.of(context).minutesAgo(delta.inMinutes);
    } else {
      return AppLocalizations.of(context).secondsAgo(delta.inSeconds);
    }
  }

  String timeSince(BuildContext context) {
    final delta = DateTime.now().toUtc().difference(this);
    return _message(context, delta);
  }
}
