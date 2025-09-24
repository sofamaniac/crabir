/// Handle streamable.com videos
part of 'media.dart';

// ignore: use_key_in_widget_constructors
class StreamableVideo extends StatelessWidget {
  final Post post;

  const StreamableVideo({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
        future: getStreamableVideo(url: post.url),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: LoadingIndicator());
          } else if (snapshot.hasData) {
            final (video, thumbnail) = snapshot.data!;
            return AnimatedContent(
              url: video.url,
              width: video.width,
              height: video.height,
              placeholderUrl: thumbnail,
              cartouche: Cartouche(
                "STREAMABLE",
                background: Colors.cyan,
              ),
            );
          } else if (snapshot.hasError) {
            return Text("Failed to load the video: ${snapshot.error}");
          } else {
            return SizedBox.shrink();
          }
        });
  }
}
