import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:url_launcher/url_launcher.dart';

class NotFoundPage extends StatelessWidget {
  final String uri;
  const NotFoundPage({super.key, required this.uri});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Page not found')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'Could not navigate to $uri',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            ElevatedButton(
              onPressed: () => context.pop(),
              child: const Text('Go Back'),
            ),
            ElevatedButton(
              onPressed: () async {
                await launchUrl(Uri.parse(uri));
              },
              child: const Text('Open in browser'),
            ),
          ],
        ),
      ),
    );
  }
}
