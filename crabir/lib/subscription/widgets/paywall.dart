import 'package:crabir/routes/routes.dart';
import 'package:crabir/subscription/bloc/subscription_bloc.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:purchases_ui_flutter/purchases_ui_flutter.dart';

class PaywallScreen extends StatelessWidget {
  const PaywallScreen({super.key});

  @override
  Widget build(BuildContext context) {
    // return FutureBuilder(
    //   future: presentPaywallIfNeeded(),
    //   builder: (context, snapshot) {
    //     if (snapshot.connectionState == ConnectionState.done) {
    //       context.go(DEFAULT_ROUTE);
    //     }
    //     return SizedBox.shrink();
    //   },
    // );
    return PaywallView();
  }
}
