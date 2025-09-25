# Use one shell per recipe
.ONESHELL:

# Paths
RUST_CRATE_DIR := reddit_api
FLUTTER_APP_DIR := crabir
LICENSES_PACKER_DIR := licenses_packer

LICENSES_JSON := $(FLUTTER_APP_DIR)/assets/rust-licenses.json
LICENSES_DIR := $(FLUTTER_APP_DIR)/assets/rust-licenses
STAMP_FILE := $(LICENSES_DIR)/.stamp
LOCKFILE := Cargo.lock

.PHONY: all
all: build

.PHONY: licenses
licenses: $(LOCKFILE)
	pushd $(LICENSES_PACKER_DIR)
	cargo run
	popd

.PHONY: frb
frb: $(RUST_CRATE_DIR)
	pushd $(FLUTTER_APP_DIR)
	flutter_rust_bridge_codegen generate --no-build-runner
	popd

.PHONY: l10n
l10n:
	pushd $(FLUTTER_APP_DIR)
	flutter gen-l10n
	popd

.PHONY: dart_build
dart_build: l10n
	pushd $(FLUTTER_APP_DIR)
	dart run build_runner build -d
	popd

.PHONY: build
build: licenses frb dart_build
	pushd $(FLUTTER_APP_DIR)
	flutter build apk --dart-define-from-file $(ENV_FILE)
	popd

.PHONY: debug
debug: frb dart_build
	pushd $(FLUTTER_APP_DIR)
	flutter run --dart-define-from-file $(ENV_FILE)
	popd

.PHONY: release
release: frb dart_build
	pushd $(FLUTTER_APP_DIR)
	flutter run --release --dart-define-from-file $(ENV_FILE)
	popd
