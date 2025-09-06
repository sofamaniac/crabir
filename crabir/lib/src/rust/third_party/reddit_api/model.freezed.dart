// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$Thing {
  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Thing);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'Thing()';
  }
}

/// @nodoc
class $ThingCopyWith<$Res> {
  $ThingCopyWith(Thing _, $Res Function(Thing) __);
}

/// Adds pattern-matching-related methods to [Thing].
extension ThingPatterns on Thing {
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
    TResult Function(Thing_Listing value)? listing,
    TResult Function(Thing_Comment value)? comment,
    TResult Function(Thing_User value)? user,
    TResult Function(Thing_Post value)? post,
    TResult Function(Thing_Message value)? message,
    TResult Function(Thing_Subreddit value)? subreddit,
    TResult Function(Thing_Award value)? award,
    TResult Function(Thing_Multi value)? multi,
    TResult Function(Thing_More value)? more,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Thing_Listing() when listing != null:
        return listing(_that);
      case Thing_Comment() when comment != null:
        return comment(_that);
      case Thing_User() when user != null:
        return user(_that);
      case Thing_Post() when post != null:
        return post(_that);
      case Thing_Message() when message != null:
        return message(_that);
      case Thing_Subreddit() when subreddit != null:
        return subreddit(_that);
      case Thing_Award() when award != null:
        return award(_that);
      case Thing_Multi() when multi != null:
        return multi(_that);
      case Thing_More() when more != null:
        return more(_that);
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
    required TResult Function(Thing_Listing value) listing,
    required TResult Function(Thing_Comment value) comment,
    required TResult Function(Thing_User value) user,
    required TResult Function(Thing_Post value) post,
    required TResult Function(Thing_Message value) message,
    required TResult Function(Thing_Subreddit value) subreddit,
    required TResult Function(Thing_Award value) award,
    required TResult Function(Thing_Multi value) multi,
    required TResult Function(Thing_More value) more,
  }) {
    final _that = this;
    switch (_that) {
      case Thing_Listing():
        return listing(_that);
      case Thing_Comment():
        return comment(_that);
      case Thing_User():
        return user(_that);
      case Thing_Post():
        return post(_that);
      case Thing_Message():
        return message(_that);
      case Thing_Subreddit():
        return subreddit(_that);
      case Thing_Award():
        return award(_that);
      case Thing_Multi():
        return multi(_that);
      case Thing_More():
        return more(_that);
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
    TResult? Function(Thing_Listing value)? listing,
    TResult? Function(Thing_Comment value)? comment,
    TResult? Function(Thing_User value)? user,
    TResult? Function(Thing_Post value)? post,
    TResult? Function(Thing_Message value)? message,
    TResult? Function(Thing_Subreddit value)? subreddit,
    TResult? Function(Thing_Award value)? award,
    TResult? Function(Thing_Multi value)? multi,
    TResult? Function(Thing_More value)? more,
  }) {
    final _that = this;
    switch (_that) {
      case Thing_Listing() when listing != null:
        return listing(_that);
      case Thing_Comment() when comment != null:
        return comment(_that);
      case Thing_User() when user != null:
        return user(_that);
      case Thing_Post() when post != null:
        return post(_that);
      case Thing_Message() when message != null:
        return message(_that);
      case Thing_Subreddit() when subreddit != null:
        return subreddit(_that);
      case Thing_Award() when award != null:
        return award(_that);
      case Thing_Multi() when multi != null:
        return multi(_that);
      case Thing_More() when more != null:
        return more(_that);
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
    TResult Function(Listing field0)? listing,
    TResult Function(Comment field0)? comment,
    TResult Function(User field0)? user,
    TResult Function(Post field0)? post,
    TResult Function()? message,
    TResult Function(Subreddit field0)? subreddit,
    TResult Function()? award,
    TResult Function(Multi field0)? multi,
    TResult Function(String id, Fullname name, int count, int depth,
            List<String> children)?
        more,
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case Thing_Listing() when listing != null:
        return listing(_that.field0);
      case Thing_Comment() when comment != null:
        return comment(_that.field0);
      case Thing_User() when user != null:
        return user(_that.field0);
      case Thing_Post() when post != null:
        return post(_that.field0);
      case Thing_Message() when message != null:
        return message();
      case Thing_Subreddit() when subreddit != null:
        return subreddit(_that.field0);
      case Thing_Award() when award != null:
        return award();
      case Thing_Multi() when multi != null:
        return multi(_that.field0);
      case Thing_More() when more != null:
        return more(
            _that.id, _that.name, _that.count, _that.depth, _that.children);
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
    required TResult Function(Listing field0) listing,
    required TResult Function(Comment field0) comment,
    required TResult Function(User field0) user,
    required TResult Function(Post field0) post,
    required TResult Function() message,
    required TResult Function(Subreddit field0) subreddit,
    required TResult Function() award,
    required TResult Function(Multi field0) multi,
    required TResult Function(String id, Fullname name, int count, int depth,
            List<String> children)
        more,
  }) {
    final _that = this;
    switch (_that) {
      case Thing_Listing():
        return listing(_that.field0);
      case Thing_Comment():
        return comment(_that.field0);
      case Thing_User():
        return user(_that.field0);
      case Thing_Post():
        return post(_that.field0);
      case Thing_Message():
        return message();
      case Thing_Subreddit():
        return subreddit(_that.field0);
      case Thing_Award():
        return award();
      case Thing_Multi():
        return multi(_that.field0);
      case Thing_More():
        return more(
            _that.id, _that.name, _that.count, _that.depth, _that.children);
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
    TResult? Function(Listing field0)? listing,
    TResult? Function(Comment field0)? comment,
    TResult? Function(User field0)? user,
    TResult? Function(Post field0)? post,
    TResult? Function()? message,
    TResult? Function(Subreddit field0)? subreddit,
    TResult? Function()? award,
    TResult? Function(Multi field0)? multi,
    TResult? Function(String id, Fullname name, int count, int depth,
            List<String> children)?
        more,
  }) {
    final _that = this;
    switch (_that) {
      case Thing_Listing() when listing != null:
        return listing(_that.field0);
      case Thing_Comment() when comment != null:
        return comment(_that.field0);
      case Thing_User() when user != null:
        return user(_that.field0);
      case Thing_Post() when post != null:
        return post(_that.field0);
      case Thing_Message() when message != null:
        return message();
      case Thing_Subreddit() when subreddit != null:
        return subreddit(_that.field0);
      case Thing_Award() when award != null:
        return award();
      case Thing_Multi() when multi != null:
        return multi(_that.field0);
      case Thing_More() when more != null:
        return more(
            _that.id, _that.name, _that.count, _that.depth, _that.children);
      case _:
        return null;
    }
  }
}

