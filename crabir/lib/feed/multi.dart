import 'package:auto_route/auto_route.dart';
import 'package:crabir/accounts/bloc/accounts_bloc.dart';
import 'package:crabir/feed/feed_new.dart';
import 'package:crabir/feed/sort_display.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

@RoutePage()
class MultiView extends StatefulWidget {
  const MultiView({
    super.key,
    required this.multi,
    this.initialSort,
  });
  final Multi multi;

  /// Override the setting-defined preferred sort.
  final FeedSort? initialSort;

  @override
  State<MultiView> createState() => _MultiViewState();
}

class _MultiViewState extends State<MultiView> {
  @override
  Widget build(BuildContext context) {
    final _ = context.watch<AccountsBloc>().state;
    final bloc = context.read<PostsSettingsCubit>();
    final settings = context.read<PostsSettingsCubit>().state;
    final FeedSort sort;
    if (settings.rememberSortByCommunity) {
      sort = settings.rememberedSorts.containsMulti(widget.multi) ??
          widget.initialSort ??
          settings.defaultSort;
    } else {
      sort = widget.initialSort ?? settings.defaultSort;
    }
    return FeedViewBodyNew(
      key: ValueKey(widget.multi),
      title: (sort) => Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(widget.multi.displayName),
          SortDisplay(sort: sort),
        ],
      ),
      newStream: (sort) => RedditAPI.client().multiPosts(
        multi: widget.multi,
        sort: sort,
      ),
      onSortChanged: (sort) {
        if (settings.rememberSortByCommunity) {
          bloc.updateRememberedSorts(
              settings.rememberedSorts.addMulti(widget.multi, sort));
        }
      },
      initialSort: sort,
    );
  }
}
//
// @RoutePage()
// class MultiView extends StatelessWidget {
//   const MultiView({super.key, required this.multi, this.initialSort});
//   final Multi multi;
//   final FeedSort? initialSort;
//
//   @override
//   Widget build(BuildContext context) {
//     return MultiViewBody(
//       multi: multi,
//       initialSort: initialSort,
//     );
//   }
// }
//
// class MultiViewBody extends StatefulWidget {
//   final Multi multi;
//   final FeedSort? initialSort;
//
//   const MultiViewBody({
//     super.key,
//     required this.multi,
//     required this.initialSort,
//   });
//
//   @override
//   State<MultiViewBody> createState() => _MultiViewBodyState();
// }
//
// class _MultiViewBodyState extends State<MultiViewBody>
//     with AutomaticKeepAliveClientMixin {
//   FeedSort sort = FeedSort.best();
//   late reddit_stream.Streamable _stream;
//   int _forceRebuild = 0;
//
//   @override
//   void initState() {
//     super.initState();
//     _initializeSort();
//   }
//
//   void _initializeSort() {
//     final settings = context.read<PostsSettingsCubit>().state;
//     sort = widget.initialSort ??
//         settings.remeberedSort.containsMulti(widget.multi) ??
//         settings.defaultSort;
//     _stream = RedditAPI.client().multiPosts(
//       multi: widget.multi,
//       sort: sort,
//     );
//   }
//
//   @override
//   bool get wantKeepAlive => true;
//
//   @override
//   Widget build(BuildContext context) {
//     super.build(context);
//     final settings = context.read<PostsSettingsCubit>();
//     return NestedScrollView(
//       headerSliverBuilder: (context, __) {
//         return [
//           SliverAppBar(
//             floating: true,
//             title: Column(
//               crossAxisAlignment: CrossAxisAlignment.start,
//               children: [
//                 Text(widget.multi.displayName),
//                 SortDisplay(sort: sort),
//               ],
//             ),
//             actions: [
//               IconButton(onPressed: () {}, icon: Icon(Icons.search)),
//               SortMenu(
//                 onSelect: (sort) {
//                   settings.updateRemeberedSort(
//                     settings.state.remeberedSort.addMulti(widget.multi, sort),
//                   );
//                   setState(
//                     () {
//                       this.sort = sort;
//                       _stream = RedditAPI.client().multiPosts(
//                         multi: widget.multi,
//                         sort: sort,
//                       );
//                       _forceRebuild += 1;
//                     },
//                   );
//                 },
//               ),
//             ],
//           ),
//         ];
//       },
//       floatHeaderSlivers: true,
//       body: RefreshIndicator(
//         onRefresh: () async {
//           await _stream.refresh();
//           setState(() {
//             _forceRebuild += 1;
//           });
//         },
//         child: ThingsScaffold(
//           stream: _stream,
//           key: ValueKey(_forceRebuild),
//           postView: (context, post) {
//             return RedditPostCard(
//               maxLines: 5,
//               post: post,
//               onLikeCallback: (direction) async {
//                 await _stream.vote(
//                   name: post.name,
//                   direction: direction,
//                   client: RedditAPI.client(),
//                 );
//               },
//               onSaveCallback: (save) async {
//                 await _stream.save(
//                   name: post.name,
//                   save: save,
//                   client: RedditAPI.client(),
//                 );
//               },
//               onTap: () => context.pushRoute(
//                 ThreadRoute(
//                   post: post,
//                 ),
//               ),
//             );
//           },
//         ),
//       ),
//     );
//   }
// }
