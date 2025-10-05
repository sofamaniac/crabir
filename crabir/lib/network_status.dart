import 'dart:async';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/settings/data/data_settings.dart';
import 'package:flutter/material.dart';

class NetworkStatus {
  static ValueNotifier<List<ConnectivityResult>> connection = ValueNotifier([]);

  static Future<void> init() async {
    connection.value = await Connectivity().checkConnectivity();

    Connectivity().onConnectivityChanged.listen((result) {
      connection.value = result;
    });
  }

  static bool canAutoplay(ImageLoading setting) {
    return setting == ImageLoading.always ||
        (connection.value.contains(ConnectivityResult.wifi) &&
            setting == ImageLoading.onWifiOnly);
  }

  static Resolution imageQuality(BuildContext context) {
    final settings = DataSettings.of(context);
    if (connection.value.contains(ConnectivityResult.wifi)) {
      return settings.imageQualityWifi;
    } else {
      return settings.imageQualityCellular;
    }
  }

  static Resolution videoQuality(BuildContext context) {
    final settings = DataSettings.of(context);
    if (connection.value.contains(ConnectivityResult.wifi)) {
      return settings.videoQualityWifi;
    } else {
      return settings.videoQualityCellular;
    }
  }
}
