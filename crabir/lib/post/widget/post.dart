import 'package:crabir/feed_list.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/post/widget/gallery.dart';
import 'package:crabir/post/widget/html_with_fade.dart';
import 'package:crabir/post/widget/video.dart';
import 'package:crabir/flair.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/subreddit.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

final horizontalPadding = 16.0;

class _PostTitle extends StatelessWidget {
  final Post post;

  final bool showThumbnail;
  final bool showFlair;

  const _PostTitle(
      {required this.post, this.showThumbnail = false, this.showFlair = true});

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
    final title = Text(
      post.title,
      textAlign: TextAlign.start,
      style: Theme.of(context).textTheme.titleMedium,
    );
    return Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      title,
      FlairView(flair: post.linkFlair),
      Row(
        children: [
          Text("${post.ups}"),
          Text(" "),
          Text("${post.numComments} comments")
        ],
      )
    ]);
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
  late bool? likes;
  late bool saved;

  _RedditPostCardState();

  @override
  void initState() {
    super.initState();
    likes = widget.post.likes;
    saved = widget.post.saved;
  }

  Widget _wrap(Widget widget) {
    return Padding(padding: EdgeInsets.symmetric(horizontal: 8), child: widget);
  }

  Widget header(BuildContext context) {
    final labelStyle = Theme.of(context).textTheme.labelSmall;
    final subreddit = widget.post.subreddit.subreddit;
    final subredditIcon = widget.post.subreddit.details?.icon;
    return Row(
      children: [
        InkWell(
          onTap: () => navigateToSubscriptionsTab(
            context,
            FeedRoute(
              feed: Feed.subreddit(subreddit),
            ),
          ),
          child: Row(
            spacing: 8,
            children: [
              if (subredditIcon != null) SubredditIcon(icon: subredditIcon),
              Text(
                'r/$subreddit',
                style: labelStyle?.copyWith(
                  color: Theme.of(context).colorScheme.primary,
                ),
              ),
            ],
          ),
        ),
        const Text(' • '),
        InkWell(
          onTap: () => (),
          child: Text(
            'u/${widget.post.author?.username ?? "[deleted]"}',
            style: labelStyle?.copyWith(
              color: Theme.of(context).colorScheme.primary,
            ),
          ),
        ),
      ],
    );
  }

  Widget footer(BuildContext context) {
    final likeColor = Theme.of(context).colorScheme.primary;
    final dislikeColor = Theme.of(context).colorScheme.secondary;
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        IconButton(
          icon: Icon(Icons.thumb_up, color: (likes == true) ? likeColor : null),
          tooltip: 'Upvote',
          onPressed: () async {
            await widget.onLike?.call(
              !(likes == true) ? VoteDirection.up : VoteDirection.neutral,
            );
            setState(() {
              if (likes == true) {
                likes = null;
              } else {
                likes = true;
              }
            });
          },
        ),
        IconButton(
          icon: Icon(Icons.thumb_down,
              color: (likes == false) ? dislikeColor : null),
          tooltip: 'Downvote',
          onPressed: () async {
            await widget.onLike?.call(
              !(likes == false) ? VoteDirection.down : VoteDirection.neutral,
            );
            setState(() {
              if (likes == false) {
                likes = null;
              } else {
                likes = false;
              }
            });
          },
        ),
        IconButton(
          onPressed: () async {
            await widget.onSave?.call(!saved);
            saved = !saved;
          },
          icon: const Icon(Icons.bookmark_outlined),
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
      Kind.image || Kind.gallery || Kind.video || Kind.selftext => widget,
      _ => _wrap(widget),
    };
  }

  Widget content(BuildContext context) {
    final fontSize = Theme.of(context).textTheme.bodyMedium?.fontSize ?? 15;
    final height = Theme.of(context).textTheme.bodyMedium?.height ?? 1;
    final widget = switch (this.widget.post.kind) {
      Kind.selftext => HtmlWithConditionalFade(
          htmlContent: this.widget.post.selftextHtml ?? "",
          maxLines: this.widget.maxLines,
          backgroundColor: Colors.black,
          fontSize: fontSize,
          height: height,
        ),
      Kind.meta => Text("meta"),
      Kind.video => VideoContent(post: this.widget.post),
      Kind.gallery => GalleryView(gallery: this.widget.post.gallery!),
      Kind.image => RedditImageView(image: this.widget.post.preview!.images[0]),
      Kind.link => Container(),
      Kind.unknown => Text("unknown"),
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
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 4),
      elevation: 1,
      child: InkWell(onTap: widget.onTap, child: contentWidget),
    );
  }
}
