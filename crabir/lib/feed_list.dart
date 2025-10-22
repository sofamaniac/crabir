import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/subreddit.dart'
    as subreddit;
import 'package:crabir/subreddit.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

/// Widget to display a subreddit in a list
class SubredditTile extends StatelessWidget {
  final subreddit.Subreddit sub;
  final TextStyle? style;
  final VoidCallback? onTap;

  const SubredditTile(
    this.sub, {
    super.key,
    this.style,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: onTap,
      leading: SubredditIcon(icon: sub.icon, radius: 12),
      title: Text(
        sub.other.displayNamePrefixed,
        // style:
        //     Theme.of(context).textTheme.bodyMedium!.copyWith(color: textColor),
        style: style,
        maxLines: 1,
      ),
    );
  }
}

/// Widget to display a multireddit in a list
class MultiRedditTile extends StatelessWidget {
  final Multi multi;
  final bool showSubtitle;
  final TextStyle? style;

  const MultiRedditTile(
    this.multi, {
    super.key,
    this.style,
    this.showSubtitle = false,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: () {
        Scaffold.of(context).closeDrawer();
        context.go(multi.path, extra: multi);
      },
      leading: CircleAvatar(
        radius: 12,
        foregroundImage: NetworkImage(multi.iconUrl),
      ),
      title: Text(
        multi.displayName,
        style: style,
        maxLines: 1,
      ),
      subtitle: showSubtitle
          ? Text(
              "${multi.subreddits.length} communities -- ${multi.visibility}")
          : null,
    );
  }
}
