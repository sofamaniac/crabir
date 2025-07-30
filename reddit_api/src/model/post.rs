pub use chrono::{DateTime, Local, Utc};
use std::backtrace::Backtrace;

use crate::error::Error;
use crate::model::author;
use crate::model::author::AuthorInfo;
use crate::model::flair::Flair;
use crate::utils;
use serde::{Deserialize, Serialize};
use serde_with::{serde_as, with_prefix};

use super::{Fullname, Thing, gallery::Gallery, subreddit::SubredditInfo};

with_prefix!(prefix_link_flair "link_flair_");
with_prefix!(prefix_flair "flair_");

pub enum Kind {
    Selftext,
    Meta,
    Image,
    /// Video hosted on Reddit
    Video,
    /// Video hosted on Youtube
    YoutubeVideo,
    /// Gallery present in `Post::Gallery`
    Gallery,
    /// Gallery at `Post::url`
    MediaGallery,
    Link,
    Unknown,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct PostID(String);

#[serde_as]
#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
///flutter_rust_bridge:opaque
pub struct Post {
    pub name: Fullname,
    pub id: PostID,
    pub user_reports: Vec<Option<String>>,
    pub gilded: u32,
    pub title: String,
    pub domain: String,
    pub url: String,
    pub permalink: String,
    pub url_overridden_by_dest: Option<String>,
    pub selftext: Option<String>,
    pub selftext_html: Option<String>,
    pub num_comments: u32,

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
    pub downs: u32,
    pub upvote_ratio: f64,
    pub ups: u32,
    pub score: i32,

    #[serde(flatten, with = "prefix_link_flair")]
    pub link_flair: Flair,

    #[serde(flatten)]
    thumbnail: ThumbnailOption,

    #[serde(
        flatten,
        serialize_with = "author::serialize_author_option",
        deserialize_with = "author::author_option"
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
    pub secure_media: Option<Media>,
    #[serde(deserialize_with = "utils::response_or_none")]
    pub secure_media_embed: Option<SecureMediaEmbed>,
    #[serde(flatten)]
    pub gallery: Option<Gallery>,

    // Moderation
    pub hide_score: bool,
    pub quarantine: bool,
    pub can_mod_post: bool,
    pub approved_by: Option<String>,
    pub mod_note: Option<String>,
    pub removed_by_category: Option<String>,
    //pub banned_by: Option<String>,
    pub mod_reason_by: Option<String>,
    pub removal_reason: Option<String>,
    pub removed_by: Option<String>,
    pub banned_at_utc: Option<f64>,
    pub approved_at_utc: Option<f64>,
    pub mod_reason_title: Option<String>,
    #[serde(deserialize_with = "utils::response_or_default")]
    pub report_reasons: Vec<String>,
    pub mod_reports: Vec<Option<String>>,

    // Post kind
    post_hint: Option<String>,
    pub is_self: bool,
    pub is_original_content: bool,
    pub is_reddit_media_domain: bool,
    is_meta: bool,
    is_created_from_ads_ui: bool,
    media_only: bool,
    category: Option<String>,
    is_video: bool,
    #[serde(default)]
    is_gallery: bool,

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
    pub total_awards_received: Option<u32>,
    pub gildings: Gildings,
    #[serde_as(deserialize_as = "serde_with::DefaultOnNull")]
    pub content_categories: Vec<String>,
    pub wls: Option<i32>,
    pub pwls: Option<i32>,
    pub allow_live_comments: bool,
    pub no_follow: bool,
    pub all_awardings: Vec<Option<String>>,
    pub awarders: Vec<Option<String>>,
    pub can_gild: bool,
    pub treatment_tags: Vec<Option<String>>,
    pub num_reports: Option<u32>,
    pub distinguished: Option<String>,
    pub is_robot_indexable: bool,
    pub num_duplicates: Option<u32>,
    pub discussion_type: Option<String>,
    pub send_replies: bool,
    pub contest_mode: bool,
    pub num_crossposts: Option<u32>,
    #[serde(default)]
    pub crosspost_parent_list: Vec<Post>,
}

impl Post {
    #[must_use]
    ///flutter_rust_bridge:sync,getter
    pub fn thumbnail(&self) -> Option<Thumbnail> {
        if let ThumbnailOption {
            url: Some(url),
            width: Some(width),
            height: Some(height),
        } = &self.thumbnail
        {
            if url == "default" {
                None
            } else {
                Some(Thumbnail {
                    url: url.clone(),
                    height: *height,
                    width: *width,
                })
            }
        } else {
            None
        }
    }

