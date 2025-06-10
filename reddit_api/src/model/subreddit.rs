use std::backtrace::Backtrace;
use std::borrow::Cow;
use std::collections::HashMap;

use crate::utils::{response_or_default, response_or_none_string};
use crate::{error::Error, model::flair::Flair};
use log::debug;
use reqwest::Url;
use serde::{Deserialize, Serialize};
use serde_with::serde_as;
use serde_with::with_prefix;

use super::Thing;

with_prefix!(prefix_user_flair "user_flair_");

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
#[serde_as]
pub struct Subreddit {
    #[serde(flatten, with = "prefix_user_flair")]
    pub user_flair: Flair,
    #[serde(rename = "submit_text_html")]
    pub submit_text_html: Option<String>,
    #[serde(rename = "restrict_posting")]
    pub restrict_posting: bool,
    #[serde(rename = "user_is_banned")]
    pub user_is_banned: bool,
    #[serde(rename = "free_form_reports")]
    pub free_form_reports: bool,
    #[serde(rename = "wiki_enabled")]
    pub wiki_enabled: Option<bool>,
    #[serde(rename = "user_is_muted")]
    pub user_is_muted: bool,
    #[serde(rename = "user_can_flair_in_sr")]
    pub user_can_flair_in_sr: Option<bool>,
    #[serde(rename = "display_name")]
    pub display_name: String,
    #[serde(rename = "header_img")]
    pub header_img: Option<String>,
    pub title: String,
    #[serde(rename = "allow_galleries")]
    pub allow_galleries: bool,

    #[serde(rename = "icon_size")]
    icon_size: Option<[usize; 2]>,
    #[serde(rename = "icon_img", deserialize_with = "response_or_none_string")]
    icon_img: Option<String>,

