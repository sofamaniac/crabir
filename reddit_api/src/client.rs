use crate::model::feed::{self, Feed};
use crate::model::{Fullname, comment, user};
use crate::result::Result;
use crate::streamable::Streamable;
use std::backtrace::Backtrace;
use std::collections::HashMap;
use std::sync::Arc;

use base64::Engine;
use base64::prelude::BASE64_URL_SAFE_NO_PAD;
use chrono::{DateTime, Utc};
use futures::{Stream, lock::Mutex};
use futures::{TryStreamExt, stream};
use log::{debug, error, info};
use reqwest::RequestBuilder;

pub use reqwest::{IntoUrl, Url};
use serde::{
    Deserialize,
    de::{DeserializeOwned, Error as _},
};
use uuid::Uuid;

use crate::{
    error::Error,
    model::{Listing, Thing, user::model::UserInfo},
};

const USER_AGENT: &str = dotenv_codegen::dotenv!("USER_AGENT");
const CLIENT_ID: &str = dotenv_codegen::dotenv!("REDDIT_API_KEY");

#[derive(Clone, Debug)]
pub struct Client {
    http: reqwest::Client,
    refresh_token: Arc<Mutex<Option<String>>>,
    current_access_token: Arc<Mutex<Option<String>>>,
}

impl Default for Client {
    fn default() -> Self {
        Self::new_anonymous()
    }
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum VoteDirection {
    Up,
    Down,
    Neutral,
}

impl From<VoteDirection> for Option<bool> {
    fn from(value: VoteDirection) -> Self {
        match value {
            VoteDirection::Up => Some(true),
            VoteDirection::Down => Some(false),
            VoteDirection::Neutral => None,
        }
    }
}

pub const SCOPES: [&str; 19] = [
    "identity",
    "edit",
    "flair",
    "history",
    "modconfig",
    "modflair",
    "modlog",
    "modposts",
    "modwiki",
    "mysubreddits",
    "privatemessages",
    "read",
    "report",
    "save",
    "submit",
    "subscribe",
    "vote",
    "wikiedit",
    "wikiread",
];

impl Client {
    fn new_reqwest_client() -> reqwest::Client {
        reqwest::Client::builder()
            .user_agent(USER_AGENT)
            .build()
            .expect("Building reqwest client should not fail")
    }
    /// Create a new client for a logged out user
    #[must_use]
    pub fn new_anonymous() -> Self {
        Self {
            http: Self::new_reqwest_client(),
            refresh_token: Arc::new(Mutex::new(None)),
            current_access_token: Arc::new(Mutex::new(None)),
        }
    }
    /// Create a new client that is authenticated
    #[must_use]
    pub fn from_refresh_token(refresh_token: String) -> Self {
        Self {
            http: Self::new_reqwest_client(),
            refresh_token: Arc::new(Mutex::new(Some(refresh_token))),
            current_access_token: Arc::new(Mutex::new(Some(String::new()))),
        }
    }

    fn base_url(&self) -> Url {
        Url::parse("https://oauth.reddit.com/").expect("Should not panic.")
        // if self.current_access_token.lock().await.is_some() {
        //     Url::parse("https://oauth.reddit.com/").expect("Should not panic.")
        // } else {
        //     // FIXME: putting www.reddit.com results in requests being blocked
        //     // Switch to oauth for anonymous instead (see https://github.com/reddit-archive/reddit/wiki/OAuth2#application-only-oauth)
        //     Url::parse("https://reddit.com/").expect("Should not panic.")
        // }
    }

    /// Authenticate the current client
    pub async fn authenticate(&self, refresh_token: String) {
        *self.refresh_token.lock().await = Some(refresh_token);
        *self.current_access_token.lock().await = Some(String::new());
    }
}

const MAX_LIMIT: u64 = 100;

#[derive(Debug, Default, Clone)]
pub struct Pager {
    after: Option<String>,
    before: Option<String>,
    limit: u64,
}

impl Pager {
    #[must_use]
    pub fn add_to_url(&self, mut url: Url) -> Url {
        if let Some(after) = &self.after {
            let _ = url.query_pairs_mut().append_pair("after", after);
        } else if let Some(before) = &self.before {
            let _ = url.query_pairs_mut().append_pair("before", before);
        }

        if self.limit > 0 {
            let _ = url
                .query_pairs_mut()
                .append_pair("limit", &format!("{}", self.limit));
        } else {
            let _ = url
                .query_pairs_mut()
                .append_pair("limit", &format!("{MAX_LIMIT}"));
        }
        url
    }
}

impl Pager {
    pub fn after(&mut self, after: Option<String>) {
        if after.is_some() {
            self.after = after;
            self.before = None;
        }
    }

