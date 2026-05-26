plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "ru.technocracy.movieflow.core.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:firebase"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.workmanager)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}