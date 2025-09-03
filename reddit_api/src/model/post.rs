use crate::result::Result;
pub use chrono::{DateTime, Local, Utc};
use getset::{CloneGetters, CopyGetters, Getters, Setters};

use crate::model::author;
use crate::model::author::AuthorInfo;
use crate::model::flair::Flair;
use crate::utils;
use crate::{client::VoteDirection, error::Error};
use serde::{Deserialize, Serialize};
use serde_with::{serde_as, with_prefix};

use super::Votable;
use super::{Fullname, Thing, comment::CommentSort, gallery::Gallery, subreddit::SubredditInfo};

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
#[derive(
    Default, Debug, Clone, PartialEq, Serialize, Deserialize, CloneGetters, Getters, Setters,
)]
#[getset(get_clone = "pub with_prefix", get = "pub(crate)")]
pub struct Post {
    /// flutter_rust_bridge:getter,sync
    name: Fullname,
    /// flutter_rust_bridge:getter,sync
    id: PostID,
    user_reports: Vec<Option<String>>,
    gilded: u32,
    /// flutter_rust_bridge:getter,sync
    title: String,
    /// flutter_rust_bridge:getter,sync
    domain: String,
    /// flutter_rust_bridge:getter,sync
    url: String,
    /// flutter_rust_bridge:getter,sync
    permalink: String,
    /// flutter_rust_bridge:getter,sync
    url_overridden_by_dest: Option<String>,
    /// flutter_rust_bridge:getter,sync
    selftext: Option<String>,
    /// flutter_rust_bridge:getter,sync
    selftext_html: Option<String>,
    /// flutter_rust_bridge:getter,sync
    num_comments: u32,

    /// Date of creation in UTC
    /// flutter_rust_bridge:getter,sync
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    created_utc: DateTime<Utc>,
    /// Date of creation in logged in user locale
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    #[serde(default)]
    created: DateTime<Local>,

    // User relationship
    clicked: bool,
    hidden: bool,
    /// flutter_rust_bridge:getter,sync
    #[getset(set = "pub(crate)")]
    saved: bool,
    /// Some(true) if upvoted, Some(false) if downvoted, None otherwise
    /// flutter_rust_bridge:getter,sync
    #[getset(set = "pub(crate)")]
    likes: Option<bool>,
    visited: bool,

    // Score
    /// flutter_rust_bridge:getter,sync
    downs: u32,
    /// flutter_rust_bridge:getter,sync
    upvote_ratio: f64,
    /// flutter_rust_bridge:getter,sync
    ups: u32,
    /// flutter_rust_bridge:getter,sync
    score: i32,

    #[serde(flatten, with = "prefix_link_flair")]
    /// flutter_rust_bridge:getter,sync
    link_flair: Flair,

    #[serde(flatten)]
    #[getset(skip)]
    thumbnail: ThumbnailOption,

