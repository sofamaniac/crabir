import 'package:source_gen/source_gen.dart';
import 'package:build/build.dart';
import 'package:analyzer/dart/element/element.dart';
import 'annotations.dart';

class CubitGenerator {
  CubitGenerator();

  String makeCubit(Element element) {
    final classElement = element as ClassElement;
    final cubitName = "${classElement.name}Cubit";
    final constructor =
        classElement.constructors.firstWhere((c) => c.isFactory);
    StringBuffer buffer = StringBuffer();
    buffer.write('''
// HydratedCubit for ${classElement.name}
class $cubitName extends HydratedCubit<${classElement.name}> {
  $cubitName() : super(${classElement.name}());

  @override
  ${classElement.name}? fromJson(Map<String, dynamic> json) {
    try {
      return ${classElement.name}.fromJson(json);
    } catch (_) {
      return ${classElement.name}();
    }
  }

  @override
  Map<String, dynamic>? toJson(${classElement.name} state) => state.toJson();
''');

    // generate methods for each field
    /// WARN: will only work with @freezed classes.
    for (final param in constructor.parameters) {
      final name = param.name;
      final type = param.type;

      buffer.writeln('''
  void update${name.toPascalCase()}($type value) => emit(state.copyWith($name: value));
''');
    }
    buffer.writeln('}'); // close cubit class
    return buffer.toString();
  }
}

class SettingsPageGenerator extends GeneratorForAnnotation<SettingsPage> {
  late String? prefix;
  late bool useFieldName;
  late String cubitName;

  void initialize(Element element) {
    final parameters =
        TypeChecker.fromRuntime(SettingsPage).firstAnnotationOf(element)!;
    prefix = parameters.getField("prefix")?.toStringValue();
    useFieldName = parameters.getField("useFieldName")?.toBoolValue() ?? false;
    cubitName = "${element.name}Cubit";
  }

  String imports(BuildStep buildStep) {
    return '''
// GENERATED CODE - DO NOT MODIFY BY HAND
part of "${buildStep.inputId.uri.pathSegments.last}";
    ''';
  }

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

    initialize(element);

    final classElement = element;
    final constructor =
        classElement.constructors.firstWhere((c) => c.isFactory);
    final className = '${classElement.name}View';

    final buffer = StringBuffer();
    buffer.write(imports(buildStep));
    buffer.write(CubitGenerator().makeCubit(element));
    final code = '''
// SettingsPage for ${classElement.name}
@RoutePage()
class $className extends StatelessWidget {

  const $className({super.key});


  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final settings = context.watch<$cubitName>().state;
    return Scaffold(
      body: ListView(
        children: [
''';

    buffer.writeln(code);

    /// WARN: will only work with @freezed classes.
    for (final param in constructor.parameters) {
      final divider =
          TypeChecker.fromRuntime(Category).firstAnnotationOf(param);
      if (divider != null) {
        buffer.writeln("Divider(),");
        final categoryName = divider.getField("name")?.toStringValue();
        if (categoryName != null) {
          buffer.writeln('Text("$categoryName"),');
        }
      }
      buffer.writeln(widget(param, element));
    }

    buffer.writeln('      ],');
    buffer.writeln('    ));');
    buffer.writeln('  }');
    buffer.writeln('}');

    return buffer.toString();
  }

  String widget(ParameterElement param, Element element) {
    final setting = TypeChecker.fromRuntime(Setting).firstAnnotationOf(param);
    if (setting == null) {
      return 'ListTile(title: Text("TODO: ${param.name}")),';
    }
    String tempTitle = "";
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
      return '$widgetType('
          'title: Text(locales.$titleKey),'
          'subtitle: ${hasDescription ? "Text(locales.$descKey)" : "null"},'
          'value: settings.${param.name},'
          'onChanged: (val) => context.read<$cubitName>().update${param.name.toPascalCase()}(val),'
          '),';
    } else if (param.type.isDartCoreBool) {
      return 'CheckboxListTile('
          'title: Text(locales.$titleKey),'
          'subtitle: ${hasDescription ? "Text(locales.$descKey)" : "null"},'
          'value: settings.${param.name},'
          'onChanged: (val) => context.read<$cubitName>().update${param.name.toPascalCase()}(val!),'
          '),';
    }
    return "";
  }
}

String pascalCase(String s) {
  return '${s[0].toUpperCase()}${s.substring(1)}';
}

extension PascalCase on String {
  String toPascalCase() {
    return '${this[0].toUpperCase()}${substring(1)}';
  }
}
