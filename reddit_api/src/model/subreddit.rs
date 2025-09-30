use std::borrow::Cow;
use std::collections::HashMap;

use crate::client::Client;
use crate::result::Result;
use crate::utils;
use crate::utils::{response_or_default, response_or_none_string};
use crate::{error::Error, model::flair::Flair};
use flutter_getter::FlutterGetters;
use flutter_rust_bridge::frb;
use getset::Getters;
use reqwest::Url;
use serde::{Deserialize, Serialize};
use serde_with::serde_as;
use serde_with::with_prefix;

use super::{Fullname, Thing};

with_prefix!(prefix_user_flair "user_flair_");

/// Subreddit's ID, is of the form `t5_xxx`
#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct SubredditID(String);

#[derive(Default, Debug, Clone, PartialEq, Getters, FlutterGetters, Serialize, Deserialize)]
#[getset(get = "pub(crate)")]
pub struct SubredditInfo {
    /// The subreddit's name (e.g. "awww")
    #[serde(rename = "subreddit")]
    subreddit: String,
    #[serde(rename = "subreddit_id")]
    subreddit_id: SubredditID,
    /// The subreddit's name prefixed with "r/"
    #[serde(rename = "subreddit_name_prefixed")]
    subreddit_name_prefixed: String,
    /// The number of subscribers of the subreddit
    #[serde(rename = "subreddit_subscribers")]
    subscribers: u32,

    #[serde(rename = "sr_detail")]
    details: Option<Details>,
}

#[derive(Default, Debug, Clone, PartialEq, Getters, FlutterGetters, Serialize, Deserialize)]
#[getset(get = "pub(crate)")]
pub struct Common {
    #[serde(default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    user_is_contributor: bool,
    #[serde(rename = "banner_img", deserialize_with = "response_or_none_string")]
    banner_img: Option<String>,
    #[serde(rename = "allowed_media_in_comments", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    allowed_media_in_comments: Vec<String>,
    #[serde(
        rename = "community_icon",
        deserialize_with = "response_or_none_string"
    )]
    #[getset(skip)]
    #[flutter_getter(skip)]
    community_icon: Option<String>,
    #[serde(rename = "user_is_banned", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    user_is_banned: bool,
    #[serde(rename = "user_is_subscriber", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    user_is_subscriber: bool,
    #[serde(rename = "free_form_reports", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    free_form_reports: bool,
    #[serde(rename = "display_name")]
    display_name: String,
    #[serde(rename = "header_img")]
    header_img: Option<String>,
    #[serde(rename = "title", deserialize_with = "response_or_none_string")]
    title: Option<String>,
    #[serde(rename = "icon_size", deserialize_with = "response_or_default")]
    #[getset(skip)]
    #[flutter_getter(skip)]
    icon_size: [usize; 2],
    #[serde(rename = "icon_img", deserialize_with = "response_or_none_string")]
    #[getset(skip)]
    #[flutter_getter(skip)]
    icon_img: Option<String>,
    #[serde(rename = "primary_color", deserialize_with = "response_or_none_string")]
    primary_color: Option<String>,

    #[serde(rename = "subscribers", deserialize_with = "response_or_default")]
    subscribers: i64,
    name: Fullname,
    #[serde(default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    quarantine: bool,

    #[serde(rename = "display_name_prefixed")]
    display_name_prefixed: String,

    #[serde(rename = "user_is_muted", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    user_is_muted: bool,

    #[serde(rename = "restrict_posting", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    restrict_posting: bool,

    #[serde(rename = "key_color", deserialize_with = "response_or_none_string")]
    key_color: Option<String>,

    #[serde(default)]
    // missing on user subreddit
    created: f64,

    #[serde(
        rename = "submit_text_label",
        deserialize_with = "response_or_none_string"
    )]
    submit_text_label: Option<String>,

    #[serde(rename = "show_media", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    show_media: bool,

    #[serde(rename = "public_description")]
    public_description: Option<String>,

    #[serde(rename = "banner_size")]
    banner_size: Option<Vec<i64>>,

    #[serde(rename = "created_utc", default)]
    // missing on user subreddit
    created_utc: f64,

    url: String,

    #[serde(rename = "restrict_commenting", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    restrict_commenting: bool,

    #[serde(rename = "submit_link_label")]
    submit_link_label: Option<String>,

    #[serde(rename = "link_flair_enabled", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    link_flair_enabled: bool,
    #[serde(rename = "link_flair_position")]
    link_flair_position: Option<String>,

    #[serde(rename = "header_size")]
    header_size: Option<Vec<i64>>,

    #[serde(rename = "accept_followers", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    accept_followers: bool,

    #[serde(rename = "disable_contributor_requests", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    disable_contributor_requests: bool,

    #[serde(rename = "subreddit_type")]
    subreddit_type: Option<String>,

    #[serde(rename = "user_is_moderator", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    user_is_moderator: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Getters, FlutterGetters, Serialize, Deserialize)]
