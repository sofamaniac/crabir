package com.sofamaniac.crabir.di

import android.content.Context
import com.sofamaniac.crabir.data.remote.reddit.auth.AuthConfig
import com.sofamaniac.crabir.data.remote.reddit.auth.BasicAuthClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.browser.AnyBrowserMatcher

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthorizationService(@ApplicationContext context: Context): AuthorizationService {
        val appAuthConfig = AppAuthConfiguration.Builder()
            .setBrowserMatcher(AnyBrowserMatcher.INSTANCE)
            .build()
        return AuthorizationService(context, appAuthConfig)
    }

    @Provides
    @Singleton
    fun provideAuthConfig(): AuthConfig {
        return AuthConfig()
    }

    @Provides
    fun provideClientAuth(): ClientAuthentication {
        return BasicAuthClient
    }

}