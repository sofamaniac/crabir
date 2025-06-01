use std::task::Poll;
use std::{collections::VecDeque, pin::Pin};

use crate::client::{Client, Pager};
use crate::listing_stream::ListingStream;
use crate::result::Result;
use futures::FutureExt;
pub use futures::{Stream, StreamExt};

use super::{Listing, Post, Sort, Thing};

/// All the kind of available feeds on reddit
#[derive(Debug, Default, PartialEq, Eq, Clone)]
/// flutter_rust_bridge:non_opaque
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

pub struct FeedStream {
    client: Client,
    feed: Feed,
    sort: Sort,
}

impl FeedStream {
    #[must_use]
    pub fn new(client: Client, feed: Feed, sort: Sort) -> FeedStream {
        Self { client, feed, sort }
    }
}

impl ListingStream for FeedStream {
    type Output = Post;

    fn fetch_next(
        &self,
        pager: &Pager,
    ) -> Pin<Box<dyn Future<Output = Result<Listing>> + Send + Sync>> {
        let client = self.client.clone();
        let feed = self.feed.clone();
        let sort = self.sort.clone();
        let pager = pager.clone();
        Box::pin(async move { client.feed_request(&feed, &sort, &pager).await })
    }
}
