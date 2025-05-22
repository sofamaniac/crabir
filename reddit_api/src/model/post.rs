pub use chrono::{DateTime, Local, Utc};
use std::time::Duration;

use crate::error::Error;
use crate::model::flair::Flair;
use crate::utils;
use serde::{Deserialize, Serialize};
use serde_with::{DurationSeconds, serde_as, with_prefix};

use super::{Fullname, Thing};

with_prefix!(prefix_link_flair "link_flair_");
with_prefix!(prefix_flair "flair_");
with_prefix!(prefix_author_flair "author_flair_");

pub enum Kind {
    Selftext,
    Meta,
    Image,
    Video,
    Gallery,
    Link,
    Unknown,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde_as]
pub struct AuthorInfo {
    #[serde(rename = "author")]
    pub username: String,
    #[serde(rename = "author_premium", default)]
    pub premium: bool,
    #[serde(flatten, with = "prefix_author_flair")]
    pub flair: Flair,
    #[serde(rename = "author_fullname", default)]
    pub fullname: String,
    #[serde(rename = "author_is_blocked")]
    pub is_blocked: bool,
    #[serde(rename = "author_patreon_flair", default)]
    pub patreon_flair: bool,
}

/// Subreddit's ID, is of the form `t5_xxx`
#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct SubredditID(String);

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct SubredditInfo {
    /// The subreddit's name (e.g. "awww")
    #[serde(rename = "subreddit")]
    pub subreddit: String,
    #[serde(rename = "subreddit_type")]
    pub subreddit_type: String,
    #[serde(rename = "subreddit_id")]
    pub subreddit_id: SubredditID,
    /// The subreddit's name prefixed with "r/"
    #[serde(rename = "subreddit_name_prefixed")]
    pub subreddit_name_prefixed: String,
    /// The number of subscribers of the subreddit
    #[serde(rename = "subreddit_subscribers")]
    pub subscribers: u64,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct PostID(String);

#[serde_as]
#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Post {
    pub name: Fullname,
    pub id: PostID,
    pub user_reports: Vec<Option<String>>,
    pub gilded: i64,
    pub title: String,
    pub domain: String,
    pub url: String,
    pub permalink: String,
    pub url_overridden_by_dest: Option<String>,
    pub selftext: Option<String>,
    pub selftext_html: Option<String>,
    pub num_comments: i64,

    /// Date of creation in UTC
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    pub created_utc: DateTime<Utc>,
    /// Date of creation in logged in user locale
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    pub created: DateTime<Local>,

    // User relationship
    pub clicked: bool,
    pub hidden: bool,
    pub saved: bool,
    /// Some(true) if upvoted, Some(false) if downvoted, None otherwise
    pub likes: Option<bool>,
    pub visited: bool,

    // Score
    pub downs: i64,
    pub upvote_ratio: f64,
    pub ups: i64,
    pub score: i64,

    #[serde(flatten, with = "prefix_link_flair")]
    pub link_flair: Flair,

    #[serde(flatten)]
    thumbnail: ThumbnailOption,

    #[serde(
        flatten,
        serialize_with = "utils::serialize_author_option",
        deserialize_with = "utils::author_option"
    )]
    pub author: Option<AuthorInfo>,

    #[serde(flatten)]
    pub subreddit: SubredditInfo,

    // Media
    #[serde(deserialize_with = "utils::response_or_none")]
    pub media: Option<Media>,
    #[serde(deserialize_with = "utils::response_or_none")]
    pub media_embed: Option<MediaEmbed>,
    #[serde(deserialize_with = "utils::response_or_none")]
    pub secure_media: Option<SecureMedia>,
    #[serde(deserialize_with = "utils::response_or_none")]
    pub secure_media_embed: Option<SecureMediaEmbed>,
    pub gallery: Option<Gallery>,

    // Moderation
    pub hide_score: bool,
    pub quarantine: bool,
    pub can_mod_post: bool,
    pub approved_by: Option<String>,
    pub mod_note: Option<String>,
    pub removed_by_category: Option<String>,
    pub banned_by: Option<String>,
    pub mod_reason_by: Option<String>,
    pub removal_reason: Option<String>,
    pub removed_by: Option<String>,
    pub banned_at_utc: Option<f32>,
    pub approved_at_utc: Option<f32>,
    pub mod_reason_title: Option<String>,
    pub report_reasons: Option<String>,
    pub mod_reports: Vec<Option<String>>,

    // Post kind
    pub post_hint: Option<String>,
    pub is_self: bool,
    pub is_original_content: bool,
    pub is_reddit_media_domain: bool,
    pub is_meta: bool,
    pub is_created_from_ads_ui: bool,
    pub media_only: bool,
    pub category: Option<String>,
    pub is_video: bool,

    // Post properties
    pub pinned: bool,
    pub over_18: bool,
    pub preview: Option<Preview>,
    pub spoiler: bool,
    pub locked: bool,
    pub stickied: bool,
    #[serde(deserialize_with = "utils::response_or_none")]
    pub edited: Option<f64>,
    /// Suggested sort for comments
    pub suggested_sort: Option<String>,
    pub view_count: Option<String>,
    pub archived: bool,
    pub is_crosspostable: bool,

    // Other stuff
    pub top_awarded_type: Option<String>,
    pub total_awards_received: Option<i64>,
    pub gildings: Gildings,
    #[serde_as(deserialize_as = "serde_with::DefaultOnNull")]
    pub content_categories: Vec<String>,
    pub wls: Option<i64>,
    pub pwls: Option<i64>,
    pub allow_live_comments: bool,
    pub no_follow: bool,
    pub all_awardings: Vec<Option<String>>,
    pub awarders: Vec<Option<String>>,
    pub can_gild: bool,
    pub treatment_tags: Vec<Option<String>>,
    pub num_reports: Option<String>,
    pub distinguished: Option<String>,
    pub is_robot_indexable: bool,
    pub num_duplicates: Option<u64>,
    pub discussion_type: Option<String>,
    pub send_replies: bool,
    pub contest_mode: bool,
    pub num_crossposts: Option<i64>,
}

