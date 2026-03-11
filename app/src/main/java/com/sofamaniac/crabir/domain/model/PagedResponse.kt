package com.sofamaniac.crabir.domain.model

data class PagedResponse<T>(
    val data: List<T> = emptyList<T>(),
    val after: String? = null,
    val total: Int = 0
)