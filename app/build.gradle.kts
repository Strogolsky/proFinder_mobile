plugins {
    id("com.android.application") version "8.10.0"
    id("org.jetbrains.kotlin.android") version "1.9.23"
    id("com.google.dagger.hilt.android") version "2.48"
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "cvut.fit.kot"
    compileSdk = 34

    defaultConfig {
        applicationId = "cvut.fit.kot"
        minSdk = 24
        targetSdk = 34
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
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    /* ---------- Compose BOM ---------- */
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))

    /* ---------- core / Kotlin ---------- */
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    /* ---------- Lifecycle ---------- */
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    /* ---------- Compose UI ---------- */
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.runtime:runtime-livedata")

    /* ---------- Navigation ---------- */
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    /* ---------- Images ---------- */
    implementation("io.coil-kt:coil-compose:2.5.0")

    /* ---------- Networking ---------- */
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    /* ---------- Hilt ---------- */
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    /* ---------- Debug / tooling ---------- */
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    /* ---------- Unit / UI tests ---------- */
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    implementation("androidx.datastore:datastore-preferences:1.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

