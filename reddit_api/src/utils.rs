use serde::de::Error;
use serde::{Deserialize, Deserializer, Serialize, Serializer};

use crate::model::post::AuthorInfo;

#[derive(Deserialize)]
#[serde(untagged)]
enum MyHelper<'a, Ty> {
    String(&'a str),
    Object(Ty),
    None {},
    EmptyVec(Vec<Ty>),
    Option(Option<Ty>),
    Bool(bool),
}
pub fn response_or_none<'de, D, T>(d: D) -> Result<Option<T>, D::Error>
where
    D: Deserializer<'de>,
    T: Deserialize<'de>,
{
    match MyHelper::deserialize(d) {
        Ok(MyHelper::Object(r)) => Ok(Some(r)),
        Ok(MyHelper::String("") | MyHelper::None {} | MyHelper::Bool(false)) => Ok(None),
        Ok(MyHelper::String(_)) => Err(D::Error::custom("Only empty strings may be provided")),
        Ok(MyHelper::EmptyVec(v)) if v.is_empty() => Ok(None),
        Ok(MyHelper::EmptyVec(_)) => Err(D::Error::custom("Only empty vecs may be provided")),
        Ok(MyHelper::Option(option)) => Ok(option),
        Ok(MyHelper::Bool(true)) => Err(D::Error::custom("Expected only `false`.")),
        Err(err) => Err(err),
    }
}

pub fn response_or_none_string<'de, D>(d: D) -> Result<Option<String>, D::Error>
where
    D: Deserializer<'de>,
{
    match MyHelper::deserialize(d) {
        Ok(MyHelper::Object(r)) => Ok(Some(r)),
        Ok(MyHelper::String("") | MyHelper::None {} | MyHelper::Bool(false)) => Ok(None),
        Ok(MyHelper::String(s)) => Ok(Some(s.to_owned())),
        Ok(MyHelper::EmptyVec(v)) if v.is_empty() => Ok(None),
        Ok(MyHelper::EmptyVec(_)) => Err(D::Error::custom("Only empty vecs may be provided")),
        Ok(MyHelper::Option(option)) => Ok(option),
        Ok(MyHelper::Bool(true)) => Err(D::Error::custom("Expected only `false`.")),
        Err(err) => Err(err),
    }
}

pub fn response_or_default<'de, D, T>(d: D) -> Result<T, D::Error>
where
    D: Deserializer<'de>,
    T: Deserialize<'de> + Default,
{
    Ok(response_or_none(d)?.unwrap_or_default())
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
