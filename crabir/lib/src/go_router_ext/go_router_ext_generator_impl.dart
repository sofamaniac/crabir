import 'package:build/build.dart';
import 'package:source_gen/source_gen.dart';
import 'package:analyzer/dart/element/element.dart';
import 'package:analyzer/dart/constant/value.dart';
import './annotations.dart';

class GoRouteDataGenerator extends GeneratorForAnnotation<CrabirRoute> {
  @override
  String generateForAnnotatedElement(
    Element element,
    ConstantReader annotation,
    BuildStep buildStep,
  ) {
    if (element is! ClassElement) {
      throw InvalidGenerationSourceError(
        'GoRouteData can only be used on classes.',
        element: element,
      );
    }

    final className = element.name;
    final routeName = annotation.peek('name')?.stringValue ??
        className; // default = class name

    final fields = element.fields.where((f) => !f.isStatic);

    // PathParam fields
    final pathFields = <FieldElement, String>{};
    for (final field in fields) {
      for (final meta in field.metadata) {
        final obj = meta.computeConstantValue();
        if (obj?.type?.getDisplayString(withNullability: false) ==
            'PathParam') {
          final name = obj!.getField('name')?.toStringValue() ?? '';
          pathFields[field] = name.isNotEmpty ? name : field.name;
        }
      }
    }

    final extraFields =
        fields.where((f) => !pathFields.containsKey(f)).toList();

    // Generate pathParameters map
    final pathParamsMap = pathFields.entries.isEmpty
        ? '{}'
        : '{${pathFields.entries.map((e) => "'${e.value}': Uri.encodeComponent(${e.key.name}.toString())").join(', ')}}';

    // Generate extra map
    final extraMap = extraFields.isEmpty
        ? '{}'
        : '{${extraFields.map((f) => "'${f.name}': ${f.name}").join(', ')}}';

    final buffer = StringBuffer()
      ..writeln('// GENERATED CODE - DO NOT MODIFY BY HAND')
      //..writeln('part of "${buildStep.inputId.uri.pathSegments.last}";')
      ..writeln('extension ${className}Builder on $className {')
      ..writeln("  static const String name = '$routeName';")
      ..writeln('  Map<String, String> get pathParams => $pathParamsMap;')
      ..writeln('  Map<String, dynamic> get extra => $extraMap;')
      ..writeln()
      ..writeln('  void goNamed(BuildContext context) => context.goNamed(')
      ..writeln('    name,')
      ..writeln('    pathParameters: pathParams,')
      ..writeln('    extra: extra,')
      ..writeln('  );')
      ..writeln()
      ..writeln('  void pushNamed(BuildContext context) => context.pushNamed(')
      ..writeln('    name,')
      ..writeln('    pathParameters: pathParams,')
      ..writeln('    extra: extra,')
      ..writeln('  );')
      ..writeln('}');
    return buffer.toString();
  }
}
