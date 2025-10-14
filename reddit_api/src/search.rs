use flutter_rust_bridge::frb;
use futures::StreamExt;
use reqwest::Url;
use uuid::Uuid;

use crate::{
    client::Client,
    model::{Thing, Timeframe, flair::Flair},
    paging_handler::stream::IntoStreamPrivate,
    result::Result,
};

pub struct SearchPost {
    client: Client,
    subreddit: Option<String>,
    query: String,
    sort: PostSearchSort,
}

impl SearchPost {
    #[frb(sync)]
    pub fn new(
        client: Client,
        subreddit: Option<String>,
        flair: Option<Flair>,
        query: Option<String>,
        sort: PostSearchSort,
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
            sort,
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
    type Output = Vec<Thing>;

    fn to_stream(&self) -> futures::stream::BoxStream<'static, Result<Self::Output>> {
        let mut query_params = vec![("sr_detail".to_owned(), "true".to_owned())];
        query_params.push(("q".to_owned(), self.query.clone()));
        query_params.append(&mut self.sort.as_query_param());
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
#[frb(non_opaque)]
pub enum PostSearchSort {
    Relevance(Timeframe),
    Hot,
    Top(Timeframe),
    New,
    Comments(Timeframe),
}

impl PostSearchSort {
    fn as_query_param(&self) -> Vec<(String, String)> {
        match self {
            Self::Relevance(timeframe) => vec![
                ("sort".to_owned(), "relevance".to_owned()),
                ("t".to_owned(), timeframe.as_query_param().to_owned()),
            ],
            Self::Hot => vec![("sort".to_owned(), "hot".to_owned())],
            Self::Top(timeframe) => vec![
                ("sort".to_owned(), "top".to_owned()),
                ("t".to_owned(), timeframe.as_query_param().to_owned()),
            ],
            Self::New => vec![("sort".to_owned(), "new".to_owned())],
            Self::Comments(timeframe) => vec![
                ("sort".to_owned(), "comments".to_owned()),
                ("t".to_owned(), timeframe.as_query_param().to_owned()),
            ],
        }
    }
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum SubredditSearchSort {
    Relevance,
    Activity,
}

impl SubredditSearchSort {
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
    sort: SubredditSearchSort,
    uuid: Uuid,
}

impl SearchSubreddit {
    #[frb(sync)]
    pub fn new(client: Client, query: String, sort: SubredditSearchSort) -> Self {
        Self {
            client,
            query,
            sort,
            uuid: Uuid::new_v4(),
        }
    }
}

impl IntoStreamPrivate for SearchSubreddit {
    type Output = Vec<Thing>;

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
