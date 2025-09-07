pub mod media;
pub mod reddit_api;

#[flutter_rust_bridge::frb(init)]
pub fn init_app() {
    std::env::set_var("RUST_BACKTRACE", "1");
    // Default utilities - feel free to customize
    flutter_rust_bridge::setup_default_user_utils();
}
