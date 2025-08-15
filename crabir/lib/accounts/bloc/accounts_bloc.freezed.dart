// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'accounts_bloc.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$AccountEvent {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is AccountEvent);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountEvent()';
  }
}

/// @nodoc
class $AccountEventCopyWith<$Res> {
  $AccountEventCopyWith(AccountEvent _, $Res Function(AccountEvent) __);
}

/// @nodoc

class Initialize implements AccountEvent {
  const Initialize();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Initialize);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountEvent.initialize()';
  }
}

/// @nodoc

class SelectAccount implements AccountEvent {
  const SelectAccount(this.index);

  final int index;

  /// Create a copy of AccountEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $SelectAccountCopyWith<SelectAccount> get copyWith =>
      _$SelectAccountCopyWithImpl<SelectAccount>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is SelectAccount &&
            (identical(other.index, index) || other.index == index));
  }

  @override
  int get hashCode => Object.hash(runtimeType, index);

  @override
  String toString() {
    return 'AccountEvent.selectAccount(index: $index)';
  }
}

/// @nodoc
abstract mixin class $SelectAccountCopyWith<$Res>
    implements $AccountEventCopyWith<$Res> {
  factory $SelectAccountCopyWith(
          SelectAccount value, $Res Function(SelectAccount) _then) =
      _$SelectAccountCopyWithImpl;
  @useResult
  $Res call({int index});
}

/// @nodoc
class _$SelectAccountCopyWithImpl<$Res>
    implements $SelectAccountCopyWith<$Res> {
  _$SelectAccountCopyWithImpl(this._self, this._then);

  final SelectAccount _self;
  final $Res Function(SelectAccount) _then;

  /// Create a copy of AccountEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? index = null,
  }) {
    return _then(SelectAccount(
      null == index
          ? _self.index
          : index // ignore: cast_nullable_to_non_nullable
              as int,
    ));
  }
}

/// @nodoc

class LoadSubscriptions implements AccountEvent {
  const LoadSubscriptions();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is LoadSubscriptions);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountEvent.loadSubscriptions()';
  }
}

/// @nodoc
mixin _$AccountStatus {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is AccountStatus);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountStatus()';
  }
}

/// @nodoc
class $AccountStatusCopyWith<$Res> {
  $AccountStatusCopyWith(AccountStatus _, $Res Function(AccountStatus) __);
}

/// @nodoc

class Uninit implements AccountStatus {
  const Uninit();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Uninit);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountStatus.uninit()';
  }
}

/// @nodoc

class Loading implements AccountStatus {
  const Loading();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Loading);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountStatus.loading()';
  }
}

/// @nodoc

class Unloaded implements AccountStatus {
  const Unloaded();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Unloaded);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountStatus.unloaded()';
  }
}

/// @nodoc

class Loaded implements AccountStatus {
  const Loaded();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Loaded);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountStatus.loaded()';
  }
}

/// @nodoc

class Failure implements AccountStatus {
  const Failure({this.message});

  final String? message;

  /// Create a copy of AccountStatus
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $FailureCopyWith<Failure> get copyWith =>
      _$FailureCopyWithImpl<Failure>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Failure &&
            (identical(other.message, message) || other.message == message));
  }

  @override
  int get hashCode => Object.hash(runtimeType, message);

  @override
  String toString() {
    return 'AccountStatus.failure(message: $message)';
  }
}

/// @nodoc
abstract mixin class $FailureCopyWith<$Res>
    implements $AccountStatusCopyWith<$Res> {
  factory $FailureCopyWith(Failure value, $Res Function(Failure) _then) =
      _$FailureCopyWithImpl;
  @useResult
  $Res call({String? message});
}

/// @nodoc
class _$FailureCopyWithImpl<$Res> implements $FailureCopyWith<$Res> {
  _$FailureCopyWithImpl(this._self, this._then);

  final Failure _self;
  final $Res Function(Failure) _then;

  /// Create a copy of AccountStatus
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? message = freezed,
  }) {
    return _then(Failure(
      message: freezed == message
          ? _self.message
          : message // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
mixin _$AccountState {
  AccountStatus get status;
  UserAccount? get account;
  List<Subreddit> get subscriptions;
  List<Multi> get multis;
  List<UserAccount> get allAccounts;

  /// Create a copy of AccountState
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $AccountStateCopyWith<AccountState> get copyWith =>
      _$AccountStateCopyWithImpl<AccountState>(
          this as AccountState, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is AccountState &&
            (identical(other.status, status) || other.status == status) &&
            (identical(other.account, account) || other.account == account) &&
            const DeepCollectionEquality()
                .equals(other.subscriptions, subscriptions) &&
            const DeepCollectionEquality().equals(other.multis, multis) &&
            const DeepCollectionEquality()
                .equals(other.allAccounts, allAccounts));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      status,
      account,
      const DeepCollectionEquality().hash(subscriptions),
      const DeepCollectionEquality().hash(multis),
      const DeepCollectionEquality().hash(allAccounts));

  @override
  String toString() {
    return 'AccountState(status: $status, account: $account, subscriptions: $subscriptions, multis: $multis, allAccounts: $allAccounts)';
  }
}

/// @nodoc
abstract mixin class $AccountStateCopyWith<$Res> {
  factory $AccountStateCopyWith(
          AccountState value, $Res Function(AccountState) _then) =
      _$AccountStateCopyWithImpl;
  @useResult
  $Res call(
      {AccountStatus status,
      UserAccount? account,
      List<Subreddit> subscriptions,
      List<Multi> multis,
      List<UserAccount> allAccounts});

  $AccountStatusCopyWith<$Res> get status;
}

/// @nodoc
class _$AccountStateCopyWithImpl<$Res> implements $AccountStateCopyWith<$Res> {
  _$AccountStateCopyWithImpl(this._self, this._then);

  final AccountState _self;
  final $Res Function(AccountState) _then;

  /// Create a copy of AccountState
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? status = null,
    Object? account = freezed,
    Object? subscriptions = null,
    Object? multis = null,
    Object? allAccounts = null,
  }) {
    return _then(_self.copyWith(
      status: null == status
          ? _self.status
          : status // ignore: cast_nullable_to_non_nullable
              as AccountStatus,
      account: freezed == account
          ? _self.account
          : account // ignore: cast_nullable_to_non_nullable
              as UserAccount?,
      subscriptions: null == subscriptions
          ? _self.subscriptions
          : subscriptions // ignore: cast_nullable_to_non_nullable
              as List<Subreddit>,
      multis: null == multis
          ? _self.multis
          : multis // ignore: cast_nullable_to_non_nullable
              as List<Multi>,
      allAccounts: null == allAccounts
          ? _self.allAccounts
          : allAccounts // ignore: cast_nullable_to_non_nullable
              as List<UserAccount>,
    ));
  }

  /// Create a copy of AccountState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $AccountStatusCopyWith<$Res> get status {
    return $AccountStatusCopyWith<$Res>(_self.status, (value) {
      return _then(_self.copyWith(status: value));
    });
  }
}