    pub fn before(&mut self, before: Option<String>) {
        if before.is_some() {
            self.before = before;
            self.after = None;
        }
    }
}

async fn parse_response<T: DeserializeOwned>(response: reqwest::Response) -> Result<T> {
    let response = response.error_for_status();
    match response {
        Ok(response) => {
            let text = response.text().await?;
            match serde_json::from_str(&text) {
                Ok(json) => Ok(json),
                Err(e) => Err(Error::Parsing {
                    source: serde_json::Error::custom(format!(
                        "serde_json error {e}. Failed to parse this post: {text}"
                    )),
                    backtrace: Backtrace::capture(),
                }),
            }
        }
        Err(error) => {
            error!("{error}");
            Err(Error::Reqwest {
                source: error,
                backtrace: Backtrace::capture(),
            })
        }
    }
}

impl Client {
    fn get(&self, url: impl IntoUrl) -> RequestBuilder {
        self.http.get(url).query(&[("raw_json", "1")])
    }
    fn post(&self, url: impl IntoUrl) -> RequestBuilder {
        self.http.post(url)
    }

    /// Check if token has expired
    async fn is_access_token_valid(&self) -> bool {
        #[derive(Deserialize)]
        struct Claims {
            exp: f64,
        }
        if let Some(token) = &*self.current_access_token.lock().await {
            let parts: Vec<&str> = token.splitn(3, '.').collect();
            // The token is not valid
            if parts.len() < 3 {
                return false;
            }
            if let Ok(Ok(claims)) = BASE64_URL_SAFE_NO_PAD
                .decode(parts[1])
                .map(|bytes| serde_json::from_slice::<Claims>(&bytes))
            {
                #[allow(clippy::cast_possible_truncation)]
                let date = DateTime::from_timestamp_millis((claims.exp * 1000.0) as i64)
                    .expect("Converting timestamp should not fail.");
                debug!("Current token is valid until ${date}");
                date >= Utc::now()
            } else {
                false
            }
        } else {
            debug!("Access token is not set");
            true
        }
    }

    /// Create a new access token for a logged out user
    pub async fn new_logged_out_user_token(&self) -> Result<()> {
        debug!("NEW LOGGED OUT USER TOKEN CREATION");
        let url =
            Url::parse("https://www.reddit.com/api/v1/access_token").expect("Should not fail");
        // WARN: Reddit recommends having a unique UUID per user, but with this method it will be
        // unique per session.
        let device_id = Uuid::new_v4().simple().to_string();
        let mut body = HashMap::new();
        body.insert(
            "grant_type",
            "https://oauth.reddit.com/grants/installed_client",
        );
        body.insert("device_id", &device_id);
        let request = self
            .http
            .post(url)
            .basic_auth::<&str, &str>(CLIENT_ID, None)
            .form(&body)
            .build()
            .expect("Building request should not fail");
        #[derive(Deserialize)]
        struct Response {
            access_token: String,
            #[serde(rename = "token_type")]
            _token_type: String,
            #[serde(rename = "expires_in")]
            _expires_in: u64,
            #[serde(rename = "scope")]
            _scope: String,
        }
        let response = self.http.execute(request).await?;
        let response: Response = parse_response(response).await?;
        *self.current_access_token.lock().await = Some(response.access_token);
        info!("[CLIENT] Anonymous token created.");
        Ok(())
    }

