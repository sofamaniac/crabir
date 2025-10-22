// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for English (`en`).
class AppLocalizationsEn extends AppLocalizations {
  AppLocalizationsEn([String locale = 'en']) : super(locale);

  @override
  String get timeframeAll => 'All time';

  @override
  String get timeframeYear => 'Year';

  @override
  String get timeframeMonth => 'Month';

  @override
  String get timeframeWeek => 'Week';

  @override
  String get timeframeDay => 'Day';

  @override
  String get timeframeHour => 'Hour';

  @override
  String get sortTop => 'Top';

  @override
  String get sortHot => 'Hot';

  @override
  String get sortNew => 'New';

  @override
  String get sortComments => 'Comments';

  @override
  String get sortRelevance => 'Most relevant';

  @override
  String get sortActivity => 'Activity';

  @override
  String get sortControversial => 'Controversial';

  @override
  String get sortRising => 'Rising';

  @override
  String get sortBest => 'Best';

  @override
  String get sortOld => 'Old';

  @override
  String get sortQA => 'Q&A';

  @override
  String get sortLive => 'Live';

  @override
  String get sortRandom => 'Random';

  @override
  String get feedHome => 'Home';

  @override
  String get feedAll => 'All';

  @override
  String get feedPopular => 'Popular';

  @override
  String get feedSaved => 'Saved';

  @override
  String get feedHistory => 'History';

  @override
  String get loginButtonLabel => 'Add an account';

  @override
  String get logoutButtonLabel => 'Logout';

  @override
  String get gotoDropdownLabel => 'Go to ...';

  @override
  String get replyTitle => 'Reply';

  @override
  String get deletedUser => '[deleted]';

  @override
  String get postEditorTitle => 'Create post';

  @override
  String get resolutionSource => 'Source';

  @override
  String get resolutionHigh => 'High';

  @override
  String get resolutionMedium => 'Medium';

  @override
  String get resolutionLow => 'Low';

  @override
  String get mediaPreviewSizeNone => 'None';

  @override
  String get mediaPreviewSizeThumbnail => 'Thumbnail';

  @override
  String get mediaPreviewSizeFull => 'Full Size';

  @override
  String get indentationStyleLines => 'Lines';

  @override
  String get indentationStyleColored => 'Colored lines';

  @override
  String get indentationStyleBars => 'Colored bars';

  @override
  String get shareLink => 'Share Link';

  @override
  String get shareLinkTitle => 'Share Title + Link';

  @override
  String get sharePost => 'Share Post';

  @override
  String get shareText => 'Share Text';

  @override
  String get shareButtonLabel => 'Share...';

  @override
  String get markRead => 'Mark post as read';

  @override
  String get markUnread => 'Mark post as unread';

  @override
  String get hidePost => 'Hide post';

  @override
  String get unhidePost => 'Unhide post';

  @override
  String get themeBackround => 'Background';

  @override
  String get themeCardBackground => 'Card background';

  @override
  String get themeToolbarBackground => 'Toolbar background';

  @override
  String get themeToolbarText => 'Toolbar text';

  @override
  String get themePrimaryColor => 'Primary color';

  @override
  String get themeHighlight => 'Highlight';

  @override
  String get themePostTitle => 'Post title';

  @override
  String get themeReadPost => 'Read post title';

  @override
  String get themeAnnouncement => 'Announcement';

  @override
  String get themeContentText => 'Context Text';

  @override
  String get themeLinkColor => 'Link color';

  @override
  String get themeSecondaryText => 'Secondary text color';

  @override
  String get themeDownvote => 'Downvote color';

