use std::task::Poll;
use std::{collections::VecDeque, pin::Pin};

use crate::client::{Client, Pager};
use crate::result::Result;
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

pub struct FeedStream<'a> {
    client: &'a Client,
    pager: Pager,
    feed: Feed,
    sort: Sort,
    posts: VecDeque<Post>,
    done: bool,
    pending_fetch: Option<Pin<Box<dyn Future<Output = Result<Listing>> + Send + Sync + 'a>>>,
}

impl<'a> FeedStream<'a> {
    #[must_use]
    pub fn new(client: &'a Client, feed: Feed, sort: Sort) -> FeedStream<'a> {
        Self {
            client,
            feed,
            sort,
            posts: VecDeque::new(),
            done: false,
            pager: Pager::default(),
            pending_fetch: None,
        }
    }
}

impl Stream for FeedStream<'_> {
    type Item = Result<Post>;

    fn poll_next(
        self: std::pin::Pin<&mut Self>,
        cx: &mut std::task::Context<'_>,
    ) -> std::task::Poll<Option<Self::Item>> {
        // Pin projection
        let this = self.get_mut();

        // Yield buffered post if available
        if let Some(post) = this.posts.pop_front() {
            return Poll::Ready(Some(Ok(post)));
        }

        // If already done, stop streaming
        if this.done {
            return Poll::Ready(None);
        }

        // Spawn an async block to fetch the next page
        // We use a `Box::pin` future and `poll` it manually
        let client = this.client;
        let pager = this.pager.clone(); // or handle clone appropriately
        let sort = this.sort;
        let feed = this.feed.clone(); // same here

        if this.pending_fetch.is_none() {
            // Create a future to fetch and store results
            let fut = async move {
                client
                    .feed_request(&feed, &sort, &pager) // implement this method to fetch a new page
                    .await
            };
            let fut = Box::pin(fut);
            this.pending_fetch = Some(fut);
        }

        // Poll the future manually
        if let Some(fut) = &mut this.pending_fetch {
            match fut.as_mut().poll(cx) {
                Poll::Pending => Poll::Pending,
                Poll::Ready(Ok(listing)) => {
                    let posts = listing
                        .children
                        .into_iter()
                        .filter_map(|child| match child {
                            Thing::Post(post) => Some(post),
                            _ => None,
                        })
                        .collect();
                    this.posts = posts;
                    if listing.after.is_none() {
                        this.done = true;
                    }
                    this.pager.after(listing.after);
                    let post = this.posts.pop_front().expect("guaranteed one post");
                    // We reset the future, otherwise the async fn is polled after completion and
                    // it crashes
                    this.pending_fetch = None;
                    Poll::Ready(Some(Ok(post)))
                }
                Poll::Ready(Err(e)) => {
                    this.done = true;
                    Poll::Ready(Some(Err(e)))
                }
            }
        } else {
            Poll::Ready(None)
        }
    }
}
