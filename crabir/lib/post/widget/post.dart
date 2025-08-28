import 'package:auto_route/auto_route.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/feed_list.dart';
import 'package:crabir/post/widget/gallery.dart';
import 'package:crabir/post/widget/html_with_fade.dart';
import 'package:crabir/post/widget/image.dart';
import 'package:crabir/post/widget/video.dart';
import 'package:crabir/flair.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/subreddit.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/time_ellapsed.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:crabir/bool_to_vote.dart';

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

class _PostTitle extends StatelessWidget {
  final Post post;

  final bool showThumbnail;

  const _PostTitle({
    required this.post,
    this.showThumbnail = false,
  });

  Widget thumbnail() {
    return Expanded(
      flex: 1,
      child: AspectRatio(
        aspectRatio: 1.0,
        child: ClipRect(
          child: Image.network(post.thumbnail!.url, fit: BoxFit.cover),
        ),
      ),
    );
  }

  Widget title(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    final title = Text(
      post.title,
      textAlign: TextAlign.start,
      style: Theme.of(context)
          .textTheme
          .titleMedium!
          .copyWith(color: theme.postTitle),
    );
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        title,
        InkWell(
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
        ),
        Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          spacing: 4,
          children: [
            Text(
              "${post.ups}",
              style: Theme.of(context).textTheme.titleMedium,
            ),
            Text('•', style: Theme.of(context).textTheme.bodySmall),
            Text(
              "${post.numComments} comments",
              style: Theme.of(context).textTheme.bodySmall,
            ),
            if (post.over18) NsfwTag(),
          ],
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Expanded(
          flex: 3,
          child: title(context),
        ),
        if (showThumbnail) thumbnail(),
      ],
    );
  }
}

class RedditPostCard extends StatefulWidget {
  final Post post;

  final Future<void> Function(bool)? onSave;
  final Future<void> Function(VoteDirection)? onLike;
  final VoidCallback? onTap;
  final int? maxLines;

  const RedditPostCard({
    super.key,
    required this.post,
    this.onLike,
    this.onSave,
    this.onTap,
    this.maxLines,
  });
  @override
  State<StatefulWidget> createState() => _RedditPostCardState();
}

class _RedditPostCardState extends State<RedditPostCard> {
  late VoteDirection? likes;
  late bool saved;

  _RedditPostCardState();

  @override
  void initState() {
    super.initState();
    likes = widget.post.likes.toVoteDirection();
    saved = widget.post.saved;
  }

  Widget _wrap(Widget widget) {
    return Padding(padding: EdgeInsets.symmetric(horizontal: 4), child: widget);
  }

  Widget header(BuildContext context) {
    final labelStyle = Theme.of(context)
        .textTheme
        .labelSmall!
        .copyWith(fontWeight: FontWeight.normal);
    final subreddit = widget.post.subreddit.subreddit;
    final subredditIcon = widget.post.subreddit.details?.icon;
    final theme = context.watch<ThemeBloc>().state;

    return Wrap(
      crossAxisAlignment: WrapCrossAlignment.center,
      children: [
        InkWell(
          onTap: () => navigateToSubscriptionsTab(
            context,
            FeedRoute(
              feed: Feed.subreddit(subreddit),
            ),
          ),
          child: Row(
            mainAxisSize: MainAxisSize.min,
            spacing: 8,
            children: [
              if (subredditIcon != null) SubredditIcon(icon: subredditIcon),
              Text(
                subreddit,
                style: labelStyle.copyWith(
                  color: theme.highlight,
                ),
              ),
            ],
          ),
        ),
        if (widget.post.isCrosspost)
          const RotatedBox(
            quarterTurns: 1,
            child: Icon(Icons.alt_route, color: Colors.greenAccent),
          ),
        const Text(' • '),
        InkWell(
          onTap: () {
            final username = widget.post.author?.username;
            if (username != null) {
              context.router.navigate(UserRoute(username: username));
            }
          },
          child: Text(
            widget.post.author?.username ?? "[deleted]",
            style: labelStyle,
          ),
        ),
        const Text(' • '),
        Text(widget.post.createdUtc.timeSince(), style: labelStyle),
        if (_showDomain()) ...[
          const Text(' • '),
          Text(
            widget.post.domain,
            style: labelStyle,
          ),
        ],
      ],
    );
  }

