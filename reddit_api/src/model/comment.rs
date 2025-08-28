use chrono::DateTime;
use chrono::Local;
use chrono::Utc;
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
use crate::utils;

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
    pub saved: bool,
    pub likes: Option<bool>,
    pub id: String,
    #[serde(deserialize_with = "utils::response_or_none")]
    /// Should be a listing if present
    /// Because they can be deeply nested we use a `Value` that is
    /// deserialize only when needed and only one level at a time.
    /// We have to do that, because otherwise parsing sometimes overflows the stack.
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
    pub gilded: i32,
    pub archived: bool,
    // pub collapsed_reason_code: Value,
    pub no_follow: bool,
    pub can_mod_post: bool,
    /// Date of creation in UTC
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    pub created_utc: DateTime<Utc>,
    /// Date of creation in logged-in user locale
    #[serde_as(as = "serde_with::TimestampSecondsWithFrac<f64>")]
    pub created: DateTime<Local>,
    pub send_replies: bool,
    pub parent_id: String,
    pub score: i32,
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
    pub downs: i32,
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
    // pub treatment_tags: Vec<Value>,
    pub link_id: String,
    pub controversiality: i32,
    // Depth is absent when requesting the user's overview.
    #[serde(default)]
    pub depth: i32,
    // pub collapsed_because_crowd_control: Value,
    // pub mod_reports: Vec<Value>,
    // pub num_reports: Value,
    pub ups: i32,

    #[serde(skip)]
    parsed_replies: Vec<Thing>,
}

impl Comment {
    /// flutter_rust_bridge:sync
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

    /// flutter_rust_bridge:sync
    /// If `more` is a `Thing::More`, if it exists in `Self::replies` or in the replies of one of
    /// its children, will replace it with `new_things`.
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
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
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
