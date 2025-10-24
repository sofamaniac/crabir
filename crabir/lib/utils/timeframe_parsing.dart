import 'package:crabir/src/rust/third_party/reddit_api/model.dart';

extension ParseTimeframe on String? {
  Timeframe? parseTimeframe() {
    return switch (this) {
      "year" => Timeframe.year,
      "month" => Timeframe.month,
      "week" => Timeframe.week,
      "day" => Timeframe.day,
      "hour" => Timeframe.hour,
      _ => null,
    };
  }
}
