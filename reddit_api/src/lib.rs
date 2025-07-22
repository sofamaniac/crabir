//#![warn(clippy::pedantic)]
#![warn(clippy::large_futures)]
#![allow(clippy::missing_panics_doc)]
#![allow(clippy::struct_excessive_bools)]
//#![deny(clippy::unwrap_used)]
// For thiserror backtrace
#![feature(error_generic_member_access)]

pub mod client;
pub mod error;
pub mod model;
pub mod result;
pub mod streamable;
mod utils;
