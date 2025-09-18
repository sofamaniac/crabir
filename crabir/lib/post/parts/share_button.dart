part of '../post.dart';

class ShareButton extends StatelessWidget {
  final Post post;
  const ShareButton({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return IconButton(
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
                      final params = ShareParams(uri: Uri.parse(post.url));
                      SharePlus.instance.share(params);
                    },
                  ),
                  ListTile(
                    leading: const Icon(Icons.text_snippet),
                    title: Text(locales.shareLinkTitle),
                    onTap: () {
                      Navigator.pop(ctx);
                      final params = ShareParams(
                        text: "${post.title} - ${post.url}",
                      );
                      SharePlus.instance.share(params);
                    },
                  ),
                  ListTile(
                    leading: const Icon(Icons.reddit),
                    title: Text(locales.sharePost),
                    onTap: () {
                      Navigator.pop(ctx);
                      final fullPermalink =
                          "https://reddit.com${post.permalink}";
                      final params = ShareParams(uri: Uri.parse(fullPermalink));
                      SharePlus.instance.share(params);
                    },
                  ),
                ],
              ),
            );
          },
        );
      },
    );
  }
}
