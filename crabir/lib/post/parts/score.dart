part of '../post.dart';

class PostScore extends StatelessWidget {
  final Post post;
  const PostScore({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    final locales = AppLocalizations.of(context);
    final spacer = const WidgetSpan(child: SizedBox(width: 8));
    return RichText(
      text: TextSpan(
        children: [
          WidgetSpan(
            alignment: PlaceholderAlignment.middle,
            child: LikeText(
              score: post.ups,
              likes: post.likes,
              style: Theme.of(context).textTheme.titleMedium!,
              scaling: 1.5,
            ),
          ),
          spacer,
          TextSpan(text: '•', style: Theme.of(context).textTheme.bodySmall),
          spacer,
          TextSpan(
            text: locales.commentsNumbered(post.numComments),
            style: Theme.of(context).textTheme.bodySmall,
          ),
          if (post.over18) ...[
            spacer,
            WidgetSpan(
              alignment: PlaceholderAlignment.middle,
              child: NsfwTag(),
            ),
          ],
        ],
      ),
    );
  }
}

class LikeText extends StatefulWidget {
  final int score;
  final bool? likes;
  final TextStyle style;
  final double scaling;

  const LikeText(
      {super.key,
      required this.score,
      required this.likes,
      required this.style,
      required this.scaling});

  @override
  State<LikeText> createState() => _LikeTextState();
}

class _LikeTextState extends State<LikeText>
    with SingleTickerProviderStateMixin {
  late double currentSize;
  double _scale = 1;

  @override
  void initState() {
    super.initState();
    currentSize = widget.style.fontSize ?? 24;
  }

  @override
  void didUpdateWidget(covariant LikeText oldWidget) {
    super.didUpdateWidget(oldWidget);
    final likes = widget.likes;
    final oldLikes = oldWidget.likes;
    final baseSize = widget.style.fontSize ?? 24;

    // Trigger animation only when liked changes from false → true
    if (likes != oldLikes) {
      if (likes == true) {
        // Grow temporarily
        setState(() {
          currentSize = baseSize * widget.scaling;
          _scale = widget.scaling;
        });

        // Shrink back after a short delay
        Future.delayed(const Duration(milliseconds: 100), () {
          if (mounted) {
            setState(() {
              currentSize = baseSize;
              _scale = 1;
            });
          }
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = CrabirTheme.of(context);
    final color = switch (widget.likes) {
      true => theme.primaryColor,
      false => Colors.cyan,
      _ => null
    };
    final scoreOffset = switch (widget.likes) {
      true => 1,
      false => -1,
      _ => 0,
    };
    // return AnimatedDefaultTextStyle(
    //   duration: const Duration(milliseconds: 200),
    //   curve: Curves.easeOut,
    //   style: TextStyle(
    //     fontSize: currentSize,
    //     fontWeight: FontWeight.bold,
    //     color: color,
    //   ),
    //   child: Text("${widget.score + scoreOffset}"),
    // );
    return TweenAnimationBuilder<double>(
      tween: Tween(begin: 1.0, end: _scale),
      duration: const Duration(milliseconds: 200),
      curve: Curves.easeOut,
      builder: (context, value, child) {
        return Transform.scale(
          scale: value,
          alignment: Alignment.center,
          child: child,
        );
      },
      child: Text(
        "${widget.score + scoreOffset}",
        style: widget.style.copyWith(
          color: color,
        ),
      ),
    );
  }
}
