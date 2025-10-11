import 'dart:math' as math;

import 'package:crabir/loading_indicator.dart';
import 'package:crabir/post/post.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/user/model.dart';
import 'package:crabir/stream/things_view.dart';
import 'package:crabir/thread/widgets/comment.dart';
import 'package:extended_nested_scroll_view/extended_nested_scroll_view.dart';
import 'package:flutter/material.dart';
import 'package:crabir/src/rust/third_party/reddit_api/paging_handler.dart'
    as reddit_stream;
import 'package:go_router/go_router.dart';
import 'package:logging/logging.dart';

part 'tabs.dart';
part 'user.go_route_ext.dart';

@CrabirRoute()
class UserView extends StatefulWidget {
  final String username;
  final String tab;
  const UserView({
    super.key,
    required this.username,
    required this.tab,
  });
  @override
  State<StatefulWidget> createState() => _UserViewState();
}

class _UserViewState extends State<UserView>
    with SingleTickerProviderStateMixin {
  late Future<UserInfo> _userFuture;
  late final TabController _controller;
  late List<UserTabs> tabs;
  double opacity = 0;
  late final List<Widget> _tabViews;

  @override
  void initState() {
    super.initState();
    _userFuture = RedditAPI.client().userAbout(username: widget.username);
    tabs = _tabs(widget.username);
    _tabViews = tabs.map((t) => t.content(widget.username)).toList();
    int index = switch (widget.tab) {
      "overview" => 0,
      "about" => 1,
      "submitted" => 2,
      "comments" => 3,
      "saved" => 4,
      "upvoted" => 5,
      "downvoted" => 6,
      "hidden" => 7,
      _ => -1,
    };
    if (index == -1) {
      Logger("UserView").warning("Invalid user profile tab ${widget.tab}");
      index = 0;
    } else if (index >= tabs.length) {
      index = 0;
    }
    _controller =
        TabController(length: tabs.length, vsync: this, initialIndex: index);
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
    return FutureBuilder(
      future: _userFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return Center(child: LoadingIndicator());
        } else if (snapshot.hasError) {
          return Text("Error while fetching user info");
        } else if (snapshot.hasData) {
          final infos = snapshot.data!;
          return SafeArea(
            key: ValueKey(widget.username),
            child: ExtendedNestedScrollView(
              onlyOneScrollInBody: true,
              floatHeaderSlivers: true,
              headerSliverBuilder: (
                BuildContext context,
                bool innerBoxIsScrolled,
              ) =>
                  [
                topBar(
                  infos,
                  tabs,
                )
              ],
              body: TabBarView(
                controller: _controller,
                children: _tabViews,
              ),
            ),
          );
        } else {
          return Text("Nothing to show.");
        }
      },
    );
  }

  SliverAppBar topBar(UserInfo infos, List<UserTabs> tabs) {
    final title = Text(
      infos.name,
      textAlign: TextAlign.center,
      style: const TextStyle(
        fontSize: 20,
        fontWeight: FontWeight.bold,
      ),
    );
    return SliverAppBar(
      expandedHeight: 200,
      pinned: true,
      floating: false,
      title: Opacity(opacity: 1 - opacity, child: title),
      flexibleSpace: LayoutBuilder(
        builder: (context, constraints) {
          final settings = context
              .dependOnInheritedWidgetOfExactType<FlexibleSpaceBarSettings>()!;
          final deltaExtent = settings.maxExtent - settings.minExtent;
          final t = (1.0 -
                  (settings.currentExtent - settings.minExtent) / deltaExtent)
              .clamp(0.0, 1.0);
          final fadeStart = math.max(0.0, 1.0 - kToolbarHeight / deltaExtent);
          const fadeEnd = 1.0;
          WidgetsBinding.instance.addPostFrameCallback(
            (_) => setState(() {
              opacity = 1.0 - Interval(fadeStart, fadeEnd).transform(t);
            }),
          );

          return Opacity(
            opacity: opacity,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                // TODO: add banner
                Flexible(
                    child: CircleAvatar(
                  radius: 40,
                  backgroundImage: NetworkImage(infos.iconImg),
                )),
                const SizedBox(height: 8),
                Flexible(child: title),
              ],
            ),
          );
        },
      ),
      bottom: PreferredSize(
        preferredSize: const Size.fromHeight(16),
        child: TabBar(
          isScrollable: true,
          tabAlignment: TabAlignment.start,
          tabs: tabs.map((tab) => Text(tab.label())).toList(),
          controller: _controller,
        ),
      ),
    );
  }
}
