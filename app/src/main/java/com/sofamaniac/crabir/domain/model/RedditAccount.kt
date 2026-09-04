package com.sofamaniac.crabir.domain.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import net.openid.appauth.AuthState
import java.io.InputStream
import java.io.OutputStream

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
        fun anonymous(): RedditAccount {
            return RedditAccount(-1, null, AuthState())
        }

        fun uninitialized(id: Int, authState: AuthState): RedditAccount {
            return RedditAccount(id, null, authState)
        }
    }

    fun isAnonymous(): Boolean {
        return id == -1
    }

    fun isUninitialized(): Boolean {
        return id == -2
    }

    fun equalsShallow(other: RedditAccount): Boolean {
        return id == other.id && info == other.info && (auth.accessToken == other.auth.accessToken && auth.refreshToken == other.auth.refreshToken)
    }
}

object AccountSerializer : Serializer<RedditAccount> {
    override val defaultValue: RedditAccount
        get() = RedditAccount.anonymous()

    override suspend fun readFrom(input: InputStream): RedditAccount {
        try {
            return Json.decodeFromString<RedditAccount>(
                RedditAccount.serializer(), input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read RedditAccount", serialization)
        }
    }

    override suspend fun writeTo(t: RedditAccount, output: OutputStream) {
        output.write(Json.encodeToString(RedditAccount.serializer(), t).encodeToByteArray())
    }
}