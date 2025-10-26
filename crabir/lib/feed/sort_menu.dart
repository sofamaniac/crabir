import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:flutter/material.dart';

class SortMenu extends StatelessWidget {
  final void Function(FeedSort) onSelect;

  const SortMenu({super.key, required this.onSelect});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return MenuAnchor(
      menuChildren: [
        MenuItemButton(
          onPressed: () => onSelect(
            FeedSort.best(),
          ),
          child: Text(locales.sortBest),
        ),
        MenuItemButton(
          onPressed: () => onSelect(
            FeedSort.hot(),
          ),
          child: Text(locales.sortHot),
        ),
        MenuItemButton(
          onPressed: () => onSelect(
            FeedSort.rising(),
          ),
          child: Text(locales.sortRising),
        ),
        SubmenuButton(
          menuChildren: menu(
            FeedSort.top,
            onSelect,
            context,
          ),
          child: Text(locales.sortTop),
        ),
        MenuItemButton(
          onPressed: () => onSelect(
            FeedSort.new_(),
          ),
          child: Text(locales.sortNew),
        ),
        SubmenuButton(
          menuChildren: menu(
            FeedSort.controversial,
            onSelect,
            context,
          ),
          child: Text(locales.sortControversial),
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
}
