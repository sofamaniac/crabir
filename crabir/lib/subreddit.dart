import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/hexcolor.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    as subreddit;
import 'package:flutter/material.dart';

class SubredditIcon extends StatelessWidget {
  final subreddit.SubredditIcon icon;
  final double radius;

  const SubredditIcon({super.key, required this.icon, this.radius = 16});

  @override
  Widget build(BuildContext context) {
    return switch (icon) {
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
  }
}
