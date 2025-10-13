import 'dart:collection';

import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/flair.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:crabir/utils/icons.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:go_router/go_router.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';

part 'filters_settings.settings_page.dart';
part 'filters_settings.freezed.dart';
part 'filters_settings.g.dart';
part 'filters_settings.go_route_ext.dart';

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
    @Setting(widget: UsersFilterButton, hasDescription: true)
    () manageHiddenUsers,
    @Default(())
    @Setting(widget: FlairsFilterButton, hasDescription: true)
    () manageHiddenFlairs,
    @Category(name: "More options")
    @Default(false)
    @Setting(hasDescription: true)
    bool showNSFW,
    @Default(true) @Setting(icon: SHOW_NSFW_ICON) bool showImageInNSFW,
    @Setting(icon: BLUR_NSFW_ICON) @Default(false) bool blurNSFW,
  }) = _FiltersSettings;

  factory FiltersSettings.fromJson(Map<String, dynamic> json) =>
      _$FiltersSettingsFromJson(json);

  factory FiltersSettings.of(BuildContext context) =>
      context.watch<FiltersSettingsCubit>().state;
}

class ReadPosts extends HydratedCubit<HashSet<String>> {
  ReadPosts() : super(HashSet());

  void mark(PostId postId) => state.add(postId.asString);
  void unmark(PostId postId) => state.remove(postId.asString);
  bool isRead(PostId postId) => state.contains(postId.asString);

  @override
  HashSet<String> fromJson(Map<String, dynamic> json) =>
      json['read'] as HashSet<String>;

  @override
  Map<String, HashSet<String>> toJson(HashSet<String> state) => {'read': state};

  factory ReadPosts.of(BuildContext context) {
    return context.watch<ReadPosts>();
  }
}

@JsonSerializable()
class GlobalFilters {
  List<String> users;
  List<Flair> flairs;
  List<String> subreddits;
  List<String> domains;

  GlobalFilters({
    this.users = const [],
    this.flairs = const [],
    this.subreddits = const [],
    this.domains = const [],
  });

  factory GlobalFilters.of(BuildContext context) =>
      context.watch<GlobalFiltersCubit>().state;

  bool _emptyString(String? s) => (s ?? "").isNotEmpty;

  bool isUserHidden(String? username) =>
      _emptyString(username) && users.contains(username);
  bool isFlairHidden(Flair flair) =>
      flairs.any((f) => f.templateId == flair.templateId);
  bool isSubredditHidden(String? name) =>
      _emptyString(name) && subreddits.contains(name);
  bool isDomainHidden(String? domain) =>
      _emptyString(domain) && domains.contains(domain);

  bool shouldHidePost(Post post) {
    final username = post.author?.username ?? "";
    final flair = post.linkFlair;
    final subreddit = post.subreddit.subreddit;
    final domain = Uri.parse(post.url).host;
    return isUserHidden(username) ||
        isFlairHidden(flair) ||
        isSubredditHidden(subreddit) ||
        isDomainHidden(domain);
  }

  GlobalFilters copyWith({
    List<String>? users,
    List<Flair>? flairs,
    List<String>? subreddits,
    List<String>? domains,
  }) {
    return GlobalFilters(
      users: users ?? this.users,
      flairs: flairs ?? this.flairs,
      subreddits: subreddits ?? this.subreddits,
      domains: domains ?? this.domains,
    );
  }

  factory GlobalFilters.fromJson(Map<String, dynamic> json) =>
      _$GlobalFiltersFromJson(json);

  Map<String, dynamic> toJson() => _$GlobalFiltersToJson(this);
}

class GlobalFiltersCubit extends HydratedCubit<GlobalFilters> {
  GlobalFiltersCubit() : super(GlobalFilters());

  void hideUser(String username) {
    if (state.users.contains(username)) {
      return;
    }
    emit(state.copyWith(users: [...state.users, username]));
  }

  void hideFlair(Flair flair) {
    if (state.flairs.contains(flair)) {
      return;
    }
    emit(state.copyWith(flairs: [...state.flairs, flair]));
  }

  void hideSubreddit(String subreddit) {
    if (state.subreddits.contains(subreddit)) {
      return;
    }
    emit(state.copyWith(subreddits: [...state.subreddits, subreddit]));
  }

  void hideDomain(String domain) {
    if (state.domains.contains(domain)) {
      return;
    }
    emit(state.copyWith(domains: [...state.domains, domain]));
  }

