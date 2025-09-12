import 'package:auto_route/auto_route.dart';
import 'package:crabir/bool_to_vote.dart';
import 'package:crabir/buttons.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/gallery/gallery.dart';
import 'package:crabir/post/widget/html_with_fade.dart';
import 'package:crabir/post/widget/image.dart';
import 'package:crabir/post/widget/video.dart';
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

part 'title.dart';
part 'header.dart';
part 'footer.dart';

final horizontalPadding = 16.0;

class NsfwTag extends StatelessWidget {
  const NsfwTag({super.key});
  @override
  Widget build(BuildContext context) {
    return Cartouche(
      "NSFW",
      background: Colors.red,
    );
  }
}

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
    return Padding(padding: EdgeInsets.symmetric(horizontal: 4), child: widget);
  }

  bool showThumbnail() {
    return switch (post.kind) {
      Kind.link || Kind.unknown => true,
      Kind.selftext || Kind.meta => false,
      _ => !showMedia,
    };
  }

  Widget title(BuildContext context) {
    return PostTitle(
      post: post,
      showThumbnail: showThumbnail(),
    );
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
      Kind.meta => Text("meta"),
      Kind.video => VideoContent(post: post),
      Kind.youtubeVideo when showMedia => YoutubeContent(post: post),
      (Kind.mediaGallery || Kind.gallery) when showMedia =>
        GalleryView(post: post),
      Kind.image when showMedia => ImageContent(
          post: post,
        ),
      Kind.link || Kind.unknown => Container(),
      Kind.selftext || _ => HtmlWithConditionalFade(
          htmlContent: post.selftextHtml ?? "",
          maxLines: maxLines,
        ),
    };

    return _contentWrap(widget);
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

class DenseCard extends RedditPostCard {
  const DenseCard(
      {super.key, required super.post, super.onSave, super.onLike, super.onTap})
      : super(showMedia: false);
  @override
  Widget build(BuildContext context) {
    final contentWidget = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      spacing: 8,
      children: [
        wrap(
          PostTitle(
            post: post,
            showThumbnail: showThumbnail(),
          ),
        ),
        wrap(
          Header(
            post: post,
            showSubredditIcon: false,
          ),
        ),
        //content(context),
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
