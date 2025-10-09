import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/bool_to_vote.dart';
import 'package:crabir/buttons.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/flair.dart';
import 'package:crabir/html_view.dart';
import 'package:crabir/separated_row.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/author.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/thread/widgets/thread.dart';
import 'package:crabir/time_ellapsed.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

class OpenedComment {
  static ValueNotifier<String?> current = ValueNotifier(null);
  OpenedComment();
}

class CommentView extends StatefulWidget {
  final Comment comment;
  final bool animateBottomBar;
  final VoidCallback? onLongPress;
  final VoidCallback? onTap;
  // Display the number of replies in the header
  final bool showRepliesNumber;
  const CommentView({
    super.key,
    required this.comment,
    this.animateBottomBar = true,
    this.onLongPress,
    this.onTap,
    this.showRepliesNumber = false,
  });
  @override
  createState() => _CommentViewState();
}

class _CommentViewState extends State<CommentView> {
  Widget topRow(CommentsSettings settings) {
    final comment = widget.comment;
    final color = CrabirTheme.of(context).secondaryText;
    final settings = CommentsSettings.of(context);
    return Row(
      mainAxisSize: MainAxisSize.max,
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      crossAxisAlignment: CrossAxisAlignment.center,
      spacing: 4,
      children: [
        Flexible(
          fit: FlexFit.loose,
          child: Row(
            spacing: 4,
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              _Username(
                author: comment.author,
                isSubmitter: comment.isSubmitter,
                distinguished: comment.distinguished,
              ),
              if (comment.author != null && settings.showUserFlair)
                Flexible(
                  fit: FlexFit.loose,
                  child: FlairView(
                    flair: comment.author!.flair,
                    showColor: settings.showFlairColors,
                    showEmoji: settings.showFlairEmojis,
                  ),
                ),
              if (comment.locked) Icon(Icons.lock, color: color),
              if (comment.stickied)
                Text(
                  "stickied",
                  style: Theme.of(context)
                      .textTheme
                      .labelMedium!
                      .copyWith(color: Colors.green),
                )
            ],
          ),
        ),
        SeparatedRow(
          spacing: 2,
          crossAxisAlignment: WrapCrossAlignment.center,
          alignment: WrapAlignment.end,
          separatorStyle:
              Theme.of(context).textTheme.labelMedium!.copyWith(color: color),
          children: [
            if (widget.showRepliesNumber)
              Cartouche(
                "+${numReplies(comment)}",
                foreground: Colors.white,
                background: Colors.lightGreen,
              ),
            LikeText(
              score: comment.score,
              likes: comment.likes,
              hidden: comment.scoreHidden,
              style: Theme.of(context).textTheme.labelMedium!,
              scaling: 1.5,
            ),
            Text(
              comment.createdUtc.timeSince(context),
              style: Theme.of(context)
                  .textTheme
                  .labelMedium!
                  .copyWith(color: color),
            ),
          ],
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder(
      valueListenable: OpenedComment.current,
      builder: (context, _, __) {
        final comment = widget.comment;
        final settings = context.watch<CommentsSettingsCubit>().state;
        final openComment = OpenedComment.current.value;
        final showBottomBar =
            openComment == comment.id || settings.buttonsAlwaysVisible;
        return GestureDetector(
          onLongPress: widget.onLongPress,
          onTap: () {
            widget.onTap?.call();
            if (openComment == comment.id) {
              OpenedComment.current.value = null;
            } else {
              OpenedComment.current.value = comment.id;
            }
          },
          behavior: HitTestBehavior.translucent,
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              spacing: 8,
              children: [
                topRow(settings),
                StyledHtml(
                  htmlContent: comment.bodyHtml,
                  showImages: settings.showCommentsImage,
                ),
                AnimatedSwitcher(
                  duration: Duration(milliseconds: 200),
                  child: showBottomBar ? bottomBar() : SizedBox.shrink(),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  Future<void> vote(VoteDirection target, bool hideButtonAfterAction) async {
    await widget.comment.vote(client: RedditAPI.client(), direction: target);
    setState(() {
      if (hideButtonAfterAction) {
        OpenedComment.current.value = null;
      }
    });
  }

  Future<void> save(bool target, bool hideButtonAfterAction) async {
    if (target) {
      await widget.comment.unsave(client: RedditAPI.client());
    } else {
      await widget.comment.save(client: RedditAPI.client());
    }
    setState(() {
      if (hideButtonAfterAction) {
        OpenedComment.current.value = null;
      }
    });
  }

  Widget bottomBar() {
    final theme = CrabirTheme.of(context);
    final likeColor = theme.primaryColor;
    final dislikeColor = theme.downvoteContent;
    final settings = CommentsSettings.of(context);
    final likes = widget.comment.likes.toVoteDirection();
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      mainAxisSize: MainAxisSize.max,
      children: [
        VoteButton.like(
          likes: likes,
          colorActive: likeColor,
          onChange: (target) async {
            await vote(target, settings.hideButtonAfterAction);
          },
        ),
        VoteButton.dislike(
          likes: likes,
          colorActive: dislikeColor,
          onChange: (target) async {
            await vote(target, settings.hideButtonAfterAction);
          },
        ),
        if (settings.showSaveButton)
          SaveButton(
            initialValue: widget.comment.saved,
            onChange: (target) async {
              await save(target, settings.hideButtonAfterAction);
            },
          ),
        IconButton(
          color: theme.secondaryText,
          onPressed: () {
            // TODO: reply to comment
            if (settings.hideButtonAfterAction) {
              OpenedComment.current.value = null;
            }
          },
          icon: Icon(Icons.reply_rounded),
        ),
        ShareButton(
          comment: widget.comment,
          onPressed: () {
            if (settings.hideButtonAfterAction) {
              OpenedComment.current.value = null;
            }
          },
        ),
      ],
    );
  }
}

int numReplies(Comment comment) {
  if (comment.replies().isEmpty) {
    return 0;
  } else {
    int acc = 0;
    for (final reply in comment.replies()) {
      acc += switch (reply) {
        Thing_Comment(field0: final comment) => 1 + numReplies(comment),
        Thing_More(:final count) => count,
        _ => 0,
      };
    }
    return acc;
  }
}

class _Username extends StatelessWidget {
  final AuthorInfo? author;
  final bool isSubmitter;
  final String? distinguished;
  const _Username({
    required this.author,
    required this.isSubmitter,
    required this.distinguished,
  });

  Widget _username(
    CommentsSettings settings,
    String? currentUser,
    Color foreground,
  ) {
    final username = author?.username ?? "u/[deleted]";
    if (username == currentUser && settings.highlightMyUsername) {
      return Cartouche(
        username,
        background: foreground,
        foreground: Colors.white,
      );
    } else if (isSubmitter) {
      return Cartouche(
        username,
        background: Colors.cyan,
        foreground: Colors.white,
      );
    } else if (distinguished == "moderator") {
      return Cartouche(
        username,
        background: Colors.green,
        foreground: Colors.white,
      );
    } else {
      return Text(
        username,
        style: TextStyle(color: foreground, fontSize: 12),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final settings = context.read<CommentsSettingsCubit>().state;
    final currentUser = context.read<AccountsBloc>().state.account?.username;
    final theme = CrabirTheme.of(context);
    return InkWell(
      onTap: () {
        if (author?.username != null && settings.clickableUsername) {
          context.go("/u/${author!.username}");
        }
      },
      child: _username(settings, currentUser, theme.highlight),
    );
  }
}

class CollapsedComment extends StatelessWidget {
  final Comment comment;
  final VoidCallback? onLongPress;

  const CollapsedComment({super.key, required this.comment, this.onLongPress});

  @override
  Widget build(BuildContext context) {
    final color = CrabirTheme.of(context).secondaryText;
    final style =
        Theme.of(context).textTheme.labelMedium!.copyWith(color: color);
    return GestureDetector(
      onLongPress: onLongPress,
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
        child: Row(
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.center,
          spacing: 4,
          children: [
            Flexible(
              fit: FlexFit.loose,
              child: Row(
                spacing: 4,
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Text(
                    "[+]",
                    style: style,
                  ),
                  Text(
                    comment.author?.username ?? "[deleted]",
                    style: style,
                  ),
                ],
              ),
            ),
            SeparatedRow(
              spacing: 2,
              crossAxisAlignment: WrapCrossAlignment.center,
              alignment: WrapAlignment.end,
              separatorStyle: Theme.of(context)
                  .textTheme
                  .labelMedium!
                  .copyWith(color: color),
              children: [
                Cartouche(
                  "+${numReplies(comment)}",
                  foreground: Colors.white,
                  background: Colors.lightGreen,
                ),
                LikeText(
                  score: comment.score,
                  likes: comment.likes,
                  hidden: comment.scoreHidden,
                  style: Theme.of(context).textTheme.labelMedium!,
                  scaling: 1.5,
                ),
                Text(
                  comment.createdUtc.timeSince(context),
                  style: Theme.of(context)
                      .textTheme
                      .labelMedium!
                      .copyWith(color: color),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
