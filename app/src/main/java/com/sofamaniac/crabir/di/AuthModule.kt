package com.sofamaniac.crabir.di

import android.content.Context
import com.sofamaniac.crabir.data.remote.reddit.auth.BasicAuthClient
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.browser.AnyBrowserMatcher
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
class AuthModule {
    @Single
    fun provideAuthorizationService(context: Context): AuthorizationService {
        val appAuthConfig = AppAuthConfiguration.Builder()
            .setBrowserMatcher(AnyBrowserMatcher.INSTANCE)
            .build()
        return AuthorizationService(context, appAuthConfig)
    }

    @Single
    fun provideBasicAuth(): ClientAuthentication {
        return BasicAuthClient
    }
}