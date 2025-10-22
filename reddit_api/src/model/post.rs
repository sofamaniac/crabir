use crate::model::subreddit::Subreddit;
use crate::result::Result;
pub use chrono::{DateTime, Local, Utc};
use flutter_getter::FlutterGetters;
use flutter_rust_bridge::frb;
use getset::{Getters, Setters};

use crate::client::Client;
use crate::model::author;
use crate::model::author::AuthorInfo;
use crate::model::flair::Flair;
use crate::utils;
use crate::votable::Votable;
use crate::{error::Error, votable::private::PrivateVotable};
use serde::{Deserialize, Serialize};
use serde_with::{serde_as, with_prefix};

use super::{Fullname, Thing, comment::CommentSort, gallery::Gallery, subreddit::SubredditInfo};

with_prefix!(prefix_link_flair "link_flair_");
with_prefix!(prefix_flair "flair_");

#[derive(Default, Debug, Clone, Copy, PartialEq, Serialize, Deserialize)]
pub enum Kind {
    #[default]
    Selftext,
    Meta,
    Image,
    /// Video hosted on Reddit
    Video,
    /// Video hosted on Youtube
    YoutubeVideo,
    /// Video hosted on Streamable
    StreamableVideo,
    /// Gallery present in `Post::Gallery`
    Gallery,
    /// Gallery at `Post::url`
    MediaGallery,
    Link,
    Unknown,
}

impl Kind {
    fn to_query_param(self) -> String {
        let s = match self {
            Kind::Selftext => "self",
            Kind::Image => "image",
            Kind::Video => "video",
            Kind::Link => "link",
            Kind::YoutubeVideo => "video",
            Kind::StreamableVideo => "video",
            Kind::Gallery => todo!(),
            Kind::MediaGallery => todo!(),
            Kind::Unknown => todo!(),
            Kind::Meta => todo!(),
        };
        s.to_owned()
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct PostID(String);

impl PostID {
    #[frb(sync, getter)]
    pub fn as_string(&self) -> String {
        self.0.clone()
    }
}

#[serde_as]
#[derive(
    Default, Debug, Clone, PartialEq, Serialize, Deserialize, FlutterGetters, Getters, Setters,
)]
#[getset(get = "pub(crate)")]
pub struct Post {
    #[getset(skip)]
    name: Fullname,
    id: PostID,
    user_reports: Vec<Option<String>>,
    gilded: u32,
    title: String,
    domain: String,
    url: String,
    permalink: String,
    url_overridden_by_dest: Option<String>,
    selftext: Option<String>,
    selftext_html: Option<String>,
    num_comments: u32,

    /// Date of creation in UTC
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    created_utc: DateTime<Utc>,
    /// Date of creation in logged in user locale
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    #[serde(default)]
    created: DateTime<Local>,

    // User relationship
    clicked: bool,
    hidden: bool,
    #[getset(skip)]
    saved: bool,
    /// Some(true) if upvoted, Some(false) if downvoted, None otherwise
    #[getset(skip)]
    likes: Option<bool>,
    visited: bool,

    // Score
    downs: u32,
    upvote_ratio: f64,
    ups: u32,
    score: i32,

    #[serde(flatten, with = "prefix_link_flair")]
    link_flair: Flair,

    #[serde(flatten)]
    #[getset(skip)]
    #[flutter_getter(skip)]
    thumbnail: ThumbnailOption,

    #[serde(
        flatten,
        serialize_with = "author::serialize_author_option",
        deserialize_with = "author::author_option"
    )]
    author: Option<AuthorInfo>,

    #[serde(flatten)]
    subreddit: SubredditInfo,

    // Media
    #[serde(deserialize_with = "utils::response_or_none")]
    media: Option<Media>,
    #[serde(deserialize_with = "utils::response_or_none")]
    media_embed: Option<MediaEmbed>,
    #[serde(deserialize_with = "utils::response_or_none")]
    secure_media: Option<Media>,
    #[serde(deserialize_with = "utils::response_or_none")]
    secure_media_embed: Option<SecureMediaEmbed>,
    #[serde(flatten)]
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
    is_self: bool,
    is_original_content: bool,
    is_reddit_media_domain: bool,
    is_meta: bool,
    is_created_from_ads_ui: bool,
    media_only: bool,
    category: Option<String>,
    is_video: bool,
    #[serde(default)]
    is_gallery: bool,

    // Post properties
    pinned: bool,
    over_18: bool,
    preview: Option<Preview>,
    spoiler: bool,
    locked: bool,
    stickied: bool,
    #[serde(deserialize_with = "utils::response_or_none")]
    edited: Option<f64>,
    /// Suggested sort for comments
    suggested_sort: Option<CommentSort>,
    view_count: Option<String>,
    archived: bool,
    is_crosspostable: bool,

    // Other stuff
    top_awarded_type: Option<String>,
    total_awards_received: Option<u32>,
    gildings: Gildings,
    // #[serde_as(deserialize_as = "serde_with::DefaultOnNull")]
    // content_categories: Vec<String>,
    // wls: Option<i32>,
    // pwls: Option<i32>,
    allow_live_comments: bool,
    no_follow: bool,
    all_awardings: Vec<Option<String>>,
    awarders: Vec<Option<String>>,
    can_gild: bool,
    treatment_tags: Vec<Option<String>>,
    // num_reports: Option<i32>,
    distinguished: Option<String>,
    // is_robot_indexable: bool,
    num_duplicates: Option<u32>,
    discussion_type: Option<String>,
    send_replies: bool,
    contest_mode: bool,
    num_crossposts: Option<u32>,
    #[serde(default)]
    crosspost_parent_list: Vec<Post>,
}

