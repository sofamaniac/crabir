use log::LevelFilter;

pub mod reddit_api;

#[flutter_rust_bridge::frb(init)]
pub fn init_app() {
    // Default utilities - feel free to customize
    //flutter_rust_bridge::setup_default_user_utils();
    setup_log_to_console();
    //std::env::set_var("RUST_BACKTRACE", "1");
}

fn setup_log_to_console() {
    let level = LevelFilter::Debug;
    #[cfg(target_os = "android")]
    {
        let config = android_logger::Config::default().with_max_level(level);
        let _ = android_logger::init_once(config);
    }

    #[cfg(any(target_os = "ios", target_os = "macos"))]
    let _ = oslog::OsLogger::new("frb_user").level_filter(level).init();

    #[cfg(target_family = "wasm")]
    let _ = crate::misc::web_utils::WebConsoleLogger::init();
}
