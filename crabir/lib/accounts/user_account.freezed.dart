// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'user_account.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$UserAccount {
  String get id;
  String get username;
  String get profilePicture;
  String? get accessToken;
  String? get refreshToken;

  /// Create a copy of UserAccount
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $UserAccountCopyWith<UserAccount> get copyWith =>
      _$UserAccountCopyWithImpl<UserAccount>(this as UserAccount, _$identity);

  /// Serializes this UserAccount to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is UserAccount &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.username, username) ||
                other.username == username) &&
            (identical(other.profilePicture, profilePicture) ||
                other.profilePicture == profilePicture) &&
            (identical(other.accessToken, accessToken) ||
                other.accessToken == accessToken) &&
            (identical(other.refreshToken, refreshToken) ||
                other.refreshToken == refreshToken));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType, id, username, profilePicture, accessToken, refreshToken);

  @override
  String toString() {
    return 'UserAccount(id: $id, username: $username, profilePicture: $profilePicture, accessToken: $accessToken, refreshToken: $refreshToken)';
  }
}

/// @nodoc
abstract mixin class $UserAccountCopyWith<$Res> {
  factory $UserAccountCopyWith(
          UserAccount value, $Res Function(UserAccount) _then) =
      _$UserAccountCopyWithImpl;
  @useResult
  $Res call(
      {String id,
      String username,
      String profilePicture,
      String? accessToken,
      String? refreshToken});
}

/// @nodoc
class _$UserAccountCopyWithImpl<$Res> implements $UserAccountCopyWith<$Res> {
  _$UserAccountCopyWithImpl(this._self, this._then);

  final UserAccount _self;
  final $Res Function(UserAccount) _then;

  /// Create a copy of UserAccount
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? username = null,
    Object? profilePicture = null,
    Object? accessToken = freezed,
    Object? refreshToken = freezed,
  }) {
    return _then(_self.copyWith(
      id: null == id
          ? _self.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      username: null == username
          ? _self.username
          : username // ignore: cast_nullable_to_non_nullable
              as String,
      profilePicture: null == profilePicture
          ? _self.profilePicture
          : profilePicture // ignore: cast_nullable_to_non_nullable
              as String,
      accessToken: freezed == accessToken
          ? _self.accessToken
          : accessToken // ignore: cast_nullable_to_non_nullable
              as String?,
      refreshToken: freezed == refreshToken
          ? _self.refreshToken
          : refreshToken // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// Adds pattern-matching-related methods to [UserAccount].
extension UserAccountPatterns on UserAccount {
  /// A variant of `map` that fallback to returning `orElse`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_UserAccount value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _UserAccount() when $default != null:
        return $default(_that);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// Callbacks receives the raw object, upcasted.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case final Subclass2 value:
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_UserAccount value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _UserAccount():
        return $default(_that);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `map` that fallback to returning `null`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_UserAccount value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _UserAccount() when $default != null:
        return $default(_that);
      case _:
        return null;
    }
  }

  /// A variant of `when` that fallback to an `orElse` callback.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(String id, String username, String profilePicture,
            String? accessToken, String? refreshToken)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _UserAccount() when $default != null:
        return $default(_that.id, _that.username, _that.profilePicture,
            _that.accessToken, _that.refreshToken);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// As opposed to `map`, this offers destructuring.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case Subclass2(:final field2):
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(String id, String username, String profilePicture,
            String? accessToken, String? refreshToken)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _UserAccount():
        return $default(_that.id, _that.username, _that.profilePicture,
            _that.accessToken, _that.refreshToken);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `when` that fallback to returning `null`
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(String id, String username, String profilePicture,
            String? accessToken, String? refreshToken)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _UserAccount() when $default != null:
        return $default(_that.id, _that.username, _that.profilePicture,
            _that.accessToken, _that.refreshToken);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _UserAccount extends UserAccount {
  const _UserAccount(
      {required this.id,
      required this.username,
      required this.profilePicture,
      this.accessToken,
      this.refreshToken})
      : super._();
  factory _UserAccount.fromJson(Map<String, dynamic> json) =>
      _$UserAccountFromJson(json);

  @override
  final String id;
  @override
  final String username;
  @override
  final String profilePicture;
  @override
  final String? accessToken;
  @override
  final String? refreshToken;

  /// Create a copy of UserAccount
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$UserAccountCopyWith<_UserAccount> get copyWith =>
      __$UserAccountCopyWithImpl<_UserAccount>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$UserAccountToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _UserAccount &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.username, username) ||
                other.username == username) &&
            (identical(other.profilePicture, profilePicture) ||
                other.profilePicture == profilePicture) &&
            (identical(other.accessToken, accessToken) ||
                other.accessToken == accessToken) &&
            (identical(other.refreshToken, refreshToken) ||
                other.refreshToken == refreshToken));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
      runtimeType, id, username, profilePicture, accessToken, refreshToken);

  @override
  String toString() {
    return 'UserAccount(id: $id, username: $username, profilePicture: $profilePicture, accessToken: $accessToken, refreshToken: $refreshToken)';
  }
}

/// @nodoc
abstract mixin class _$UserAccountCopyWith<$Res>
    implements $UserAccountCopyWith<$Res> {
  factory _$UserAccountCopyWith(
          _UserAccount value, $Res Function(_UserAccount) _then) =
      __$UserAccountCopyWithImpl;
  @override
  @useResult
  $Res call(
      {String id,
      String username,
      String profilePicture,
      String? accessToken,
      String? refreshToken});
}

/// @nodoc
class __$UserAccountCopyWithImpl<$Res> implements _$UserAccountCopyWith<$Res> {
  __$UserAccountCopyWithImpl(this._self, this._then);

  final _UserAccount _self;
  final $Res Function(_UserAccount) _then;

  /// Create a copy of UserAccount
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? id = null,
    Object? username = null,
    Object? profilePicture = null,
    Object? accessToken = freezed,
    Object? refreshToken = freezed,
  }) {
    return _then(_UserAccount(
      id: null == id
          ? _self.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      username: null == username
          ? _self.username
          : username // ignore: cast_nullable_to_non_nullable
              as String,
      profilePicture: null == profilePicture
          ? _self.profilePicture
          : profilePicture // ignore: cast_nullable_to_non_nullable
              as String,
      accessToken: freezed == accessToken
          ? _self.accessToken
          : accessToken // ignore: cast_nullable_to_non_nullable
              as String?,
      refreshToken: freezed == refreshToken
          ? _self.refreshToken
          : refreshToken // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

// dart format on
