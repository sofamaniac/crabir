## [unreleased]

### 🚀 Features

- Report menu
- Giphy in comments and user / subreddit links

### 🐛 Bug Fixes

- Navigate to subreddit when searching
- Show nsfw switch crashing the app
- Reduce indent size in threads
- Better paywall behavior
- Revenue cat initialization
- Properly handle spoilers in text
- Navigate to post from user's tab
- Galleries no longer jump to end in user tabs

### 💼 Other

- *(deps)* Bump source_gen from 2.0.0 to 3.1.0 in /crabir
## [0.1.0] - 2025-10-14

### 🚀 Features

- Logout button and fix: refresh after adding account
- *(post)* Now with different title color for read post
- *(animation)* Score now animates when changing
- *(subreddit)* Can now (un)subscribe from subreddits
- *(share)* Now with the ability to share posts
- *(subreddit)* Right side menu, more option button and rules view
- *(theming)* Add support for dark/light theme
- *(theming)* Basic theme editor
- *(theme)* Button to switch between dark/light theme
- *(theming)* Select theme while editing
- *(comments)* Add button to share a comment
- *(video)* Add button to go fullscreen on videos
- *(feed)* Support remembering last selected sort by community
- Add lock icon on locked posts
- *(animation)* Add animation to upvote/downvote/save buttons
- *(filters)* Add the possibility to filter flairs, domains, subreddits, and users
- *(theme)* Add editor for secondary text and downvote colors
- Add support for streamable.com videos
- Add missing post buttons
- Adaptive icon
- Subscribe & favorite subreddits
- Lateral menu settings
- Go to dropdown menu
- Basic comment editor
- Basic post creator
- *(post editor)* Support for nsfw & spoiler
- *(post editor)* Support for selftext & link posts

### 🐛 Bug Fixes

- Buttons under comments not hiding properly
- Now display placeholder while video is loading or not visible
- Reddit updated its api
- Missing localization for sort in settings
- *(navigation)* Properly go back to home screen before exiting the app
- *(gallery)* Properly blur the content of galleries
- *(flair)* Now properly adapt text color for readibility
- *(flair)* Luminance check was not always done
- *(post)* Hide selftext when spoiler
- *(gallery)* Now properly blur when spoiler
- *(video)* Spoiler now working
- *(video)* Better video player behavior when there is multiple videos on screen
- Missing thumbnails in thread
- Video player beheavior
- *(comment)* Alignment and flair taking too much space
- *(theming)* Wrong colors were applied
- *(theming)* Fix cards and add system nav bar theming
- *(client)* Some subscriptions where missing
- *(comment)* Fix crash when username is not specified
- *(post)* Change stickied post title color
- *(video)* Add missing background in video timer
- *(login)* Login now properly work
- Remembered community sorts now works properly
- *(licenses)* Now show licenses of rust dependencies
- Now display license notice when available and add utility to generate the assets required
- *(feed)* Avoid duplicated posts
- Reset UI when switching account
- Text in post header going under subreddit icon
- Buttons color
- Status bar color
- Comments view
- *(user view)* Scroll position is now independant for each tab
- *(user view)* Scroll position is now independant for each tab
- Better button animation
- User view
- Max lines in html view
- Empty selftext adding empty space in post view
- Hide comments score
- Post more option button color
- Loading indicator was permanently there in thread view
- Put thread in safe area
- Remove shadow in crosspost and tapping on card now navigates to parent post
- Switching user now goes back to first tab
- Theming
- *(thread)* Collapse comment
- *(theme)* Fix typo in theme preventing them from being saved
- *(thread)* Score not always on the end of the row
- *(comment)* Missing space between score and time
- *(media)* Fullscreen media going behind status bar
- *(post)* Show selftext under the media content of a post
- Honour show comment images option (mostly)
- Honour show comment images option
- Selftext alignment in post card
- Keep only one comment bar open at a time
- Align settings titles
- Remove duplicate posts from feeds
- Avoid calling setState after dispose
- Better swipe transition, with threshold support
- Reset 3rd tab when leaving it
- Video player placeholder
- Videos no longer capture device audio
- Show correct subreddit icon
- Better video behavior
- Refresh feed button
- Even better video player behavior, and now with more cartouche
- Fix video behavior for real this time
- Blur nsfw now updates immediately when setting changes
- Small change to account indicator & selector
- Number of child comments
- Remove unneeded divider
- Tabs in user profile not working properly
- Tabs in user profile not working properly
- Search tab freeze
- Filters not being saved
- Settings screen leaving application
- Navigation to user tab

### 💼 Other

- Resolution
- Upgrade everything required to build with share_plus
- *(deps)* Bump connectivity_plus from 6.1.5 to 7.0.0 in /crabir

### 🚜 Refactor

- *(post)* Split post widget into component widgets
- *(video)* Simplified how the video player is handled using `VisibilityDetector`
- *(video)* Simplified how the video player is handled using `VisibilityDetector`
- Move post view into its own file
- *(settings)* Change widget to selection resolution
