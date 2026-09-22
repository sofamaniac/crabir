package com.sofamaniac.crabir.domain.repository

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.MediaDownloader
import com.sofamaniac.crabir.settings.data.dataSettingsStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

@Single
class MediaDownloaderRepository(private val client: MediaDownloader, private val context: Context) {
    companion object {
        private const val TAG = "MediaDownloaderRepository"
    }

    suspend fun download(
        url: String,
        filename: String?,
        folder: String?,
        onProgress: (Int) -> Unit = {},
    ): Result<Pair<Uri, MediaType>> =
        withContext(Dispatchers.IO) {
            val destination = context.dataSettingsStore.data.first().downloadLocation?.toUri()
            client.download(url).fold(onSuccess = { body ->
                Log.d(
                    TAG,
                    """
                    url: $url
                    contentType: ${body.contentType()}
                    contentLength: ${body.contentLength()}
                    """.trimIndent()
                )

                val type = body.contentType()
                if (type == null) {
                    return@fold Result.failure(IllegalArgumentException("No content type"))
                }
                val filename =
                    filename ?: url.toUri().lastPathSegment?.split(".")?.first()
                    ?: System.currentTimeMillis()
                        .toString()
                runCatching {
                    val uri = if (destination != null) {
                        saveToStorage(
                            body,
                            filename,
                            type,
                            destination,
                            folder = folder,
                            onProgress = onProgress
                        )
                    } else {
                        saveToDownloads(context, body, filename, type, folder, onProgress)
                    }
                    Pair(uri, type)
                }.onFailure {
                    Log.e(TAG, "download", it)
                }
            }, onFailure = {
                Log.e(TAG, "download", it)
                Result.failure(it)
            })
        }

    private fun saveToStorage(
        body: ResponseBody,
        filename: String,
        type: MediaType,
        destination: Uri,
        folder: String?,
        onProgress: (Int) -> Unit,
    ): Uri {
        Log.d(TAG, "saveToStorage: $destination")
        val directory = DocumentFile.fromTreeUri(context, destination)
        if (directory == null) {
            Log.e(TAG, "saveToStorage: directory is null")
            throw IOException("Could not open directory")
        }
        val folder = if (folder != null) {
            directory.findFile(folder) ?: directory.createDirectory(folder)
        } else {
            directory
        }
        if (folder == null) {
            Log.e(TAG, "saveToStorage: final folder is null")
            throw IOException("Could not open directory")
        }
        val file = folder.createFile(type.toString(), filename)
        if (file == null) {
            Log.e(TAG, "saveToStorage: file is null")
            throw IOException("Could not create file")
        }
        val inputStream = body.byteStream()
        val outputStream = context.contentResolver.openOutputStream(file.uri)
        if (outputStream == null) {
            Log.e(TAG, "saveToStorage: output stream is null")
            throw IOException("Could not open output stream")
        }
        outputStream.use { output ->
            inputStream.use { input ->
                copyWithProgress(input, output, body.contentLength(), onProgress)
            }
        }
        return file.uri
    }
}

private fun saveToDownloads(
    context: Context,
    body: ResponseBody,
    filename: String,
    type: MediaType,
    folder: String?,
    onProgress: (Int) -> Unit,
): Uri {
    val directory =
        Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )

    if (!directory.exists()) {
        directory.mkdirs()
    }
    val folder =
        if (folder != null) {
            val subdirectory = File(directory, folder)
            if (!subdirectory.exists()) {
                subdirectory.mkdirs()
            }
            subdirectory
        } else {
            directory
        }
    val name = "$filename.${type.subtype}"

    val file = File(
        folder,
        name
    )

    FileOutputStream(file).use { output ->

        body.byteStream().use { input ->

            copyWithProgress(
                input = input,
                output = output,
                totalBytes = body.contentLength(),
                onProgress = onProgress,
            )
        }
    }

    MediaScannerConnection.scanFile(
        context,
        arrayOf(file.absolutePath),
        arrayOf(body.contentType()?.toString()),
        null
    )

    return Uri.fromFile(file)
}


