use serde::{Deserialize, Serialize};

use crate::model::{Fullname, Timeframe};

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
/// flutter_rust_bridge:non_opaque
pub enum UserStreamSort {
    Hot,
    Top(Timeframe),
    New,
    Controversial(Timeframe),
}
impl UserStreamSort {
    pub(crate) fn to_query(self) -> Vec<(&'static str, &'static str)> {
        match self {
            Self::Hot => vec![("sort", "hot")],
            Self::New => vec![("sort", "new")],
            Self::Top(timeframe) => vec![("sort", "top"), ("t", timeframe.as_query_param())],
            Self::Controversial(timeframe) => {
                vec![("sort", "controversial"), ("t", timeframe.as_query_param())]
            }
        }
    }
}

/// Info return by /u/me/about.json
#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct User {
    #[serde(flatten)]
    pub info: UserInfo,

    #[serde(rename = "gold_expiration")]
    pub gold_expiration: Option<String>,
    // #[serde(rename = "has_gold_subscription")]
    // pub has_gold_subscription: bool,
    #[serde(rename = "is_sponsor")]
    pub is_sponsor: bool,
    #[serde(rename = "num_friends")]
    pub num_friends: i64,
    //pub features: Features,
    #[serde(rename = "can_edit_name")]
    pub can_edit_name: bool,
    #[serde(rename = "new_modmail_exists")]
    pub new_modmail_exists: Option<String>,
    pub coins: i64,
    #[serde(rename = "can_create_subreddit")]
    pub can_create_subreddit: bool,
    #[serde(rename = "suspension_expiration_utc")]
    pub suspension_expiration_utc: Option<f32>,
    // #[serde(rename = "has_stripe_subscription")]
    // pub has_stripe_subscription: bool,
    // #[serde(rename = "has_android_subscription")]
    // pub has_android_subscription: bool,
    #[serde(rename = "has_mod_mail")]
    pub has_mod_mail: bool,
    #[serde(rename = "has_mail")]
    pub has_mail: bool,
    // #[serde(rename = "has_paypal_subscription")]
    // pub has_paypal_subscription: bool,
    // #[serde(rename = "has_subscribed_to_premium")]
    // pub has_subscribed_to_premium: bool,
    // #[serde(rename = "in_redesign_beta")]
    // pub in_redesign_beta: bool,
    #[serde(rename = "password_set")]
    pub password_set: bool,
    //  pub modhash: String,
    #[serde(rename = "is_suspended")]
    pub is_suspended: bool,
    #[serde(rename = "force_password_reset")]
    pub force_password_reset: bool,
    #[serde(rename = "inbox_count")]
    pub inbox_count: i64,
    #[serde(rename = "gold_creddits")]
    pub gold_creddits: i64,
    #[serde(rename = "has_ios_subscription")]
    pub has_ios_subscription: bool,
    // #[serde(rename = "in_beta")]
    // pub in_beta: bool,
    // #[serde(rename = "has_visited_new_profile")]
    // pub has_visited_new_profile: bool,
    // #[serde(rename = "has_external_account")]
    // pub has_external_account: bool,
}
impl User {
    pub(crate) fn name(&self) -> crate::model::Fullname {
        Fullname(format!("t2_{}", self.info.id))
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct UserSubreddit {
    #[serde(rename = "default_set")]
    pub default_set: bool,
    #[serde(rename = "user_is_contributor")]
    pub user_is_contributor: bool,
    #[serde(rename = "banner_img")]
    pub banner_img: String,
    #[serde(rename = "allowed_media_in_comments")]
    pub allowed_media_in_comments: Vec<String>,
    #[serde(rename = "user_is_banned")]
    pub user_is_banned: bool,
    #[serde(rename = "free_form_reports")]
    pub free_form_reports: bool,
    #[serde(rename = "community_icon")]
    pub community_icon: Option<String>,
    #[serde(rename = "show_media")]
    pub show_media: bool,
    #[serde(rename = "icon_color")]
    pub icon_color: String,
    #[serde(rename = "user_is_muted")]
    pub user_is_muted: Option<bool>,
    #[serde(rename = "display_name")]
    pub display_name: String,
    #[serde(rename = "header_img")]
    pub header_img: Option<String>,
    pub title: String,
    pub coins: i64,
    #[serde(rename = "previous_names")]
    pub previous_names: Vec<String>,
    #[serde(rename = "over_18")]
    pub over_18: bool,
    #[serde(rename = "icon_size")]
    pub icon_size: Vec<i64>,
    #[serde(rename = "primary_color")]
    pub primary_color: String,
    #[serde(rename = "icon_img")]
    pub icon_img: String,
    pub description: String,
    #[serde(rename = "submit_link_label")]
    pub submit_link_label: String,
    #[serde(rename = "header_size")]
    pub header_size: Option<String>,
    #[serde(rename = "restrict_posting")]
    pub restrict_posting: bool,
    #[serde(rename = "restrict_commenting")]
    pub restrict_commenting: bool,
    pub subscribers: i64,
    #[serde(rename = "submit_text_label")]
    pub submit_text_label: String,
    #[serde(rename = "is_default_icon")]
    pub is_default_icon: bool,
    #[serde(rename = "link_flair_position")]
    pub link_flair_position: String,
    #[serde(rename = "display_name_prefixed")]
    pub display_name_prefixed: String,
    #[serde(rename = "key_color")]
    pub key_color: String,
    #[serde(rename = "is_default_banner")]
    pub is_default_banner: bool,
    pub url: String,
    pub quarantine: bool,
    #[serde(rename = "banner_size")]
    pub banner_size: Option<String>,
    #[serde(rename = "user_is_moderator")]
    pub user_is_moderator: bool,
    #[serde(rename = "public_description")]
    pub public_description: String,
    #[serde(rename = "link_flair_enabled")]
    pub link_flair_enabled: bool,
    #[serde(rename = "disable_contributor_requests")]
    pub disable_contributor_requests: bool,
    #[serde(rename = "subreddit_type")]
    pub subreddit_type: String,
    #[serde(rename = "user_is_subscriber")]
    pub user_is_subscriber: bool,
}

/// Info returned by /u/someuser/about.json
#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct UserInfo {
    pub id: String,
    pub name: String,
    pub subreddit: UserSubreddit,
    pub created: f64,
    #[serde(rename = "over_18")]
    pub over_18: bool,
    #[serde(rename = "created_utc")]
    pub created_utc: f64,
    /// Missing when querying api/v1/me
    #[serde(rename = "is_blocked", default)]
    pub is_blocked: bool,
    #[serde(rename = "is_employee")]
    pub is_employee: bool,
    /// Missing when querying api/v1/me
    #[serde(rename = "is_friend", default)]
    pub is_friend: bool,
    #[serde(rename = "awardee_karma")]
    pub awardee_karma: i64,
    #[serde(rename = "is_gold")]
    pub is_gold: bool,
    #[serde(rename = "is_mod")]
    pub is_mod: bool,
    pub verified: bool,
    #[serde(rename = "awarder_karma")]
    pub awarder_karma: i64,
    #[serde(rename = "has_verified_email")]
    pub has_verified_email: bool,
    #[serde(rename = "has_subscribed")]
    pub has_subscribed: bool,
    #[serde(rename = "icon_img")]
    pub icon_img: String,
    #[serde(rename = "hide_from_robots")]
    pub hide_from_robots: bool,
    #[serde(rename = "link_karma")]
    pub link_karma: i64,
    #[serde(rename = "total_karma")]
    pub total_karma: i64,
    #[serde(rename = "comment_karma")]
    pub comment_karma: i64,
    #[serde(rename = "accept_followers")]
    pub accept_followers: bool,
    #[serde(rename = "accept_chats", default)]
    pub accept_chats: bool,
    #[serde(rename = "accept_pms", default)]
    pub accept_pms: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Snoovatar {
    #[serde(rename = "pref_show_snoovatar")]
    pub pref_show_snoovatar: bool,
    #[serde(rename = "snoovatar_size")]
    // TODO find what the type is
    pub snoovatar_size: Option<String>,
    #[serde(rename = "snoovatar_img")]
    pub snoovatar_img: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Preferences {
    #[serde(rename = "pref_autoplay", default)]
    pub pref_autoplay: bool,
    #[serde(rename = "pref_video_autoplay", default)]
    pub pref_video_autoplay: bool,
    #[serde(rename = "pref_no_profanity", default)]
    pub pref_no_profanity: bool,
    #[serde(rename = "pref_geopopular", default)]
    pub pref_geopopular: String,
    #[serde(rename = "pref_show_trending", default)]
    pub pref_show_trending: bool,
    #[serde(rename = "pref_show_presence", default)]
    pub pref_show_presence: bool,
    #[serde(rename = "pref_nightmode", default)]
    pub pref_nightmode: bool,
    #[serde(rename = "pref_top_karma_subreddits", default)]
    pub pref_top_karma_subreddits: bool,
    #[serde(rename = "pref_clickgadget", default)]
    pub pref_clickgadget: i64,
    #[serde(rename = "pref_show_twitter", default)]
    pub pref_show_twitter: bool,
}
