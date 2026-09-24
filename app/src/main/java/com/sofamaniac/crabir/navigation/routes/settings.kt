package com.sofamaniac.crabir.navigation.routes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
class SettingsPage(val route: SettingsRoute? = null) : Route

@Serializable
object ViewManagerRoute : Route

@Serializable
object SortManagerRoute : Route


sealed interface SettingsRoute : Parcelable {
    sealed interface DetailsRoute : SettingsRoute
    sealed interface ExtraRoute : DetailsRoute

    @Serializable
    @Parcelize
    object Main : SettingsRoute

    @Serializable
    @Parcelize
    object Licenses : DetailsRoute

    sealed interface General : SettingsRoute {
        @Serializable
        @Parcelize
        object Main : General, DetailsRoute

        @Serializable
        @Parcelize
        object Feeds : General, ExtraRoute

        @Serializable
        @Parcelize
        object Posts : General, ExtraRoute

        @Serializable
        @Parcelize
        object Comments : General, ExtraRoute

        @Serializable
        @Parcelize
        object Views : General, ExtraRoute

        @Serializable
        @Parcelize
        object LateralMenu : General, ExtraRoute
    }

    sealed interface Theme : SettingsRoute {
        @Serializable
        @Parcelize
        object Main : Theme, DetailsRoute

        @Serializable
        @Parcelize
        object Editor : Theme, ExtraRoute
    }

    @Serializable
    @Parcelize
    object Filters : DetailsRoute

    @Serializable
    @Parcelize
    object DebugOptions : DetailsRoute

    @Serializable
    @Parcelize
    object Api : DetailsRoute

    @Serializable
    @Parcelize
    object Data : DetailsRoute

    @Serializable
    @Parcelize
    object History : DetailsRoute
}
