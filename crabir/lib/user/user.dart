import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/widget/post.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/src/rust/api/simple.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/user/model.dart';
import 'package:crabir/stream/things_view.dart';
import 'package:crabir/thread/widgets/comment.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:crabir/src/rust/third_party/reddit_api/streamable.dart'
    as reddit_stream;

part 'tabs.dart';

/// Defaults to the current logged in user if not specified.
/// Should only be called when there is a non-anonymous user logged in.
@RoutePage()
class UserView extends StatelessWidget {
  final String? username;
  const UserView({super.key, this.username});

  @override
  Widget build(BuildContext context) {
    final currentUser = context.watch<AccountsBloc>().state.account;
    final user = username ?? currentUser?.username;
    if (user == null) {
      return Center(child: LoadingIndicator());
    }
    return _UserView(key: ValueKey(user), username: user);
  }
}

class _UserView extends StatefulWidget {
  const _UserView({super.key, required this.username});

  final String username;

  @override
  State<_UserView> createState() => _UserViewState();
}

class _UserViewState extends State<_UserView> {
  late Future<UserInfo> _userFuture;

  @override
  void initState() {
    super.initState();
    _userFuture = RedditAPI.client().userAbout(username: widget.username);
  }

  List<UserTabs> _tabs(String? currentUser) {
    if (currentUser == widget.username) {
      return allUserTabs;
    } else {
      return publicUserTabs;
    }
  }

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AccountsBloc>().state;
    final currentUser = state.account?.username;
    final tabs = _tabs(currentUser);
    return FutureBuilder(
        future: _userFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: LoadingIndicator());
          } else if (snapshot.hasError) {
            return Text("Error while fetching user info");
          } else if (snapshot.hasData) {
            final infos = snapshot.data!;
            return AutoTabsRouter.tabBar(
              homeIndex: 0,
              routes: tabs.map((tab) {
                if (tab case UserTabs.about) {
                  return UserAboutRoute(
                    username: widget.username,
                    about: infos,
                  );
                } else {
                  return tab.route(widget.username);
                }
              }).toList(),
              builder: (context, child, tabController) {
                return NestedScrollView(
                  headerSliverBuilder: (_, __) => [
                    SliverAppBar.large(
                      //floating: true,
                      flexibleSpace: FlexibleSpaceBar(
                        background: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            CircleAvatar(
                              radius: 40,
                              backgroundImage: NetworkImage(infos.iconImg),
                            ),
                            const SizedBox(height: 8),
                            Text(
                              infos.name,
                              style: TextStyle(
                                  fontSize: 20, fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                      ),
                      pinned: false,
                      bottom: TabBar(
                        isScrollable: true,
                        tabAlignment: TabAlignment.start,
                        tabs: tabs.map((tab) => Text(tab.name())).toList(),
                        controller: tabController,
                      ),
                    )
                  ],
                  floatHeaderSlivers: true,
                  body: child,
                );
              },
            );
          } else {
            return Text("Nothing to show.");
          }
        });
  }
}
