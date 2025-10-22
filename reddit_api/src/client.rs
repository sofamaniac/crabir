use crate::model::comment::Comment;
use crate::model::feed::{self, Feed};
use crate::model::flair::Flair;
use crate::model::multi::{Multi, MultiStream};
use crate::model::post::PostSubmit;
use crate::model::rule::Rule;
use crate::model::subreddit::Subreddit;
use crate::model::{Fullname, Post, comment};
use crate::paging_handler::PagingHandler;
use crate::result::Result;
use crate::search::{PostSearchSort, SearchPost, SearchSubreddit, SubredditSearchSort};
use crate::votable::private::PrivateVotable;
use std::collections::{HashMap, HashSet};
use std::sync::Arc;
use std::time::Duration;

use base64::Engine;
use base64::prelude::BASE64_URL_SAFE_NO_PAD;
use chrono::{DateTime, Utc};
use flutter_rust_bridge::frb;
use futures::Stream;
use futures::{TryStreamExt, stream};
use log::{debug, error, info};
use reqwest::RequestBuilder;

pub use reqwest::{IntoUrl, Url};
use serde::Serialize;
use serde::{
    Deserialize,
    de::{DeserializeOwned, Error as _},
};
use std::sync::RwLock;
use tracing::instrument;
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
    refresh_token: Arc<RwLock<Option<String>>>,
    current_access_token: Arc<RwLock<Option<String>>>,
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
            .timeout(Duration::from_secs(30))
            .build()
            .expect("Building reqwest client should not fail")
    }
    /// Create a new client for a logged out user
    #[must_use]
    pub fn new_anonymous() -> Self {
        Self {
            http: Self::new_reqwest_client(),
            refresh_token: Arc::new(RwLock::new(None)),
            current_access_token: Arc::new(RwLock::new(None)),
        }
    }
    /// Create a new client that is authenticated
    #[must_use]
    pub fn from_refresh_token(refresh_token: String) -> Self {
        Self {
            http: Self::new_reqwest_client(),
            refresh_token: Arc::new(RwLock::new(Some(refresh_token))),
            current_access_token: Arc::new(RwLock::new(Some(String::new()))),
        }
    }

    pub(crate) fn base_url(&self) -> Url {
        Url::parse("https://oauth.reddit.com/").expect("Should not panic.")
    }

    pub(crate) fn join_url(&self, url: &str) -> Url {
        self.base_url().join(url).expect("Should not fail.")
    }

    /// Authenticate the current client
    pub async fn authenticate(&self, refresh_token: String) {
        *self.refresh_token.write().unwrap() = Some(refresh_token);
        *self.current_access_token.write().unwrap() = Some(String::new());
    }
}

const MAX_LIMIT: u64 = 100;

#[derive(Debug, Clone)]
pub struct Pager {
    after: Option<String>,
    before: Option<String>,
    limit: u64,
}

impl Default for Pager {
    fn default() -> Self {
        Self {
            after: Some(String::new()),
            before: None,
            limit: 100,
        }
    }
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
                        e.column().saturating_sub(1000),
                        e.column().saturating_add(1000),
                        &text[e.column().saturating_sub(1000)..e.column().saturating_add(1000)]
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

pub(crate) async fn parse_thing<T>(response: reqwest::Response) -> Result<T>
where
    T: TryFrom<Thing, Error = Error>,
{
    let thing: Thing = parse_response(response).await?;
    thing.try_into()
}

impl Client {
    pub(crate) fn get(&self, url: impl IntoUrl) -> RequestBuilder {
        self.http.get(url).query(&[("raw_json", "1")])
    }
    fn post(&self, url: impl IntoUrl) -> RequestBuilder {
        self.http.post(url)
    }

