#[derive(Debug, ::thiserror::Error)]
pub enum Error {
    #[error("Error in reqwest")]
    Reqwest(#[from] reqwest::Error),
    #[error("Error while parsing json")]
    Parsing(#[from] serde_json::Error),
    #[error("Invalid Thing type")]
    InvalidThing,
    #[error("Error while decode base 64")]
    Base64(#[from] base64::DecodeError),
}
