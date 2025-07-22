import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/comment.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/login.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/user/model.dart';
import 'package:crabir/stream/things_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

enum _Tabs {
  overview,
  about,
  posts,
  comments,
  saved,
  upvoted,
  downvoted,
  hidden,
  gilded,
  friends,
  blocked;

  String name() {
    return switch (this) {
      _Tabs.overview => "Overview",
      _Tabs.about => "About",
      _Tabs.posts => "Posts",
      _Tabs.comments => "Comments",
      _Tabs.saved => "Saved",
      _Tabs.upvoted => "Upvoted",
      _Tabs.downvoted => "Downvoted",
      _Tabs.hidden => "Hidden",
      _Tabs.gilded => "Gilded",
      _Tabs.friends => "Friends",
      _Tabs.blocked => "Blocked",
    };
  }
}

class CurrentUserView extends StatelessWidget {
  const CurrentUserView({super.key});

  @override
  Widget build(BuildContext context) {
    final tabs = [
      _Tabs.about,
      _Tabs.overview,
      _Tabs.posts,
      _Tabs.comments,
      _Tabs.saved,
      _Tabs.upvoted,
      _Tabs.downvoted,
      _Tabs.hidden,
      _Tabs.gilded,
      _Tabs.friends,
      _Tabs.blocked,
    ];
    return BlocBuilder<AccountsBloc, AccountState>(
      builder: (context, accounts) {
        if (accounts.status == AccountStatus.uninit) {
          context.read<AccountsBloc>().add(Initialize());
          return Container();
        } else if (accounts.account?.id == UserAccount.anonymous().id) {
          // TODO: account selection box
          return Container();
        }
        final username = accounts.account!.username;
        return DefaultTabController(
          length: tabs.length,
          child: NestedScrollView(
            headerSliverBuilder: (_, __) => [
              SliverAppBar.large(
                //floating: true,
                pinned: false,
                bottom: TabBar(
                  isScrollable: true,
                  tabAlignment: TabAlignment.start,
                  tabs: tabs.map((tab) => Text(tab.name())).toList(),
                ),
              )
            ],
            floatHeaderSlivers: true,
            body: TabBarView(
              children: tabs.map((tab) {
                return switch (tab) {
                  _Tabs.overview => ThingsScaffold(
                      postView: _postView,
                      commentView: _commentView,
                      stream: RedditAPI.client().userOverview(
                        username: username,
                        sort: UserStreamSort.new_(),
                      ),
                    ),
                  _Tabs.posts => ThingsScaffold(
                      postView: _postView,
                      stream: RedditAPI.client().userSubmitted(
                        username: username,
                        sort: UserStreamSort.new_(),
                      ),
                      commentView: _commentView,
                    ),
                  _Tabs.saved => ThingsScaffold(
                      postView: _postView,
                      stream: RedditAPI.client().userSaved(username: username),
                      commentView: _commentView,
                    ),
                  _Tabs.upvoted => ThingsScaffold(
                      stream:
                          RedditAPI.client().userUpvoted(username: username),
                      postView: _postView,
                      commentView: _commentView,
                    ),
                  _Tabs.downvoted => ThingsScaffold(
                      stream:
                          RedditAPI.client().userDownvoted(username: username),
                      postView: _postView,
                      commentView: _commentView,
                    ),
                  _Tabs.hidden => ThingsScaffold(
                      stream: RedditAPI.client().userHidden(username: username),
                      postView: _postView,
                      commentView: _commentView,
                    ),
                  _ => Text(tab.name())
                };
              }).toList(),
            ),
          ),
        );
      },
    );
  }
}

Widget _postView(BuildContext context, Post post) {
  return RedditPostCard(post: post);
}

Widget _commentView(BuildContext context, Comment comment) {
  return CommentView(
    comment: comment,
    animateBottomBar: false,
  );
}
