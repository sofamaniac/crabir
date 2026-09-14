package com.sofamaniac.crabir.navigation.routes

import com.sofamaniac.crabir.ui.user.ProfileTabs
import kotlinx.serialization.Serializable

@Serializable
class ProfileRoute(val username: String, val tab: ProfileTabs = ProfileTabs.Overview) : Route

@Serializable
object SavedRoute : Route {
    const val URL = "crabir://saved"
}