    /// Get a new access token
    async fn refresh_token(&self) -> Result<()> {
        #[derive(Deserialize)]
        struct Response {
            access_token: String,
        }
        info!("[CLIENT] Refreshing token");
        let refresh_token = match &*self.refresh_token.lock().await {
            None => return Ok(()),
            Some(token) => token.clone(),
        };
        let url = Url::parse("https://www.reddit.com/").expect("Should not panic.");
        let url = url.join("api/v1/access_token").expect("Should not fail.");
        let mut body = HashMap::new();
        body.insert("grant_type", "refresh_token");
        body.insert("refresh_token", &refresh_token);

        let request = self
            .http
            .post(url)
            .basic_auth::<&str, &str>(CLIENT_ID, None)
            .form(&body)
            .build()
            .expect("Building request should not fail");
        debug!("{request:#?}");
        let response = self.http.execute(request).await?;
        let response: Response = parse_response(response).await?;
        *self.current_access_token.lock().await = Some(response.access_token);
        info!("[CLIENT] Token has been refreshed.");
        Ok(())
    }

    async fn execute(&self, request: RequestBuilder) -> Result<reqwest::Response> {
        if !self.is_access_token_valid().await {
            self.refresh_token().await?;
        }
        let request = if self.is_access_token_valid().await {
            if let Some(access_token) = &*self.current_access_token.lock().await {
                request.bearer_auth(access_token)
            } else {
                request.basic_auth::<&str, &str>(CLIENT_ID, None)
            }
        } else {
            request
        };
        debug!("Executing {request:#?}");
        let res = self
            .http
            .execute(request.build().expect("Building request should not fail"))
            .await?;
        debug!("Done with request.");
        res.error_for_status().map_err(Into::into)
    }

    /// flutter_rust_bridge:sync
    pub fn feed_stream(&self, feed: &Feed, sort: &feed::FeedSort) -> Streamable {
        let base_url = self.base_url();
        Streamable::new(feed::FeedStream::new(
            self.clone(),
            feed.clone(),
            *sort,
            base_url,
        ))
    }

    /// Get the info of the current user.
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    pub async fn logged_user_info(&self) -> Result<UserInfo> {
        let base_url = self.base_url();
        let url = base_url.join("api/v1/me.json").expect("Should not fail");
        let request = self.get(url);
        let response = self.execute(request).await?;
        let json = parse_response(response).await?;
        Ok(json)
    }

    fn stream_listing(
        &self,
        url: Url,
        pager: Option<Pager>,
        query_params: &[(&str, &str)],
    ) -> impl Stream<Item = Result<Listing>> {
        stream::unfold(
            (url, pager.unwrap_or_default()),
            move |(url, mut pager)| async move {
                let client = self.clone();
                let url = pager.add_to_url(url.clone());
                let request = client.get(url.clone());
                let request = request.query(query_params);
                let response = client.execute(request).await;
                debug!("[stream_listing] page done");
                match response {
                    Err(e) => Some((Err(e), (url, Pager::default()))),
                    Ok(response) => match parse_response::<Thing>(response).await {
                        Ok(thing) => match thing {
                            Thing::Listing(listing) => {
                                pager.after(listing.after.clone());
                                debug!("{pager:?}");
                                if listing.after.clone()?.is_empty() {
                                    return None;
                                }
                                Some((Ok(listing), (url, pager)))
                            }
                            _ => Some((
                                Err(Error::InvalidThing {
                                    backtrace: Backtrace::capture(),
                                }),
                                (url, Pager::default()),
                            )),
                        },
                        Err(e) => Some((Err(e), (url, Pager::default()))),
                    },
                }
            },
        )
    }

    pub(crate) fn stream_vec<T>(
        self,
        url: Url,
        pager: Option<Pager>,
        query_params: &[(&str, &str)],
    ) -> impl Stream<Item = Result<T>>
    where
        T: TryFrom<Thing>,
    {
        stream::unfold(
            (url, pager.unwrap_or_default(), Vec::new()),
            move |(url, mut pager, mut buffer)| {
                let client = self.clone();
                async move {
                    if let Some(thing) = buffer.pop() {
                        Some((Ok(thing), (url, pager, buffer)))
                    } else {
                        let url = pager.add_to_url(url.clone());
                        let request = client.get(url.clone());
                        let request = request.query(query_params);
                        let response = client.execute(request).await;
                        match response {
                            Err(e) => Some((Err(e), (url, Pager::default(), buffer))),
                            Ok(response) => match parse_response::<Thing>(response).await {
                                Ok(thing) => match thing {
                                    Thing::Listing(listing) => {
                                        pager.after(listing.after.clone());
                                        debug!("{pager:?}");
                                        if listing.after.clone()?.is_empty() {
                                            return None;
                                        }
                                        buffer = listing
                                            .children
                                            .into_iter()
                                            .filter_map(|item| item.try_into().ok())
                                            .collect();
                                        buffer.reverse();

                                        buffer.pop().map(|thing| (Ok(thing), (url, pager, buffer)))
                                    }
                                    _ => Some((
                                        Err(Error::InvalidThing {
                                            backtrace: Backtrace::capture(),
                                        }),
                                        (url, Pager::default(), buffer),
                                    )),
                                },
                                Err(e) => Some((Err(e), (url, Pager::default(), buffer))),
                            },
                        }
                    }
                }
            },
        )
    }

