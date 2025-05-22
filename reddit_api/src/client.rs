use crate::model::Fullname;
use crate::model::feed::Feed;
use crate::{model::feed::FeedStream, result::Result};
use std::sync::Arc;

use base64::{
    Engine,
    prelude::{BASE64_STANDARD, BASE64_URL_SAFE_NO_PAD},
};
use chrono::{DateTime, Utc};
use futures::{Stream, lock::Mutex};
use log::*;
use reqwest::{
    Request, RequestBuilder,
    header::{self, HeaderMap, HeaderValue},
};

pub use reqwest::{IntoUrl, Url};
use serde::{
    Deserialize,
    de::{DeserializeOwned, Error as _},
};

use crate::{
    error::Error,
    model::{Listing, Sort, Thing, post::Post, user::UserInfo},
};

const USER_AGENT: &str = "com.sofamaniac.reboost";
const CLIENT_ID: &str = "w0DROpe2H7uv2inVTvSfZw";

#[derive(Clone, Debug)]
pub struct Client {
    client: Arc<Mutex<reqwest::Client>>,
    refresh_token: Arc<Mutex<Option<String>>>,
    current_access_token: Arc<Mutex<Option<String>>>,
}

impl Default for Client {
    fn default() -> Self {
        Self::new_anonymous()
    }
}

