part of 'comments_settings.dart';

class _CommentsSortSelection extends StatelessWidget {
  final CommentSort value;
  final Widget title;
  final Widget? subtitle;
  final Widget? leading;
  final void Function(CommentSort) onChanged;

  const _CommentsSortSelection({
    required this.value,
    required this.onChanged,
    required this.title,
    this.subtitle,
    this.leading,
  });

  Future<CommentSort?> _showSelectionDialogue(BuildContext context) async {
    final result = await showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text("Select sort"),
            actions: commentSorts
                .map((sort) => ListTile(
                      title: Text(sort.label(context)),
                      onTap: () => Navigator.pop(context, sort),
                    ))
                .toList(),
          );
        });
    return result;
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: title,
      leading: leading,
      subtitle: Text(value.label(context)),
      onTap: () async {
        final sort = await _showSelectionDialogue(context);
        if (sort != null) {
          onChanged(sort);
        }
      },
    );
  }
}
