part of '../post.dart';

class PostScore extends StatelessWidget {
  final Post post;
  const PostScore({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final color = CrabirTheme.of(context).alternativeText;
    final spacer = const WidgetSpan(child: SizedBox(width: 8));
    return RichText(
      text: TextSpan(
        children: [
          WidgetSpan(
            alignment: PlaceholderAlignment.middle,
            child: LikeText(
              score: post.ups,
              likes: post.likes,
              style: Theme.of(context).textTheme.titleMedium!,
              scaling: 1.5,
            ),
          ),
          spacer,
          TextSpan(
              text: '•',
              style: Theme.of(context)
                  .textTheme
                  .bodySmall!
                  .copyWith(color: color)),
          spacer,
          TextSpan(
            text: locales.commentsNumbered(post.numComments),
            style:
                Theme.of(context).textTheme.bodySmall!.copyWith(color: color),
          ),
          if (post.over18) ...[
            spacer,
            WidgetSpan(
              alignment: PlaceholderAlignment.middle,
              child: NsfwTag(),
            ),
          ],
        ],
      ),
    );
  }
}
