use crate::{client::VoteDirection, votable::private::PrivateVotable};

pub(crate) mod private {
    use crate::model::Fullname;

    pub trait PrivateVotable {
        fn name(&self) -> &Fullname;
        fn set_likes(&mut self, likes: Option<bool>);
        fn set_saved(&mut self, saved: bool);
    }
}

pub trait Votable: PrivateVotable {
    async fn vote(
        &mut self,
        direction: VoteDirection,
        client: &crate::client::Client,
    ) -> crate::result::Result<()> {
        client.vote(self.name(), direction).await?;
        self.set_likes(direction.into());
        Ok(())
    }

    async fn save(&mut self, client: &crate::client::Client) -> crate::result::Result<()> {
        client.save(self.name()).await?;
        self.set_saved(true);
        Ok(())
    }
    async fn unsave(&mut self, client: &crate::client::Client) -> crate::result::Result<()> {
        client.unsave(self.name()).await?;
        self.set_saved(false);
        Ok(())
    }
}
