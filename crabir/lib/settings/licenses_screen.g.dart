// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'licenses_screen.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RustLicense _$RustLicenseFromJson(Map<String, dynamic> json) => RustLicense(
      package: json['name'] as String,
      version: json['version'] as String?,
      license: json['license'] as String?,
      repository: json['repository'] as String?,
      authors: json['authors'] as String?,
      licenseFiles: (json['license_files'] as List<dynamic>)
          .map((e) => e as String)
          .toList(),
    );
