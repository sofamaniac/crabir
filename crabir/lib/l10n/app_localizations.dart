import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:intl/intl.dart' as intl;

import 'app_localizations_en.dart';

// ignore_for_file: type=lint

/// Callers can lookup localized strings with an instance of AppLocalizations
/// returned by `AppLocalizations.of(context)`.
///
/// Applications need to include `AppLocalizations.delegate()` in their app's
/// `localizationDelegates` list, and the locales they support in the app's
/// `supportedLocales` list. For example:
///
/// ```dart
/// import 'l10n/app_localizations.dart';
///
/// return MaterialApp(
///   localizationsDelegates: AppLocalizations.localizationsDelegates,
///   supportedLocales: AppLocalizations.supportedLocales,
///   home: MyApplicationHome(),
/// );
/// ```
///
/// ## Update pubspec.yaml
///
/// Please make sure to update your pubspec.yaml to include the following
/// packages:
///
/// ```yaml
/// dependencies:
///   # Internationalization support.
///   flutter_localizations:
///     sdk: flutter
///   intl: any # Use the pinned version from flutter_localizations
///
///   # Rest of dependencies
/// ```
///
/// ## iOS Applications
///
/// iOS applications define key application metadata, including supported
/// locales, in an Info.plist file that is built into the application bundle.
/// To configure the locales supported by your app, you’ll need to edit this
/// file.
///
/// First, open your project’s ios/Runner.xcworkspace Xcode workspace file.
/// Then, in the Project Navigator, open the Info.plist file under the Runner
/// project’s Runner folder.
///
/// Next, select the Information Property List item, select Add Item from the
/// Editor menu, then select Localizations from the pop-up menu.
///
/// Select and expand the newly-created Localizations item then, for each
/// locale your application supports, add a new item and select the locale
/// you wish to add from the pop-up menu in the Value field. This list should
/// be consistent with the languages listed in the AppLocalizations.supportedLocales
/// property.
abstract class AppLocalizations {
  AppLocalizations(String locale)
      : localeName = intl.Intl.canonicalizedLocale(locale.toString());

  final String localeName;

  static AppLocalizations of(BuildContext context) {
    return Localizations.of<AppLocalizations>(context, AppLocalizations)!;
  }

  static const LocalizationsDelegate<AppLocalizations> delegate =
      _AppLocalizationsDelegate();

  /// A list of this localizations delegate along with the default localizations
  /// delegates.
  ///
  /// Returns a list of localizations delegates containing this delegate along with
  /// GlobalMaterialLocalizations.delegate, GlobalCupertinoLocalizations.delegate,
  /// and GlobalWidgetsLocalizations.delegate.
  ///
  /// Additional delegates can be added by appending to this list in
  /// MaterialApp. This list does not have to be used at all if a custom list
  /// of delegates is preferred or required.
  static const List<LocalizationsDelegate<dynamic>> localizationsDelegates =
      <LocalizationsDelegate<dynamic>>[
    delegate,
    GlobalMaterialLocalizations.delegate,
    GlobalCupertinoLocalizations.delegate,
    GlobalWidgetsLocalizations.delegate,
  ];

  /// A list of this localizations delegate's supported locales.
  static const List<Locale> supportedLocales = <Locale>[Locale('en')];

  /// No description provided for @timeframeAll.
  ///
  /// In en, this message translates to:
  /// **'All time'**
  String get timeframeAll;

  /// No description provided for @timeframeYear.
  ///
  /// In en, this message translates to:
  /// **'Year'**
  String get timeframeYear;

  /// No description provided for @timeframeMonth.
  ///
  /// In en, this message translates to:
  /// **'Month'**
  String get timeframeMonth;

  /// No description provided for @timeframeWeek.
  ///
  /// In en, this message translates to:
  /// **'Week'**
  String get timeframeWeek;

  /// No description provided for @timeframeDay.
  ///
  /// In en, this message translates to:
  /// **'Day'**
  String get timeframeDay;

