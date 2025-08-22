import 'package:build/build.dart';
import 'package:source_gen/source_gen.dart';

import 'settings_page_generator_impl.dart';

Builder settingsPageBuilder(BuilderOptions options) => LibraryBuilder(
      SettingsPageGenerator(),
      generatedExtension: '.settings_page.dart',
    );
