package com.sofamaniac.crabir

import android.net.Uri
import androidx.core.net.toUri

fun <T> Result<T>.toOption(): T? {
    return if (isSuccess) {
        getOrNull()
    } else {
        null
    }
}

fun <T> Result<Result<T>>.flatten(): Result<T> {
    return this.fold(onSuccess = { it }, onFailure = { Result.failure(it) })
}

inline fun <T, F> T?.map(default: () -> F, map: (T) -> F): F {
    return if (this != null) {
        map(this)
    } else {
        default()
    }
}

fun isVideoUrl(url: String): Boolean {
    return url.toUri().isVideoUrl()
}

fun Uri.isVideoUrl(): Boolean {
    val videoExtensions = listOf("gif", "mp4", "webm", "webp")
    val extension = lastPathSegment?.split(".")?.last()
    return extension in videoExtensions
}

fun Uri.isImageUrl(): Boolean {
    val extensions = listOf("jpg", "png", "jpeg", "png", "svg")
    val extension = lastPathSegment?.split(".")?.last()
    return extension in extensions
}

fun isImageUrl(url: String): Boolean {
    return url.toUri().isImageUrl()
}
