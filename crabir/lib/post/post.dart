import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/bool_to_vote.dart';
import 'package:crabir/buttons.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/gallery/gallery.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/markdown_editor/editor.dart';
import 'package:crabir/markdown_render.dart';
import 'package:crabir/media/media.dart';
import 'package:crabir/network_status.dart';
import 'package:crabir/post/parts/html_with_fade.dart';
import 'package:crabir/post/parts/image.dart';
import 'package:crabir/post/parts/video.dart';
import 'package:crabir/flair.dart';
import 'package:crabir/report.dart';
import 'package:crabir/search_posts/widgets/search.dart';
import 'package:crabir/separated_row.dart';
import 'package:crabir/settings/filters/filters_settings.dart';
import 'package:crabir/settings/posts/posts_settings.dart';
import 'package:crabir/settings/theme/theme.dart';
import 'package:crabir/src/rust/api/reddit_api.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/feed.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/rule.dart';
import 'package:crabir/subreddit.dart';
import 'package:crabir/time_ellapsed.dart';
import 'package:crabir/user/user.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:share_plus/share_plus.dart';
import 'package:url_launcher/url_launcher.dart';

part 'parts/title.dart';
part 'parts/header.dart';
part 'parts/footer.dart';
part 'parts/score.dart';
part 'parts/tags.dart';
part 'parts/post_flair.dart';
part 'parts/thumbnail.dart';
part 'parts/share_button.dart';
part 'dense_card.dart';
part 'post_card.dart';

typedef SaveCallback = Future<void> Function(bool);
typedef LikeCallback = Future<void> Function(VoteDirection);

Widget wrapPostElement(Widget widget) {
  return Padding(
    padding: EdgeInsetsGeometry.symmetric(horizontal: 16),
    child: widget,
  );
}

extension ShowThumbnail on Kind {
  bool showThumbnail(bool showMedia) {
    return switch (this) {
      Kind.link || Kind.unknown => true,
      Kind.selftext || Kind.meta => false,
      _ => !showMedia,
    };
  }
}

extension IsMediaKind on Kind {
  bool isMedia() {
    return switch (this) {
      Kind.image ||
      Kind.gallery ||
      Kind.video ||
      Kind.youtubeVideo ||
      Kind.mediaGallery =>
        true,
      _ => false,
    };
  }
}

class PostCubit extends Cubit<Post> {
  PostCubit(super.post);
}

extension HasContent on Post {
  bool hasContent({bool allowMedia = true}) {
    return (selftext?.isNotEmpty ?? false) || (kind.isMedia() && allowMedia);
  }
}
