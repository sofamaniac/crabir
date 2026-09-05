/*
 * Copyright (c) 2025 Antoine Grimod
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.mikepenz.aboutlibraries) apply false
    alias(libs.plugins.mikepenz.aboutlibraries.android) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    id("com.google.devtools.ksp") version "2.3.2"
    id("tech.mappie.plugin") version "2.4.10-2.4.3" apply true
    id("androidx.room") version "2.8.4" apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.detekt)
}

dependencyAnalysis {
    issues {
        all {
            onAny {
                severity("fail")
            }
        }
    }
}


detekt {
    toolVersion = "2.0.0-alpha.6"
    config.setFrom(rootProject.file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}