    #[serde(rename = "primary_color")]
    pub primary_color: String,
    #[serde(rename = "active_user_count", deserialize_with = "response_or_default")]
    pub active_user_count: u64,
    #[serde(rename = "display_name_prefixed")]
    pub display_name_prefixed: String,
    #[serde(rename = "accounts_active", deserialize_with = "response_or_default")]
    pub accounts_active: Option<u64>,
    #[serde(rename = "public_traffic")]
    pub public_traffic: bool,
    pub subscribers: i64,
    pub name: String,
    pub quarantine: bool,
    #[serde(rename = "hide_ads")]
    pub hide_ads: bool,
    #[serde(rename = "prediction_leaderboard_entry_type")]
    pub prediction_leaderboard_entry_type: i64,
    #[serde(rename = "emojis_enabled")]
    pub emojis_enabled: bool,
    #[serde(rename = "advertiser_category")]
    pub advertiser_category: String,
    #[serde(rename = "public_description")]
    pub public_description: String,
    #[serde(rename = "comment_score_hide_mins")]
    pub comment_score_hide_mins: i64,
    #[serde(rename = "allow_predictions")]
    pub allow_predictions: bool,
    #[serde(rename = "user_has_favorited")]
    pub user_has_favorited: bool,
    #[serde(
        rename = "community_icon",
        deserialize_with = "response_or_none_string"
    )]
    community_icon: Option<String>,
    #[serde(rename = "banner_background_image")]
    pub banner_background_image: String,
    #[serde(rename = "original_content_tag_enabled")]
    pub original_content_tag_enabled: bool,
    #[serde(rename = "community_reviewed")]
    pub community_reviewed: bool,
    #[serde(rename = "submit_text")]
    pub submit_text: String,
    #[serde(rename = "description_html")]
    pub description_html: Option<String>,
    #[serde(rename = "spoilers_enabled")]
    pub spoilers_enabled: bool,
    #[serde(rename = "comment_contribution_settings")]
    pub comment_contribution_settings: CommentContributionSettings,
    #[serde(rename = "allow_talks")]
    pub allow_talks: bool,
    #[serde(rename = "header_size")]
    pub header_size: Option<Vec<i64>>,
    #[serde(rename = "all_original_content")]
    pub all_original_content: bool,
    #[serde(rename = "has_menu_widget")]
    pub has_menu_widget: bool,
    #[serde(rename = "is_enrolled_in_new_modmail")]
    pub is_enrolled_in_new_modmail: Option<bool>,
    #[serde(rename = "key_color")]
    pub key_color: String,
    #[serde(rename = "can_assign_user_flair")]
    pub can_assign_user_flair: bool,
    pub created: f64,
    pub wls: Option<u64>,
    #[serde(rename = "show_media_preview")]
    pub show_media_preview: bool,
    #[serde(rename = "submission_type")]
    pub submission_type: String,
    #[serde(rename = "user_is_subscriber")]
    pub user_is_subscriber: bool,
    #[serde(rename = "allowed_media_in_comments", default)]
    pub allowed_media_in_comments: Vec<String>,
    #[serde(rename = "allow_videogifs")]
    pub allow_videogifs: bool,
    #[serde(rename = "should_archive_posts")]
    pub should_archive_posts: bool,
    #[serde(rename = "allow_polls")]
    pub allow_polls: bool,
    #[serde(rename = "collapse_deleted_comments")]
    pub collapse_deleted_comments: bool,
    #[serde(rename = "emojis_custom_size")]
    pub emojis_custom_size: Option<Vec<i64>>,
    #[serde(rename = "public_description_html")]
    pub public_description_html: Option<String>,
    #[serde(rename = "allow_videos")]
    pub allow_videos: bool,
    #[serde(rename = "is_crosspostable_subreddit")]
    pub is_crosspostable_subreddit: Option<bool>,
    #[serde(rename = "notification_level", default)]
    pub notification_level: Option<String>,
    #[serde(rename = "should_show_media_in_comments_setting")]
    pub should_show_media_in_comments_setting: bool,
    #[serde(rename = "can_assign_link_flair")]
    pub can_assign_link_flair: bool,
    #[serde(rename = "accounts_active_is_fuzzed")]
    pub accounts_active_is_fuzzed: bool,
    #[serde(rename = "allow_prediction_contributors")]
    pub allow_prediction_contributors: bool,
    #[serde(rename = "submit_text_label")]
    pub submit_text_label: String,
    #[serde(rename = "link_flair_position")]
    pub link_flair_position: String,
    #[serde(rename = "user_sr_flair_enabled")]
    pub user_sr_flair_enabled: Option<bool>,
    #[serde(rename = "user_flair_enabled_in_sr")]
    pub user_flair_enabled_in_sr: bool,
    #[serde(rename = "allow_discovery")]
    pub allow_discovery: bool,
    #[serde(rename = "accept_followers")]
    pub accept_followers: bool,
    #[serde(rename = "user_sr_theme_enabled")]
    pub user_sr_theme_enabled: bool,
    #[serde(rename = "link_flair_enabled")]
    pub link_flair_enabled: bool,
    #[serde(rename = "disable_contributor_requests")]
    pub disable_contributor_requests: bool,
    #[serde(rename = "subreddit_type")]
    pub subreddit_type: String,
    #[serde(rename = "suggested_comment_sort")]
    pub suggested_comment_sort: Option<String>,
    #[serde(rename = "banner_img")]
    pub banner_img: String,
    #[serde(rename = "banner_background_color")]
    pub banner_background_color: String,
    #[serde(rename = "show_media")]
    pub show_media: bool,
    pub id: String,
    #[serde(rename = "user_is_moderator")]
    pub user_is_moderator: bool,
    pub over18: bool,
    #[serde(rename = "header_title")]
    pub header_title: String,
    pub description: String,
    #[serde(rename = "submit_link_label")]
    pub submit_link_label: String,
    #[serde(rename = "restrict_commenting")]
    pub restrict_commenting: bool,
    #[serde(rename = "allow_images")]
    pub allow_images: bool,
    pub lang: String,
    pub url: String,
    #[serde(rename = "created_utc")]
    pub created_utc: f64,
    #[serde(rename = "banner_size")]
    pub banner_size: Option<Vec<i64>>,
    #[serde(rename = "mobile_banner_image")]
    pub mobile_banner_image: String,
    #[serde(rename = "user_is_contributor")]
    pub user_is_contributor: bool,
    #[serde(rename = "allow_predictions_tournament")]
    pub allow_predictions_tournament: bool,
}

pub struct Icon {
    pub url: String,
    pub width: usize,
    pub height: usize,
}

impl Subreddit {
    #[must_use]
    /// flutter_rust_bridge:getter,sync
    pub fn icon(&self) -> Option<Icon> {
        if let Some(url) = &self.icon_img {
            assert!(self.icon_size.is_some());
            Some(Icon {
                url: url.to_owned(),
                width: self.icon_size?[0],
                height: self.icon_size?[1],
            })
        } else if let Some(url) = &self.community_icon {
            let url = Url::parse(url).ok()?;
            let params: HashMap<Cow<str>, Cow<str>> = url.query_pairs().collect();
            let width: usize = params.get("width")?.parse().ok()?;
            let height: usize = width;

            Some(Icon {
                url: url.to_string(),
                width,
                height,
            })
        } else {
            None
        }
    }
}

impl TryFrom<Thing> for Subreddit {
    type Error = Error;

    fn try_from(value: Thing) -> Result<Self, Self::Error> {
        match value {
            Thing::Subreddit(subreddit) => Ok(subreddit),
            _ => Err(Error::InvalidThing {
                backtrace: Backtrace::capture(),
            }),
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
