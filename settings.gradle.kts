rootProject.name = "ParticleEmitter"

pluginManagement {
    repositories {
        google()
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

include(":particle-emitter")

include(":samples:androidApp")
include(":samples:shared")
include(":samples:desktopApp")
include(":samples:webApp")
