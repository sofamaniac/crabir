package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.openid.appauth.AuthState

object AuthStateSerializer : KSerializer<AuthState> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("AuthState", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AuthState) {
        encoder.encodeString(value.jsonSerializeString())
    }

    override fun deserialize(decoder: Decoder): AuthState {
        return AuthState.jsonDeserialize(decoder.decodeString())
    }
}

@Serializable
data class RedditAccount(
    val id: Int,
    val info: UserDTO?,
    @Serializable(with = AuthStateSerializer::class)
    val auth: AuthState,
) {
    companion object {

        const val ANONYMOUS = "Anonymous"
        const val UNINITIALIZED_ID = -2
        const val ANONYMOUS_ID = -1
        fun anonymous(): RedditAccount {
            return RedditAccount(-1, null, AuthState())
        }

        fun uninitialized(id: Int, authState: AuthState): RedditAccount {
            return RedditAccount(id, null, authState)
        }
    }

    fun isAnonymous(): Boolean {
        return id == ANONYMOUS_ID
    }

    fun isUninitialized(): Boolean {
        return id == UNINITIALIZED_ID
    }

    fun equalsShallow(other: RedditAccount): Boolean {
        val isAuthEqual = auth.accessToken == other.auth.accessToken &&
                auth.refreshToken == other.auth.refreshToken
        return id == other.id && info == other.info && isAuthEqual
    }
}
