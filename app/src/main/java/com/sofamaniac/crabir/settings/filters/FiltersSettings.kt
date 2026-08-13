package com.sofamaniac.crabir.settings.filters

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.datastore.dataStore
import com.sofamaniac.crabir.LocalFiltersSettings
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.settings.DataStoreJsonSerializer
import kotlinx.serialization.Serializable

@Serializable
data class FiltersSettings(
    val showNSFW: Boolean = true,
    val showNSFWMedia: Boolean = true,
    val blurNSFW: Boolean = false,
    val titleFilters: List<String> = emptyList(),
    val authorFilters: List<String> = emptyList(),
    val domainFilters: List<String> = emptyList(),
    val subredditFilters: List<String> = emptyList(),
    val flairFilters: List<String> = emptyList(),
) {
    fun addSubreddit(subreddit: String): FiltersSettings {
        assert(!subreddit.contains("r/"))
        return copy(subredditFilters = subredditFilters + subreddit)
    }

    fun addAuthor(author: String): FiltersSettings {
        assert(!author.contains("u/"))
        return copy(authorFilters = authorFilters + author)
    }
}

val Context.filtersDataStore by dataStore(
    fileName = "reboost_filters.json",
    serializer = DataStoreJsonSerializer(
        serializer = FiltersSettings.serializer(),
        defaultValue = FiltersSettings()
    )
)

@Composable
internal fun rememberFiltersSettings(): FiltersSettings? {
    val context = LocalContext.current
    val filtersSettingsDataStore = remember(context) { context.filtersDataStore }
    val filtersSettings by filtersSettingsDataStore.data.collectAsState(
        initial = null,
    )
    return filtersSettings
}

@Composable
fun rememberPostsFilter(
    whitelistSubreddit: List<String> = emptyList(),
    whitelistAuthor: List<String> = emptyList(),
): (PostData) -> Boolean {
    val settings = LocalFiltersSettings.current
    assert(whitelistSubreddit.none { it.contains("r/") })
    assert(whitelistAuthor.none { it.contains("u/") })
    return { post ->
        val title = post.title
        val author = post.author.username
        val domain = post.url.toUri().host ?: ""
        val subreddit = post.subreddit.name
        val flair = post.linkFlair.text

        val filteredOnTitle = settings.titleFilters.any { Regex(it).matches(title) }
        val filteredOnDomain = settings.domainFilters.any { Regex(it).matches(domain) }
        val filteredOnSubreddit = settings.subredditFilters.any { Regex(it).matches(subreddit) }
            .and(whitelistSubreddit.none { Regex(it).matches(subreddit) })
        val filteredOnAuthor = settings.authorFilters.any { Regex(it).matches(author) }.and(
            whitelistAuthor.none { Regex(it).matches(author) }
        )
        val filteredOnFlair = settings.flairFilters.any { Regex(it).matches(flair) }

        !(filteredOnTitle || filteredOnDomain || filteredOnSubreddit || filteredOnAuthor || filteredOnFlair)
    }
}