package com.sofamaniac.crabir.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.data.remote.interceptors.CountInterceptor
import com.sofamaniac.crabir.data.remote.interceptors.ForceJsonInterceptor
import com.sofamaniac.crabir.data.remote.interceptors.RateLimitInterceptor
import com.sofamaniac.crabir.data.remote.interceptors.loggingInterceptor
import com.sofamaniac.crabir.data.remote.reddit.MediaUploadInterface
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.SubredditAPI
import com.sofamaniac.crabir.data.remote.reddit.auth.RedditAuthenticator
import com.sofamaniac.crabir.data.remote.streamable.StreamableAPI
import com.sofamaniac.crabir.data.remote.utils.URISerializer
import com.sofamaniac.crabir.data.remote.utils.URLSerializer
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import nl.adaptivity.xmlutil.serialization.XML
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.annotation.Singleton
import retrofit2.Retrofit
import java.net.URI
import java.net.URL
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://oauth.reddit.com/"


@Module(includes = [AccountsModule::class, AuthModule::class])
@ComponentScan
@Configuration
class NetworkModule {

    @Single
    fun provideRedditAuthenticator(
        accountsRepository: AccountsRepository,
        authService: AuthorizationService,
        clientAuth: ClientAuthentication,
    ): RedditAuthenticator {
        return RedditAuthenticator(
            accountsRepository, authService, clientAuth
        )
    }

    @Single
    fun provideRateLimiter(): RateLimitInterceptor {
        return RateLimitInterceptor()
    }

    @Single
    fun provideForceJsonInterceptor(): ForceJsonInterceptor {
        return ForceJsonInterceptor()
    }

    @Single
    fun provideOkHttpClient(
        authInterceptor: RedditAuthenticator,
        rateLimitInterceptor: RateLimitInterceptor,
        forceJsonInterceptor: ForceJsonInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(rateLimitInterceptor)
            .addInterceptor(forceJsonInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(CountInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            exceptionsWithDebugInfo = BuildConfig.DEBUG
            isLenient = true
            coerceInputValues = true
            serializersModule = SerializersModule {
                contextual(URL::class, URLSerializer)
                contextual(URI::class, URISerializer)
            }
        }
    }

    @Single
    fun provideRedditApiService(
        okHttpClient: OkHttpClient,
        json: Json
    ): RedditAPIService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(RedditAPIService::class.java)
    }

    @Single
    fun provideSubredditAPIService(
        okHttpClient: OkHttpClient,
        json: Json
    ): SubredditAPI {
        return provideRedditApiService(okHttpClient, json)
    }

    @Single
    fun mediaUploaderService(): MediaUploadInterface {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                XML.v1.asConverterFactory("application/xml".toMediaType())
            )
            .client(client).build().create(MediaUploadInterface::class.java)
    }

    @Single
    fun provideStreamableAPI(
        json: Json
    ): StreamableAPI {
        val contentType = "application/json".toMediaType()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.streamable.com")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(StreamableAPI::class.java)
    }
}

