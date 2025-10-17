use std::collections::HashMap;

use chrono::DateTime;
use chrono::Local;
use chrono::Utc;
use flutter_getter::FlutterGetters;
use flutter_rust_bridge::frb;
use getset::Getters;
use log::debug;
use serde::Deserialize;
use serde::Serialize;
use serde_json::Value;
use serde_with::serde_as;

use super::Fullname;
use super::Thing;
use super::author;
use super::author::AuthorInfo;
use crate::error::Error;
use crate::model::gallery::GalleryMedia;
use crate::utils;
use crate::votable::{Votable, private::PrivateVotable};

impl TryFrom<Thing> for Comment {
    type Error = Error;

    fn try_from(value: Thing) -> Result<Self, Self::Error> {
        match value {
            Thing::Comment(comment) => Ok(comment),
            _ => Err(Error::InvalidThing),
        }
    }
}

#[serde_as]
#[derive(Default, Debug, Clone, Getters, FlutterGetters, PartialEq, Serialize, Deserialize)]
#[getset(get = "pub(crate)")]
pub struct Comment {
    // SUBREDDIT
    subreddit_id: String,
    subreddit_name_prefixed: String,
    subreddit_type: String,
    subreddit: String,
    #[serde(
        flatten,
        serialize_with = "author::serialize_author_option",
        deserialize_with = "author::author_option"
    )]
    author: Option<AuthorInfo>,
    #[getset(skip)]
    saved: bool,
    #[getset(skip)]
    likes: Option<bool>,
    id: String,
    #[serde(deserialize_with = "utils::response_or_none")]
    /// Should be a listing if present
    /// Because they can be deeply nested we use a `Value` that is
    /// deserialize only when needed and only one level at a time.
    /// We have to do that, because otherwise parsing sometimes overflows the stack.
    #[getset(skip)]
    #[flutter_getter(skip)]
    replies: Option<Value>,

    // pub approved_at_utc: Value,
    // pub comment_type: Value,
    // pub awarders: Vec<Value>,
    // pub mod_reason_by: Value,
    // pub banned_by: Value,
    // pub total_awards_received: u32,
    // pub user_reports: Vec<Value>,
    // pub banned_at_utc: Value,
    // pub mod_reason_title: Value,
    gilded: i32,
    archived: bool,
    // pub collapsed_reason_code: Value,
    no_follow: bool,
    can_mod_post: bool,
    /// Date of creation in UTC
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    created_utc: DateTime<Utc>,
    /// Date of creation in logged-in user locale
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    created: DateTime<Local>,
    send_replies: bool,
    parent_id: Fullname,
    score: i32,
    // pub approved_by: Value,
    // pub mod_note: Value,
    // pub all_awardings: Vec<Value>,
    collapsed: bool,
    body: String,
    #[serde(deserialize_with = "utils::response_or_none")]
    edited: Option<f64>,
    // pub top_awarded_type: Value,
    #[getset(skip)]
    name: Fullname,
    is_submitter: bool,
    downs: i32,
    body_html: String,
    // pub removal_reason: Value,
    // pub collapsed_reason: Value,
    #[serde(default)]
    distinguished: Option<String>,
    // pub associated_award: Value,
    stickied: bool,
    can_gild: bool,
    // pub gildings: Gildings,
    // pub unrepliable_reason: Value,
    score_hidden: bool,
    permalink: String,
    locked: bool,
    // pub report_reasons: Value,
    // pub treatment_tags: Vec<Value>,
    link_id: String,
    controversiality: i32,
    // Depth is absent when requesting the user's overview.
    #[serde(default)]
    depth: i32,
    // pub collapsed_because_crowd_control: Value,
    // pub mod_reports: Vec<Value>,
    // pub num_reports: Value,
    ups: i32,

    #[serde(skip)]
    #[getset(skip)]
    #[flutter_getter(skip)]
    parsed_replies: Vec<Thing>,

    /// Optional embeded images
    #[serde(default)]
    media_metadata: HashMap<String, GalleryMedia>,
}

impl Comment {
    #[frb(sync)]
    pub fn replies(&mut self) -> Vec<Thing> {
        if self.parsed_replies.is_empty() {
            self.parsed_replies = match &self.replies {
                Some(value) => match serde_json::from_value(value.clone()) {
                    Ok(Thing::Listing(listing)) => listing.children.clone(),
                    _ => Vec::new(),
                },
                None => Vec::new(),
            }
        }
        self.parsed_replies.clone()
    }

    /// If `more` is a `Thing::More`, if it exists in `Self::replies` or in the replies of one of
    /// its children, will replace it with `new_things`.
    #[frb(sync)]
    pub fn replace_more(&mut self, more: &Thing, new_things: &[Thing]) {
        if let Thing::More { .. } = more {
            // Ensure replies where parsed.
            let _ = self.replies();
            if let Some(index) = self.parsed_replies.iter().position(|t| t == more) {
                debug!("replaced more");
                self.parsed_replies
                    .splice(index..index + 1, new_things.to_vec());
            } else {
                for thing in &mut self.parsed_replies {
                    if let Thing::Comment(comment) = thing {
                        comment.replace_more(more, new_things);
                    }
                }
            }
        }
    }

    /// When submitting a new comment, the response of the post request does not contains its depth
    pub(crate) fn set_depth(&mut self, depth: i32) {
        self.depth = depth;
    }
}

impl PrivateVotable for Comment {
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
impl Votable for Comment {}

#[derive(Debug, Clone, Copy, PartialEq, Eq, Deserialize, Serialize)]
#[serde(rename_all = "lowercase")]
pub enum CommentSort {
    Confidence,
    Top,
    New,
    Controversial,
    Old,
    Random,
    QA,
    Live,
}

impl CommentSort {
    pub(crate) fn as_url(&self) -> &'static str {
        match self {
            Self::Confidence => "confidence",
            Self::Top => "top",
            Self::New => "new",
            Self::Controversial => "controversial",
            Self::Old => "old",
            Self::Random => "random",
            Self::QA => "qa",
            Self::Live => "live",
        }
    }
}

// #[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
// pub struct Gildings {}
