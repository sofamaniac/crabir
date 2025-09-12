part of 'media.dart';

enum Resolution {
  source,
  high,
  medium,
  low,
}

extension FromResolution<T> on List<T> {
  T withResolution(Resolution resolution) {
    final log = Logger("FromResolution");
    switch (resolution) {
      case Resolution.source:
        log.warning("Calling with Resolution.source which may not be correct");
        return last;
      case Resolution.high:
        return last;
      case Resolution.medium:
        return this[length ~/ 2];
      case Resolution.low:
        return first;
    }
  }
}

extension RedditImageGetResolution on RedditImage {
  ImageBase withResolution(Resolution resolution, bool blur) {
    final variants = this.variants.nsfw ?? this.variants.obfuscated;
    if (variants != null && blur) {
      return variants.withResolution(resolution);
    }
    switch (resolution) {
      case Resolution.source:
        return source;
      default:
        return resolutions.withResolution(resolution);
    }
  }
}

extension VariantGetResolution on VariantInner {
  withResolution(Resolution resolution) {
    switch (resolution) {
      case Resolution.source:
        return source;
      default:
        return resolutions.withResolution(resolution);
    }
  }
}