  /// No description provided for @timeframeHour.
  ///
  /// In en, this message translates to:
  /// **'Hour'**
  String get timeframeHour;

  /// No description provided for @sortTop.
  ///
  /// In en, this message translates to:
  /// **'Top'**
  String get sortTop;

  /// No description provided for @sortHot.
  ///
  /// In en, this message translates to:
  /// **'Hot'**
  String get sortHot;

  /// No description provided for @sortNew.
  ///
  /// In en, this message translates to:
  /// **'New'**
  String get sortNew;

  /// No description provided for @sortComments.
  ///
  /// In en, this message translates to:
  /// **'Comments'**
  String get sortComments;

  /// No description provided for @sortRelevance.
  ///
  /// In en, this message translates to:
  /// **'Most relevant'**
  String get sortRelevance;

  /// No description provided for @sortActivity.
  ///
  /// In en, this message translates to:
  /// **'Activity'**
  String get sortActivity;

  /// No description provided for @sortControversial.
  ///
  /// In en, this message translates to:
  /// **'Controversial'**
  String get sortControversial;

  /// No description provided for @sortRising.
  ///
  /// In en, this message translates to:
  /// **'Rising'**
  String get sortRising;

  /// No description provided for @sortBest.
  ///
  /// In en, this message translates to:
  /// **'Best'**
  String get sortBest;

  /// No description provided for @sortOld.
  ///
  /// In en, this message translates to:
  /// **'Old'**
  String get sortOld;

  /// No description provided for @sortQA.
  ///
  /// In en, this message translates to:
  /// **'Q&A'**
  String get sortQA;

  /// No description provided for @sortLive.
  ///
  /// In en, this message translates to:
  /// **'Live'**
  String get sortLive;

  /// No description provided for @sortRandom.
  ///
  /// In en, this message translates to:
  /// **'Random'**
  String get sortRandom;

  /// No description provided for @feedHome.
  ///
  /// In en, this message translates to:
  /// **'Home'**
  String get feedHome;

  /// No description provided for @feedAll.
  ///
  /// In en, this message translates to:
  /// **'All'**
  String get feedAll;

  /// No description provided for @feedPopular.
  ///
  /// In en, this message translates to:
  /// **'Popular'**
  String get feedPopular;

  /// No description provided for @loginButtonLabel.
  ///
  /// In en, this message translates to:
  /// **'Add an account'**
  String get loginButtonLabel;

  /// No description provided for @logoutButtonLabel.
  ///
  /// In en, this message translates to:
  /// **'Logout'**
  String get logoutButtonLabel;

  /// Number of years ago, abbreviated (e.g., '1y', '2y')
  ///
  /// In en, this message translates to:
  /// **'{count, plural, =1 {{count}y} other {{count}y}}'**
  String yearsAgo(int count);

  /// Number of months ago, abbreviated (e.g., '1m', '2m')
  ///
  /// In en, this message translates to:
  /// **'{count, plural, =1 {{count}m} other {{count}m}}'**
  String monthsAgo(int count);

  /// Number of days ago, abbreviated (e.g., '1d', '3d')
  ///
  /// In en, this message translates to:
  /// **'{count, plural, =1 {{count}d} other {{count}d}}'**
  String daysAgo(int count);

  /// Number of hours ago, abbreviated (e.g., '1h', '6h')
  ///
  /// In en, this message translates to:
  /// **'{count, plural, =1 {{count}h} other {{count}h}}'**
  String hoursAgo(int count);

  /// Number of minutes ago, abbreviated (e.g., '1min', '12min')
  ///
  /// In en, this message translates to:
  /// **'{count, plural, =1 {{count}min} other {{count}min}}'**
  String minutesAgo(int count);

  /// Number of seconds ago, abbreviated (e.g., '1s', '45s')
  ///
  /// In en, this message translates to:
  /// **'{count, plural, =1 {{count}s} other {{count}s}}'**
  String secondsAgo(int count);

