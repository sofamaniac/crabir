use futures::StreamExt;
use reqwest::Url;
use uuid::Uuid;

use crate::{
    client::Client,
    model::{Thing, flair::Flair},
    result::Result,
    streamable::stream::IntoStreamPrivate,
};

pub struct SearchPost {
    client: Client,
    subreddit: Option<String>,
    query: String,
}

impl SearchPost {
    ///flutter_rust_bridge:sync
    pub fn new(
        client: Client,
        subreddit: Option<String>,
        flair: Option<Flair>,
        query: Option<String>,
    ) -> Self {
        let mut query_params = Vec::new();
        if let Some(flair) = flair {
            query_params.push(format!("flair:{}", flair.text.unwrap()))
        };
        if let Some(query) = query {
            query_params.push(query);
        }
        Self {
            client,
            subreddit,
            query: query_params.join(" "),
        }
    }

    fn to_url(&self) -> Url {
        let url = self.client.base_url();
        self.subreddit
            .as_ref()
            .map_or(url.join("search.json").expect("Should not fail"), |sub| {
                url.join(&format!("r/{sub}/search.json"))
                    .expect("Should not fail")
            })
    }
}

impl IntoStreamPrivate for SearchPost {
    type Output = Thing;

    fn to_stream(&self) -> futures::stream::BoxStream<'static, Result<Self::Output>> {
        let mut query_params = vec![("sr_detail".to_owned(), "true".to_owned())];
        query_params.push(("q".to_owned(), self.query.clone()));
        if self.subreddit.is_some() {
            query_params.push(("restrict_sr".to_owned(), "true".to_owned()));
        }
        self.client
            .clone()
            .stream_vec(self.to_url(), None, query_params)
            .boxed()
    }
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum SearchSort {
    Relevance,
    Activity,
}

impl SearchSort {
    fn as_query_param(&self) -> String {
        match self {
            Self::Relevance => "relevance".to_owned(),
            Self::Activity => "activity".to_owned(),
        }
    }
}

#[derive(Debug, Clone)]
pub struct SearchSubreddit {
    client: Client,
    query: String,
    sort: SearchSort,
    uuid: Uuid,
}

impl SearchSubreddit {
    ///flutter_rust_bridge:sync
    pub fn new(client: Client, query: String, sort: SearchSort) -> Self {
        Self {
            client,
            query,
            sort,
            uuid: Uuid::new_v4(),
        }
    }
}

impl IntoStreamPrivate for SearchSubreddit {
    type Output = Thing;

    fn to_stream(&self) -> futures::stream::BoxStream<'static, Result<Self::Output>> {
        let mut query_params = vec![("sr_detail".to_owned(), "true".to_owned())];
        query_params.push(("q".to_owned(), self.query.clone()));
        query_params.push(("sort".to_owned(), self.sort.as_query_param()));
        query_params.push((
            "search_query_id".to_owned(),
            self.uuid.hyphenated().to_string(),
        ));
        let url = self
            .client
            .base_url()
            .join("/subreddits/search.json")
            .expect("Should not fail.");
        self.client
            .clone()
            .stream_vec(url, None, query_params)
            .boxed()
    }
}
