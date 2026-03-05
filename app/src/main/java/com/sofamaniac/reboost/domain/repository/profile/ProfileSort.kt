package com.sofamaniac.reboost.domain.repository.profile

import com.sofamaniac.reboost.domain.repository.profile.ProfileSort.Top


/** Only [Top] accepts a [com.sofamaniac.reboost.data.remote.dto.Timeframe]*/
enum class ProfileSort {
    Top,
    New,
    Hot;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}