/// @nodoc

class Thing_Listing extends Thing {
  const Thing_Listing(this.field0) : super._();

  final Listing field0;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Thing_ListingCopyWith<Thing_Listing> get copyWith =>
      _$Thing_ListingCopyWithImpl<Thing_Listing>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Thing_Listing &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'Thing.listing(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $Thing_ListingCopyWith<$Res>
    implements $ThingCopyWith<$Res> {
  factory $Thing_ListingCopyWith(
          Thing_Listing value, $Res Function(Thing_Listing) _then) =
      _$Thing_ListingCopyWithImpl;
  @useResult
  $Res call({Listing field0});
}

/// @nodoc
class _$Thing_ListingCopyWithImpl<$Res>
    implements $Thing_ListingCopyWith<$Res> {
  _$Thing_ListingCopyWithImpl(this._self, this._then);

  final Thing_Listing _self;
  final $Res Function(Thing_Listing) _then;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(Thing_Listing(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Listing,
    ));
  }
}

/// @nodoc

class Thing_Comment extends Thing {
  const Thing_Comment(this.field0) : super._();

  final Comment field0;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Thing_CommentCopyWith<Thing_Comment> get copyWith =>
      _$Thing_CommentCopyWithImpl<Thing_Comment>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Thing_Comment &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'Thing.comment(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $Thing_CommentCopyWith<$Res>
    implements $ThingCopyWith<$Res> {
  factory $Thing_CommentCopyWith(
          Thing_Comment value, $Res Function(Thing_Comment) _then) =
      _$Thing_CommentCopyWithImpl;
  @useResult
  $Res call({Comment field0});
}

/// @nodoc
class _$Thing_CommentCopyWithImpl<$Res>
    implements $Thing_CommentCopyWith<$Res> {
  _$Thing_CommentCopyWithImpl(this._self, this._then);

  final Thing_Comment _self;
  final $Res Function(Thing_Comment) _then;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(Thing_Comment(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Comment,
    ));
  }
}