  /// No description provided for @comments.
  ///
  /// In en, this message translates to:
  /// **'Comments'**
  String get comments;

  /// Label for load more button
  ///
  /// In en, this message translates to:
  /// **'{n, plural, =1{Load 1 comment} other{Load {n} comments}}'**
  String loadMoreComments(int n);

  /// No description provided for @postSearchBar.
  ///
  /// In en, this message translates to:
  /// **'Search posts'**
  String get postSearchBar;

  /// No description provided for @subredditsSearchBar.
  ///
  /// In en, this message translates to:
  /// **'Search communities'**
  String get subredditsSearchBar;

  /// No description provided for @comments_defaultSort.
  ///
  /// In en, this message translates to:
  /// **'Default Sort'**
  String get comments_defaultSort;

  /// No description provided for @comments_sortDescription.
  ///
  /// In en, this message translates to:
  /// **'Choose how comments are sorted'**
  String get comments_sortDescription;

  /// No description provided for @comments_useSuggestedSort.
  ///
  /// In en, this message translates to:
  /// **'Use suggested sort'**
  String get comments_useSuggestedSort;

  /// No description provided for @comments_useSuggestedSortDescription.
  ///
  /// In en, this message translates to:
  /// **'Automatically apply Reddit’s suggested sort'**
  String get comments_useSuggestedSortDescription;

  /// No description provided for @comments_showNavigationBar.
  ///
  /// In en, this message translates to:
  /// **'Show navigation bar'**
  String get comments_showNavigationBar;

  /// No description provided for @comments_showUserAvatar.
  ///
  /// In en, this message translates to:
  /// **'Show user avatar'**
  String get comments_showUserAvatar;

  /// No description provided for @comments_showCommentsImage.
  ///
  /// In en, this message translates to:
  /// **'Show images in comments'**
  String get comments_showCommentsImage;

  /// No description provided for @comments_postMediaPreviewSize.
  ///
  /// In en, this message translates to:
  /// **'Post media preview size'**
  String get comments_postMediaPreviewSize;

  /// No description provided for @comments_buttonsAlwaysVisible.
  ///
  /// In en, this message translates to:
  /// **'Always show action buttons'**
  String get comments_buttonsAlwaysVisible;

  /// No description provided for @comments_hideButtonAfterAction.
  ///
  /// In en, this message translates to:
  /// **'Hide action button after use'**
  String get comments_hideButtonAfterAction;

  /// No description provided for @comments_collapseAutoMod.
  ///
  /// In en, this message translates to:
  /// **'Collapse AutoModerator'**
  String get comments_collapseAutoMod;

  /// No description provided for @comments_collapseDisruptiveComment.
  ///
  /// In en, this message translates to:
  /// **'Collapse disruptive comments'**
  String get comments_collapseDisruptiveComment;

  /// No description provided for @comments_showPostUpvotePercentage.
  ///
  /// In en, this message translates to:
  /// **'Show post upvote percentage'**
  String get comments_showPostUpvotePercentage;

  /// No description provided for @comments_highlightMyUsername.
  ///
  /// In en, this message translates to:
  /// **'Highlight my username'**
  String get comments_highlightMyUsername;

  /// No description provided for @comments_showFloatingButton.
  ///
  /// In en, this message translates to:
  /// **'Show floating button'**
  String get comments_showFloatingButton;

  /// No description provided for @comments_showAwards.
  ///
  /// In en, this message translates to:
  /// **'Show awards'**
  String get comments_showAwards;

  /// No description provided for @comments_clickableAwards.
  ///
  /// In en, this message translates to:
  /// **'Make awards clickable'**
  String get comments_clickableAwards;

  /// No description provided for @comments_showUserFlair.
  ///
  /// In en, this message translates to:
  /// **'Show user flair'**
  String get comments_showUserFlair;

  /// No description provided for @comments_showFlairColors.
  ///
  /// In en, this message translates to:
  /// **'Show flair colors'**
  String get comments_showFlairColors;

