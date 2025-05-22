// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'feed.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

/// @nodoc
mixin _$Feed {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() home,
    required TResult Function() all,
    required TResult Function() popular,
    required TResult Function(String field0) subreddit,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? home,
    TResult? Function()? all,
    TResult? Function()? popular,
    TResult? Function(String field0)? subreddit,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? home,
    TResult Function()? all,
    TResult Function()? popular,
    TResult Function(String field0)? subreddit,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Feed_Home value) home,
    required TResult Function(Feed_All value) all,
    required TResult Function(Feed_Popular value) popular,
    required TResult Function(Feed_Subreddit value) subreddit,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Feed_Home value)? home,
    TResult? Function(Feed_All value)? all,
    TResult? Function(Feed_Popular value)? popular,
    TResult? Function(Feed_Subreddit value)? subreddit,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Feed_Home value)? home,
    TResult Function(Feed_All value)? all,
    TResult Function(Feed_Popular value)? popular,
    TResult Function(Feed_Subreddit value)? subreddit,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FeedCopyWith<$Res> {
  factory $FeedCopyWith(Feed value, $Res Function(Feed) then) =
      _$FeedCopyWithImpl<$Res, Feed>;
}

/// @nodoc
class _$FeedCopyWithImpl<$Res, $Val extends Feed>
    implements $FeedCopyWith<$Res> {
  _$FeedCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of Feed
  /// with the given fields replaced by the non-null parameter values.
}

/// @nodoc
abstract class _$$Feed_HomeImplCopyWith<$Res> {
  factory _$$Feed_HomeImplCopyWith(
          _$Feed_HomeImpl value, $Res Function(_$Feed_HomeImpl) then) =
      __$$Feed_HomeImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$Feed_HomeImplCopyWithImpl<$Res>
    extends _$FeedCopyWithImpl<$Res, _$Feed_HomeImpl>
    implements _$$Feed_HomeImplCopyWith<$Res> {
  __$$Feed_HomeImplCopyWithImpl(
      _$Feed_HomeImpl _value, $Res Function(_$Feed_HomeImpl) _then)
      : super(_value, _then);

  /// Create a copy of Feed
  /// with the given fields replaced by the non-null parameter values.
}

/// @nodoc

class _$Feed_HomeImpl extends Feed_Home {
  const _$Feed_HomeImpl() : super._();

  @override
  String toString() {
    return 'Feed.home()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$Feed_HomeImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() home,
    required TResult Function() all,
    required TResult Function() popular,
    required TResult Function(String field0) subreddit,
  }) {
    return home();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? home,
    TResult? Function()? all,
    TResult? Function()? popular,
    TResult? Function(String field0)? subreddit,
  }) {
    return home?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? home,
    TResult Function()? all,
    TResult Function()? popular,
    TResult Function(String field0)? subreddit,
    required TResult orElse(),
  }) {
    if (home != null) {
      return home();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Feed_Home value) home,
    required TResult Function(Feed_All value) all,
    required TResult Function(Feed_Popular value) popular,
    required TResult Function(Feed_Subreddit value) subreddit,
  }) {
    return home(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Feed_Home value)? home,
    TResult? Function(Feed_All value)? all,
    TResult? Function(Feed_Popular value)? popular,
    TResult? Function(Feed_Subreddit value)? subreddit,
  }) {
    return home?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Feed_Home value)? home,
    TResult Function(Feed_All value)? all,
    TResult Function(Feed_Popular value)? popular,
    TResult Function(Feed_Subreddit value)? subreddit,
    required TResult orElse(),
  }) {
    if (home != null) {
      return home(this);
    }
    return orElse();
  }
}

abstract class Feed_Home extends Feed {
  const factory Feed_Home() = _$Feed_HomeImpl;
  const Feed_Home._() : super._();
}

/// @nodoc
abstract class _$$Feed_AllImplCopyWith<$Res> {
  factory _$$Feed_AllImplCopyWith(
          _$Feed_AllImpl value, $Res Function(_$Feed_AllImpl) then) =
      __$$Feed_AllImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$Feed_AllImplCopyWithImpl<$Res>
    extends _$FeedCopyWithImpl<$Res, _$Feed_AllImpl>
    implements _$$Feed_AllImplCopyWith<$Res> {
  __$$Feed_AllImplCopyWithImpl(
      _$Feed_AllImpl _value, $Res Function(_$Feed_AllImpl) _then)
      : super(_value, _then);

  /// Create a copy of Feed
  /// with the given fields replaced by the non-null parameter values.
}

/// @nodoc

class _$Feed_AllImpl extends Feed_All {
  const _$Feed_AllImpl() : super._();

  @override
  String toString() {
    return 'Feed.all()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$Feed_AllImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() home,
    required TResult Function() all,
    required TResult Function() popular,
    required TResult Function(String field0) subreddit,
  }) {
    return all();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? home,
    TResult? Function()? all,
    TResult? Function()? popular,
    TResult? Function(String field0)? subreddit,
  }) {
    return all?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? home,
    TResult Function()? all,
    TResult Function()? popular,
    TResult Function(String field0)? subreddit,
    required TResult orElse(),
  }) {
    if (all != null) {
      return all();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Feed_Home value) home,
    required TResult Function(Feed_All value) all,
    required TResult Function(Feed_Popular value) popular,
    required TResult Function(Feed_Subreddit value) subreddit,
  }) {
    return all(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Feed_Home value)? home,
    TResult? Function(Feed_All value)? all,
    TResult? Function(Feed_Popular value)? popular,
    TResult? Function(Feed_Subreddit value)? subreddit,
  }) {
    return all?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Feed_Home value)? home,
    TResult Function(Feed_All value)? all,
    TResult Function(Feed_Popular value)? popular,
    TResult Function(Feed_Subreddit value)? subreddit,
    required TResult orElse(),
  }) {
    if (all != null) {
      return all(this);
    }
    return orElse();
  }
}

