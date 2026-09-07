package com.sofamaniac.crabir.domain.repository.feed

import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.repository.LinksRepository
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Singleton

@Singleton
class HistoryRepository(
    private val visitedPostsDao: VisitedPostsDao,
    override val votableRepository: LinksRepository,
) : PostFeedRepository<FeedParams>() {

    val json = Json { ignoreUnknownKeys = true }

    override suspend fun getThings(
        after: Fullname,
        params: FeedParams,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        val timestamp = runCatching {
            if (after.name.isBlank()) {
                System.currentTimeMillis()
            } else {
                after.name.toLong()
            }
        }.getOrElse { e ->
            return PagingSource.LoadResult.Error(e)
        }
        val entities =
            visitedPostsDao.getHistory(before = timestamp)
        val nextPage = entities.lastOrNull()?.let {
            visitedPostsDao.getPost(it.id)
        }
        return PagingSource.LoadResult.Page(
            entities.map { it.id },
            nextKey = nextPage?.id,
            prevKey = null
        )
    }
}
