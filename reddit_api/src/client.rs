use crate::listing_stream::{ListingStream, Wrapper};
use crate::model::Fullname;
use crate::model::feed::Feed;
use crate::{model::feed::FeedStream, result::Result};
use std::backtrace::Backtrace;
use std::collections::HashMap;
use std::sync::Arc;

use base64::Engine;
use base64::prelude::BASE64_URL_SAFE_NO_PAD;
use chrono::{DateTime, Utc};
use futures::{Stream, lock::Mutex};
use log::{debug, error, info};
use reqwest::RequestBuilder;

pub use reqwest::{IntoUrl, Url};
use serde::{
    Deserialize,
    de::{DeserializeOwned, Error as _},
};

use crate::{
    error::Error,
    model::{Listing, Sort, Thing, post::Post, user::UserInfo},
};

const USER_AGENT: &str = "com.sofamaniac.crabir";
const CLIENT_ID: &str = "w0DROpe2H7uv2inVTvSfZw";

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

    async fn base_url(&self) -> Url {
        if self.current_access_token.lock().await.is_some() {
            Url::parse("https://oauth.reddit.com/").expect("Should not panic.")
        } else {
            // FIXME: putting www.reddit.com results in requests being blocked
            // Switch to oauth for anonymous instead (see https://github.com/reddit-archive/reddit/wiki/OAuth2#application-only-oauth)
            Url::parse("https://reddit.com/").expect("Should not panic.")
        }
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

    pub(crate) async fn feed_request(
        &self,
        feed: &Feed,
        sort: &Sort,
        pager: &Pager,
    ) -> Result<Listing> {
        let base_url = self.base_url().await;
        let url = match feed {
            Feed::Home => base_url.clone(),
            Feed::All => base_url.join("r/all/").expect("Should not fail."),
            Feed::Popular => base_url.join("r/popular/").expect("Should not fail."),
            Feed::Subreddit(subreddit) => base_url
                .join(&format!("r/{subreddit}/"))
                .expect("Should not fail"),
        };
        let url = sort.add_to_url(&url);
        let url = pager.add_to_url(url);
        let request = self.get(url);
        let posts = self.execute(request).await?;
        let json = parse_response(posts).await?;
        match json {
            Thing::Listing(listing) => Ok(listing),
            _ => Err(Error::InvalidThing {
                backtrace: Backtrace::capture(),
            }),
        }
    }

    /// Get the info of the current user.
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    pub async fn logged_user_info(&self) -> Result<UserInfo> {
        let base_url = self.base_url().await;
        let url = base_url.join("api/v1/me.json").expect("Should not fail");
        let request = self.get(url);
        let response = self.execute(request).await?;
        let json = parse_response(response).await?;
        Ok(json)
    }

    /// Get a stream of posts for the specified feed with the specified sort.
    /// Yield an error when the request to reddit API failed.
    pub fn feed(&self, feed: Feed, sort: Sort) -> impl Stream<Item = Result<Post>> + Send + Sync {
        Wrapper::new(FeedStream::new(self.clone(), feed, sort))
    }

    /// Get one page of a listing.
    async fn paged_request<T: TryFrom<Thing>>(
        &self,
        url: &Url,
        pager: &Pager,
    ) -> Result<(Vec<T>, Pager)> {
        let listing = self.paged_listing(url, pager).await?;
        let mut pager = Pager::default();
        pager.after(listing.after);
        let things = listing
            .children
            .into_iter()
            .filter_map(|e| e.try_into().ok())
            .collect();
        Ok((things, pager))
    }

    async fn paged_listing(&self, url: &Url, pager: &Pager) -> Result<Listing> {
        let url = pager.add_to_url(url.clone());
        let request = self.get(url);
        let response = self.execute(request).await?;
        let thing: Thing = parse_response(response).await?;
        thing.try_into()
    }

    /// Try to collect everything from a paged request. Will continue until there is no
    /// more item to collect.
    async fn collect_paged<T: TryFrom<Thing>>(&self, url: &Url) -> Result<Vec<T>> {
        let (mut things, pager) = self.paged_request(url, &Pager::default()).await?;
        loop {
            let (mut page_things, pager) = self.paged_request(url, &pager).await?;
            things.append(&mut page_things);
            match pager.after {
                Some(s) if s.is_empty() => break,
                None => break,
                _ => (),
            }
        }
        Ok(things)
    }

    /// Get the list of subreddits the current user is subscribed to.
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    pub async fn subsriptions(&self) -> Result<Vec<crate::model::subreddit::Subreddit>> {
        let base_url = self.base_url().await;
        let url = base_url
            .join("subreddits/mine/subscriber.json")
            .expect("Should not fail.");
        self.collect_paged(&url).await
    }

    /// Get the list of multireddits the current user is subscribed to.
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    pub async fn multis(&self) -> Result<Vec<crate::model::multi::Multi>> {
        let base_url = self.base_url().await;
        let url = base_url
            .join("api/multi/mine.json")
            .expect("Should not fail.");
        self.collect_paged(&url).await
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

        let url = self
            .base_url()
            .await
            .join("api/vote")
            .expect("Should not fail.");
        let request = self.post(url);
        let request = request.query(&[("id", thing.as_ref()), ("dir", &format!("{dir}"))]);
        let _ = self.execute(request).await?;
        Ok(())
    }

    /// Save a saveable item (i.e. a [`Post`] or a [`Comment`]).
    /// # Errors
    /// Returns an error if the request failed.
    pub async fn save(&self, thing: &Fullname) -> Result<()> {
        let url = self
            .base_url()
            .await
            .join("api/save")
            .expect("Should not fail.");
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
            .await
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
    pub fn saved(&self, username: String) -> impl Stream<Item = Result<Thing>> + Send + Sync {
        Wrapper::new(SaveFeed {
            client: self.clone(),
            username,
        })
    }
}

struct SaveFeed {
    client: Client,
    username: String,
}

impl ListingStream for SaveFeed {
    type Output = Thing;

    fn fetch_next(
        &self,
        pager: &Pager,
    ) -> std::pin::Pin<Box<dyn Future<Output = Result<Listing>> + Send + Sync>> {
        let client = self.client.clone();
        let pager = pager.clone();
        let username = self.username.clone();
        let fut = async move {
            let url = client
                .base_url()
                .await
                .join(&format!("user/{username}/saved.json"))
                .expect("Should not fail.");
            client.paged_listing(&url, &pager).await
        };
        Box::pin(fut)
    }
}
