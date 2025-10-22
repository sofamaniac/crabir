part of '../post.dart';

shareModelBottomSheet(BuildContext context, Post post) {
  final locales = AppLocalizations.of(context);

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
                final fullPermalink = "https://reddit.com${post.permalink}";
                final params = ShareParams(uri: Uri.parse(fullPermalink));
                SharePlus.instance.share(params);
              },
            ),
            ListTile(
              leading: const RotatedBox(
                quarterTurns: 1,
                child: Icon(
                  Icons.alt_route,
                ),
              ),
              title: Text("Crosspost"),
              onTap: () => CrosspostEditor(post: post).pushNamed(ctx),
            ),
            if (kDebugMode)
              ListTile(
                leading: Icon(Icons.bug_report),
                title: Text("Print post debug info"),
                onTap: () => debugPost(post: post),
              )
          ],
        ),
      );
    },
  );
}

class ShareButton extends LongShortButton {
  const ShareButton({super.key, required super.post, required super.short});

  @override
  String label(BuildContext context) {
    return AppLocalizations.of(context).shareButtonLabel;
  }

  @override
  void onTap(BuildContext context) {
    if (!short) {
      Navigator.pop(context);
    }
    shareModelBottomSheet(context, post);
  }

  @override
  Widget icon(BuildContext context) {
    return Icon(Icons.share);
  }
}
