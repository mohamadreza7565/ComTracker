plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.app.comtracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.comtracker"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

   with(libs){
       implementation(androidx.core.ktx)
       implementation(androidx.lifecycle.runtime.ktx)
       implementation(androidx.activity.compose)
       implementation(platform(androidx.compose.bom))
       implementation(androidx.ui)
       implementation(androidx.ui.graphics)
       implementation(androidx.ui.tooling.preview)
       implementation(androidx.material3)
       testImplementation(junit)
       androidTestImplementation(androidx.junit)
       androidTestImplementation(androidx.espresso.core)
       androidTestImplementation(platform(androidx.compose.bom))
       androidTestImplementation(androidx.ui.test.junit4)
       debugImplementation(androidx.ui.tooling)
       debugImplementation(androidx.ui.test.manifest)

       implementation (androidx.work.runtime.ktx)
       implementation (converter.gson)

       ksp(hilt.compiler)
       implementation(hilt.android)
       implementation(hilt.compose)
       implementation(ktor.core)
       implementation(ktor.android)
       implementation(ktor.auth)
       implementation(ktor.logging)
       implementation(ktor.serialization)
       implementation(ktor.content.negotiation)
       implementation(ktor.gson)
       implementation(kotlin.coroutines.core)
       implementation("com.github.samanzamani:PersianDate:1.7.1")

       ksp(room.compiler)
       implementation(room)
       implementation(room.ktx)

       implementation(kotlin.immutable)
   }
}