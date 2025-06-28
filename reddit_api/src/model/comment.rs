use serde::Deserialize;
use serde::Serialize;

use super::Fullname;
use super::Listing;
use super::Thing;
use super::author;
use super::author::AuthorInfo;
use crate::utils;

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Comment {
    // SUBREDDIT
    pub subreddit_id: String,
    pub subreddit_name_prefixed: String,
    pub subreddit_type: String,
    pub subreddit: String,
    #[serde(
        flatten,
        serialize_with = "author::serialize_author_option",
        deserialize_with = "author::author_option"
    )]
    pub author: Option<AuthorInfo>,
    //
    pub saved: bool,
    pub likes: Option<bool>,
    pub id: String,
    #[serde(deserialize_with = "utils::response_or_none")]
    /// Should be a listing if present
    replies: Option<Box<Thing>>,

    // pub approved_at_utc: Value,
    // pub comment_type: Value,
    // pub awarders: Vec<Value>,
    // pub mod_reason_by: Value,
    // pub banned_by: Value,
    // pub total_awards_received: i64,
    // pub user_reports: Vec<Value>,
    // pub banned_at_utc: Value,
    // pub mod_reason_title: Value,
    pub gilded: i64,
    pub archived: bool,
    // pub collapsed_reason_code: Value,
    pub no_follow: bool,
    pub can_mod_post: bool,
    pub created_utc: f64,
    pub send_replies: bool,
    pub parent_id: String,
    pub score: i64,
    // pub approved_by: Value,
    // pub mod_note: Value,
    // pub all_awardings: Vec<Value>,
    pub collapsed: bool,
    pub body: String,
    #[serde(deserialize_with = "utils::response_or_none")]
    pub edited: Option<f64>,
    // pub top_awarded_type: Value,
    pub name: Fullname,
    pub is_submitter: bool,
    pub downs: i64,
    pub body_html: String,
    // pub removal_reason: Value,
    // pub collapsed_reason: Value,
    // pub distinguished: Value,
    // pub associated_award: Value,
    pub stickied: bool,
    pub can_gild: bool,
    // pub gildings: Gildings,
    // pub unrepliable_reason: Value,
    pub score_hidden: bool,
    pub permalink: String,
    pub locked: bool,
    // pub report_reasons: Value,
    pub created: f64,
    // pub treatment_tags: Vec<Value>,
    pub link_id: String,
    pub controversiality: i64,
    pub depth: i64,
    // pub collapsed_because_crowd_control: Value,
    // pub mod_reports: Vec<Value>,
    // pub num_reports: Value,
    pub ups: i64,
}

impl Comment {
    /// flutter_rust_bridge:getter,sync
    pub fn replies(&self) -> Vec<Thing> {
        match &self.replies {
            Some(listing) => match &**listing {
                Thing::Listing(listing) => listing.children.clone(),
                _ => Vec::new(),
            },
            None => Vec::new(),
        }
    }
}

// #[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
// pub struct Gildings {}