impl Post {
    #[must_use]
    pub fn thumbnail(&self) -> Option<Thumbnail> {
        if let ThumbnailOption {
            url: Some(url),
            width: Some(width),
            height: Some(height),
        } = &self.thumbnail
        {
            Some(Thumbnail {
                url: url.clone(),
                height: *height,
                width: *width,
            })
        } else {
            None
        }
    }

    #[must_use]
    pub fn kind(&self) -> Kind {
        if self.is_video {
            Kind::Video
        } else if self.is_self {
            Kind::Selftext
        } else if self.is_meta {
            Kind::Meta
        } else if self.gallery.is_some() {
            Kind::Gallery
        } else if let Some(post_hint) = &self.post_hint {
            match post_hint.as_str() {
                "image" => Kind::Image,
                "rich:video" | "hosted:video" => Kind::Video,
                "link" => Kind::Link,
                _ => Kind::Unknown,
            }
        } else {
            Kind::Unknown
        }
    }
}

impl TryFrom<Thing> for Post {
    type Error = Error;

    fn try_from(value: Thing) -> Result<Self, Self::Error> {
        match value {
            Thing::Post(post) => Ok(post),
            _ => Err(Error::InvalidThing),
        }
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct MediaEmbed {
    pub content: String,
    pub width: i64,
    pub scrolling: bool,
    pub height: i64,
}

pub struct Thumbnail {
    pub url: String,
    pub height: u64,
    pub width: u64,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
struct ThumbnailOption {
    #[serde(rename = "thumbnail")]
    pub url: Option<String>,
    #[serde(rename = "thumbnail_height")]
    pub height: Option<u64>,
    #[serde(rename = "thumbnail_width")]
    pub width: Option<u64>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(remote = "ThumbnailOption")]
struct ThumbnailURL {
    #[serde(rename = "thumbnail_url")]
    pub url: Option<String>,
    #[serde(rename = "thumbnail_height")]
    pub height: Option<u64>,
    #[serde(rename = "thumbnail_width")]
    pub width: Option<u64>,
}

#[derive(Serialize, Deserialize, Debug, PartialEq, Clone)]
#[serde(untagged)]
pub enum SecureMedia {
    RedditVideo(RedditVideo),
    Oembed {
        oembed: Oembed,
        #[serde(rename = "type")]
        type_field: String,
    },
}

#[serde_as]
#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct RedditVideo {
    bitrate_kbps: u64,
    width: u64,
    height: u64,
    has_audio: bool,
    is_gif: bool,
    fallback_url: String,
    scrubber_media_url: String,
    dash_url: String,
    hls_url: String,
    /// Duration in seconds
    #[serde_as(as = "DurationSeconds<u64>")]
    duration: Duration,
    transcoding_status: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct SecureMediaEmbed {
    pub content: String,
    pub width: i64,
    pub scrolling: bool,
    pub media_domain_url: String,
    pub height: i64,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Gildings {}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Preview {
    pub images: Vec<Image>,
    pub enabled: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Image {
    pub source: ImageBase,
    pub resolutions: Vec<ImageBase>,
    pub variants: Variants,
    pub id: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct ImageBase {
    pub url: String,
    pub width: i64,
    pub height: i64,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Variants {}

#[derive(Serialize, Deserialize, Debug, PartialEq, Clone)]
#[serde(untagged)]
pub enum Media {
    RedditVideo(RedditVideo),
    Oembed {
        oembed: Oembed,
        #[serde(rename = "type")]
        type_field: String,
    },
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Oembed {
    pub provider_url: String,
    pub title: String,
    pub html: String,
    pub height: i64,
    pub width: i64,
    pub version: String,
    pub author_name: String,
    pub provider_name: String,
    #[serde(rename = "type")]
    pub type_field: String,
    pub author_url: String,
    #[serde(flatten, with = "ThumbnailURL")]
    thumbnail: ThumbnailOption,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Gallery {}
