import 'package:auto_route/annotations.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:crabir/l10n/app_localizations.dart';

part 'posts_settings.freezed.dart';
part 'posts_settings.settings_page.dart';
part 'posts_settings.g.dart';

@freezed
@SettingsPage(prefix: "posts_", useFieldName: true)
abstract class PostsSettings with _$PostsSettings {
  PostsSettings._();
  factory PostsSettings({
    @Setting(widget: _SortSelection)
    @Default(FeedSort.best())
    FeedSort defaultHomeSort,
    @Setting(widget: _SortSelection)
    @Default(FeedSort.hot())
    FeedSort defaultSort,
    @Setting(hasDescription: true) @Default(true) bool rememberSortByCommunity,
    // TODO: manage saved sort
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
}

class _SortSelection extends StatelessWidget {
  final Widget title;
  final Widget? subtitle;
  final FeedSort value;
  final void Function(FeedSort) onChanged;

  const _SortSelection({
    required this.value,
    required this.onChanged,
    required this.title,
    this.subtitle,
  });

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

  Widget _buildOption(BuildContext context, FeedSort sort) {
    return ListTile(
      title: Text(sort.label(context)),
      onTap: () => Navigator.of(context).pop(sort),
    );
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: title,
      subtitle: Text(value.label(context)),
      onTap: () async {
        final sort = await _showFeedSortDialog(context);
        if (sort != null) {
          onChanged(sort);
        }
      },
    );
  }
}
