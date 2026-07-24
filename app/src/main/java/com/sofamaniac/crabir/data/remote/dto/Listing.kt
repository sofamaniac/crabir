package com.sofamaniac.crabir.data.remote.dto

import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListingData<Type>(
    val after: Fullname? = null,
    val dist: Int? = null,
    @SerialName("modhash") val modHash: String? = null,
    val children: List<Type> = emptyList(),
)