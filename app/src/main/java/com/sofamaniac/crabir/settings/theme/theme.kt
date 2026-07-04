@file:UseSerializers(ColorSerializer::class)

package com.sofamaniac.crabir.settings.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.datastore.dataStore
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.settings.DataStoreJsonSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


enum class ColorFields {
    Background,
    CardBackground,
    ToolbarBackground,
    ToolbarText,
    PrimaryColor,
    Highlight,
    PostTitle,
    ReadPost,
    Announcement,
    ContentColor,
    LinkColor,
    SecondaryText,
    Downvote,
}

@Serializable
data class CrabirTheme(
    val background: Color,
    val cardBackground: Color,
    val toolbarBackground: Color,
    val toolbarText: Color,
    val primaryColor: Color,
    val highlight: Color,
    val postTitle: Color,
    val readPost: Color,
    val announcement: Color,
    val contentColor: Color,
    val linkColor: Color,
    val secondaryText: Color,
    val downvote: Color,
    val saved: Color,
) {
    fun getFieldValue(field: ColorFields): Color {
        return when (field) {
            ColorFields.Background -> background
            ColorFields.CardBackground -> cardBackground
            ColorFields.ToolbarBackground -> toolbarBackground
            ColorFields.ToolbarText -> toolbarText
            ColorFields.PrimaryColor -> primaryColor
            ColorFields.Highlight -> highlight
            ColorFields.PostTitle -> postTitle
            ColorFields.ReadPost -> readPost
            ColorFields.Announcement -> announcement
            ColorFields.ContentColor -> contentColor
            ColorFields.LinkColor -> linkColor
            ColorFields.SecondaryText -> secondaryText
            ColorFields.Downvote -> downvote
        }
    }

    fun updateFieldValue(field: ColorFields, value: Color): CrabirTheme {
        val newTheme = when (field) {
            ColorFields.Background -> this.copy(background = value)
            ColorFields.CardBackground -> this.copy(cardBackground = value)
            ColorFields.ToolbarBackground -> this.copy(toolbarBackground = value)
            ColorFields.ToolbarText -> this.copy(toolbarText = value)
            ColorFields.PrimaryColor -> this.copy(primaryColor = value)
            ColorFields.Highlight -> this.copy(highlight = value)
            ColorFields.PostTitle -> this.copy(postTitle = value)
            ColorFields.ReadPost -> this.copy(readPost = value)
            ColorFields.Announcement -> this.copy(announcement = value)
            ColorFields.ContentColor -> this.copy(contentColor = value)
            ColorFields.LinkColor -> this.copy(linkColor = value)
            ColorFields.SecondaryText -> this.copy(secondaryText = value)
            ColorFields.Downvote -> this.copy(downvote = value)
        }
        return newTheme
    }

    internal fun toDarkColorScheme(): ColorScheme {
        return darkColorScheme(
            primary = primaryColor,
            background = background,
            surface = cardBackground,
            surfaceVariant = toolbarBackground,
            tertiary = highlight,
        )
    }

    internal fun toLightColorScheme(): ColorScheme {
        return lightColorScheme(
            primary = primaryColor,
            background = background,
            surface = cardBackground,
            surfaceVariant = toolbarBackground,
            tertiary = highlight,
        )
    }

    companion object {
        internal fun fromColorScheme(colorScheme: ColorScheme): CrabirTheme {
            return CrabirTheme(
                primaryColor = colorScheme.primary,
                background = colorScheme.background,
                cardBackground = colorScheme.primaryContainer,
                contentColor = colorScheme.onPrimaryContainer,
                toolbarBackground = colorScheme.surfaceVariant,
                highlight = colorScheme.tertiary,
                toolbarText = colorScheme.onSurfaceVariant,
                postTitle = colorScheme.onPrimaryContainer,
                readPost = colorScheme.onSurface,
                announcement = Color(0xff00ff00),
                linkColor = Color(0xff4b91e2),
                downvote = Color(0xFF448AFF),
                secondaryText = colorScheme.onPrimaryFixedVariant,
                saved = Color(0xFFFFD740)
            )
        }
    }
}

val DefaultDarkTheme = CrabirTheme(
    background = Color.Black,
    cardBackground = Color.Black,
    toolbarBackground = Color.Black,
    toolbarText = Color.White,
    primaryColor = Color(0xffff6e40),
    secondaryText = Color(0xffb7b8bc),
    highlight = Color(0xffff0000),
    postTitle = Color(0xfff5f6f8),
    readPost = Color(0xffb7b8bc),
    announcement = Color(0xff00ff00),
    contentColor = Color(0xfff5f6f8),
    linkColor = Color(0xff4b91e2),
    downvote = Color(0xFF448AFF),
    saved = Color(0xFFFFD740),
)

