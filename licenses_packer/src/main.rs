use clap::Parser;
use serde::{Deserialize, Serialize};
use std::{fs, path::PathBuf};
use subprocess::{Popen, PopenConfig, Redirection};

#[derive(Parser)]
#[command(version, about, long_about = None)]
struct Cli {
    /// List of folders for which to extract the licenses
    #[arg(long)]
    rust_folders: Vec<PathBuf>,
    /// Path to the flutter assets folder
    #[arg(long)]
    assets_folder: PathBuf,
}

#[derive(Deserialize, Serialize, Debug)]
struct CrateInfo {
    name: String,
    version: Option<String>,
    license: Option<String>,
    repository: Option<String>,
    license_files: Option<Vec<String>>,
}

fn main() {
    let cli = Cli::parse();

    let json_path = cli.assets_folder.clone().join("rust-licenses.json");
    let licenses_dir = cli.assets_folder.clone().join("rust-licenses/");

    let mut json: Vec<CrateInfo> = Vec::new();

    for folder in cli.rust_folders {
        // Obtaining json
        let mut proc = Popen::create(
            &["cargo", "license", "--json"],
            PopenConfig {
                stdout: Redirection::Pipe,
                cwd: Some(folder.clone().into_os_string()),
                ..Default::default()
            },
        )
        .expect("Failed to initialize proc");
        let (out, _) = proc.communicate(None).expect("Failure in proc");
        let mut data: Vec<CrateInfo> = serde_json::from_str(&out.expect("Unexpected empty output"))
            .expect("Failed to parse CrateInfo");
        json.append(&mut data);

        // Fetching licenses files
        let mut proc = Popen::create(
            &[
                "cargo",
                "licenses",
                "collect",
                "--path",
                licenses_dir.to_str().expect("Invalid assets dir"),
            ],
            PopenConfig {
                cwd: Some(folder.clone().into_os_string()),
                ..Default::default()
            },
        )
        .expect("Failed to initialize proc");
        let (_, _) = proc.communicate(None).expect("Failure in proc");
    }

    for dep in &mut json {
        // Cargo licenses normalises file names
        let dep_name = dep.name.replace("-", "_");
        let mut files = Vec::new();

        let licenses_dir = fs::read_dir(&licenses_dir).expect("Could not find licenses dir");
        for file in licenses_dir {
            if file.is_err() {
                continue;
            }
            let file = file.unwrap();
            let file_name = file
                .file_name()
                .into_string()
                .expect("Could not convert OS String");
            let parts: Vec<_> = file_name.split("-LICENSE").collect();
            if parts[0] == dep_name {
                let path: PathBuf = file.path().iter().skip_while(|s| *s != "assets").collect();
                files.push(path.to_string_lossy().into_owned());
            }
        }
        dep.license_files = Some(files);
    }

    let out = serde_json::to_string_pretty(&json).unwrap();
    fs::write(json_path, out).expect("write updated rust-licenses.json");
}
