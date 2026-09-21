package com.sofamaniac.crabir.ui.media.image

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.sofamaniac.crabir.LocalDataSettings
import com.sofamaniac.crabir.R
import java.io.File

@Composable
fun DownloadButton(url: Uri, subreddit: String? = null) {
    val settings = LocalDataSettings.current
    val context = LocalContext.current
    val toast = stringResource(R.string.downloading)
    val download = {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
        val destination = settings.downloadLocation?.toUri()
        val subfolder =
            if (settings.subfolderPerCommunity && subreddit != null) subreddit else null
        saveToStorage(context, url, destination, subfolder)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                download()
            }
        }
    )
    IconButton(onClick = {
        if (!context.canWriteToDownload()) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            download()
        }
    }) {
        Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
    }
}

private fun Context.canWriteToDownload(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}

fun saveToStorage(
    context: Context,
    url: Uri,
    destination: Uri?,
    subfolder: String?,
) {
    Log.d("DownloadButton", "saveToStorage: $url")
    val request = DownloadManager.Request(url)
    val name = url.lastPathSegment ?: return
    Log.d("DownloadButton", "saveToStorage: $destination")
    request.setTitle(name)
    request.setDescription("Downloading Media")
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    val subreddit = subfolder?.let { it + File.separator }?.ifBlank { "" }
    val subPath = listOfNotNull("crabir", subreddit, name).joinToString(
        File.separator,
        prefix = File.separator
    )
    Log.d("DownloadButton", "saveToStorage: $subPath")
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subPath)
    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    manager.enqueue(request)
}
