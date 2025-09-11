use flutter_rust_bridge::frb;
use futures::Stream;

use crate::{
    client::{Client, Pager, parse_response},
    model::Thing,
    result::Result,
    streamable::Streamable,
};

use super::{
    model::{User, UserInfo, UserStreamSort},
    streams::{UserStream, UserStreamSorted},
};

impl Client {
    pub(super) fn user_stream<T: TryFrom<Thing>>(
        self,
        endpoint: String,
        pager: Option<Pager>,
    ) -> impl Stream<Item = Result<T>> {
        let url = self
            .base_url()
            .join(&endpoint)
            .expect("Should not fail to build url.");
        self.clone().stream_vec(url, pager, ())
    }
    pub(super) fn sorted_user_stream<T: TryFrom<Thing>>(
        self,
        endpoint: String,
        sort: UserStreamSort,
        pager: Option<Pager>,
    ) -> impl Stream<Item = Result<T>> {
        let mut url = self
            .base_url()
            .join(&endpoint)
            .expect("Should not fail to build url.");
        for (name, value) in sort.to_query() {
            url.query_pairs_mut().append_pair(name, value);
        }
        self.stream_vec(url, pager, ())
    }
    /// Get saved items ( both [`Post`] and [`Comment`] ) for the specified user.
    /// # Errors
    /// Fails if api request fails.
    #[frb(sync)]
    pub fn user_saved(&self, username: String) -> Streamable {
        Streamable::new(UserStream::new(
            self.clone(),
            username,
            String::from("saved"),
        ))
    }

    #[frb(sync)]
    pub fn user_overview(&self, username: String, sort: UserStreamSort) -> Streamable {
        Streamable::new(UserStreamSorted::new(
            self.clone(),
            username,
            sort,
            String::from("overview"),
        ))
    }
    #[frb(sync)]
    pub fn user_submitted(&self, username: String, sort: UserStreamSort) -> Streamable {
        Streamable::new(UserStreamSorted::new(
            self.clone(),
            username,
            sort,
            String::from("submitted"),
        ))
    }
    #[frb(sync)]
    pub fn user_comments(&self, username: String, sort: UserStreamSort) -> Streamable {
        Streamable::new(UserStreamSorted::new(
            self.clone(),
            username,
            sort,
            String::from("comments"),
        ))
    }
    #[frb(sync)]
    pub fn user_upvoted(&self, username: String) -> Streamable {
        Streamable::new(UserStream::new(
            self.clone(),
            username,
            String::from("upvoted"),
        ))
    }
    #[frb(sync)]
    pub fn user_downvoted(&self, username: String) -> Streamable {
        Streamable::new(UserStream::new(
            self.clone(),
            username,
            String::from("downvoted"),
        ))
    }
    #[frb(sync)]
    pub fn user_hidden(&self, username: String) -> Streamable {
        Streamable::new(UserStream::new(
            self.clone(),
            username,
            String::from("hidden"),
        ))
    }
    #[frb(sync)]
    pub fn user_gilded(&self, username: String) -> Streamable {
        Streamable::new(UserStream::new(
            self.clone(),
            username,
            String::from("gilded"),
        ))
    }

    pub async fn user_about(&self, username: String) -> Result<UserInfo> {
        let url = self
            .base_url()
            .join(&format!("user/{username}/about.json"))
            .expect("Should not fail.");
        let request = self.get(url);
        let result = self.execute(request).await?;
        let result: Thing = parse_response(result).await?;
        let user: User = result.try_into()?;
        Ok(user.info)
    }
}
