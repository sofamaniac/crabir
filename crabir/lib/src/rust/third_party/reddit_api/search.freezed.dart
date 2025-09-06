// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'search.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$PostSearchSort {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is PostSearchSort);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'PostSearchSort()';
  }
}

/// @nodoc
class $PostSearchSortCopyWith<$Res> {
  $PostSearchSortCopyWith(PostSearchSort _, $Res Function(PostSearchSort) __);
}

/// Adds pattern-matching-related methods to [PostSearchSort].
extension PostSearchSortPatterns on PostSearchSort {
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
    TResult Function(PostSearchSort_Relevance value)? relevance,
    TResult Function(PostSearchSort_Hot value)? hot,
    TResult Function(PostSearchSort_Top value)? top,
    TResult Function(PostSearchSort_New value)? new_,
    TResult Function(PostSearchSort_Comments value)? comments,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case PostSearchSort_Relevance() when relevance != null:
        return relevance(_that);
      case PostSearchSort_Hot() when hot != null:
        return hot(_that);
      case PostSearchSort_Top() when top != null:
        return top(_that);
      case PostSearchSort_New() when new_ != null:
        return new_(_that);
      case PostSearchSort_Comments() when comments != null:
        return comments(_that);
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
    required TResult Function(PostSearchSort_Relevance value) relevance,
    required TResult Function(PostSearchSort_Hot value) hot,
    required TResult Function(PostSearchSort_Top value) top,
    required TResult Function(PostSearchSort_New value) new_,
    required TResult Function(PostSearchSort_Comments value) comments,
  }) {
    final _that = this;
    switch (_that) {
      case PostSearchSort_Relevance():
        return relevance(_that);
      case PostSearchSort_Hot():
        return hot(_that);
      case PostSearchSort_Top():
        return top(_that);
      case PostSearchSort_New():
        return new_(_that);
      case PostSearchSort_Comments():
        return comments(_that);
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
    TResult? Function(PostSearchSort_Relevance value)? relevance,
    TResult? Function(PostSearchSort_Hot value)? hot,
    TResult? Function(PostSearchSort_Top value)? top,
    TResult? Function(PostSearchSort_New value)? new_,
    TResult? Function(PostSearchSort_Comments value)? comments,
  }) {
    final _that = this;
    switch (_that) {
      case PostSearchSort_Relevance() when relevance != null:
        return relevance(_that);
      case PostSearchSort_Hot() when hot != null:
        return hot(_that);
      case PostSearchSort_Top() when top != null:
        return top(_that);
      case PostSearchSort_New() when new_ != null:
        return new_(_that);
      case PostSearchSort_Comments() when comments != null:
        return comments(_that);
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
    TResult Function(Timeframe field0)? relevance,
    TResult Function()? hot,
    TResult Function(Timeframe field0)? top,
    TResult Function()? new_,
    TResult Function(Timeframe field0)? comments,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case PostSearchSort_Relevance() when relevance != null:
        return relevance(_that.field0);
      case PostSearchSort_Hot() when hot != null:
        return hot();
      case PostSearchSort_Top() when top != null:
        return top(_that.field0);
      case PostSearchSort_New() when new_ != null:
        return new_();
      case PostSearchSort_Comments() when comments != null:
        return comments(_that.field0);
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
    required TResult Function(Timeframe field0) relevance,
    required TResult Function() hot,
    required TResult Function(Timeframe field0) top,
    required TResult Function() new_,
    required TResult Function(Timeframe field0) comments,
  }) {
    final _that = this;
    switch (_that) {
      case PostSearchSort_Relevance():
        return relevance(_that.field0);
      case PostSearchSort_Hot():
        return hot();
      case PostSearchSort_Top():
        return top(_that.field0);
      case PostSearchSort_New():
        return new_();
      case PostSearchSort_Comments():
        return comments(_that.field0);
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
    TResult? Function(Timeframe field0)? relevance,
    TResult? Function()? hot,
    TResult? Function(Timeframe field0)? top,
    TResult? Function()? new_,
    TResult? Function(Timeframe field0)? comments,
  }) {
    final _that = this;
    switch (_that) {
      case PostSearchSort_Relevance() when relevance != null:
        return relevance(_that.field0);
      case PostSearchSort_Hot() when hot != null:
        return hot();
      case PostSearchSort_Top() when top != null:
        return top(_that.field0);
      case PostSearchSort_New() when new_ != null:
        return new_();
      case PostSearchSort_Comments() when comments != null:
        return comments(_that.field0);
      case _:
        return null;
    }
  }
}

