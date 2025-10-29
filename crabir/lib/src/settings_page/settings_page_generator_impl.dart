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
  final Logger log = Logger("$cubitName");
  $cubitName() : super(${classElement.name}());

  @override
  ${classElement.name}? fromJson(Map<String, dynamic> json) {
    try {
      log.info("Successfully restored $cubitName");
      return ${classElement.name}.fromJson(json);
    } catch (err) {
      log.severe("Failed to resotre $cubitName: \$err");
      return ${classElement.name}();
    }
  }

  @override
  Map<String, dynamic>? toJson(${classElement.name} state) => state.toJson();
''');

    // generate methods for each field
    /// WARN: will only work with @freezed classes.
    for (final param in constructor.formalParameters) {
      final name = param.name!;
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
        TypeChecker.typeNamed(SettingsPage).firstAnnotationOf(element)!;
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
@CrabirRoute()
// SettingsPage for ${classElement.name}
class $className extends StatelessWidget {

  const $className({super.key});


  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final settings = context.watch<$cubitName>().state;
    return Scaffold(
      appBar: AppBar(),
      body: ListView(
        children: [
''';

    buffer.writeln(code);

    /// WARN: will only work with @freezed classes.
    for (final param in constructor.formalParameters) {
      final divider = TypeChecker.typeNamed(Category).firstAnnotationOf(param);
      if (divider != null &&
          (divider.getField("divider")?.toBoolValue() ?? true)) {
        buffer.writeln("Divider(),");
        final categoryName = divider.getField("name")?.toStringValue();
        if (categoryName != null) {
          buffer.writeln(
              'ListTile(leading: Icon(null), title: Text("$categoryName", style: Theme.of(context)'
              '.textTheme'
              '.labelMedium'
              '?.copyWith(color: CrabirTheme.of(context).highlight)))'
              ',');
        }
      }
      buffer.writeln(widget(param, element));
    }

    buffer.writeln('      ],');
    buffer.writeln('    ),);');
    buffer.writeln('  }');
    buffer.writeln('}');

    return buffer.toString();
  }

  String widget(FormalParameterElement param, Element element) {
    final setting = TypeChecker.typeNamed(Setting).firstAnnotationOf(param);
    if (setting == null) {
      return 'ListTile(title: Text("TODO: ${param.name}")),';
    }
    String tempTitle = "";
    if (useFieldName) {
      tempTitle = param.name!;
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
    final iconData = setting.getField('icon')?.variable?.name;

    final dependsOn = setting.getField("dependsOn")?.toStringValue() ?? "";
    final isBool = param.type.isDartCoreBool;
    final onChangedFunc =
        '(val) => context.read<$cubitName>().update${param.name!.toPascalCase()}(val${isBool ? '!' : ''}) ';
    final String onChanged;
    if (dependsOn.isNotEmpty) {
      onChanged = 'settings.$dependsOn ? $onChangedFunc : null';
    } else {
      onChanged = onChangedFunc;
    }

    if (widgetType != null) {
      return '$widgetType('
          'title: Text(locales.$titleKey),'
          'leading: Icon($iconData),'
          'subtitle: ${hasDescription ? "Text(locales.$descKey)" : "null"},'
          'value: settings.${param.name},'
          'onChanged: $onChanged,'
          '),';
    } else if (isBool) {
      return 'CheckboxListTile('
          'title: Text(locales.$titleKey),'
          'secondary: Icon($iconData),'
          'subtitle: ${hasDescription ? "Text(locales.$descKey)" : "null"},'
          'value: settings.${param.name},'
          'onChanged: $onChanged,'
          '),';
    }
    return 'ListTile(title: Text("TODO: ${param.name}"),'
        'leading: ${iconData != null ? "Icon($iconData)" : "null"},'
        'subtitle: ${hasDescription ? "Text(locales.$descKey)" : "null"},'
        '),';
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