abstract class Feed_All extends Feed {
  const factory Feed_All() = _$Feed_AllImpl;
  const Feed_All._() : super._();
}

/// @nodoc
abstract class _$$Feed_PopularImplCopyWith<$Res> {
  factory _$$Feed_PopularImplCopyWith(
          _$Feed_PopularImpl value, $Res Function(_$Feed_PopularImpl) then) =
      __$$Feed_PopularImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$Feed_PopularImplCopyWithImpl<$Res>
    extends _$FeedCopyWithImpl<$Res, _$Feed_PopularImpl>
    implements _$$Feed_PopularImplCopyWith<$Res> {
  __$$Feed_PopularImplCopyWithImpl(
      _$Feed_PopularImpl _value, $Res Function(_$Feed_PopularImpl) _then)
      : super(_value, _then);

  /// Create a copy of Feed
  /// with the given fields replaced by the non-null parameter values.
}

/// @nodoc

class _$Feed_PopularImpl extends Feed_Popular {
  const _$Feed_PopularImpl() : super._();

  @override
  String toString() {
    return 'Feed.popular()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$Feed_PopularImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() home,
    required TResult Function() all,
    required TResult Function() popular,
    required TResult Function(String field0) subreddit,
  }) {
    return popular();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? home,
    TResult? Function()? all,
    TResult? Function()? popular,
    TResult? Function(String field0)? subreddit,
  }) {
    return popular?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? home,
    TResult Function()? all,
    TResult Function()? popular,
    TResult Function(String field0)? subreddit,
    required TResult orElse(),
  }) {
    if (popular != null) {
      return popular();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Feed_Home value) home,
    required TResult Function(Feed_All value) all,
    required TResult Function(Feed_Popular value) popular,
    required TResult Function(Feed_Subreddit value) subreddit,
  }) {
    return popular(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Feed_Home value)? home,
    TResult? Function(Feed_All value)? all,
    TResult? Function(Feed_Popular value)? popular,
    TResult? Function(Feed_Subreddit value)? subreddit,
  }) {
    return popular?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Feed_Home value)? home,
    TResult Function(Feed_All value)? all,
    TResult Function(Feed_Popular value)? popular,
    TResult Function(Feed_Subreddit value)? subreddit,
    required TResult orElse(),
  }) {
    if (popular != null) {
      return popular(this);
    }
    return orElse();
  }
}

abstract class Feed_Popular extends Feed {
  const factory Feed_Popular() = _$Feed_PopularImpl;
  const Feed_Popular._() : super._();
}

/// @nodoc
abstract class _$$Feed_SubredditImplCopyWith<$Res> {
  factory _$$Feed_SubredditImplCopyWith(_$Feed_SubredditImpl value,
          $Res Function(_$Feed_SubredditImpl) then) =
      __$$Feed_SubredditImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String field0});
}

/// @nodoc
class __$$Feed_SubredditImplCopyWithImpl<$Res>
    extends _$FeedCopyWithImpl<$Res, _$Feed_SubredditImpl>
    implements _$$Feed_SubredditImplCopyWith<$Res> {
  __$$Feed_SubredditImplCopyWithImpl(
      _$Feed_SubredditImpl _value, $Res Function(_$Feed_SubredditImpl) _then)
      : super(_value, _then);

  /// Create a copy of Feed
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? field0 = null,
  }) {
    return _then(_$Feed_SubredditImpl(
      null == field0
          ? _value.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$Feed_SubredditImpl extends Feed_Subreddit {
  const _$Feed_SubredditImpl(this.field0) : super._();

  @override
  final String field0;

  @override
  String toString() {
    return 'Feed.subreddit(field0: $field0)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$Feed_SubredditImpl &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  /// Create a copy of Feed
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$Feed_SubredditImplCopyWith<_$Feed_SubredditImpl> get copyWith =>
      __$$Feed_SubredditImplCopyWithImpl<_$Feed_SubredditImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() home,
    required TResult Function() all,
    required TResult Function() popular,
    required TResult Function(String field0) subreddit,
  }) {
    return subreddit(field0);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? home,
    TResult? Function()? all,
    TResult? Function()? popular,
    TResult? Function(String field0)? subreddit,
  }) {
    return subreddit?.call(field0);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? home,
    TResult Function()? all,
    TResult Function()? popular,
    TResult Function(String field0)? subreddit,
    required TResult orElse(),
  }) {
    if (subreddit != null) {
      return subreddit(field0);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(Feed_Home value) home,
    required TResult Function(Feed_All value) all,
    required TResult Function(Feed_Popular value) popular,
    required TResult Function(Feed_Subreddit value) subreddit,
  }) {
    return subreddit(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(Feed_Home value)? home,
    TResult? Function(Feed_All value)? all,
    TResult? Function(Feed_Popular value)? popular,
    TResult? Function(Feed_Subreddit value)? subreddit,
  }) {
    return subreddit?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(Feed_Home value)? home,
    TResult Function(Feed_All value)? all,
    TResult Function(Feed_Popular value)? popular,
    TResult Function(Feed_Subreddit value)? subreddit,
    required TResult orElse(),
  }) {
    if (subreddit != null) {
      return subreddit(this);
    }
    return orElse();
  }
}

abstract class Feed_Subreddit extends Feed {
  const factory Feed_Subreddit(final String field0) = _$Feed_SubredditImpl;
  const Feed_Subreddit._() : super._();

  String get field0;

  /// Create a copy of Feed
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$Feed_SubredditImplCopyWith<_$Feed_SubredditImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
