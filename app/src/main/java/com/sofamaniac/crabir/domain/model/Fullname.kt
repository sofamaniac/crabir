package com.sofamaniac.crabir.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
@Parcelize
value class Fullname(val name: String) : Parcelable
