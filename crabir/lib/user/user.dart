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
@RoutePage()
class UserView extends StatelessWidget {
  final String? username;
  const UserView({super.key, this.username});

  @override
  Widget build(BuildContext context) {
    final currentUser = context.watch<AccountsBloc>().state.account;
    final user = username ?? currentUser?.username;
    if (user == null || currentUser?.isAnonymous == true) {
      WidgetsBinding.instance.addPostFrameCallback((_) async {
        final result = await showDialog(
          context: context,
          builder: (context) => const Dialog(
            child: Text("TODO: account selection"),
          ),
        );
        if (result == null) {
          context.tabsRouter.setActiveIndex(0);
        }
      });
      return const SizedBox.shrink();
    } else {
      return _UserView(username: user);
    }
  }
}

class _UserView extends StatefulWidget {
  const _UserView({required this.username});

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

  List<UserTabs> _tabs(String currentUser) {
    if (currentUser == widget.username) {
      return allUserTabs;
    } else {
      return publicUserTabs;
    }
  }

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AccountsBloc>().state;
    if (state.account == null) {
      return Container();
    }
    final currentUser = state.account!.username;
    final tabs = _tabs(currentUser);
    return FutureBuilder(
        future: _userFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: LoadingIndicator());
          } else if (snapshot.hasError) {
            return Text("Error while fetching user info");
          } else if (snapshot.hasData) {
            return AutoTabsRouter.tabBar(
              homeIndex: 0,
              routes: tabs.map((tab) => tab.route(widget.username)).toList(),
              builder: (context, child, tabController) {
                return NestedScrollView(
                  headerSliverBuilder: (_, __) => [
                    SliverAppBar.large(
                      //floating: true,
                      leading: CircleAvatar(
                        foregroundImage:
                            NetworkImage(state.account!.profilePicture),
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
