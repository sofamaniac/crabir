package com.sofamaniac.crabir.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute : Route

@Serializable
object SubscriptionsRoute : Route

@Serializable
class SearchRoute(val subreddit: String = "", val flair: String = "", val initialTab: Int? = null) :
    Route {
    companion object {
        const val URL = "crabir://search"
    }
}

@Serializable
object InboxRoute : Route

@Serializable
object HistoryRoute : Route

@Serializable
/**
 * @param subreddit the prefixed display name of the subreddit
 */
class SubredditRoute(val subreddit: String) : Route {
    init {
        assert(subreddit.contains("/"))
    }
}


@Serializable
/**
 * @param subreddit the prefixed display name of the subreddit
 */
class SubredditInfoRoute(val subreddit: String) : Route {

    init {
        assert(subreddit.startsWith("r/"))
    }
}

@Serializable
class MultiRoute(val name: String) : Route {
    init {
        assert(name.startsWith("m/"))
    }
}

@Serializable
class MultiInfoRoute(val name: String) : Route {
    init {
        assert(name.startsWith("m/"))
    }
}
