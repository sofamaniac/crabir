use std::borrow::Cow;
use std::collections::HashMap;

use crate::utils;
use crate::utils::{response_or_default, response_or_none_string};
use crate::{error::Error, model::flair::Flair};
use flutter_rust_bridge::frb;
use reqwest::Url;
use serde::{Deserialize, Serialize};
use serde_with::serde_as;
use serde_with::with_prefix;

use super::{Fullname, Thing};

with_prefix!(prefix_user_flair "user_flair_");

/// Subreddit's ID, is of the form `t5_xxx`
#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct SubredditID(String);

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct SubredditInfo {
    /// The subreddit's name (e.g. "awww")
    #[serde(rename = "subreddit")]
    pub subreddit: String,
    #[serde(rename = "subreddit_id")]
    pub subreddit_id: SubredditID,
    /// The subreddit's name prefixed with "r/"
    #[serde(rename = "subreddit_name_prefixed")]
    pub subreddit_name_prefixed: String,
    /// The number of subscribers of the subreddit
    #[serde(rename = "subreddit_subscribers")]
    pub subscribers: u32,

    #[serde(rename = "sr_detail")]
    pub details: Option<Details>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Common {
    #[serde(default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub user_is_contributor: bool,
    #[serde(rename = "banner_img", deserialize_with = "response_or_none_string")]
    pub banner_img: Option<String>,
    #[serde(rename = "allowed_media_in_comments", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub allowed_media_in_comments: Vec<String>,
    #[serde(
        rename = "community_icon",
        deserialize_with = "response_or_none_string"
    )]
    community_icon: Option<String>,
    #[serde(rename = "user_is_banned", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub user_is_banned: bool,
    #[serde(rename = "user_is_subscriber", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub user_is_subscriber: bool,
    #[serde(rename = "free_form_reports", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub free_form_reports: bool,
    #[serde(rename = "display_name")]
    pub display_name: String,
    #[serde(rename = "header_img")]
    pub header_img: Option<String>,
    #[serde(rename = "title", deserialize_with = "response_or_none_string")]
    pub title: Option<String>,
    #[serde(rename = "icon_size", deserialize_with = "response_or_default")]
    icon_size: [usize; 2],
    #[serde(rename = "icon_img", deserialize_with = "response_or_none_string")]
    icon_img: Option<String>,
    #[serde(rename = "primary_color", deserialize_with = "response_or_none_string")]
    pub primary_color: Option<String>,

    #[serde(rename = "subscribers", deserialize_with = "response_or_default")]
    pub subscribers: i64,
    pub name: Fullname,
    #[serde(default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub quarantine: bool,

    #[serde(rename = "display_name_prefixed")]
    pub display_name_prefixed: String,

    #[serde(rename = "user_is_muted", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub user_is_muted: bool,

    #[serde(rename = "restrict_posting", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub restrict_posting: bool,

    #[serde(rename = "key_color", deserialize_with = "response_or_none_string")]
    pub key_color: Option<String>,

    #[serde(default)]
    // missing on user subreddit
    pub created: f64,

    #[serde(
        rename = "submit_text_label",
        deserialize_with = "response_or_none_string"
    )]
    pub submit_text_label: Option<String>,

    #[serde(rename = "show_media", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub show_media: bool,

    #[serde(rename = "public_description")]
    pub public_description: Option<String>,

    #[serde(rename = "banner_size")]
    pub banner_size: Option<Vec<i64>>,

    #[serde(rename = "created_utc", default)]
    // missing on user subreddit
    pub created_utc: f64,

    pub url: String,

    #[serde(rename = "restrict_commenting", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub restrict_commenting: bool,

    #[serde(rename = "submit_link_label")]
    pub submit_link_label: Option<String>,

    #[serde(rename = "link_flair_enabled", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub link_flair_enabled: bool,
    #[serde(rename = "link_flair_position")]
    pub link_flair_position: Option<String>,

    #[serde(rename = "header_size")]
    pub header_size: Option<Vec<i64>>,

    #[serde(rename = "accept_followers", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub accept_followers: bool,

    #[serde(rename = "disable_contributor_requests", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub disable_contributor_requests: bool,

    #[serde(rename = "subreddit_type")]
    pub subreddit_type: Option<String>,

    #[serde(rename = "user_is_moderator", default)]
    #[serde(deserialize_with = "utils::response_or_default")]
    pub user_is_moderator: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Details {
    #[serde(default, deserialize_with = "utils::response_or_default")]
    pub default_set: bool,
    #[serde(default, deserialize_with = "utils::response_or_none_string")]
    pub icon_color: Option<String>,
    pub previous_names: Vec<String>,
    #[serde(default, deserialize_with = "utils::response_or_default")]
    pub over_18: bool,
    pub description: Option<String>,
    #[serde(flatten)]
    pub other: Common,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
#[serde_as]
pub struct Subreddit {
    #[serde(flatten)]
    pub other: Common,

    #[serde(flatten, with = "prefix_user_flair")]
    pub user_flair: Flair,
    #[serde(rename = "submit_text_html")]
    pub submit_text_html: Option<String>,
    #[serde(rename = "wiki_enabled")]
    pub wiki_enabled: Option<bool>,
    #[serde(rename = "user_can_flair_in_sr")]
    pub user_can_flair_in_sr: Option<bool>,
    #[serde(rename = "allow_galleries", deserialize_with = "response_or_default")]
    pub allow_galleries: bool,

    #[serde(rename = "active_user_count", deserialize_with = "response_or_default")]
    pub active_user_count: u32,
    #[serde(rename = "accounts_active", deserialize_with = "response_or_default")]
    pub accounts_active: Option<u32>,
    #[serde(rename = "public_traffic", deserialize_with = "response_or_default")]
    pub public_traffic: bool,
    #[serde(rename = "hide_ads", deserialize_with = "response_or_default")]
    pub hide_ads: bool,
    #[serde(
        rename = "prediction_leaderboard_entry_type",
        deserialize_with = "response_or_default"
    )]
    pub prediction_leaderboard_entry_type: i64,
    #[serde(rename = "emojis_enabled")]
    pub emojis_enabled: bool,
    #[serde(rename = "advertiser_category")]
    pub advertiser_category: Option<String>,
    #[serde(rename = "comment_score_hide_mins")]
    pub comment_score_hide_mins: Option<i64>,
    #[serde(rename = "allow_predictions", deserialize_with = "response_or_default")]
    pub allow_predictions: bool,
    #[serde(
        rename = "user_has_favorited",
        deserialize_with = "response_or_default"
    )]
    pub user_has_favorited: bool,
    #[serde(rename = "banner_background_image")]
    pub banner_background_image: Option<String>,
    #[serde(
        rename = "original_content_tag_enabled",
        deserialize_with = "response_or_default"
    )]
    pub original_content_tag_enabled: bool,
    #[serde(
        rename = "community_reviewed",
        deserialize_with = "response_or_default"
    )]
    pub community_reviewed: bool,
    #[serde(rename = "submit_text")]
    pub submit_text: Option<String>,
    #[serde(rename = "description_html")]
    pub description_html: Option<String>,
    #[serde(rename = "spoilers_enabled", deserialize_with = "response_or_default")]
    pub spoilers_enabled: bool,
    #[serde(rename = "comment_contribution_settings")]
    pub comment_contribution_settings: CommentContributionSettings,
    #[serde(rename = "allow_talks", deserialize_with = "response_or_default")]
    pub allow_talks: bool,
    #[serde(
        rename = "all_original_content",
        deserialize_with = "response_or_default"
    )]
    pub all_original_content: bool,
    #[serde(rename = "has_menu_widget", deserialize_with = "response_or_default")]
    pub has_menu_widget: bool,
    #[serde(rename = "is_enrolled_in_new_modmail")]
    pub is_enrolled_in_new_modmail: Option<bool>,
    #[serde(
        rename = "can_assign_user_flair",
        deserialize_with = "response_or_default"
    )]
    pub can_assign_user_flair: bool,
    pub wls: Option<u32>,
    #[serde(
        rename = "show_media_preview",
        deserialize_with = "response_or_default"
    )]
    pub show_media_preview: bool,
    #[serde(
        rename = "submission_type",
        deserialize_with = "response_or_none_string"
    )]
    pub submission_type: Option<String>,
    #[serde(rename = "allow_videogifs", deserialize_with = "response_or_default")]
    pub allow_videogifs: bool,
    #[serde(
        rename = "should_archive_posts",
        deserialize_with = "response_or_default"
    )]
    pub should_archive_posts: bool,
    #[serde(rename = "allow_polls", deserialize_with = "response_or_default")]
    pub allow_polls: bool,
    #[serde(
        rename = "collapse_deleted_comments",
        deserialize_with = "response_or_default"
    )]
    pub collapse_deleted_comments: bool,
    #[serde(rename = "emojis_custom_size")]
    pub emojis_custom_size: Option<Vec<i64>>,
    #[serde(rename = "public_description_html")]
    pub public_description_html: Option<String>,
    #[serde(rename = "allow_videos", deserialize_with = "response_or_default")]
    pub allow_videos: bool,
    #[serde(rename = "is_crosspostable_subreddit")]
    pub is_crosspostable_subreddit: Option<bool>,
    #[serde(rename = "notification_level", default)]
    pub notification_level: Option<String>,
    #[serde(
        rename = "should_show_media_in_comments_setting",
        deserialize_with = "response_or_default"
    )]
    pub should_show_media_in_comments_setting: bool,
    #[serde(
        rename = "can_assign_link_flair",
        deserialize_with = "response_or_default"
    )]
    pub can_assign_link_flair: bool,
    #[serde(
        rename = "accounts_active_is_fuzzed",
        deserialize_with = "response_or_default"
    )]
    pub accounts_active_is_fuzzed: bool,
    #[serde(
        rename = "allow_prediction_contributors",
        deserialize_with = "response_or_default"
    )]
    pub allow_prediction_contributors: bool,
    #[serde(rename = "user_sr_flair_enabled")]
    pub user_sr_flair_enabled: Option<bool>,
    #[serde(
        rename = "user_flair_enabled_in_sr",
        deserialize_with = "response_or_default"
    )]
    pub user_flair_enabled_in_sr: bool,
    #[serde(rename = "allow_discovery", deserialize_with = "response_or_default")]
    pub allow_discovery: bool,
    #[serde(
        rename = "user_sr_theme_enabled",
        deserialize_with = "response_or_default"
    )]
    pub user_sr_theme_enabled: bool,
    #[serde(rename = "suggested_comment_sort")]
    pub suggested_comment_sort: Option<String>,
    #[serde(
        rename = "banner_background_color",
        deserialize_with = "response_or_none_string"
    )]
    pub banner_background_color: Option<String>,
    pub id: String,
    #[serde(default, deserialize_with = "utils::response_or_default")]
    pub over18: bool,
    #[serde(rename = "header_title", deserialize_with = "response_or_none_string")]
    pub header_title: Option<String>,
    #[serde(rename = "description", deserialize_with = "response_or_none_string")]
    pub description: Option<String>,
    #[serde(rename = "allow_images", deserialize_with = "response_or_default")]
    pub allow_images: bool,
    #[serde(rename = "lang", deserialize_with = "response_or_none_string")]
    pub lang: Option<String>,
    #[serde(
        rename = "mobile_banner_image",
        deserialize_with = "response_or_none_string"
    )]
    pub mobile_banner_image: Option<String>,
    // #[serde(rename = "user_is_contributor")]
    // pub user_is_contributor: bool,
    #[serde(
        rename = "allow_predictions_tournament",
        deserialize_with = "response_or_default"
    )]
    pub allow_predictions_tournament: bool,
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
        if let Some(url) = &self.icon_img {
            SubredditIcon::Image(Icon {
                url: url.to_owned(),
                width: self.icon_size[0],
                height: self.icon_size[1],
            })
        } else if let Some(url) = &self.community_icon {
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
        } else {
            let color = self
                .key_color
                .clone()
                .unwrap_or(self.primary_color.clone().unwrap_or("black".to_owned()));
            SubredditIcon::Color(color)
        }
    }
}

impl Subreddit {
    #[must_use]
    #[frb(sync, getter)]
    pub fn icon(&self) -> SubredditIcon {
        self.other.icon()
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

    fn try_from(value: Thing) -> Result<Self, Self::Error> {
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