    fn video_kind(&self) -> Kind {
        const DOMAINS: [&str; 2] = ["youtube.com", "youtu.be"];
        if DOMAINS.contains(&self.domain.as_str()) {
            Kind::YoutubeVideo
        } else {
            Kind::Video
        }
    }

    #[must_use]
    ///flutter_rust_bridge:sync,getter
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
                "hosted:video" => Kind::Video,
                "rich:video" => self.video_kind(),
                "link" => Kind::Link,
                _ => Kind::Unknown,
            }
        } else if self.url.starts_with("https://www.reddit.com/gallery") {
            Kind::MediaGallery
        } else if is_image_url(&self.url) {
            Kind::Image
        } else {
            Kind::Unknown
        }
    }

    ///flutter_rust_bridge:sync,getter
    pub fn is_crosspost(&self) -> bool {
        !self.crosspost_parent_list.is_empty()
    }
}

fn is_image_url(url: &str) -> bool {
    const EXTENSIONS: [&str; 5] = ["jpg", "jpeg", "png", "svg", "gif"];
    EXTENSIONS.iter().any(|pat| url.ends_with(pat))
}

impl TryFrom<Thing> for Post {
    type Error = Error;

    fn try_from(value: Thing) -> Result<Self, Self::Error> {
        match value {
            Thing::Post(post) => Ok(post),
            _ => Err(Error::InvalidThing {
                backtrace: Backtrace::capture(),
            }),
        }
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct MediaEmbed {
    pub content: String,
    pub width: u32,
    pub scrolling: bool,
    pub height: u32,
}

pub struct Thumbnail {
    pub url: String,
    pub height: u32,
    pub width: u32,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct ThumbnailOption {
    #[serde(rename = "thumbnail")]
    pub url: Option<String>,
    #[serde(rename = "thumbnail_height")]
    pub height: Option<u32>,
    #[serde(rename = "thumbnail_width")]
    pub width: Option<u32>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(remote = "ThumbnailOption")]
pub struct ThumbnailURL {
    #[serde(rename = "thumbnail_url")]
    pub url: Option<String>,
    #[serde(rename = "thumbnail_height")]
    pub height: Option<u32>,
    #[serde(rename = "thumbnail_width")]
    pub width: Option<u32>,
}

#[derive(Serialize, Deserialize, Debug, PartialEq, Clone)]
#[serde(untagged)]
/// flutter_rust_bridge:non_opaque
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
/// flutter_rust_bridge:non_opaque
pub struct RedditVideo {
    pub bitrate_kbps: u32,
    pub width: u32,
    pub height: u32,
    pub has_audio: bool,
    pub is_gif: bool,
    pub fallback_url: String,
    pub scrubber_media_url: String,
    pub dash_url: String,
    pub hls_url: String,
    /// Duration in seconds
    //#[serde_as(as = "DurationSeconds<u32>")]
    pub duration: u32,
    pub transcoding_status: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct SecureMediaEmbed {
    pub content: String,
    pub width: u32,
    pub scrolling: bool,
    pub media_domain_url: String,
    pub height: u32,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Gildings {}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Preview {
    pub images: Vec<RedditImage>,
    pub enabled: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
/// flutter_rust_bridge:non_opaque
pub struct RedditImage {
    pub source: ImageBase,
    pub resolutions: Vec<ImageBase>,
    pub variants: Variants,
    pub id: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct ImageBase {
    pub url: String,
    pub width: u32,
    pub height: u32,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
/// flutter_rust_bridge:non_opaque
pub struct Variants {
    pub gif: Option<VariantInner>,
    pub mp4: Option<VariantInner>,
    pub obfuscated: Option<VariantInner>,
    pub nsfw: Option<VariantInner>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
/// flutter_rust_bridge:non_opaque
pub struct VariantInner {
    pub source: ImageBase,
    pub resolutions: Vec<ImageBase>,
}

#[derive(Serialize, Deserialize, Debug, PartialEq, Clone)]
/// flutter_rust_bridge:non_opaque
pub enum Media {
    #[serde(rename = "reddit_video")]
    RedditVideo(RedditVideo),
    #[serde(rename = "oembed")]
    Oembed {
        oembed: Oembed,
        #[serde(rename = "type")]
        type_field: String,
    },
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
/// flutter_rust_bridge:non_opaque
pub struct Oembed {
    pub provider_url: String,
    pub title: String,
    pub html: String,
    pub height: u32,
    pub width: u32,
    pub version: String,
    pub author_name: String,
    pub provider_name: String,
    #[serde(rename = "type")]
    pub type_field: String,
    pub author_url: String,
    #[serde(flatten, with = "ThumbnailURL")]
    pub thumbnail: ThumbnailOption,
}