    /// Try to collect everything from a paged request. Will continue until there is no
    /// more item to collect.
    async fn collect_paged<T: TryFrom<Thing>>(
        &self,
        url: &Url,
        query_params: &[(&str, &str)],
    ) -> Result<Vec<T>> {
        let res = self.stream_listing(url.clone(), None, query_params);
        let res: Vec<Listing> = res.try_collect().await?;
        Ok(res
            .into_iter()
            .flat_map(|listing| listing.children)
            .filter_map(|e| e.try_into().ok())
            .collect())
    }

    /// Get the list of all subreddits the current user is subscribed to.
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    pub async fn subsriptions(&self) -> Result<Vec<crate::model::subreddit::Subreddit>> {
        let base_url = self.base_url();
        let url = base_url
            .join("subreddits/mine/subscriber.json")
            .expect("Should not fail.");
        self.collect_paged(&url, &[]).await
    }

    /// Get the list of multireddits the current user is subscribed to.
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    pub async fn multis(&self) -> Result<Vec<crate::model::multi::Multi>> {
        let base_url = self.base_url();
        let url = base_url
            .join("api/multi/mine.json")
            .expect("Should not fail.");
        self.collect_paged(&url, &[]).await
    }

    /// Vote on a votable item (i.e. a [`Post`] or a [`Comment`]).
    /// # Errors
    /// Returns an error if the request failed.
    pub async fn vote(&self, thing: &Fullname, direction: VoteDirection) -> Result<()> {
        let dir = match direction {
            VoteDirection::Up => 1,
            VoteDirection::Down => -1,
            VoteDirection::Neutral => 0,
        };

        let url = self.base_url().join("api/vote").expect("Should not fail.");
        let request = self.post(url);
        let request = request.query(&[("id", thing.as_ref()), ("dir", &format!("{dir}"))]);
        let _ = self.execute(request).await?;
        Ok(())
    }

    /// Save a saveable item (i.e. a [`Post`] or a [`Comment`]).
    /// # Errors
    /// Returns an error if the request failed.
    pub async fn save(&self, thing: &Fullname) -> Result<()> {
        let url = self.base_url().join("api/save").expect("Should not fail.");
        let request = self.post(url);
        let request = request.query(&[("id", &thing)]);
        let _ = self.execute(request).await?;
        Ok(())
    }

    /// Unsave a saveable item (i.e. a [`Post`] or a [`Comment`]).
    /// # Errors
    /// Returns an error if the request failed.
    pub async fn unsave(&self, thing: &Fullname) -> Result<()> {
        let url = self
            .base_url()
            .join("api/unsave")
            .expect("Should not fail.");
        let request = self.post(url);
        let request = request.query(&[("id", &thing)]);
        let _ = self.execute(request).await?;
        Ok(())
    }

    /// Get saved items ( both [`Post`] and [`Comment`] ) for the specified user.
    /// # Errors
    /// Fails if api request fails.
    /// flutter_rust_bridge:sync
    pub fn user_saved(&self, username: String) -> Streamable {
        Streamable::new(user::streams::UserStream::new(
            self.clone(),
            username,
            String::from("saved"),
        ))
    }

