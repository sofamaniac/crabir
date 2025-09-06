import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/search_posts/bloc/search_bloc.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/search.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class SortMenu extends StatelessWidget {
  const SortMenu({super.key});

  @override
  Widget build(BuildContext context) {
    final bloc = context.read<PostSearchBloc>();
    final locales = AppLocalizations.of(context);
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
            context,
          ),
          child: Text(locales.sortTop),
        ),
        SubmenuButton(
          menuChildren: menu(
            PostSearchSort.relevance,
            (sort) => bloc.add(
              SetSort(
                sort,
              ),
            ),
            context,
          ),
          child: Text(locales.sortRelevance),
        ),
        MenuItemButton(
          onPressed: () => bloc.add(
            SetSort(PostSearchSort.hot()),
          ),
          child: Text(locales.sortHot),
        ),
        MenuItemButton(
          onPressed: () => bloc.add(
            SetSort(PostSearchSort.new_()),
          ),
          child: Text(locales.sortNew),
        ),
        SubmenuButton(
          menuChildren: menu(
            PostSearchSort.comments,
            (sort) => bloc.add(
              SetSort(sort),
            ),
            context,
          ),
          child: Text(locales.sortComments),
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
