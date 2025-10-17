use flutter_getter::FlutterGetters;
use getset::Getters;
use serde::{Deserialize, Serialize};

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize, FlutterGetters, Getters)]
#[getset(get = "pub(crate)")]
/// Data returned by https://www.reddit.com/r/{subreddit}/about/rules
pub struct Rule {
    kind: RuleKind,
    description: String,
    short_name: String,
    description_html: String,
    violation_reason: String,
}

#[derive(Default, Debug, Clone, Copy, PartialEq, Eq, Serialize, Deserialize)]
pub enum RuleKind {
    #[serde(rename = "all")]
    #[default]
    All,
    #[serde(rename = "link")]
    Link,
    #[serde(rename = "comment")]
    Comment,
}