pub enum VoteDirection {
    Up,
    Down,
    Neutral,
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

/// Basic auth string
fn bearer_token() -> String {
    let token = BASE64_STANDARD.encode(format!("{CLIENT_ID}:"));
    format!("Basic {token}")
}

impl Client {
    fn new_reqwest_client(token: Option<&str>) -> reqwest::Client {
        let mut headers = HeaderMap::new();
        let bearer_token = match token {
            None => bearer_token(),
            Some(token) => format!("Bearer {token}"),
        };
        debug!("{bearer_token}");
        headers.insert(
            header::AUTHORIZATION,
            HeaderValue::from_str(&bearer_token).unwrap(),
        );
        reqwest::Client::builder()
            .user_agent(USER_AGENT)
            .default_headers(headers)
            .build()
            .unwrap()
    }
    /// Create a new client for a logged out user
    #[must_use]
    pub fn new_anonymous() -> Self {
        Self {
            client: Arc::new(Mutex::new(Self::new_reqwest_client(None))),
            refresh_token: Arc::new(Mutex::new(None)),
            current_access_token: Arc::new(Mutex::new(None)),
        }
    }
    /// Create a new client that is authenticated
    #[must_use]
    pub fn new_user(access_token: String, refresh_token: String) -> Self {
        Self {
            client: Arc::new(Mutex::new(Self::new_reqwest_client(Some(&access_token)))),
            refresh_token: Arc::new(Mutex::new(Some(refresh_token))),
            current_access_token: Arc::new(Mutex::new(Some(access_token))),
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
    pub async fn authenticate(&self, access_token: String, refresh_token: String) {
        *self.current_access_token.lock().await = Some(access_token.clone());
        *self.client.lock().await = Self::new_reqwest_client(Some(&access_token));
        *self.refresh_token.lock().await = Some(refresh_token);
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
    let text = response.text().await?;
    match serde_json::from_str(&text) {
        Ok(json) => Ok(json),
        Err(e) => Err(Error::Parsing(serde_json::Error::custom(format!(
            "serde_json error {e}. Failed to parse this post: {text}"
        )))),
    }
}

impl Client {
    async fn get(&self, url: impl IntoUrl) -> RequestBuilder {
        self.client
            .lock()
            .await
            .get(url)
            .query(&[("raw_json", "1")])
    }
    async fn post(&self, url: impl IntoUrl) -> RequestBuilder {
        self.client.lock().await.post(url)
    }

    /// Check if token has expired
    /// # Errors
    /// Returns an error if the token is not in proper base64 or if it does not contain the `exp`
    /// field.
    async fn is_access_token_valid(&self) -> Result<bool> {
        #[derive(Deserialize)]
        struct Claims {
            exp: f64,
        }
        if let Some(token) = &*self.current_access_token.lock().await {
            let parts: Vec<&str> = token.splitn(3, '.').collect();
            let payload_bytes = BASE64_URL_SAFE_NO_PAD.decode(parts[1])?;
            let claims: Claims = serde_json::from_slice(&payload_bytes)?;
            #[allow(clippy::cast_possible_truncation)]
            let date = DateTime::from_timestamp_millis((claims.exp * 1000.0) as i64).unwrap();
            debug!("Current token is valid until ${date}");
            Ok(date >= Utc::now())
        } else {
            debug!("Access token is not set");
            Ok(true)
        }
    }

    /// Get a new access token
    async fn refresh_token(&self) -> Result<()> {
        #[derive(Deserialize)]
        struct Response {
            access_token: String,
        }
        info!("[CLIENT] Refreshing token");
        let refresh_token = self.refresh_token.lock().await;
        if refresh_token.is_none() {
            return Ok(());
        }
        let refresh_token = refresh_token.clone().unwrap();
        let url = Url::parse("https://reddit.com/").expect("Should not panic.");
        let url = url.join("api/v1/access_token").expect("Should not fail.");
        let request = self.post(url).await;
        // When refreshing the token we must use the same authorization header as during the
        // initial negociation.
        let request = request.header(
            header::AUTHORIZATION,
            HeaderValue::from_str(&bearer_token()).unwrap(),
        );
        let body = format!("grant_type=refresh_token&refresh_token={refresh_token}");
        let request = request.body(body).build().unwrap();
        debug!("{request:#?}");
        let response = self.client.lock().await.execute(request).await?;
        //let response: Response = response.json().await?;
        let text = response.text().await.unwrap();
        debug!("{text}");
        let response: Response = serde_json::from_str(&text)?;
        self.authenticate(response.access_token, refresh_token)
            .await;
        info!("[CLIENT] Token has been refreshed.");
        Ok(())
    }

    async fn execute(&self, request: Request) -> Result<reqwest::Response> {
        debug!("Executing {request:?}");
        if !self.is_access_token_valid().await? {
            self.refresh_token().await?;
        }
        self.client
            .lock()
            .await
            .execute(request)
            .await
            .map_err(std::convert::Into::into)
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
        let request = self.get(url).await;
        let posts = self.execute(request.build().unwrap()).await?;
        let json = parse_response(posts).await?;
        match json {
            Thing::Listing(listing) => Ok(listing),
            _ => Err(Error::InvalidThing),
        }
    }

    /// Get the info of the current user.
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    pub async fn logged_user_info(&self) -> Result<UserInfo> {
        let base_url = self.base_url().await;
        let url = base_url.join("api/v1/me.json").expect("Should not fail");
        let request = self.get(url).await;
        let response = self.execute(request.build().unwrap()).await?;
        let json = parse_response(response).await?;
        Ok(json)
    }

    /// Get a stream of posts for the specified feed with the specified sort.
    /// Yield an error when the request to reddit API failed.
    pub fn feed(&self, feed: Feed, sort: Sort) -> impl Stream<Item = Result<Post>> + Send + Sync {
        FeedStream::new(self, feed, sort)
    }

    /// Get one page of a listing.
    async fn paged_request<T: TryFrom<Thing>>(
        &self,
        url: &Url,
        pager: &Pager,
    ) -> Result<(Vec<T>, Pager)> {
        let url = pager.add_to_url(url.clone());
        let request = self.get(url).await;
        let response = self.execute(request.build().unwrap()).await?;
        let thing: Thing = parse_response(response).await?;
        let listing: Listing = thing.try_into()?;
        let mut pager = Pager::default();
        pager.after(listing.after);
        let things = listing
            .children
            .into_iter()
            .filter_map(|e| e.try_into().ok())
            .collect();
        Ok((things, pager))
    }

    /// Try to collect everything from a paged request. Will continue until there is no
    /// more item to collect.
    async fn collect_paged<T: TryFrom<Thing>>(&self, url: &Url) -> Result<Vec<T>> {
        let mut things = Vec::new();
        let pager = Pager::default();
        loop {
            let (mut page_things, pager) = self.paged_request(url, &pager).await?;
            things.append(&mut page_things);
            if pager.after.is_none() {
                break;
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
    pub async fn vote(&self, thing: Fullname, direction: VoteDirection) -> Result<()> {
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
        let request = self.post(url).await;
        let request = request.query(&[("id", thing.as_ref()), ("dir", &format!("{dir}"))]);
        let _ = self
            .execute(request.build().expect("Should not fail."))
            .await?;
        Ok(())
    }

    /// Save a saveable item (i.e. a [`Post`] or a [`Comment`]).
    /// # Errors
    /// Returns an error if the request failed.
    pub async fn save(&self, thing: Fullname) -> Result<()> {
        let url = self
            .base_url()
            .await
            .join("api/save")
            .expect("Should not fail.");
        let request = self.post(url).await;
        let request = request.query(&[("id", &thing)]);
        let _ = self
            .execute(request.build().expect("Should not fail."))
            .await?;
        Ok(())
    }

    /// Unsave a saveable item (i.e. a [`Post`] or a [`Comment`]).
    /// # Errors
    /// Returns an error if the request failed.
    pub async fn unsave(&self, thing: Fullname) -> Result<()> {
        let url = self
            .base_url()
            .await
            .join("api/unsave")
            .expect("Should not fail.");
        let request = self.post(url).await;
        let request = request.query(&[("id", &thing)]);
        let _ = self
            .execute(request.build().expect("Should not fail."))
            .await?;
        Ok(())
    }
}