    /// Check if token has expired
    fn is_access_token_valid(&self) -> bool {
        #[derive(Deserialize)]
        struct Claims {
            exp: f64,
        }
        let token = self.current_access_token.read().unwrap().clone();
        if let Some(token) = token {
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
        *self.current_access_token.write().unwrap() = Some(response.access_token);
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
        let refresh_token = self.refresh_token.read().unwrap().clone();
        let refresh_token = match refresh_token {
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
        *self.current_access_token.write().unwrap() = Some(response.access_token);
        info!("[CLIENT] Token has been refreshed.");
        Ok(())
    }

    pub async fn logout(&self) -> Result<()> {
        let access_token = self.current_access_token.read().unwrap().clone();
        if let Some(access_token) = access_token {
            let url = self.join_url("api/v1/revoke_token");
            let request = self
                .http
                .post(url)
                .basic_auth::<&str, &str>(CLIENT_ID, None)
                .query(&[
                    ("token", access_token),
                    ("token_type_hint", String::from("access_token")),
                ])
                .build()
                .expect("Building request should not fail");
            let res = self.http.execute(request).await?.error_for_status();
            if res.is_err() {
                log::error!("Failed to loguout: {res:#?}");
                res?;
            }
            log::info!("[logout] Successfully logged out");
            // self.new_logged_out_user_token().await
            Ok(())
        } else {
            log::info!("[logout] The access_token was null.");
            Ok(())
        }
    }

    pub(crate) async fn execute(&self, request: RequestBuilder) -> Result<reqwest::Response> {
        if !self.is_access_token_valid() {
            self.refresh_token().await?;
        }
        let access_token = self.current_access_token.read().unwrap().clone();
        let request = if let Some(access_token) = access_token {
            request.bearer_auth(access_token)
        } else {
            request.basic_auth::<&str, &str>(CLIENT_ID, None)
        };
        debug!("Executing {request:#?}");
        let request = request.build().expect("Building request should not fail");
        let url = request.url().clone();
        let res = self.http.clone().execute(request).await;
        let res = match res {
            Ok(res) => res,
            Err(err) => {
                log::error!("Error while executing request ({}) : {:#?}", url, err);
                return Err(err.into());
            }
        };
        res.error_for_status().map_err(Into::into)
    }

    #[frb(sync)]
    pub fn feed_stream(&self, feed: &Feed, sort: &feed::FeedSort) -> PagingHandler {
        let base_url = self.base_url();
        PagingHandler::new(feed::FeedStream::new(
            self.clone(),
            feed.clone(),
            *sort,
            base_url,
        ))
    }

    /// Get the info of the current user.
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    #[instrument]
    pub async fn logged_user_info(&self) -> Result<UserInfo> {
        let url = self.join_url("api/v1/me.json");
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
            move |(base_url, pager)| async move {
                // Return None if after is None
                pager.after.as_ref()?;
                let client = self.clone();
                let url = pager.add_to_url(base_url.clone());
                let request = client.get(url.clone());
                let request = request.query(query_params);
                let response = client.execute(request).await;
                match response {
                    Err(e) => Some((Err(e), (base_url, Pager::default()))),
                    Ok(response) => match parse_response::<Thing>(response).await {
                        Ok(thing) => match thing {
                            Thing::Listing(listing) => {
                                let pager = Pager {
                                    after: listing.after.clone(),
                                    ..pager
                                };
                                Some((Ok(listing), (base_url, pager)))
                            }
                            _ => Some((Err(Error::InvalidThing), (url, Pager::default()))),
                        },
                        Err(e) => Some((Err(e), (base_url, Pager::default()))),
                    },
                }
            },
        )
    }

    pub(crate) fn stream_vec<Q>(
        self,
        url: Url,
        pager: Option<Pager>,
        query_params: Q,
    ) -> impl Stream<Item = Result<Vec<Thing>>>
    where
        Q: Serialize + Clone,
    {
        struct Util<Q: Serialize + Clone> {
            url: Url,
            pager: Pager,
            query_params: Q,
            seen: HashSet<Fullname>,
            done: bool,
        }

        let init = Util {
            url,
            pager: pager.unwrap_or_default(),
            query_params,
            seen: HashSet::new(),
            done: false,
        };
        stream::unfold(init, move |mut acc| {
            let client = self.clone();
            let query_params = acc.query_params.clone();
            async move {
                if !acc.done {
                    let url = acc.pager.add_to_url(acc.url.clone());
                    let request = client.get(url.clone());
                    let request = request.query(&query_params);
                    let response = client.execute(request).await;
                    match response {
                        Err(e) => Some((Err(e), acc)),
                        Ok(response) => match parse_thing::<Listing>(response).await {
                            Ok(listing) => {
                                acc.pager.after(listing.after.clone());
                                acc.done = listing.after.is_none()
                                    || listing.after.is_some_and(|after| after.is_empty());
                                let buffer: Vec<Thing> = listing
                                    .children
                                    .into_iter()
                                    .filter(|thing| {
                                        // Keep things that either do not have a name
                                        // or that were not already seen
                                        thing.name().is_none_or(|name| acc.seen.insert(name))
                                    })
                                    .collect();

                                Some((Ok(buffer), acc))
                            }
                            Err(e) => Some((Err(e), acc)),
                        },
                    }
                } else {
                    None
                }
            }
        })
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
    #[instrument]
    pub async fn subscriptions(&self) -> Result<Vec<crate::model::subreddit::Subreddit>> {
        let url = self.join_url("subreddits/mine/subscriber.json");
        let result: Vec<Subreddit> = self.collect_paged(&url, &[]).await?;
        Ok(result)
    }

    /// Subscribe to a subreddit
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    #[instrument]
    pub async fn subscribe(&self, name: &Fullname) -> Result<()> {
        let url = self.join_url("api/subscribe");
        let request = self.post(url);
        let request = request.query(&[
            ("action", "sub"),
            ("action_source", "onboarding"),
            ("skip_initial_defaults", "true"),
            ("sr", name.as_ref()),
        ]);
        self.execute(request).await?;
        Ok(())
    }

    /// Unsubscribe to a subreddit
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    #[instrument]
    pub async fn unsubscribe(&self, name: &Fullname) -> Result<()> {
        let url = self.join_url("api/subscribe");
        let request = self.post(url);
        let request = request.query(&[
            ("action", "unsub"),
            ("action_source", "onboarding"),
            ("sr", name.as_ref()),
        ]);
        self.execute(request).await?;
        Ok(())
    }

    /// Get html of wiki index of subreddit `name` (e.g: unixporn).
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    #[instrument]
    pub async fn wiki(&self, name: String) -> Result<String> {
        let url = self.join_url(&format!("r/{name}/wiki.json"));
        let request = self.get(url);
        let result = self.execute(request).await?;
        if let Thing::Wikipage { content_html } = parse_response(result).await? {
            Ok(content_html)
        } else {
            Err(Error::InvalidThing)
        }
    }

    /// Get the rules of a subreddit by its display name (e.g.: unixporn).
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    #[instrument]
    pub async fn rules(&self, name: String) -> Result<Vec<Rule>> {
        #[derive(Deserialize)]
        struct Response {
            rules: Vec<Rule>,
        }
        let url = self.join_url(&format!("r/{name}/about/rules.json"));
        let request = self.get(url);
        let result = self.execute(request).await?;
        let Response { rules } = parse_response(result).await?;
        Ok(rules)
    }

    /// Report an `Thing`
    /// # Errors
    /// Returns an error if the http client fails or if the request fails.
    #[instrument]
    pub async fn report(&self, name: Fullname, reason: String) -> Result<()> {
        let url = self.join_url("api/report");
        let request = self.post(url);
        let mut params = HashMap::new();
        params.insert("api_type", "json");
        params.insert("thing_id", name.as_ref());
        params.insert("reason", &reason);
        let request = request.form(&params);
        self.execute(request).await?;
        Ok(())
    }

    /// Get the list of multireddits the current user is subscribed to.
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    #[instrument]
    pub async fn multis(&self) -> Result<Vec<crate::model::multi::Multi>> {
        let url = self.join_url("api/multi/mine.json");
        let request = self.get(url).query(&[("expand_srs", "true")]);
        let result = self.execute(request).await?;
        Ok(parse_response::<Vec<Thing>>(result)
            .await?
            .into_iter()
            .filter_map(|t| TryInto::<Multi>::try_into(t).ok())
            .collect())
    }

    /// Get a multi from its link (`/user/{username}/m/{name}`).
    /// # Errors
    /// Returns an error if the http client fails or if the parsing of the response fails.
    #[instrument]
    pub async fn get_multi(&self, multi_path: &str) -> Result<Multi> {
        let url = self
            .join_url("api/multi")
            .join(multi_path)
            .expect("[get_multi] Could not parse url.");
        let request = self.get(url);
        let result = self.execute(request).await?;
        parse_thing(result).await
    }

    #[frb(sync)]
    pub fn multi_posts(&self, multi: &Multi, sort: &feed::FeedSort) -> PagingHandler {
        let base_url = self.base_url();
        PagingHandler::new(MultiStream::new(
            self.clone(),
            multi.clone(),
            *sort,
            base_url,
        ))
    }

    /// Vote on a votable item (i.e. a [`Post`] or a [`comment::Comment`]).
    /// # Errors
    /// Returns an error if the request failed.
    #[instrument]
    pub(crate) async fn vote(&self, fullname: &Fullname, direction: VoteDirection) -> Result<()> {
        let dir = match direction {
            VoteDirection::Up => 1,
            VoteDirection::Down => -1,
            VoteDirection::Neutral => 0,
        };

        let url = self.join_url("api/vote");
        let request = self.post(url);
        let request = request.query(&[("id", fullname.as_ref()), ("dir", &format!("{dir}"))]);
        let _ = self.execute(request).await?;
        Ok(())
    }

    /// Save a saveable item (i.e. a [`Post`] or a [`comment::Comment`]).
    /// # Errors
    /// Returns an error if the request failed.
    #[instrument]
    pub(crate) async fn save(&self, thing: &Fullname) -> Result<()> {
        let url = self.join_url("api/save");
        let request = self.post(url);
        let request = request.query(&[("id", &thing)]);
        let _ = self.execute(request).await?;
        Ok(())
    }

    /// Unsave a saveable item (i.e. a [`Post`] or a [`comment::Comment`]).
    /// # Errors
    /// Returns an error if the request failed.
    #[instrument]
    pub(crate) async fn unsave(&self, thing: &Fullname) -> Result<()> {
        let url = self.join_url("api/unsave");
        let request = self.post(url);
        let request = request.query(&[("id", &thing)]);
        let _ = self.execute(request).await?;
        Ok(())
    }

    /// Hide a [`Post`]
    /// # Errors
    /// Returns an error if the request failed.
    #[instrument]
    pub(crate) async fn hide(&self, post: &Post) -> Result<()> {
        let url = self.join_url("api/hide");
        let request = self.post(url);
        let request = request.query(&[("id", &post.name().as_ref())]);
        let _ = self.execute(request).await?;
        Ok(())
    }

    /// Unhide a [`Post`]
    /// # Errors
    /// Returns an error if the request failed.
    #[instrument]
    pub(crate) async fn unhide(&self, post: &Post) -> Result<()> {
        let url = self.join_url("api/unhide");
        let request = self.post(url);
        let request = request.query(&[("id", &post.name().as_ref())]);
        let _ = self.execute(request).await?;
        Ok(())
    }

    /// Returns the comments for the post at the given permalink. Each element in the vec is either
    /// a [`Thing::Comment`] or a [`Thing::More`].
    /// # Errors
    /// Fails if the request fails or the parsing of the response fails.
    #[instrument]
    pub async fn comments(
        &self,
        permalink: String,
        sort: Option<comment::CommentSort>,
    ) -> Result<(Post, Vec<Thing>)> {
        let url = self.join_url(&format!("{permalink}.json"));
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
    #[instrument]
    pub async fn get_post(&self, permalink: String) -> Result<Post> {
        self.comments(permalink, None).await.map(|r| r.0)
    }

    #[instrument]
    pub async fn load_more_comments(
        &self,
        parent_id: &Fullname,
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
        let url = self.join_url("api/morechildren.json");
        let children: Vec<String> = children.into_iter().take(100).collect();
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

    #[frb(sync)]
    pub fn search_post(
        &self,
        subreddit: Option<String>,
        flair: Option<Flair>,
        query: Option<String>,
        sort: PostSearchSort,
    ) -> PagingHandler {
        PagingHandler::new(Box::new(SearchPost::new(
            self.clone(),
            subreddit,
            flair,
            query,
            sort,
        )))
    }

    #[frb(sync)]
    pub fn search_subreddits(&self, query: String, sort: SubredditSearchSort) -> PagingHandler {
        PagingHandler::new(Box::new(SearchSubreddit::new(self.clone(), query, sort)))
    }

    /// Get info on subreddit at 'r/`subreddit`/about'.
    #[instrument]
    pub async fn subreddit_about(&self, subreddit: String) -> Result<Subreddit> {
        let url = self.join_url(&format!("r/{subreddit}/about.json"));
        let request = self.get(url);
        let response = self.execute(request).await?;
        parse_thing(response).await
    }

    /// (un)favorite subreddit
    #[instrument]
    pub async fn favorite(&self, subreddit: &str, make_favorite: bool) -> Result<()> {
        let url = self.join_url("api/favorite");
        let request = self.post(url).query(&[
            ("sr_name", subreddit),
            ("make_favorite", &make_favorite.to_string()),
        ]);
        self.execute(request).await?;
        Ok(())
    }

    /// Submit a comment
    #[instrument]
    pub async fn submit_comment(
        &self,
        parent: &Fullname,
        body: &str,
        depth: i32,
    ) -> Result<Comment> {
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
        let url = self.join_url("api/comment");
        let request = self.post(url).form(&[
            ("api_type", "json"),
            ("text", body),
            ("thing_id", parent.as_ref()),
            ("raw_json", "1"),
        ]);
        let res = self.execute(request).await?;
        let response: Response = parse_response(res).await?;
        let thing = response
            .json
            .data
            .things
            .first()
            .ok_or(Error::Custom {
                message: "No comment in answer".to_owned(),
            })?
            .clone();
        let mut comment: Comment = thing.try_into()?;
        comment.set_depth(depth);
        Ok(comment)
    }

    /// Resolve short link. `link` should be a valid url.
    #[instrument]
    pub async fn resolve_short_link(&self, link: &str) -> Result<String> {
        let request = self
            .http
            .get(Url::parse(link).expect("Should not fail."))
            .build()
            .expect("Should not fail");
        let response = self.http.execute(request).await?;
        let url = response.url().to_string();
        Ok(url)
    }

    /// Create post
    #[instrument]
    pub async fn submit_post(&self, post: PostSubmit) -> Result<()> {
        let url = self.join_url("api/submit");
        let request = self.post(url);
        let request = request.form(&post);
        let _ = self.execute(request).await?;
        Ok(())
    }
}
