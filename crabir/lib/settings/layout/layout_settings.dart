import 'package:collection/collection.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/settings/settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/src/go_router_ext/annotations.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/multi.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:go_router/go_router.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';

part 'layout_settings.g.dart';
part 'layout_settings.settings_page.dart';
part 'layout_settings.go_route_ext.dart';
part 'layout_settings.freezed.dart';

enum ViewKind {
  card,
  compact,
  dense;

  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return switch (this) {
      ViewKind.card => locales.viewCard,
      ViewKind.compact => locales.viewCompact,
      ViewKind.dense => locales.viewDense,
    };
  }
}

final _multiPrefix = "_MULTI_";

@JsonSerializable()
class RememberedView {
  final Map<String, ViewKind> data;
  const RememberedView({this.data = const {}});

  factory RememberedView.fromJson(Map<String, dynamic> json) =>
      _$RememberedViewFromJson(json);

  Map<String, dynamic> toJson() => _$RememberedViewToJson(this);

  RememberedView addFeed(Feed feed, ViewKind kind) {
    Map<String, ViewKind> newData = Map.from(data);
    newData[_feedToString(feed)] = kind;
    return RememberedView(data: newData);
  }

  RememberedView addMulti(Multi multi, ViewKind kind) {
    Map<String, ViewKind> newData = Map.from(data);
    newData[_multiToString(multi)] = kind;
    return RememberedView(data: newData);
  }

  RememberedView removeView(String feed) {
    Map<String, ViewKind> newData = Map.from(data);
    newData.remove(feed);
    return RememberedView(data: newData);
  }

  ViewKind? containsFeed(Feed feed) {
    return data[_feedToString(feed)];
  }

  ViewKind? containsMulti(Multi multi) {
    return data[_multiToString(multi)];
  }

  Iterable<(String, ViewKind)> inOrder() {
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

@freezed
@SettingsPage(prefix: "layout_", useFieldName: true)
abstract class LayoutSettings with _$LayoutSettings {
  factory LayoutSettings({
    @Setting(widget: _ViewKindSelection)
    @Default(ViewKind.card)
    ViewKind defaultView,
    @Setting() @Default(false) bool rememberByCommunity,
    @Setting(widget: _ManageViewButton)
    @Default(RememberedView())
    RememberedView rememberedView,
    @Category() @Setting() @Default(()) () font,
    @Setting() @Default(true) bool showThumbnail,
    @Setting(dependsOn: "showThumbnail") @Default(false) bool thumbnailOnLeft,
    @Setting() @Default(false) bool prefixCommunities,
    @Category(name: "Cards") @Setting() @Default(true) bool previewText,
    @Setting(dependsOn: "previewText", widget: _LengthSelection)
    @Default(5)
    int previewTextLength,
  }) = _LayoutSettings;
  factory LayoutSettings.fromJson(Map<String, dynamic> json) =>
      _$LayoutSettingsFromJson(json);
  factory LayoutSettings.of(BuildContext context) =>
      context.watch<LayoutSettingsCubit>().state;
  LayoutSettings._();
}

class _LengthSelection extends SettingButton<int> {
  const _LengthSelection({
    super.subtitle,
    super.leading,
    super.onChanged,
    required super.title,
    required super.value,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: leading,
      title: title,
      subtitle: TextFormField(
        initialValue: "$value",
        onChanged: (val) => onChanged?.call(int.parse(val)),
        keyboardType: TextInputType.number,
        inputFormatters: [
          FilteringTextInputFormatter.digitsOnly
        ], // Only numbers can be entered
      ),
    );
  }
}

class TextInputFormatter {}

class _ViewKindSelection extends StatelessWidget {
  final Widget title;
  final Widget? subtitle;
  final ViewKind value;
  final Widget? leading;
  final void Function(ViewKind) onChanged;

  const _ViewKindSelection({
    required this.title,
    this.subtitle,
    required this.value,
    this.leading,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: leading,
      title: title,
      subtitle: subtitle,
      trailing: DropdownMenu<ViewKind>(
        dropdownMenuEntries: ViewKind.values
            .map(
              (kind) => DropdownMenuEntry(
                value: kind,
                label: kind.label(context),
              ),
            )
            .toList(),
        onSelected: (kind) => onChanged(kind!),
        initialSelection: value,
      ),
    );
  }
}

class _ManageViewButton extends SettingButton<RememberedView> {
  const _ManageViewButton({
    required super.title,
    super.subtitle,
    required super.onChanged,
    required super.value,
    super.leading,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text("Manage Sorts"),
      leading: leading,
      onTap: () => ManageRememberedView().pushNamed(context),
    );
  }
}

@CrabirRoute()
class ManageRememberedView extends StatelessWidget {
  const ManageRememberedView({super.key});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<LayoutSettingsCubit>();
    final data = settings.state.rememberedView;
    final feeds = data.inOrder().toList();
    final locales = AppLocalizations.of(context);
    return Scaffold(
      body: ListView.builder(
        itemCount: feeds.length,
        itemBuilder: (context, index) {
          final (feed, view) = feeds[index];
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
            subtitle: Text(view.label(context)),
            trailing: IconButton(
              onPressed: () {
                settings.updateRememberedView(data.removeView(feed));
              },
              icon: Icon(Icons.remove),
            ),
          );
        },
      ),
    );
  }
}
