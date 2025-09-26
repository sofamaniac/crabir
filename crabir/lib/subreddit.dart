import 'package:auto_route/auto_route.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/hexcolor.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    as subreddit;
import 'package:flutter/material.dart';

class SubredditIcon extends StatelessWidget {
  final subreddit.SubredditIcon icon;
  final double radius;
  final bool clickable;
  final String? subredditName;

  const SubredditIcon({
    super.key,
    required this.icon,
    this.radius = 16,
    this.clickable = false,
    this.subredditName,
  });

  @override
  Widget build(BuildContext context) {
    final avatar = switch (icon) {
      subreddit.SubredditIcon_Image(field0: final icon) => CircleAvatar(
          radius: radius,
          backgroundColor: Colors.transparent,
          child: ClipOval(
            child: Image(
              image: CachedNetworkImageProvider(icon.url),
              width: radius * 2,
              height: radius * 2,
              fit: BoxFit.contain,
            ),
          ),
        ),
      subreddit.SubredditIcon_Color(field0: final color) => CircleAvatar(
          radius: radius,
          backgroundColor: color.stringToColor(),
          child: Text("r/"),
        )
    };
    if (clickable && subredditName != null) {
      return InkWell(
        onTap: () => context.router.navigate(
          FeedRoute(
            key: ValueKey(subredditName!),
            feed: Feed.subreddit(subredditName!),
          ),
        ),
        child: avatar,
      );
    } else {
      return avatar;
    }
  }
}