    #[serde(
        flatten,
        serialize_with = "author::serialize_author_option",
        deserialize_with = "author::author_option"
    )]
    /// flutter_rust_bridge:getter,sync
    author: Option<AuthorInfo>,

    #[serde(flatten)]
    /// flutter_rust_bridge:getter,sync
    subreddit: SubredditInfo,

    // Media
    #[serde(deserialize_with = "utils::response_or_none")]
    /// flutter_rust_bridge:getter,sync
    media: Option<Media>,
    #[serde(deserialize_with = "utils::response_or_none")]
    /// flutter_rust_bridge:getter,sync
    media_embed: Option<MediaEmbed>,
    #[serde(deserialize_with = "utils::response_or_none")]
    /// flutter_rust_bridge:getter,sync
    secure_media: Option<Media>,
    #[serde(deserialize_with = "utils::response_or_none")]
    /// flutter_rust_bridge:getter,sync
    secure_media_embed: Option<SecureMediaEmbed>,
    #[serde(flatten)]
    /// flutter_rust_bridge:getter,sync
    gallery: Option<Gallery>,

    // Moderation
    hide_score: bool,
    quarantine: bool,
    can_mod_post: bool,
    approved_by: Option<String>,
    mod_note: Option<String>,
    removed_by_category: Option<String>,
    //pub banned_by: Option<String>,
    mod_reason_by: Option<String>,
    removal_reason: Option<String>,
    removed_by: Option<String>,
    banned_at_utc: Option<f64>,
    approved_at_utc: Option<f64>,
    mod_reason_title: Option<String>,
    #[serde(deserialize_with = "utils::response_or_default")]
    report_reasons: Vec<String>,
    mod_reports: Vec<Option<String>>,

    // Post kind
    post_hint: Option<String>,
    /// flutter_rust_bridge:getter,sync
    is_self: bool,
    is_original_content: bool,
    /// flutter_rust_bridge:getter,sync
    is_reddit_media_domain: bool,
    is_meta: bool,
    is_created_from_ads_ui: bool,
    media_only: bool,
    category: Option<String>,
    is_video: bool,
    #[serde(default)]
    is_gallery: bool,

    // Post properties
    /// flutter_rust_bridge:getter,sync
    pinned: bool,
    /// flutter_rust_bridge:getter,sync
    over_18: bool,
    /// flutter_rust_bridge:getter,sync
    preview: Option<Preview>,
    /// flutter_rust_bridge:getter,sync
    spoiler: bool,
    /// flutter_rust_bridge:getter,sync
    locked: bool,
    /// flutter_rust_bridge:getter,sync
    stickied: bool,
    #[serde(deserialize_with = "utils::response_or_none")]
    /// flutter_rust_bridge:getter,sync
    edited: Option<f64>,
    /// Suggested sort for comments
    /// flutter_rust_bridge:getter,sync
    suggested_sort: Option<CommentSort>,
    view_count: Option<String>,
    archived: bool,
    is_crosspostable: bool,

    // Other stuff
    top_awarded_type: Option<String>,
    total_awards_received: Option<u32>,
    gildings: Gildings,
    #[serde_as(deserialize_as = "serde_with::DefaultOnNull")]
    content_categories: Vec<String>,
    wls: Option<i32>,
    pwls: Option<i32>,
    allow_live_comments: bool,
    no_follow: bool,
    all_awardings: Vec<Option<String>>,
    awarders: Vec<Option<String>>,
    can_gild: bool,
    treatment_tags: Vec<Option<String>>,
    num_reports: Option<u32>,
    distinguished: Option<String>,
    is_robot_indexable: bool,
    num_duplicates: Option<u32>,
    discussion_type: Option<String>,
    send_replies: bool,
    contest_mode: bool,
    num_crossposts: Option<u32>,
    #[serde(default)]
    /// flutter_rust_bridge:getter,sync
    crosspost_parent_list: Vec<Post>,
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

impl Votable for Post {
    async fn vote(
        &mut self,
        direction: VoteDirection,
        client: &crate::client::Client,
    ) -> crate::result::Result<()> {
        client.vote(&self.name, direction).await?;
        self.likes = direction.into();
        Ok(())
    }

    async fn save(&mut self, client: &crate::client::Client) -> crate::result::Result<()> {
        client.save(&self.name).await?;
        self.saved = true;
        Ok(())
    }
    async fn unsave(&mut self, client: &crate::client::Client) -> crate::result::Result<()> {
        client.unsave(&self.name).await?;
        self.saved = false;
        Ok(())
    }
}

fn is_image_url(url: &str) -> bool {
    const EXTENSIONS: [&str; 5] = ["jpg", "jpeg", "png", "svg", "gif"];
    EXTENSIONS.iter().any(|pat| url.ends_with(pat))
}

impl TryFrom<Thing> for Post {
    type Error = Error;

    fn try_from(value: Thing) -> std::result::Result<Self, Self::Error> {
        match value {
            Thing::Post(post) => Ok(post),
            _ => Err(Error::InvalidThing),
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