  bool _showDomain() {
    return !widget.post.isSelf &&
        !widget.post.isRedditMediaDomain &&
        widget.post.domain != "reddit.com";
  }

  Widget footer(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    final likeColor = theme.primaryColor;
    final dislikeColor = Theme.of(context).colorScheme.secondary;
    final savedColor = Colors.yellow;
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        IconButton(
          icon: Icon(
            Icons.thumb_up,
            color: (likes == VoteDirection.up) ? likeColor : null,
          ),
          tooltip: 'Upvote',
          onPressed: () async {
            final target = (likes == VoteDirection.up)
                ? VoteDirection.neutral
                : VoteDirection.up;
            await widget.onLike?.call(target);
            setState(() {
              likes = target;
            });
          },
        ),
        IconButton(
          icon: Icon(
            Icons.thumb_down,
            color: (likes == VoteDirection.down) ? dislikeColor : null,
          ),
          tooltip: 'Downvote',
          onPressed: () async {
            final target = (likes == VoteDirection.down)
                ? VoteDirection.neutral
                : VoteDirection.down;
            await widget.onLike?.call(target);
            setState(() {
              likes = target;
            });
          },
        ),
        IconButton(
          onPressed: () async {
            await widget.onSave?.call(!saved);
            saved = !saved;
          },
          icon: Icon(
            Icons.bookmark_outlined,
            color: saved ? savedColor : null,
          ),
          tooltip: "Save",
        ),
        IconButton(
          icon: const Icon(Icons.comment),
          tooltip: 'Comments',
          onPressed: () => navigateToSubscriptionsTab(
            context,
            ThreadRoute(
              permalink: widget.post.permalink,
              post: widget.post,
            ),
          ),
        ),
        IconButton(
          icon: const Icon(Icons.exit_to_app),
          tooltip: 'Open in',
          onPressed: () async {
            final url = Uri.parse(
              widget.post.urlOverriddenByDest ?? widget.post.url,
            );
            await launchUrl(url);
          },
        ),
        IconButton(
          icon: const Icon(Icons.share),
          tooltip: 'Share',
          onPressed: () {
            debugPost(post: widget.post);
          },
        ),
      ],
    );
  }

  bool showThumbnail() {
    if (widget.post.thumbnail == null) {
      return false;
    } else {
      return switch (widget.post.kind) {
        Kind.link || Kind.unknown => true,
        _ => false,
      };
    }
  }

  Widget title(BuildContext context) {
    return _PostTitle(
      post: widget.post,
      showThumbnail: showThumbnail(),
    );
  }

  Widget _contentWrap(Widget widget) {
    return switch (this.widget.post.kind) {
      Kind.image ||
      Kind.gallery ||
      Kind.video ||
      Kind.youtubeVideo ||
      Kind.mediaGallery =>
        widget,
      _ => _wrap(widget),
    };
  }

  Widget content(BuildContext context) {
    final widget = switch (this.widget.post.kind) {
      Kind.selftext => HtmlWithConditionalFade(
          htmlContent: this.widget.post.selftextHtml ?? "",
          maxLines: this.widget.maxLines,
        ),
      Kind.meta => Text("meta"),
      Kind.video => VideoContent(post: this.widget.post),
      Kind.youtubeVideo => YoutubeContent(post: this.widget.post),
      Kind.gallery => GalleryView(gallery: this.widget.post.gallery!),
      Kind.mediaGallery => MediaGalleryView(post: this.widget.post),
      Kind.image => ImageContent(post: this.widget.post),
      Kind.link || Kind.unknown => Container(),
      //Kind.unknown => Text("unknown"),
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
        _wrap(header(context)),
        _wrap(title(context)),
        content(context),
        _wrap(footer(context)),
      ],
    );
    final theme = context.watch<ThemeBloc>().state;
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      color: theme.background,
      elevation: 1,
      child: InkWell(
        onTap: widget.onTap,
        child: contentWidget,
      ),
    );
  }
}
