use futures::StreamExt;
use reqwest::Url;
use serde::{Deserialize, Serialize};

use crate::model::subreddit::Details;
use crate::result::Result;
use crate::{client::Client, error::Error, paging_handler::stream::IntoStreamPrivate};

use super::{Fullname, Thing, feed::FeedSort};

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct Multi {
    #[serde(rename = "can_edit", default)]
    pub can_edit: bool,
    #[serde(rename = "display_name")]
    pub display_name: String,
    pub name: Fullname,
    #[serde(rename = "description_html")]
    pub description_html: String,
    #[serde(rename = "num_subscribers")]
    pub num_subscribers: i64,
    #[serde(rename = "copied_from")]
    pub copied_from: Option<String>,
    #[serde(rename = "icon_url")]
    pub icon_url: String,
    pub subreddits: Vec<SubredditDetails>,
    #[serde(rename = "created_utc")]
    pub created_utc: f64,
    pub visibility: String,
    pub created: f64,
    #[serde(rename = "over_18")]
    pub over_18: bool,
    pub path: String,
    pub owner: String,
    #[serde(rename = "key_color")]
    pub key_color: Option<String>,
    #[serde(rename = "is_subscriber")]
    pub is_subscriber: bool,
    #[serde(rename = "owner_id")]
    pub owner_id: String,
    #[serde(rename = "description_md")]
    pub description_md: String,
    #[serde(rename = "is_favorited")]
    pub is_favorited: bool,
}

impl TryFrom<Thing> for Multi {
    type Error = Error;

    fn try_from(value: Thing) -> Result<Self> {
        match value {
            Thing::Multi(multi) => Ok(multi),
            _ => Err(Error::InvalidThing),
        }
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SubredditDetails {
    /// name of the subreddit (e.g. awwnime)
    pub name: String,
    pub data: Details,
}

pub struct MultiStream {
    client: Client,
    multi: Multi,
    sort: FeedSort,
    base_url: Url,
}

impl MultiStream {
    pub fn new(client: Client, multi: Multi, sort: FeedSort, base_url: Url) -> Box<Self> {
        Box::new(Self {
            client,
            multi,
            sort,
            base_url,
        })
    }

    fn to_url(&self) -> Url {
        let base_url = self.base_url.clone();
        let url = base_url.join(&self.multi.path).expect("Should not fail.");
        let url = &self.sort.add_to_url(&url);
        url.clone()
    }
}

impl IntoStreamPrivate for MultiStream {
    type Output = Vec<Thing>;

    fn to_stream(&self) -> futures::stream::BoxStream<'static, Result<Self::Output>> {
        self.client
            .clone()
            .stream_vec(self.to_url(), None, &[("sr_detail", "true")])
            .boxed()
    }
}
