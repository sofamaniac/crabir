use std::{
    collections::VecDeque,
    pin::Pin,
    task::{Poll, ready},
};

use crate::{client::Pager, result::Result};
use futures::Stream;

use crate::{
    client::Client,
    model::{Listing, Thing},
};

pub(crate) struct Wrapper<T>
where
    T: ListingStream + Unpin,
{
    listing: T,
    things: VecDeque<<T as ListingStream>::Output>,
    pending_fetch: Option<Pin<Box<dyn Future<Output = Result<Listing>> + Send + Sync>>>,
    done: bool,
    pager: Pager,
}

impl<T> Wrapper<T>
where
    T: ListingStream + Unpin,
{
    pub(crate) fn new(listing: T) -> Self {
        Self {
            listing,
            things: VecDeque::new(),
            pending_fetch: None,
            done: false,
            pager: Pager::default(),
        }
    }
}

pub(crate) trait ListingStream {
    type Output: TryFrom<Thing> + Unpin + Send + Sync;
    fn fetch_next(
        &self,
        pager: &Pager,
    ) -> Pin<Box<dyn Future<Output = Result<Listing>> + Send + Sync>>;
}

impl<T> Stream for Wrapper<T>
where
    T: ListingStream + Unpin + Send + Sync,
{
    type Item = Result<<T as ListingStream>::Output>;

    fn poll_next(
        self: std::pin::Pin<&mut Self>,
        cx: &mut std::task::Context<'_>,
    ) -> std::task::Poll<Option<Self::Item>> {
        // Pin projection
        let this = self.get_mut();

        // Yield buffered post if available
        if let Some(thing) = this.things.pop_front() {
            return Poll::Ready(Some(Ok(thing)));
        }

        if this.done {
            return Poll::Ready(None);
        }

        if this.pending_fetch.is_none() {
            // Create a future to fetch and store results
            let fut = this.listing.fetch_next(&this.pager);
            this.pending_fetch = Some(fut);
        }

        // Poll the future manually
        if let Some(fut) = &mut this.pending_fetch {
            let listing = ready!(fut.as_mut().poll(cx));
            match listing {
                Err(e) => {
                    this.done = true;
                    Poll::Ready(Some(Err(e)))
                }
                Ok(listing) => {
                    // We reset the future, otherwise the async fn is polled after completion and
                    // it crashes
                    this.pending_fetch = None;

                    let things = listing
                        .children
                        .into_iter()
                        .filter_map(|child| child.try_into().ok())
                        .collect();
                    this.things = things;
                    if listing.after.is_none() {
                        this.done = true;
                    }
                    this.pager.after(listing.after);
                    if let Some(post) = this.things.pop_front() {
                        Poll::Ready(Some(Ok(post)))
                    } else {
                        // WARN: This is incorrect if the page we got contained only incorrect
                        // items
                        this.done = true;
                        Poll::Ready(None)
                    }
                }
            }
        } else {
            Poll::Ready(None)
        }
    }
}
