import 'package:auto_route/auto_route.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/settings/theme/theme_event.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_colorpicker/flutter_colorpicker.dart';

@RoutePage(name: "CrabirThemeEditorPage")
class CrabirThemeEditor extends StatefulWidget {
  const CrabirThemeEditor({super.key});

  @override
  State<CrabirThemeEditor> createState() => _CrabirThemeEditorState();
}

class _CrabirThemeEditorState extends State<CrabirThemeEditor> {
  late Brightness currentBrightness;

  @override
  void initState() {
    super.initState();
    currentBrightness = switch (context.read<ThemeBloc>().state.mode) {
      ThemeMode.dark => Brightness.dark,
      ThemeMode.light => Brightness.light,
      ThemeMode.system => Theme.of(context).brightness,
    };
  }

  @override
  Widget build(BuildContext context) {
    final themeBuilder = currentBrightness == Brightness.dark
        ? ColorScheme.dark
        : ColorScheme.light;
    final themeBloc = context.watch<ThemeBloc>().state;
    final currentTheme = switch (currentBrightness) {
      Brightness.dark => themeBloc.dark,
      Brightness.light => themeBloc.light,
    };
    return Theme(
      data: ThemeData(
        useMaterial3: true,
        colorScheme: themeBuilder(
          primary: currentTheme.primaryColor,
          surface: currentTheme.background,
          secondary: currentTheme.highlight,
        ),
        cardTheme: CardThemeData(color: currentTheme.cardBackground),
      ),
      child: MediaQuery(
        data: MediaQuery.of(context)
            .copyWith(platformBrightness: currentBrightness),
        child: Scaffold(
          appBar: AppBar(
            title: DropdownMenu(
              initialSelection: currentBrightness,
              dropdownMenuEntries: [
                DropdownMenuEntry(value: Brightness.dark, label: "Dark theme"),
                DropdownMenuEntry(
                    value: Brightness.light, label: "Light theme"),
              ],
              onSelected: (brightness) => setState(() {
                currentBrightness = brightness ?? currentBrightness;
              }),
            ),
          ),
          body: ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: ThemeField.values.length,
            itemBuilder: (context, index) {
              final field = ThemeField.values[index];
              return _ColorField(
                field: field,
                brightness: currentBrightness,
              );
            },
          ),
        ),
      ),
    );
  }
}

class _ColorField extends StatelessWidget {
  final ThemeField field;
  final Brightness brightness;

  const _ColorField({required this.field, required this.brightness});

  @override
  Widget build(BuildContext context) {
    final label = field.label(context);
    final bloc = context.watch<ThemeBloc>();
    final theme = switch (brightness) {
      Brightness.dark => bloc.state.dark,
      Brightness.light => bloc.state.light,
    };
    final color = theme.fromField(field);
    return ListTile(
      //leading: CircleAvatar(backgroundColor: color),
      leading: Container(
        width: 40,
        height: 40,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: color,
          border: Border.all(
            color: Colors.grey, // or Colors.white, etc.
            width: 1,
          ),
        ),
      ),

      title: Text(label),
      onTap: () async {
        Color pickerColor = color;

        await showDialog(
          context: context,
          builder: (context) {
            return AlertDialog(
              title: Text('Pick $label'),
              content: SingleChildScrollView(
                child: BlockPicker(
                  pickerColor: pickerColor,
                  onColorChanged: (c) => pickerColor = c,
                ),
              ),
              actions: [
                TextButton(
                  child: const Text('Cancel'),
                  onPressed: () => Navigator.of(context).pop(),
                ),
                ElevatedButton(
                  child: const Text('Select'),
                  onPressed: () {
                    Navigator.of(context).pop();
                    bloc.add(
                      ThemeEvent.setColor(
                        field: field,
                        color: pickerColor,
                        brightness: brightness,
                      ),
                    );
                  },
                ),
              ],
            );
          },
        );
      },
    );
  }
}
