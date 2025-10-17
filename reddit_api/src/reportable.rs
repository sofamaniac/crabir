use crate::{client, result::Result};

pub trait Reportable {
    async fn report(client: client::Client, reason: String) -> Result<()> {
        Ok(())
    }
}