#[getset(get = "pub(crate)")]
pub struct Details {
    #[serde(default, deserialize_with = "utils::response_or_default")]
    default_set: bool,
    #[serde(default, deserialize_with = "utils::response_or_none_string")]
    icon_color: Option<String>,
    previous_names: Vec<String>,
    #[serde(default, deserialize_with = "utils::response_or_default")]
    over_18: bool,
    description: Option<String>,
    #[serde(flatten)]
    other: Common,
}

#[derive(Default, Debug, Clone, Getters, FlutterGetters, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
#[serde_as]
#[getset(get = "pub(crate)")]
pub struct Subreddit {
    #[serde(flatten)]
    other: Common,

    #[serde(flatten, with = "prefix_user_flair")]
    user_flair: Flair,
    #[serde(rename = "submit_text_html")]
    submit_text_html: Option<String>,
    #[serde(rename = "wiki_enabled")]
    wiki_enabled: Option<bool>,
    #[serde(rename = "user_can_flair_in_sr")]
    user_can_flair_in_sr: Option<bool>,
    #[serde(rename = "allow_galleries", deserialize_with = "response_or_default")]
    allow_galleries: bool,

    #[serde(
        rename = "active_user_count",
        deserialize_with = "response_or_default",
        default
    )]
    #[deprecated(note = "Reddit seems to have remove it from it responses")]
    active_user_count: u32,
    #[serde(
        rename = "accounts_active",
        deserialize_with = "response_or_default",
        default
    )]
    #[deprecated(note = "Reddit seems to have remove it from it responses")]
    accounts_active: u32,
    #[serde(rename = "public_traffic", deserialize_with = "response_or_default")]
    public_traffic: bool,
    #[serde(rename = "hide_ads", deserialize_with = "response_or_default")]
    hide_ads: bool,
    #[serde(
        rename = "prediction_leaderboard_entry_type",
        deserialize_with = "response_or_default"
    )]
    prediction_leaderboard_entry_type: i64,
    #[serde(rename = "emojis_enabled")]
    emojis_enabled: bool,
    #[serde(rename = "advertiser_category")]
    advertiser_category: Option<String>,
    #[serde(rename = "comment_score_hide_mins")]
    comment_score_hide_mins: Option<i64>,
    #[serde(rename = "allow_predictions", deserialize_with = "response_or_default")]
    allow_predictions: bool,
    #[serde(
        rename = "user_has_favorited",
        deserialize_with = "response_or_default"
    )]
    user_has_favorited: bool,
    #[serde(rename = "banner_background_image")]
    banner_background_image: Option<String>,
    #[serde(
        rename = "original_content_tag_enabled",
        deserialize_with = "response_or_default"
    )]
    original_content_tag_enabled: bool,
    #[serde(
        rename = "community_reviewed",
        deserialize_with = "response_or_default"
    )]
    community_reviewed: bool,
    #[serde(rename = "submit_text")]
    submit_text: Option<String>,
    #[serde(rename = "description_html")]
    description_html: Option<String>,
    #[serde(rename = "spoilers_enabled", deserialize_with = "response_or_default")]
    spoilers_enabled: bool,
    #[serde(rename = "comment_contribution_settings")]
    comment_contribution_settings: CommentContributionSettings,
    #[serde(rename = "allow_talks", deserialize_with = "response_or_default")]
    allow_talks: bool,
    #[serde(
        rename = "all_original_content",
        deserialize_with = "response_or_default"
    )]
    all_original_content: bool,
    #[serde(rename = "has_menu_widget", deserialize_with = "response_or_default")]
    has_menu_widget: bool,
    #[serde(rename = "is_enrolled_in_new_modmail")]
    is_enrolled_in_new_modmail: Option<bool>,
    #[serde(
        rename = "can_assign_user_flair",
        deserialize_with = "response_or_default"
    )]
    can_assign_user_flair: bool,
    wls: Option<u32>,
    #[serde(
        rename = "show_media_preview",
        deserialize_with = "response_or_default"
    )]
    show_media_preview: bool,
    #[serde(
        rename = "submission_type",
        deserialize_with = "response_or_none_string"
    )]
    submission_type: Option<String>,
    #[serde(rename = "allow_videogifs", deserialize_with = "response_or_default")]
    allow_videogifs: bool,
    #[serde(
        rename = "should_archive_posts",
        deserialize_with = "response_or_default"
    )]
    should_archive_posts: bool,
    #[serde(rename = "allow_polls", deserialize_with = "response_or_default")]
    allow_polls: bool,
    #[serde(
        rename = "collapse_deleted_comments",
        deserialize_with = "response_or_default"
    )]
    collapse_deleted_comments: bool,
    #[serde(rename = "emojis_custom_size")]
    emojis_custom_size: Option<Vec<i64>>,
    #[serde(rename = "public_description_html")]
    public_description_html: Option<String>,
    #[serde(rename = "allow_videos", deserialize_with = "response_or_default")]
    allow_videos: bool,
    #[serde(rename = "is_crosspostable_subreddit")]
    is_crosspostable_subreddit: Option<bool>,
    #[serde(rename = "notification_level", default)]
    notification_level: Option<String>,
    #[serde(
        rename = "should_show_media_in_comments_setting",
        deserialize_with = "response_or_default"
    )]
    should_show_media_in_comments_setting: bool,
    #[serde(
        rename = "can_assign_link_flair",
        deserialize_with = "response_or_default"
    )]
    can_assign_link_flair: bool,
    #[serde(
        rename = "allow_prediction_contributors",
        deserialize_with = "response_or_default"
    )]
    allow_prediction_contributors: bool,
    #[serde(rename = "user_sr_flair_enabled")]
    user_sr_flair_enabled: Option<bool>,
    #[serde(
        rename = "user_flair_enabled_in_sr",
        deserialize_with = "response_or_default"
    )]
    user_flair_enabled_in_sr: bool,
    #[serde(rename = "allow_discovery", deserialize_with = "response_or_default")]
    allow_discovery: bool,
    #[serde(
        rename = "user_sr_theme_enabled",
        deserialize_with = "response_or_default"
    )]
    user_sr_theme_enabled: bool,
    #[serde(rename = "suggested_comment_sort")]
    suggested_comment_sort: Option<String>,
    #[serde(
        rename = "banner_background_color",
        deserialize_with = "response_or_none_string"
    )]
    banner_background_color: Option<String>,
    id: String,
    #[serde(default, deserialize_with = "utils::response_or_default")]
    over18: bool,
    #[serde(rename = "header_title", deserialize_with = "response_or_none_string")]
    header_title: Option<String>,
    #[serde(rename = "description", deserialize_with = "response_or_none_string")]
    description: Option<String>,
    #[serde(rename = "allow_images", deserialize_with = "response_or_default")]
    allow_images: bool,
    #[serde(rename = "lang", deserialize_with = "response_or_none_string")]
    lang: Option<String>,
    #[serde(
        rename = "mobile_banner_image",
        deserialize_with = "response_or_none_string"
    )]
    mobile_banner_image: Option<String>,
    // #[serde(rename = "user_is_contributor")]
    // pub user_is_contributor: bool,
    #[serde(
        rename = "allow_predictions_tournament",
        deserialize_with = "response_or_default"
    )]
    allow_predictions_tournament: bool,
}

