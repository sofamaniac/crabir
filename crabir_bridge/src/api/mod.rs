pub mod reddit_api;

#[flutter_rust_bridge::frb(init)]
pub fn init_app() {
    // Default utilities - feel free to customize
    // flutter_rust_bridge::setup_default_user_utils();
    setup_log_to_console();
    std::env::set_var("RUST_BACKTRACE", "1");
}

fn setup_log_to_console() {
    let level = log::LevelFilter::Trace;
    let filter = env_filter::Builder::new()
        .filter_module("h2", log::LevelFilter::Error)
        .build();
    #[cfg(target_os = "android")]
    let _ = android_logger::init_once(
        android_logger::Config::default()
            .with_max_level(level)
            .with_filter(filter),
    );

    #[cfg(any(target_os = "ios", target_os = "macos"))]
    let _ = oslog::OsLogger::new("frb_user")
        .level_filter(level)
        .with_filter(filter)
        .init();

    #[cfg(target_family = "wasm")]
    let _ = crate::misc::web_utils::WebConsoleLogger::init();
}
