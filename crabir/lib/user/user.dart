import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
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

/// Display a user profile.
/// If `username` is not specified, defaults to the current user.
@RoutePage()
class UserView extends StatelessWidget {
  const UserView({super.key, this.username});

  final String? username;

  List<UserTabs> _tabs(String currentUser) {
    if (currentUser == username) {
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
    return AutoTabsRouter.tabBar(
      homeIndex: 0,
      routes: tabs.map((tab) => tab.route(username ?? currentUser)).toList(),
      builder: (context, child, tabController) {
        return NestedScrollView(
          headerSliverBuilder: (_, __) => [
            SliverAppBar.large(
              //floating: true,
              leading: CircleAvatar(
                foregroundImage: NetworkImage(state.account!.profilePicture),
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
  }
}
