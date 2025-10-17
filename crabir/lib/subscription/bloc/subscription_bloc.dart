// subscription_bloc.dart
import 'package:flutter/foundation.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:logging/logging.dart';
import 'package:purchases_flutter/purchases_flutter.dart';
import 'package:purchases_ui_flutter/purchases_ui_flutter.dart';

part 'subscription_event.dart';
part 'subscription_state.dart';
part 'subscription_bloc.freezed.dart';

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

class SubscriptionBloc extends Bloc<SubscriptionEvent, SubscriptionState> {
  final _entitlement = "subscribed";
  SubscriptionBloc() : super(const SubscriptionState.loading()) {
    on<Initialize>(_onInitialize);
    on<Changed>(_onSubscriptionChanged);
  }

  Future<void> _onInitialize(
    Initialize event,
    Emitter<SubscriptionState> emit,
  ) async {
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

      // Get current status
      final info = await Purchases.getCustomerInfo();
      final isSubscribed = info.entitlements.active.containsKey(_entitlement);

      emit(isSubscribed
          ? const SubscriptionState.subscribed()
          : const SubscriptionState.unsubscribed());

      // Start listening for changes
      Purchases.addCustomerInfoUpdateListener((info) {
        final active = info.entitlements.active.containsKey(_entitlement);
        add(Changed(active));
      });
    } catch (e) {
      emit(const SubscriptionState.unsubscribed());
    }
  }

  Future<void> _onSubscriptionChanged(
    Changed event,
    Emitter<SubscriptionState> emit,
  ) async {
    emit(event.isSubscribed
        ? const SubscriptionState.subscribed()
        : const SubscriptionState.unsubscribed());
  }
}
