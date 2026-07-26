package com.sofamaniac.crabir

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri

@Composable
fun SetShortcuts() {
    val currentAccount = LocalRedditAccount.current
    val context = LocalContext.current
    if (currentAccount.isAnonymous()) {
        ShortcutManagerCompat.removeAllDynamicShortcuts(context)
    } else {

        val intent = Intent(Intent.ACTION_VIEW, "com.sofamaniac.crabir://saved".toUri())
        intent.setPackage(context.packageName)

        val shortcut = ShortcutInfoCompat.Builder(context, "saved")
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_favorite))
            .setShortLabel("Saved")
            .setLongLabel("See saved links")
            .setIntent(intent)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

}