plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
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
        //firebase
    //implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))



    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation ("com.google.android.gms:play-services-maps:18.2.0")

    implementation ("com.google.maps.android:maps-compose:2.11.4")
    implementation ("com.google.maps.android:maps-ktx:3.4.0")
    implementation ("com.google.maps.android:maps-utils-ktx:3.4.0")

    // Retrofit for API calls
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")



    //new
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.android.gms:play-services-auth:21.0.1")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.firebase:firebase-auth:22.3.0")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.firebase:firebase-firestore:24.10.0")

    // Material 3 (Jetpack Compose UI)
    implementation ("androidx.compose.material3:material3:1.2.1")

// Jetpack Compose dependencies (basic setup)
    implementation ("androidx.compose.ui:ui:1.6.3")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.3")
    implementation ("androidx.compose.foundation:foundation:1.6.3")
    implementation ("androidx.compose.material:material:1.6.3")
    implementation ("androidx.navigation:navigation-compose:2.7.7")

// Activity and Lifecycle (needed for rememberLauncherForActivityResult)
    implementation ("androidx.activity:activity-compose:1.8.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation ("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("androidx.compose.material:material-icons-extended:1.6.3")
    implementation("io.coil-kt:coil-compose:2.4.0") // latest version

    implementation("androidx.compose.animation:animation:1.4.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")







}