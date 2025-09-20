import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:json_annotation/json_annotation.dart';

part 'licenses_screen.g.dart';

@JsonSerializable(createToJson: false)
class RustLicense {
  @JsonKey(name: "name")
  final String package;
  final String? version;
  final String? license;
  final String? repository;
  @JsonKey(name: "license_files")
  final List<String> licenseFiles;
  final String? authors;

  RustLicense({
    required this.package,
    required this.version,
    required this.license,
    required this.repository,
    required this.authors,
    required this.licenseFiles,
  });

  factory RustLicense.fromJson(Map<String, dynamic> json) =>
      _$RustLicenseFromJson(json);
}

Future<List<RustLicense>> loadRustLicenses() async {
  final jsonStr = await rootBundle.loadString('assets/rust-licenses.json');
  final List<dynamic> data = jsonDecode(jsonStr);
  return data.map((e) => RustLicense.fromJson(e)).toList();
}

Future<void> registerRustLicenses() async {
  final licenses = await loadRustLicenses();

  for (final license in licenses) {
    // Register with Flutter's LicenseRegistry
    for (final file in license.licenseFiles) {
      final content = await rootBundle.loadString(file);
      LicenseRegistry.addLicense(() async* {
        yield LicenseEntryWithLineBreaks(
          [license.package], // package names
          content,
        );
      });
    }
    if (license.licenseFiles.isEmpty) {
      LicenseRegistry.addLicense(() async* {
        yield LicenseEntryWithLineBreaks(
          [license.package], // package names
          "${license.package} version ${license.version}\nLicense: ${license.license}",
        );
      });
    }
  }
}
