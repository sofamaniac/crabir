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

@RoutePage()
class CurrentUserView extends StatelessWidget {
  const CurrentUserView({super.key});

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AccountsBloc>().state;
    if (state.account == null) {
      return Container();
    }
    final username = state.account!.username;
    return AutoTabsRouter.tabBar(
      homeIndex: 0,
      routes: allUserTabs.map((tab) => tab.route(username)).toList(),
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
                tabs: allUserTabs.map((tab) => Text(tab.name())).toList(),
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
