{ pkgs, devenv, lib, config, inputs, ... }:

{
  # https://devenv.sh/basics/
  env.GREET = "devenv";

  android = {
    enable = true;
    flutter.enable = true;

     ndk = {
      enable = true;
      version = [ "26.3.11579264" ];
    };
    
    buildTools.version = [ "34.0.0" ];
    platforms.version = [ "33" "34" "35" ];
  };

  # https://devenv.sh/packages/
  packages = [ pkgs.git ];

  # https://devenv.sh/languages/
  languages.rust = {
    enable = true;
    channel = "stable";
    targets = [ 
      "aarch64-linux-android"
      "i686-linux-android"
      "x86_64-linux-android"
      "armv7-linux-androideabi"
    ];
  };

  # https://devenv.sh/processes/
  # processes.cargo-watch.exec = "cargo-watch";

  # https://devenv.sh/services/
  # services.postgres.enable = true;

  enterShell = ''
    export ENV_FILE="$DEVENV_ROOT/.env"
    export FLUTTER_DIR="$DEVENV_ROOT/crabir"
    export FRB_DIR="$DEVENV_ROOT/crabir_dir"
  '';

  # https://devenv.sh/tasks/
  # tasks = {
  #   "myproj:setup".exec = "mytool build";
  #   "devenv:enterShell".after = [ "myproj:setup" ];
  # };

  # https://devenv.sh/tests/
  enterTest = ''
    echo "Running tests"
    git --version | grep --color=auto "${pkgs.git.version}"
  '';

  scripts.run.exec = ''
    pushd $FLUTTER_DIR
    flutter_rust_bridge_codegen generate
    flutter run --dart-define-from-file $ENV_FILE
    popd
  '';

  scripts.build.exec = ''
    pushd $FLUTTER_DIR
    flutter_rust_bridge_codegen generate
    flutter build apk --dart-define-from-file $ENV_FILE
    popd
  '';

  # https://devenv.sh/git-hooks/
  # git-hooks.hooks.shellcheck.enable = true;

  # See full reference at https://devenv.sh/reference/options/
}
