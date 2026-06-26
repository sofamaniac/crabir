package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.domain.repository.DataInterface

interface CommunityData : DataInterface {
    val displayNamePrefixed: String
    val displayName: String
}