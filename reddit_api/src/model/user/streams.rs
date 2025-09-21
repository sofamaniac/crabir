//! This file contains the streams for each tabs of a user profile.
// Copyright (c) 2025 sofamaniac. All Rights Reserved.

use super::model::UserStreamSort;
use crate::streamable::stream::IntoStreamPrivate;
use crate::{client::Client, model::Thing, result::Result};
use futures::stream::BoxStream;
pub use futures::{Stream, StreamExt};

#[derive(Clone)]
pub struct UserStream {
    username: String,
    endpoint: String,
    client: Client,
}
impl UserStream {
    pub(crate) fn new(client: Client, username: String, endpoint: String) -> Box<Self> {
        Box::new(Self {
            client,
            username,
            endpoint,
        })
    }
    fn url(&self) -> String {
        format!("user/{}/{}.json", self.username, self.endpoint)
    }
}
impl IntoStreamPrivate for UserStream {
    type Output = Vec<Thing>;
    fn to_stream(&self) -> BoxStream<'static, Result<Vec<Thing>>> {
        let client = self.client.clone();
        client.user_stream(self.url(), None).boxed()
    }
}
impl PartialEq for UserStream {
    fn eq(&self, other: &Self) -> bool {
        self.username == other.username
    }
}
#[derive(Clone)]
pub struct UserStreamSorted {
    client: Client,
    username: String,
    sort: UserStreamSort,
    endpoint: String,
}
impl PartialEq for UserStreamSorted {
    fn eq(&self, other: &Self) -> bool {
        self.username == other.username && self.sort == other.sort
    }
}

impl UserStreamSorted {
    pub(crate) fn new(
        client: Client,
        username: String,
        sort: UserStreamSort,
        endpoint: String,
    ) -> Box<Self> {
        Box::new(Self {
            client,
            username,
            endpoint,
            sort,
        })
    }
    fn url(&self) -> String {
        format!("user/{}/{}.json", self.username, self.endpoint)
    }
}
impl IntoStreamPrivate for UserStreamSorted {
    type Output = Vec<Thing>;
    fn to_stream(&self) -> BoxStream<'static, Result<Vec<Thing>>> {
        let client = self.client.clone();
        client
            .sorted_user_stream(self.url(), self.sort, None)
            .boxed()
    }
}
