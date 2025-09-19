import 'package:auto_route/auto_route.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/settings/theme/theme_event.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_colorpicker/flutter_colorpicker.dart';

@RoutePage(name: "CrabirThemeEditorPage")
class CrabirThemeEditor extends StatelessWidget {
  const CrabirThemeEditor({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("TODO")),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: ThemeField.values.length,
        itemBuilder: (context, index) {
          final field = ThemeField.values[index];
          return _ColorField(field: field);
        },
      ),
    );
  }
}

class _ColorField extends StatelessWidget {
  final ThemeField field;

  const _ColorField({required this.field});

  @override
  Widget build(BuildContext context) {
    final label = field.label(context);
    final color = CrabirTheme.of(context).fromField(field);
    final bloc = context.watch<ThemeBloc>();
    final brightness = MediaQuery.of(context).platformBrightness;
    return ListTile(
      //leading: CircleAvatar(backgroundColor: color),
      leading: Container(
        width: 40,
        height: 40,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: color,
          border: Border.all(
            color: Theme.of(context).dividerColor, // or Colors.white, etc.
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
