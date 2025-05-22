// Needs to be pub for the bridge
pub use futures::lock::Mutex;
pub use reddit_api::client::Client;
use reddit_api::model::feed::{Stream, StreamExt};
use reddit_api::model::post::{Kind, Thumbnail};
//use reddit_api::model::Timeframe;
use std::sync::Arc;
use std::sync::LazyLock;

pub use reddit_api::error::Error;
use reddit_api::model::feed::Feed;
use reddit_api::model::post::Post;
use reddit_api::model::Sort;

//pub use reddit_api::{Fullname, Saveable, Votable};

pub static CLIENT: LazyLock<reddit_api::client::Client> =
    LazyLock::new(reddit_api::client::Client::new_anonymous);

#[flutter_rust_bridge::frb]
pub struct RedditAPI {}

impl RedditAPI {
    #[flutter_rust_bridge::frb(sync)]
    pub fn client() -> Client {
        let client = &*CLIENT;
        client.clone()
    }
}

// #[flutter_rust_bridge::frb(mirror(Feed))]
// pub enum _Feed {
//     Home,
//     All,
//     Popular,
//     Subreddit(String),
// }

// #[flutter_rust_bridge::frb(mirror(Sort))]
// pub enum _Sort {
//     Best,
//     Hot,
//     Top(Timeframe),
//     Rising,
//     New(Timeframe),
//     Controversial(Timeframe),
// }

pub type FeedStream = Box<dyn Stream<Item = Result<Post, Error>> + Send + Sync + Unpin>;

#[flutter_rust_bridge::frb(opaque)]
pub struct FeedWrapper {
    pub stream: Arc<Mutex<FeedStream>>,
}

impl FeedWrapper {
    #[flutter_rust_bridge::frb(sync)]
    pub fn new(feed: Feed, sort: Sort) -> Self {
        let stream = CLIENT.feed(feed, sort);
        let stream: FeedStream = Box::new(stream);
        let stream = Arc::new(Mutex::new(stream));
        FeedWrapper { stream }
    }
    pub async fn next(&mut self) -> Result<Option<Post>, Error> {
        self.stream
            .clone()
            .lock_owned()
            .await
            .next()
            .await
            .transpose()
    }
}

#[flutter_rust_bridge::frb(external)]
impl Post {
    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn kind(&self) -> Kind {}
    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn thumbnail(&self) -> Option<Thumbnail> {}
}

#[flutter_rust_bridge::frb(init)]
pub fn init_app() {
    // Default utilities - feel free to customize
    flutter_rust_bridge::setup_default_user_utils();
    setup_the_logger();
}

fn setup_the_logger() {
    #[cfg(target_os = "android")]
    android_logger::init_once(
        android_logger::Config::default().with_max_level(log::LevelFilter::Trace),
    );
}
