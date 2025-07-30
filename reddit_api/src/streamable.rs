use futures::lock::Mutex;

use crate::{
    client::{Client, VoteDirection},
    model::{Fullname, Thing},
    result::Result,
};
use futures::{StreamExt, stream::BoxStream};
use stream::IntoStreamPrivate;

pub(crate) mod stream {
    use crate::result::Result;
    use futures::stream::BoxStream;
    pub trait IntoStreamPrivate {
        type Output;
        //fn to_stream(self) -> impl Stream<Item = Result<Self::Output>> + Unpin;
        fn to_stream(&self) -> BoxStream<'static, Result<Self::Output>>;
    }
}

pub trait IntoStream: IntoStreamPrivate<Output = Thing> + Send + Sync {}

impl<T: IntoStreamPrivate<Output = Thing> + Send + Sync> IntoStream for T {}

/// flutter_rust_bridge:opaque
pub struct Streamable {
    listing: Box<dyn IntoStream>,
    things: Mutex<Vec<Thing>>,
    done: Mutex<bool>,
    stream: Mutex<BoxStream<'static, Result<Thing>>>,
}

impl Streamable {
    pub(crate) fn new(listing: Box<dyn IntoStream>) -> Streamable {
        Self {
            stream: listing.to_stream().into(),
            listing,
            things: Vec::new().into(),
            done: false.into(),
        }
    }

    pub async fn refresh(&mut self) {
        let mut stream = self.stream.lock().await;
        *stream = self.listing.to_stream();
        let mut things = self.things.lock().await;
        things.clear();
        let mut done = self.done.lock().await;
        *done = false;
    }

    /// Returns true if there are still elements remaining.
    /// flutter_rust_bridge:
    pub async fn next(&mut self) -> Result<bool> {
        // Calling stream.next() after its completions is an error.
        if *self.done.lock().await {
            return Ok(false);
        }
        let mut stream = self.stream.lock().await;
        if let Some(next) = stream.next().await {
            let mut things = self.things.lock().await;
            things.push(next?);
            Ok(true)
        } else {
            let mut done = self.done.lock().await;
            *done = true;
            Ok(false)
        }
    }

    pub async fn nth(&self, n: u32) -> Option<Thing> {
        let things = self.things.lock().await;
        things.get(n as usize).cloned()
    }

    /// flutter_rust_bridge:getter
    pub async fn get_length(&self) -> u32 {
        self.things.lock().await.len() as u32
    }

    pub async fn vote(
        &self,
        name: &Fullname,
        direction: VoteDirection,
        client: &Client,
    ) -> Result<()> {
        client.vote(name, direction).await?;
        let mut things = self.things.lock().await;
        let thing = things
            .iter()
            .position(|thing| &thing.name().unwrap_or_default() == name);
        if let Some(thing) = thing {
            things[thing].likes(direction);
        }
        Ok(())
    }

    pub async fn save(&self, name: &Fullname, save: bool, client: &Client) -> Result<()> {
        if save {
            client.save(name).await?;
        } else {
            client.unsave(name).await?;
        }
        let mut things = self.things.lock().await;
        let thing = things
            .iter()
            .position(|thing| &thing.name().unwrap_or_default() == name);
        if let Some(thing) = thing {
            things[thing].save(save);
        }
        Ok(())
    }

    pub async fn get_all(&self) -> Vec<Thing> {
        self.things.lock().await.clone()
    }
}
