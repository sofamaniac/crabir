import 'package:crabir/src/rust/third_party/reddit_api/model/user/model.dart';

import 'package:freezed_annotation/freezed_annotation.dart';

part 'user_account.freezed.dart';
part 'user_account.g.dart';

@freezed
abstract class UserAccount with _$UserAccount {
  const UserAccount._();
  const factory UserAccount({
    required String id,
    required String username,
    required String profilePicture,
    String? accessToken,
    String? refreshToken,
  }) = _UserAccount;

  factory UserAccount.fromJson(Map<String, dynamic> json) =>
      _$UserAccountFromJson(json);

  factory UserAccount.fromUserInfo(
    UserInfo info,
    String accessToken,
    String refreshToken,
  ) =>
      UserAccount(
        id: info.id,
        username: info.name,
        profilePicture: info.iconImg,
        accessToken: accessToken,
        refreshToken: refreshToken,
      );

  factory UserAccount.anonymous() => const UserAccount(
        id: "anonymous",
        username: "anonymous",
        profilePicture:
            "https://www.redditstatic.com/avatars/defaults/v2/avatar_default_1.png",
      );

  bool get isAnonymous => id == "anonymous";
}
