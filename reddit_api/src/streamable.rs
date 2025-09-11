use flutter_rust_bridge::frb;
use futures::lock::Mutex;

use crate::{
    client::{Client, VoteDirection},
    model::{Fullname, Thing, Votable as _},
    result::Result,
};
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

pub trait IntoStream: IntoStreamPrivate<Output = Thing> + Send + Sync {}

impl<T: IntoStreamPrivate<Output = Thing> + Send + Sync> IntoStream for T {}

#[frb(opaque)]
pub struct Streamable {
    listing: Box<dyn IntoStream>,
    things: Vec<Thing>,
    done: bool,
    stream: Mutex<BoxStream<'static, Result<Thing>>>,
}

impl Streamable {
    pub(crate) fn new(listing: Box<dyn IntoStream>) -> Streamable {
        Self {
            stream: listing.to_stream().into(),
            listing,
            things: Vec::new(),
            done: false,
        }
    }

    pub async fn refresh(&mut self) {
        self.things.clear();
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
            self.things.push(next?);
            Ok(true)
        } else {
            self.done = true;
            Ok(false)
        }
    }

    #[frb(sync)]
    pub fn nth(&self, n: u32) -> Option<Thing> {
        self.things.get(n as usize).cloned()
    }

    #[frb(sync)]
    pub fn get_all(&self) -> Vec<Thing> {
        self.things.clone()
    }

    #[frb(sync, getter)]
    pub fn get_length(&self) -> u32 {
        self.things.len() as u32
    }

    pub async fn vote(
        &mut self,
        name: &Fullname,
        direction: VoteDirection,
        client: &Client,
    ) -> Result<()> {
        let index = self
            .things
            .iter()
            .position(|t| t.name().is_some_and(|n| &n == name));
        if index.is_none() {
            return Ok(());
        }
        let thing = self.things.get_mut(index.unwrap());
        match thing {
            Some(Thing::Post(val)) => val.vote(direction, client).await?,
            Some(Thing::Comment(val)) => val.vote(direction, client).await?,
            _ => (),
        };
        Ok(())
    }

    pub async fn save(&mut self, name: &Fullname, save: bool, client: &Client) -> Result<()> {
        if save {
            client.save(name).await?;
        } else {
            client.unsave(name).await?;
        }
        let index = self
            .things
            .iter()
            .position(|thing| &thing.name().unwrap_or_default() == name);
        if index.is_none() {
            return Ok(());
        }
        let thing = self.things.get_mut(index.unwrap());
        match thing {
            Some(Thing::Post(val)) if save => val.save(client).await?,
            Some(Thing::Post(val)) if !save => val.unsave(client).await?,
            Some(Thing::Comment(val)) if save => val.save(client).await?,
            Some(Thing::Comment(val)) if !save => val.unsave(client).await?,
            _ => (),
        };
        Ok(())
    }
}
