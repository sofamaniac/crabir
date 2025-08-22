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
    final constructor =
        classElement.constructors.firstWhere((c) => c.isFactory);

    final buffer = StringBuffer();
    buffer.writeln('// GENERATED CODE - DO NOT MODIFY BY HAND');
    buffer.writeln('import "${buildStep.inputId.uri}";');
    buffer.writeln('import "package:flutter/material.dart";');
    buffer.writeln('import "package:auto_route/auto_route.dart";');
    buffer.writeln('');
    buffer.writeln('// SettingsPage for ${classElement.name}');
    buffer.writeln('@RoutePage()');
    buffer.writeln('class ${classElement.name}View extends StatelessWidget {');
    buffer.writeln('  final ${classElement.name} settings;');
    buffer.writeln(
        '  const ${classElement.name}View({super.key, required this.settings});');
    buffer.writeln('  @override');
    buffer.writeln('  Widget build(BuildContext context) {');
    buffer.writeln('    return Scaffold(body: ListView(');
    buffer.writeln('      children: [');

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
      final titleKey = setting.getField('titleKey')?.toStringValue();
      final descKey = setting.getField('descriptionKey')?.toStringValue();
      final widgetType = setting.getField('widget')?.toTypeValue();

      if (widgetType != null) {
        buffer.writeln('$widgetType('
            'value: settings.${param.name},'
            'onChanged: (v) => setState(() => settings = settings.copyWith(${param.name}: v)),'
            '),');
      } else if (param.type.isDartCoreBool) {
        buffer.writeln('SwitchListTile('
            'title: Text(locales.$titleKey),'
            'subtitle: ${descKey != null ? "Text(locales.$descKey)" : "null"},'
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
