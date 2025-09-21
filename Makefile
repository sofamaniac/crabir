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

# Default target
.PHONY: all
all: build

# Generate license text files + enrich JSON
licenses: $(LOCKFILE)
	mkdir -p $(LICENSES_DIR)
	pushd $(RUST_CRATE_DIR)
	cargo license --json --output ../$(LICENSES_JSON)
	cargo licenses collect --path ../$(LICENSES_DIR)
	popd
	pushd $(LICENSES_PACKER_DIR)
	cargo run
	popd

# Combined target to generate all license assets
.PHONY: licenses

# Flutter Rust Bridge codegen
.PHONY: frb
frb:
	pushd $(FLUTTER_APP_DIR)
	flutter_rust_bridge_codegen generate
	popd

# Flutter build (ensures licenses and FRB are up to date)
.PHONY: build
build: licenses frb
	pushd $(FLUTTER_APP_DIR)
	flutter build apk --dart-define-from-file $(ENV_FILE)
	popd

