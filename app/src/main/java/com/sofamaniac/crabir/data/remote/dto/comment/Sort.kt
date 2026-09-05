/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.dto.comment

import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.SortInterface
import kotlinx.serialization.Serializable

@Serializable
enum class Sort : SortInterface {
    Confidence {
        override val representation: Int = R.string.SortConfidence
    },
    Top {
        override val representation: Int = R.string.SortTop
    },
    Best {
        override val representation: Int = R.string.SortBest
    },
    New {
        override val representation: Int = R.string.SortNew
    },
    Controversial {
        override val representation: Int = R.string.SortControversial
    },
    Old {
        override val representation: Int = R.string.SortOld
    },
    Random {
        override val representation: Int = R.string.SortRandom
    },
    Qa {
        override val representation: Int = R.string.SortQa
    },
    Live {
        override val representation: Int = R.string.SortLive
    };

    override fun toString(): String {
        return super.toString().lowercase()
    }

    override val isTimeframe: Boolean = false
}
