@file:UseSerializers(ColorSerializer::class)

package com.sofamaniac.reboost.settings

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.datastore.dataStore
import com.sofamaniac.reboost.LocalTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


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
data class ReboostTheme(
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

    fun updateFieldValue(field: ColorFields, value: Color): ReboostTheme {
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
}

val DefaultReboostTheme = ReboostTheme(
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
)

val Context.themeDataStore by dataStore(
    fileName = "reboost_theme.json",
    serializer = DataStoreJsonSerializer(
        serializer = ReboostTheme.serializer(),
        defaultValue = DefaultReboostTheme
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
fun rememberAppTheme(): ReboostTheme {
    val context = LocalContext.current
    val themeDataStore = remember(context) { context.themeDataStore }
    val theme by themeDataStore.data.collectAsState(
        initial = DefaultReboostTheme,
    )
    Log.d("Theme", "recompose $theme")
    return theme
}

@Composable
fun ProvideReboostTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val theme = LocalTheme.current
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> theme.toDarkColorScheme()
        else -> theme.toLightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        //typography = Typography,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeEditor() {
    val context = LocalContext.current
    val themeDataStore = remember(context) { context.themeDataStore }
    val theme by themeDataStore.data.collectAsState(
        initial = DefaultReboostTheme
    )
    var activeColorField by remember { mutableStateOf<ColorFields?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Theme editor") })
        },
    ) { paddingValues ->
        activeColorField?.let { field ->
            var currentColor by remember { mutableStateOf(theme.getFieldValue(field)) }
            ColorPickerDialogue(
                currentColor,
                onColorChange = {
                    currentColor = it
                }, onDismissRequest = {
                    activeColorField = null
                },
                applyChanges = {
                    scope.launch {
                        themeDataStore.updateData {
                            theme.updateFieldValue(field, currentColor)
                        }
                        activeColorField = null
                    }
                }
            )
        }
        Column(modifier = Modifier.padding(paddingValues)) {
            ThemePreviewer { activeColorField = it }
            TextButton(onClick = {
                scope.launch {
                    themeDataStore.updateData { DefaultReboostTheme }
                }
            }) {
                Text("Reset to default")
            }
            LazyColumn {
                items(ColorFields.entries.size, key = { ColorFields.entries[it] }) { field ->
                    val field = ColorFields.entries[field]
                    ListItem(
                        headlineContent = { Text(field.name) },
                        trailingContent = {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(theme.getFieldValue(field))
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        },
                        modifier = Modifier.clickable {
                            activeColorField = field
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ThemePreviewer(setActiveField: (ColorFields) -> Unit) {
    val theme = rememberAppTheme()
    Card(
        modifier = Modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = theme.cardBackground)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Post title", modifier = Modifier.clickable {
                setActiveField(ColorFields.PostTitle)
            }, color = theme.postTitle)
            Text("Read", modifier = Modifier.clickable {
                setActiveField(ColorFields.ReadPost)
            }, color = theme.readPost)
            Text("Announcement", modifier = Modifier.clickable {
                setActiveField(ColorFields.Announcement)
            }, color = theme.announcement)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Community", modifier = Modifier.clickable {
                setActiveField(ColorFields.Highlight)
            }, color = theme.highlight)
            Text("Secondary text", modifier = Modifier.clickable {
                setActiveField(ColorFields.SecondaryText)
            }, color = theme.secondaryText)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Content text", modifier = Modifier.clickable {
                setActiveField(ColorFields.ContentColor)
            }, color = theme.contentColor)
            Text("Link", modifier = Modifier.clickable {
                setActiveField(ColorFields.LinkColor)
            }, color = theme.linkColor)
            Text("Downvote", modifier = Modifier.clickable {
                setActiveField(ColorFields.Downvote)
            }, color = theme.downvote)
        }
    }
}


@Composable
fun ColorPickerDialogue(
    color: Color,
    onColorChange: (Color) -> Unit,
    onDismissRequest: () -> Unit,
    applyChanges: () -> Unit,
) {
    var advancedMode by remember { mutableStateOf(true) }
    Dialog(onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), shape = RoundedCornerShape(16.dp)
        ) {
            ColorPicker(color, onColorChange, advancedMode)
            Row() {
                TextButton(onClick = { advancedMode = !advancedMode }) {
                    if (advancedMode) {
                        Text("Presets")
                    } else {
                        Text("Custom")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
                TextButton(onClick = applyChanges) {
                    Text("Confirm")
                }
            }
        }
    }
}
