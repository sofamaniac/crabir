/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.reddit

import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ListingData<Type>(
    val after: Fullname? = null,
    val dist: Int? = null,
    @SerialName("modhash") val modHash: String? = null,
    val children: List<Type> = emptyList()
)
