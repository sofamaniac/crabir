use std::collections::HashMap;

use serde::{Deserialize, Serialize};

use super::post::ImageBase;

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Gallery {
    gallery_data: GalleryData,
    media_metadata: HashMap<String, MediaMetadata>,
}
#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct GalleryData {
    items: Vec<MediaId>,
}

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct MediaId {
    media_id: String,
    id: u64,
}

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(tag = "status")]
pub enum MediaMetadata {
    #[serde(rename = "valid")]
    Media {
        #[serde(rename = "e")]
        kind: MediaKind,
        m: String,
        #[serde(rename = "s")]
        source: ImageBase,
        #[serde(rename = "p")]
        previews: Vec<ImageBase>,
        #[serde(rename = "o", default)]
        obfuscated: Vec<ImageBase>,
    },
}

#[derive(Debug, Clone, Copy, PartialEq, Eq, Serialize, Deserialize)]
pub enum MediaKind {
    Image,
    AnimatedImage,
}
