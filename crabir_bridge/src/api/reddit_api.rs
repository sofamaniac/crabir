// Needs to be pub for the bridge
pub use futures::lock::Mutex;
use log::debug;
pub use reddit_api::client::Client;
use reddit_api::model::comment::Comment;
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

#[flutter_rust_bridge::frb(sync)]
pub fn debug_post(post: &Post) {
    debug!("{post:#?}");
}

#[flutter_rust_bridge::frb(sync)]
pub fn debug_comment(comment: &Comment) {
    debug!("{comment:#?}");
}
