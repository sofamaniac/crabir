package com.sofamaniac.crabir.navigation

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.navigation.routes.SettingsRoute
import kotlinx.serialization.json.Json

val FullnameType = object : NavType<Fullname>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Fullname? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Fullname::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Fullname {
        return Fullname(value)
    }

    override fun put(bundle: Bundle, key: String, value: Fullname) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: Fullname): String {
        return value.name
    }
}

val NullableFullnameType = object : NavType<Fullname?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Fullname? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Fullname::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Fullname? {
        return if (value == "null") null else Fullname(value)
    }

    override fun put(bundle: Bundle, key: String, value: Fullname?) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: Fullname?): String {
        return value?.name ?: "null"
    }
}

val NullableSettingsRoute = object : NavType<SettingsRoute?>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): SettingsRoute? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, SettingsRoute::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): SettingsRoute? {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: SettingsRoute?) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: SettingsRoute?): String {
        return Json.encodeToString(value)
    }
}
