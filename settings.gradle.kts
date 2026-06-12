rootProject.name = "ParticleEmitter"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":particle-emitter")

include(":presentation")

include(":samples:androidApp")
include(":samples:shared")
include(":samples:desktopApp")
include(":samples:webApp")
