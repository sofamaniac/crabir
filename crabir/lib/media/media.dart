import 'dart:async';
import 'dart:math';

import 'package:auto_route/auto_route.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:crabir/buttons.dart';
import 'package:crabir/cartouche.dart';
import 'package:crabir/l10n/app_localizations.dart';
import 'package:crabir/loading_indicator.dart';
import 'package:crabir/network_status.dart';
import 'package:crabir/settings/data/data_settings.dart';
import 'package:crabir/settings/theme/theme_bloc.dart';
import 'package:crabir/src/rust/third_party/reddit_api/client.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/post.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:logging/logging.dart';
import 'package:photo_view/photo_view.dart';
import 'package:video_player/video_player.dart';
import 'package:crabir/src/rust/third_party/reddit_api/model/gallery.dart'
    as gallery;
import 'package:visibility_detector/visibility_detector.dart';

part 'image.dart';
part 'gif.dart';
part 'resolution.dart';
part 'fullscreen_media_view.dart';
