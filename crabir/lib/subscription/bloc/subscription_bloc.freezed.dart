// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'subscription_bloc.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$SubscriptionEvent implements DiagnosticableTreeMixin {
  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    properties..add(DiagnosticsProperty('type', 'SubscriptionEvent'));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is SubscriptionEvent);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'SubscriptionEvent()';
  }
}

/// @nodoc
class $SubscriptionEventCopyWith<$Res> {
  $SubscriptionEventCopyWith(
      SubscriptionEvent _, $Res Function(SubscriptionEvent) __);
}

/// Adds pattern-matching-related methods to [SubscriptionEvent].
extension SubscriptionEventPatterns on SubscriptionEvent {
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
    TResult Function(Changed value)? changed,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Initialize() when initialize != null:
        return initialize(_that);
      case Changed() when changed != null:
        return changed(_that);
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
    required TResult Function(Changed value) changed,
  }) {
    final _that = this;
    switch (_that) {
      case Initialize():
        return initialize(_that);
      case Changed():
        return changed(_that);
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
    TResult? Function(Initialize value)? initialize,
    TResult? Function(Changed value)? changed,
  }) {
    final _that = this;
    switch (_that) {
      case Initialize() when initialize != null:
        return initialize(_that);
      case Changed() when changed != null:
        return changed(_that);
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
    TResult Function(bool isSubscribed)? changed,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Initialize() when initialize != null:
        return initialize();
      case Changed() when changed != null:
        return changed(_that.isSubscribed);
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
    required TResult Function(bool isSubscribed) changed,
  }) {
    final _that = this;
    switch (_that) {
      case Initialize():
        return initialize();
      case Changed():
        return changed(_that.isSubscribed);
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
    TResult? Function()? initialize,
    TResult? Function(bool isSubscribed)? changed,
  }) {
    final _that = this;
    switch (_that) {
      case Initialize() when initialize != null:
        return initialize();
      case Changed() when changed != null:
        return changed(_that.isSubscribed);
      case _:
        return null;
    }
  }
}

/// @nodoc

class Initialize with DiagnosticableTreeMixin implements SubscriptionEvent {
  Initialize();

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    properties
      ..add(DiagnosticsProperty('type', 'SubscriptionEvent.initialize'));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Initialize);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'SubscriptionEvent.initialize()';
  }
}

/// @nodoc

class Changed with DiagnosticableTreeMixin implements SubscriptionEvent {
  Changed(this.isSubscribed);

  final bool isSubscribed;

  /// Create a copy of SubscriptionEvent
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $ChangedCopyWith<Changed> get copyWith =>
      _$ChangedCopyWithImpl<Changed>(this, _$identity);

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    properties
      ..add(DiagnosticsProperty('type', 'SubscriptionEvent.changed'))
      ..add(DiagnosticsProperty('isSubscribed', isSubscribed));
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Changed &&
            (identical(other.isSubscribed, isSubscribed) ||
                other.isSubscribed == isSubscribed));
  }

  @override
  int get hashCode => Object.hash(runtimeType, isSubscribed);

  @override
  String toString({DiagnosticLevel minLevel = DiagnosticLevel.info}) {
    return 'SubscriptionEvent.changed(isSubscribed: $isSubscribed)';
  }
}

/// @nodoc
abstract mixin class $ChangedCopyWith<$Res>
    implements $SubscriptionEventCopyWith<$Res> {
  factory $ChangedCopyWith(Changed value, $Res Function(Changed) _then) =
      _$ChangedCopyWithImpl;
  @useResult
  $Res call({bool isSubscribed});
}

/// @nodoc
class _$ChangedCopyWithImpl<$Res> implements $ChangedCopyWith<$Res> {
  _$ChangedCopyWithImpl(this._self, this._then);

  final Changed _self;
  final $Res Function(Changed) _then;

  /// Create a copy of SubscriptionEvent
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? isSubscribed = null,
  }) {
    return _then(Changed(
      null == isSubscribed
          ? _self.isSubscribed
          : isSubscribed // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

// dart format on
