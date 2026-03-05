package com.sofamaniac.reboost.data.remote.dto

interface SortInterface {
    val entries: List<SortInterface>
    val isTimeframe: Boolean
    val representation: Int
}