import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosX64()
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

android {
    namespace = "dev.piotrprus.particleemitter"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
