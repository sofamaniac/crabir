use serde::{Deserialize, Serialize};

use crate::error::Error;

use super::Thing;

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct Multi {
    #[serde(rename = "can_edit")]
    pub can_edit: bool,
    #[serde(rename = "display_name")]
    pub display_name: String,
    pub name: String,
    #[serde(rename = "description_html")]
    pub description_html: String,
    #[serde(rename = "num_subscribers")]
    pub num_subscribers: i64,
    #[serde(rename = "copied_from")]
    pub copied_from: Option<String>,
    #[serde(rename = "icon_url")]
    pub icon_url: String,
    pub subreddits: Vec<SubredditName>,
    #[serde(rename = "created_utc")]
    pub created_utc: f64,
    pub visibility: String,
    pub created: f64,
    #[serde(rename = "over_18")]
    pub over_18: bool,
    pub path: String,
    pub owner: String,
    #[serde(rename = "key_color")]
    pub key_color: Option<String>,
    #[serde(rename = "is_subscriber")]
    pub is_subscriber: bool,
    #[serde(rename = "owner_id")]
    pub owner_id: String,
    #[serde(rename = "description_md")]
    pub description_md: String,
    #[serde(rename = "is_favorited")]
    pub is_favorited: bool,
}

impl TryFrom<Thing> for Multi {
    type Error = Error;

    fn try_from(value: Thing) -> Result<Self, Self::Error> {
        match value {
            Thing::Multi(multi) => Ok(multi),
            _ => Err(Error::InvalidThing),
        }
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SubredditName {
    /// name of the subreddit (e.g. awwnime)
    pub name: String,
}
