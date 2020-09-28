plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

group = "com.example"
version = "1.0-SNAPSHOT"

android {
    compileSdkVersion(Version.Android.compileSdkVersion)
    defaultConfig {
        applicationId = "com.uooo.simple.example"
        minSdkVersion(Version.Android.minSdkVersion)
        targetSdkVersion(Version.Android.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        getByName("main").res.srcDir("src/main/res-override")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:${Version.coreKtx}")
    implementation("androidx.appcompat:appcompat:${Version.appcompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Version.constraintlayout}")
    implementation("androidx.cardview:cardview:${Version.cardview}")
    implementation("androidx.paging:paging-runtime:${Version.paging}")
    implementation("androidx.paging:paging-rxjava2:${Version.paging}")
    implementation("com.google.android.material:material:${Version.material}")

    implementation("org.koin:koin-android:${Version.koin}")
    implementation("org.koin:koin-androidx-scope:${Version.koin}")
    implementation("org.koin:koin-androidx-viewmodel:${Version.koin}")

    implementation("io.reactivex.rxjava2:rxjava:${Version.rxjava}")
    implementation("io.reactivex.rxjava2:rxandroid:${Version.rxandroid}")
    implementation("com.jakewharton.rxrelay2:rxrelay:${Version.rxrelay}")
    implementation("io.sellmair:disposer:${Version.disposer}")
    implementation("com.jakewharton.rxbinding3:rxbinding:${Version.rxbinding}")
    implementation("com.jakewharton.rxbinding3:rxbinding-core:${Version.rxbinding}")
    implementation("com.jakewharton.rxbinding3:rxbinding-appcompat:${Version.rxbinding}")
    implementation("com.jakewharton.rxbinding3:rxbinding-recyclerview:${Version.rxbinding}")
    implementation("com.jakewharton.rxbinding3:rxbinding-swiperefreshlayout:${Version.rxbinding}")

    implementation("com.github.bumptech.glide:glide:${Version.glide}")
    implementation("com.github.bumptech.glide:okhttp3-integration:${Version.glide}")
    kapt("com.github.bumptech.glide:compiler:${Version.glide}")

    implementation("com.google.android.exoplayer:exoplayer:${Version.exoplayer}")

    implementation("com.airbnb.android:lottie:${Version.lottie}")

    implementation("com.akaita.java:rxjava2-debug:${Version.rxjava2Debug}")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Version.leakcanary}")

    testImplementation("junit:junit:${Version.junit}")
    testImplementation("io.mockk:mockk:${Version.mockk}")
    androidTestImplementation("androidx.test:runner:${Version.testRunner}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Version.espressoCore}")

    implementation("com.github.HaarigerHarald:android-youtubeExtractor:master-SNAPSHOT")

    implementation(project(":shared:data"))
    implementation(project(":shared:domain"))
    implementation(project(":data"))
    implementation(project(":domain"))
}
