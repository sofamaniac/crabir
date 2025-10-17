// subscription_bloc.dart
import 'package:flutter/foundation.dart';
import 'package:logging/logging.dart';
import 'package:purchases_flutter/purchases_flutter.dart';
import 'package:purchases_ui_flutter/purchases_ui_flutter.dart';

// void presentPaywall() async {
//   final paywallResult = await RevenueCatUI.presentPaywall();
//   //log('Paywall result: $paywallResult');
// }

Future<void> initRevenueCat() async {
  try {
    // Configure RevenueCat
    if (kDebugMode) {
      await Purchases.configure(
        PurchasesConfiguration("test_czmMWYVlxWzjfRhNvGzeXIWnHOq"),
      );
    } else {
      await Purchases.configure(
        PurchasesConfiguration("goog_pkSBOavmncFweWFHROYvEmPtMoc"),
      );
    }
  } catch (e) {
    Logger("initRevenueCat").severe("Failed to initalize revenue cat");
  }
}

Future<bool> presentPaywallIfNeeded() async {
  final PaywallResult paywallResult =
      await RevenueCatUI.presentPaywallIfNeeded("subscribed");
  return switch (paywallResult) {
    PaywallResult.notPresented ||
    PaywallResult.purchased ||
    PaywallResult.restored =>
      true,
    _ => false
  };
  //log('Paywall result: $paywallResult');
}
