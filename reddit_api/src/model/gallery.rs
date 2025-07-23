use std::collections::HashMap;

use serde::{Deserialize, Serialize};

use super::post::ImageBase;

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize, Default)]
pub struct Gallery {
    gallery_data: GalleryData,
    media_metadata: HashMap<String, MediaMetadata>,
}

impl Gallery {
    /// flutter_rust_bridge:sync
    pub fn get(&self, index: u32) -> ImageBase {
        // TODO: support for resolution, and obfuscation
        let id = &self.gallery_data.items[index as usize];
        if let MediaMetadata::Media(GalleryMedia { source, .. }) =
            self.media_metadata.get(&id.media_id).unwrap()
        {
            source.clone().into()
        } else {
            ImageBase {
                url: "none".to_string(),
                width: 0,
                height: 0,
            }
        }
    }

    /// flutter_rust_bridge:sync,getter
    pub fn get_length(&self) -> u32 {
        self.gallery_data.items.len() as u32
    }
}

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize, Default)]
pub struct GalleryData {
    items: Vec<MediaId>,
}

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct MediaId {
    media_id: String,
    id: u32,
}

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(tag = "status")]
pub struct GalleryMedia {
    #[serde(rename = "e")]
    pub kind: MediaKind,
    #[serde(rename = "m")]
    /// A string like "image/jpg", or "image/gif"
    pub media_type: String,
    #[serde(rename = "s")]
    source: Image,
    #[serde(rename = "p")]
    previews: Vec<Image>,
    #[serde(rename = "o", default)]
    obfuscated: Vec<Image>,
}

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(tag = "status")]
pub enum MediaMetadata {
    #[serde(rename = "valid")]
    Media(GalleryMedia),
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
struct Image {
    pub u: String,
    pub x: u32,
    pub y: u32,
}

impl From<Image> for ImageBase {
    fn from(value: Image) -> Self {
        Self {
            url: value.u,
            width: value.x,
            height: value.y,
        }
    }
}

#[derive(Debug, Clone, Copy, PartialEq, Eq, Serialize, Deserialize)]
pub enum MediaKind {
    Image,
    AnimatedImage,
}
