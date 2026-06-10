package com.sofamaniac.crabir.ui.media.image

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlin.time.Clock

@Composable
fun DownloadButton(url: Uri) {
    val context = LocalContext.current
    var isDownloading by remember { mutableStateOf(false) }
    IconButton(onClick = {
        if (!isDownloading) {
            Toast.makeText(context, "Downloading Image", Toast.LENGTH_SHORT).show()
            saveToStorage(context, url)
        }
    }) {
        Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
    }
}

fun saveToStorage(context: Context, url: Uri) {
    val request = DownloadManager.Request(url)
    val name = url.lastPathSegment ?: Clock.System.now().toString()
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
    request.setTitle(name)
    request.setDescription("Downloading Media")
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_PICTURES, name)
    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    manager.enqueue(request)
}