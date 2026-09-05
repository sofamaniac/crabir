package com.sofamaniac.crabir.di

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.data.remote.RandditAPI
import com.sofamaniac.crabir.data.remote.interceptors.CountInterceptor
import com.sofamaniac.crabir.data.remote.interceptors.ForceJsonInterceptor
import com.sofamaniac.crabir.data.remote.interceptors.RateLimitInterceptor
import com.sofamaniac.crabir.data.remote.interceptors.loggingInterceptor
import com.sofamaniac.crabir.data.remote.reddit.InboxAPI
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

private const val BaseUrl = "https://oauth.reddit.com/"
private const val Timeout: Long = 30

@Module(includes = [AccountsModule::class, AuthModule::class])
@ComponentScan
@Configuration
class NetworkModule {

    @Single
    fun provideRedditAuthenticator(
        accountsRepository: AccountsRepository,
        authService: AuthorizationService,
        clientAuth: ClientAuthentication,
        context: Application,
    ): RedditAuthenticator {
        return RedditAuthenticator(
            accountsRepository, authService, clientAuth, context
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
            .connectTimeout(Timeout, TimeUnit.SECONDS)
            .readTimeout(Timeout, TimeUnit.SECONDS)
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

    @Single(binds = [RedditAPIService::class, InboxAPI::class, SubredditAPI::class])
    fun provideRedditApiService(
        okHttpClient: OkHttpClient,
        json: Json,
    ): RedditAPIService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .build()
            .create(RedditAPIService::class.java)
    }

    @Single(binds = [RandditAPI::class])
    fun provideRandditApiService(json: Json): RandditAPI {
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://crabir.com")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .build()
            .create(RandditAPI::class.java)
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
            .baseUrl(BaseUrl)
            .addConverterFactory(
                XML.v1.asConverterFactory("application/xml".toMediaType())
            )
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .client(client).build().create(MediaUploadInterface::class.java)
    }

    @Single
    fun provideStreamableAPI(
        json: Json,
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
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .build()
            .create(StreamableAPI::class.java)
    }
}

