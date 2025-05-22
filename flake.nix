{
  description = "Flutter environment";

  inputs = {
    flake-utils.url = "github:numtide/flake-utils";
    nixpkgs.url = "github:NixOS/nixpkgs";
    rust-overlay.url = "github:oxalica/rust-overlay";
  };

  outputs =
    { nixpkgs, flake-utils, rust-overlay, ... }:
    flake-utils.lib.eachSystem [ "x86_64-linux" ] (
      system:
      let
        pkgs = import nixpkgs {
          inherit system;
          config.allowUnfree = true;
          android_sdk.accept_license = true;
          # overlays = [rust-overlay.overlays.default];
        };
        # rust-toolchain = pkgs.rust-bin.fromRustupToolchainFile ./toolchain.toml;
        buildToolsVer = "35.0.1";
        androidEnv = pkgs.androidenv.override { licenseAccepted = true; };
        androidComposition = androidEnv.composeAndroidPackages {
          cmdLineToolsVersion = "8.0";
          toolsVersion = "26.1.1";
          platformToolsVersion = "34.0.4";
          buildToolsVersions = [ buildToolsVer "34.0.0" "35.0.0" ];
          platformVersions = [ "33" "34" "35" ];
          abiVersions = [ "x86_64" ];

          includeNDK = true;
          ndkVersion = "26.1.10909125";
          systemImageTypes = [
            "google_apis"
            "google_apis_playstore"
          ];
          extraLicenses = [
            "android-googletv-license"
            "android-sdk-arm-dbt-license"
            "android-sdk-license"
            "android-sdk-preview-license"
            "google-gdk-license"
            "intel-android-extra-license"
            "intel-android-sysimage-license"
            "mips-android-sysimage-license"
          ];
        };
        androidSdk = androidComposition.androidsdk;
      in
      {
        devShell =
          with pkgs;
          mkShell {
            ANDROID_EMULATOR_WAIT_TIME_BEFORE_KILL = "1";
            ANDROID_SDK_ROOT = "${androidSdk}/libexec/android-sdk";
            ANDROID_HOME = "${androidSdk}/libexec/android-sdk";
            ANDROID_AVD_HOME = "$HOME/.android/avd";
            JAVA_HOME = jdk17.home;
            FLUTTER_ROOT = flutter;
            DART_ROOT = "${flutter}/bin/cache/dart-sdk";
            GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${androidSdk}/libexec/android-sdk/build-tools/${buildToolsVer}/aapt2";
            QT_QPA_PLATFORM = "wayland;xcb";

            buildInputs = [
              androidSdk
              flutter
              gradle
              jdk17

              #rust-bin.stable.latest.default # Stable rust, default profile. If not sure, always choose this.
              # rust-toolchain
              rustup
              cargo
              rust-analyzer
            ];

            # See https://github.com/fzyzcjy/flutter_rust_bridge/issues/2527
            LD_LIBRARY_PATH = "/home/sofamaniac/Nextcloud/programmation/boostrs/lib/:$LD_LIBRARY_PATH";

            shellHook = ''
              if [ -z "$PUB_CACHE" ]; then
                export PATH="$PATH:$HOME/.pub-cache/bin"
              else
                export PATH="$PATH:$PUB_CACHE/bin"
              fi

              ${flutter} config --android-sdk $ANDROID_HOME --jdk-dir $JAVA_HOME > /dev/null 2>&1
            '';
          };
      }
    );
}
