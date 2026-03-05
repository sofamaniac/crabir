package com.sofamaniac.reboost.data.remote.dto.post

import com.sofamaniac.reboost.R
import com.sofamaniac.reboost.data.remote.dto.SortInterface

enum class Sort : SortInterface {
    Hot {
        override val representation: Int = R.string.SortHot
    },
    Best {
        override val representation: Int = R.string.SortBest
    },
    New {
        override val representation: Int = R.string.SortNew
    },
    Rising {
        override val representation: Int = R.string.SortRising
    },
    Top {
        override val isTimeframe: Boolean = true
        override val representation: Int = R.string.SortTop
    },
    Controversial {
        override val isTimeframe: Boolean = true
        override val representation: Int = R.string.SortControversial
    };

    override fun toString(): String {
        return super.toString().lowercase()
    }

    override val isTimeframe: Boolean = false
}