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
    id("com.google.dagger.hilt.android") version "2.59.2" apply false
    id("tech.mappie.plugin") version "2.3.10-2.4.1" apply true
    id("androidx.room") version "2.8.4" apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
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