plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android.plugin)
    id("kotlin-kapt")
}

android {
    namespace = "com.nevadev.padeliummarhaba.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // Use a version of Compose compatible with Kotlin 1.8
        kotlinCompilerExtensionVersion = "1.5.1" // Adjust this version as necessary
    }

    packaging {
        resources {
            excludes += listOf(
                "/META-INF/versions/9/OSGI-INF/MANIFEST.MF",  // Exclude specific MANIFEST.MF files
                "/META-INF/AL2.0",
                "/META-INF/LGPL2.1"
            )
        }
    }
}

dependencies {
    implementation (libs.androidx.compose.compiler)
    implementation (libs.androidx.compose.runtime)
    implementation(libs.hilt.android)
    // implementation(project(":data"))
    kapt(libs.hilt.android.compiler)
    implementation (libs.squareup.retrofit)
    implementation (libs.squareup.converter.gson)
}