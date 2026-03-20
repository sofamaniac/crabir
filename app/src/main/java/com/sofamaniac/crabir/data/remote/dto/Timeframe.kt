/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
enum class Timeframe {
    Hour,
    Day,
    Week,
    Month,
    Year,
    All;

    override fun toString(): String {
        return super.toString().lowercase()
    }
    val representation
        get() = when (this) {
            Hour -> com.sofamaniac.crabir.R.string.hour
            Day -> com.sofamaniac.crabir.R.string.day
            Week -> com.sofamaniac.crabir.R.string.week
            Month -> com.sofamaniac.crabir.R.string.month
            Year -> com.sofamaniac.crabir.R.string.year
            All -> com.sofamaniac.crabir.R.string.all_time
        }
}