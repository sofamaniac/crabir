import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/buttons.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/flair.dart';
import 'package:crabir/html_view.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/comments/comments_settings.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/author.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/time_ellapsed.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class CommentView extends StatefulWidget {
  final Comment comment;
  final bool animateBottomBar;
  final VoidCallback? onLongPress;
  final VoidCallback? onTap;
  const CommentView({
    super.key,
    required this.comment,
    this.animateBottomBar = true,
    this.onLongPress,
    this.onTap,
  });
  @override
  createState() => _CommentViewState();
}

class _CommentViewState extends State<CommentView>
    with AutomaticKeepAliveClientMixin {
  bool showBottomBar = false;

  // we have to keep alive to remember `showBottomBar`.
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();
    showBottomBar = !widget.animateBottomBar;
  }

  Widget topRow(CommentsSettings settings) {
    final comment = widget.comment;
    return Row(
      children: [
        Expanded(
          child: RichText(
            text: TextSpan(
              children: [
                WidgetSpan(
                  alignment: PlaceholderAlignment.bottom,
                  child: _Username(
                    author: comment.author,
                    isSubmitter: comment.isSubmitter,
                    distinguished: comment.distinguished,
                  ),
                ),
                if (comment.author?.flair != null &&
                    settings.showUserFlair) ...[
                  const WidgetSpan(child: SizedBox(width: 8)),
                  WidgetSpan(
                    alignment: PlaceholderAlignment.middle,
                    child: FlairView(flair: comment.author!.flair),
                  ),
                ],
              ],
            ),
          ),
        ),
        Text(
          "${comment.scoreHidden ? "?" : comment.score} · ${comment.createdUtc.timeSince()}",
          style: Theme.of(context).textTheme.labelMedium,
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    final comment = widget.comment;
    final settings = context.watch<CommentsSettingsCubit>().state;
    final showBottomBar = this.showBottomBar || settings.buttonsAlwaysVisible;
    final VoidCallback? onTap = widget.onTap ??
        (widget.animateBottomBar
            ? () => setState(
                  () {
                    this.showBottomBar = !this.showBottomBar;
                  },
                )
            : null);
    return GestureDetector(
      onLongPress: widget.onLongPress,
      onTap: onTap,
      child: AnimatedSize(
        duration: Duration(milliseconds: 200),
        alignment: Alignment.topCenter,
        child: Card(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 8.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                topRow(settings),
                StyledHtml(
                  htmlContent: comment.bodyHtml,
                  onLinkTap: defaultLinkHandler,
                  showImages: settings.showCommentsImage,
                ),
                if (showBottomBar) bottomBar(),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Future<void> vote(VoteDirection target, bool hideButtonAfterAction) async {
    await widget.comment.vote(client: RedditAPI.client(), direction: target);
    setState(() {
      if (hideButtonAfterAction) {
        showBottomBar = false;
      }
    });
  }

  Future<void> save(bool target, bool hideButtonAfterAction) async {
    if (target) {
      await RedditAPI.client().unsave(thing: widget.comment.name);
    } else {
      await RedditAPI.client().save(thing: widget.comment.name);
    }
    setState(() {
      if (hideButtonAfterAction) {
        showBottomBar = false;
      }
    });
  }

  Widget bottomBar() {
    final likeColor = Theme.of(context).colorScheme.primary;
    final dislikeColor = Colors.cyanAccent;
    final settings = context.watch<CommentsSettingsCubit>().state;
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      mainAxisSize: MainAxisSize.max,
      children: [
        VoteButton.like(
          initialValue: widget.comment.likes,
          colorActive: likeColor,
          onChange: (target) async {
            await vote(target, settings.hideButtonAfterAction);
          },
        ),
        VoteButton.dislike(
          initialValue: widget.comment.likes,
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
          onPressed: () {
            // TODO: reply to comment
            if (settings.hideButtonAfterAction) {
              showBottomBar = false;
            }
          },
          icon: Icon(Icons.reply_rounded),
        ),
        IconButton(
          onPressed: () {
            // TODO: share comment
            if (settings.hideButtonAfterAction) {
              showBottomBar = false;
            }
          },
          icon: Icon(Icons.share),
        ),
      ],
    );
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
      CommentsSettings settings, String? currentUser, Color foreground) {
    final username = author?.username;
    if (username == currentUser &&
        username != null &&
        settings.highlightMyUsername) {
      return Cartouche(
        username,
        background: foreground,
        foreground: Colors.white,
      );
    } else if (isSubmitter) {
      return Cartouche(
        username!,
        background: Colors.cyan,
        foreground: Colors.white,
      );
    } else if (distinguished == "moderator") {
      return Cartouche(
        username!,
        background: Colors.green,
        foreground: Colors.white,
      );
    } else {
      return Cartouche(
        username ?? "u/[deleted]",
        foreground: foreground,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final settings = context.read<CommentsSettingsCubit>().state;
    final currentUser = context.read<AccountsBloc>().state.account?.username;
    final theme = context.read<ThemeBloc>().state;
    return InkWell(
      onTap: () {
        if (author?.username != null && settings.clickableUsername) {
          context.router.navigate(
            UserRoute(username: author!.username),
          );
        }
      },
      child: _username(settings, currentUser, theme.primaryColor),
    );
  }
}
