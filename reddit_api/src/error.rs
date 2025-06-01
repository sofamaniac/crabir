use std::backtrace::Backtrace;

#[derive(Debug, ::thiserror::Error)]
pub enum Error {
    #[error("Error in reqwest {backtrace}")]
    Reqwest {
        #[from]
        source: reqwest::Error,
        #[backtrace]
        backtrace: Backtrace,
    },
    #[error("Error while parsing json: {source}\n({backtrace})")]
    Parsing {
        #[from]
        source: serde_json::Error,
        #[backtrace]
        backtrace: Backtrace,
    },
    #[error("Invalid Thing type {backtrace}")]
    InvalidThing {
        #[backtrace]
        backtrace: Backtrace,
    },
    #[error("Error while decode base 64: {source}")]
    Base64 {
        #[from]
        source: base64::DecodeError,
        #[backtrace]
        backtrace: Backtrace,
    },
    #[error("Error: {message}, {backtrace}")]
    Custom {
        message: String,
        backtrace: Backtrace,
    },
}
