use serde::{Deserialize, Deserializer, Serialize, Serializer};
use serde_with::{serde_as, with_prefix};

use super::flair::Flair;

with_prefix!(prefix_author_flair "author_flair_");

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde_as]
pub struct AuthorInfo {
    #[serde(rename = "author")]
    pub username: String,
    #[serde(rename = "author_premium", default)]
    pub premium: bool,
    #[serde(flatten, with = "prefix_author_flair")]
    pub flair: Flair,
    #[serde(rename = "author_fullname", default)]
    pub fullname: String,
    #[serde(rename = "author_is_blocked")]
    pub is_blocked: bool,
    #[serde(rename = "author_patreon_flair", default)]
    pub patreon_flair: bool,
}

pub fn author_option<'de, D>(d: D) -> Result<Option<AuthorInfo>, D::Error>
where
    D: Deserializer<'de>,
{
    let author = AuthorInfo::deserialize(d)?;
    if &author.username == "[deleted]" {
        Ok(None)
    } else {
        Ok(Some(author))
    }
}

#[allow(clippy::ref_option)]
pub fn serialize_author_option<S>(
    author: &Option<AuthorInfo>,
    serializer: S,
) -> Result<S::Ok, S::Error>
where
    S: Serializer,
{
    if let Some(author) = author {
        author.serialize(serializer)
    } else {
        let author = AuthorInfo {
            username: "[deleted]".to_string(),
            ..Default::default()
        };
        author.serialize(serializer)
    }
}
