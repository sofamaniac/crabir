import 'package:source_gen/source_gen.dart';
import 'package:build/build.dart';
import 'package:analyzer/dart/element/element.dart';
import 'annotations.dart';

class SettingsPageGenerator extends GeneratorForAnnotation<SettingsPage> {
  @override
  String generateForAnnotatedElement(
    Element element,
    ConstantReader annotation,
    BuildStep buildStep,
  ) {
    if (element is! ClassElement) {
      throw InvalidGenerationSourceError(
        '@SettingsPage can only be applied to classes.',
        element: element,
      );
    }

    final classElement = element;
    final parameters =
        TypeChecker.fromRuntime(SettingsPage).firstAnnotationOf(element)!;
    final constructor =
        classElement.constructors.firstWhere((c) => c.isFactory);
    final className = '${classElement.name}View';

    final buffer = StringBuffer();
    buffer.writeln('// GENERATED CODE - DO NOT MODIFY BY HAND');
    buffer.writeln('import "${buildStep.inputId.uri}";');
    buffer.writeln('import "package:flutter/material.dart";');
    buffer.writeln('import "package:auto_route/auto_route.dart";');
    buffer.writeln(
        'import "package:flutter_gen/gen_l10n/app_localizations.dart";');
    buffer.writeln('');
    buffer.writeln('// SettingsPage for ${classElement.name}');
    buffer.writeln('@RoutePage()');
    buffer.writeln('class $className extends StatefulWidget {');
    buffer.writeln('  final ${classElement.name} settings;');
    buffer.writeln('  @override');
    buffer.writeln('  createState() => _${className}State();');
    buffer.writeln('  const $className({super.key, required this.settings});');
    buffer.writeln('}');
    buffer.writeln('class _${className}State extends State<$className> {');
    buffer.writeln('  late ${classElement.name} settings;');
    buffer.writeln('  @override');
    buffer.writeln('  initState() {');
    buffer.writeln('    settings = widget.settings;');
    buffer.writeln('  }');
    buffer.writeln('  @override');
    buffer.writeln('  Widget build(BuildContext context) {');
    buffer.writeln('    final locales = AppLocalizations.of(context)!;');
    buffer.writeln('    return Scaffold(body: ListView(');
    buffer.writeln('      children: [');

    /// WARN: will only work with @freezed classes.
    for (final param in constructor.parameters) {
      final setting = TypeChecker.fromRuntime(Setting).firstAnnotationOf(param);
      if (setting == null) {
        // throw InvalidGenerationSourceError(
        //   'Constructor parameter `${param.name}` in `${element.name}` must have a @Setting annotation.',
        //   element: param,
        // );
        buffer.writeln('ListTile(title: Text("TODO: ${param.name}")),');
        continue;
      }
      String tempTitle = "";
      final useFieldName =
          parameters.getField("useFieldName")?.toBoolValue() ?? false;
      if (useFieldName) {
        tempTitle = param.name;
      } else {
        try {
          tempTitle = setting.getField("titleKey")!.toStringValue()!;
        } catch (e) {
          throw InvalidGenerationSourceError(
              "Constructor parameter `${param.name}` in `${element.name}`: A title must be given if `SettingsPage.useFieldName` is not set to true.");
        }
      }
      final String titleKey;
      final prefix = parameters.getField("prefix")?.toStringValue();
      if (prefix != null) {
        titleKey = "$prefix$tempTitle";
      } else {
        titleKey = tempTitle;
      }
      final String? descKey;
      if (useFieldName) {
        descKey = "${titleKey}Description";
      } else {
        descKey = setting.getField("descriptionKey")?.toStringValue();
      }

      final widgetType = setting.getField('widget')?.toTypeValue();
      final hasDescription =
          setting.getField("hasDescription")?.toBoolValue() ?? false;

      if (widgetType != null) {
        buffer.writeln('$widgetType('
            'value: settings.${param.name},'
            'onChanged: (v) => setState(() => settings = settings.copyWith(${param.name}: v)),'
            '),');
      } else if (param.type.isDartCoreBool) {
        buffer.writeln('SwitchListTile('
            'title: Text(locales.$titleKey),'
            'subtitle: ${hasDescription ? "Text(locales.$descKey)" : "null"},'
            'value: settings.${param.name},'
            'onChanged: (v) => setState(() => settings = settings.copyWith(${param.name}: v)),'
            '),');
      }
    }

    buffer.writeln('      ],');
    buffer.writeln('    ));');
    buffer.writeln('  }');
    buffer.writeln('}');

    return buffer.toString();
  }
}
