use comment::Comment;
use flutter_rust_bridge::frb;
use multi::Multi;
pub use post::Post;
use serde::{Deserialize, Serialize};
use subreddit::Subreddit;
use user::model::User;

use crate::{client::VoteDirection, error::Error};

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
#[frb(non_opaque)]
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
            Thing::Comment(comment) => Some(comment.get_name()),
            Thing::User(user) => Some(user.name()),
            Thing::Post(post) => Some(post.get_name()),
            Thing::Message => todo!(),
            Thing::Subreddit(subreddit) => Some(subreddit.other().get_name()),
            Thing::Award => None,
            Thing::Multi(multi) => Some(multi.name.clone()),
            Thing::More { .. } => None,
        }
    }
}

impl TryFrom<Thing> for Listing {
    type Error = Error;

    fn try_from(value: Thing) -> Result<Self, Self::Error> {
        match value {
            Thing::Listing(listing) => Ok(listing),
            _ => Err(Error::InvalidThing),
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
#[frb(non_opaque)]
pub enum Timeframe {
    Hour,
    Day,
    Week,
    Month,
    Year,
    All,
}

impl Timeframe {
    pub(crate) fn as_query_param(self) -> &'static str {
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
pub trait Votable {
    fn name(&self) -> &Fullname;
    fn set_likes(&mut self, likes: Option<bool>);
    fn set_saved(&mut self, saved: bool);
    async fn vote(
        &mut self,
        direction: VoteDirection,
        client: &crate::client::Client,
    ) -> crate::result::Result<()> {
        client.vote(self.name(), direction).await?;
        self.set_likes(direction.into());
        Ok(())
    }

    async fn save(&mut self, client: &crate::client::Client) -> crate::result::Result<()> {
        client.save(self.name()).await?;
        self.set_saved(true);
        Ok(())
    }
    async fn unsave(&mut self, client: &crate::client::Client) -> crate::result::Result<()> {
        client.unsave(self.name()).await?;
        self.set_saved(false);
        Ok(())
    }
}
