#[derive(Debug, ::thiserror::Error)]
pub enum Error {
    #[error("Error in reqwest")]
    Reqwest {
        #[from]
        source: reqwest::Error,
    },
    #[error("Error while parsing json: {source}\n")]
    Parsing {
        #[from]
        source: serde_json::Error,
    },
    #[error("Invalid Thing type")]
    InvalidThing,
    #[error("Error while decode base 64: {source}")]
    Base64 {
        #[from]
        source: base64::DecodeError,
    },
    #[error("Error: {message}")]
    Custom { message: String },
}
