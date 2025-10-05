import 'package:auto_route/auto_route.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/routes/routes.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/settings/lateral_menu/lateral_menu_settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/settings/theme/theme_event.dart';
import 'package:crabir/utils/brightness_extension.dart';
import 'package:crabir/utils/icons.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

List<Widget> options(BuildContext context) {
  final settings = LateralMenuSettings.of(context);
  final locales = AppLocalizations.of(context);
  final theme = CrabirTheme.of(context);
  Icon icon(IconData icon) => Icon(icon, color: theme.secondaryText);
  final children = [
    if (settings.showGoToDropdown) GoToDropdown(),
    if (settings.showGoToCommunity)
      ListTile(
        leading: icon(GOTO_COMMUNITY_ICON),
        title: Text(locales.lateralMenu_showGoToCommunity),
        onTap: () => context.router.navigate(SubscriptionsTabRoute()),
      ),
    if (settings.showGoToUser)
      ListTile(
        leading: icon(GOTO_USER_ICON),
        title: Text(locales.lateralMenu_showGoToUser),
      ),
    if (settings.darkMode)
      SwitchListTile(
        secondary: icon(DARK_MODE_ICON),
        title: Text(locales.lateralMenu_darkMode),
        value: context.brightness == Brightness.dark,
        onChanged: (enable) {
          if (enable) {
            context
                .read<ThemeBloc>()
                .add(ThemeEvent.setMode(mode: ThemeMode.dark));
          } else {
            context
                .read<ThemeBloc>()
                .add(ThemeEvent.setMode(mode: ThemeMode.light));
          }
        },
      ),
    if (settings.showNSFW)
      SwitchListTile(
        secondary: icon(SHOW_NSFW_ICON),
        title: Text(locales.filters_showNSFW),
        value: FiltersSettings.of(context).showNSFW,
        onChanged: (enable) {
          context.read<FiltersSettingsCubit>().updateShowNSFW(enable);
        },
      ),
    if (settings.blurNSFW)
      SwitchListTile(
        secondary: icon(BLUR_NSFW_ICON),
        title: Text(locales.filters_blurNSFW),
        value: FiltersSettings.of(context).blurNSFW,
        onChanged: (enable) {
          context.read<FiltersSettingsCubit>().updateBlurNSFW(enable);
        },
      ),
  ];
  if (children.isNotEmpty) {
    children.add(Divider());
  }
  return children;
}

class GoToDropdown extends StatefulWidget {
  const GoToDropdown({super.key});

  @override
  State<GoToDropdown> createState() => _GoToDropdownState();
}

class _GoToDropdownState extends State<GoToDropdown> {
  bool open = false;
  @override
  Widget build(BuildContext context) {
    final icon = open ? Icons.arrow_drop_up : Icons.arrow_drop_down;
    final theme = CrabirTheme.of(context);
    final locales = AppLocalizations.of(context);
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        ListTile(
          leading: Icon(GOTO_ICON, color: theme.secondaryText),
          title: Text(locales.gotoDropdownLabel),
          onTap: () => setState(
            () {
              open = !open;
            },
          ),
          trailing: Icon(icon),
        ),
        AnimatedSize(
          duration: Duration(milliseconds: 100),
          child: Align(
            alignment: Alignment.topCenter,
            child: open
                ? Column(
                    children: [
                      ListTile(
                        leading: Icon(null),
                        title: Text(locales.lateralMenu_showGoToCommunity),
                        onTap: () {
                          Scaffold.of(context).closeDrawer();
                          context.router.navigate(SubscriptionsTabRoute());
                        },
                      ),
                      ListTile(
                        leading: Icon(null),
                        title: Text(locales.lateralMenu_showGoToUser),
                        onTap: () {
                          Scaffold.of(context).closeDrawer();
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(
                              content: Text("Not implemented"),
                            ),
                          );
                        },
                      )
                    ],
                  )
                : SizedBox.shrink(),
          ),
        ),
      ],
    );
  }
}
