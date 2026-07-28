/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 11:25 PM
 *
 */

import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.mikepenz.aboutlibraries)
    alias(libs.plugins.mikepenz.aboutlibraries.android)
    alias(libs.plugins.koin.compiler)
    id("com.google.devtools.ksp")
    id("tech.mappie.plugin")
    id("androidx.room")
    id("kotlin-parcelize")
}

android {
    namespace = "com.sofamaniac.crabir"
    androidResources {
        generateLocaleConfig = true
    }
    compileSdk = 37
    defaultConfig {
        applicationId = "com.sofamaniac.crabir"
        // Cannot run on < 26 because of formatElapsedTimeLocalized
        minSdk = 28
        targetSdk = 37
        versionCode = 13
        versionName = "0.5.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.sofamaniac.crabir"

        val keystoreFile = project.rootProject.file("apikeys.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val apiKey = properties.getProperty("REDDIT_CLIENT_ID")!!

        buildConfigField(
            type = "String",
            name = "REDDIT_CLIENT_ID",
            value = apiKey
        )

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    //    compileOptions {
    //        sourceCompatibility = JavaVersion.VERSION_21
    //        targetCompatibility = JavaVersion.VERSION_21
    //    }
    //    kotlinOptions {
    //        jvmTarget = "21"
    //    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/MANIFEST.MF",
                "META-INF/versions/9/OSGI-INF/MANIFEST.MF",
                "META-INF/*.kotlin_module"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    //    kotlinOptions {
    //        jvmTarget = "11"
    //    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

// Avoid duplicate annotations https://stackoverflow.com/a/58909363
configurations {
    all {
        exclude(group = "org.jetbrains", module = "annotations-java5")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-android-extensions-runtime")
    }
}

aboutLibraries {
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    }
}

dependencies {


    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.core.splashscreen)
    //noinspection LoginCredentials
    implementation(libs.appauth)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.coroutines.core)

    // DI
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)


    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.adaptive.layout)
    ksp(libs.kotlin.metadata.jvm)


    implementation(libs.mappie.api)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor) // Optional, for request/response logging

    // Json serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    //implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)

    // Parse HTML-encoded urls
    //implementation(libs.commons.text)

    // More material icons
    implementation(libs.androidx.material.icons.extended)

    // Images
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.zoomable)
    runtimeOnly(libs.coil3.coil.gif)
    // TODO: switch to landscapist once zoomable can transmit single tap event to its parent
    //    implementation(libs.landscapist.coil)
    //    implementation(libs.landscapist.zoomable)
    //    implementation(libs.landscapist.image.gallery)

    // Blur effect
    implementation(libs.haze)
    implementation(libs.haze.blur)
    implementation(libs.haze.blur.materials)

    // Video player
    implementation(libs.androidx.media3.exoplayer)
    runtimeOnly(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui.compose.material3)

    // Rooms
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Render markdown
    implementation(libs.multiplatform.markdown.renderer)
    implementation(libs.multiplatform.markdown.renderer.m3)
    implementation(libs.multiplatform.markdown.renderer.coil3)

    // Parse html
    implementation(libs.ksoup)

    // License page
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose.core)
    implementation(libs.aboutlibraries.compose.m3)

    // Support for XML serialization / deserialization
    implementation(libs.xml.serialization)

    implementation(project(":redditMarkdown"))
}