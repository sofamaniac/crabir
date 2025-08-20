import 'package:crabir/src/rust/third_party/reddit_api/model.dart';

const timeOptions = [
  Timeframe.hour,
  Timeframe.day,
  Timeframe.week,
  Timeframe.month,
  Timeframe.year,
  Timeframe.all
];

String timeframeToString(Timeframe timeframe) {
  return switch (timeframe) {
    Timeframe.hour => "Hour",
    Timeframe.day => "Day",
    Timeframe.week => "Week",
    Timeframe.month => "Month",
    Timeframe.year => "Year",
    Timeframe.all => "All",
  };
}
