part of 'thread.dart';

class IndentGuidesPainter extends CustomPainter {
  final int depth;

  IndentGuidesPainter({required this.depth});

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = Colors.grey.shade800
      ..strokeWidth = 1.0;

    const double spacing = 12.0;
    for (int i = 0; i < depth; i++) {
      final x = i * spacing + spacing / 2;
      canvas.drawLine(Offset(x, 0), Offset(x, size.height), paint);
    }
  }

  @override
  bool shouldRepaint(covariant IndentGuidesPainter oldDelegate) =>
      oldDelegate.depth != depth;
}
