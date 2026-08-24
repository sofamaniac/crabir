## [0.6.1] - 2026-08-24

### 🚀 Features

- Create crosspost
- *(post editor)* Change flair
- Change post flair
- Enable searching for nsfw communities
- Subscribe / unsubscribe to subreddit
- About & overview tab in profile
- Handle reddit shortlinks
- See more comments & fix position not maintained when returning to thread
- French translation
- Read messages
- Add support for app language setting
- Support for streamable.com videos
- Add download button (partial)
- Mute filters
- Set view per community or global
- Change quality of video that supports it (code written by gemini)
- Change account when submitting post / crosspost
- Flair settings
- Hide author & clickable author / community
- Choose which button to show below post
- Start video on wifi only / never, upvote on save
- Better splash screen
- Switch from markdown to richtext json where applicable
- Badge showing the number of unread messages in bottom tab bar
- Scheduled theme mode
- Quality settings for images
- Network based video quality
- App shortcuts
- New upvote / downvote button icons
- Limit height of images in card
- Manage saved community view settings
- Upvote on save
- Read inbox and mark messages as read
- Send new messages
- When reopening thread scroll to last position
- Report comment
- Share / copy comment
- Comments settings
- Traduction en français
- Comment navigation toolbar or using volume keys
- Reintroduce the ability to go to random communities
- Configure lateral menu

### 🐛 Bug Fixes

- Profile button in bottom bar crashes the app
- Opening thread in search crashes the app
- Logout flow
- Reload content when adding account
- Wrong theme applied when following system settings
- Issue in deep linking and several home views being pushed on top of one another
- User info not loading after logging in
- Thread not properly closing
- The app works properly on api 28
- Bigger avatar size
- Share menu & changed more options menu to a dialog
- Theme editor crashing app
- Cannot change sort in search
- Icon size too big in account tile
- Remember sort of community
- Add back dividers between posts, and between top level comments
- Gallery background
- Multi account
- Add warning before logging in
- Drawer not fully closing
- Ensure unicity of accounts
- Cannot serialize auth state
- Gallery
- Crash when opening thread
- Saved tab crashing the app & view jumping around
- Reddit videos
- Wrong color for gallery title
- Allow dash in username
- Properly support opening preview.redd.it links
- Media metadata deserialization
- Refresh thread
- Gallery not properly working
- Disable gif & image rendering in markdown
- Youtube video size
- Score string not properly updating for comments
- Respect hidden score on collapsed comments
- Edit post dialog not opening
- Spoiler gallery not blurred
- Going back from preview.redd.it
- Giphy
- Crash when ups and downs are zero
- Opening crosspost crashing the app
- Community search
- Crosspost creator & post creator now with snackbar
- Refresh indicator
- Allow post header to occupy multiple lines
- Better video support
- Feed always going to top after changing sort
- Properly insert comment when loading more
- Player remaining time text color
- Remove Loading text on r/popular and r/all
- Avoid crash when no preview is available for a gallery element
- Trying to fix spoiler in markdown
- Proper markdown parsing
- Proper markdown parsing
- Remove old spoiler parsing code
- Hide links in spoilers
- Text size in comments and posts
- Division by zero
- Gradient color
- Spoilers & superscript no longer render in code blocks
- Post sometimes not showing in search
- Switching account while viewing profile could crash the app
- Gallery sometimes crashes the app
- Remove singleton on profile repositories
- Remove math parsing from Markdown parser
- Less api calls when viewing user's comments
- Superscript did not respect escaping
- Translation
- Reddit autolinks
- Translations
- Unnecessary requests to fetch subscriptions
- Deep linking recreating activity
- Image post do not require text
- Link style in markdown
- Search subreddits by default
- *(ui)* Better option select menu
- Respect thread suggested sort
- Update UI when hiding post
- Missing translations
- Incorrect system bar color when viewing media in fullscreen
- Use proper drawer function to handle back gesture
- Anonymous accounts
- Prevent dismiss when fullscreen content is zoomed
- Markdown rendering should be less laggy
- Reddit username/subreddit link
- Links in post & comments not opening in app
- No longer crashes when no internet
- Gallery not respecting spoiler & blur
- *(markdown)* Relative link improperly parsed
- Can now reply to comment / post without crashing the app
- App crash when switching to anonymous from profile view
- Issue when switching back and forth to the anonymous user
- Editing flair crash the app
- R/all & r/popular
- Post body flickers when scrolling
- Image size in markdown
- Blur on older android version
- Wrong comment being displayed in profile
- Toolbar hidden behind keyboard in markdown editor
- Add comment to view when posting a new comment and switch to a modal bottom sheet for the comment editor
- Post would turn black on older api when video playback started (fix written by gemini)
- Video breaking when opening in fullscreen
- Video not working when going back from a thread
- Overflow in post & subreddit top bar & better handling of subreddit banner
- Only one post should try to show the video if they are the same video
- Incorrect depth when replying directly to post
- Fab icon color
- User comment tab showing nothing
- System bar background color
- View settings page missing top bar
- Crash when no internet and loading subscriptions
- Video autostart
- App startup flicker
- Fullscreen image
- Prevent nested spoilers
- Clickable element in spoilers
- Table headers do not always have content
- Video player sometimes overflowing its bounds
- Richtext image & table render & make spoilers inline
- Better richtext tables
- Inbox could crash the app
- Incorrect colors when using dynamic colors
- Blurred video
- Open in app button
- Make post header slightly bigger
- Richtext would sometimes be missing parts
- Crosspost creator
- Video autostart
- Post search improper position after setting sort
- Do not open keyboard in search tab when query is not empty
- Anonymous browsing (again)
- User / community link color
- Switch color in drawer and search tab
- Settings tiles
- Add top bar to license page
- Dynamic color secondary text
- Missing close button in theme editor
- Add divier between posts
- Upvote animation playing again when opening rhread
- Badge in bottom bar not updating
- Sort not properly remembered
- Cache size
- Sort not properly remembered
- System bars color not properly resetting when exiting fullscreen media view
- Make images in comments take full width
- Display self text in crosspost
- Show posts when viewing muted subreddit
- Comment background color
- Theme previewer padding
- More replies button padding
- Remove hardcoded strings
- Sort not properly remembered
- Add missing selftext in feed
- Missing lock icon in post header

