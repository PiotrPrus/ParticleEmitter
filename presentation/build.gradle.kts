import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":particle-emitter"))
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.material3)
    implementation("io.github.alexzhirkevich:qrose:1.0.1")
}

compose.desktop {
    application {
        mainClass = "dev.piotrprus.particleemitter.presentation.MainKt"

        nativeDistributions {
            packageName = "ParticleEmitterTalk"
            packageVersion = "1.0.0"
        }
    }
}
