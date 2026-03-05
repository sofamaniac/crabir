package com.sofamaniac.reboost.domain.repository.profile

import com.sofamaniac.reboost.data.remote.dto.Timeframe

data class ProfileFeedParams(val username: String, val sort: ProfileSort, val timeframe: Timeframe?)
