// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
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

/// Adds pattern-matching-related methods to [AccountEvent].
extension AccountEventPatterns on AccountEvent {
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
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Initialize value)? initialize,
    TResult Function(SelectAccount value)? selectAccount,
    TResult Function(LoadSubscriptions value)? loadSubscriptions,
    TResult Function(AddAccount value)? addAccount,
    TResult Function(Logout value)? logout,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Initialize() when initialize != null:
        return initialize(_that);
      case SelectAccount() when selectAccount != null:
        return selectAccount(_that);
      case LoadSubscriptions() when loadSubscriptions != null:
        return loadSubscriptions(_that);
      case AddAccount() when addAccount != null:
        return addAccount(_that);
      case Logout() when logout != null:
        return logout(_that);
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
  TResult map<TResult extends Object?>({
    required TResult Function(Initialize value) initialize,
    required TResult Function(SelectAccount value) selectAccount,
    required TResult Function(LoadSubscriptions value) loadSubscriptions,
    required TResult Function(AddAccount value) addAccount,
    required TResult Function(Logout value) logout,
  }) {
    final _that = this;
    switch (_that) {
      case Initialize():
        return initialize(_that);
      case SelectAccount():
        return selectAccount(_that);
      case LoadSubscriptions():
        return loadSubscriptions(_that);
      case AddAccount():
        return addAccount(_that);
      case Logout():
        return logout(_that);
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
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Initialize value)? initialize,
    TResult? Function(SelectAccount value)? selectAccount,
    TResult? Function(LoadSubscriptions value)? loadSubscriptions,
    TResult? Function(AddAccount value)? addAccount,
    TResult? Function(Logout value)? logout,
  }) {
    final _that = this;
    switch (_that) {
      case Initialize() when initialize != null:
        return initialize(_that);
      case SelectAccount() when selectAccount != null:
        return selectAccount(_that);
      case LoadSubscriptions() when loadSubscriptions != null:
        return loadSubscriptions(_that);
      case AddAccount() when addAccount != null:
        return addAccount(_that);
      case Logout() when logout != null:
        return logout(_that);
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
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? initialize,
    TResult Function(int index)? selectAccount,
    TResult Function()? loadSubscriptions,
    TResult Function()? addAccount,
    TResult Function()? logout,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Initialize() when initialize != null:
        return initialize();
      case SelectAccount() when selectAccount != null:
        return selectAccount(_that.index);
      case LoadSubscriptions() when loadSubscriptions != null:
        return loadSubscriptions();
      case AddAccount() when addAccount != null:
        return addAccount();
      case Logout() when logout != null:
        return logout();
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
  TResult when<TResult extends Object?>({
    required TResult Function() initialize,
    required TResult Function(int index) selectAccount,
    required TResult Function() loadSubscriptions,
    required TResult Function() addAccount,
    required TResult Function() logout,
  }) {
    final _that = this;
    switch (_that) {
      case Initialize():
        return initialize();
      case SelectAccount():
        return selectAccount(_that.index);
      case LoadSubscriptions():
        return loadSubscriptions();
      case AddAccount():
        return addAccount();
      case Logout():
        return logout();
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
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? initialize,
    TResult? Function(int index)? selectAccount,
    TResult? Function()? loadSubscriptions,
    TResult? Function()? addAccount,
    TResult? Function()? logout,
  }) {
    final _that = this;
    switch (_that) {
      case Initialize() when initialize != null:
        return initialize();
      case SelectAccount() when selectAccount != null:
        return selectAccount(_that.index);
      case LoadSubscriptions() when loadSubscriptions != null:
        return loadSubscriptions();
      case AddAccount() when addAccount != null:
        return addAccount();
      case Logout() when logout != null:
        return logout();
      case _:
        return null;
    }
  }
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

class AddAccount implements AccountEvent {
  const AddAccount();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is AddAccount);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountEvent.addAccount()';
  }
}

/// @nodoc

class Logout implements AccountEvent {
  const Logout();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Logout);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'AccountEvent.logout()';
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

