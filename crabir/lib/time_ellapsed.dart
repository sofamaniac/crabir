extension TimeEllapsed on DateTime {
  // TODO: localization
  String _message(Duration delta) {
    if (delta.inDays > 365) {
      final int years = (delta.inDays / 365).toInt();
      return "${years}y";
    } else if (delta.inDays >= 30) {
      final int months = (delta.inDays / 30).toInt();
      return "${months}m";
    } else if (delta.inDays >= 1) {
      final int days = delta.inDays;
      return "${days}d";
    } else if (delta.inHours >= 1) {
      final hours = delta.inHours;
      return "${hours}h";
    } else if (delta.inMinutes >= 1) {
      final minutes = delta.inMinutes;
      return "${minutes}min";
    } else {
      final seconds = delta.inSeconds;
      return "${seconds}s";
    }
  }

  String timeSince() {
    final delta = DateTime.now().toUtc().difference(this);
    return _message(delta);
  }
}
