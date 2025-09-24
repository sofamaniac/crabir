//! Handle requests to streamable.com.

use crate::result::Result;
use reqwest::Url;
use serde::{Deserialize, Serialize};

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct StreamableAnswer {
    pub status: i64,
    pub percent: i64,
    pub url: String,
    #[serde(rename = "embed_code")]
    pub embed_code: String,
    pub files: Files,
    #[serde(rename = "thumbnail_url")]
    pub thumbnail_url: String,
    pub title: String,
    #[serde(rename = "audio_channels")]
    pub audio_channels: i64,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Files {
    #[serde(rename = "mp4-mobile")]
    pub mp4_mobile: Video,
    pub mp4: Video,
    pub original: Original,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Video {
    pub status: i64,
    pub url: String,
    pub framerate: i64,
    pub height: i64,
    pub width: i64,
    pub bitrate: i64,
    pub size: i64,
    pub duration: f64,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Original {
    pub framerate: f64,
    pub bitrate: i64,
    pub size: i64,
    pub duration: f64,
    pub height: i64,
    pub width: i64,
}

pub async fn get_streamable_video(url: &str) -> Result<(Video, String)> {
    let url = Url::parse(url).expect("Could not parse streamable url");
    let id = url
        .path_segments()
        .expect("Could not extract path segments from streamable url")
        .next_back()
        .expect("Could not extract id from streamable url");
    let client = reqwest::Client::new();
    let request = client
        .get(format!("https://api.streamable.com/videos/{id}"))
        .build()?;
    let result = client.execute(request).await?;
    let json = result.text().await?;
    match serde_json::from_str::<StreamableAnswer>(&json) {
        Ok(answer) => Ok((answer.files.mp4_mobile, answer.thumbnail_url)),
        Err(err) => {
            log::error!("[get_streamable_video] Error while parsing: {err}");
            Err(err.into())
        }
    }
}