val DefaultLightTheme = CrabirTheme(
    background = Color.White,
    cardBackground = Color.White,
    toolbarBackground = Color.White,
    toolbarText = Color.Black,
    primaryColor = Color(0xffff6e40),
    secondaryText = Color(0xffb7b8bc),
    highlight = Color(0xffff0000),
    postTitle = Color.Black,
    readPost = Color(0xffb7b8bc),
    announcement = Color(0xff00ff00),
    contentColor = Color.Black,
    linkColor = Color(0xff4b91e2),
    downvote = Color(0xFF448AFF),
    saved = Color(0xFFFFD740),
)

val AUTHOR_CARTOUCHE_COLOR = Color(0xFF448AFF)
val MODERATOR_CARTOUCHE_COLOR = Color(0xFF388E3C)
val GIF_CARTOUCHE_COLOR = Color(0xFF0097A7)
val VIDEO_CARTOUCHE_COLOR = Color(0xFFE64A19)
val ADMIN_CARTOUCHE_COLOR = Color(0xFFE64A19)
val YOUTUBE_CARTOUCHE_COLOR = Color(0xFFC00000)

enum class ThemeMode {
    Dark, Light, System, Scheduled;

    fun toStringResource(): Int {
        return when (this) {
            Dark -> R.string.DarkMode
            Light -> R.string.LightMode
            System -> R.string.SystemMode
            Scheduled -> R.string.ScheduledMode
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class ThemeSettings(
    val dark: CrabirTheme,
    val light: CrabirTheme,
    val mode: ThemeMode,
    val dynamicColor: Boolean,
    val lightModeStartTime: Int = 6,
    val lightModeEndTime: Int = 18,
) {

    fun currentTheme(default: ThemeMode): CrabirTheme {
        assert(default != ThemeMode.System)
        val mode = if (mode == ThemeMode.System) default else mode
        return when (mode) {
            ThemeMode.Dark -> dark
            ThemeMode.Light -> light
            else -> {
                throw Exception("Unreachable code")
            }
        }
    }

    companion object {
        val DEFAULT = ThemeSettings(DefaultDarkTheme, DefaultLightTheme, ThemeMode.System, true)
    }
}

val Context.themeDataStore by dataStore(
    fileName = "reboost_theme.json",
    serializer = DataStoreJsonSerializer(
        serializer = ThemeSettings.serializer(),
        defaultValue = ThemeSettings.DEFAULT
    )
)


object ColorSerializer : KSerializer<Color> {
    override val descriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeLong(value.value.toLong())
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color(decoder.decodeLong().toULong())
    }
}

@Composable
fun rememberThemeSettings(): ThemeSettings {
    val context = LocalContext.current
    val themeDataStore = remember(context) { context.themeDataStore }
    val theme by themeDataStore.data.collectAsState(
        initial = ThemeSettings.DEFAULT,
    )
    return theme
}

@Composable
fun rememberThemeMode(): ThemeMode {
    val theme = rememberThemeSettings()
    return when (theme.mode) {
        ThemeMode.System -> if (isSystemInDarkTheme()) ThemeMode.Dark else ThemeMode.Light
        else -> theme.mode
    }
}

@Composable
fun rememberAppTheme(): CrabirTheme {
    val theme = rememberThemeSettings()
    val colorScheme = MaterialTheme.colorScheme
    val dynamicTheme = CrabirTheme.fromColorScheme(colorScheme)
    val mode = rememberThemeMode()
    val crabirTheme = theme.currentTheme(mode)
    setSystemBarsColor()(theme.mode, crabirTheme.toolbarBackground)
    if (theme.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        return dynamicTheme
    }
    Log.d("rememberAppTheme", "rememberAppTheme: ${theme.mode}")
    return crabirTheme
}

@Composable
fun setSystemBarsColor(): (ThemeMode, Color) -> Unit {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window
    val windowInsetsController =
        WindowCompat.getInsetsController(window!!, window.decorView)
    return { mode, color ->
        if (Build.VERSION.SDK_INT < 35) {
            window.navigationBarColor = color.toArgb()
        }
        when (mode) {
            ThemeMode.Dark -> {
                windowInsetsController.isAppearanceLightStatusBars = false
                windowInsetsController.isAppearanceLightNavigationBars = false
            }

            ThemeMode.Light -> {
                windowInsetsController.isAppearanceLightStatusBars = true
                windowInsetsController.isAppearanceLightNavigationBars = true
            }

            else -> {}
        }
    }
}

@Composable
fun ConfigureMaterialTheme(
    content: @Composable () -> Unit,
) {

    val themeSettings = rememberThemeSettings()
    val context = LocalContext.current
    val darkModeEnabled = when (themeSettings.mode) {
        ThemeMode.Dark -> true
        ThemeMode.Light -> false
        ThemeMode.System -> isSystemInDarkTheme()
        else -> false
    }


    val colorScheme = when {
        themeSettings.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkModeEnabled) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                context
            )
        }

        darkModeEnabled -> themeSettings.dark
            .toDarkColorScheme()

        else -> themeSettings.light.toLightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        //typography = Typography,
        content = content
    )
}