/// @nodoc

class PostSearchSort_Relevance extends PostSearchSort {
  const PostSearchSort_Relevance(this.field0) : super._();

  final Timeframe field0;

  /// Create a copy of PostSearchSort
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $PostSearchSort_RelevanceCopyWith<PostSearchSort_Relevance> get copyWith =>
      _$PostSearchSort_RelevanceCopyWithImpl<PostSearchSort_Relevance>(
          this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is PostSearchSort_Relevance &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'PostSearchSort.relevance(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $PostSearchSort_RelevanceCopyWith<$Res>
    implements $PostSearchSortCopyWith<$Res> {
  factory $PostSearchSort_RelevanceCopyWith(PostSearchSort_Relevance value,
          $Res Function(PostSearchSort_Relevance) _then) =
      _$PostSearchSort_RelevanceCopyWithImpl;
  @useResult
  $Res call({Timeframe field0});
}

/// @nodoc
class _$PostSearchSort_RelevanceCopyWithImpl<$Res>
    implements $PostSearchSort_RelevanceCopyWith<$Res> {
  _$PostSearchSort_RelevanceCopyWithImpl(this._self, this._then);

  final PostSearchSort_Relevance _self;
  final $Res Function(PostSearchSort_Relevance) _then;

  /// Create a copy of PostSearchSort
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(PostSearchSort_Relevance(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Timeframe,
    ));
  }
}

/// @nodoc

class PostSearchSort_Hot extends PostSearchSort {
  const PostSearchSort_Hot() : super._();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is PostSearchSort_Hot);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'PostSearchSort.hot()';
  }
}

/// @nodoc

class PostSearchSort_Top extends PostSearchSort {
  const PostSearchSort_Top(this.field0) : super._();

  final Timeframe field0;

  /// Create a copy of PostSearchSort
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $PostSearchSort_TopCopyWith<PostSearchSort_Top> get copyWith =>
      _$PostSearchSort_TopCopyWithImpl<PostSearchSort_Top>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is PostSearchSort_Top &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'PostSearchSort.top(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $PostSearchSort_TopCopyWith<$Res>
    implements $PostSearchSortCopyWith<$Res> {
  factory $PostSearchSort_TopCopyWith(
          PostSearchSort_Top value, $Res Function(PostSearchSort_Top) _then) =
      _$PostSearchSort_TopCopyWithImpl;
  @useResult
  $Res call({Timeframe field0});
}

/// @nodoc
class _$PostSearchSort_TopCopyWithImpl<$Res>
    implements $PostSearchSort_TopCopyWith<$Res> {
  _$PostSearchSort_TopCopyWithImpl(this._self, this._then);

  final PostSearchSort_Top _self;
  final $Res Function(PostSearchSort_Top) _then;

  /// Create a copy of PostSearchSort
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(PostSearchSort_Top(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Timeframe,
    ));
  }
}

/// @nodoc

class PostSearchSort_New extends PostSearchSort {
  const PostSearchSort_New() : super._();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is PostSearchSort_New);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'PostSearchSort.new_()';
  }
}

/// @nodoc

class PostSearchSort_Comments extends PostSearchSort {
  const PostSearchSort_Comments(this.field0) : super._();

  final Timeframe field0;

  /// Create a copy of PostSearchSort
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $PostSearchSort_CommentsCopyWith<PostSearchSort_Comments> get copyWith =>
      _$PostSearchSort_CommentsCopyWithImpl<PostSearchSort_Comments>(
          this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is PostSearchSort_Comments &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'PostSearchSort.comments(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $PostSearchSort_CommentsCopyWith<$Res>
    implements $PostSearchSortCopyWith<$Res> {
  factory $PostSearchSort_CommentsCopyWith(PostSearchSort_Comments value,
          $Res Function(PostSearchSort_Comments) _then) =
      _$PostSearchSort_CommentsCopyWithImpl;
  @useResult
  $Res call({Timeframe field0});
}

/// @nodoc
class _$PostSearchSort_CommentsCopyWithImpl<$Res>
    implements $PostSearchSort_CommentsCopyWith<$Res> {
  _$PostSearchSort_CommentsCopyWithImpl(this._self, this._then);

  final PostSearchSort_Comments _self;
  final $Res Function(PostSearchSort_Comments) _then;

  /// Create a copy of PostSearchSort
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(PostSearchSort_Comments(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Timeframe,
    ));
  }
}

// dart format on
