import 'package:auto_route/auto_route.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/bool_to_vote.dart';
import 'package:crabir/buttons.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/gallery/gallery.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/post/parts/html_with_fade.dart';
import 'package:crabir/post/parts/image.dart';
import 'package:crabir/post/parts/video.dart';
import 'package:crabir/flair.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/subreddit.dart';
import 'package:crabir/time_ellapsed.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:url_launcher/url_launcher.dart';

part 'parts/title.dart';
part 'parts/header.dart';
part 'parts/footer.dart';
part 'parts/score.dart';
part 'parts/tags.dart';
part 'parts/separated_row.dart';
part 'parts/thumbnail.dart';
part 'dense_card.dart';

class RedditPostCard extends StatelessWidget {
  final Post post;

  final Future<void> Function(bool)? onSave;
  final Future<void> Function(VoteDirection)? onLike;
  final VoidCallback? onTap;
  final int? maxLines;

  /// Whether to show media in full size.
  /// If set to false, show a thumbnail instead.
  final bool showMedia;

  const RedditPostCard({
    super.key,
    required this.post,
    this.onLike,
    this.onSave,
    this.onTap,
    this.maxLines,
    this.showMedia = true,
  });

  Widget wrap(Widget widget) {
    return Padding(
        padding: EdgeInsets.symmetric(horizontal: 16), child: widget);
  }

  bool showThumbnail() {
    return switch (post.kind) {
      Kind.link || Kind.unknown => true,
      Kind.selftext || Kind.meta => false,
      _ => !showMedia,
    };
  }

  Widget title(BuildContext context) {
    final title = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        PostTitle(post: post),
        flair(context),
        PostScore(post: post),
      ],
    );
    if (showThumbnail()) {
      return Thumbnail(
        post: post,
        child: title,
      );
    }
    return title;
  }

  Widget _contentWrap(Widget widget) {
    return switch (post.kind) {
      Kind.image ||
      Kind.gallery ||
      Kind.video ||
      Kind.youtubeVideo ||
      Kind.mediaGallery =>
        widget,
      _ => wrap(widget),
    };
  }

  Widget content(BuildContext context) {
    final widget = switch (post.kind) {
      // Media types
      Kind.video when showMedia => VideoContent(post: post),
      Kind.youtubeVideo when showMedia => YoutubeContent(post: post),
      (Kind.mediaGallery || Kind.gallery) when showMedia =>
        GalleryView(post: post),
      Kind.image when showMedia => ImageContent(
          post: post,
        ),
      // Text types
      Kind.meta => Text("meta"),
      Kind.link || Kind.unknown => Container(),
      Kind.selftext || _ => HtmlWithConditionalFade(
          htmlContent: post.selftextHtml ?? "",
          maxLines: maxLines,
        ),
    };

    return _contentWrap(widget);
  }

  Widget flair(BuildContext context) {
    final settings = context.read<PostsSettingsCubit>().state;
    final Widget flairWidget;
    if (settings.tapFlairToSearch) {
      flairWidget = InkWell(
        onTap: () => context.router.root.navigate(
          SearchPostsRoute(
            // provide key to differentiate the pages
            // but prevents rebuilding when clicking the same flair again
            key: ValueKey("${post.subreddit.subreddit}-${post.linkFlair}"),
            subreddit: post.subreddit.subreddit,
            flair: post.linkFlair,
          ),
        ),
        child: FlairView(flair: post.linkFlair),
      );
    } else {
      flairWidget = FlairView(flair: post.linkFlair);
    }

    if (post.spoiler) {
      return Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        spacing: 8,
        children: [SpoilerTag(), flairWidget],
      );
    } else {
      return flairWidget;
    }
  }

  @override
  Widget build(BuildContext context) {
    final contentWidget = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      spacing: 8,
      children: [
        wrap(Header(post: post)),
        wrap(title(context)),
        content(context),
        wrap(
          Footer(
            post: post,
            onLike: onLike,
            onSave: onSave,
          ),
        ),
      ],
    );
    final theme = context.watch<ThemeBloc>().state;
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      color: theme.background,
      elevation: 1,
      child: InkWell(
        onTap: onTap,
        child: contentWidget,
      ),
    );
  }
}
