import 'package:crabir/search_posts/bloc/search_bloc.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/search.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class SortMenu extends StatelessWidget {
  const SortMenu({super.key});

  @override
  Widget build(BuildContext context) {
    final bloc = context.read<PostSearchBloc>();
    return MenuAnchor(
      menuChildren: [
        SubmenuButton(
          menuChildren: menu(
            PostSearchSort.top,
            (sort) => bloc.add(
              SetSort(
                sort,
              ),
            ),
          ),
          child: Text("Top"),
        ),
        SubmenuButton(
          menuChildren: menu(
            PostSearchSort.relevance,
            (sort) => bloc.add(
              SetSort(
                sort,
              ),
            ),
          ),
          child: Text("Relevance"),
        ),
        MenuItemButton(
          onPressed: () => bloc.add(
            SetSort(PostSearchSort.hot()),
          ),
          child: Text("Hot"),
        ),
        MenuItemButton(
          onPressed: () => bloc.add(
            SetSort(PostSearchSort.new_()),
          ),
          child: Text("New"),
        ),
        SubmenuButton(
          menuChildren: menu(
            PostSearchSort.comments,
            (sort) => bloc.add(
              SetSort(sort),
            ),
          ),
          child: Text("Comments"),
        ),
      ],
      builder: (_, MenuController controller, Widget? child) {
        return IconButton(
          onPressed: () {
            if (controller.isOpen) {
              controller.close();
            } else {
              controller.open();
            }
          },
          icon: const Icon(Icons.sort),
        );
      },
    );
  }

  List<Widget> menu(
    PostSearchSort Function(Timeframe) sort,
    Function(PostSearchSort) onSelect,
  ) {
    return timeOptions
        .map(
          (timeframe) => MenuItemButton(
            onPressed: () => onSelect(sort(timeframe)),
            child: Text(timeframeToString(timeframe)),
          ),
        )
        .toList();
  }
}