/// @nodoc

class _AccountState implements AccountState {
  _AccountState(
      {this.status = const Uninit(),
      this.account,
      final List<Subreddit> subscriptions = const [],
      final List<Multi> multis = const [],
      final List<UserAccount> allAccounts = const []})
      : _subscriptions = subscriptions,
        _multis = multis,
        _allAccounts = allAccounts;

  @override
  @JsonKey()
  final AccountStatus status;
  @override
  final UserAccount? account;
  final List<Subreddit> _subscriptions;
  @override
  @JsonKey()
  List<Subreddit> get subscriptions {
    if (_subscriptions is EqualUnmodifiableListView) return _subscriptions;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_subscriptions);
  }

  final List<Multi> _multis;
  @override
  @JsonKey()
  List<Multi> get multis {
    if (_multis is EqualUnmodifiableListView) return _multis;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_multis);
  }

  final List<UserAccount> _allAccounts;
  @override
  @JsonKey()
  List<UserAccount> get allAccounts {
    if (_allAccounts is EqualUnmodifiableListView) return _allAccounts;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_allAccounts);
  }

  /// Create a copy of AccountState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$AccountStateCopyWith<_AccountState> get copyWith =>
      __$AccountStateCopyWithImpl<_AccountState>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _AccountState &&
            (identical(other.status, status) || other.status == status) &&
            (identical(other.account, account) || other.account == account) &&
            const DeepCollectionEquality()
                .equals(other._subscriptions, _subscriptions) &&
            const DeepCollectionEquality().equals(other._multis, _multis) &&
            const DeepCollectionEquality()
                .equals(other._allAccounts, _allAccounts));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      status,
      account,
      const DeepCollectionEquality().hash(_subscriptions),
      const DeepCollectionEquality().hash(_multis),
      const DeepCollectionEquality().hash(_allAccounts));

  @override
  String toString() {
    return 'AccountState(status: $status, account: $account, subscriptions: $subscriptions, multis: $multis, allAccounts: $allAccounts)';
  }
}

/// @nodoc
abstract mixin class _$AccountStateCopyWith<$Res>
    implements $AccountStateCopyWith<$Res> {
  factory _$AccountStateCopyWith(
          _AccountState value, $Res Function(_AccountState) _then) =
      __$AccountStateCopyWithImpl;
  @override
  @useResult
  $Res call(
      {AccountStatus status,
      UserAccount? account,
      List<Subreddit> subscriptions,
      List<Multi> multis,
      List<UserAccount> allAccounts});

  @override
  $AccountStatusCopyWith<$Res> get status;
}

/// @nodoc
class __$AccountStateCopyWithImpl<$Res>
    implements _$AccountStateCopyWith<$Res> {
  __$AccountStateCopyWithImpl(this._self, this._then);

  final _AccountState _self;
  final $Res Function(_AccountState) _then;

  /// Create a copy of AccountState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? status = null,
    Object? account = freezed,
    Object? subscriptions = null,
    Object? multis = null,
    Object? allAccounts = null,
  }) {
    return _then(_AccountState(
      status: null == status
          ? _self.status
          : status // ignore: cast_nullable_to_non_nullable
              as AccountStatus,
      account: freezed == account
          ? _self.account
          : account // ignore: cast_nullable_to_non_nullable
              as UserAccount?,
      subscriptions: null == subscriptions
          ? _self._subscriptions
          : subscriptions // ignore: cast_nullable_to_non_nullable
              as List<Subreddit>,
      multis: null == multis
          ? _self._multis
          : multis // ignore: cast_nullable_to_non_nullable
              as List<Multi>,
      allAccounts: null == allAccounts
          ? _self._allAccounts
          : allAccounts // ignore: cast_nullable_to_non_nullable
              as List<UserAccount>,
    ));
  }

  /// Create a copy of AccountState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $AccountStatusCopyWith<$Res> get status {
    return $AccountStatusCopyWith<$Res>(_self.status, (value) {
      return _then(_self.copyWith(status: value));
    });
  }
}

// dart format on