  /// No description provided for @comments_showFlairEmojis.
  ///
  /// In en, this message translates to:
  /// **'Show flair emojis'**
  String get comments_showFlairEmojis;

  /// No description provided for @comments_clickToCollapse.
  ///
  /// In en, this message translates to:
  /// **'Click to collapse comments'**
  String get comments_clickToCollapse;

  /// No description provided for @comments_hideTextCollapsed.
  ///
  /// In en, this message translates to:
  /// **'Hide text when collapsed'**
  String get comments_hideTextCollapsed;

  /// No description provided for @comments_loadCollapsed.
  ///
  /// In en, this message translates to:
  /// **'Load comments collapsed'**
  String get comments_loadCollapsed;

  /// No description provided for @comments_animateCollapse.
  ///
  /// In en, this message translates to:
  /// **'Animate collapse'**
  String get comments_animateCollapse;

  /// No description provided for @comments_clickableUsername.
  ///
  /// In en, this message translates to:
  /// **'Clickable usernames'**
  String get comments_clickableUsername;

  /// No description provided for @comments_highlightNewComments.
  ///
  /// In en, this message translates to:
  /// **'Highlight new comments'**
  String get comments_highlightNewComments;

  /// No description provided for @comments_volumeRockerNavigation.
  ///
  /// In en, this message translates to:
  /// **'Navigate with volume rocker'**
  String get comments_volumeRockerNavigation;

  /// No description provided for @comments_animateNavigation.
  ///
  /// In en, this message translates to:
  /// **'Animate navigation'**
  String get comments_animateNavigation;

  /// No description provided for @comments_showSaveButton.
  ///
  /// In en, this message translates to:
  /// **'Show save button'**
  String get comments_showSaveButton;

  /// No description provided for @comments_swipeToClose.
  ///
  /// In en, this message translates to:
  /// **'Swipe to close thread'**
  String get comments_swipeToClose;

  /// No description provided for @posts_defaultHomeSort.
  ///
  /// In en, this message translates to:
  /// **'Default Home sort'**
  String get posts_defaultHomeSort;

  /// No description provided for @posts_defaultSort.
  ///
  /// In en, this message translates to:
  /// **'Default sort'**
  String get posts_defaultSort;

  /// No description provided for @posts_rememberSortByCommunity.
  ///
  /// In en, this message translates to:
  /// **'Remember by Community'**
  String get posts_rememberSortByCommunity;

  /// No description provided for @posts_rememberSortByCommunityDescription.
  ///
  /// In en, this message translates to:
  /// **'Each community will remember the last sort you selected.'**
  String get posts_rememberSortByCommunityDescription;

  /// No description provided for @posts_showAwards.
  ///
  /// In en, this message translates to:
  /// **'Show awards'**
  String get posts_showAwards;

  /// No description provided for @posts_clickableAwards.
  ///
  /// In en, this message translates to:
  /// **'Clickable awards'**
  String get posts_clickableAwards;

  /// No description provided for @posts_showPostFlair.
  ///
  /// In en, this message translates to:
  /// **'Show post flair'**
  String get posts_showPostFlair;

  /// No description provided for @posts_showFlairColors.
  ///
  /// In en, this message translates to:
  /// **'Show flair colors'**
  String get posts_showFlairColors;

  /// No description provided for @posts_showEmojis.
  ///
  /// In en, this message translates to:
  /// **'Show emojis'**
  String get posts_showEmojis;

  /// No description provided for @posts_tapFlairToSearch.
  ///
  /// In en, this message translates to:
  /// **'Tap flair to search'**
  String get posts_tapFlairToSearch;

  /// No description provided for @posts_showAuthor.
  ///
  /// In en, this message translates to:
  /// **'Show author'**
  String get posts_showAuthor;

  /// No description provided for @posts_clickableCommunity.
  ///
  /// In en, this message translates to:
  /// **'Tap community to visit'**
  String get posts_clickableCommunity;

