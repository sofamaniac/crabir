import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/search.dart';
import 'package:crabir/utils/timeframe_parsing.dart';

extension ParsePostSearchSort on String? {
  PostSearchSort? parsePostSearchSort(String? timeframe) {
    if (this == null) {
      return null;
    } else {
      final time = timeframe.parseTimeframe() ?? Timeframe.all;
      return switch (this) {
        "new" => PostSearchSort.new_(),
        "hot" => PostSearchSort.hot(),
        "top" => PostSearchSort.top(time),
        "relevance" => PostSearchSort.relevance(time),
        "comments" => PostSearchSort.comments(time),
        _ => null,
      };
    }
  }
}
