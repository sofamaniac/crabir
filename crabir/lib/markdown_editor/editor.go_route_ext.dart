// dart format width=80
// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'editor.dart';

// **************************************************************************
// GoRouteDataGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
extension MarkdownEditorBuilder on MarkdownEditor {
  static const String name = 'MarkdownEditor';
  Map<String, String> get pathParams => {};
  Map<String, dynamic> get extra => {'controller': controller};

  void goNamed(BuildContext context) => context.goNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  void pushNamed(BuildContext context) => context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  static MarkdownEditor fromExtra(Map<String, dynamic> extra) {
    return MarkdownEditor(
        controller: extra['controller'] as TextEditingController?);
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
        'parentName': parentName
      };

  void goNamed(BuildContext context) => context.goNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  void pushNamed(BuildContext context) => context.pushNamed(
        name,
        pathParameters: pathParams,
        extra: extra,
      );

  static CommentEditor fromExtra(Map<String, dynamic> extra) {
    return CommentEditor(
        title: extra['title'] as String,
        author: extra['author'] as String,
        text: extra['text'] as String,
        parentName: extra['parentName'] as Fullname);
  }
}
