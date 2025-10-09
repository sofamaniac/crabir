import 'package:build/build.dart';
import 'package:source_gen/source_gen.dart';

import 'go_router_ext_generator_impl.dart';

Builder crabirRouteBuilder(BuilderOptions options) =>
    PartBuilder([GoRouteDataGenerator()], '.go_route_ext.dart');
