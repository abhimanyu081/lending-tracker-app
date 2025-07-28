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

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LendingTracker"
include(":app")
include(":core:common")
include(":core:design")
include(":core:data")
include(":core:domain")
include(":feature:auth")
include(":feature:dashboard")
include(":feature:transaction")
include(":feature:person")