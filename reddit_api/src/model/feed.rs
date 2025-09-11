use crate::client::Client;
use crate::result::Result;
use crate::streamable::stream::IntoStreamPrivate;
use flutter_rust_bridge::frb;
pub use futures::{Stream, StreamExt};
use reqwest::Url;
use serde::{Deserialize, Serialize};

use super::{Thing, Timeframe};

/// All the kind of available feeds on reddit
#[derive(Debug, Default, PartialEq, Eq, Clone)]
#[frb(non_opaque)]
pub enum Feed {
    #[default]
    /// Home feed of the current user
    Home,
    /// r/all
    All,
    /// r/popular
    Popular,
    /// Should be a subreddit name without the `r/` prefix (e.g. "awwnime")
    Subreddit(String),
}

#[derive(Serialize, Deserialize, Debug, PartialEq, Eq, Clone, Copy)]
#[frb(non_opaque, json_serializable)]
pub enum FeedSort {
    Best,
    Hot,
    New(Timeframe),
    Top(Timeframe),
    Rising,
    Controversial(Timeframe),
}

impl FeedSort {
    #[must_use]
    pub(crate) fn add_to_url(&self, url: &Url) -> Url {
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

pub struct FeedStream {
    client: Client,
    feed: Feed,
    sort: FeedSort,
    base_url: Url,
}

impl FeedStream {
    pub fn new(client: Client, feed: Feed, sort: FeedSort, base_url: Url) -> Box<Self> {
        Box::new(Self {
            client,
            feed,
            sort,
            base_url,
        })
    }

    fn to_url(&self) -> Url {
        let base_url = self.base_url.clone();
        let url = match &self.feed {
            Feed::Home => base_url.clone(),
            Feed::All => base_url.join("r/all/").expect("Should not fail."),
            Feed::Popular => base_url.join("r/popular/").expect("Should not fail."),
            Feed::Subreddit(subreddit) => base_url
                .join(&format!("r/{subreddit}/"))
                .expect("Should not fail"),
        };
        let url = &self.sort.add_to_url(&url);
        url.clone()
    }
}

impl IntoStreamPrivate for FeedStream {
    type Output = Thing;

    fn to_stream(&self) -> futures::stream::BoxStream<'static, Result<Self::Output>> {
        let query_parameters = vec![("sr_detail".to_owned(), "true".to_owned())];
        self.client
            .clone()
            .stream_vec(self.to_url(), None, query_parameters)
            .boxed()
    }
}
