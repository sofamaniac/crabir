part of 'thread.dart';

class ShareButton extends StatelessWidget {
  final Comment comment;
  final VoidCallback? onPressed;
  const ShareButton({super.key, required this.comment, this.onPressed});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return IconButton(
      color: Colors.grey,
      icon: const Icon(Icons.share),
      onPressed: () {
        showModalBottomSheet(
          context: context,
          builder: (ctx) {
            return SafeArea(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  ListTile(
                    leading: const Icon(Icons.link),
                    title: Text(locales.shareLink),
                    onTap: () {
                      Navigator.pop(ctx);
                      final fullPermalink =
                          "https://reddit.com${comment.permalink}";
                      final params = ShareParams(uri: Uri.parse(fullPermalink));
                      SharePlus.instance.share(params);
                    },
                  ),
                  ListTile(
                    leading: const Icon(Icons.list),
                    title: Text(locales.shareText),
                    onTap: () {
                      Navigator.pop(ctx);
                      final params = ShareParams(text: comment.body);
                      SharePlus.instance.share(params);
                    },
                  ),
                ],
              ),
            );
          },
        );
        onPressed?.call();
      },
    );
  }
}
