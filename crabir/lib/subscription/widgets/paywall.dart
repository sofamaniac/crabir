import 'package:crabir/subscription/bloc/subscription_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:purchases_flutter/purchases_flutter.dart';
import 'package:purchases_ui_flutter/purchases_ui_flutter.dart';

class PaywallScreen extends StatefulWidget {
  const PaywallScreen({super.key});

  @override
  State<PaywallScreen> createState() => _PaywallScreenState();
}

class _PaywallScreenState extends State<PaywallScreen> {
  Offerings? _offerings;
  bool _isLoading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadOfferings();
  }

  Future<void> _loadOfferings() async {
    try {
      final offerings = await Purchases.getOfferings();
      setState(() {
        _offerings = offerings;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = 'Failed to load products: $e';
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (_error != null) {
      return Scaffold(
        body: Center(child: Text(_error!)),
      );
    }

    final availablePackages = _offerings?.current?.availablePackages ?? [];

    if (availablePackages.isEmpty) {
      return const Scaffold(
        body: Center(child: Text('No available products.')),
      );
    }

    return Scaffold(
      appBar: AppBar(title: const Text('Upgrade to Premium')),
      body: PaywallView(
        displayCloseButton: false,
        onPurchaseCompleted: (_, __) =>
            context.read<SubscriptionBloc>().add(Changed(true)),
        onRestoreCompleted: (info) => context.read<SubscriptionBloc>().add(
              Changed(
                info.entitlements.props.contains("subscribed"),
              ),
            ),
      ),
    );
  }
}
