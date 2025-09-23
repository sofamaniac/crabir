import 'dart:collection';

import 'package:auto_route/auto_route.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';

part 'filters_settings.settings_page.dart';
part 'filters_settings.freezed.dart';
part 'filters_settings.g.dart';

@freezed
@SettingsPage(prefix: "filters_", useFieldName: true)
abstract class FiltersSettings with _$FiltersSettings {
  FiltersSettings._();
  factory FiltersSettings({
    @Category(name: "Muting options")
    @Default(())
    @Setting(widget: SubredditsFilterButton, hasDescription: true)
    () manageHiddenCommunities,
    @Default(())
    @Setting(widget: DomainsFilterButton, hasDescription: true)
    () manageHiddenDomains,
    @Default(())
    @Setting(widget: UserFilterButton, hasDescription: true)
    () manageHiddenUsers,
    @Default(())
    @Setting(widget: FlairFilterButton, hasDescription: true)
    () manageHiddenFlairs,
    @Category(name: "More options")
    @Default(false)
    @Setting(hasDescription: true)
    bool showNSFW,
    @Default(true) @Setting(icon: "Icons.image") bool showImageInNSFW,
    @Setting(icon: "Icons.blur_on") @Default(false) bool blurNSFW,
  }) = _FiltersSettings;

  factory FiltersSettings.fromJson(Map<String, dynamic> json) =>
      _$FiltersSettingsFromJson(json);
}

class ReadPosts extends HydratedCubit<HashSet<String>> {
  ReadPosts() : super(HashSet());

  void mark(String postId) => state.add(postId);
  bool isRead(String postId) => state.contains(postId);

  @override
  HashSet<String> fromJson(Map<String, dynamic> json) =>
      json['read'] as HashSet<String>;

  @override
  Map<String, HashSet<String>> toJson(HashSet<String> state) => {'read': state};
}

class GlobalFilters {
  List<String> users = [];
  List<String> flairs = [];
  List<String> subreddits = [];
  List<String> domains = [];
}

class GlobalFiltersCubit extends HydratedCubit<GlobalFilters> {
  GlobalFiltersCubit() : super(GlobalFilters());

  void hideUser(String username) => state.users.add(username);
  void hideFlair(String text) => state.flairs.add(text);
  void hideSubreddit(String name) => state.subreddits.add(name);
  void hideDomain(String domain) => state.domains.add(domain);

  void setUsers(List<String> users) => state.users = users;
  void setFlairs(List<String> flairs) => state.flairs = flairs;
  void setSubreddits(List<String> subreddits) => state.subreddits = subreddits;
  void setDomains(List<String> domains) => state.domains = domains;

  bool isUserHidden(String username) => state.users.contains(username);
  bool isFlairHidden(String text) => state.flairs.contains(text);
  bool isSubredditHidden(String name) => state.subreddits.contains(name);
  bool isDomainHidden(String domain) => state.domains.contains(domain);

  bool shouldHidePost(Post post) {
    final username = post.author?.username ?? "";
    final flairText = post.linkFlair.text ?? "";
    final subreddit = post.subreddit.subreddit;
    if (username.isNotEmpty && isUserHidden(username)) {
      return true;
    } else if (flairText.isNotEmpty && isFlairHidden(flairText)) {
      return true;
    } else if (isSubredditHidden(subreddit)) {
      return true;
    } else if (isDomainHidden(Uri.parse(post.url).host)) {
      return true;
    } else {
      return false;
    }
  }

  @override
  GlobalFilters fromJson(Map<String, dynamic> json) =>
      json['hidden'] as GlobalFilters;

  @override
  Map<String, GlobalFilters> toJson(GlobalFilters state) => {'hidden': state};
}

class FilterEditor extends StatelessWidget {
  final List<String> filters;
  final void Function(List<String>) onChanged;
  final Widget title;

  const FilterEditor({
    super.key,
    required this.filters,
    required this.onChanged,
    required this.title,
  });

  @override
  Widget build(BuildContext context) {
    return AlertDialog.adaptive(
      title: title,
      actions: [
        TextButton(
          onPressed: () => Navigator.pop(context),
          child: Text("Exit"),
        )
      ],
      content: ListView.builder(
        shrinkWrap: true,
        itemCount: filters.length,
        itemBuilder: (context, index) {
          final filter = filters[index];
          return ListTile(
            title: Text(filter),
            trailing: IconButton(
              onPressed: () {
                filters.remove(filter);
                onChanged(filters);
              },
              icon: Icon(Icons.remove),
            ),
          );
        },
      ),
    );
  }
}

abstract class EditorFilterButton extends StatelessWidget {
  final () value;
  final Widget title;
  final Widget? leading;
  final void Function(()) onChanged;
  final Widget? subtitle;

  const EditorFilterButton({
    super.key,
    required this.value,
    required this.title,
    this.leading,
    required this.onChanged,
    this.subtitle,
  });

  @mustBeOverridden
  void updateField(GlobalFiltersCubit cubit, List<String> filters);

  @mustBeOverridden
  List<String> getField(BuildContext context);

  @override
  Widget build(BuildContext context) {
    final cubit = context.watch<GlobalFiltersCubit>();
    return ListTile(
      leading: leading,
      title: title,
      subtitle: subtitle,
      onTap: () => showAdaptiveDialog(
        context: context,
        builder: (context) => FilterEditor(
          filters: getField(context),
          onChanged: (newVal) => updateField(cubit, newVal),
          title: title,
        ),
      ),
    );
  }
}

class UserFilterButton extends EditorFilterButton {
  const UserFilterButton({
    super.key,
    required super.value,
    required super.title,
    required super.onChanged,
    super.leading,
    super.subtitle,
  });

  @override
  List<String> getField(BuildContext context) {
    return context.watch<GlobalFiltersCubit>().state.users;
  }

  @override
  void updateField(GlobalFiltersCubit cubit, List<String> filters) {
    cubit.setUsers(filters);
  }
}

class FlairFilterButton extends EditorFilterButton {
  const FlairFilterButton({
    super.key,
    required super.value,
    required super.title,
    required super.onChanged,
    super.leading,
    super.subtitle,
  });

  @override
  List<String> getField(BuildContext context) {
    return context.watch<GlobalFiltersCubit>().state.flairs;
  }

  @override
  void updateField(GlobalFiltersCubit cubit, List<String> filters) {
    cubit.setFlairs(filters);
  }
}

class SubredditsFilterButton extends EditorFilterButton {
  const SubredditsFilterButton({
    super.key,
    required super.value,
    required super.title,
    required super.onChanged,
    super.leading,
    super.subtitle,
  });

  @override
  List<String> getField(BuildContext context) {
    return context.watch<GlobalFiltersCubit>().state.subreddits;
  }

  @override
  void updateField(GlobalFiltersCubit cubit, List<String> filters) {
    cubit.setSubreddits(filters);
  }
}

class DomainsFilterButton extends EditorFilterButton {
  const DomainsFilterButton({
    super.key,
    required super.value,
    required super.title,
    required super.onChanged,
    super.leading,
    super.subtitle,
  });

  @override
  List<String> getField(BuildContext context) {
    return context.watch<GlobalFiltersCubit>().state.domains;
  }

  @override
  void updateField(GlobalFiltersCubit cubit, List<String> filters) {
    cubit.setDomains(filters);
  }
}
