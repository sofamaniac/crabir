import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/sort.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/comment.dart';
import 'package:crabir/src/settings_page/annotations.dart';
import 'package:crabir/thread/widgets/thread.dart';
import 'package:crabir/utils/icons.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:auto_route/annotations.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:logging/logging.dart';

part 'comments_settings.freezed.dart';
part 'comments_settings.settings_page.dart';
part 'comments_settings.g.dart';
part '__comments_sort_selection.dart';

enum IndentationStyle {
  lines,
  coloredLines,
  bars;

  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return switch (this) {
      IndentationStyle.lines => locales.indentationStyleLines,
      IndentationStyle.coloredLines => locales.indentationStyleColored,
      IndentationStyle.bars => locales.indentationStyleBars,
    };
  }
}

enum CollapseAction {
  /// Tap to collapse comment
  tap,

  /// Long press to collapse comment
  hold,
}

@freezed
@SettingsPage(prefix: "comments_", useFieldName: true)
abstract class CommentsSettings with _$CommentsSettings {
  CommentsSettings._();
  factory CommentsSettings({
    @Setting(widget: _CommentsSortSelection)
    @Default(CommentSort.top)
    CommentSort defaultSort,
    @Setting() @Default(true) bool useSuggestedSort,
    @Category(name: "Appearance")
    @Setting()
    @Default(true)
    // TODO:
    bool showNavigationBar,
    // TODO: user profile pictures (requires an additional api call)
    // @Setting() @Default(true) bool showUserAvatar,
    @Setting() @Default(true) bool showCommentsImage,
    @Setting(widget: MediaPreviewSizeSelect)
    @Default(MediaPreviewSize.thumbnail)
    MediaPreviewSize postMediaPreviewSize,
    @Setting() @Default(false) bool buttonsAlwaysVisible,
    @Setting() @Default(true) bool hideButtonAfterAction,
    // TODO:
    @Setting() @Default(true) bool collapseAutoMod,
    // TODO:
    @Setting() @Default(true) bool collapseDisruptiveComment,
    // TODO:
    @Setting() @Default(true) bool showPostUpvotePercentage,
    @Setting(widget: IndentationStyleSelect, icon: THREAD_LEVEL_INDICATOR_ICON)
    @Default(IndentationStyle.lines)
    IndentationStyle threadGuide,
    @Setting() @Default(true) bool highlightMyUsername,
    // TODO: floating button
    // @Setting() @Default(true) bool showFloatingButton,
    // TODO: Awards
    // @Category(name: "Awards") @Setting() @Default(true) bool showAwards,
    // @Setting() @Default(true) bool clickableAwards,
    @Category(name: "Flairs") @Setting() @Default(true) bool showUserFlair,
    @Setting() @Default(true) bool showFlairColors,
    @Setting() @Default(true) bool showFlairEmojis,
    // TODO:
    @Category(name: "Behavior")
    @Setting()
    @Default(CollapseAction.hold)
    CollapseAction clickToCollapse,
    // TODO:
    @Setting() @Default(true) bool hideTextCollapsed,
    // TODO:
    @Setting() @Default(true) bool loadCollapsed,
    // TODO:
    @Setting() @Default(true) bool animateCollapse,
    @Setting() @Default(true) bool clickableUsername,
    @Category(name: "Navigation")
    // TODO: highlightNewComments
    // @Setting()
    // @Default(true)
    // bool highlightNewComments,
    // TODO: navigation
    //@Default() NavigationMode defaultNavigationMode,
    // @Setting() @Default(true) bool volumeRockerNavigation,
    // @Setting() @Default(true) bool animateNavigation,
    @Category(name: "Visible buttons")
    @Setting()
    @Default(true)
    bool showSaveButton,
    // TODO:
    //@Default() GoToTopButtonAction goToTopButton,
    @Category(name: "Gestures") @Setting() @Default(true) bool swipeToClose,
    @Setting(hasDescription: true) @Default(35) int distanceThreshold,
  }) = _CommentsSetttings;
  factory CommentsSettings.fromJson(Map<String, dynamic> json) =>
      _$CommentsSetttingsFromJson(json);

  factory CommentsSettings.of(BuildContext context) =>
      context.watch<CommentsSettingsCubit>().state;
}

extension CommentsSettingsFromContext on CommentsSettings {
  CommentsSettings of(BuildContext context) {
    return context.watch<CommentsSettingsCubit>().state;
  }
}

enum MediaPreviewSize {
  none,
  thumbnail,
  fullPreview;

  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return switch (this) {
      MediaPreviewSize.none => locales.mediaPreviewSizeNone,
      MediaPreviewSize.thumbnail => locales.mediaPreviewSizeThumbnail,
      MediaPreviewSize.fullPreview => locales.mediaPreviewSizeFull,
    };
  }
}

class MediaPreviewSizeSelect extends StatelessWidget {
  final Widget title;
  final Widget? subtitle;
  final MediaPreviewSize value;
  final Widget? leading;
  final void Function(MediaPreviewSize) onChanged;
  const MediaPreviewSizeSelect({
    super.key,
    required this.title,
    required this.onChanged,
    required this.value,
    this.subtitle,
    this.leading,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: title,
      leading: leading,
      subtitle: subtitle,
      trailing: DropdownMenu(
        dropdownMenuEntries: MediaPreviewSize.values
            .map(
              (res) => DropdownMenuEntry(
                value: res,
                label: res.label(context),
              ),
            )
            .toList(),
        onSelected: (res) => onChanged(res!),
        initialSelection: value,
      ),
    );
  }
}

class IndentationStyleSelect extends StatelessWidget {
  final Widget title;
  final Widget? subtitle;
  final IndentationStyle value;
  final Widget? leading;
  final void Function(IndentationStyle) onChanged;
  const IndentationStyleSelect({
    super.key,
    required this.title,
    required this.onChanged,
    required this.value,
    this.subtitle,
    this.leading,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: title,
      leading: leading,
      subtitle: subtitle,
      trailing: DropdownMenu(
        dropdownMenuEntries: IndentationStyle.values
            .map(
              (res) => DropdownMenuEntry(
                value: res,
                label: res.label(context),
              ),
            )
            .toList(),
        onSelected: (res) => onChanged(res!),
        initialSelection: value,
      ),
    );
  }
}
