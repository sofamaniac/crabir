// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user_account.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_UserAccount _$UserAccountFromJson(Map<String, dynamic> json) => _UserAccount(
      id: json['id'] as String,
      username: json['username'] as String,
      profilePicture: json['profilePicture'] as String,
      accessToken: json['accessToken'] as String?,
      refreshToken: json['refreshToken'] as String?,
    );

Map<String, dynamic> _$UserAccountToJson(_UserAccount instance) =>
    <String, dynamic>{
      'id': instance.id,
      'username': instance.username,
      'profilePicture': instance.profilePicture,
      'accessToken': instance.accessToken,
      'refreshToken': instance.refreshToken,
    };
