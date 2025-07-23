use std::backtrace::Backtrace;

use comment::Comment;
use multi::Multi;
pub use post::Post;
use serde::{Deserialize, Serialize};
use subreddit::Subreddit;
use user::model::User;

use crate::error::Error;

pub mod author;
pub mod comment;
pub mod feed;
pub mod flair;
pub mod gallery;
pub mod multi;
pub mod post;
pub mod subreddit;
pub mod user;

#[derive(Serialize, Deserialize, Default, Debug, Clone, PartialEq, Eq, PartialOrd, Ord)]
/// Represent the kind and the ID of a thing.
/// For example a post has a fullname of the form "t3_xxxx".
pub struct Fullname(String);

impl AsRef<str> for Fullname {
    fn as_ref(&self) -> &str {
        &self.0
    }
}

#[derive(Serialize, Deserialize, Debug, Clone, PartialEq)]
#[serde(tag = "kind", content = "data")]
#[allow(clippy::large_enum_variant)]
/// flutter_rust_bridge:non_opaque
pub enum Thing {
    #[serde(rename = "Listing")]
    Listing(Listing),
    #[serde(rename = "t1")]
    Comment(Comment),
    #[serde(rename = "t2")]
    User(User),
    #[serde(rename = "t3")]
    Post(Post),
    #[serde(rename = "t4")]
    Message,
    #[serde(rename = "t5")]
    Subreddit(Subreddit),
    #[serde(rename = "t6")]
    Award,
    #[serde(rename = "LabeledMulti")]
    Multi(Multi),
    /// How to load more comment in a given thread
    #[serde(rename = "more")]
    More {
        /// Has the same id as its first children
        /// If there is no children is "_".
        id: String,
        /// Name of the first children. If there is none is "t1__".
        name: Fullname,
        count: u32,
        depth: u32,
        children: Vec<String>,
    },
}

impl Thing {
    /// flutter_rust_bridge:getter,sync
    pub fn name(&self) -> Option<Fullname> {
        match self {
            Thing::Listing(_) => None,
            Thing::Comment(comment) => Some(comment.name.clone()),
            Thing::User(user) => Some(user.name()),
            Thing::Post(post) => Some(post.name.clone()),
            Thing::Message => todo!(),
            Thing::Subreddit(subreddit) => Some(subreddit.other.name.clone()),
            Thing::Award => None,
            Thing::Multi(multi) => Some(multi.name.clone()),
            Thing::More { .. } => None,
        }
    }

    pub(crate) fn likes(&mut self, direction: crate::client::VoteDirection) {
        let likes: Option<bool> = direction.into();
        match self {
            Thing::Comment(comment) => comment.likes = likes,
            Thing::Post(post) => post.likes = likes,
            _ => (),
        }
    }

    pub(crate) fn save(&mut self, save: bool) {
        match self {
            Thing::Comment(comment) => comment.saved = save,
            Thing::Post(post) => post.saved = save,
            _ => (),
        }
    }
}

impl TryFrom<Thing> for Listing {
    type Error = Error;

    fn try_from(value: Thing) -> Result<Self, Self::Error> {
        match value {
            Thing::Listing(listing) => Ok(listing),
            _ => Err(Error::InvalidThing {
                backtrace: Backtrace::capture(),
            }),
        }
    }
}

#[derive(Serialize, Deserialize, Debug, Default, Clone, PartialEq)]
pub struct Listing {
    pub after: Option<String>,
    pub before: Option<String>,
    pub dist: Option<u32>,
    /// Is null when querying oauth.reddit.com
    pub modhash: Option<String>,
    geofilter: Option<String>,
    #[serde(default)]
    pub children: Vec<Thing>,
}

#[derive(Serialize, Deserialize, Debug, PartialEq, Eq, Clone, Copy)]
pub enum Timeframe {
    Hour,
    Day,
    Week,
    Month,
    Year,
    All,
}

impl Timeframe {
    fn as_query_param(self) -> &'static str {
        match self {
            Self::Hour => "hour",
            Self::Day => "day",
            Self::Week => "week",
            Self::Month => "month",
            Self::Year => "year",
            Self::All => "all",
        }
    }
}
