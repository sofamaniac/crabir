//#![warn(clippy::pedantic)]
#![warn(clippy::large_futures)]
#![allow(clippy::missing_panics_doc)]
#![allow(clippy::struct_excessive_bools)]
//#![deny(clippy::unwrap_used)]

pub mod client;
pub mod error;
pub mod model;
pub mod paging_handler;
pub mod reportable;
pub mod result;
pub mod search;
pub mod streamable;
mod utils;
pub mod votable;
