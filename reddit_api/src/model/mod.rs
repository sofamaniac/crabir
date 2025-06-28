use std::backtrace::{self, Backtrace};

use comment::Comment;
use multi::Multi;
pub use post::Post;
use reqwest::Url;
use serde::{Deserialize, Serialize};
use subreddit::Subreddit;
use user::User;

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
        id: String,
        name: String,
        count: u32,
        depth: u32,
        children: Vec<String>,
    },
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
    pub dist: Option<u64>,
    /// Is null when querying oauth.reddit.com
    pub modhash: Option<String>,
    geofilter: Option<String>,
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

#[derive(Serialize, Deserialize, Debug, PartialEq, Eq, Clone, Copy)]
/// flutter_rust_bridge:non_opaque
pub enum Sort {
    Best,
    Hot,
    New(Timeframe),
    Top(Timeframe),
    Rising,
    Controversial(Timeframe),
}

impl Sort {
    #[must_use]
    pub fn add_to_url(&self, url: &Url) -> Url {
        let sort = match &self {
            Self::Best => "best.json",
            Self::Hot => "hot.json",
            Self::Rising => "rising.json",
            Self::Top(_) => "top.json",
            Self::New(_) => "new.json",
            Self::Controversial(_) => "controversial.json",
        };
        let mut url = url.join(sort).expect("Should not fail.");
        match &self {
            Self::Top(timeframe) | Self::New(timeframe) | Self::Controversial(timeframe) => {
                let _ = url
                    .query_pairs_mut()
                    .append_pair("t", timeframe.as_query_param());
            }
            _ => (),
        }
        url
    }
}