impl Post {
    #[must_use]
    #[frb(sync, getter)]
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
        const YOUTUBE: [&str; 2] = ["youtube.com", "youtu.be"];
        const STREAMABLE: [&str; 1] = ["streamable.com"];
        let domain = self.domain.as_str();
        if YOUTUBE.contains(&domain) {
            Kind::YoutubeVideo
        } else if STREAMABLE.contains(&domain) {
            Kind::StreamableVideo
        } else {
            Kind::Video
        }
    }

    #[must_use]
    #[frb(sync, getter)]
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

    #[frb(sync, getter)]
    pub fn is_crosspost(&self) -> bool {
        !self.crosspost_parent_list.is_empty()
    }

    /// Hide a post
    /// # Error
    /// Fails when the underlying request fails.
    pub async fn hide(&mut self, client: Client) -> Result<()> {
        client.hide(self).await?;
        self.hidden = true;
        Ok(())
    }

    /// Unhide a post
    /// # Error
    /// Fails when the underlying request fails.
    pub async fn unhide(&mut self, client: Client) -> Result<()> {
        client.unhide(self).await?;
        self.hidden = false;
        Ok(())
    }
}

impl PrivateVotable for Post {
    fn name(&self) -> &Fullname {
        &self.name
    }

    fn set_likes(&mut self, likes: Option<bool>) {
        self.likes = likes;
    }

    fn set_saved(&mut self, saved: bool) {
        self.saved = saved;
    }

    fn add_to_score(&mut self, delta: i32) {
        self.score += delta;
    }
}
impl Votable for Post {}

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
#[frb(non_opaque)]
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
#[frb(non_opaque)]
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
#[frb(non_opaque)]
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
pub struct Variants {
    pub gif: Option<VariantInner>,
    pub mp4: Option<VariantInner>,
    pub obfuscated: Option<VariantInner>,
    pub nsfw: Option<VariantInner>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct VariantInner {
    pub source: ImageBase,
    pub resolutions: Vec<ImageBase>,
}

#[derive(Serialize, Deserialize, Debug, PartialEq, Clone)]
#[frb(non_opaque)]
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
#[frb(non_opaque)]
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

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize, Setters, FlutterGetters)]
pub struct PostSubmitBuilder {
    title: String,
    text: Option<String>,
    #[flutter_getter(skip)]
    subreddit: String,
    nsfw: bool,
    spoiler: bool,
    kind: Kind,
    flair_id: Option<String>,
    flair_text: Option<String>,
    url: Option<String>,
    sendreplies: bool,
}

impl PostSubmitBuilder {
    #[frb(sync)]
    pub fn new() -> Self {
        Self::default()
    }

    #[frb(sync, setter)]
    pub fn set_title(&mut self, title: String) {
        self.title = title;
    }

    #[frb(sync, setter)]
    pub fn set_text(&mut self, text: Option<String>) {
        self.text = text;
    }

    #[frb(sync, setter)]
    pub fn set_subreddit(&mut self, subreddit: &Subreddit) {
        self.subreddit = subreddit.other().display_name().clone();
    }

    #[frb(sync, setter)]
    pub fn set_nsfw(&mut self, nsfw: bool) {
        self.nsfw = nsfw;
    }

    #[frb(sync, setter)]
    pub fn set_spoiler(&mut self, spoiler: bool) {
        self.spoiler = spoiler;
    }

    #[frb(sync, setter)]
    pub fn set_kind(&mut self, kind: Kind) {
        self.kind = kind;
    }

    #[frb(sync, setter)]
    pub fn set_flair_id(&mut self, flair_id: Option<String>) {
        self.flair_id = flair_id;
    }

    #[frb(sync, setter)]
    pub fn set_flair_text(&mut self, flair_text: Option<String>) {
        self.flair_text = flair_text;
    }

