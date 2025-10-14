part of 'media.dart';

enum Resolution {
  source,
  high,
  medium,
  low,
}

extension ResolutionLabel on Resolution {
  String label(BuildContext context) {
    final locales = AppLocalizations.of(context);
    return switch (this) {
      Resolution.source => locales.resolutionSource,
      Resolution.high => locales.resolutionHigh,
      Resolution.medium => locales.resolutionMedium,
      Resolution.low => locales.resolutionLow,
    };
  }
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
  ImageBase withResolution(Resolution resolution) {
    switch (resolution) {
      case Resolution.source:
        return source;
      default:
        return resolutions.withResolution(resolution);
    }
  }
}

extension GalleryMediaResolution on gallery.GalleryMedia {
  gallery.Image withResolution(Resolution resolution, bool blur) {
    final List<gallery.Image> previews;
    if (blur) {
      previews = obfuscated;
    } else {
      previews = this.previews;
    }
    switch (source) {
      case gallery.Source_Image(:final source):
        if (resolution == Resolution.source && blur) {
          return obfuscated.last;
        } else if (resolution == Resolution.source) {
          return source;
        } else {
          return previews.withResolution(resolution);
        }
      case gallery.Source_AnimatedImage():
        return previews.withResolution(resolution);
    }
  }
}
