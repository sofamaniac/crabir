package com.sofamaniac.crabir.domain.repository.profile

import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.SortInterface


enum class ProfileSort : SortInterface {
    Top {
        override val isTimeframe: Boolean = true
        override val representation: Int = R.string.SortTop
    },
    New {
        override val representation: Int = R.string.SortNew
    },
    Hot {
        override val representation: Int = R.string.SortHot
    };

    override fun toString(): String {
        return super.toString().lowercase()
    }

    override val isTimeframe: Boolean = false

}