  /// No description provided for @posts_clickableUser.
  ///
  /// In en, this message translates to:
  /// **'Tap username to view profile'**
  String get posts_clickableUser;

  /// No description provided for @posts_showFloatingButton.
  ///
  /// In en, this message translates to:
  /// **'Show floating button'**
  String get posts_showFloatingButton;

  /// No description provided for @posts_showHideButton.
  ///
  /// In en, this message translates to:
  /// **'Hide'**
  String get posts_showHideButton;

  /// No description provided for @posts_showMarkAsReadButton.
  ///
  /// In en, this message translates to:
  /// **'Mark as read'**
  String get posts_showMarkAsReadButton;

  /// No description provided for @posts_showShareButton.
  ///
  /// In en, this message translates to:
  /// **'Share'**
  String get posts_showShareButton;

  /// No description provided for @posts_showCommentsButton.
  ///
  /// In en, this message translates to:
  /// **'Comments'**
  String get posts_showCommentsButton;

  /// No description provided for @posts_showOpenInAppButton.
  ///
  /// In en, this message translates to:
  /// **'Open in app'**
  String get posts_showOpenInAppButton;

  /// No description provided for @data_wifiDataSaver.
  ///
  /// In en, this message translates to:
  /// **'Wifi data saver'**
  String get data_wifiDataSaver;

  /// No description provided for @data_wifiDataSaverDescription.
  ///
  /// In en, this message translates to:
  /// **'Load lower-size media'**
  String get data_wifiDataSaverDescription;

  /// No description provided for @data_mobileDataSaver.
  ///
  /// In en, this message translates to:
  /// **'Mobile data saver'**
  String get data_mobileDataSaver;

  /// No description provided for @data_mobileDataSaverDescription.
  ///
  /// In en, this message translates to:
  /// **'Load lower-size media'**
  String get data_mobileDataSaverDescription;

  /// No description provided for @data_autoplay.
  ///
  /// In en, this message translates to:
  /// **'Autoplay videos'**
  String get data_autoplay;

  /// No description provided for @data_videoQuality.
  ///
  /// In en, this message translates to:
  /// **'Video quality'**
  String get data_videoQuality;

  /// No description provided for @data_minimumQuality.
  ///
  /// In en, this message translates to:
  /// **'Minimum quality'**
  String get data_minimumQuality;

  /// No description provided for @data_maximumQuality.
  ///
  /// In en, this message translates to:
  /// **'Maximum quality'**
  String get data_maximumQuality;

  /// No description provided for @data_loadImages.
  ///
  /// In en, this message translates to:
  /// **'Load images'**
  String get data_loadImages;

  /// No description provided for @data_preferredQuality.
  ///
  /// In en, this message translates to:
  /// **'Image Quality'**
  String get data_preferredQuality;

  /// No description provided for @filters_blurNSFW.
  ///
  /// In en, this message translates to:
  /// **'Blur images in NSFW posts'**
  String get filters_blurNSFW;
}

class _AppLocalizationsDelegate
    extends LocalizationsDelegate<AppLocalizations> {
  const _AppLocalizationsDelegate();

  @override
  Future<AppLocalizations> load(Locale locale) {
    return SynchronousFuture<AppLocalizations>(lookupAppLocalizations(locale));
  }

  @override
  bool isSupported(Locale locale) =>
      <String>['en'].contains(locale.languageCode);

  @override
  bool shouldReload(_AppLocalizationsDelegate old) => false;
}

AppLocalizations lookupAppLocalizations(Locale locale) {
  // Lookup logic when only language code is specified.
  switch (locale.languageCode) {
    case 'en':
      return AppLocalizationsEn();
  }

  throw FlutterError(
      'AppLocalizations.delegate failed to load unsupported locale "$locale". This is likely '
      'an issue with the localizations generation tool. Please file an issue '
      'on GitHub with a reproducible sample app and the gen-l10n configuration '
      'that was used.');
}
