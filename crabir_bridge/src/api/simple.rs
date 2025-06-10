use flutter_rust_bridge::JoinHandle;
use futures::channel::mpsc::{self, Receiver};
// Needs to be pub for the bridge
pub use futures::lock::Mutex;
use futures::SinkExt;
use log::debug;
pub use reddit_api::client::Client;
use reddit_api::client::VoteDirection;
use reddit_api::model::feed::{Stream, StreamExt};
use reddit_api::model::post::{Kind, Thumbnail};
use reddit_api::model::subreddit::{Icon, Subreddit};
use std::sync::LazyLock;
use std::time;

pub use reddit_api::error::Error;
use reddit_api::model::feed::Feed;
use reddit_api::model::post::Post;
use reddit_api::model::{Sort, Timeframe};

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

pub type PostStream = Box<dyn Stream<Item = Result<Post, Error>> + Send + Sync + Unpin>;

pub struct FeedState {
    feed: Feed,
    sort: Sort,
    posts: Vec<Post>,
    done: bool,
    loading: bool,
    bg_task: Option<JoinHandle<()>>,
    rx: Receiver<Result<Post, Error>>,
}

impl std::fmt::Debug for FeedState {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.debug_struct("FeedState")
            //.field("stream", &self.stream)
            .field("feed", &self.feed)
            .field("sort", &self.sort)
            .field("posts", &self.posts)
            .field("loading", &self.loading)
            .field("done", &self.loading)
            //.field("base_state", &self.base_state)
            .finish()
    }
}

impl FeedState {
    #[flutter_rust_bridge::frb(sync)]
    pub fn new(feed: Feed, sort: Sort) -> Self {
        let (_, rx) = mpsc::channel(10);
        //frb::spawn requires to be in an async function
        Self {
            feed,
            sort,
            posts: Vec::new(),
            done: false,
            loading: false,
            bg_task: None,
            rx,
        }
    }

    async fn new_bg_task(&mut self) {
        if let Some(bg_task) = self.bg_task.as_ref() {
            bg_task.abort();
        }
        let (mut tx, rx) = mpsc::channel(10);
        self.rx = rx;
        let feed_clone = self.feed.clone();
        let sort_clone = self.sort;
        self.bg_task = Some(flutter_rust_bridge::spawn(async move {
            let mut stream = CLIENT.feed(feed_clone, sort_clone);
            while let Some(post) = stream.next().await {
                if tx.send(post).await.is_err() {
                    break;
                }
            }
        }));
    }

    #[flutter_rust_bridge::frb]
    pub async fn refresh(&mut self) {
        self.new_bg_task().await;
        self.posts.clear();
    }

    /// Returns true if there is no more posts to load
    #[flutter_rust_bridge::frb]
    pub async fn next(&mut self) -> Result<bool, Error> {
        if self.bg_task.is_none() {
            self.new_bg_task().await;
        }
        if let Some(message) = self.rx.next().await {
            let post = message?;
            self.posts.push(post);
            Ok(false)
        } else {
            Ok(true)
        }
    }

    #[flutter_rust_bridge::frb(sync)]
    pub fn nth(&self, n: u32) -> Option<Post> {
        self.posts.get(n as usize).cloned()
    }

    /// Vote on a post
    #[flutter_rust_bridge::frb]
    pub async fn vote(&mut self, index: u32, direction: VoteDirection) -> Result<(), Error> {
        let index = index as usize;
        if let Some(post) = self.posts.get_mut(index) {
            CLIENT.vote(&post.name, direction).await?;
            post.likes = direction.into();
        }
        Ok(())
    }

    /// Save / unsave a post
    #[flutter_rust_bridge::frb]
    pub async fn save(&mut self, index: u32, save: bool) -> Result<(), Error> {
        let index = index as usize;
        if let Some(post) = self.posts.get_mut(index) {
            if save {
                CLIENT.save(&post.name).await?;
            } else {
                CLIENT.unsave(&post.name).await?;
            }
            post.saved = save;
        }
        Ok(())
    }

    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn get_length(&self) -> u32 {
        self.posts.len() as u32
    }

    // frb will automatically strip the prefix from setters and getters.
    #[flutter_rust_bridge::frb(setter)]
    pub async fn set_feed(&mut self, feed: Feed) {
        if feed != self.feed {
            self.feed = feed;
            self.refresh().await;
        }
    }

    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn get_feed(&self) -> Feed {
        self.feed.clone()
    }

    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn get_sort(&self) -> Sort {
        self.sort
    }
    #[flutter_rust_bridge::frb(setter)]
    pub async fn set_sort(&mut self, sort: Sort) {
        if sort != self.sort {
            self.sort = sort;
            self.refresh().await;
        }
    }

    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn get_done(&self) -> bool {
        self.done
    }
    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn get_loading(&self) -> bool {
        self.loading
    }

    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn get_title(&self) -> String {
        match &self.feed {
            Feed::Home => "Home".to_owned(),
            Feed::Subreddit(subreddit) => subreddit.clone(),
            Feed::All => "r/all".to_owned(),
            Feed::Popular => "r/popular".to_owned(),
        }
    }

    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn get_sort_string(&self) -> String {
        let start = match self.sort {
            Sort::Best => "Best",
            Sort::Hot => "Hot",
            Sort::Rising => "Rising",
            Sort::New(_) => "New",
            Sort::Top(_) => "Top",
            Sort::Controversial(_) => "Controversial",
        };
        let end = match self.sort {
            Sort::New(timeframe) | Sort::Top(timeframe) | Sort::Controversial(timeframe) => {
                Some(match timeframe {
                    Timeframe::All => "all time",
                    Timeframe::Year => "year",
                    Timeframe::Month => "month",
                    Timeframe::Week => "week",
                    Timeframe::Day => "day",
                    Timeframe::Hour => "hour",
                })
            }
            _ => None,
        };

        if let Some(end) = end {
            format!("{start} {end}")
        } else {
            start.to_owned()
        }
    }
}

#[flutter_rust_bridge::frb(external)]
impl Post {
    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn kind(&self) -> Kind {}
    #[flutter_rust_bridge::frb(sync, getter)]
    pub fn thumbnail(&self) -> Option<Thumbnail> {}
}

#[flutter_rust_bridge::frb(sync)]
pub fn debug_post(post: &Post) {
    debug!("{post:#?}");
}

#[flutter_rust_bridge::frb(init)]
pub fn init_app() {
    std::env::set_var("RUST_BACKTRACE", "1");
    // Default utilities - feel free to customize
    flutter_rust_bridge::setup_default_user_utils();
    setup_the_logger();
}

fn setup_the_logger() {
    #[cfg(target_os = "android")]
    android_logger::init_once(
        android_logger::Config::default().with_max_level(log::LevelFilter::Debug),
    );
}
