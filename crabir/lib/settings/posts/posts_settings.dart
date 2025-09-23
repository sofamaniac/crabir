import 'package:auto_route/auto_route.dart';
import 'package:collection/collection.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';

part 'posts_settings.freezed.dart';
part 'posts_settings.g.dart';
part 'posts_settings.settings_page.dart';

final _multiPrefix = "_MULTI_";

class ManageSortButton extends StatelessWidget {
  final Widget title;
  final Widget? subtitle;
  final Widget? leading;
  final void Function(RememberedSort) onChanged;
  final RememberedSort value;
  const ManageSortButton({
    super.key,
    required this.title,
    this.subtitle,
    required this.onChanged,
    required this.value,
    this.leading,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text("Manage Sorts"),
      leading: leading,
      onTap: () => context.router.navigate(ManageSortRoute()),
    );
  }
}

@RoutePage()
class ManageSortView extends StatelessWidget {
  const ManageSortView({super.key});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<PostsSettingsCubit>();
    final data = settings.state.rememberedSorts;
    final feeds = data.inOrder().toList();
    final locales = AppLocalizations.of(context);
    return Scaffold(
      body: ListView.builder(
        itemCount: feeds.length,
        itemBuilder: (context, index) {
          final (feed, sort) = feeds[index];
          final Widget title = switch (feed) {
            "_HOME" => Text(locales.feedHome),
            "_ALL" => Text(locales.feedAll),
            "_POPULAR" => Text(locales.feedPopular),
            _ when feed.startsWith(_multiPrefix) => RichText(
                text: TextSpan(
                  children: [
                    TextSpan(text: feed.substring(_multiPrefix.length)),
                    TextSpan(text: "feed")
                  ],
                ),
              ),
            _ => Text(feed),
          };
          return ListTile(
            title: title,
            subtitle: Text(sort.labelWithTimeframe(context)),
            trailing: IconButton(
              onPressed: () {
                settings.updateRememberedSorts(data.removeSort(feed));
              },
              icon: Icon(Icons.remove),
            ),
          );
        },
      ),
    );
  }
}

@freezed
@SettingsPage(prefix: "posts_", useFieldName: true)
abstract class PostsSettings with _$PostsSettings {
  factory PostsSettings({
    @Setting(widget: _SortSelection)
    @Default(FeedSort.best())
    FeedSort defaultHomeSort,
    @Setting(widget: _SortSelection)
    @Default(FeedSort.hot())
    FeedSort defaultSort,
    @Setting(hasDescription: true) @Default(true) bool rememberSortByCommunity,
    @Setting(widget: ManageSortButton)
    @Default(RememberedSort())
    RememberedSort rememberedSorts,
    @Category(name: "Awards") @Setting() @Default(true) bool showAwards,
    @Setting() @Default(true) bool clickableAwards,
    @Category(name: "Flairs") @Setting() @Default(true) bool showPostFlair,
    @Setting() @Default(true) bool showFlairColors,
    @Setting() @Default(true) bool showEmojis,
    @Setting() @Default(true) bool tapFlairToSearch,
    @Category(name: "Post info") @Setting() @Default(true) bool showAuthor,
    @Setting() @Default(true) bool clickableCommunity,
    @Setting() @Default(true) bool clickableUser,
    @Category(name: "Visible buttons")
    @Setting()
    @Default(true)
    bool showFloatingButton,
    @Setting() @Default(false) bool showHideButton,
    @Setting() @Default(false) bool showMarkAsReadButton,
    @Setting() @Default(false) bool showShareButton,
    @Setting() @Default(true) bool showCommentsButton,
    @Setting() @Default(true) bool showOpenInAppButton,
  }) = _PostsSettings;
  factory PostsSettings.fromJson(Map<String, dynamic> json) =>
      _$PostsSettingsFromJson(json);
  factory PostsSettings.of(BuildContext context) =>
      context.watch<PostsSettingsCubit>().state;
  PostsSettings._();
}

@JsonSerializable()
class RememberedSort {
  final Map<String, FeedSort> data;
  const RememberedSort({this.data = const {}});

  factory RememberedSort.fromJson(Map<String, dynamic> json) =>
      _$RememberedSortFromJson(json);

  Map<String, dynamic> toJson() => _$RememberedSortToJson(this);

  RememberedSort addFeed(Feed feed, FeedSort sort) {
    Map<String, FeedSort> newData = Map.from(data);
    newData[_feedToString(feed)] = sort;
    return RememberedSort(data: newData);
  }

  RememberedSort addMulti(Multi multi, FeedSort sort) {
    Map<String, FeedSort> newData = Map.from(data);
    newData[_multiToString(multi)] = sort;
    return RememberedSort(data: newData);
  }

  RememberedSort removeSort(String feed) {
    Map<String, FeedSort> newData = Map.from(data);
    newData.remove(feed);
    return RememberedSort(data: newData);
  }

  FeedSort? containsFeed(Feed feed) {
    return data[_feedToString(feed)];
  }

  FeedSort? containsMulti(Multi multi) {
    return data[_multiToString(multi)];
  }

  Iterable<(String, FeedSort)> inOrder() {
    final keys = data.keys.sorted();
    return keys.map((k) => (k, data[k]!));
  }

  String _feedToString(Feed feed) {
    return switch (feed) {
      Feed_Home() => "_HOME",
      Feed_All() => "_ALL",
      Feed_Popular() => "_POPULAR",
      Feed_Subreddit(field0: final subreddit) => subreddit,
    };
  }

  String _multiToString(Multi multi) {
    return "$_multiPrefix${multi.displayName}";
  }
}

class _SortSelection extends StatelessWidget {
  final Widget title;
  final Widget? subtitle;
  final FeedSort value;
  final Widget? leading;
  final void Function(FeedSort) onChanged;

  const _SortSelection({
    required this.value,
    required this.onChanged,
    required this.title,
    this.subtitle,
    this.leading,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: title,
      leading: leading,
      subtitle: Text(value.label(context)),
      onTap: () async {
        final sort = await _showFeedSortDialog(context);
        if (sort != null) {
          onChanged(sort);
        }
      },
    );
  }

  Widget _buildOption(BuildContext context, FeedSort sort) {
    return ListTile(
      title: Text(sort.label(context)),
      onTap: () => Navigator.of(context).pop(sort),
    );
  }

  Future<FeedSort?> _showFeedSortDialog(BuildContext context) async {
    return showDialog<FeedSort>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text("Select sort"),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                _buildOption(context, const FeedSort.best()),
                _buildOption(context, const FeedSort.hot()),
                _buildOption(context, const FeedSort.rising()),
                const Divider(),
                ExpansionTile(
                  title: const Text("New"),
                  children: Timeframe.values
                      .map(
                        (t) => _buildOption(
                          context,
                          FeedSort.new_(t),
                        ),
                      )
                      .toList(),
                ),
                ExpansionTile(
                  title: const Text("Top"),
                  children: Timeframe.values
                      .map(
                        (t) => _buildOption(
                          context,
                          FeedSort.top(t),
                        ),
                      )
                      .toList(),
                ),
                ExpansionTile(
                  title: const Text("Controversial"),
                  children: Timeframe.values
                      .map(
                        (t) => _buildOption(
                          context,
                          FeedSort.controversial(t),
                        ),
                      )
                      .toList(),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
