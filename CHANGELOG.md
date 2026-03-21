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
