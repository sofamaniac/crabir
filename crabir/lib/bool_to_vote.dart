import 'package:crabir/src/rust/third_party/reddit_api/client.dart';

extension ToVoteDirection on bool? {
  VoteDirection toVoteDirection() {
    if (this == null) return VoteDirection.neutral;
    return this! ? VoteDirection.up : VoteDirection.down;
  }
}
