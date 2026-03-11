package com.sofamaniac.crabir.domain.repository.profile

import com.sofamaniac.crabir.data.remote.dto.Timeframe

data class ProfileFeedParams(val username: String, val sort: ProfileSort, val timeframe: Timeframe?)