/// @nodoc

class Thing_User extends Thing {
  const Thing_User(this.field0) : super._();

  final User field0;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Thing_UserCopyWith<Thing_User> get copyWith =>
      _$Thing_UserCopyWithImpl<Thing_User>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Thing_User &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'Thing.user(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $Thing_UserCopyWith<$Res> implements $ThingCopyWith<$Res> {
  factory $Thing_UserCopyWith(
          Thing_User value, $Res Function(Thing_User) _then) =
      _$Thing_UserCopyWithImpl;
  @useResult
  $Res call({User field0});
}

/// @nodoc
class _$Thing_UserCopyWithImpl<$Res> implements $Thing_UserCopyWith<$Res> {
  _$Thing_UserCopyWithImpl(this._self, this._then);

  final Thing_User _self;
  final $Res Function(Thing_User) _then;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(Thing_User(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as User,
    ));
  }
}

/// @nodoc

class Thing_Post extends Thing {
  const Thing_Post(this.field0) : super._();

  final Post field0;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Thing_PostCopyWith<Thing_Post> get copyWith =>
      _$Thing_PostCopyWithImpl<Thing_Post>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Thing_Post &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'Thing.post(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $Thing_PostCopyWith<$Res> implements $ThingCopyWith<$Res> {
  factory $Thing_PostCopyWith(
          Thing_Post value, $Res Function(Thing_Post) _then) =
      _$Thing_PostCopyWithImpl;
  @useResult
  $Res call({Post field0});
}

/// @nodoc
class _$Thing_PostCopyWithImpl<$Res> implements $Thing_PostCopyWith<$Res> {
  _$Thing_PostCopyWithImpl(this._self, this._then);

  final Thing_Post _self;
  final $Res Function(Thing_Post) _then;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(Thing_Post(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Post,
    ));
  }
}

/// @nodoc

class Thing_Message extends Thing {
  const Thing_Message() : super._();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Thing_Message);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'Thing.message()';
  }
}

/// @nodoc

class Thing_Subreddit extends Thing {
  const Thing_Subreddit(this.field0) : super._();

  final Subreddit field0;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Thing_SubredditCopyWith<Thing_Subreddit> get copyWith =>
      _$Thing_SubredditCopyWithImpl<Thing_Subreddit>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Thing_Subreddit &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'Thing.subreddit(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $Thing_SubredditCopyWith<$Res>
    implements $ThingCopyWith<$Res> {
  factory $Thing_SubredditCopyWith(
          Thing_Subreddit value, $Res Function(Thing_Subreddit) _then) =
      _$Thing_SubredditCopyWithImpl;
  @useResult
  $Res call({Subreddit field0});
}

/// @nodoc
class _$Thing_SubredditCopyWithImpl<$Res>
    implements $Thing_SubredditCopyWith<$Res> {
  _$Thing_SubredditCopyWithImpl(this._self, this._then);

  final Thing_Subreddit _self;
  final $Res Function(Thing_Subreddit) _then;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(Thing_Subreddit(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Subreddit,
    ));
  }
}

/// @nodoc

