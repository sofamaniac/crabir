class CrabirRoute {
  /// Optional explicit route name. Defaults to the class name.
  final String? name;
  const CrabirRoute({this.name});
}

class PathParam {
  final String? name;
  const PathParam([this.name]);
}