    #[frb(sync, setter)]
    pub fn set_sendreplies(&mut self, sendreplies: bool) {
        self.sendreplies = sendreplies;
    }

    #[frb(sync, setter)]
    pub fn set_url(&mut self, url: Option<String>) {
        self.url = url;
    }

    #[frb(sync)]
    pub fn build(self) -> Result<PostSubmit> {
        if self.title.is_empty() {
            return Err(Error::Custom {
                message: "Missing post title".to_owned(),
            });
        } else if self.text.is_none() && self.url.is_none() {
            return Err(Error::Custom {
                message: "text and url cannot be both empty".to_owned(),
            });
        } else if self.subreddit.is_empty() {
            return Err(Error::Custom {
                message: "Missing community".to_owned(),
            });
        } else {
            return Ok(PostSubmit {
                api_type: "json".to_owned(),
                title: self.title,
                text: self.text,
                subreddit: self.subreddit,
                nsfw: self.nsfw,
                spoiler: self.spoiler,
                kind: self.kind.to_query_param(),
                sendreplies: self.sendreplies,
                flair_id: self.flair_id,
                flair_text: self.flair_text,
                url: self.url,
            });
        }
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize, Setters)]
pub struct PostSubmit {
    api_type: String,
    title: String,
    text: Option<String>,
    #[serde(rename = "sr")]
    subreddit: String,
    nsfw: bool,
    spoiler: bool,
    kind: String,
    #[serde(rename = "sendreplies")]
    sendreplies: bool,
    #[serde(skip_serializing_if = "Option::is_none")]
    flair_id: Option<String>,
    #[serde(skip_serializing_if = "Option::is_none")]
    flair_text: Option<String>,
    #[serde(skip_serializing_if = "Option::is_none")]
    url: Option<String>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize, Setters, FlutterGetters)]
pub struct CrosspostBuilder {
    title: String,
    #[flutter_getter(skip)]
    subreddit: String,
    #[flutter_getter(skip)]
    post_fullname: Fullname,
    nsfw: bool,
    spoiler: bool,
    sendreplies: bool,
    flair_id: Option<String>,
    flair_text: Option<String>,
}

impl CrosspostBuilder {
    #[frb(sync)]
    pub fn new() -> Self {
        Self::default()
    }
    #[frb(sync, setter)]
    pub fn set_title(&mut self, title: String) {
        self.title = title;
    }

    #[frb(sync, setter)]
    pub fn set_subreddit(&mut self, subreddit: &Subreddit) {
        self.subreddit = subreddit.other().display_name().clone();
    }

    #[frb(sync, setter)]
    pub fn set_post_fullname(&mut self, post: &Post) {
        self.post_fullname = post.name.clone();
    }

    #[frb(sync, setter)]
    pub fn set_nsfw(&mut self, nsfw: bool) {
        self.nsfw = nsfw;
    }

    #[frb(sync, setter)]
    pub fn set_spoiler(&mut self, spoiler: bool) {
        self.spoiler = spoiler;
    }

    #[frb(sync, setter)]
    pub fn set_sendreplies(&mut self, sendreplies: bool) {
        self.sendreplies = sendreplies;
    }

    #[frb(sync, setter)]
    pub fn set_flair_id(&mut self, flair_id: Option<String>) {
        self.flair_id = flair_id;
    }

    #[frb(sync, setter)]
    pub fn set_flair_text(&mut self, flair_text: Option<String>) {
        self.flair_text = flair_text;
    }

    #[frb(sync)]
    pub fn build(self) -> Result<Crosspost> {
        if self.title.is_empty() {
            return Err(Error::Custom {
                message: "Missing post title".to_owned(),
            });
        } else if self.subreddit.is_empty() {
            return Err(Error::Custom {
                message: "Missing community".to_owned(),
            });
        } else {
            return Ok(Crosspost {
                api_type: "json".to_owned(),
                title: self.title,
                subreddit: self.subreddit,
                nsfw: self.nsfw,
                spoiler: self.spoiler,
                kind: "crosspost".to_owned(),
                sendreplies: self.sendreplies,
                flair_id: self.flair_id,
                flair_text: self.flair_text,
                post_fullname: self.post_fullname,
            });
        }
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize, Setters)]
pub struct Crosspost {
    api_type: String,
    title: String,
    #[serde(rename = "sr")]
    subreddit: String,
    #[serde(rename = "crosspost_fullname")]
    post_fullname: Fullname,
    nsfw: bool,
    spoiler: bool,
    kind: String,
    #[serde(rename = "sendreplies")]
    sendreplies: bool,
    #[serde(skip_serializing_if = "Option::is_none")]
    flair_id: Option<String>,
    #[serde(skip_serializing_if = "Option::is_none")]
    flair_text: Option<String>,
}
