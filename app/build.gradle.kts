plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.adsxml"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.adsxml"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)



    implementation("androidx.activity:activity-compose:1.9.1")
    implementation(platform("androidx.compose:compose-bom:2024.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")



    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(project(":admobAds:core"))
    implementation(project(":admobAds:adsMain"))
    implementation(project(":admobAds:appOpen"))
    implementation(project(":admobAds:appUpdate"))
    implementation(project(":admobAds:bannerAds"))
    implementation(project(":admobAds:composeViews"))
    implementation(project(":admobAds:consent"))
    implementation(project(":admobAds:debug"))
    implementation(project(":admobAds:firebaseConfig"))
    implementation(project(":admobAds:interstitials"))
    implementation(project(":admobAds:nativeAds"))
    implementation(project(":admobAds:rewadedAd"))
    implementation(project(":admobAds:supaBase"))
    implementation(project(":admobAds:rewardedInterAds"))
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")
//    implementation("com.ikame.android-sdk:ikm-android-sdk-release:3.0.140-beta")


}