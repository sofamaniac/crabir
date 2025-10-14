part of '../post.dart';

class NsfwTag extends StatelessWidget {
  const NsfwTag({super.key});
  @override
  Widget build(BuildContext context) {
    return Cartouche(
      "NSFW",
      background: Colors.red,
    );
  }
}

class SpoilerTag extends StatelessWidget {
  const SpoilerTag({super.key});
  @override
  Widget build(BuildContext context) {
    return Cartouche(
      "SPOILER",
      borderColor: Colors.red,
      foreground: Colors.red,
    );
  }
}
