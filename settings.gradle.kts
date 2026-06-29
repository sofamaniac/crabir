/*
 * Copyright (c) 2025 Antoine Grimod
 */

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"

    id("com.autonomousapps.build-health") version "3.16.0"
    id("com.android.application") version "9.2.1" apply false
    id("org.jetbrains.kotlin.jvm") version "2.3.10" apply false
    id("org.jetbrains.kotlin.android") version "2.3.10" apply false
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "Crabir"
include(":app")
include(":redditMarkdown")