private fun copyWithProgress(
    input: InputStream,
    output: OutputStream,
    totalBytes: Long,
    onProgress: (Int) -> Unit,
) {
    val buffer = ByteArray(32 * 1024)
    var downloadedBytes = 0L
    var lastProgress = 0
    while (true) {
        val read = input.read(buffer)
        if (read == -1) {
            break
        }

        output.write(buffer, 0, read)
        downloadedBytes += read

        if (totalBytes > 0) {
            val progress = (downloadedBytes * 100 / totalBytes).toInt().coerceAtMost(100)
            // Update every 10%
            if (progress / 10 != lastProgress / 10) {
                lastProgress = progress
                onProgress(progress)
            }
        }
    }
}

class MediaDownloadWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {
    private val repository: MediaDownloaderRepository by inject()

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_URL) ?: return Result.failure()
        val filename = inputData.getString(KEY_FILENAME) ?: return Result.failure()
        val folder = inputData.getString(KEY_FOLDER)
        DownloadNotification.createNotificationChannel(applicationContext)
        setForeground(DownloadNotification.createForegroundInfo(applicationContext, filename, 0))
        val savedUri =
            repository.download(
                url,
                folder = folder,
                filename = filename,
                onProgress = { progress ->
                    setProgressAsync(workDataOf(KEY_PROGRESS to progress))
                    setForegroundAsync(
                        DownloadNotification.createForegroundInfo(
                            applicationContext,
                            filename,
                            progress
                        )
                    )
                }
            ).onSuccess { res ->
                setProgress(
                    workDataOf(
                        KEY_PROGRESS to 100,
                        KEY_DESTINATION to res.first.toString()
                    )
                )
                DownloadNotification.complete(applicationContext, filename, res.first, res.second)
            }.onFailure {
                DownloadNotification.error(applicationContext, filename)
            }
        return savedUri.fold(onSuccess = {
            Result.success(workDataOf(KEY_DESTINATION to savedUri.toString()))
        }, onFailure = {
            Result.failure(workDataOf(KEY_ERROR to (it.message ?: "Download failed")))
        })
    }


    companion object {
        const val KEY_URL = "download_url"
        const val KEY_FILENAME = "filename"
        const val KEY_FOLDER = "folder"
        const val KEY_PROGRESS = "progress"
        const val KEY_DESTINATION = "uri"
        const val KEY_ERROR = "error"
    }
}

object DownloadNotification {
    private const val CHANNEL_ID = "download_channel"
    private const val NOTIFICATION_ID = 1001
    private const val COMPLETE_NOTIFICATION_ID = 1002
    private const val ERROR_NOTIFICATION_ID = 1003
    fun complete(
        applicationContext: Context,
        filename: String,
        savedUri: Uri,
        type: MediaType,
    ) {
        val viewIntent = Intent(
            Intent.ACTION_VIEW,
            savedUri
        ).apply {
            setDataAndType(
                savedUri,
                type.toString()
            )
            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            savedUri.hashCode(),
            viewIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download_done)
            .setContentTitle("Download complete")
            .setContentIntent(pendingIntent)
            .setContentText(filename)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .build()
        val manager =
            ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
        manager?.notify(COMPLETE_NOTIFICATION_ID, notification)
    }

    fun error(applicationContext: Context, filename: String) {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_error)
            .setContentTitle("Download failed")
            .setContentText(filename)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ERROR)
            .build()
        val manager =
            ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
        manager?.notify(ERROR_NOTIFICATION_ID, notification)
    }

    fun createForegroundInfo(
        applicationContext: Context,
        filename: String,
        progress: Int,
    ): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle("Downloading")
            .setContentText(filename)
            .setProgress(100, progress, false)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .build()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID, notification)
        }
    }

    fun createNotificationChannel(applicationContext: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Media downloads",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notifications for media downloads"
        }
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

object MediaDownloadManager {
    fun enqueue(
        context: Context,
        url: String,
        filename: String,
        folder: String?,
    ): UUID {
        val inputData = workDataOf(
            MediaDownloadWorker.KEY_URL to url,
            MediaDownloadWorker.KEY_FILENAME to filename,
            MediaDownloadWorker.KEY_FOLDER to folder
        )
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val request = OneTimeWorkRequestBuilder<MediaDownloadWorker>().setInputData(inputData)
            .setConstraints(constraints).build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "media_download_${url}_$filename",
            ExistingWorkPolicy.KEEP,
            request
        )
        return request.id
    }
}
