// GENERATED CODE - DO NOT MODIFY BY HAND
// dart format width=80

part of 'editor.dart';

// **************************************************************************
// GoRouteDataGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
extension MarkdownEditorBuilder on MarkdownEditor {
  static const String name = 'MarkdownEditor';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra => {
        'controller': controller,
        'maxLines': maxLines,
        'minLines': minLines,
        'hint': hint,
        'onChanged': onChanged
      };

  void goNamed(BuildContext context) => context.goNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  Future<T?> pushNamed<T extends Object?>(BuildContext context) =>
      context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  static MarkdownEditor fromExtra(Map<String, dynamic> extra) {
    return MarkdownEditor(
        controller: extra['controller'] as TextEditingController?,
        maxLines: extra['maxLines'] as int?,
        minLines: extra['minLines'] as int?,
        hint: extra['hint'] as String?,
        onChanged: extra['onChanged'] as void Function(String)?);
  }
}

// GENERATED CODE - DO NOT MODIFY BY HAND
extension CommentEditorBuilder on CommentEditor {
  static const String name = 'CommentEditor';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra => {
        'title': title,
        'author': author,
        'text': text,
        'parentName': parentName,
        'depth': depth
      };

  void goNamed(BuildContext context) => context.goNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  Future<T?> pushNamed<T extends Object?>(BuildContext context) =>
      context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  static CommentEditor fromExtra(Map<String, dynamic> extra) {
    return CommentEditor(
        title: extra['title'] as String,
        author: extra['author'] as String,
        text: extra['text'] as String,
        parentName: extra['parentName'] as Fullname,
        depth: extra['depth'] as int);
  }
}

// GENERATED CODE - DO NOT MODIFY BY HAND
extension CrosspostEditorBuilder on CrosspostEditor {
  static const String name = 'CrosspostEditor';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra => {'post': post};

  void goNamed(BuildContext context) => context.goNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  Future<T?> pushNamed<T extends Object?>(BuildContext context) =>
      context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  static CrosspostEditor fromExtra(Map<String, dynamic> extra) {
    return CrosspostEditor(post: extra['post'] as Post);
  }
}
