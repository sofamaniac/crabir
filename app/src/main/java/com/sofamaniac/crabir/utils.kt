package com.sofamaniac.crabir

fun <T> Result<T>.toOption(): T? {
    return if (isSuccess) {
        getOrNull()
    } else {
        null
    }
}

inline fun <T, F> T?.map(default: () -> F, map: (T) -> F): F {
    return if (this != null) {
        map(this)
    } else {
        default()
    }
}