    /// flutter_rust_bridge:sync
    pub fn user_overview(&self, username: String, sort: user::model::UserStreamSort) -> Streamable {
        Streamable::new(user::streams::UserStreamSorted::new(
            self.clone(),
            username,
            sort,
            String::from("overview"),
        ))
    }
    /// flutter_rust_bridge:sync
    pub fn user_submitted(
        &self,
        username: String,
        sort: user::model::UserStreamSort,
    ) -> Streamable {
        Streamable::new(user::streams::UserStreamSorted::new(
            self.clone(),
            username,
            sort,
            String::from("submitted"),
        ))
    }
    /// flutter_rust_bridge:sync
    pub fn user_comments(&self, username: String, sort: user::model::UserStreamSort) -> Streamable {
        Streamable::new(user::streams::UserStreamSorted::new(
            self.clone(),
            username,
            sort,
            String::from("comments"),
        ))
    }
    /// flutter_rust_bridge:sync
    pub fn user_upvoted(&self, username: String) -> Streamable {
        Streamable::new(user::streams::UserStream::new(
            self.clone(),
            username,
            String::from("upvoted"),
        ))
    }
    /// flutter_rust_bridge:sync
    pub fn user_downvoted(&self, username: String) -> Streamable {
        Streamable::new(user::streams::UserStream::new(
            self.clone(),
            username,
            String::from("downvoted"),
        ))
    }
    /// flutter_rust_bridge:sync
    pub fn user_hidden(&self, username: String) -> Streamable {
        Streamable::new(user::streams::UserStream::new(
            self.clone(),
            username,
            String::from("hidden"),
        ))
    }
    /// flutter_rust_bridge:sync
    pub fn user_gilded(&self, username: String) -> Streamable {
        Streamable::new(user::streams::UserStream::new(
            self.clone(),
            username,
            String::from("gilded"),
        ))
    }

    /// Returns the comments for the post at the given permalink. Each element in the vec is either
    /// a [`Thing::Comment`] or a [`Thing::More`].
    /// # Errors
    /// Fails if the request fails or the parsing of the response fails.
    pub async fn comments(
        &self,
        permalink: String,
        sort: Option<comment::CommentSort>,
    ) -> Result<Vec<Thing>> {
        let url = self
            .base_url()
            .join(&format!("{permalink}.json"))
            .expect("Should not fail.");
        let request = self.get(url);
        let request = if let Some(sort) = sort {
            request.query(&[("sort", sort.as_url())])
        } else {
            request
        };
        let result = self.execute(request).await?;
        let result: [Thing; 2] = parse_response(result).await?;
        if let [_, Thing::Listing(comments)] = result {
            Ok(comments.children)
        } else {
            Err(Error::InvalidThing {
                backtrace: Backtrace::capture(),
            })
        }
    }

    // FIXME: crash when there are too many children because the url is too long.
    // the doc says that it supports only up to 100 children.
    pub async fn load_more_comments(
        &self,
        parent_id: Fullname,
        children: Vec<String>,
        sort: Option<comment::CommentSort>,
    ) -> Result<Vec<Thing>> {
        #[derive(Deserialize)]
        struct Response {
            json: Json,
        }
        #[derive(Deserialize)]
        struct Json {
            // error: Option<String>,
            #[serde(default)]
            data: Data,
        }
        #[derive(Deserialize, Default)]
        struct Data {
            things: Vec<Thing>,
        }
        let url = self
            .base_url()
            .join("api/morechildren.json")
            .expect("Should not fail.");
        let request = self.get(url).query(&[
            ("api_type", "json"),
            ("children", &children.join(",")),
            ("link_id", parent_id.as_ref()),
        ]);
        let request = if let Some(sort) = sort {
            request.query(&[("sort", sort.as_url())])
        } else {
            request
        };
        let result = self.execute(request).await?;
        let result: Response = parse_response(result).await?;
        Ok(result.json.data.things)
    }

    pub(crate) fn user_stream<T: TryFrom<Thing>>(
        self,
        endpoint: String,
        pager: Option<Pager>,
    ) -> impl Stream<Item = Result<T>> {
        let url = self
            .base_url()
            .join(&endpoint)
            .expect("Should not fail to build url.");
        self.clone().stream_vec(url, pager, &[])
    }
    pub(crate) fn sorted_user_stream<T: TryFrom<Thing>>(
        self,
        endpoint: String,
        sort: user::model::UserStreamSort,
        pager: Option<Pager>,
    ) -> impl Stream<Item = Result<T>> {
        let mut url = self
            .base_url()
            .join(&endpoint)
            .expect("Should not fail to build url.");
        for (name, value) in sort.to_query() {
            url.query_pairs_mut().append_pair(name, value);
        }
        self.stream_vec(url, pager, &[])
    }
}