  void setUsers(List<String> users) => emit(state.copyWith(users: users));
  void setFlairs(List<Flair> flairs) => emit(state.copyWith(flairs: flairs));
  void setSubreddits(List<String> subreddits) =>
      emit(state.copyWith(subreddits: subreddits));
  void setDomains(List<String> domains) =>
      emit(state.copyWith(domains: domains));

  @override
  GlobalFilters fromJson(Map<String, dynamic> json) =>
      GlobalFilters.fromJson(json);

  @override
  Map<String, dynamic> toJson(GlobalFilters state) => state.toJson();
}

class FilterEditor extends StatelessWidget {
  final List<Widget> filters;
  final Widget title;

  const FilterEditor({
    super.key,
    required this.filters,
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
        itemBuilder: (context, index) => filters[index],
      ),
    );
  }
}

class SubredditsFilterButton extends StatelessWidget {
  final Widget title;
  final Widget leading;
  final Widget? subtitle;
  final () value;
  final void Function(()) onChanged;
  const SubredditsFilterButton({
    super.key,
    required this.title,
    required this.leading,
    this.subtitle,
    required this.value,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    final cubit = context.watch<GlobalFiltersCubit>();
    final subreddits = cubit.state.subreddits;
    return ListTile(
      leading: leading,
      title: title,
      subtitle: subtitle,
      onTap: () {
        showDialog(
          context: context,
          builder: (context) => FilterEditor(
            filters: subreddits
                .map(
                  (s) => ListTile(
                    leading: IconButton(
                      icon: Icon(Icons.remove),
                      onPressed: () {
                        subreddits.remove(s);
                        cubit.setSubreddits(subreddits);
                      },
                    ),
                    title: Text(s),
                  ),
                )
                .toList(),
            title: title,
          ),
        );
      },
    );
  }
}

class UsersFilterButton extends StatelessWidget {
  final Widget title;
  final Widget leading;
  final Widget? subtitle;
  final () value;
  final void Function(()) onChanged;
  const UsersFilterButton({
    super.key,
    required this.title,
    required this.leading,
    this.subtitle,
    required this.value,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    final cubit = context.watch<GlobalFiltersCubit>();
    final users = cubit.state.users;
    return ListTile(
      leading: leading,
      title: title,
      subtitle: subtitle,
      onTap: () {
        showDialog(
          context: context,
          builder: (context) => FilterEditor(
            filters: users
                .map(
                  (u) => ListTile(
                    leading: IconButton(
                      icon: Icon(Icons.remove),
                      onPressed: () {
                        users.remove(u);
                        cubit.setUsers(users);
                      },
                    ),
                    title: Text(u),
                  ),
                )
                .toList(),
            title: title,
          ),
        );
      },
    );
  }
}

class DomainsFilterButton extends StatelessWidget {
  final Widget title;
  final Widget leading;
  final Widget? subtitle;
  final () value;
  final void Function(()) onChanged;
  const DomainsFilterButton({
    super.key,
    required this.title,
    required this.leading,
    this.subtitle,
    required this.value,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    final cubit = context.watch<GlobalFiltersCubit>();
    final domains = cubit.state.domains;
    return ListTile(
      leading: leading,
      title: title,
      subtitle: subtitle,
      onTap: () {
        showDialog(
          context: context,
          builder: (context) => FilterEditor(
            filters: domains
                .map(
                  (d) => ListTile(
                    leading: IconButton(
                      icon: Icon(Icons.remove),
                      onPressed: () {
                        domains.remove(d);
                        cubit.setDomains(domains);
                      },
                    ),
                    title: Text(d),
                  ),
                )
                .toList(),
            title: title,
          ),
        );
      },
    );
  }
}

class FlairsFilterButton extends StatelessWidget {
  final Widget title;
  final Widget leading;
  final Widget? subtitle;
  final () value;
  final void Function(()) onChanged;
  const FlairsFilterButton({
    super.key,
    required this.title,
    required this.leading,
    this.subtitle,
    required this.value,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    final cubit = context.watch<GlobalFiltersCubit>();
    final flairs = cubit.state.flairs;
    return ListTile(
      leading: leading,
      title: title,
      subtitle: subtitle,
      onTap: () {
        showDialog(
          context: context,
          builder: (context) => FilterEditor(
            filters: flairs
                .map(
                  (f) => ListTile(
                    leading: IconButton(
                      icon: Icon(Icons.remove),
                      onPressed: () {
                        flairs.remove(f);
                        cubit.setFlairs(flairs);
                      },
                    ),
                    title: Text(f.text ?? "NO TEXT"),
                  ),
                )
                .toList(),
            title: title,
          ),
        );
      },
    );
  }
}
