plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
}

kotlin {
    // Native macOS (Apple silicon), matching the target published by the library.
    macosArm64 {
        binaries.executable {
            entryPoint = "com.example.particleemitter.main"
        }
    }

    sourceSets {
        macosArm64Main.dependencies {
            implementation(project(":shared"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.material3)
        }
    }
}
