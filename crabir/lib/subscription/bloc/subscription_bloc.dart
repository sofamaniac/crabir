// subscription_bloc.dart
import 'package:logging/logging.dart';
import 'package:purchases_flutter/purchases_flutter.dart';
import 'package:purchases_ui_flutter/purchases_ui_flutter.dart';

Future<void> initRevenueCat() async {
  try {
    // Configure RevenueCat
    await Purchases.configure(
      PurchasesConfiguration("goog_pkSBOavmncFweWFHROYvEmPtMoc"),
    );
  } catch (e) {
    Logger("initRevenueCat").severe("Failed to initalize revenue cat");
  }
}

/// Returns false only if the user is not subscribed or the purchased failed.
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
}
