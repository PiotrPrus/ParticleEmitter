import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.maven.publish)
}

kotlin {
    android {
        namespace = "dev.piotrprus.particleemitter"
        compileSdk = 35
        minSdk = 24

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        optimization {
            consumerKeepRules.apply {
                publish = true
                file("consumer-rules.pro")
            }
        }
    }

    jvm()

    iosArm64()
    iosSimulatorArm64()

    macosArm64()

    js {
        browser()
    }

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.animation)
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("io.github.piotrprus", "particle-emitter", "1.0.5")

    pom {
        name.set("ParticleEmitter")
        description.set("Kotlin Multiplatform particle effects library built on Compose Multiplatform. Physics-based animations with Canvas and Compose rendering engines for Android, JVM desktop, and iOS.")
        inceptionYear.set("2024")
        url.set("https://github.com/PiotrPrus/ParticleEmitter/")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("piotrprus")
                name.set("Piotr Prus")
                url.set("https://github.com/PiotrPrus/")
            }
        }

        scm {
            url.set("https://github.com/PiotrPrus/ParticleEmitter/")
            connection.set("scm:git:git://github.com/PiotrPrus/ParticleEmitter.git")
            developerConnection.set("scm:git:ssh://git@github.com/PiotrPrus/ParticleEmitter.git")
        }
    }
}
