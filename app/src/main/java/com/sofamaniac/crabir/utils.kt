package com.sofamaniac.crabir

fun <T> Result<T>.toOption(): T? {
    return if (isSuccess) {
        getOrNull()
    } else {
        null
    }
}
