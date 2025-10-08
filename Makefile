# Use one shell per recipe
.ONESHELL:

# Paths
FLUTTER_APP_DIR := crabir

## Rust / FRB files
RUST_FILES := $(wildcard reddit_api/src/*)
FRB_INPUT := $(wildcard crabir_bridge/src/api/*)
FRB_OUTPUT := crabir_bridge/src/frb_generated.rs

## L10N files
LANG_FILES := $(wildcard $(FLUTTER_APP_DIR)/lib/l10n/*.arb)
L10N_OUTPUT := $(FLUTTER_APP_DIR)/lib/l10n/app_localizations.dart

## Licenses files
LICENSES_PACKER_DIR := licenses_packer
LICENSES_JSON := $(FLUTTER_APP_DIR)/assets/rust-licenses.json
LICENSES_DIR := $(FLUTTER_APP_DIR)/assets/rust-licenses
LOCKFILE := Cargo.lock

$(LICENSES_JSON): $(LOCKFILE)
	cd $(LICENSES_PACKER_DIR)
	cargo run

$(FRB_OUTPUT): $(RUST_CRATE_DIR) $(FRB_INPUT)
	cd $(FLUTTER_APP_DIR)
	flutter_rust_bridge_codegen generate --no-build-runner

$(L10N_OUTPUT): $(LANG_FILES)
	cd $(FLUTTER_APP_DIR)
	flutter gen-l10n

.PHONY: dart_build
dart_build: $(L10N_OUTPUT)
	cd $(FLUTTER_APP_DIR)
	dart run build_runner build -d

.PHONY: build
build: $(LICENSES_JSON) $(FRB_OUTPUT) dart_build
	cd $(FLUTTER_APP_DIR)
	flutter build apk --dart-define-from-file $(ENV_FILE)

.PHONY: debug
debug: $(FRB_OUTPUT) dart_build
	cd $(FLUTTER_APP_DIR)
	flutter run --dart-define-from-file $(ENV_FILE)

.PHONY: release
release: $(LICENSES_JSON) $(FRB_OUTPUT) dart_build
	cd $(FLUTTER_APP_DIR)
	flutter run --release --dart-define-from-file $(ENV_FILE)

