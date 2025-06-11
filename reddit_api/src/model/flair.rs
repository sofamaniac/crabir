use serde::{Deserialize, Serialize};

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Flair {
    pub css_class: Option<String>,
    #[serde(default)]
    pub richtext: Vec<Richtext>,
    // TODO parse color
    pub background_color: Option<String>,
    pub text_color: Option<String>,
    pub text: Option<String>,
    #[serde(rename = "type")]
    pub flair_type: Option<String>,
    pub template_id: Option<String>,
    pub position: Option<String>,
}

#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(tag = "e")]
/// flutter_rust_bridge:non_opaque
pub enum Richtext {
    #[serde(rename = "text")]
    Text { t: String },
    #[serde(rename = "emoji")]
    Emoji { a: String, u: String },
}
