import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class LoadingIndicator extends StatelessWidget {
  const LoadingIndicator({super.key});
  @override
  Widget build(BuildContext context) {
    final theme = context.watch<ThemeBloc>().state;
    return Center(child: CircularProgressIndicator(color: theme.primaryColor));
  }
}
