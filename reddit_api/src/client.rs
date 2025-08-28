use crate::model::feed::{self, Feed};
use crate::model::flair::Flair;
use crate::model::multi::{Multi, MultiStream};
use crate::model::{Fullname, Post, comment};
use crate::result::Result;
use crate::search::{PostSearchSort, SearchPost, SearchSubreddit, SubredditSearchSort};
use crate::streamable::Streamable;
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
use serde::Serialize;
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

    pub(crate) fn base_url(&self) -> Url {
        Url::parse("https://oauth.reddit.com/").expect("Should not panic.")
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

pub(crate) async fn parse_response<T: DeserializeOwned>(response: reqwest::Response) -> Result<T> {
    let response = response.error_for_status();
    match response {
        Ok(response) => {
            let text = response.text().await?;
            match serde_json::from_str(&text) {
                Ok(json) => Ok(json),
                Err(e) => {
                    error!("Error while parsing {e}");
                    error!(
                        "Context column ({} to {}): {}",
                        e.column().saturating_sub(2000),
                        e.column().saturating_add(1000),
                        &text[e.column().saturating_sub(2000)..e.column().saturating_add(1000)]
                    );
                    Err(Error::Parsing {
                        source: serde_json::Error::custom(format!(
                            "serde_json error {e}. Failed to parse : {text}"
                        )),
                    })
                }
            }
        }
        Err(error) => {
            error!("{error}");
            Err(Error::Reqwest { source: error })
        }
    }
}

impl Client {
    pub(crate) fn get(&self, url: impl IntoUrl) -> RequestBuilder {
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

    pub(crate) async fn execute(&self, request: RequestBuilder) -> Result<reqwest::Response> {
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
                            _ => Some((Err(Error::InvalidThing), (url, Pager::default()))),
                        },
                        Err(e) => Some((Err(e), (url, Pager::default()))),
                    },
                }
            },
        )
    }

    pub(crate) fn stream_vec<T, Q>(
        self,
        url: Url,
        pager: Option<Pager>,
        query_params: Q,
    ) -> impl Stream<Item = Result<T>>
    where
        T: TryFrom<Thing>,
        Q: Serialize + Clone,
    {
        stream::unfold(
            (url, pager.unwrap_or_default(), Vec::new(), false),
            move |(url, mut pager, mut buffer, done)| {
                let client = self.clone();
                let query_params = query_params.clone();
                async move {
                    if let Some(thing) = buffer.pop() {
                        Some((Ok(thing), (url, pager, buffer, done)))
                    } else if !done {
                        let url = pager.add_to_url(url.clone());
                        let request = client.get(url.clone());
                        let request = request.query(&query_params);
                        let response = client.execute(request).await;
                        match response {
                            Err(e) => Some((Err(e), (url, Pager::default(), buffer, done))),
                            Ok(response) => match parse_response::<Thing>(response).await {
                                Ok(thing) => match thing {
                                    Thing::Listing(listing) => {
                                        pager.after(listing.after.clone());
                                        let done = listing.after.is_none()
                                            || listing.after.is_some_and(|after| after.is_empty());
                                        buffer = listing
                                            .children
                                            .into_iter()
                                            .filter_map(|item| item.try_into().ok())
                                            .collect();
                                        buffer.reverse();

                                        buffer
                                            .pop()
                                            .map(|thing| (Ok(thing), (url, pager, buffer, done)))
                                    }
                                    _ => Some((
                                        Err(Error::InvalidThing),
                                        (url, Pager::default(), buffer, done),
                                    )),
                                },
                                Err(e) => Some((Err(e), (url, Pager::default(), buffer, done))),
                            },
                        }
                    } else {
                        None
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
        let request = self.get(url);
        //let request = self.get(url).query(&[("expand_srs", "true")]);
        let result = self.execute(request).await?;
        Ok(parse_response::<Vec<Thing>>(result)
            .await?
            .into_iter()
            .filter_map(|t| TryInto::<Multi>::try_into(t).ok())
            .collect())
    }

    ///flutter_rust_bridge:sync
    pub fn multi_posts(&self, multi: &Multi, sort: &feed::FeedSort) -> Streamable {
        let base_url = self.base_url();
        Streamable::new(MultiStream::new(
            self.clone(),
            multi.clone(),
            *sort,
            base_url,
        ))
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

    /// Returns the comments for the post at the given permalink. Each element in the vec is either
    /// a [`Thing::Comment`] or a [`Thing::More`].
    /// # Errors
    /// Fails if the request fails or the parsing of the response fails.
    pub async fn comments(
        &self,
        permalink: String,
        sort: Option<comment::CommentSort>,
    ) -> Result<(Post, Vec<Thing>)> {
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
        if let [Thing::Listing(post), Thing::Listing(comments)] = result {
            match post.children.first() {
                Some(Thing::Post(post)) => Ok((post.clone(), comments.children)),
                _ => Err(Error::InvalidThing),
            }
        } else {
            Err(Error::InvalidThing)
        }
    }
    /// Returns the content of the post at the given permalink.
    /// # Errors
    /// Fails if the request fails or the parsing of the response fails.
    pub async fn get_post(&self, permalink: String) -> Result<Post> {
        self.comments(permalink, None).await.map(|r| r.0)
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

    ///flutter_rust_bridge:sync
    pub fn search_post(
        &self,
        subreddit: Option<String>,
        flair: Option<Flair>,
        query: Option<String>,
        sort: PostSearchSort,
    ) -> Streamable {
        Streamable::new(Box::new(SearchPost::new(
            self.clone(),
            subreddit,
            flair,
            query,
            sort,
        )))
    }

    ///flutter_rust_bridge:sync
    pub fn search_subreddits(&self, query: String, sort: SubredditSearchSort) -> Streamable {
        Streamable::new(Box::new(SearchSubreddit::new(self.clone(), query, sort)))
    }
}
