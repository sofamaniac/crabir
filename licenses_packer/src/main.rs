use serde::{Deserialize, Serialize};
use std::{fs, path::PathBuf};

#[derive(Deserialize, Serialize, Debug)]
struct CrateInfo {
    name: String,
    version: Option<String>,
    license: Option<String>,
    repository: Option<String>,
    license_files: Option<Vec<String>>,
}

fn main() {
    let asset_dir = PathBuf::from("../crabir/assets");
    let json_path = asset_dir.clone().join("rust-licenses.json");
    let licenses_dir = asset_dir.clone().join("rust-licenses/");

    let data = fs::read_to_string(&json_path).expect("read rust-licenses.json");
    let mut crates: Vec<CrateInfo> = serde_json::from_str(&data).expect("parse JSON");

    for dep in &mut crates {
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

    let out = serde_json::to_string_pretty(&crates).unwrap();
    fs::write(json_path, out).expect("write updated rust-licenses.json");
}
