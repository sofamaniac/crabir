# Use one shell per recipe
.ONESHELL:

# Paths
MAKEFILE_DIR := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
FLUTTER_APP_DIR := $(MAKEFILE_DIR)/crabir

## Rust / FRB files
RUST_FILES := $(shell find reddit_api -type f -name "*.rs")
FRB_INPUT := $(shell find crabir_bridge/src/api -type f -name "*.rs")
FRB_OUTPUT := $(FLUTTER_APP_DIR)/lib/src/rust/frb_generated.rs

## L10N files
LANG_FILES := $(wildcard $(FLUTTER_APP_DIR)/lib/l10n/*.arb)
L10N_OUTPUT := $(FLUTTER_APP_DIR)/lib/l10n/app_localizations.dart

## Licenses files
LICENSES_PACKER_DIR := $(MAKEFILE_DIR)/licenses_packer
LICENSES_JSON := $(FLUTTER_APP_DIR)/assets/rust-licenses.json
LICENSES_DIR := $(FLUTTER_APP_DIR)/assets/rust-licenses
LOCKFILE := $(MAKEFILE_DIR)/Cargo.lock

## Dart files
DART_SRC := $(shell find $(FLUTTER_APP_DIR)/lib -type f -name '*.dart')
CODEGEN_OUTPUT := $(FLUTTER_APP_DIR)/build/generated

# Run codegen only if Dart sources changed
$(CODEGEN_OUTPUT): $(DART_SRC)
	cd $(FLUTTER_APP_DIR)
	dart run build_runner build -d
	# Touch a dummy file so Make knows it’s up-to-date
	touch $(CODEGEN_OUTPUT)

$(LICENSES_JSON): $(LOCKFILE)
	cd $(LICENSES_PACKER_DIR)
	cargo run --  --rust-folders=$(MAKEFILE_DIR)/reddit_api --assets-folder=$(FLUTTER_APP_DIR)/assets

$(FRB_OUTPUT): $(RUST_FILES) $(FRB_INPUT)
	cd $(FLUTTER_APP_DIR)
	flutter_rust_bridge_codegen generate --no-build-runner

$(L10N_OUTPUT): $(LANG_FILES)
	cd $(FLUTTER_APP_DIR)
	flutter gen-l10n

.PHONY: l10n
l10n: $(L10N_OUTPUT)

.PHONY: frb
frb: $(FRB_OUTPUT)

.PHONY: codegen
codegen: $(CODEGEN_OUTPUT)

.PHONY: licenses
licenses: $(LICENSES_JSON)

.PHONY: build
build: $(LICENSES_JSON) $(FRB_OUTPUT) $(CODEGEN_OUTPUT)
	cd $(FLUTTER_APP_DIR)
	flutter build apk --dart-define-from-file $(ENV_FILE)

.PHONY: debug
debug: $(FRB_OUTPUT) $(CODEGEN_OUTPUT)
	cd $(FLUTTER_APP_DIR)
	flutter run --dart-define-from-file $(ENV_FILE)

.PHONY: release
release: $(LICENSES_JSON) $(FRB_OUTPUT) $(CODEGEN_OUTPUT)
	cd $(FLUTTER_APP_DIR)
	flutter run --release --dart-define-from-file $(ENV_FILE)

.PHONY: appbundle
appbundle: $(LICENSES_JSON) $(FRB_OUTPUT) $(CODEGEN_OUTPUT)
	cd $(FLUTTER_APP_DIR)
	flutter build appbundle --dart-define-from-file $(ENV_FILE)
