// Needs to be pub for the bridge
pub use futures::lock::Mutex;
use log::debug;
pub use reddit_api::client::Client;
use reddit_api::model::{
    comment::Comment,
    post::{Kind, Thumbnail},
};
use std::sync::LazyLock;

pub use reddit_api::error::Error;
use reddit_api::model::post::Post;

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

#[flutter_rust_bridge::frb(sync)]
pub fn debug_comment(comment: &Comment) {
    debug!("{comment:#?}");
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
