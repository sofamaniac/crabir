use flutter_rust_bridge::frb;
use futures::lock::Mutex;

use crate::{model::Thing, result::Result};
use futures::{StreamExt, stream::BoxStream};
use stream::IntoStreamPrivate;

pub(crate) mod stream {
    use crate::result::Result;
    use futures::stream::BoxStream;
    pub trait IntoStreamPrivate {
        type Output;
        fn to_stream(&self) -> BoxStream<'static, Result<Self::Output>>;
    }
}

pub trait IntoStream: IntoStreamPrivate<Output = Vec<Thing>> + Send + Sync {}

impl<T: IntoStreamPrivate<Output = Vec<Thing>> + Send + Sync> IntoStream for T {}

#[frb(opaque)]
pub struct Streamable {
    listing: Box<dyn IntoStream>,
    pages: Vec<Vec<Thing>>,
    done: bool,
    stream: Mutex<BoxStream<'static, Result<Vec<Thing>>>>,
}

impl Streamable {
    pub(crate) fn new(listing: Box<dyn IntoStream>) -> Streamable {
        Self {
            stream: listing.to_stream().into(),
            listing,
            pages: Vec::new(),
            done: false,
        }
    }

    pub async fn refresh(&mut self) {
        self.pages.clear();
        let mut stream = self.stream.lock().await;
        *stream = self.listing.to_stream();
        self.done = false;
    }

    /// Returns true if there are still elements remaining.
    pub async fn next(&mut self) -> Result<bool> {
        // Calling stream.next() after its completions is an error.
        if self.done {
            return Ok(false);
        }
        let mut stream = self.stream.lock().await;
        if let Some(next) = stream.next().await {
            self.pages.push(next?);
            Ok(true)
        } else {
            self.done = true;
            Ok(false)
        }
    }

    #[frb(sync)]
    pub fn last(&self) -> Vec<Thing> {
        self.pages.last().cloned().unwrap_or_default()
    }

    #[frb(sync)]
    pub fn nth(&self, n: u32) -> Option<Vec<Thing>> {
        self.pages.get(n as usize).cloned()
    }

    #[frb(sync)]
    pub fn get_all(&self) -> Vec<Thing> {
        self.pages.iter().cloned().flatten().collect()
    }

    #[frb(sync, getter)]
    pub fn get_length(&self) -> u32 {
        self.pages.iter().fold(0usize, |acc, r| acc + r.len()) as u32
    }
}
