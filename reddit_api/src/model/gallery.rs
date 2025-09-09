use core::f32;
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
    pub fn get(&self, index: u32) -> Option<GalleryMedia> {
        let id = &self.gallery_data.items[index as usize];
        if let Some(MediaMetadata::Media(media)) = self.media_metadata.get(&id.media_id) {
            Some(media.clone())
        } else {
            None
        }
    }

    /// flutter_rust_bridge:sync,getter
    pub fn get_aspect_ratio(&self) -> f32 {
        let mut aspect_ratio = f32::INFINITY;
        for image in self.media_metadata.values() {
            if let MediaMetadata::Media(GalleryMedia { source, .. }) = image {
                if aspect_ratio.is_infinite() {
                    aspect_ratio = source.aspect_ratio()
                } else if aspect_ratio > source.aspect_ratio() {
                    aspect_ratio = source.aspect_ratio();
                }
            }
        }
        aspect_ratio
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
    caption: Option<String>,
}

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(tag = "status")]
/// flutter_rust_bridge:non_opaque
pub struct GalleryMedia {
    #[serde(rename = "m")]
    /// A string like "image/jpg", or "image/gif"
    pub media_type: String,
    #[serde(rename = "s", flatten)]
    pub source: Source,
    #[serde(rename = "p")]
    pub previews: Vec<Image>,
    #[serde(rename = "o", default)]
    pub obfuscated: Vec<Image>,
}

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(tag = "status")]
pub enum MediaMetadata {
    #[serde(rename = "valid")]
    Media(GalleryMedia),
    #[serde(rename = "failed")]
    Failed,
}

#[derive(Default, Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
pub struct Image {
    pub u: String,
    pub x: u32,
    pub y: u32,
}
#[derive(Default, Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
pub struct AnimatedImage {
    pub x: u32,
    pub y: u32,
    pub gif: String,
    pub mp4: String,
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

#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
#[serde(tag = "e")]
/// flutter_rust_bridge:non_opaque
pub enum Source {
    Image {
        #[serde(rename = "s")]
        source: Image,
    },
    AnimatedImage {
        #[serde(rename = "s")]
        source: AnimatedImage,
    },
}

impl Source {
    fn aspect_ratio(&self) -> f32 {
        let r = match self {
            Self::Image { source } => source.x as f32 / source.y as f32,
            Self::AnimatedImage { source } => source.x as f32 / source.y as f32,
        };
        if r.is_nan() { f32::INFINITY } else { r }
    }
}