  @override
  String yearsAgo(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '${count}y',
      one: '${count}y',
    );
    return '$_temp0';
  }

  @override
  String monthsAgo(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '${count}m',
      one: '${count}m',
    );
    return '$_temp0';
  }

  @override
  String daysAgo(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '${count}d',
      one: '${count}d',
    );
    return '$_temp0';
  }

  @override
  String hoursAgo(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '${count}h',
      one: '${count}h',
    );
    return '$_temp0';
  }

  @override
  String minutesAgo(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '${count}min',
      one: '${count}min',
    );
    return '$_temp0';
  }

  @override
  String secondsAgo(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '${count}s',
      one: '${count}s',
    );
    return '$_temp0';
  }

  @override
  String get comments => 'comments';

  @override
  String commentsNumbered(int n) {
    String _temp0 = intl.Intl.pluralLogic(
      n,
      locale: localeName,
      other: '$n comments',
      one: '1 comment',
    );
    return '$_temp0';
  }

  @override
  String loadMoreComments(int n) {
    String _temp0 = intl.Intl.pluralLogic(
      n,
      locale: localeName,
      other: 'Load $n comments',
      one: 'Load 1 comment',
    );
    return '$_temp0';
  }

  @override
  String get postSearchBar => 'Search posts';

  @override
  String get subredditsSearchBar => 'Search communities';

  @override
  String get comments_defaultSort => 'Default Sort';

  @override
  String get comments_sortDescription => 'Choose how comments are sorted';

  @override
  String get comments_useSuggestedSort => 'Use suggested sort';

  @override
  String get comments_useSuggestedSortDescription =>
      'Automatically apply Reddit’s suggested sort';

  @override
  String get comments_showNavigationBar => 'Show navigation bar';

  @override
  String get comments_showUserAvatar => 'Show user avatar';

  @override
  String get comments_showCommentsImage => 'Show images in comments';

  @override
  String get comments_postMediaPreviewSize => 'Post media preview size';

  @override
  String get comments_buttonsAlwaysVisible => 'Always show action buttons';

  @override
  String get comments_hideButtonAfterAction => 'Hide action button after use';

  @override
  String get comments_collapseAutoMod => 'Collapse AutoModerator';

  @override
  String get comments_collapseDisruptiveComment =>
      'Collapse disruptive comments';

  @override
  String get comments_showPostUpvotePercentage => 'Show post upvote percentage';

  @override
  String get comments_highlightMyUsername => 'Highlight my username';

  @override
  String get comments_showFloatingButton => 'Show floating button';

  @override
  String get comments_showAwards => 'Show awards';

  @override
  String get comments_clickableAwards => 'Make awards clickable';

  @override
  String get comments_showUserFlair => 'Show user flair';

  @override
  String get comments_showFlairColors => 'Show flair colors';

  @override
  String get comments_showFlairEmojis => 'Show flair emojis';

  @override
  String get comments_clickToCollapse => 'Click to collapse comments';

  @override
  String get comments_hideTextCollapsed => 'Hide text when collapsed';

  @override
  String get comments_loadCollapsed => 'Load comments collapsed';

  @override
  String get comments_animateCollapse => 'Animate collapse';

  @override
  String get comments_clickableUsername => 'Clickable usernames';

  @override
  String get comments_highlightNewComments => 'Highlight new comments';

  @override
  String get comments_volumeRockerNavigation => 'Navigate with volume rocker';

  @override
  String get comments_animateNavigation => 'Animate navigation';

  @override
  String get comments_showSaveButton => 'Show save button';

  @override
  String get comments_swipeToClose => 'Swipe to close thread';

  @override
  String get comments_distanceThreshold => 'Distance threshold';

  @override
  String get comments_distanceThresholdDescription =>
      'Dragging distance from edge as a percentage of screen width before closing and going back.';

  @override
  String get comments_threadGuide => 'Thread guides';

  @override
  String get posts_defaultHomeSort => 'Default Home sort';

  @override
  String get posts_defaultSort => 'Default sort';

  @override
  String get posts_rememberSortByCommunity => 'Remember by Community';

  @override
  String get posts_rememberSortByCommunityDescription =>
      'Each community will remember the last sort you selected.';

  @override
  String get posts_rememberedSorts => 'Manage Sorts';

  @override
  String get posts_showAwards => 'Show awards';

  @override
  String get posts_clickableAwards => 'Clickable awards';

  @override
  String get posts_showPostFlair => 'Show post flair';

  @override
  String get posts_showFlairColors => 'Show flair colors';

  @override
  String get posts_showFlairEmojis => 'Show emojis';

  @override
  String get posts_tapFlairToSearch => 'Tap flair to search';

  @override
  String get posts_showAuthor => 'Show author';

  @override
  String get posts_clickableCommunity => 'Tap community to visit';

  @override
  String get posts_clickableUser => 'Tap username to view profile';

  @override
  String get posts_showFloatingButton => 'Show floating button';

  @override
  String get posts_showHideButton => 'Hide';

  @override
  String get posts_showMarkAsReadButton => 'Mark as read';

  @override
  String get posts_showShareButton => 'Share';

  @override
  String get posts_showCommentsButton => 'Comments';

  @override
  String get posts_showOpenInAppButton => 'Open in app';

  @override
  String get layout_defaultView => 'Default post view';

  @override
  String get layout_rememberByCommunity => 'Remember for each community';

  @override
  String get layout_thumbnailOnLeft => 'Thumbnail on left';

  @override
  String get layout_font => 'Font size and style';

  @override
  String get layout_prefixCommunities => 'Prefix subreddit with /r';

  @override
  String get data_autoplay => 'Autoplay videos';

  @override
  String get data_videoQualityWifi =>
      'Preferred video quality when connected to Wifi';

  @override
  String get data_videoQualityCellular =>
      'Preferred video quality when connected to cellular';

  @override
  String get data_minimumQuality => 'Minimum quality';

  @override
  String get data_maximumQuality => 'Maximum quality';

  @override
  String get data_loadImages => 'Load images';

  @override
  String get data_imageQualityWifi =>
      'Preferred image quality when connected to Wifi';

  @override
  String get data_imageQualityCellular =>
      'Preferred image quality when connected to cellular';

  @override
  String get filters_manageHiddenCommunities => 'Communities';

  @override
  String get filters_manageHiddenCommunitiesDescription =>
      'Hide posts from these communities';

  @override
  String get filters_manageHiddenDomains => 'Domains';

  @override
  String get filters_manageHiddenDomainsDescription =>
      'Hide posts from these domains';

  @override
  String get filters_manageHiddenUsers => 'Users';

  @override
  String get filters_manageHiddenUsersDescription =>
      'Hide posts from these users';

  @override
  String get filters_manageHiddenFlairs => 'Flairs';

  @override
  String get filters_manageHiddenFlairsDescription =>
      'Hide posts with these flairs';

  @override
  String get filters_showNSFW => 'Show NSFW';

  @override
  String get filters_showNSFWDescription =>
      'Show content labeled Not Safe For Work';

  @override
  String get filters_showImageInNSFW => 'Show images in NSFW posts';

  @override
  String get filters_blurNSFW => 'Blur images in NSFW posts';

  @override
  String get lateralMenu_showHome => 'Home';

  @override
  String get lateralMenu_showHomeDescription =>
      'Default feed. To set any community, go to Subscriptions list and select \"Set as default\".';

  @override
  String get lateralMenu_showHomeFeed => 'Home Feed';

  @override
  String get lateralMenu_showHomeFeedDescription => 'Posts from subscriptions';

  @override
  String get lateralMenu_showPopular => 'Popular';

  @override
  String get lateralMenu_showAll => 'All';

  @override
  String get lateralMenu_showSaved => 'Saved';

  @override
  String get lateralMenu_showHistory => 'History';

  @override
  String get lateralMenu_showProfile => 'Profile';

  @override
  String get lateralMenu_showInbox => 'Inbox';

  @override
  String get lateralMenu_showFriends => 'Friends';

  @override
  String get lateralMenu_showDrafts => 'Drafts';

  @override
  String get lateralMenu_showModeration => 'Moderation';

  @override
  String get lateralMenu_showSearch => 'Search';

  @override
  String get lateralMenu_showGoToDropdown => 'Go to dropdown';

  @override
  String get lateralMenu_showGoToCommunity => 'Go to community';

  @override
  String get lateralMenu_showGoToUser => 'Go to user';

  @override
  String get lateralMenu_darkMode => 'Dark mode';

  @override
  String get lateralMenu_showNSFW => 'Show NSFW';

  @override
  String get lateralMenu_blurNSFW => 'Blur NSFW';

  @override
  String get lateralMenu_showSubsInMenu => 'Show in menu';

  @override
  String get lateralMenu_showSubsIcon => 'Show icons';

  @override
  String get lateralMenu_showSubsFavOnly => 'Show only favorites';

  @override
  String get lateralMenu_showAccountAvatar => 'Show avatar';

  @override
  String get lateralMenu_showAccountUsername => 'Show username';
}
