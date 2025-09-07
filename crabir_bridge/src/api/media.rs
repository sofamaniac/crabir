use reddit_api::model::gallery::Gallery;

pub enum Resolution {
    Source,
    High,
    Medium,
    Low,
}

pub struct Data {
    pub url: String,
    pub width: u32,
    pub height: u32,
    pub aspect_ratio: f32,
}

pub trait WithResolution {
    fn get(&self, resolution: Resolution) -> Self;
}
