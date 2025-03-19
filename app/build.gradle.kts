plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.place3mapbar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.place3mapbar"
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // Google Maps SDK for Android
    implementation(libs.places)
    implementation(libs.play.services.maps)

// Google maps Compose
    implementation(libs.maps.compose)

    implementation(libs.timber)

    implementation("androidx.navigation:navigation-compose:2.7.5") // Latest Navigation for Compose
    implementation("androidx.compose.material3:material3:1.2.0")   // Material 3 for Bottom Navigation


    implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0") // DataStore for saving login state


        implementation("androidx.compose.material3:material3:1.2.0") // Latest Material 3






}