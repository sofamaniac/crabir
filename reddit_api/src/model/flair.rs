use serde::{Deserialize, Serialize};
use std::collections::HashMap;

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Flair {
    pub css_class: Option<String>,
    #[serde(default)]
    pub richtext: Vec<HashMap<String, String>>,
    // TODO parse color
    background_color: Option<String>,
    text_color: Option<String>,
    pub text: Option<String>,
    #[serde(rename = "type")]
    pub flair_type: Option<String>,
    pub template_id: Option<String>,
    pub position: Option<String>,
}
