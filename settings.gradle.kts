pluginManagement {
    repositories {
        google {

        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MovieFlow"
include(":app")

// core
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":core:firebase")

// features
include(":feature:feed")
include(":feature:search")
include(":feature:profile")
include(":feature:collections")
include(":feature:details")
include(":feature:sync")