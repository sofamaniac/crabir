import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';

class SortMenu extends StatelessWidget {
  final void Function(FeedSort) onSelect;

  const SortMenu({super.key, required this.onSelect});

  @override
  Widget build(BuildContext context) {
    return MenuAnchor(
      menuChildren: [
        MenuItemButton(
          onPressed: () => onSelect(
            FeedSort.best(),
          ),
          child: Text("Best"),
        ),
        MenuItemButton(
          onPressed: () => onSelect(
            FeedSort.hot(),
          ),
          child: Text("Hot"),
        ),
        MenuItemButton(
          onPressed: () => onSelect(
            FeedSort.rising(),
          ),
          child: Text("Rising"),
        ),
        SubmenuButton(menuChildren: menu(FeedSort.top), child: Text("Top")),
        SubmenuButton(menuChildren: menu(FeedSort.new_), child: Text("New")),
        SubmenuButton(
          menuChildren: menu(FeedSort.controversial),
          child: Text("Controversial"),
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
    FeedSort Function(Timeframe) sort,
    //FeedStateProvider state,
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