class Thing_Award extends Thing {
  const Thing_Award() : super._();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is Thing_Award);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  String toString() {
    return 'Thing.award()';
  }
}

/// @nodoc

class Thing_Multi extends Thing {
  const Thing_Multi(this.field0) : super._();

  final Multi field0;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Thing_MultiCopyWith<Thing_Multi> get copyWith =>
      _$Thing_MultiCopyWithImpl<Thing_Multi>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Thing_Multi &&
            (identical(other.field0, field0) || other.field0 == field0));
  }

  @override
  int get hashCode => Object.hash(runtimeType, field0);

  @override
  String toString() {
    return 'Thing.multi(field0: $field0)';
  }
}

/// @nodoc
abstract mixin class $Thing_MultiCopyWith<$Res>
    implements $ThingCopyWith<$Res> {
  factory $Thing_MultiCopyWith(
          Thing_Multi value, $Res Function(Thing_Multi) _then) =
      _$Thing_MultiCopyWithImpl;
  @useResult
  $Res call({Multi field0});
}

/// @nodoc
class _$Thing_MultiCopyWithImpl<$Res> implements $Thing_MultiCopyWith<$Res> {
  _$Thing_MultiCopyWithImpl(this._self, this._then);

  final Thing_Multi _self;
  final $Res Function(Thing_Multi) _then;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? field0 = null,
  }) {
    return _then(Thing_Multi(
      null == field0
          ? _self.field0
          : field0 // ignore: cast_nullable_to_non_nullable
              as Multi,
    ));
  }
}

/// @nodoc

class Thing_More extends Thing {
  const Thing_More(
      {required this.id,
      required this.name,
      required this.count,
      required this.depth,
      required final List<String> children})
      : _children = children,
        super._();

  /// Has the same id as its first children
  /// If there is no children is "_".
  final String id;

  /// Name of the first children. If there is none is "t1__".
  final Fullname name;
  final int count;
  final int depth;
  final List<String> _children;
  List<String> get children {
    if (_children is EqualUnmodifiableListView) return _children;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_children);
  }

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $Thing_MoreCopyWith<Thing_More> get copyWith =>
      _$Thing_MoreCopyWithImpl<Thing_More>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Thing_More &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.count, count) || other.count == count) &&
            (identical(other.depth, depth) || other.depth == depth) &&
            const DeepCollectionEquality().equals(other._children, _children));
  }

  @override
  int get hashCode => Object.hash(runtimeType, id, name, count, depth,
      const DeepCollectionEquality().hash(_children));

  @override
  String toString() {
    return 'Thing.more(id: $id, name: $name, count: $count, depth: $depth, children: $children)';
  }
}

/// @nodoc
abstract mixin class $Thing_MoreCopyWith<$Res> implements $ThingCopyWith<$Res> {
  factory $Thing_MoreCopyWith(
          Thing_More value, $Res Function(Thing_More) _then) =
      _$Thing_MoreCopyWithImpl;
  @useResult
  $Res call(
      {String id, Fullname name, int count, int depth, List<String> children});
}

/// @nodoc
class _$Thing_MoreCopyWithImpl<$Res> implements $Thing_MoreCopyWith<$Res> {
  _$Thing_MoreCopyWithImpl(this._self, this._then);

  final Thing_More _self;
  final $Res Function(Thing_More) _then;

  /// Create a copy of Thing
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  $Res call({
    Object? id = null,
    Object? name = null,
    Object? count = null,
    Object? depth = null,
    Object? children = null,
  }) {
    return _then(Thing_More(
      id: null == id
          ? _self.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as Fullname,
      count: null == count
          ? _self.count
          : count // ignore: cast_nullable_to_non_nullable
              as int,
      depth: null == depth
          ? _self.depth
          : depth // ignore: cast_nullable_to_non_nullable
              as int,
      children: null == children
          ? _self._children
          : children // ignore: cast_nullable_to_non_nullable
              as List<String>,
    ));
  }
}

// dart format on