### 💼 Other

- Proper parser for reddit markdown
- Remove markwon
- Update all dependencies that have new versions

### 🚜 Refactor

- More resilient info fetching for user
- Changed how accounts are handled
- Change source of truth for post and comments to a room
- Use proper card
- Remove unused code
- Repositories
- Moving markdown processing to its own module
- Swipe to dismiss box
- Simple history table
- Feed view unnecessary passing of view models
- Switch from hilt + dagger to koin
- Remove LocalDrawerState
- Avoid too many disk access to check if post was read
- Move settings into local composition providers
- Richtext
- Update ListItem
- History management
- Switch to landscapist for images

### ⚙️ Miscellaneous Tasks

- Update gradle properties
## [5.1] - 2026-03-21

### 🐛 Bug Fixes

- Crash in release mode
## [0.5] - 2026-03-21

### 🚀 Features

- Hide / unhide post, share post, copy post title
- Show rules in post creator
- Can now report posts
- Fully functional search tab
- Fullscreen video player & blur nsfw / spoiler
- Blur NSFW switch

### 🐛 Bug Fixes

- Dynamic theme crashing app on api < 31
- Set community when creating post from subreddit
- Switch to linear progress indicator
- *(a11y)* Add tooltip to vote/save buttons
- Gallery broken after exiting fullscreen
- Show placeholder for video posts without supported video
- Query not properly showing when searching flair
- System bar colors
- Add text when there is no comment to display
- Spacing between posts
- Disable blur background on pre android 12
- Markdown not properly displaying when viewing thread
- Subreddit deep linking

### 🚜 Refactor

- Introduce Fullname type to avoid confusion between things id / name
- Provide easy access to current account
- Changed a lot of things
## [0.4] - 2026-03-16

### 🚀 Features

- Collapse comments
- Comment on posts and reply to comments
- Post creator

### 🐛 Bug Fixes

- Feed going back to beginning when going back
- Incorrect comment background color
- Selftext is sometimes cut in thread view
- Prevent videos from being added to gallery
- Floating action button appearing on wrong scroll direction
- Floating action button colors

### 💼 Other

- Version code & name
- Changelog
## [0.3] - 2026-03-12

### 🚀 Features

- Support for multi column view
- History
- New license screen

### 🐛 Bug Fixes

- Missing app launcher
- Wrong icon selected in bottom bar
- Go to top of feed when changing sort
- Do not show black bar at the bottom of gallery if there is no caption
- Tapping "More" in thread not always working
- Wrong icon selected in tab row when viewing subreddit
- Navigation to user profile from outside link
## [0.2] - 2026-03-11

### 🚀 Features

- Implement bottom navigation bar and paging
- Navigation refactor
- Add working posts view

### 💼 Other

- Improve post fetching and add JSON support
- Subreddit subscription view
- Subreddit view and list
- Upgrade to JDK 21 and update dependencies
- Implement Home, Saved, and Subreddit post views
- Improve code organization and add missing types
- Route type, comments, posts, and flair
- Clean up and remove unused code
- Move Sort and Timeframe to post package
- Room database, markdown, post IDs, subreddit information
- Improve navigation and comment handling
- URL and URI handling
- Comments, home, and view
- Post view improvements
- Fetch full subreddit details and introduce Mappie for data mapping
- Convert snake_case to camelCase in PostDataFlat and update dependencies
- Feed now refreshes when changing sort