impl Subreddit {
    #[must_use]
    #[frb(sync, getter)]
    pub fn icon(&self) -> SubredditIcon {
        self.other.icon()
    }
    pub async fn subscribe(&mut self, client: &Client) -> Result<()> {
        client.subscribe(self.other.name()).await?;
        self.other.user_is_subscriber = true;
        Ok(())
    }
    pub async fn unsubscribe(&mut self, client: &Client) -> Result<()> {
        client.unsubscribe(self.other.name()).await?;
        self.other.user_is_subscriber = false;
        Ok(())
    }
    pub async fn favorite(&mut self, favorite: bool, client: &Client) -> Result<()> {
        client.favorite(self.other.display_name(), favorite).await?;
        self.user_has_favorited = favorite;
        Ok(())
    }
}

#[frb(non_opaque)]
pub enum SubredditIcon {
    Image(Icon),
    Color(String),
}

pub struct Icon {
    pub url: String,
    pub width: usize,
    pub height: usize,
}

impl Common {
    #[must_use]
    #[frb(sync, getter)]
    pub fn icon(&self) -> SubredditIcon {
        if let Some(url) = &self.community_icon {
            let url = Url::parse(url).expect("Could not parse community icon url");
            let params: HashMap<Cow<str>, Cow<str>> = url.query_pairs().collect();
            let width: usize = params
                .get("width")
                .expect("Width not found in community icon url")
                .parse()
                .expect("Could not parse width in community icon url");
            let height: usize = width;

            SubredditIcon::Image(Icon {
                url: url.to_string(),
                width,
                height,
            })
        } else if let Some(url) = &self.icon_img {
            SubredditIcon::Image(Icon {
                url: url.to_owned(),
                width: self.icon_size[0],
                height: self.icon_size[1],
            })
        } else {
            let color = self
                .key_color
                .clone()
                .unwrap_or(self.primary_color.clone().unwrap_or("black".to_owned()));
            SubredditIcon::Color(color)
        }
    }
}

impl Details {
    #[must_use]
    #[frb(sync, getter)]
    pub fn icon(&self) -> SubredditIcon {
        self.other.icon()
    }
}

impl TryFrom<Thing> for Subreddit {
    type Error = Error;

    fn try_from(value: Thing) -> Result<Self> {
        match value {
            Thing::Subreddit(subreddit) => Ok(subreddit),
            _ => Err(Error::InvalidThing),
        }
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct CommentContributionSettings {
    #[serde(
        rename = "allowed_media_types",
        deserialize_with = "response_or_default",
        default
    )]
    pub allowed_media_types: Vec<String>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub enum NotificationLevel {
    #[default]
    Unknown,
    Low,
}