/// Adds pattern-matching-related methods to [AccountStatus].
extension AccountStatusPatterns on AccountStatus {
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
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Uninit value)? uninit,
    TResult Function(Loading value)? loading,
    TResult Function(Unloaded value)? unloaded,
    TResult Function(Loaded value)? loaded,
    TResult Function(Failure value)? failure,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Uninit() when uninit != null:
        return uninit(_that);
      case Loading() when loading != null:
        return loading(_that);
      case Unloaded() when unloaded != null:
        return unloaded(_that);
      case Loaded() when loaded != null:
        return loaded(_that);
      case Failure() when failure != null:
        return failure(_that);
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
  TResult map<TResult extends Object?>({
    required TResult Function(Uninit value) uninit,
    required TResult Function(Loading value) loading,
    required TResult Function(Unloaded value) unloaded,
    required TResult Function(Loaded value) loaded,
    required TResult Function(Failure value) failure,
  }) {
    final _that = this;
    switch (_that) {
      case Uninit():
        return uninit(_that);
      case Loading():
        return loading(_that);
      case Unloaded():
        return unloaded(_that);
      case Loaded():
        return loaded(_that);
      case Failure():
        return failure(_that);
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
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Uninit value)? uninit,
    TResult? Function(Loading value)? loading,
    TResult? Function(Unloaded value)? unloaded,
    TResult? Function(Loaded value)? loaded,
    TResult? Function(Failure value)? failure,
  }) {
    final _that = this;
    switch (_that) {
      case Uninit() when uninit != null:
        return uninit(_that);
      case Loading() when loading != null:
        return loading(_that);
      case Unloaded() when unloaded != null:
        return unloaded(_that);
      case Loaded() when loaded != null:
        return loaded(_that);
      case Failure() when failure != null:
        return failure(_that);
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
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? uninit,
    TResult Function()? loading,
    TResult Function()? unloaded,
    TResult Function()? loaded,
    TResult Function(String? message)? failure,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Uninit() when uninit != null:
        return uninit();
      case Loading() when loading != null:
        return loading();
      case Unloaded() when unloaded != null:
        return unloaded();
      case Loaded() when loaded != null:
        return loaded();
      case Failure() when failure != null:
        return failure(_that.message);
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
  TResult when<TResult extends Object?>({
    required TResult Function() uninit,
    required TResult Function() loading,
    required TResult Function() unloaded,
    required TResult Function() loaded,
    required TResult Function(String? message) failure,
  }) {
    final _that = this;
    switch (_that) {
      case Uninit():
        return uninit();
      case Loading():
        return loading();
      case Unloaded():
        return unloaded();
      case Loaded():
        return loaded();
      case Failure():
        return failure(_that.message);
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
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? uninit,
    TResult? Function()? loading,
    TResult? Function()? unloaded,
    TResult? Function()? loaded,
    TResult? Function(String? message)? failure,
  }) {
    final _that = this;
    switch (_that) {
      case Uninit() when uninit != null:
        return uninit();
      case Loading() when loading != null:
        return loading();
      case Unloaded() when unloaded != null:
        return unloaded();
      case Loaded() when loaded != null:
        return loaded();
      case Failure() when failure != null:
        return failure(_that.message);
      case _:
        return null;
    }
  }
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
  $UserAccountCopyWith<$Res>? get account;
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

  /// Create a copy of AccountState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $UserAccountCopyWith<$Res>? get account {
    if (_self.account == null) {
      return null;
    }

    return $UserAccountCopyWith<$Res>(_self.account!, (value) {
      return _then(_self.copyWith(account: value));
    });
  }
}

/// Adds pattern-matching-related methods to [AccountState].
extension AccountStatePatterns on AccountState {
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
    TResult Function(_AccountState value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _AccountState() when $default != null:
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
    TResult Function(_AccountState value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _AccountState():
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
    TResult? Function(_AccountState value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _AccountState() when $default != null:
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
    TResult Function(
            AccountStatus status,
            UserAccount? account,
            List<Subreddit> subscriptions,
            List<Multi> multis,
            List<UserAccount> allAccounts)?
        $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _AccountState() when $default != null:
        return $default(_that.status, _that.account, _that.subscriptions,
            _that.multis, _that.allAccounts);
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
    TResult Function(
            AccountStatus status,
            UserAccount? account,
            List<Subreddit> subscriptions,
            List<Multi> multis,
            List<UserAccount> allAccounts)
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _AccountState():
        return $default(_that.status, _that.account, _that.subscriptions,
            _that.multis, _that.allAccounts);
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
    TResult? Function(
            AccountStatus status,
            UserAccount? account,
            List<Subreddit> subscriptions,
            List<Multi> multis,
            List<UserAccount> allAccounts)?
        $default,
  ) {
    final _that = this;
    switch (_that) {
      case _AccountState() when $default != null:
        return $default(_that.status, _that.account, _that.subscriptions,
            _that.multis, _that.allAccounts);
      case _:
        return null;
    }
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
  @override
  $UserAccountCopyWith<$Res>? get account;
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

  /// Create a copy of AccountState
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $UserAccountCopyWith<$Res>? get account {
    if (_self.account == null) {
      return null;
    }

    return $UserAccountCopyWith<$Res>(_self.account!, (value) {
      return _then(_self.copyWith(account: value));
    });
  }
}

// dart format on
