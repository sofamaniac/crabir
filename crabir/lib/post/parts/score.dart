part of '../post.dart';

class PostScore extends StatelessWidget {
  final Post post;
  const PostScore({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    // TODO: animate on `post.likes` change
    final locales = AppLocalizations.of(context);
    return RichText(
      text: TextSpan(
        children: [
          TextSpan(
            text: "${post.ups}",
            style: Theme.of(context).textTheme.titleMedium,
          ),
          TextSpan(text: ' • ', style: Theme.of(context).textTheme.bodySmall),
          TextSpan(
            text: locales.commentsNumbered(post.numComments),
            style: Theme.of(context).textTheme.bodySmall,
          ),
          if (post.over18) WidgetSpan(child: NsfwTag()),
        ],
      ),
    );
  }
}
