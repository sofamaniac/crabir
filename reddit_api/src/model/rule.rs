use flutter_getter::FlutterGetters;
use getset::Getters;
use serde::{Deserialize, Serialize};

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize, FlutterGetters, Getters)]
#[getset(get = "pub(crate)")]
/// Data returned by https://www.reddit.com/r/{subreddit}/about/rules
pub struct Rule {
    kind: String,
    description: String,
    short_name: String,
    description_html: String,
}
