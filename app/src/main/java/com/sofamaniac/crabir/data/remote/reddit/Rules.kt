package com.sofamaniac.crabir.data.remote.reddit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rules(
    val rules: List<Rule> = emptyList(),
    @SerialName("site_rules") val siteRules: List<String> = emptyList(),
) {
    fun filter(kind: Kind): Rules {
        return Rules(
            rules.filter { kind == Kind.All || it.kind == kind || it.kind == Kind.All },
            siteRules
        )
    }
}

@Serializable
enum class Kind {
    @SerialName("link")
    Link,

    @SerialName("comment")
    Comment,

    @SerialName("all")
    All,
}

@Serializable
data class Rule(
    val kind: Kind,
    val description: String,
    @SerialName("short_name") val shortName: String,
    @SerialName("violation_reason") val violationReason: String,
    val priority: Int,
)
