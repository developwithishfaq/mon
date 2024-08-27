plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
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
    implementation(project(":admobAds:rewardedInterAds"))
//    implementation("com.ikame.android-sdk:ikm-android-sdk-release:3.0.140-beta")


}